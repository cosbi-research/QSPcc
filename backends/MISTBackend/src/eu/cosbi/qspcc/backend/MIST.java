package eu.cosbi.qspcc.backend;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.backend.errors.MISTErrorMessage;
import eu.cosbi.qspcc.dag.DAGFunctionalListener;
import eu.cosbi.qspcc.dag.DAGListener;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.expressions.type.DimensionType;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.interfaces.CompilerBackend;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.interfaces.annotations.BackendModule;
import eu.cosbi.qspcc.interfaces.annotations.CliOption;
import eu.cosbi.qspcc.interfaces.annotations.CliOptions;

@BackendModule(name = "mist")
@CliOptions(@CliOption(opt = MIST.TEMPLATE_OPTION, longOpt = "template", hasArg = true, description = "Mist is a C formatting guideline. A <TEMPLATE> formatting folder is provided as input. Please check the docs folder for info on the stylistic guidelines.", argName = "TEMPLATE-FOLDER", required = false))
public class MIST extends CompilerBackend implements DAGListener<AAST, AASTNode, String> {
    private Logger logger = LogManager.getLogger(MIST.class);
    public static final String TEMPLATE_OPTION = "mist";
    static Pattern tag_re = Pattern.compile("<(\\/?\\w+:?\\w*)>");
    static Pattern if_re = Pattern.compile("(?m)^if\\s*\\(?(.+)\\)?\\s*$");
    static Pattern elseif_re = Pattern.compile("(?m)^elseif\\s*\\(?(.+)\\)?\\s*$");
    static Pattern else_re = Pattern.compile("(?m)^\\s*else\\s*$");
    static Pattern end_re = Pattern.compile("(?m)^\\s*end\\s*$");

    // shared among different files
    static List<String> otherParams = new ArrayList<String>();
    // used as global variables translated as Ith(y, name) in fluxes
    static List<String> speciesNames = new ArrayList<String>();
    // list of variables defined inside fluxes tag
    static Set<String> variables = new HashSet<String>();
    static String initConditionColumnName = null;
    static String parametersColumnName = null;
    // key : name of folder that should contain this fluxes.h, value: string to
    // be flushed
    static SortedMap<String, StringBuffer> fluxesSection = new TreeMap<String, StringBuffer>();
    // key : name of folder that should contain this odes.h, value: string to
    // be flushed
    static SortedMap<String, StringBuffer> odesSection = new TreeMap<String, StringBuffer>();

    public MIST(IFunction[] coreFunctions, String mainSourcePath, String destFolder, String inputFile, Integer indent,
	    Map<String, List<String>> backendSpecificOptions, boolean dockerized) {
	super(coreFunctions, mainSourcePath, destFolder, inputFile, indent, backendSpecificOptions, dockerized);
    }

    @Override
    public String extension() {
	return "c";
    }

    @Override
    public void prepareOutput() {
    }

    @Override
    public void finalizeOutput() {
    }

    @Override
    public void onTranslationStart(Program p) throws Exception {
	// search only FLUXES VARIABLES (fill fluxesVariables set)
	p.mainCompilationUnit().walkOnExit(new DAGFunctionalListener<Program, AAST>() {
	    @Override
	    public void on(Program p, AAST aast) throws GException {
		// WALK ON FILES OF THE PROGRAM (exit order)
		aast.rootNode().walkOnEnter(new DAGFunctionalListener<AAST, AASTNode>() {
		    boolean fluxSectionOpened = false;
		    boolean odesSectionOpened = false;

		    @Override
		    public void on(AAST aast, AASTNode node) throws GException {
			// WALK ON AAST NODES of each file of the program
			// (enter
			// order)
			Tag curTag = null;
			if (node.type().equals(NodeType.LINECOMMENT)) {
			    Matcher m = tag_re.matcher(node.code());
			    if (m.find()) {
				String tagName = m.group(1);
				// if we found a tag, is closing or opening?
				boolean opening = true;
				if (tagName.startsWith("/")) {
				    // remove /
				    tagName = tagName.substring(1, tagName.length());
				    opening = false;
				    fluxSectionOpened = false;
				    odesSectionOpened = false;
				}
				if (opening) {
				    curTag = Tag.fromString(tagName);
				    if (curTag == null)
					return;
				    fluxSectionOpened = curTag.isFlux();
				    odesSectionOpened = curTag.isODE();
				    if (fluxSectionOpened)
					if (curTag.equals(Tag.FLUXES))
					    fluxesSection.put("", new StringBuffer());
					else {
					    String variant = curTag.variant(tagName);
					    if (variant != null && !fluxesSection.containsKey(variant))
						fluxesSection.put(variant, new StringBuffer());
					}
				    else if (odesSectionOpened)
					if (curTag.equals(Tag.ODES))
					    odesSection.put("", new StringBuffer());
					else {
					    String variant = curTag.variant(tagName);
					    if (variant != null && !odesSection.containsKey(variant))
						odesSection.put(variant, new StringBuffer());
					}

				}
			    }
			} else if (fluxSectionOpened
				&& (node.type().equals(NodeType.ASSIGN) || node.type().equals(NodeType.GASSIGN))) {
			    // grab all the variables
			    // needed because we need to know the variables
			    // before actually generating the mist
			    // translation
			    AASTNode lhsID = node.child(NodeType.LHS).nestedChild(NodeType.ID);
			    if (lhsID == null)
				return;
			    variables.add(lhsID.code());

			}
		    }
		}, false);
	    }
	}, false);
    }

    @Override
    public void genCode(AAST program) throws TypeException, UnboundException, SyntaxException {
    }

    @Override
    public void onWalkStarted(AAST ref) throws GException {
	logger.debug("### Evaluating file: " + ref.sourcePath() + " ###");
    }

    @Override
    public void onWalkCompleted(AAST ref) throws GException {
	logger.debug("### File: " + ref.sourcePath() + " evaluated. ###");
    }

    @Override
    public void translate(AAST aast, boolean stopOnError) throws GException {
	// actually look for tags in source language
	aast.rootNode().walkFor(this, stopOnError);
    }

    @Override
    public void onEnter(AAST aast, AASTNode node) throws GException {
	if (node.type().equals(NodeType.LINECOMMENT)) {
	    Matcher m = tag_re.matcher(node.code());
	    if (m.find()) {
		String tagName = m.group(1);
		// if we found a tag, is closing or opening?
		boolean opening = true;
		if (tagName.startsWith("/")) {
		    // remove /
		    tagName = tagName.substring(1, tagName.length());
		    opening = false;
		}
		Tag curTag = Tag.fromString(tagName);
		if (curTag == null) {
		    logger.warn("Found unsupported tag '" + tagName + "' skipping.");
		    return;
		}
		Tag.open(curTag, tagName, opening);
	    }
	} else if (Tag.opened() != null)
	    processStartTagContent(aast, node);
	// skip the rest
    }

    @Override
    public void onExit(AAST ref, AASTNode curNode, Deque<String> results) throws GException {
    }

    private void processStartTagContent(AAST aast, AASTNode node) throws GException {
	Tag curTag = Tag.opened();
	switch (curTag) {
	case FLUXES:
	case FLUXES_SECTION:
	    // for generic fluxes
	    String fluxesFolderName = "";
	    if (curTag.equals(Tag.FLUXES_SECTION))
		fluxesFolderName = curTag.variant();

	    StringBuffer sb = fluxesSection.get(fluxesFolderName);
	    if (sb == null)
		sb = new StringBuffer();

	    switch (node.type()) {
	    case LINECOMMENT:
		sb.append("/* ").append(node.code().replace("%", "")).append(" */").append(NL);
		break;
	    case IF:
	    case WHILE:
		String conditionalTranslation = matlab2MIST(node);
		sb.append(conditionalTranslation).append(NL);
		break;
	    case EXPR_STMT:
		String stmtTranslation = matlab2MIST(node);
		sb.append(stmtTranslation).append(";").append(NL);
		break;
	    case GASSIGN:
	    case ASSIGN:
		if (node.parentExists(NodeType.IF) || node.parentExists(NodeType.WHILE))
		    return; // already handled in IF node
		AASTNode lhsID = node.child(NodeType.LHS).nestedChild(NodeType.ID);
		if (lhsID == null)
		    throw new UndefinedTranslationException(MISTErrorMessage.UNSUPPORTED_TAG_ASSIGNMENT, node, curTag);
		AASTNode rhs = node.child(NodeType.RHS);
		String rhsTranslation = matlab2MIST(rhs);
		sb.append(lhsID.code()).append(" = ").append(rhsTranslation).append(";").append(NL);
	    }

	    break;
	case ODES:
	case ODES_SECTION:
	    // for generic fluxes
	    String odesFolderName = "";
	    if (curTag.equals(Tag.ODES_SECTION))
		odesFolderName = curTag.variant();

	    sb = odesSection.get(odesFolderName);
	    if (sb == null)
		sb = new StringBuffer();

	    switch (node.type()) {
	    case LINECOMMENT:
		sb.append("/* ").append(node.code().replace("%", "")).append(" */").append(NL);
		break;
	    case GASSIGN:
	    case ASSIGN:
		if (node.parentExists(NodeType.IF) || node.parentExists(NodeType.WHILE))
		    return; // already handled in IF node
		AASTNode lhsApply = node.child(NodeType.LHS).nestedChild(NodeType.APPLY);
		if (lhsApply == null)
		    throw new UndefinedTranslationException(MISTErrorMessage.UNSUPPORTED_ODE_ASSIGNMENT, node);
		AASTNode rhs = node.child(NodeType.RHS);
		String rhsTranslation = matlab2MIST(rhs);
		String lhsTranslation = "Ith(ydot, " + lhsApply.child(NodeType.FUNCTION_PARAMETER_LIST).code() + ")";
		sb.append(lhsTranslation).append(" = ").append(rhsTranslation).append(";").append(NL);
	    }
	    break;
	case INIT_CONDITION_NAMES:
	    // here we are interested to the first LHS of assignments
	    if (node.type().equals(NodeType.ID) && node.parentExists(NodeType.LHS, 2)
		    && initConditionColumnName == null)
		initConditionColumnName = node.code();
	    break;
	case PARAM_NAME:
	    // here we are interested to the first LHS of assignments
	    if (node.type().equals(NodeType.ID) && node.parentExists(NodeType.LHS, 2) && parametersColumnName == null)
		parametersColumnName = node.code();
	    break;
	case SPECIES_NAMES:
	case OTHER_PARAMS:
	    List<String> list;
	    switch (curTag) {
	    case SPECIES_NAMES:
		list = speciesNames;
		break;
	    case OTHER_PARAMS:
		list = otherParams;
		break;
	    default:
		throw new UndefinedTranslationException(MISTErrorMessage.CODE_UNSUPPORTED_TAG, node, curTag);
	    }
	    // here we are interested to LHS of assignments
	    if (node.type().equals(NodeType.ID) && node.parentExists(NodeType.LHS, 2)) {
		AASTNode assignNode = node.parent(NodeType.ASSIGN);
		GType etype = node.expr();
		if (etype.isCastableToScalar())
		    list.add(node.code());
		else if (etype.isCastableToMatrix()) {
		    DimensionType dtype = (DimensionType) etype;
		    IntType[] dims = dtype.dims();
		    int totalDim = 1;
		    for (IntType dim : dims) {
			if (!dim.hasValue())
			    throw new UndefinedTranslationException(MISTErrorMessage.MATRIX_DIMENSION_UNKNOWN, node,
				    curTag, assignNode.code());
			else
			    totalDim *= dim.valueAsInt();
		    }
		    for (int i = 1; i <= totalDim; ++i)
			list.add(node.code() + "_" + String.format("%02d", i));
		} else
		    throw new UndefinedTranslationException(MISTErrorMessage.ASSIGNMENT_TYPE_UNSUPPORTED, node, curTag,
			    assignNode.code(), etype);
	    }
	    break;
	}

    }

    private String matlab2MIST(AASTNode curRoot) throws GException {
	final String code = curRoot.code();
	List<String> newCode = new ArrayList<String>(1);
	newCode.add(code);
	// find variables used in this block
	curRoot.walkOnEnter(new DAGFunctionalListener<AAST, AASTNode>() {
	    boolean eltimesreplaced = false;
	    boolean eldivreplaced = false;

	    @Override
	    public void on(AAST aast, AASTNode node) throws GException {
		if (node.type().equals(NodeType.LINECOMMENT)) {
		    newCode.set(0, newCode.get(0).replace(node.code(), "/* " + node.code().replace("%", "") + " */"));
		} else if (node.type().equals(NodeType.ID) && !node.parent().equals(NodeType.APPLY)) {
		    String variable = node.code();

		    if (!variables.contains(variable) && !speciesNames.contains(variable)) {
			// it should be wrapped with UP
			newCode.set(0, newCode.get(0).replaceFirst("(?<!UP\\()" + variable,
				Matcher.quoteReplacement("UP(" + variable + ")")));
		    }
		} else if (node.type().equals(NodeType.ID) && node.parent().equals(NodeType.APPLY)) {
		    AASTNode applyNode = node.parent();
		    String applyCode = node.code();
		    // skip functions
		    if (aast.functionSymbolExists(node))
			return;
		    List<AASTNode> params = applyNode.childs(NodeType.FUNCTION_PARAMETER_LIST);
		    // if in species name
		    String paramStr = applyNode.child(NodeType.FUNCTION_PARAMETER_LIST).code();
		    if (speciesNames.contains(paramStr)) {
			// it should be wrapped in Ith(...)
			String toBeReplaced = applyNode.code();
			newCode.set(0,
				newCode.get(0).replaceFirst(Pattern.quote(toBeReplaced), "Ith(y, " + paramStr + ")"));
			return;
		    }

		    if (params.size() != 1)
			throw new UndefinedTranslationException(MISTErrorMessage.MULTIPLE_PARAMETERS_UNSUPPORTED,
				applyNode);
		    AASTNode param = params.get(0);
		    if (expr(param) == null || !expr(param).equals(BType.INT))
			throw new UndefinedTranslationException(MISTErrorMessage.NON_INT_PARAMETER_UNSUPPORTED,
				applyNode);

		    IntType ptype = (IntType) expr(param);
		    if (!ptype.hasValue())
			throw new UndefinedTranslationException(
				MISTErrorMessage.UNKNOWN_INT_PARAMETER_VALUE_UNSUPPORTED, applyNode);

		    newCode.set(0,
			    newCode.get(0).replaceFirst(Pattern.quote(applyNode.code()), Matcher.quoteReplacement(
				    "UP(" + applyCode + "_" + String.format("%02d", ptype.valueAsInt()) + ")")));
		} else if (!eltimesreplaced && node.type().equals(NodeType.ELEMENTWISE_TIMES)) {
		    newCode.set(0, newCode.get(0).replace(".*", "*"));
		    eltimesreplaced = true;
		} else if (!eldivreplaced && node.type().equals(NodeType.ELEMENTWISE_LEFTDIV)) {
		    newCode.set(0, newCode.get(0).replace("./", "*"));
		    eldivreplaced = true;
		} else if (node.type().equals(NodeType.EXP) || node.type().equals(NodeType.ELEMENTWISE_EXP)) {
		    // translate LHS, translate RHS, then join using pow C
		    // function
		    String lhs = node.childs().get(0).code();
		    String rhs = node.childs().get(1).code();
		    String originalCode = node.code();
		    newCode.set(0, newCode.get(0).replaceFirst(Pattern.quote(originalCode),
			    Matcher.quoteReplacement("pow(" + lhs + ", " + rhs + ")")));
		} else if (node.type().equals(NodeType.IF)) {
		    // match if
		    Matcher m = if_re.matcher(newCode.get(0));
		    if (m.find()) {
			String tmp = newCode.get(0);
			String expression = node.child(NodeType.EXPRESSION).code();
			newCode.set(0, tmp.substring(0, m.start()) + "if(" + expression + "){"
				+ tmp.substring(m.end(), tmp.length()));
		    }
		    // match eventual elseif
		    m = elseif_re.matcher(newCode.get(0));
		    List<AASTNode> elseif = new ArrayList<AASTNode>();
		    for (AASTNode child : node.childs())
			if (child.type().equals(NodeType.ELSEIF))
			    elseif.add(child);
		    int i = 0;
		    while (m.find()) {
			AASTNode elseifchild = elseif.get(i);
			String tmp = newCode.get(0);
			String expression = elseifchild.child(NodeType.EXPRESSION).code();
			newCode.set(0, tmp.substring(0, m.start()) + "}else if(" + expression + "){"
				+ tmp.substring(m.end(), tmp.length()));
			// rematch needed newcode changed
			m = elseif_re.matcher(newCode.get(0));
			i++;
		    }
		    // match else
		    m = else_re.matcher(newCode.get(0));
		    if (m.find()) {
			String tmp = newCode.get(0);
			newCode.set(0, tmp.substring(0, m.start()) + "}else{" + tmp.substring(m.end(), tmp.length()));
		    }
		    // match end
		    m = end_re.matcher(newCode.get(0));
		    if (m.find()) {
			String tmp = newCode.get(0);
			newCode.set(0, tmp.substring(0, m.start()) + "}" + tmp.substring(m.end(), tmp.length()));
		    }
		}
	    }
	}, false);

	String operatorsFreeCode = newCode.get(0);
	return operatorsFreeCode;
    }

    @Override
    public void onTranslationEnd(Program program, List<String> fun_names) throws Exception {
	try {
	    String programName = program.mainCompilationUnit().name();
	    Path destpath = Paths.get(destFolder);
	    Path templatepath = null;
	    List<String> templatevalues = backendSpecificOptions.get(TEMPLATE_OPTION);
	    if (templatevalues != null && !templatevalues.isEmpty())
		templatepath = Paths.get(templatevalues.get(0));

	    // write indices
	    InputStream fStream = null;
	    if (templatepath == null) {
		fStream = MIST.class.getClassLoader().getResourceAsStream("resources/indices.h");
		if (fStream == null)
		    fStream = new FileInputStream(Paths.get("backends/MISTBackend/resources").toAbsolutePath()
			    .resolve("indices.h").toString());
	    } else
		fStream = new FileInputStream(templatepath.resolve("indices.h").toString());

	    String indices = IOUtils.toString(fStream, "UTF-8");
	    // actually substitute tags
	    indices = indices.replaceAll("\\/.*<" + Tag.N_SPECIES_NAMES + ">.*\\/",
		    Integer.toString(speciesNames.size()));
	    indices = indices.replaceAll("(?m)^.*<" + Tag.SPECIES_NAMES + ">.*$", String.join("," + NL, speciesNames));
	    indices = indices.replaceAll("\\/.*<" + Tag.N_OTHER_PARAMS + ">.*\\/",
		    Integer.toString(otherParams.size()));
	    // substitute in indexes.h
	    if (parametersColumnName != null)
		indices = indices.replaceAll("\\/[^/]*<" + Tag.PARAM_NAME + ">[^/]*\\/", parametersColumnName);
	    else
		logger.warn("Missing tag '" + Tag.PARAM_NAME + "' in program '" + programName + "' Skipping.");
	    indices = indices.replaceAll("(?m)^.*<" + Tag.OTHER_PARAMS + ">.*$", String.join("," + NL, otherParams));

	    // write it
	    BufferedWriter mkwriter = Files.newBufferedWriter(destpath.resolve("indices.h"));
	    mkwriter.write(indices);
	    mkwriter.close();

	    // write parameters
	    if (templatepath == null) {
		fStream = MIST.class.getClassLoader().getResourceAsStream("resources/parameters.h");
		if (fStream == null)
		    fStream = new FileInputStream(Paths.get("backends/MISTBackend/resources").toAbsolutePath()
			    .resolve("parameters.h").toString());
	    } else
		fStream = new FileInputStream(templatepath.resolve("parameters.h").toString());

	    String parameters = IOUtils.toString(fStream, "UTF-8");
	    // substitute in parameters.h
	    if (parametersColumnName != null)
		parameters = parameters.replaceAll("\\/[^/]*<" + Tag.PARAM_NAME + ">[^/]*\\/", parametersColumnName);
	    if (initConditionColumnName != null)
		parameters = parameters.replaceAll("\\/[^/]*?<" + Tag.INIT_CONDITION_NAMES + ">[^/]*?\\/",
			initConditionColumnName);
	    else
		logger.warn(
			"Missing tag '" + Tag.INIT_CONDITION_NAMES + "' in program '" + programName + "' Skipping.");

	    // write it
	    mkwriter = Files.newBufferedWriter(destpath.resolve("parameters.h"));
	    mkwriter.write(parameters);
	    mkwriter.close();

	    // write fluxes
	    if (templatepath == null) {
		fStream = MIST.class.getClassLoader().getResourceAsStream("resources/fluxes.h");
		if (fStream == null)
		    fStream = new FileInputStream(Paths.get("backends/MISTBackend/resources").toAbsolutePath()
			    .resolve("fluxes.h").toString());
	    } else
		fStream = new FileInputStream(templatepath.resolve("fluxes.h").toString());

	    String fluxes = IOUtils.toString(fStream, "UTF-8");
	    // actually substitute tags
	    StringBuffer sb = fluxesSection.get("");
	    if (sb == null)
		sb = new StringBuffer();

	    // include definition of variables
	    sb.append("/* VARIABLES DEFINITION */").append(NL);
	    for (String var : variables)
		sb.append("realtype ").append(var).append(";").append(NL);
	    sb.append(NL);
	    sb.append("/* FLUX COMPARTMENTS */").append(NL);
	    // fluxes sections
	    for (Map.Entry<String, StringBuffer> entry : fluxesSection.entrySet()) {
		if (entry.getKey().equals(""))
		    continue;
		// create directory for fluxes section
		Path fluxSectionPath = Paths.get(destFolder, entry.getKey());
		Files.createDirectories(fluxSectionPath);
		// add include to general fluxes header
		sb.append("#include \"" + entry.getKey() + "/fluxes.h\"").append(NL);

		// serialize custom section flux
		// __MODEL_FLUXES_H__
		String fluxSection = new String(fluxes);
		fluxSection = fluxSection.replace("__MODEL_FLUXES_H__",
			"__MODEL_" + entry.getKey().toUpperCase() + "_FLUXES_H__");
		fluxSection = fluxSection.replaceAll("(?m)^.*<" + Tag.FLUXES + ">.*$", entry.getValue().toString());
		// write it
		mkwriter = Files.newBufferedWriter(fluxSectionPath.resolve("fluxes.h"));
		mkwriter.write(fluxSection);
		mkwriter.close();
	    }
	    fluxes = fluxes.replaceAll("(?m)^.*<" + Tag.FLUXES + ">.*$", sb.toString());

	    // write it
	    mkwriter = Files.newBufferedWriter(destpath.resolve("fluxes.h"));
	    mkwriter.write(fluxes);
	    mkwriter.close();

	    // write odes
	    if (templatepath == null) {
		fStream = MIST.class.getClassLoader().getResourceAsStream("resources/odes.h");
		if (fStream == null)
		    fStream = new FileInputStream(
			    Paths.get("backends/MISTBackend/resources").toAbsolutePath().resolve("odes.h").toString());
	    } else
		fStream = new FileInputStream(templatepath.resolve("odes.h").toString());

	    String odes = IOUtils.toString(fStream, "UTF-8");
	    // actually substitute tags
	    sb = odesSection.get("");
	    if (sb == null)
		sb = new StringBuffer();

	    // odes sections
	    for (Map.Entry<String, StringBuffer> entry : odesSection.entrySet()) {
		if (entry.getKey().equals(""))
		    continue;
		// create directory for fluxes section
		Path fluxSectionPath = Paths.get(destFolder, entry.getKey());
		Files.createDirectories(fluxSectionPath);
		// add include to general fluxes header
		sb.append("#include \"" + entry.getKey() + "/odes.h\"").append(NL);

		// __MODEL_ODES_H__
		String odeSection = new String(odes);
		odeSection = odeSection.replace("__MODEL_ODES_H__",
			"__MODEL_" + entry.getKey().toUpperCase() + "_ODES_H__");
		odeSection = odeSection.replaceAll("(?m)^.*<" + Tag.ODES + ">.*$", entry.getValue().toString());
		// write it
		mkwriter = Files.newBufferedWriter(fluxSectionPath.resolve("odes.h"));
		mkwriter.write(odeSection);
		mkwriter.close();
	    }
	    odes = odes.replaceAll("(?m)^.*<" + Tag.ODES + ">.*$", sb.toString());

	    // write it
	    mkwriter = Files.newBufferedWriter(destpath.resolve("odes.h"));
	    mkwriter.write(odes);
	    mkwriter.close();

	} catch (IOException e) {
	    logger.error("Unrecoverable error writing MIST templates: " + e.getMessage());
	    logger.debug("Unrecoverable error writing MIST templates: " + e.getMessage(), e);
	}

    }

    protected GType expr(AASTNode node) {
	GType exprResult = node.expr();
	if (exprResult == null && node.childs().size() == 1) {
	    return expr(node.childs().get(0));
	} else {
	    return exprResult;
	}
    }

}

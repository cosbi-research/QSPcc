package eu.cosbi.qspcc.backend;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Matcher;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import eu.cosbi.qspcc.ast.AAST;
import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.NodeType;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.ast.attrs.NodeAttr;
import eu.cosbi.qspcc.backend.attributes.CAttr;
import eu.cosbi.qspcc.backend.errors.CErrorMessage;
import eu.cosbi.qspcc.backend.interfaces.Function;
import eu.cosbi.qspcc.backend.interfaces.MainCode;
import eu.cosbi.qspcc.backend.utils.CsvUtils;
import eu.cosbi.qspcc.backend.utils.StoredCsv;
import eu.cosbi.qspcc.backend.utils.VarVisibility;
import eu.cosbi.qspcc.dag.DAGListener;
import eu.cosbi.qspcc.exceptions.CastException;
import eu.cosbi.qspcc.exceptions.ErrorMessage;
import eu.cosbi.qspcc.exceptions.GException;
import eu.cosbi.qspcc.exceptions.InputException;
import eu.cosbi.qspcc.exceptions.MissingNodeException;
import eu.cosbi.qspcc.exceptions.OutputException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.exceptions.TypeException;
import eu.cosbi.qspcc.exceptions.UnboundException;
import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;
import eu.cosbi.qspcc.exceptions.UnfinishedTranslationException;
import eu.cosbi.qspcc.expressions.type.BoolType;
import eu.cosbi.qspcc.expressions.type.DimensionType;
import eu.cosbi.qspcc.expressions.type.FunctionType;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.IndexType;
import eu.cosbi.qspcc.expressions.type.IntType;
import eu.cosbi.qspcc.expressions.type.MatrixType;
import eu.cosbi.qspcc.expressions.type.ScalarType;
import eu.cosbi.qspcc.expressions.type.SliceType;
import eu.cosbi.qspcc.expressions.type.StringType;
import eu.cosbi.qspcc.expressions.type.StructType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.qspcc.expressions.type.ValuedType;
import eu.cosbi.qspcc.expressions.type.interfaces.DimsType;
import eu.cosbi.qspcc.interfaces.CompilerBackend;
import eu.cosbi.qspcc.interfaces.CompilerFrontend;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;
import eu.cosbi.qspcc.interfaces.annotations.BackendModule;
import eu.cosbi.qspcc.interfaces.annotations.CliOption;
import eu.cosbi.qspcc.interfaces.annotations.CliOptions;
import eu.cosbi.qspcc.interfaces.annotations.StepType;
import eu.cosbi.qspcc.symbols.StructSymbols;
import eu.cosbi.qspcc.symbols.Symbols;
import eu.cosbi.qspcc.symbols.type.SymbolType;
import eu.cosbi.utils.Constants;
import eu.cosbi.utils.Quadruple;
import eu.cosbi.utils.Quintuple;
import eu.cosbi.utils.Triple;
import eu.cosbi.utils.Tuple;
import eu.cosbi.utils.TypeUtils;

@BackendModule(name = "c")
@CliOptions({
		@CliOption(opt = C.SUN_VER_OPTION, longOpt = "sundials-version", hasArg = true, description = "Sundials major version to be used by the translated program. Supported values are 2,3,4,5,6. Default is '6'", argName = "VERSION", required = false),
		@CliOption(opt = C.SUN_INCLUDE, longOpt = "sundials-include-path", hasArg = true, description = "The include path for the local installation of the Sundials library. default: /usr/local/include", argName = "INCLUDE-PATH", required = false),
		@CliOption(opt = C.SUN_LIB, longOpt = "sundials-lib-path", hasArg = true, description = "The path to the library files (.so, .a, .dynlib) for the local installation of the Sundials library. default: /usr/local/lib", argName = "LIB-PATH", required = false),
		@CliOption(opt = C.STD_LIB, longOpt = "std-lib-path", hasArg = true, description = "The path to the library files (.so, .a, .dynlib) for zlib and other standard libraries. default: /usr/lib", argName = "LIB-PATH", required = false),
		@CliOption(opt = C.ENABLE_PREALLOCATED_MEMORY_CHECK, longOpt = "preallocated-memory-check", hasArg = false, description = "By default no memory checks are performed for variables preallocated with functions such as eye,ones,nan,zeroes (faster). Enable this flag (slower) if you get memory access errors and want to check the variables' memory.", argName = "", required = false),
		@CliOption(opt = C.NONSYMM_SPARSE_LINEAR_PROBLEM, longOpt = "asym-sparse-matrix", hasArg = true, description = "Assume a sparse matrix structure. Accepts the following parameter values: REAL_SYMM for real symmetric matrices, REAL_SYMM_POSITIVE_DEF for real symmetric positive-definite matrices, REAL_NOSYMM for real non-symmetric matrices. By default REAL_NOSYMM is assumed.", argName = "", required = false),
		@CliOption(opt = C.STORE_INPUT, longOpt = "store-input", hasArg = false, description = "Makes the CSV file contents hard-coded in the output program at compile time (faster). To read CSV files everytime at runtime (slower) do not specify this flag.", argName = "", required = false),
		@CliOption(opt = C.TCMALLOC, longOpt = "tcmalloc-path", hasArg = true, description = "Faster version of malloc, library path. default: /usr/local/tcmalloc/lib", argName = "LIB-PATH", required = false),
		@CliOption(opt = C.TCMALLOCINCLUDE, longOpt = "tcmalloc-include-path", hasArg = true, description = "Faster version of malloc, include path. default: /usr/local/tcmalloc/include", argName = "INCLUDE-PATH", required = false) })
public class C extends CompilerBackend implements DAGListener<AAST, AASTNode, String>, MainCode {
	private Logger logger = LogManager.getLogger(C.class);
	public static final String SUN_VER_OPTION = "sunver";
	public static String defaultSunVer = "6";
	public static final String SUN_LIB = "sunlib";
	public static Path defaultSunLibPath = Paths.get("/usr", "local", "sundials-6.2.0", "lib");
	public static final String SUN_INCLUDE = "suninclude";
	public static Path defaultSunPath = Paths.get("/usr", "local", "sundials-6.2.0", "include");
	public static final String STD_LIB = "stdlib";
	public static Path defaultStdLibPath = Paths.get("/usr", "lib");
	public static final String NONSYMM_SPARSE_LINEAR_PROBLEM = "slm";
	public static String defaultUseSparseLinear = "REAL_NOSYMM";
	public static final String STORE_INPUT = "si";
	public static Boolean defaultStoreInput = false;
	public static final String TCMALLOC = "tcmalloclib";
	public static Path defaultTCMallocPath = Paths.get("/usr", "local", "tcmalloc", "lib");
	public static final String TCMALLOCINCLUDE = "tcmallocinclude";
	public static Path defaultTCMallocInclude = Paths.get("/usr", "local", "tcmalloc", "include");
	public static final String ENABLE_PREALLOCATED_MEMORY_CHECK = "prealloccheck";
	public static Boolean defaultEnablePreallocatedCheck = false;

	public final static String OBJECTS_FOLDER_NAME = "objs";
	protected static final String TIC_GLOBAL_VARIABLE = "__tic";
	protected static final String TOC_GLOBAL_VARIABLE = "__toc";
	protected static final String INPUT_ID_STRUCT = "INPUT_COLUMN";
	protected static final String QSPCC_DIRECTIVE = "@qspcc";
	public static final String CONDITION_DOUBLE_ZERO = "1e-10";

	protected static final String TYPE_PLACEHOLDER = "#PREFIX#";
	protected static final String DIMENSION_PLACEHOLDER = "#DIMENSION#";
	protected static final String ENSURE_CAPACITY_FUN_GENERIC_NAME = "v" + TYPE_PLACEHOLDER + "Ensure"
			+ DIMENSION_PLACEHOLDER + "DCapacity";
	protected static final int ENSURE_CAPACITY_MEMORY_EXTRASTORAGE = 100;

	static Boolean enablePreallocatedCheck = defaultEnablePreallocatedCheck;
	static String sparseMatrixType = defaultUseSparseLinear;
	// flag that tells whether is the case of storing the loaded csv
	// in memory
	private static Boolean storeInput = defaultStoreInput;

	// to be moved to middle-end optimizer when it will exist
	// global structures
	// globalStructs variable is non-empty only on main block
	// and collects information on structures defined in the whole program
	protected Map<String, Triple<GType, SymbolType, List<String>>> globalStructs;

	// true if base structure for this undefined struct variable already exists
	List<String> baseStructAlreadyExists;

	protected static BufferedWriter vwriter, hwriter, ussunwriter, ushwriter, uscwriter, storedcsvhwriter,
			storedcsvcwriter;

	protected boolean tic_defined, toc_defined;

	// used to give a name to the csv struct that store the parameters
	// set in a user-defined csv"
	public static final String INPUT_TYPE = "INPUT";
	public static final String SLICE_TYPE_I = "islice";
	public static final String SLICE_TYPE_D = "dslice";
	public static final String STRUCT_POINTER_ACCESS = "->";
	public static final String STRUCT_ACCESS = GType.STRUCT_SEP;
	// [1%2@3%4] is a 2x2 matrix with the following separators
	public static final String MATRIX_SEPARATOR = "@";
	public static final String VECTOR_SEPARATOR = "§";
	public static final String PARAMS_SEPARATOR = "##";
	public static final String APPLY_SEPARATOR = "ç";
	private static final String SPARSE_EXPR_SEPARATOR = "₺";
	public static final String LHS_VECTOR_SEPARATOR = "$";

	protected static final String SUN_INIT_PREFIX = "sun_init";
	protected static final String CSV_LOADED_PREFIX = "stored_csv_";

	protected int sun_init_number = 0;
	protected static int csv_loaded_number = 0;

	// used to give a name to the SUN_INIT struct, global among C class instances0
	protected static int user_data_struct_number = 0;
	protected static final String USER_DATA_STRUCT_PREFIX = "USER_DATA_";
	protected static final String STRUCT_SUFFIX = "_s";
	protected int user_data_variable_number = 0;

	protected static final String USER_DATA_VARIABLE_PREFIX = "user_data_impl_";
	protected static final String MATRIX_DIMS_SUFFIX = "_dims";
	protected static final int COL_DIMENSION = 1;
	protected static final int ROW_DIMENSION = 0;
	protected static boolean ROWMAJOR = true;
	// default parameters for ODE solving
	protected static Double DEFAULT_REL_TOL = 1.0E-5;
	protected static Double DEFAULT_ABS_TOL = 1.0E-6;
	protected static Double DEFAULT_MAX_STEP = -1.0;
	protected static Double DEFAULT_INITIAL_STEP = -1.0;
	protected static Integer DEFAULT_MAX_ORDER = -1;
	// default number of steps in which the integrator will provide
	// approximations of the solution if the tspan is specified as
	// a start/end matrix only. ex. [1 10]
	protected static Integer DEFAULT_NUMBER_STEPS = 100;

	List<Function> inlineFun;
	List<Function> storedFun;
	Function curFun = null;

	// local function body
	protected StringBuffer localFunctions;
	// local struct body
	protected StringBuffer localStruct;
	// global variables
	protected StringBuffer globalVars;
	// local struct body
	protected StringBuffer translationInitialization;

	private AASTNode curStmtList;
	// structs defined globally
	protected static Set<String> structs;
	// matrix of struct types defined (in general)
	protected static Set<String> matrixTypes;
	protected static Set<String> matrixCopyOnFunctions;
	protected static Set<String> alreadyAllocatedInputArrays;
	protected static Set<String> globalVariables;
	protected static Set<String> wrapFunctions;

	// the maximum depth of a matrix in this program, ex 3 for max 3D matrices
	private static int maxMatrixDimension = 0;
	private static List<Triple<String, String, Integer>> matricesDefined = null;
	// list of matrix indices on which we can summarize
	private static Set<Integer> summarizeByCol = null;
	protected static Set<String> skip_function_names = null;

	// third set: symbols related to a specific code block
	protected List<Triple<AASTNode, StringBuffer, Set<String>>> translationBufferList = new ArrayList<Triple<AASTNode, StringBuffer, Set<String>>>();
	// index of matrix-scalar expression or pointwise matrix matrix expression
	// can be replaced if it happens to be inside a matrix definition
	protected String EXPR_INDEX = "_expr_i";
	protected String EXPR_POS = "_expr_pos";
	protected String SPARSE_COL_INDEX = "_sparse_col_i";
	protected String SPARSE_ROW_INDEX = "_sparse_row_i";
	protected String SPARSE_ELEMENT_COUNT = "_sparse_nz";
	protected String SPARSE_TMP_DOUBLE_COLUMN = "_dsparse_tmp_column";
	protected String SPARSE_TMP_DOUBLE_MARKER = "_dsparse_tmp_marker";
	protected String SPARSE_TMP_INT_COLUMN = "_isparse_tmp_column";
	protected String SPARSE_TMP_INT_MARKER = "_isparse_tmp_marker";
	protected int SLICE_ARRAY_DIMS = 1;
	// this node (and his childs) shouldn't be translated
	protected AASTNode skipTranslationUntil = null;
	private boolean stopOnError;
	protected static final String SEQUENCE_TO_MATRIX_FUN = "v?!SequenceVector";

	// C-backend specific options
	protected String programName = null;
	protected String sundialsVersion = null;
	protected List<String> supportedSundialsVersions = Arrays.asList("5", "2", "3", "4");
	protected Path sunincludepath = null;
	protected Path sunlibpath = null;
	protected Path stdlibpath = null;
	protected Path tcmallocpath = null;
	protected Path tcmallocinclude = null;

	protected List<String> supportedSparseMatrixTypes = Arrays.asList("REAL_NOSYMM", "REAL_SYMM",
			"REAL_SYMM_POSITIVE_DEF");

	// enum that stores parameters (name and default values) that can optionally
	// be passed to the numerical integrator
	private enum OdeParameter {
		REL_TOL(new String[] { "RelTol" }, DEFAULT_REL_TOL), ABS_TOL(new String[] { "AbsTol" }, DEFAULT_ABS_TOL),
		MAX_STEP(new String[] { "MaxStep" }, DEFAULT_MAX_STEP),
		INITIAL_STEP(new String[] { "InitialStep" }, DEFAULT_INITIAL_STEP),
		MAX_ORDER(new String[] { "MaxOrder" }, DEFAULT_MAX_ORDER);

		private String[] aliases;
		private Object defaultValue;

		private OdeParameter(String[] aliases, Object defaultValue) {
			this.aliases = aliases;
			this.defaultValue = defaultValue;
		}

		public boolean is(String strValue) {
			for (String alias : aliases)
				if (alias.equals(strValue))
					return true;
			return false;
		}
	}

	public C(IFunction[] coreFunctions, String mainSourcePath, String destFolder, String inputFile, Integer indent,
			Map<String, List<String>> backendSpecificOptions, boolean dockerized) {
		super(coreFunctions, mainSourcePath, destFolder, inputFile, indent, backendSpecificOptions, dockerized);
		localFunctions = new StringBuffer();
		globalVars = new StringBuffer();
		localStruct = new StringBuffer();
		translationInitialization = new StringBuffer();
		globalStructs = new HashMap<String, Triple<GType, SymbolType, List<String>>>();
		baseStructAlreadyExists = new ArrayList<String>();
		inlineFun = new ArrayList<Function>();
		storedFun = new ArrayList<Function>();
	}

	public StringBuffer getLocalStructBuffer() {
		return localStruct;
	}

	public StringBuffer getLocalFunctionsBuffer() {
		return localFunctions;
	}

	public List<Triple<AASTNode, StringBuffer, Set<String>>> getTranslationBufferList() {
		return translationBufferList;
	}

	@Override
	public Path mainPath() {
		Path p = Paths.get(destFolder).resolve(OBJECTS_FOLDER_NAME);
		// create if it does not exists
		p.toFile().mkdirs();
		return p;
	}

	@Override
	public void translate(AAST program, boolean stopOnError) throws GException {
		this.stopOnError = stopOnError;
		translationBufferList.add(new Triple<>(program.rootNode(), translation, new HashSet<String>()));
		// actually translate
		program.rootNode().walkFor(this, stopOnError);
	}

	@Override
	public void onWalkCompleted(AAST program) {
	}

	@Override
	public void onWalkStarted(AAST program) {
	}

	@Override
	public void onEnter(AAST program, AASTNode curRoot) throws GException {
		if (curRoot.type().equals(NodeType.FUNCTION)) {
			// switch to another file
			String curFunName = curRoot.child(NodeType.ID).name();
			List<String> outSymbols = (List<String>) curRoot.attr(NodeAttr.FUNCTION_OUTPUT);
			List<String> funParams = (List<String>) curRoot.attr(NodeAttr.FUNCTION_PARAMS);

			if (curRoot.hasAttr(NodeAttr.FUNCTION_RESOLVED) && (Boolean) curRoot.attr(NodeAttr.FUNCTION_RESOLVED)) {
				// checked and used in newTranslation/newComment
				// string generation is made by C but translations goes to CFunction
				curFun = getCFunctionInstance(this, curRoot, curFunName, outSymbols, funParams);
				inlineFun.add(curFun);
			} else {
				skipTranslationUntil = curRoot;
				String funName = curRoot.child(NodeType.ID).name();
				program.addWarning(
						new UndefinedTranslationException(CErrorMessage.WARN_SKIP_FUNCTION, curRoot, funName));
				skip_function_names.add(funName);
			}
		} else if (curRoot.type().equals(NodeType.STATEMENT_LIST)) {
			curStmtList = curRoot;
			// change translation buffer at start of statement list of command statement
			if (curRoot.parentExists(NodeType.IF) || curRoot.parentExists(NodeType.ELSEIF)
					|| curRoot.parentExists(NodeType.ELSE) || curRoot.parentExists(NodeType.WHILE)
					|| curRoot.parentExists(NodeType.FOR) || curRoot.parentExists(NodeType.PARFOR)) {
				if (curRoot.parentExists(NodeType.FOR) || curRoot.parentExists(NodeType.PARFOR)) {
					AASTNode parent;
					String idx;
					if (curRoot.parentExists(NodeType.FOR)) {
						parent = curRoot.parent(NodeType.FOR);
					} else {
						parent = curRoot.parent(NodeType.PARFOR);
					}
					idx = "_pos" + parent.symbol();
					String iteratorID = parent.child(NodeType.ID).symbol();

				}
				addTranslationBuffer(curRoot);

			}
		} else if (curRoot.type().equals(NodeType.LINECOMMENT)) {
			if (curRoot.name().contains(QSPCC_DIRECTIVE)) {
				int start = curRoot.name().indexOf(QSPCC_DIRECTIVE);
				if (start > 0) {
					AASTNode stmtList = curRoot.parent(NodeType.STATEMENT_LIST);
					int statement_number = stmtList.childPosition(curRoot);
					if (statement_number + 1 < stmtList.childs().size())
						stmtList.childs().get(statement_number + 1).attr(CAttr.OMP_DIRECTIVE,
								curRoot.name().substring(start).replaceAll(QSPCC_DIRECTIVE, "#pragma omp "));
				}
			}
		}

	}

	protected Function getCFunctionInstance(C c, AASTNode curRoot, String curFunName, List<String> outSymbols,
			List<String> funParams) {
		return new CFunction(this, coreFunctions, curRoot,
				Paths.get(destFolder, curFunName + ".c").toAbsolutePath().toString(), destFolder, defineVariables, 1,
				backendSpecificOptions, curFunName, outSymbols, funParams, getGlobalBuffer(), getLocalStructBuffer(),
				dockerized);
	}

	@Override
	public void onExit(AAST program, AASTNode curRoot, Deque<String> results) throws GException {
		if (skipTranslationUntil != null) {
			if (curRoot.equals(skipTranslationUntil)) {
				skipTranslationUntil = null;
			}
			// skip translation
			return;
		}
		StringBuffer result = new StringBuffer();
		List<String> child_results;

		try {
			switch (curRoot.type()) {
			case PROGRAM:
			case ID_NODE:
			case LHS:
			case RHS:
			case NL:
			case SEMI:
			case COMMA:
			case FUNCTION_RETURN:
			case PARAMETER_LIST:
			case CLEAR:
			case CLEARVARS:
			case CLOSE:
			case EXPRESSION:
			case PERSISTENT:
				// handled in createVariable, thanks to PERSISTENT_VAR attribute on variables
				// mentioned here
			case GLOBAL:
				// handled in createVariable, thanks to GLOBAL_VAR attribute on variables
				// mentioned here
				// do nothing don't change stack
				return;
			case AT:
				// references to functions like @f where f is a function
				return;
			case VOID:
				// if inside LHS produce a null result
				if (curRoot.parentExists(NodeType.LHS))
					results.push("<VOID>");
				return;
			case FUNCTION:
				// removes the child results from the stack.
				child_results = getChildResults(results, curRoot.childs(NodeType.PARAMETER_LIST).size());
				if (curFun != null) {

					results.push(curFun.functionName());
					Function removed = inlineFun.remove(inlineFun.size() - 1);

					storedFun.add(removed);

					if (!inlineFun.isEmpty()) {
						curFun = inlineFun.get(inlineFun.size() - 1);
					} else {
						curFun = null;
					}
				}
				return;

			case PARFOR:
			case FOR: {
				// write the for body
				StringBuffer forLoc = new StringBuffer();
				if (curRoot.type().equals(NodeType.PARFOR))
					forLoc.append("#pragma omp parallel for" + NL);
				forLoc.append("for(");

				// write with the last three results
				child_results = getChildResults(results, curRoot.childs().size() - 1);

				if (child_results.size() < 2) {
					throw new UndefinedTranslationException(CErrorMessage.INTERNAL_FOR_NEEDS_INDEX_AND_EXPRESSION,
							curRoot);
				}

				String variableTypeAndName = child_results.get(0);
				String conditionString = child_results.get(1);
				AASTNode conditionNode = getExprNode(curRoot.childs().get(1));
				String[] iteratorContents = variableTypeAndName.split(VECTOR_SEPARATOR);
				String[] conditions = conditionString.split(VECTOR_SEPARATOR);
				// check type
				GType type = getExprGeneralized(conditionNode);
				if (!type.isCastableToMatrix())
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_LOOP, curRoot, type);

				String variableType = iteratorContents[0];
				String variableName = iteratorContents[1];
				String idx = null;

				if (type.equals(BType.MATRIX_ACCESS_SLICE)) {
					SliceType conditionType = (SliceType) type;

					if (conditionNode.type().equals(NodeType.COLON)) {
						// this conditionNode wasn't translated because used just to loop
						int condition_idx = 0;
						for (GType sliceElement : conditionType.slices()) {
							if (TypeUtils.isDegeneratedMatrix(sliceElement))
								// if it's a degenerated matrix, use the only element inside
								conditions[condition_idx] = conditions[condition_idx] + STRUCT_ACCESS + "matrix[0]";

							condition_idx++;
						}
						// assume linear increase in case of slice defined in-for
						conditions[1] = variableName + "+" + conditions[1];
						conditions[2] = variableName + "<=" + conditions[2];
						idx = variableName;
					} else {
						String exprStatement = conditions[0];
						conditions = new String[3];
						idx = "_pos" + curRoot.symbol();
						String updateFun = "v?SliceAccess";
						if (conditionType.of().equals(BType.SCALAR)) {
							updateFun = updateFun.replace('?', 'd');
						} else {
							updateFun = updateFun.replace('?', 'i');
						}
						conditions[2] = idx + " < " + TypeUtils.matrixDimName(exprStatement, 1);
						// conditions comes from the translation of the expression
						conditions[0] = updateFun + "( &" + exprStatement + ", 0), " + idx + " = 0";
						conditions[1] = updateFun + "( &" + exprStatement + ", ++" + idx + ")";
					}
				} else if (type.equals(BType.MATRIX)) {
					MatrixType conditionType = (MatrixType) type;
					idx = "_pos" + curRoot.symbol();
					String[] mdims = matrixDimsToStr(curRoot, conditionType);
					String end = String.join(" * ", mdims);
					String exprStatement = conditions[0];
					conditions = new String[3];
					// start
					conditions[0] = exprStatement + STRUCT_ACCESS + "matrix[0], " + idx + " = 0";
					// condition
					conditions[2] = idx + " < " + end;
					// step
					conditions[1] = exprStatement + STRUCT_ACCESS + "matrix[++" + idx + "]";
				} else
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_LOOP, curRoot, type);

				forLoc.append(variableName).append("=").append(conditions[0]).append(";");
				forLoc.append(conditions[2]).append(";");
				forLoc.append(variableName).append("=").append(conditions[1]).append(")").append(NL);

				forLoc.append("{").append(NL);
				forLoc.append(getCurrentTranslationBuffer(curRoot).second().toString());
				forLoc.append("}").append(NL);

				// delete the if String Buffer
				removeTranslationBuffer();
				// define variable in parent buffer (before for)
				if (idx != null)
					createVariable(GType.get(BType.INT), idx, NodeType.ID, "0", false,
							VarVisibility.LOCAL_TO_BLOCK.context(curRoot));

				// write the if buffer on the previous String Buffer
				// here you need to split on each line
				newComment(curRoot);
				newTranslation(curRoot, forLoc.toString());

				return;
			}
			case IF:
			case WHILE: {
				// gets the last things to be written
				StringBuffer loc = new StringBuffer();
				// number of translations to read, read just condition because translation is on
				// the stack
				// of translation buffers, + one extra value for every else if (that can add his
				// own initialization block)
				int n_childs = 1;
				// number of buffers to read and paste, usually 1,
				// in case of if/else 2 (the then, and the else)
				int extra_branch = 0;
				String stmt;
				if (curRoot.type().equals(NodeType.WHILE)) {
					stmt = "while";
				} else if (curRoot.type().equals(NodeType.IF)) {
					stmt = "if";
					for (AASTNode child : curRoot.childs())
						if (child.type().equals(NodeType.ELSE))
							extra_branch++;
						else if (child.type().equals(NodeType.ELSEIF)) {
							extra_branch++;
							n_childs++;
						}
				} else
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_NODE, curRoot);

				child_results = getChildResults(results, n_childs);

				Tuple<String, StringBuffer> condition = getConditionString(curRoot, child_results);
				if (condition == null)
					throw new UndefinedTranslationException(
							CErrorMessage.INTERNAL_CONTROL_STATEMENT_CONDITION_UNDEFINED, curRoot);

				// omp directives if present
				// if (curRoot.hasAttr(CAttr.OMP_DIRECTIVE) && extra_branch == 0 && n_childs ==
				// 1) {
				// SyntaxException ex = new SyntaxException(CErrorMessage.WARN_OMP_DIRECTIVE,
				// curRoot, curRoot.type());
				// curRoot.compilationUnit().addWarning(ex);
				// loc = new StringBuffer();
				// loc.append(curRoot.attr(CAttr.OMP_DIRECTIVE)).append(NL);
				// } else {
				loc.append(stmt);
				loc.append("(");
				loc.append(condition.first());
				loc.append(")");
				// }

				List<String> extras = new ArrayList<String>(extra_branch);
				String body;
				// get extras first, because they are the first ones on the stack
				for (int b = 0; b < extra_branch; ++b) {
					body = getCurrentTranslationBuffer(curRoot).second().toString();
					// delete the if String Buffer
					removeTranslationBuffer();
					// as-is
					extras.add(body);
				}
				// reverse order since this is a stack
				Collections.reverse(extras);

				loc.append("{").append(NL);

				body = getCurrentTranslationBuffer(curRoot).second().toString();
				// delete the if String Buffer
				removeTranslationBuffer();
				loc.append(body);

				loc.append("}").append(NL);

				// add extras
				for (int b = 0; b < extra_branch; ++b)
					loc.append(extras.get(b));
				// write the if buffer on the previous String Buffer
				// here you need to split on each line
				newComment(curRoot);
				if (condition.second() != null) {
					if (n_childs > 1)
						for (int nc = 1; nc < child_results.size(); ++nc)
							condition.second().append(child_results.get(nc));
					newTranslation(curRoot, condition.second().toString());
				} else if (n_childs > 1) {
					StringBuffer sb = new StringBuffer();
					for (int nc = 1; nc < child_results.size(); ++nc)
						sb.append(child_results.get(nc));
					newTranslation(curRoot, sb.toString());
				}

				newTranslation(curRoot, loc.toString());
				return;
			}
			case ELSEIF: {
				// WITH ELSEIF JUST RESET THE TRANSLATION BUFFER
				// it will be used by the parent if
				StringBuffer elsif = new StringBuffer();
				elsif.append("else if");
				// get condition
				child_results = getChildResults(results, 1);
				Tuple<String, StringBuffer> condition = getConditionString(curRoot, child_results);
				elsif.append("(");
				elsif.append(condition.first());
				elsif.append(")");
				elsif.append("{").append(NL);
				elsif.append(getCurrentTranslationBuffer(curRoot).second().toString());
				elsif.append("}").append(NL);
				clearCurrentTranslationBuffer(curRoot, elsif.toString());
				// notify if to add this extra header
				results.push((condition.second() == null) ? "" : condition.second().toString());
				return;
			}
			case ELSE:
				// WITH ELSE JUST RESET THE TRANSLATION BUFFER
				// it will be used by the parent if
				StringBuffer els = new StringBuffer();
				els.append("else").append("{").append(NL);
				els.append(getCurrentTranslationBuffer(curRoot).second().toString());
				els.append("}").append(NL);
				clearCurrentTranslationBuffer(curRoot, els.toString());
				return;
			case LINECOMMENT:
				return;

			case EXPR_STMT: {
				if (curRoot.hasAttr(NodeAttr.TRANSLATE)) {
					child_results = getChildResults(results, curRoot.childs().size() - 1);
					if (child_results.size() == 1 && curFun != null) {
						String exprTranslation = child_results.get(0);
						child_results.clear();
						String lhs = curFun.singleReturnSymbol();
						child_results.add(lhs + " = " + exprTranslation + ";");
					}
					// translate as it is
					newComment(curRoot);
					newTranslation(curRoot, String.join(NL, child_results));
				} else if (curRoot.hasAttr(CAttr.TRANSLATE)) {
					child_results = getChildResults(results, curRoot.childs().size() - 1);
					// translate as it is
					newComment(curRoot);
					List<String> newChildResults = new ArrayList<String>(child_results.size());
					for (String child : child_results)
						if (child.endsWith(";"))
							newChildResults.add(child);
						else
							newChildResults.add(child + ";");
					newTranslation(curRoot, String.join(NL, newChildResults));
				}

				return;
			}
			case STATEMENT_LIST:
				// curFun = null;
				return;

			case PARENS:
				// the if, elseif, while node will set his own brackets on the condition
				if (!curRoot.parentExists(NodeType.IF, 2) && !curRoot.parentExists(NodeType.ELSEIF, 2)
						&& !curRoot.parentExists(NodeType.WHILE, 2)) {
					child_results = getChildResults(results, curRoot.childs().size());
					String leftT = child_results.get(0);
					AASTNode topLevel = getTopLevelExpression(curRoot);
					GType topLevelExpr = getExprGeneralized(topLevel);
					if (topLevelRHSExpression(curRoot) && TypeUtils.getExprGeneralized(curRoot).isCastableToMatrix()) {
						// call this only when you are sure you want to translate now
						AASTNode outNode = getOutputNode(curRoot);
						String newMatrix = getNodeSymbol(outNode);
						StringBuffer sb = new StringBuffer();
						sb = finalizeExpression(curRoot, outNode, curRoot, sb, "(" + leftT + ")", null, null, null,
								false, true, false);

						newComment(curRoot);
						newTranslation(curRoot, sb.toString());
						results.push(newMatrix);
					} else
					// if this is a symbol, and we are concatenating this symbol with other
					// expressions, don't add parenthesis
					if ((curRoot.symbol().equals(leftT) && curRoot.parentExists(TypeUtils.expressionNodes()))
							|| (topLevelExpr.equals(BType.MATRIX) && ((MatrixType) topLevelExpr).isSparse()))
						results.push(leftT);
					else
						results.push("(" + leftT + ")");
				}
				return;

			case COLON:

				// populates the slice type
				if (curRoot.expr() != null && curRoot.expr().equals(BType.MATRIX_ACCESS_ALL)) {
					IndexType etype = ((IndexType) curRoot.expr());
					// dimension of the matrix this end refers to
					IntType mdim = etype.dimension();

					// note: this value won't be used everything will be done in
					// apply node
					if (mdim != null) {
						results.push(typeToStrValue(mdim));
					} else {
						results.push("");
					}
				} else if (!curRoot.parent().type().equals(NodeType.COLON)) {
					if (curRoot.childExists(NodeType.COLON))
						child_results = getChildResults(results, curRoot.childs().size() + 1);
					else {
						child_results = getChildResults(results, curRoot.childs().size());
						// implicit 1 for two-parameter slices
						child_results.add(1, "1");
					}
					// we're in a for codition, we don't have to translate as a matrix
					// but with the conditions
					if (curRoot.parent().parent().type().equals(NodeType.FOR)
							|| curRoot.parent().parent().type().equals(NodeType.PARFOR)) {
						String thisResult = child_results.get(0) + VECTOR_SEPARATOR + child_results.get(1)
								+ VECTOR_SEPARATOR + child_results.get(2);
						results.push(thisResult);
					} else {
						results.push(translateSliceOperator(curRoot, child_results));
					}
				}
				return;
			case EXP:
			case PLUS:
			case MINUS:
			case TIMES:
			case LEFTDIV:
			case RIGHTDIV:
			case ELEMENTWISE_TIMES:
			case ELEMENTWISE_LEFTDIV:
			case ELEMENTWISE_EXP:
				AASTNode topLevel = getTopLevelExpression(curRoot);
				child_results = getChildResults(results, curRoot.childs().size());
				/*
				 * use only a single Algorithm, takes as input also if sparse matrices are
				 * inside expr (add flag to toplevel). if toplevel.. Completed if sparse..
				 * update accumulator with expr till now (including sparse matrix) and add to
				 * stack <update acc code>|symbol.matrix[expr_index] for expression from here to
				 * toplevel else go on as already did
				 * 
				 * --> we need to change the pushed element to introduce a separator for symbol
				 * and acc code.
				 * 
				 * (flag)? --> we need to change finalizeExpression to add at least a double
				 * for, first on row, second on column. (flag)? --> we need to change expr_index
				 * to be c+r*dim2
				 */
				// true if this (potentially sub-)expression involves a sparse matrix
				boolean[] sparseParams = new boolean[curRoot.childs().size()];
				for (int i = 0; i < curRoot.childs().size(); ++i) {
					AASTNode operand = TypeUtils.getIDNode(curRoot.childs().get(i));
					GType oexpr = TypeUtils.getExprGeneralized(operand);
					sparseParams[i] = oexpr.equals(BType.MATRIX) && ((MatrixType) oexpr).isSparse();
					if (sparseParams[i]) {
						if (!topLevel.hasAttr(CAttr.CONTAINS_SPARSE_SUBEXPRESSION))
							topLevel.attr(CAttr.CONTAINS_SPARSE_SUBEXPRESSION, new LinkedList<AASTNode>());
						// add id, transpose, or apply that returns sparse matrices
						if (operand.type().equals(NodeType.ID) || operand.type().equals(NodeType.ELEMENTWISE_CCT)
								|| operand.type().equals(NodeType.TRANSPOSE) || operand.type().equals(NodeType.APPLY))
							// add id to list of sparse subnodes
							((List<AASTNode>) topLevel.attr(CAttr.CONTAINS_SPARSE_SUBEXPRESSION)).add(operand);
					}
				}
				GType rootExpr = TypeUtils.getExprGeneralized(topLevel);
				boolean sparseExpr = rootExpr.equals(BType.MATRIX) && ((MatrixType) rootExpr).isSparse();

				// TODO: check if there is at least a sparse matrix in this expression.
				// if yes always use *SparseExpression function
				if (curRoot.childs().size() == 1) {
					// unary
					AASTNode left = curRoot.childs().get(0);
					results.push(translateUnaryScalarExpression(topLevel, curRoot, left, child_results.get(0),
							sparseParams[0]));
				} else {

					// binary
					AASTNode left = curRoot.childs().get(0);
					AASTNode right = curRoot.childs().get(1);
					GType lexpr = getExprGeneralized(left);
					GType rexpr = getExprGeneralized(right);
					if (lexpr.isCastableToScalar() && rexpr.isCastableToScalar())
						results.push(translateScalarExpression(curRoot, left, right, child_results));
					else if (lexpr.isCastableToMatrix() && rexpr.isCastableToScalar()) {
						results.push(translateMatrixScalarExpression(topLevel, curRoot, left, right,
								child_results.get(0), child_results.get(1), false, sparseParams[0]));
					} else if (rexpr.isCastableToMatrix() && lexpr.isCastableToScalar()) {
						results.push(translateMatrixScalarExpression(topLevel, curRoot, right, left,
								child_results.get(1), child_results.get(0), true, sparseParams[1]));
					} else if (rexpr.isCastableToMatrix() && lexpr.isCastableToMatrix()) {
						String leftT = child_results.get(0);
						String rightT = child_results.get(1);

						if (isPointwiseOperation(curRoot.type()) || TypeUtils.isDegeneratedMatrix(rexpr)
								|| TypeUtils.isDegeneratedMatrix(lexpr)) {
							results.push(translateMatrixMatrixPointwiseExpression(topLevel, curRoot, left, right, leftT,
									rightT, sparseParams));
						} else if (curRoot.type().equals(NodeType.TIMES) || curRoot.type().equals(NodeType.RIGHTDIV)
								|| curRoot.type().equals(NodeType.LEFTDIV))
							if (curRoot.type().equals(NodeType.LEFTDIV))
								results.push(translateMatrixMatrixExpression(topLevel, curRoot, right, left, rightT,
										leftT, sparseParams));
							else
								results.push(translateMatrixMatrixExpression(topLevel, curRoot, left, right, leftT,
										rightT, sparseParams));
						else
							throw new TypeException(CErrorMessage.UNSUPPORTED_OPERATOR_TYPES, curRoot, lexpr, rexpr);
					} else
						throw new TypeException(CErrorMessage.UNSUPPORTED_OPERATOR_TYPES, curRoot, lexpr, rexpr);
				}
				return;

			case NEG:
			case GRTE:
			case GRT:
			case LSTE:
			case LST:
			case DOUBLE_EQ:
			case LOG_AND:
			case LOG_OR:
			case BIN_AND:
			case BIN_OR:
			case NEQ:
				child_results = getChildResults(results, curRoot.childs().size());
				if (child_results.size() > 1)
					results.push(translateBooleanExpression(curRoot, child_results));
				else
					results.push(translateUnaryBooleanExpression(curRoot, child_results));
				return;

			case ID:
				String rootName = curRoot.name();
				GType currType = curRoot.expr();
				// if we're in a for condition (namely (i=1:100))
				// push it into results, it will be managed with
				// an in-line declaration
				if (curRoot.parent().type().equals(NodeType.FOR) || curRoot.parent().type().equals(NodeType.PARFOR)) {
					String cType = exprTypeToCType(currType);
					results.push(cType + VECTOR_SEPARATOR + rootName);
					return;
				}

				if (curRoot.parent().type().equals(NodeType.CLEAR) || curRoot.parent().type().equals(NodeType.CLEARVARS)
						|| curRoot.parent().type().equals(NodeType.CLOSE)) {
					// ignore clear specifiers
					return;
				}

				// if it's an environment variable, or a function without parameters translate
				// it here
				// if(curRoot.hasAttr(NodeAttr.ETYPE_FROM_EXTERNAL_ENV) && (Boolean)
				// curRoot.attr(NodeAttr.ETYPE_FROM_EXTERNAL_ENV)){
				if ((curRoot.hasAttr(NodeAttr.FUNCALL) || isCoreFunction(curRoot))
						&& !curRoot.parent().type().equals(NodeType.APPLY)) {
					String params = null;
					String[] actualParams = new String[0];
					String funName = rootName;
					if (isCoreFunction(curRoot)) {
						String resultString = translateCoreFunction(curRoot, funName, params);
						// NOTE: this sould be false only if the top-level is an EXPR_STMT
						// otherwise core functions should at least return an empty string
						// see "class" or "toc" core functions
						if (resultString != null) {
							results.push(resultString);
							AASTNode exprNode = curRoot.parent(TypeUtils.expressionLimitNodes());
							if (exprNode.type().equals(NodeType.EXPR_STMT))
								// if parent is expr stmt translate this
								exprNode.attr(CAttr.TRANSLATE, true);
						}
					} else if (curRoot.expr().isCastableToScalar()) {
						AASTNode exprNode = curRoot.parent(TypeUtils.expressionLimitNodes());
						if (exprNode.type().equals(NodeType.EXPR_STMT))
							// if parent is expr stmt translate this
							exprNode.attr(CAttr.TRANSLATE, true);
						results.push(funName + "(" + String.join(", ", actualParams) + ")");
					} else {
						AASTNode outNode = getOutputNode(curRoot, true, true);
						String assign;
						String outNodeName;
						if (GType.get(BType.VOID).equals(getExprGeneralized(outNode))) {
							assign = "";
							outNodeName = "";
						} else {
							outNodeName = getNodeSymbol(outNode);
							assign = outNodeName + " = ";
						}
						newComment(curRoot);
						newTranslation(curRoot, assign + funName + "();" + NL);
						results.push(outNodeName);
					}
					return;
				}
				// coreFunctions[]

				// do not allocate memory for already allocated function parameters
				// push just the name of the related function parameter
				if (curFun != null && curFun.getFunctionParams().contains(curRoot.name())) {
					results.push(curRoot.name());
					return;
				}

				if (currType != null && currType.equals(BType.MATRIX) && ((MatrixType) currType).isInput()) {
					// in matlab you can access the field of a previously loaded data file both just
					// with the name
					// of the field or with the usual sintax struct_name.field_name
					if (curRoot.parentExists(NodeType.FIELDACCESS)) {
					} else {
						// case just field
						createVariable(curRoot, false, curRoot.parentExists(NodeType.GASSIGN) ? VarVisibility.GLOBAL
								: VarVisibility.LOCAL_TO_FUNCTION);
					}
				}
				// case function definition
				if (curRoot.parent().type().equals(NodeType.PARAMETER_LIST)
						|| curRoot.parent().type().equals(NodeType.FUNCTION_RETURN)
						|| curRoot.parent().type().equals(NodeType.FUNCTION)) {
					if (curRoot.hasAttr(NodeAttr.IS_FUNCTION_ENV_PARAM))
						// doesn't create env params
						return;
					GType returnType = getExprGeneralized(curRoot);
					if (returnType != null && returnType instanceof MatrixType)
						createVariable(curRoot, false, VarVisibility.LOCAL_TO_FUNCTION);

					return;
				}

				GType cexpr = curRoot.expr();
				if ((!curRoot.parent().type().equals(NodeType.APPLY) || curRoot.parentExists(NodeType.LHS))
						&& !curRoot.hasAttr(NodeAttr.IS_FUNCTION_PARAM) && cexpr != null
						// do not allocate space for matrices nested in other
						// matrices
						&& !(cexpr.equals(BType.MATRIX) && curRoot.parentExists(NodeType.MATRIX)
								&& !curRoot.parentExists(NodeType.LHS))
						// do not allocate statically space for matrix to be returned by a function
						&& !(cexpr.equals(BType.MATRIX) && curRoot.hasAttr(NodeAttr.IS_FUNCTION_OUTPUT)
								&& (Boolean) curRoot.hasAttr(NodeAttr.IS_FUNCTION_OUTPUT))
						// if in a fieldaccess a.b and i'm not a
						&& !(curRoot.parent().type().equals(NodeType.FIELDACCESS)
								&& !curRoot.parent().childs().get(0).equals(curRoot))
						&& !cexpr.equals(BType.VOID)) {
					createVariable(curRoot, false,
							(curRoot.parentExists(NodeType.GASSIGN) || curRoot.hasAttr(NodeAttr.GLOBAL_VAR))
									? VarVisibility.GLOBAL
									: VarVisibility.LOCAL_TO_FUNCTION);
				}

				// if it's part of the output (just if it's not already initialized)
				if (curRoot.hasAttr(NodeAttr.IS_FUNCTION_OUTPUT)
						&& (Boolean) curRoot.hasAttr(NodeAttr.IS_FUNCTION_OUTPUT)) {

					// do not redeclare it in the case of main function output, for in this case it
					// has already been
					// declared
					AASTNode parentFunction = curRoot.parent(NodeType.FUNCTION);
					if (parentFunction.hasAttr(NodeAttr.FUNCTION_OUTPUT)) {
						List<String> returnType = (List<String>) parentFunction.attr(NodeAttr.FUNCTION_OUTPUT);
						if (returnType != null && returnType.size() == 1 && returnType.get(0).equals(curRoot.name())) {
							results.push(curRoot.name());
							return;
						}
					}

					createVariable(curRoot, false,
							(curRoot.parentExists(NodeType.GASSIGN) || curRoot.hasAttr(NodeAttr.GLOBAL_VAR))
									? VarVisibility.GLOBAL
									: VarVisibility.LOCAL_TO_FUNCTION);
				}

				results.push(curRoot.name());

				return;
			case INT:
			case FLOAT:
				results.push(curRoot.name());
				return;

			case STRING:
				results.push("\"" + curRoot.name().replaceAll("'|\"", "").replaceAll("% ", "%% ") + "\"");
				return;

			case RETURNS:
				newComment(curRoot);
				if (curFun.needsOutputStructure())
					newTranslation(curRoot, curFun.fillOutputStructureCode());
				else if (curFun.isVoid())
					newTranslation(curRoot, "return;" + NL);
				else
					newTranslation(curRoot, "return " + curFun.singleReturnSymbol() + ";" + NL);
				return;

			case END:
				IndexType etype = ((IndexType) curRoot.expr());
				// dimension of the matrix this end refers to
				IntType mdim = etype.dimension();
				results.push(mdim.valueAsString());
				return;

			case FUNCTION_PARAMETER_LIST:

				child_results = getChildResults(results, curRoot.childs().size());
				// parameters of.. matrix access

				if ((curRoot.childs().size() == 1 && curRoot.childExists(NodeType.END))
						|| curRoot.hasAttr(NodeAttr.MATRIX_PARAMS)) {
					AASTNode matrixNode = (AASTNode) curRoot.attr(NodeAttr.MATRIX_PARAMS);

					if (!matrixNode.expr().isCastableToMatrix())
						if (matrixNode.expr().isCastableToScalar() && child_results.size() == 1
								&& "1".equals(child_results.get(0))) {
							// scalar of 1 is itself
							results.push("1");
							return;
						} else
							throw new UndefinedTranslationException(CErrorMessage.INTERNAL_APPLY_TO_NON_MATRIX, curRoot,
									matrixNode.expr());
					IntType[] dims = ((DimensionType) matrixNode.expr()).dims();
					// if the matrix I'm trying to access is one-dimensional
					// and I'm trying to access with just one integer index, it could be
					// more convenient to access to the matrix in a flat way
					results.push(String.join(PARAMS_SEPARATOR, child_results));
					// parameters of.. function call
				} else if (curRoot.hasAttr(NodeAttr.FUNCALL_PARAMS)) {
					// child_results
					AASTNode functionNode = program.functionSymbolNode(curRoot.parent().child(NodeType.ID));
					if (functionNode.hasAttr(NodeAttr.FUNCTION_ENV_PARAMS)) {
						List<AASTNode> env_params = (List<AASTNode>) functionNode.attr(NodeAttr.FUNCTION_ENV_PARAMS);
						for (AASTNode env_param : env_params)
							if (!child_results.contains(env_param.symbol()))
								child_results.add(env_param.symbol());
					}
					results.push(String.join(PARAMS_SEPARATOR, child_results));
				} else
					throw new UndefinedTranslationException(CErrorMessage.INTERNAL_MISSING_PARAMETER_CONTEXT, curRoot,
							child_results);
				return;

			case APPLY: {
				// get 2 results: 1 apply id, 2 apply function parameter list
				if (curRoot.childExists(NodeType.FUNCTION_PARAMETER_LIST))
					child_results = getChildResults(results, curRoot.childs().size());
				else
					// get only id (case a = b; where b is a function)
					child_results = getChildResults(results, 1);

				if (curRoot.hasAttr(NodeAttr.MATACCESS)) {
					List<AASTNode> params = new ArrayList<AASTNode>();
					boolean allParamsNumeric = true;
					for (AASTNode param : curRoot.childs().get(1).childs()) {
						// go below the expressions
						if (param.type().equals(NodeType.EXPRESSION)) {
							param = param.childs().get(0);
						}
						AASTNode p = TypeUtils.getIDNode(param);
						allParamsNumeric &= getExprGeneralized(p).isCastableToScalar();
						params.add(param);
					}

					AASTNode whereToApplyTheAccess = curRoot.child(NodeType.ID);
					if (whereToApplyTheAccess == null) {
						whereToApplyTheAccess = curRoot.child(NodeType.FIELDACCESS);
					}

					if (curRoot.parentExists(NodeType.LHS, 2))
						// because this will be translated by assign statement,
						// push just the LHS id node translation
						results.push(child_results.get(0) + APPLY_SEPARATOR + child_results.get(1));
					else {
						// pay attention, this WONT set CAttr.TRANSLATE=false
						// because of the parameters
						AASTNode outNode = getOutputNode(curRoot, true, false);
						boolean assignNow = canBeTranslatedByRHS(curRoot);
						String outputNodeName = getNodeSymbol(outNode);

						String[] translatedParamsArr = child_results.get(1).split("\\" + PARAMS_SEPARATOR);
						// || curRoot.parentExists(NodeType.LHS) .. because
						// if inside a deep lhs should be translated now because we are not able to
						// propagate it too up
						// unless we complicate quite a bit the code.
						// if not inside lhs, push for translation in parent assign
						GType inType = getExprGeneralized(whereToApplyTheAccess);
						if (enablePreallocatedCheck && inType.equals(BType.MATRIX) && allParamsNumeric) {
							// do only the check (checkOnly = true)
							StringBuffer sb = new StringBuffer();
							ensureMatrixMemory(sb, whereToApplyTheAccess, null, (MatrixType) inType,
									translatedParamsArr, true, false, false, null);
							// if assignNow is false, but we have to check for correct memory access,
							// do it now
							newTranslation(curRoot, sb.toString());
						}
						results.push(translateMatrixAccess(curRoot, outNode, outputNodeName, whereToApplyTheAccess,
								child_results.get(0), params, null, null, translatedParamsArr, assignNow));
					}
				} else if (curRoot.hasAttr(NodeAttr.FUNCALL)) {
					AASTNode funIdNode = curRoot.child(NodeType.ID);
					AAST aast = curRoot.compilationUnit();
					if (!aast.functionSymbolExists(funIdNode))
						throw new UnboundException(CErrorMessage.INTERNAL_USER_FUNCTION_NOT_FOUND, curRoot,
								child_results.get(0));

					String[] actualParams = null;
					if (child_results.size() == 1 || "".equals(child_results.get(1)))
						actualParams = new String[0];
					else
						actualParams = child_results.get(1).split("\\" + PARAMS_SEPARATOR);
					AASTNode funNode = aast.functionSymbolNode(funIdNode);
					List<AASTNode> formalParams = funNode.childs(NodeType.PARAMETER_LIST);
					List<AASTNode> actualParamNodes = curRoot.childs(NodeType.FUNCTION_PARAMETER_LIST);

					List<String> newParams = new ArrayList<>(formalParams.size());
					int i = 0;

					GType paramExpr = null;
					if (actualParamNodes != null) {
						for (; i < actualParamNodes.size() && i < actualParams.length; ++i) {
							newParams.add(actualParams[i]);
							paramExpr = getExprGeneralized(actualParamNodes.get(i));
							if (paramExpr != null && paramExpr.equals(BType.STRUCT) && paramExpr.isInput())
								// add extra parameter inputName that this param relies on
								newParams.add(paramExpr.inputName());
						}
					}
					// there are also env parameters
					if (funNode.hasAttr(NodeAttr.FUNCTION_ENV_PARAMS)) {
						List<AASTNode> env_params = (List<AASTNode>) funNode.attr(NodeAttr.FUNCTION_ENV_PARAMS);
						int env_start = i;
						for (; i < actualParams.length; ++i) {
							newParams.add(actualParams[i]);
							paramExpr = getExprGeneralized(env_params.get(i - env_start));
							if (paramExpr != null && paramExpr.equals(BType.STRUCT) && paramExpr.isInput())
								// add extra parameter inputName that this param relies on
								newParams.add(paramExpr.inputName());
						}
					}
					// check if actual params match formal params. if they are lower fill with NAN
					// and NULL
					// since in advanced languages calling a function without their params is
					// allowed while in C it's not
					// add null/nan params
					if (child_results.size() > 1) {
						if (formalParams.size() > actualParams.length) {
							for (; i < formalParams.size() - actualParams.length; ++i)
								if (formalParams.get(i).expr() != null
										&& !formalParams.get(i).expr().equals(BType.VOID))
									newParams.add(getNullValue(formalParams.get(i)));
						}
						// substitute params
						child_results.set(1, String.join(PARAMS_SEPARATOR, newParams));
					} // otherwise no parameters

					if (isCoreFunction(curRoot.child(NodeType.ID))) {
						String resultCoreFunctionTranslated = translateCoreFunction(curRoot, child_results.get(0),
								child_results.get(1));

						// NOTE: this sould be false only if the top-level is an EXPR_STMT
						// otherwise core functions should at least return an empty string
						// see "class" or "toc" core functions
						if (resultCoreFunctionTranslated != null) {
							results.push(resultCoreFunctionTranslated);
							AASTNode exprNode = curRoot.parent(TypeUtils.expressionLimitNodes());
							if (exprNode.type().equals(NodeType.EXPR_STMT))
								// if parent is expr stmt translate this
								exprNode.attr(CAttr.TRANSLATE, true);
						}
					} else if (curRoot.expr().isCastableToScalar()) {
						AASTNode exprNode = curRoot.parent(TypeUtils.expressionLimitNodes());
						if (exprNode.type().equals(NodeType.EXPR_STMT))
							// if parent is expr stmt translate this
							exprNode.attr(CAttr.TRANSLATE, true);
						String[] translatedParams;
						if (child_results.size() > 1)
							translatedParams = child_results.get(1).split("\\" + PARAMS_SEPARATOR);
						else
							translatedParams = new String[0];
						results.push(child_results.get(0) + "(" + String.join(", ", translatedParams) + ")");
					} else {
						AASTNode outNode = getOutputNode(curRoot);
						String assign;
						String outNodeName;
						String tmpSymbol = null;
						GType outType = getExprGeneralized(outNode);
						boolean outMatrix = outType.equals(BType.MATRIX);
						if (GType.get(BType.VOID).equals(outType)) {
							assign = "";
							outNodeName = "";
						} else {
							outNodeName = getNodeSymbol(outNode);
							// if not matrix at this point is struct
							if (!outMatrix)
								if (outNode.equals(curRoot))
									// in this case the output node doesn't exist
									// in the source language (ex [a,b] = f(....)
									// getOutputNode will generate an intermediary
									// pointer that will be in outNodeName
									assign = outNodeName + " = ";
								else
									// in this case the output node exist in the source
									// language [a = f(...)] and it was already initialized
									// as a full empty struct, so assign structures not pointers
									assign = outNodeName + " = *";
							else if (curRoot.parentExists(NodeType.EXPR_STMT)
									|| funNode.hasAttr(NodeAttr.FUN_OUTPUT_IS_INPUT))
								assign = outNodeName + " = ";
							else {
								tmpSymbol = Symbols.getSymbolFromType(curRoot.symbol(), BType.MATRIX);
								assign = exprTypeToCType(outType) + " " + tmpSymbol + " = ";
							}
						}
						StringBuffer translation = new StringBuffer();
						newComment(curRoot);
						if (child_results.size() > 1) {
							String[] translatedParams = child_results.get(1).split("\\" + PARAMS_SEPARATOR);
							// ensure types are ok or can be casted
							adjustParamTypes(curRoot, translatedParams);
							translation.append(assign + child_results.get(0) + "(" + String.join(", ", translatedParams)
									+ ");" + NL);
						} else {
							translation.append(assign + child_results.get(0) + "();" + NL);
						}

						if (outMatrix && !curRoot.parentExists(NodeType.EXPR_STMT)
								&& !funNode.hasAttr(NodeAttr.FUN_OUTPUT_IS_INPUT)) {
							MatrixType mtype = (MatrixType) getExprGeneralized(curRoot);
							assignWithMemcpy(translation, mtype.dims().length, tmpSymbol, outNodeName, outNode,
									mtype.of());
						}

						newComment(curRoot);
						newTranslation(curRoot, translation.toString());
						results.push(outNodeName);
					}
				} else
					throw new UndefinedTranslationException(CErrorMessage.INTERNAL_APPLY_TYPE_UNDEFINED, curRoot);
				return;
			}
			case GASSIGN:
			case ASSIGN:
				child_results = getChildResults(results, curRoot.childs().size() - 1);
				// if don't translate flag was set
				if (curRoot.hasAttr(CAttr.TRANSLATE) && !((Boolean) curRoot.attr(CAttr.TRANSLATE)))
					return;

				List<AASTNode> lhs = (List<AASTNode>) curRoot.attr(NodeAttr.LHS);
				AASTNode rhs = (AASTNode) curRoot.attr(NodeAttr.RHS);
				// String rhs_symbol = (String)rhs.attr(NodeAttr.SYMBOL);
				Map<AASTNode, List<AASTNode>> lhs_params = (Map<AASTNode, List<AASTNode>>) curRoot
						.attr(NodeAttr.LHS_PARAMS);

				String rhs_symbol = child_results.get(child_results.size() - 1);
				List<String> lhs_symbols = new ArrayList<String>();
				String[] lhs_params_translation = null;
				if (lhs.size() > 1 && child_results.get(0).contains(LHS_VECTOR_SEPARATOR)) {
					for (String l : child_results.get(0).split("\\" + LHS_VECTOR_SEPARATOR))
						lhs_symbols.add(l);
					if (lhs_symbols.size() != lhs.size())
						throw new UnfinishedTranslationException(CErrorMessage.INTERNAL_MISSING_LHS_TRANSLATION,
								curRoot);
				} else if (child_results.get(0).contains(APPLY_SEPARATOR)) {
					String[] apply = child_results.get(0).split("\\" + APPLY_SEPARATOR);
					if (apply.length > 2 || apply.length == 0)
						throw new UnfinishedTranslationException(CErrorMessage.INTERNAL_MISSING_LHS_TRANSLATION,
								curRoot);
					lhs_symbols.add(apply[0]);
					lhs_params_translation = apply[1].split("\\" + PARAMS_SEPARATOR);
				} else
					lhs_symbols.add(child_results.get(0));

				result = new StringBuffer();

				if (lhs.size() == 1) {
					// if in case of deletion, output is a temporary variable, that will be then
					// re-assigned to lhs. lhs becomes the input, the only non-matrix-access-all
					// parameter
					// will be inverted (1 -> 2:n , 1:3:6 -> [2 4 5 6])
					// a(:,1) = []; <==> a = a(:,2:n)
					boolean is_delete = curRoot.hasAttr(NodeAttr.DELETE_ASSIGN_MATRIX)
							&& (Boolean) curRoot.attr(NodeAttr.DELETE_ASSIGN_MATRIX);

					AASTNode lhsNode = lhs.get(0);
					List<AASTNode> lhsNodeParams;
					String outputNodeName;
					if (is_delete) {
						outputNodeName = lhs_symbols.get(0) + "_reduced";
						createVariable(lhs.get(0).expr(), outputNodeName, NodeType.ID, null, false,
								VarVisibility.LOCAL_TO_BLOCK.context(curRoot));
						// invert parameters
						List<AASTNode> origParams = lhs_params.get(lhsNode);
						List<GType> invertedParamTypes = new ArrayList<GType>(origParams.size());
						List<String> invertedParamNames = new ArrayList<String>(origParams.size());
						DimensionType dtype = (DimensionType) lhsNode.expr();
						for (int i = 0; i < origParams.size(); ++i) {
							AASTNode param = origParams.get(i);
							if (param.expr().equals(BType.MATRIX_ACCESS_ALL)) {
								// just copy
								invertedParamTypes.add(param.expr());
								invertedParamNames.add(param.symbol());
							} else if (param.expr().isCastableToScalar()) {
								// invert, creating a matrix with all the elements except this one
								// get len of dimension
								IntType dim = dtype.dims()[i];
								// this matrix will contain all the elements except param
								String nelems = dim.valueAsString() + "-1";
								IntType inverseMatDim = (IntType) GType.get(BType.INT).name(nelems);
								String paramTranslation = param.symbol();
								String inverseMatName = "_inverse_vector_" + curRoot.symbol();
								String inverseMatStaticName = getStaticMatrixArrayName(inverseMatName);
								DimensionType inverseMatType = (DimensionType) GType.get(BType.MATRIX, inverseMatName,
										GType.get(BType.INT), new IntType[] { inverseMatDim });
								// force to be static matrix
								createVariable(inverseMatType, inverseMatName, NodeType.ID, null, false, true,
										VarVisibility.LOCAL_TO_BLOCK.context(curRoot), false);
								String cnt = "_cnt" + curRoot.symbol();
								createVariable(GType.get(BType.INT), cnt, NodeType.ID, "0", false, false,
										VarVisibility.LOCAL_TO_BLOCK.context(curRoot), false);
								// fill matrix
								String idx = "_i" + curRoot.symbol();
								result.append("for(int ").append(idx).append(" = 0; ");
								result.append(idx).append(" < ").append(dim.valueAsString());
								result.append(" ; ").append("++").append(idx).append(")").append(NL);
								// skip id that cooresponds to the param we are inverting
								result.append(TAB).append("if(").append(paramTranslation).append(" != 1+").append(idx)
										.append(")").append(NL);
								result.append(TAB).append(TAB).append(inverseMatStaticName).append("[").append(cnt)
										.append("++] = 1+").append(idx).append(";").append(NL);

								invertedParamNames.add(inverseMatName);
								invertedParamTypes.add(inverseMatType);
								// should enter here only once, checked by middle end
							} else
								throw new UndefinedTranslationException(ErrorMessage.UNSUPPORTED, curRoot);
						}
						// instead of a(:,1)=[] compute a = a(:,2:n)
						newComment(curRoot, "instead of " + curRoot.name() + " compute equivalently " + lhsNode.name()
								+ " = " + lhsNode.name() + "(<complementary params>)");
						newTranslation(curRoot, result.toString());
						result = new StringBuffer();
						translateMatrixAccess(curRoot, lhsNode, outputNodeName, lhsNode, lhs_symbols.get(0), null,
								invertedParamNames, invertedParamTypes, null, true);

						lhsNodeParams = new ArrayList<>();
						rhs = new AASTNode(curRoot.compilationUnit(), NodeType.ID, curRoot.name(), curRoot.lineNumber(),
								curRoot.colNumber(), curRoot);
						IntType[] rhsDims = new IntType[dtype.dims().length];
						int i = 0;
						for (IntType d : dtype.dims())
							rhsDims[i++] = (IntType) GType.get(d).name(TypeUtils.matrixDimName(outputNodeName, i));
						GType rhsType = GType.get(BType.MATRIX, outputNodeName, dtype.of(), rhsDims);
						rhs.expr(rhsType);
						rhs_symbol = outputNodeName;
					} else {
						lhsNodeParams = lhs_params.get(lhsNode);
					}
					result.append(translateAssign(curRoot, lhsNode, lhs_symbols.get(0), lhs_params_translation,
							lhsNodeParams, rhs, rhs_symbol));
					newComment(curRoot);
					newTranslation(curRoot, result.toString());

				} else {
					// get the right hand side dimensions as string if the right hand side is a
					// dimensiontype
					if (rhs.expr() instanceof DimensionType) {
						List<String> rhsDimsAsStrings = nodeDimsToStr(rhs, ((DimensionType) rhs.expr()).dims(), false);

						for (int i = 0; i < lhs.size(); ++i) {
							AASTNode lhs_part = lhs.get(i);

							// split rhs in ith part for lhs
							translateMatrixAccess(curRoot, lhs.get(i), lhs_symbols.get(i), rhs, rhs_symbol, null, null,
									(List<GType>) lhs_part.attr(NodeAttr.MATRIX_SLICES),
									lhs_symbols.toArray(new String[lhs_symbols.size()]), true);

						}
					} else if (rhs.expr() instanceof StructType) {
						StructType rhs_struct_type = ((StructType) rhs.expr());
						// check whether the lhs elements are as many as the rhs ones
						Iterator<Tuple<List<GType>, String>> structFieldsIt = rhs_struct_type.iterFields();
						List<AASTNode> funReturnNodes = null;
						if (rhs.type().equals(NodeType.APPLY) && rhs.hasAttr(NodeAttr.FUNCALL))
							funReturnNodes = curRoot.compilationUnit().functionSymbolNode(rhs.child(NodeType.ID))
									.childs(NodeType.FUNCTION_RETURN);

						if (lhs.size() != rhs_struct_type.numberOfFields())
							throw new UndefinedTranslationException(
									CErrorMessage.INTERNAL_STRUCT_ASSIGN_INVALID_LHS_NUMBER, curRoot, lhs.size(),
									rhs_struct_type.numberOfFields());

						String structName = rhs.symbol();
						for (int fieldCounter = 0; fieldCounter < lhs.size(); fieldCounter++) {
							// lhs element
							AASTNode lhs_part = lhs.get(fieldCounter);
							// struct field
							Tuple<List<GType>, String> field = structFieldsIt.next();

							if (lhs_part.type().equals(NodeType.VOID))
								// skip "don't care" assignment [~,a] = f(x)
								continue;

							boolean ret_input = (funReturnNodes != null)
									? funReturnNodes.get(fieldCounter).hasAttr(NodeAttr.IS_OUTPUT_INPUT)
									: false;
							List<GType> struct_field_types = field.first();
							String struct_field_name = field.second();

							if (!ret_input && lhs_part.expr() instanceof DimensionType) {
								Boolean notAllocated = false;
								for (int dimCounter = 0; dimCounter < ((DimensionType) lhs_part.expr())
										.dims().length; dimCounter++) {
									IntType dim = ((DimensionType) lhs_part.expr()).dims()[dimCounter];
									if (!dim.hasIntValue()) {
										notAllocated = true;
									}
								}

								if (notAllocated) {
									createVariable(lhs_part, true, VarVisibility.LOCAL_TO_FUNCTION);
									// it's been done inside the function
								}
							}

							GType struct_field_type = struct_field_types.get(0);
							if (!ret_input && struct_field_type.equals(BType.MATRIX)) {
								MatrixType mtype = (MatrixType) struct_field_type;
								assignWithMemcpy(result, mtype.dims().length,
										structName + STRUCT_POINTER_ACCESS + struct_field_name,
										lhs_symbols.get(fieldCounter), lhs_part, mtype.of());
							} else
								result.append(lhs_symbols.get(fieldCounter)).append(" = ").append(structName)
										.append(STRUCT_POINTER_ACCESS).append(struct_field_name).append(";").append(NL);
						}
						newComment(curRoot);
						newTranslation(curRoot, result.toString());
					}
				}
				return;
			case ELEMENTWISE_CCT:
			case TRANSPOSE: {
				child_results = getChildResults(results, curRoot.childs().size());
				String matrixTranslation = child_results.get(0);
				AASTNode matrixNode = curRoot.childs().get(0);

				GType matrixNodeExpr = getExprGeneralized(matrixNode);

				if (matrixNodeExpr.equals(BType.MATRIX_ACCESS_SLICE)) {
					// ignore transpose of slice
					results.push(matrixTranslation);
					return;
				} else if (!matrixNodeExpr.equals(BType.MATRIX))
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TRANSPOSE_OF_NONMATRIX, curRoot,
							matrixNodeExpr);
				IntType[] dims = ((MatrixType) matrixNodeExpr).dims();
				GType matrixTypeOf = ((MatrixType) matrixNodeExpr).of();
				AASTNode outNode = getOutputNode(curRoot);
				String lhsSymbol = outNode.symbol();
				if (dims == null || dims.length == 0)
					throw new UndefinedTranslationException(
							CErrorMessage.INTERNAL_TRANSPOSE_OF_MATRIX_WITHOUT_DIMENSIONS, curRoot);
				if (dims.length == 1) {
					StringBuffer sb = new StringBuffer();

					sb.append(lhsSymbol).append("=").append(matrixTranslation).append(";").append(NL);

					newComment(curRoot);
					newTranslation(curRoot, sb.toString());

					return;
				} else if (dims.length != 2) {
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TRANSPOSE_OF_NDIMENSIONAL_MATRIX,
							curRoot, matrixNodeExpr);
				}

				String prefix = "";
				if (matrixTypeOf.type().equals(BType.INT) || matrixTypeOf.type().equals(BType.BOOL)) {
					prefix = "i";
				} else if (matrixTypeOf.type().equals(BType.SCALAR)) {
					prefix = "d";
				} else {
					throw new UndefinedTranslationException(
							CErrorMessage.UNSUPPORTED_TRANSPOSE_OF_UNSUPPORTED_DERIVATE_TYPE, curRoot, matrixTypeOf);
				}

				StringBuffer sb = new StringBuffer();
				String dim1 = TypeUtils.matrixDimName(matrixTranslation, 1);
				String dim2 = TypeUtils.matrixDimName(matrixTranslation, 2);
				MatrixType mtype = (MatrixType) matrixNodeExpr;
				String sparseMod = "";
				if (!mtype.isSparse()) {
					// sparse transposition allocates out matrix inside function
					ensureMatrixMemory(sb, outNode, null, mtype, new String[] { dim2, dim1 });
					if (matrixTranslation.equals(lhsSymbol)) {
						// if like K = K' use transposeInplace
						// keep dimensions swapped because ensureMatrixMemory will swap dimensions (and
						// poly_basis)
						sb.append(prefix).append("TransposeMatrixInplace(" + lhsSymbol + STRUCT_ACCESS + "matrix, "
								+ dim2 + ", " + dim1 + ");").append(NL);
						if (isStaticAllocation((MatrixType) matrixNodeExpr) && outNode.parentExists(NodeType.LHS)
								&& curRoot.parentExists(NodeType.RHS)
								&& outNode.name().equals(getIDNode(curRoot).name())) {
							// if dimension of outNode is known, ensurememory won't change anything.
							// furthermore if the code is K=K' the static allocation allocated the
							// dimensions
							// for K (not K') so we need to swap the dimensions here
							String swapVar = "_" + lhsSymbol + "_swap";
							createVariable(GType.get(BType.INT), swapVar, NodeType.ID, null, false,
									VarVisibility.LOCAL_TO_BLOCK.context(curRoot));
							sb.append(swapVar).append(" = ").append(dim1).append(";").append(NL);
							sb.append(dim1).append(" = ").append(dim2).append(";").append(NL);
							sb.append(dim2).append(" = ").append(swapVar).append(";").append(NL);
						}
					} else {
						// here we assume that matrixTranslation is a variable and not an expression
						sb.append(prefix).append("TransposeMatrix(" + lhsSymbol + STRUCT_ACCESS + "matrix, "
								+ matrixTranslation + STRUCT_ACCESS + "matrix, " + dim1 + ", " + dim2 + ");")
								.append(NL);
					}
				} else {
					// sparse matrix
					if (matrixTranslation.equals(lhsSymbol))
						// transpose inplace
						throw new TypeException(CErrorMessage.USER_CLI_PARAM_EXCEPTION, curRoot, 1, matrixTranslation);
					sb.append(prefix).append("TransposeSparseMatrix(&" + lhsSymbol + ", &" + matrixTranslation + ");")
							.append(NL);
				}

				newComment(curRoot);
				newTranslation(curRoot, sb.toString());
				results.push(lhsSymbol);
				return;
			}
			case MATRIX:
				child_results = getChildResults(results, curRoot.childs().size());
				// translate only top-level matrices, unless multiple_assign
				if (curRoot.childs().size() == 1 && curRoot.childs().get(0).hasAttr(CAttr.VOID_VECTOR)) {
					AASTNode outNode = getOutputNode(curRoot);
					String mname = getNodeSymbol(outNode);
					MatrixType mtype = (MatrixType) getExprGeneralized(outNode);

					// write initialization
					String[] defaultParameters = new String[mtype.dims().length];
					for (int d = 0; d < mtype.dims().length; ++d)
						defaultParameters[d] = "0";
					StringBuffer sb = new StringBuffer();
					ensureMatrixMemory(sb, outNode, null, mtype, defaultParameters);
					if (curRoot.parentExists(new NodeType[] { NodeType.ASSIGN, NodeType.GASSIGN }))
						newComment(curRoot.parent(new NodeType[] { NodeType.ASSIGN, NodeType.GASSIGN }));
					newTranslation(curRoot, sb.toString());
					// the result is the name of the empty vector
					results.push(mname);
				} else if (curRoot.hasAttr(NodeAttr.TRANSLATE) && (Boolean) curRoot.attr(NodeAttr.TRANSLATE)) {

					AASTNode outNode = getOutputNode(curRoot);
					String mname = getStructFreeNodeSymbol(outNode);
					// write initialization
					createVariable(outNode);
					// create symbol to update his values
					// 1D access
					// cols
					String curColIdx = "__i" + mname;
					VarVisibility context = VarVisibility.LOCAL_TO_BLOCK.context(curRoot);
					createVariable(GType.get(BType.INT), curColIdx, NodeType.ID, null, false, context);
					String curRowIdx = "__r" + mname;
					// rows
					createVariable(GType.get(BType.INT), curRowIdx, NodeType.ID, null, false, context);
					StringBuffer initialization = new StringBuffer();
					initialization.append(curColIdx).append(" = 0;").append(NL);
					initialization.append(curRowIdx).append(" = 0;").append(NL);

					if (curRoot != outNode && !(Boolean) curRoot.attr(NodeAttr.FIXED_MATRIX)) {
						// first of all copy the actual childrenmatrixdimension
						// to a temporary variable
						// (to avoid that ensureMatrixMemory(), called in MATRIX node changes it)
						List<String> strdims = nodeDimsToInternalReference(outNode);

						String rows = "original_rows_" + mname;
						String cols = "original_cols_" + mname;

						createVariable(GType.get(BType.INT), rows, NodeType.ID, null, false, context);
						initialization.append(rows).append(" = ").append(strdims.get(0)).append(";").append(NL);
						if (strdims.size() > 1) {
							createVariable(GType.get(BType.INT), cols, NodeType.ID, null, false, context);
							initialization.append(cols).append(" = ").append(strdims.get(1)).append(";").append(NL);
						}
					}
					translateComment(curRoot, curRoot.name());
					newTranslation(curRoot, initialization.toString());

					MatrixType mtype = (MatrixType) getExprGeneralized(outNode);
					GType ofType = mtype.of();
					String[] outDims = new String[mtype.dims().length];
					// if this matrix is in a structure, consider it as to be allocated dynamically
					Boolean hasAllDims = !outNode.type().equals(NodeType.FIELDACCESS)
							&& !outNode.parentExists(NodeType.FIELDACCESS);
					// dimensions of matrix
					for (int count = 0; count < mtype.dims().length; count++) {
						outDims[count] = TypeUtils.matrixDimName(mname, count + 1);
						if (!mtype.dims()[count].hasValue())
							hasAllDims = false;
					}
					Boolean[] one_dimensional_as_row = (Boolean[]) curRoot
							.attr(NodeAttr.ONE_DIMENSIONAL_VECTORS_INTERPRETATION);
					// dimensions of subvectors
					// ROWS
					List<String> rowDimensions = new ArrayList<String>(2);
					for (int i = 0; i < curRoot.childs().size(); ++i) {
						AASTNode vector = curRoot.childs().get(i);
						if (vector.hasAttr(CAttr.VOID_VECTOR)
								|| vector.expr() != null && vector.expr().equals(BType.VOID))
							continue;
						// we look only the first, assuming the dimensions are coherent (checked by
						// middle-end)
						GType t = ((List<GType>) vector.attr(NodeAttr.VECTOR_TYPES)).get(0);
						List<AASTNode> vectorNodes = (List<AASTNode>) vector.attr(NodeAttr.VECTOR_VALUES);
						boolean assign_to_self = (vectorNodes.size() == 1
								&& vectorNodes.get(0).hasAttr(NodeAttr.DYNAMIC_ID)
								&& outNode.symbol().equals(vectorNodes.get(0).symbol()));
						if (ofType.canRepresent(t))
							rowDimensions.add("1");
						else if (assign_to_self)
							// row dimension is dynamic add symbol
							rowDimensions
									.add(((DimensionType) getExprGeneralized(vectorNodes.get(0))).dims()[ROW_DIMENSION]
											.valueAsString());
						else if (t instanceof DimensionType) {
							DimensionType submatrix = (DimensionType) t;
							IntType[] rdims = TypeUtils.realDims(submatrix.dims());
							if (rdims.length == 1) {
								// check how we should interpret 1d matrices
								if (one_dimensional_as_row[i])
									rowDimensions.add("1");
								else
									rowDimensions.add(rdims[0].valueAsString());
							} else
								rowDimensions.add(submatrix.dims()[ROW_DIMENSION].valueAsString());
						} else
							throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_MATRIX_ELEMENT_TYPE,
									curRoot, t, mtype);
					}

					// COLUMNS, SKIP EMPTY LINES (COMPOSED OF EMPTY VECTORS)
					String[] columnDimensions = null;
					for (int j = 0; j < curRoot.childs().size(); ++j) {
						AASTNode firstVector = curRoot.childs().get(j);
						if (firstVector.hasAttr(CAttr.VOID_VECTOR)
								|| firstVector.expr() != null && firstVector.expr().equals(BType.VOID))
							continue;
						List<GType> firstVectorTypes = (List<GType>) firstVector.attr(NodeAttr.VECTOR_TYPES);
						List<AASTNode> firstVectorNodes = (List<AASTNode>) firstVector.attr(NodeAttr.VECTOR_VALUES);
						boolean assign_to_self = (firstVectorNodes.size() == 1
								&& firstVectorNodes.get(0).hasAttr(NodeAttr.DYNAMIC_ID)
								&& outNode.symbol().equals(firstVectorNodes.get(0).symbol()));
						columnDimensions = new String[firstVectorTypes.size()];
						if (firstVectorTypes.size() == 1 && (firstVectorTypes.get(0).isEmptyMatrix() || assign_to_self))
							// skip vectors with just an empty matrix as only element
							// case [[]; a]
							continue;

						for (int i = 0; i < firstVectorTypes.size(); ++i) {
							GType t = firstVectorTypes.get(i);
							if (ofType.canRepresent(t))
								columnDimensions[i] = "1";
							else if (t instanceof DimensionType) {
								DimensionType submatrix = (DimensionType) t;
								// check how 1D vectors should be handled
								IntType[] rdims = TypeUtils.realDims(submatrix.dims());
								if (rdims.length == 1)
									if (one_dimensional_as_row[j])
										columnDimensions[i] = rdims[0].valueAsString();
									else
										columnDimensions[i] = "1";
								else {
									columnDimensions[i] = submatrix.dims()[COL_DIMENSION].valueAsString();
								}
							} else
								throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_MATRIX_ELEMENT_TYPE,
										curRoot, t, mtype);
						}
						break;
					}
					// if(columnDimensions.length==1 )

					// -- DYNAMIC ALLOCATION --
					if (!hasAllDims) {
						String columnDimension = String.join("+", columnDimensions);
						String rowDimension = String.join("+", rowDimensions);
						// other dimensions
						String[] dimensions = new String[mtype.dims().length];
						dimensions[ROW_DIMENSION] = rowDimension;
						dimensions[COL_DIMENSION] = columnDimension;
						// start from 2 because we skip row/col dimensions
						for (int j = 2; j < mtype.dims().length; ++j)
							dimensions[j] = mtype.dims()[j].valueAsString();

						StringBuffer allocatingBuffer = new StringBuffer();
						ensureMatrixMemory(allocatingBuffer, outNode, dimensions);
						newTranslation(curRoot, allocatingBuffer.toString());
					}

					for (String row : child_results) {
						// ROWS that comes from inner matrices
						String[] innerrows = row.split("\\" + MATRIX_SEPARATOR);
						for (String innerrow : innerrows)
							newTranslation(curRoot, String.join(" ", innerrow.split("\\" + VECTOR_SEPARATOR)));
					}
					results.push(mname);
				} else {
					// matrix of matrix
					results.push(String.join(MATRIX_SEPARATOR, child_results));
				}
				return;
			case VECTOR:
				child_results = getChildResults(results, curRoot.childs().size());

				if (curRoot.hasAttr(NodeAttr.VECTOR_VALUES)) {

					AASTNode vmatrix;
					boolean matrixOfMatrix = false;
					if (curRoot.parentExists(NodeType.VECTOR)) {
						int vectorDepth = curRoot.parentDepth(NodeType.VECTOR);
						if (curRoot.parentExists(TypeUtils.expressionNodes(), vectorDepth)
								|| curRoot.parentExists(NodeType.APPLY, vectorDepth))
							// there is an expression in between, translate this intermediate vector
							vmatrix = curRoot.parent();
						else {
							// just matrix of matrix, translate top-level matrix only
							vmatrix = getTopMatrix(curRoot);
							matrixOfMatrix = true;
						}
					} else
						// no parent matrix
						vmatrix = curRoot.parent();
					// the parent matrix
					AASTNode pmatrix = curRoot.parent(NodeType.MATRIX);
					// one boolean for each vector
					Boolean[] one_dimensional_as_row_by_vector = (Boolean[]) pmatrix
							.attr(NodeAttr.ONE_DIMENSIONAL_VECTORS_INTERPRETATION);
					// get position of this vector in matrix
					Boolean one_dimensional_as_row = one_dimensional_as_row_by_vector[childPosition(pmatrix, curRoot)];
					MatrixType vectorType = (MatrixType) getExprGeneralized(curRoot);
					AASTNode outputNode = getOutputNode(vmatrix);
					String vsymbol = getNodeSymbol(outputNode);
					String plainvsymbol = getStructFreeNodeSymbol(outputNode);
					String curColIdx = "__i" + plainvsymbol;
					String curRowIdx = "__r" + plainvsymbol;

					// if the matrix hasn't the dimensions as INT it hasn't been allocated,
					// you'd better do it now
					MatrixType vMatrixType = (MatrixType) getExprGeneralized(vmatrix);
					GType vofType = vMatrixType.of();
					IntType[] vMatrixTypeDims = vMatrixType.dims();
					List<AASTNode> children = curRoot.childs();

					List<AASTNode> values = (List<AASTNode>) curRoot.attr(NodeAttr.VECTOR_VALUES);
					// true if submatrix is explicit like [[1,2],[3,4]]
					// false if submatrix is implicit like [a,b] where a,b: matrix
					boolean symbolicMatrices = false;

					for (AASTNode value : values)
						symbolicMatrices |= !value.type().equals(NodeType.MATRIX);

					// in this case we assume the matrix is 1D
					boolean mixedValues = (Boolean) curRoot.attr(NodeAttr.VECTOR_MIXED_VALUES);
					boolean matrixValues = (Boolean) curRoot.attr(NodeAttr.VECTOR_MATRIX_VALUES);
					List<String> vectorCellTranslation = new ArrayList<String>();

					if (mixedValues || (matrixValues && symbolicMatrices)) {

						List<GType> vector_parts = (List<GType>) curRoot.attr(NodeAttr.VECTOR_TYPES);
						List<AASTNode> vector_nodes = (List<AASTNode>) curRoot.attr(NodeAttr.VECTOR_VALUES);

						String[] columnDimensions = new String[child_results.size()];
						List<String> matrixDimensions = null;
						int memcpyCnt = 0;
						// --- ITERATION ON ELEMENTS OF THIS VECTOR -----
						for (int i = 0; i < child_results.size(); ++i) {
							AASTNode curChild = getExprNode(curRoot.childs().get(i));
							if (vector_parts.get(i).isCastableToScalar()) {
								// update variables that keep track of the part of the matrix we are filling
								vectorCellTranslation.add(vsymbol + STRUCT_ACCESS + "matrix[" + curColIdx + "++] = "
										+ child_results.get(i) + ";");
								columnDimensions[i] = "1";
							} else if (vector_parts.get(i) instanceof SliceType) {
								// don't use nodedimstostr here because it will max() between an 1xN and Nx1
								// matrix
								// not desiderable here
								List<String> childrenSliceDimensions = nodeDimsToSymbolicReference(curChild);
								if (matrixDimensions == null)
									matrixDimensions = childrenSliceDimensions;

								StringBuffer sb = new StringBuffer();
								String ncols = TypeUtils.matrixDimName(vmatrix, 2);

								columnDimensions[i] = TypeUtils.matrixDimName(vector_nodes.get(i), 1);
								if (one_dimensional_as_row) {
									// interpret as row

									sb.append("#pragma omp smid").append(NL);
									sb.append("for(int j=0; j<" + columnDimensions[i] + "; j++)" + NL);
									sb.append(TAB + vsymbol + STRUCT_ACCESS + "matrix[" + curRowIdx + "*" + ncols
											+ " + " + curColIdx + " + j] = " + getSequenceAccess(children.get(i), "j")
											+ ";" + NL);

									// update variables that keep track of the part of the matrix we are filling
									if (i == child_results.size() - 1) {
										sb.append(curRowIdx).append("+= 1").append(";").append(NL);
										// re-initialize column to 0 because the vector is finished
										sb.append(curColIdx).append("= 0").append(";").append(NL);
									} else
										sb.append(curColIdx).append("+=").append(columnDimensions[i]).append(";")
												.append(NL);
								} else {
									// interpret as column

									sb.append("#pragma omp smid").append(NL);
									sb.append("for(int j=0; j<" + columnDimensions[i] + "; j++)" + NL);
									sb.append(TAB + vsymbol + STRUCT_ACCESS + "matrix[(" + curRowIdx + "+j)*" + ncols
											+ " + " + curColIdx + "] = " + getSequenceAccess(children.get(i), "j") + ";"
											+ NL);

									// update variables that keep track of the part of the matrix we are filling
									if (i == child_results.size() - 1) {
										sb.append(curRowIdx).append("+= ").append(columnDimensions[i]).append(";")
												.append(NL);
										// re-initialize column to 0 because the vector is finished
										sb.append(curColIdx).append("= 0").append(";").append(NL);
									} else {
										sb.append(curColIdx).append("+= 1;").append(NL);
									}
								}

								vectorCellTranslation.add(sb.toString());
							} else {
								DimensionType curType = (DimensionType) vector_parts.get(i);
								// if this vector is of elements different from the top-level matrix
								// this is an incompatible assignment -> switch to for
								boolean dynamic_id = curChild.hasAttr(NodeAttr.DYNAMIC_ID);
								boolean incompatibleElement = !dynamic_id
										&& !curType.of().type().equals(vofType.type());
								String ofType = exprTypeToCType(curType.of());
								String vofTypeAsString = exprTypeToCType(vofType);
								IntType[] dims = curType.dims();
								// don't use nodedimstostr here because it will max() between an 1xN and Nx1
								// matrix
								// not desiderable here
								List<String> childrenMatrixDimensions = nodeDimsToSymbolicReference(curChild);
								// a complex element is an element with an expression in it [1 2 2*x+[1 2 3] 2
								// 3]
								// this means that it is passed through the expressionNodes and thus do have at
								// least
								// a ".matrix[EXPR_INDEX]" access. We need to substitute EXPR_INDEX with the
								// actual index
								AASTNode vectorElemNode = ((AASTNode) TypeUtils.getIDNode(vector_nodes.get(i)));
								boolean complexElement = vectorElemNode.hasType(TypeUtils.expressionNodes())
										&& !vectorElemNode.symbol().equals(child_results.get(i));
								if (!mixedValues && dims.length == 1) {
									// if mixed value assume 1D matrix is row vector
									// not mixed at least 2 dimensions are needed in the code below:
									// add extra '1' dimension so that the code will assume column vector
									List<IntType> tmplist = new ArrayList<IntType>(2);
									tmplist.add(dims[0]);
									tmplist.add(new IntType(BType.INT, 1));
									dims = tmplist.toArray(new IntType[tmplist.size()]);
									childrenMatrixDimensions.add("1");
								}

								StringBuffer sb = new StringBuffer();
								sb.append(NL);
								// we are joining always the "column" dimension
								IntType rConstrainedDim, cConstrainedDim;
								String ncol, nrow;
								// decide the value of matrix row and column for this vector
								if (curChild.fullname().equals(outputNode.fullname())) {
									// if curChild is equal to out node we are enlarging the same matrix
									// a = [a; x]
									// the dimension is the original a dimension (before ensurememory)
									if (dims.length > 1) {
										ncol = "original_cols_" + plainvsymbol;
										nrow = "original_rows_" + plainvsymbol;
									} else {
										// assume 1xN vector, only for case of mixed scalar/vector
										// thanks to previous if
										ncol = "original_rows_" + plainvsymbol;
										nrow = "0";
									}
								} else {
									// normal case curchild is different from output node
									if (dims.length > 1) {
										ncol = childrenMatrixDimensions.get(COL_DIMENSION);
										nrow = childrenMatrixDimensions.get(ROW_DIMENSION);
									} else {
										// assume 1xN vector, only for case of mixed scalar/vector
										// thanks to previous if
										ncol = childrenMatrixDimensions.get(ROW_DIMENSION);
										nrow = "0";
									}
								}

								// if we are concatenating multiple matrices or matrices with scalars
								// if it's a simple vector take the vector dimension
								if (childrenMatrixDimensions.size() > 1) {
									if (mixedValues && (!dims[ROW_DIMENSION].hasValue()
											|| dims[ROW_DIMENSION].valueAsInt() != 1))
										throw new UndefinedTranslationException(
												CErrorMessage.UNSUPPORTED_MATRIX_DIMENSIONS_INCONSISTENT, curRoot);

									columnDimensions[i] = childrenMatrixDimensions.get(COL_DIMENSION);
									if (matrixDimensions == null)
										matrixDimensions = childrenMatrixDimensions;
								} else {
									// r vsymbol don't need to be updated here
									columnDimensions[i] = childrenMatrixDimensions.get(0);
								}

								if (dims == null || dims.length == 0)
									throw new TypeException(CErrorMessage.INTERNAL_MATRIX_VECTOR_DIMENSION_UNKNOWN,
											curRoot, curType);

								if (child_results.size() > 1 || complexElement || incompatibleElement) {
									int k = 0;
									List<Integer> onesDims = new ArrayList<Integer>(dims.length);
									// how many array entry we will fill in this code
									List<Integer> dimToBeAddedPos = new ArrayList<Integer>(dims.length);
									for (int w = 0; w < dims.length; ++w) {
										IntType dim = dims[w];
										// save in array the dims different from 1 (dims to be added)
										// and the dims '1' (onesDims)
										if (!dim.hasValue() || dim.valueAsInt() != 1) {
											dimToBeAddedPos.add(w);
										} else
											onesDims.add(w);
										// this should be done always (don't strip-out ones)
										// because otherwise join of column vector will become impossible [a b] a = nx1
										// b = nxm
										for (int t = 0; t < k; ++t)
											sb.append(TAB);
										// add N-1 for i0...iN-1

										String dimStringValue = childrenMatrixDimensions.get(w);
										sb.append("#pragma omp smid").append(NL);
										sb.append("for(int i" + k + "=1 ; i" + k + " <= " + dimStringValue + "; ++i" + k
												+ ")").append(NL);
										k++;
									}

									String[] rhs_access = new String[dims.length];
									String rhs_iter_symbol = null;
									if (!complexElement)
										rhs_iter_symbol = child_results.get(i);
									else
										rhs_iter_symbol = getIterNode(vector_nodes.get(i)).symbol();
									for (int ap = 0; ap < dims.length; ++ap)
										rhs_access[ap] = "(i" + ap + "-1) * " + rhs_iter_symbol + STRUCT_ACCESS
												+ "poly_basis[" + ap + "]";

									String[] lhs_access = new String[dims.length];
									for (int ap = 0; ap < dims.length; ++ap)
										if (ap == COL_DIMENSION)
											lhs_access[ap] = "(" + curColIdx + " + i" + ap + "-1) * " + vsymbol
													+ STRUCT_ACCESS + "poly_basis[" + ap + "]";
										else if (ap == ROW_DIMENSION)
											lhs_access[ap] = "(" + curRowIdx + " + i" + ap + "-1) * " + vsymbol
													+ STRUCT_ACCESS + "poly_basis[" + ap + "]";
										else
											lhs_access[ap] = "(i" + ap + "-1) * " + vsymbol + STRUCT_ACCESS
													+ "poly_basis[" + ap + "]";

									for (int t = 0; t < dims.length; ++t)
										sb.append(TAB);
									// rhs index
									String leftMatrixAccess = String.join(" + ", lhs_access);
									if (dimToBeAddedPos.size() > 1) {
										// matrix access (perhaps a matrix access should be performed here)
										sb.append(vsymbol + STRUCT_ACCESS + "matrix[" + leftMatrixAccess + "] = ");
										// lhs index
										String index = String.join(" + ", rhs_access);
										if (incompatibleElement)
											sb.append("(" + vofTypeAsString + ") ");

										if (!complexElement)
											sb.append(child_results.get(i)).append(STRUCT_ACCESS + "matrix")
													.append("[" + index + "];").append(NL);
										else
											// if complex this means this is a matrix/matrix pointwise operation
											// or a matrix scalar operation.
											// Since this piece of code ended up here instead of in a top-level
											// expression,
											// replace EXPR_INDEX with the actual index we are using
											sb.append(child_results.get(i).replaceAll(EXPR_INDEX, index)).append(";")
													.append(NL);
									} else {
										// optimize removing matrix access usage
										sb.append(vsymbol + STRUCT_ACCESS + "matrix[" + leftMatrixAccess + "] = ");
										String index = rhs_access[rhs_access.length
												- (dims.length - dimToBeAddedPos.get(0))];
										if (incompatibleElement)
											sb.append("(" + vofTypeAsString + ") ");
										if (!complexElement)
											sb.append(child_results.get(i)).append(STRUCT_ACCESS + "matrix")
													.append("[" + index + "];").append(NL);
										else
											sb.append(child_results.get(i).replaceAll(EXPR_INDEX, index)).append(";")
													.append(NL);
									}
								} else {
									// row bind, this is the only element of this vector
									// just use memcpy
									if (memcpyCnt != 0 || (!dynamic_id || !vsymbol.equals(child_results.get(i)))) {
										// actually do it just if source and target are not the same matrix
										// and this is the first memcopy
										String totalCellsCopied = String.join("*", childrenMatrixDimensions);

										sb.append("memcpy(")
												.append(vsymbol + STRUCT_ACCESS + "matrix + (" + ncol + " * "
														+ curRowIdx + ")")
												.append(", ").append(child_results.get(i) + STRUCT_ACCESS + "matrix")
												.append(", ").append(totalCellsCopied).append("*sizeof(" + ofType + ")")
												.append(")").append(";").append(NL);
										memcpyCnt++;
									} else {
										sb.append("/* skip memcpy of itself */").append(NL);
									}
								}

								// update variables that keep track of the part of the matrix we are filling
								if (i == child_results.size() - 1) {
									if (!nrow.equals("0"))
										sb.append(curRowIdx).append("+=").append(nrow).append(";").append(NL);
									// re-initialize column to 0 because the vector is finished
									sb.append(curColIdx).append("= 0").append(";").append(NL);
								} else
									sb.append(curColIdx).append("+=").append(ncol).append(";").append(NL);

								vectorCellTranslation.add(sb.toString());
							}
						}

						// elements on the same line separated by vector_separator
						results.push(String.join(VECTOR_SEPARATOR, vectorCellTranslation));
					} else if (!matrixValues) {
						// only scalars
						IntType[] vectorDims = vectorType.dims();
						// choose if we need to use curColIdx or curRowIdx
						boolean degeneratedVector = TypeUtils.isDegeneratedMatrix(vectorType);
						boolean rowMatrix = (vMatrixTypeDims.length == 1
								|| (vMatrixTypeDims[0].hasValue() && vMatrixTypeDims[0].valueAsInt().equals(1)));
						boolean rowVector = (vectorDims.length == 1
								|| vectorDims[0].hasIntValue() && vectorDims[0].valueAsInt().equals(1));
						if (!degeneratedVector && rowVector || degeneratedVector && rowMatrix)
							for (int i = 0; i < child_results.size(); ++i)
								vectorCellTranslation.add(vsymbol + STRUCT_ACCESS + "matrix[" + curColIdx + "++] = "
										+ child_results.get(i) + ";");
						else
							// use row index
							for (int i = 0; i < child_results.size(); ++i)
								vectorCellTranslation.add(vsymbol + STRUCT_ACCESS + "matrix[" + curRowIdx + "++] = "
										+ child_results.get(i) + ";");

						// elements on the same line separated by vector_separator
						results.push(String.join(VECTOR_SEPARATOR, vectorCellTranslation));
					} else {
						// only explicit matrices. array represent rows (fixed to
						// the number
						// of rows of the first matrix)
						String[] bigMatrix = new String[2 * child_results.get(0).split("\\" + MATRIX_SEPARATOR).length];
						// i = row we are considering
						for (int i = 0, j = 0; i < bigMatrix.length; i += 2, ++j) {
							bigMatrix[i] = new String();
							for (String submatrix : child_results) {
								String[] submatrixrows = submatrix.split("\\" + MATRIX_SEPARATOR);
								if (!"".equals(bigMatrix[i]))
									bigMatrix[i] += VECTOR_SEPARATOR;
								// take first element of each submatrix and compose
								// a single row
								bigMatrix[i] += submatrixrows[j].replaceAll(curRowIdx, curColIdx);
							}
							// add row
							bigMatrix[i + 1] = curRowIdx + "++;";
						}
						results.push(String.join(MATRIX_SEPARATOR, bigMatrix));
					}
				} else {
					// do not translate a matrix -> case LHS
					results.push(String.join(LHS_VECTOR_SEPARATOR, child_results));
					// PLUS if empty vector, push back the translation readed, and add the
					// translation of the empty vector
					if (curRoot.childs().size() == 1 && curRoot.childExists(NodeType.VOID)) {
						// vector of 0 elements
						// translation of content of vector
						curRoot.attr(CAttr.VOID_VECTOR, true);
						results.push("");
					}
				}

				return;
			case FIELDACCESS:
				AASTNode baseStruct = curRoot.childs().get(0);
				GType baseStrType = getExprGeneralized(baseStruct);
				String outputSymbol = null;
				// create variable for base of struct
				boolean created = createVariable(curRoot.childs().get(0));
				// exclude dots
				int n_res = 0;
				for (int r = 0; r < curRoot.childs().size(); ++r)
					if (!curRoot.childs().get(r).type().equals(NodeType.DOT)) {
						n_res++;
					}
				child_results = getChildResults(results, n_res);
				// output
				outputSymbol = String.join(STRUCT_ACCESS, child_results);

				if (baseStrType.isInput()) {
					StringBuffer bufToTranslate = new StringBuffer();

					// I have to take the first two fields in the result stack: the first one
					// identifies the field to access, the second one the struct

					String fieldName = child_results.get(1);

					accessToInputStruct(bufToTranslate, outputSymbol, fieldName, baseStrType.inputName());
					// append a new translation
					newComment(curRoot,
							"Getting the input column with name " + fieldName + " at line " + curRoot.lineNumber());
					newTranslation(curRoot, bufToTranslate.toString());

				}

				// push into the results stack the name of the matrix I've
				// just gotten
				results.push(outputSymbol);
				break;
			case HOLD:
				child_results = getChildResults(results, curRoot.childs().size() - 1);
				// emit warning not implemented
				curRoot.compilationUnit()
						.addWarning(new UndefinedTranslationException(CErrorMessage.WARN_VOID_IMPLEMENTATION, curRoot));
			case DOT:
				break;
			default:
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_NODE, curRoot);
			}

		} catch (Exception e) {
			if (e instanceof GException)
				throw e;
			else {
				GException ex = new GException(CErrorMessage.INTERNAL_TREE_WALK_EXCEPTION, curRoot);
				logger.debug(ex.stringify() + ": " + e.getMessage(), e);
				throw ex;
			}
		}
	}

	/**
	 * get position child node (vector) inside parent list of children (matrix)
	 * excluding void vectors and void nodes.
	 * 
	 * @param parent
	 * @param child
	 * @return
	 */
	private int childPosition(AASTNode parent, AASTNode child) {
		int cnt = 0;
		for (AASTNode c : parent.childs()) {
			if (c.equals(child))
				return cnt;
			else if (!c.hasAttr(CAttr.VOID_VECTOR) && !c.type().equals(NodeType.VOID))
				// count only childs non-void
				cnt++;
		}
		return -1;
	}

	private boolean isPointwiseOperation(NodeType type) {
		return type.equals(NodeType.ELEMENTWISE_TIMES) || type.equals(NodeType.ELEMENTWISE_LEFTDIV)
				|| type.equals(NodeType.ELEMENTWISE_RIGHTDIV) || type.equals(NodeType.ELEMENTWISE_EXP)
				|| type.equals(NodeType.PLUS) || type.equals(NodeType.MINUS);
	}

	private void assignWithMemcpy(StringBuffer translation, int tmp_dims_length, String tmpSymbol, String outNodeName,
			AASTNode outNode, GType ofType) throws TypeException, UndefinedTranslationException {
		String[] dimsStr = new String[tmp_dims_length];
		for (int d = 1; d <= tmp_dims_length; ++d)
			dimsStr[d - 1] = TypeUtils.matrixDimName(tmpSymbol, d);
		ensureMatrixMemory(translation, outNode, dimsStr);
		translation.append("memcpy(" + outNodeName + STRUCT_ACCESS + "matrix, " + tmpSymbol + STRUCT_ACCESS + "matrix, "
				+ String.join(" * ", dimsStr) + " * sizeof(" + exprTypeToCType(ofType) + "));" + NL);
	}

	private Tuple<String, StringBuffer> getConditionString(AASTNode curRoot, List<String> child_results)
			throws UndefinedTranslationException, TypeException {
		Tuple<String, StringBuffer> condition = null;
		if (child_results.size() > 0) {
			String firstExpression = null;

			firstExpression = child_results.get(0);

			// check whether it's a real expression or a variable
			// (matlab means == NULL as testing a variable)
			AASTNode expressionNode = curRoot.child(NodeType.EXPRESSION);
			AASTNode coreExprNode = getIDNode(expressionNode);
			GType expressionType = coreExprNode.expr();

			condition = new Tuple<>(firstExpression, null);
			// int/bool are left as they are
			if (!expressionType.isCastableToBool()) {
				// case matrices
				if (!expressionType.equals(BType.MATRIX))
					// if matrix of scalar to be implemented
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_CONTROL_STATEMENT_CONDITION,
							curRoot, coreExprNode.code(), expressionType);
				else if (!((MatrixType) expressionType).of().isCastableToBool())
					// if matrix of scalar to be implemented
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_CONTROL_STATEMENT_CONDITION,
							curRoot, coreExprNode.code(), ((MatrixType) expressionType).of());
				else if (!TypeUtils.isDegeneratedMatrix(expressionType)) {
					condition = getConditionalCondition(curRoot, expressionType, firstExpression);

					if (condition.first() == null)
						throw new UndefinedTranslationException(
								CErrorMessage.INTERNAL_CONTROL_STATEMENT_CONDITION_UNDEFINED, curRoot);
				} else if (coreExprNode.type().equals(NodeType.APPLY)
						&& coreExprNode.symbol().equals(firstExpression)) {
					// case if( a(b) )
					// where a(b) is a degenerate matrix, add .matrix[0]
					condition = new Tuple<>(condition.first() + STRUCT_ACCESS + "matrix[0]", null);
				}
			}

		}
		return condition;
	}

	private AASTNode getTopMatrix(AASTNode curRoot) {
		// parent of vector is always matrix.
		AASTNode vmatrix = curRoot.parent();
		// take the topmost matrix
		while (vmatrix.parentExists(NodeType.MATRIX))
			vmatrix = vmatrix.parent(NodeType.MATRIX);
		return vmatrix;
	}

	/**
	 * C macro that checks for nullness of this node type
	 * 
	 * @param p
	 * @return
	 * @throws TypeException
	 * @throws UndefinedTranslationException
	 */
	private String getNullCheckMacro(AASTNode p) throws TypeException, UndefinedTranslationException {
		return getNullableTypeName(p, "").toUpperCase() + "_IS_NULL";
	}

	/**
	 * C macro that checks for emptyness of this node type
	 * 
	 * @param p
	 * @return
	 * @throws TypeException
	 * @throws UndefinedTranslationException
	 */
	private String getEmptyCheckMacro(AASTNode p) throws TypeException, UndefinedTranslationException {
		return getEmptyCheckTypeName(p, "").toUpperCase() + "_IS_EMPTY";
	}

	/**
	 * value that corresponds to null for this type
	 * 
	 * @param curRoot
	 * @return
	 * @throws TypeException
	 * @throws UndefinedTranslationException
	 */
	private String getNullValue(AASTNode curRoot) throws TypeException, UndefinedTranslationException {
		return getNullableTypeName(curRoot, "Null");
	}

	private String getNullableTypeName(AASTNode curRoot, String suffix)
			throws TypeException, UndefinedTranslationException {
		GType expr = getExprGeneralized(curRoot);
		if (GType.get(BType.MATRIX_ACCESS_SLICE).equals(expr)) {
			SliceType t = (SliceType) expr;
			switch (t.of().type()) {
			case INT:
				return SLICE_TYPE_I + suffix;
			case SCALAR:
				return SLICE_TYPE_D + suffix;
			default:
				throw new TypeException(CErrorMessage.TODO_INTERNAL_SLICE_TYPE, curRoot);
			}
		} else if (GType.get(BType.MATRIX).equals(expr) || expr.isCastableToScalar()) {
			return exprTypeToCType(expr).trim() + suffix;
		} else if (GType.get(BType.STRUCT).equals(expr)) {
			StructType t = (StructType) expr;
			return getStructName(t) + suffix;
		} else
			throw new TypeException(CErrorMessage.UNSUPPORTED_TYPE, curRoot, expr);
	}

	private String getEmptyCheckTypeName(AASTNode curRoot, String suffix)
			throws TypeException, UndefinedTranslationException {
		GType expr = getExprGeneralized(curRoot);
		if (GType.get(BType.MATRIX_ACCESS_SLICE).equals(expr)) {
			SliceType t = (SliceType) expr;
			switch (t.of().type()) {
			case INT:
				return SLICE_TYPE_I + suffix;
			case SCALAR:
				return SLICE_TYPE_D + suffix;
			default:
				throw new TypeException(CErrorMessage.TODO_INTERNAL_SLICE_TYPE, curRoot);
			}
		} else if (GType.get(BType.MATRIX).equals(expr) || expr.isCastableToScalar()) {
			return exprTypeToCType(expr).trim() + suffix;
		} else {
			throw new TypeException(CErrorMessage.UNSUPPORTED_TYPE, curRoot, expr);
		}
	}

	private String findInputVar(StringBuffer bufToTranslate, String fieldName, String structName) {
		String columnVariableName = Symbols.getSymbolFromType("INPUT_COLUMN", BType.STRUCT);
		bufToTranslate.append("INPUT_COLUMN* ").append(columnVariableName).append("=").append(structName)
				.append(STRUCT_POINTER_ACCESS).append("getColumn(").append(structName).append(", \"").append(fieldName)
				.append("\");").append(NL);
		return columnVariableName;
	}

	private void assignMatrixFromInputStruct(StringBuffer bufToTranslate, String columnVariableName,
			String outputSymbol, String fieldName) {
		bufToTranslate.append(outputSymbol).append(STRUCT_ACCESS).append("matrix=").append(columnVariableName)
				.append(STRUCT_POINTER_ACCESS).append("values").append(";").append(NL);
	}

	private void assignMatrixStructureFromInputStruct(StringBuffer bufToTranslate, String columnVariableName,
			String outputSymbol, String fieldName) {
		String dim = TypeUtils.matrixDimName(outputSymbol, 1);
		bufToTranslate.append(dim).append("=").append(columnVariableName).append(STRUCT_POINTER_ACCESS)
				.append("columnLenght;").append(NL);
		String staticPolyBasisName = getStaticMatrixPolyBasisName(outputSymbol);
		bufToTranslate.append("static __thread ").append("int").append(" ").append(staticPolyBasisName)
				.append("[1] = {1};").append(NL);
		bufToTranslate.append(outputSymbol + STRUCT_ACCESS + "poly_basis").append("=").append(staticPolyBasisName)
				.append(";").append(NL);
		bufToTranslate.append(outputSymbol + STRUCT_ACCESS + "__realsize").append("=").append(dim).append(";")
				.append(NL);
	}

	private void accessToInputStruct(StringBuffer bufToTranslate, String outputSymbol, String fieldName,
			String structName) {
		// gets the index inside the struct (created as a CSV pointer)
		String columnVariableName = findInputVar(bufToTranslate, fieldName, structName);
		assignMatrixFromInputStruct(bufToTranslate, columnVariableName, outputSymbol, fieldName);
		assignMatrixStructureFromInputStruct(bufToTranslate, columnVariableName, outputSymbol, fieldName);
	}

	/**
	 * param types can be casted (ex 1dmatrix can be casted to 2dmatrix)
	 * 
	 * @param curRoot
	 * @param translatedParams
	 * @throws UndefinedTranslationException
	 * @throws CastException
	 * @throws TypeException
	 */
	private void adjustParamTypes(AASTNode curRoot, String[] translatedParams)
			throws UndefinedTranslationException, CastException, TypeException {
		AASTNode funIdNode = curRoot.child(NodeType.ID);
		FunctionType funType = null;
		if (funIdNode.hasAttr(NodeAttr.REF_FUNCTION)) {
			funType = (FunctionType) ((AASTNode) funIdNode.attr(NodeAttr.REF_FUNCTION)).expr();
		} else
			funType = (FunctionType) funIdNode.expr();
		List<GType> funParamType = funType.inputs();
		List<AASTNode> params = curRoot.childs(NodeType.FUNCTION_PARAMETER_LIST);
		GType[] funActualParamType = new GType[params.size()];
		for (int i = 0; i < params.size(); ++i) {
			AASTNode param = params.get(i);
			funActualParamType[i] = getExprGeneralized(param);
		}

		checkFunctionTypes(getCurrentTranslationBuffer(curRoot).second(), curRoot, funParamType, funActualParamType,
				translatedParams);
	}

	private void checkFunctionTypes(StringBuffer sb, AASTNode curRoot, List<GType> funFormalParamTypes,
			GType[] funActualParamTypes, String[] translatedParams)
			throws CastException, UndefinedTranslationException, TypeException {

		AASTNode funIdNode = curRoot.child(NodeType.ID);
		Iterator<GType> formalIt = funFormalParamTypes.iterator();
		for (int i = 0; formalIt.hasNext(); ++i) {
			GType formalptype = formalIt.next();
			if (formalptype.equals(BType.VOID))
				continue;
			else if (i >= funActualParamTypes.length) {
				String[] formalAsStr = new String[funFormalParamTypes.size()];
				Iterator<GType> formalNewIt = funFormalParamTypes.iterator();
				for (int f = 0; formalNewIt.hasNext(); ++f)
					formalAsStr[f] = formalNewIt.next().toString();
				String[] actualAsStr = new String[funActualParamTypes.length];
				for (int f = 0; f < funActualParamTypes.length; ++f)
					actualAsStr[f] = funActualParamTypes[f].toString();
				// just warning because it's possible in general that a user-def function is
				// called without parameters
				// but the actual function should check varargin variable to discriminate the
				// cases
				curRoot.compilationUnit().addWarning(new InputException(CErrorMessage.WARN_FUNCTION_MISSING_PARAMETERS,
						curRoot, funIdNode.name(), actualAsStr, formalAsStr));
				break;
			}
			GType actualptype = funActualParamTypes[i];

			if (formalptype != null && actualptype != null)
				if (formalptype.equals(BType.MATRIX) && actualptype.equals(BType.MATRIX)) {
					MatrixType fptype = (MatrixType) formalptype;
					MatrixType aptype = (MatrixType) actualptype;
					IntType[] fptypeOrigDims = fptype.dims();
					IntType[] aptypeOrigDims = aptype.dims();
					IntType[] fptypeRealDims = TypeUtils.realDims(fptypeOrigDims);
					IntType[] aptypeRealDims = TypeUtils.realDims(aptypeOrigDims);
					if (fptypeOrigDims.length != aptypeOrigDims.length) {
						List<String> newParamDimNames = new LinkedList<String>();
						// if the difference is only for '1' dimensions
						String newParamName = translatedParams[i] + "_" + funIdNode.name();
						// force static!
						createVariable(GType.get(fptype).name(newParamName), newParamName, null, null, false, false,
								VarVisibility.PERSISTENT, false);

						StringBuffer dimensionsBuffer = new StringBuffer();
						List<String> dimensions = new ArrayList<String>();
						if (fptypeOrigDims.length > aptypeOrigDims.length) {
							// needed larger matrix. create fictitious ones in the missing
							// positions
							int pos = 0;
							String paramDim;
							for (int j = 0; j < fptypeOrigDims.length; ++j) {
								paramDim = TypeUtils.matrixDimName(newParamName, j + 1);
								newParamDimNames.add(paramDim);
								if (j < aptypeOrigDims.length
										&& (!fptypeOrigDims[j].hasValue() || !fptypeOrigDims[j].value().equals(1))) {
									dimensionsBuffer.append(paramDim).append(" = ");
									dimensions.add(TypeUtils.matrixDimName(translatedParams[i], ++pos));
									dimensionsBuffer.append(dimensions.get(dimensions.size() - 1)).append(";");
									dimensionsBuffer.append(NL);
								} else {
									dimensionsBuffer.append(paramDim).append(" = 1;").append(NL);
									dimensions.add("1");
								}
							}

						} else {
							if (fptypeRealDims.length != aptypeRealDims.length) {
								// if == -> no problem: the difference is only in some bogous 1
								// if != then we are losing the dimensions that actualtype has more than formal
								// type
								// emit a warning
								TypeException ex = new TypeException(CErrorMessage.WARN_FUNCTION_MATRIX_SIZE_CAST,
										curRoot, translatedParams[i], curRoot.child(NodeType.ID).name(), fptype,
										aptype);
								curRoot.compilationUnit().addWarning(ex);
							}
							// needed a shorter matrix. create a fictitious matrix with less ones
							int pos = 0;
							String paramDim;
							// copy the dimensions needed
							for (int j = 0; j < aptypeOrigDims.length && pos < fptypeOrigDims.length; ++j) {
								if (!aptypeOrigDims[j].hasValue() || !aptypeOrigDims[j].value().equals(1)) {
									paramDim = TypeUtils.matrixDimName(newParamName, ++pos);
									newParamDimNames.add(paramDim);
									dimensionsBuffer.append(paramDim).append(" = ");
									dimensions.add(aptypeOrigDims[j].name());
									dimensionsBuffer.append(dimensions.get(dimensions.size() - 1));
									dimensionsBuffer.append(";").append(NL);
								}
							}
						}

						// bool/int -> int
						if (fptype.of().isCastableToBool() && aptype.of().isCastableToBool()
								// scalar -> double
								|| fptype.of().equals(BType.SCALAR) && aptype.of().equals(BType.SCALAR)) {
							sb.append(dimensionsBuffer.toString());
							sb.append(copyMatrixFields(newParamName, translatedParams[i]));
						} else {
							// for loop to adapt the two matrices, throw away dimensionsBuffer
							ensureMatrixMemory(sb, null, newParamName, fptype,
									dimensions.toArray(new String[dimensions.size()]), false, false, false, null);
							String dim = String.join("*", dimensions);
							String ofctype = exprTypeToCType(fptype.of());
							sb.append("#pragma omp smid").append(NL);
							sb.append("for(int " + EXPR_INDEX + "=0; " + EXPR_INDEX + " < " + dim + "; ++" + EXPR_INDEX
									+ ")").append(NL);
							sb.append(TAB).append(newParamName).append(".matrix[").append(EXPR_INDEX).append("] = (")
									.append(ofctype).append(") ").append(translatedParams[i]).append(".matrix[")
									.append(EXPR_INDEX).append("];").append(NL);
						}
						// change translated param
						translatedParams[i] = newParamName;
					} else if (!fptype.of().equals(aptype.of().type())) {
						curRoot.compilationUnit()
								.addWarning(new TypeException(CErrorMessage.WARN_FUNCTION_MATRIX_TYPE_CAST, curRoot,
										translatedParams[i], curRoot.child(NodeType.ID).name(), fptype, aptype));
						// if different types cast types
						// if the difference is only for '1' dimensions
						String newParamName = translatedParams[i] + "_" + funIdNode.name();
						// force static!
						createVariable(GType.get(fptype).name(newParamName), newParamName, null, null, false, false,
								VarVisibility.PERSISTENT, false);
						// for loop to adapt the two matrices, throw away dimensionsBuffer
						String[] dims = new String[fptype.dims().length];
						for (int k = 0; k < fptype.dims().length; ++k)
							dims[k] = TypeUtils.matrixDimName(translatedParams[i], k + 1);
						ensureMatrixMemory(sb, null, newParamName, fptype, dims, false, false, false, null);
						String dim = String.join("*", dims);
						String ofctype = exprTypeToCType(fptype.of());
						sb.append("#pragma omp smid").append(NL);
						sb.append(
								"for(int " + EXPR_INDEX + "=0; " + EXPR_INDEX + " < " + dim + "; ++" + EXPR_INDEX + ")")
								.append(NL);
						sb.append(TAB).append(newParamName).append(".matrix[").append(EXPR_INDEX).append("] = (")
								.append(ofctype).append(") ").append(translatedParams[i]).append(".matrix[")
								.append(EXPR_INDEX).append("];").append(NL);

						// change translated param
						translatedParams[i] = newParamName;
					}
				} else if (formalptype.equals(BType.MATRIX_ACCESS_SLICE)
						&& actualptype.equals(BType.MATRIX_ACCESS_SLICE)) {
					SliceType fptype = (SliceType) formalptype;
					SliceType aptype = (SliceType) actualptype;
					String newParamName = null;
					if (!fptype.of().equals(aptype.of().type())) {
						// if slices are not of the same type
						switch (fptype.of().type()) {
						case SCALAR:
							switch (aptype.of().type()) {
							case BOOL:
							case INT:
								// i can cast only bool/int to scalar
								newParamName = translatedParams[i] + "_" + funIdNode.name();
								createVariable(GType.get(fptype).name(newParamName), newParamName, null, null, false,
										VarVisibility.LOCAL_TO_FUNCTION);
								newComment(curRoot,
										"Match function " + funIdNode.name() + " parameter type for parameter "
												+ translatedParams[i] + " at line " + curRoot.lineNumber());
								newTranslation(curRoot, sliceInit(newParamName, translatedParams[i], fptype, true));
								break;
							default:
								throw new CastException(CErrorMessage.UNSUPPORTED_FUN_ACTUAL_PARAM_DONT_MATCH, curRoot,
										funIdNode.code(), aptype.name(), aptype, fptype.name(), fptype);
							}
							break;
						default:
							throw new CastException(CErrorMessage.UNSUPPORTED_FUN_ACTUAL_PARAM_DONT_MATCH, curRoot,
									funIdNode.code(), aptype.name(), aptype, fptype.name(), fptype);
						}
						// change translated param
						translatedParams[i] = newParamName;
					}
				} else if (formalptype.equals(BType.MATRIX) && actualptype.isCastableToScalar()) {
					String newParamName = Symbols.getSymbolFromType(funIdNode.name() + "_param_" + Integer.toString(i),
							BType.MATRIX);
					MatrixType fptype = (MatrixType) formalptype;
					GType ofType = fptype.of();
					IntType[] dims = new IntType[fptype.dims().length];
					for (int d = 0; d < fptype.dims().length; ++d)
						dims[d] = (IntType) GType.get(BType.INT, 1);
					MatrixType fakeMatrixType = (MatrixType) GType.get(BType.MATRIX, newParamName, ofType, dims);
					// create degenerated matrix used just to be passed to function
					createVariable(fakeMatrixType, newParamName, NodeType.ID, null, false, false,
							VarVisibility.LOCAL_TO_BLOCK.context(curRoot), false);
					sb.append(newParamName + STRUCT_ACCESS + "matrix[0] = " + translatedParams[i] + ";" + NL);
					// change translated param
					translatedParams[i] = newParamName;
				}
		}
	}

	private String copyMatrixFields(String out, String in) {
		StringBuffer sb = new StringBuffer();
		// if type is the same
		sb.append(out).append(STRUCT_ACCESS).append("matrix").append(" = ").append(in).append(STRUCT_ACCESS)
				.append("matrix;").append(NL);
		sb.append(out).append(STRUCT_ACCESS).append("poly_basis").append(" = ").append(in).append(STRUCT_ACCESS)
				.append("poly_basis;").append(NL);
		sb.append(out).append(STRUCT_ACCESS).append("__realsize").append(" = ").append(in).append(STRUCT_ACCESS)
				.append("__realsize;").append(NL);

		return sb.toString();
	}

	/**
	 * get symbolic dimension only if present on type.
	 * 
	 * @param matrix
	 * @param dim
	 * @return
	 */
	private String symbolicValue(AASTNode matrixNode, int dimIndex) {
		return symbolicValue(matrixNode, dimIndex, true);
	}

	private String symbolicValue(AASTNode exprNode, int dimIndex, boolean referenceDim) {
		AASTNode matrixNode = getExprNode(exprNode);
		DimensionType matrix = (DimensionType) matrixNode.expr();
		String ret;
		if (referenceDim) {
			ValuedType dim = matrix.dims()[dimIndex];
			if (TypeUtils.expressionNodes().contains(matrixNode.type()))
				// handle case in which the node we should get the dimensions of is a complex
				// expression (a+b)/c-k
				// in this case we rely on the middle end feature
				// that names the dimensions of the matrices
				if (!dim.hasName() && dim.hasValue())
					ret = dim.valueAsString();
				else
					ret = dim.name();
			else if (!dim.hasName() && dim.hasValue())
				ret = dim.valueAsString();
			else if (dim.hasName())
				ret = dim.name();
			else
				ret = TypeUtils.matrixDimName(matrixNode.symbol(), dimIndex + 1);
		} else {
			ret = TypeUtils.matrixDimName(matrixNode.symbol(), dimIndex + 1);
		}

		return ret;
	}

	// get the dimensions of a node as a list of symbolic reference
	protected List<String> nodeDimsToSymbolicReference(AASTNode node) {
		AASTNode exprNode = getExprNode(node);
		DimensionType mType = (DimensionType) exprNode.expr();
		IntType[] dims = mType.dims();
		return nodeDimsToSymbolicReference(exprNode, dims);
	}

	protected List<String> nodeDimsToSymbolicReference(AASTNode exprNode, IntType[] dims) {
		List<String> result = new ArrayList<String>();
		for (int k = 0; k < dims.length; ++k) {
			result.add(symbolicValue(exprNode, k));
		}
		return result;
	}

	// get the dimensions of a node as a list of matrix.dimi
	protected List<String> nodeDimsToInternalReference(AASTNode inMatrix) {
		AASTNode exprNode = getExprNode(inMatrix);
		DimensionType mType = (DimensionType) exprNode.expr();
		List<String> result = new ArrayList<String>();
		for (int k = 0; k < mType.dims().length; ++k) {
			result.add(symbolicValue(exprNode, k, false));
		}
		return result;
	}

	private String[] matrixDimsToStr(AASTNode node, DimensionType matrix) throws TypeException {
		IntType[] dims = matrix.dims();
		String[] sdims = new String[dims.length];
		if (dims == null || dims.length == 0)
			throw new TypeException(CErrorMessage.INTERNAL_MATRIX_DIMENSIONS, node, matrix.name());

		for (int i = 1; i <= dims.length; ++i)
			sdims[i - 1] = TypeUtils.matrixDimName(matrix.name(), i);

		return sdims;
	}

	/**
	 * get the dimensions of a node as a list of integer skipping the ones
	 * useDimName: use the actual IntType name instead of TypeUtils on the node name
	 * removeOnes: remove useless dimensions (1)
	 */
	protected List<String> nodeDimsToStr(AASTNode node, IntType[] dims, boolean useDimName) throws TypeException {
		GType expr = getExprGeneralized(node);
		boolean asIs = expr.equals(BType.MATRIX) && ((MatrixType) expr).dims().length >= dims.length;
		// don't group with max() if output matrix dimensions are greater than iter
		// dimensions
		return nodeDimsToStr(node, dims, useDimName, asIs, asIs);
	}

	protected List<String> nodeDimsToStr(AASTNode node, IntType[] dims, boolean useDimName, boolean plainDims)
			throws TypeException {
		return nodeDimsToStr(node, dims, useDimName, plainDims, false);
	}

	protected List<String> nodeDimsToStr(AASTNode node, IntType[] dims, boolean useDimName, boolean plainDims,
			boolean includeOnes) throws TypeException {
		List<String> sdims = new ArrayList<String>();
		List<String> tdims = new ArrayList<String>();
		AASTNode exprNode = null;
		if (node != null)
			exprNode = getExprNode(node);
		if (dims == null)
			throw new TypeException(CErrorMessage.INTERNAL_MATRIX_DIMENSIONS, exprNode, exprNode.name());

		for (int i = 1; i <= dims.length; ++i) {
			IntType dim = dims[i - 1];
			if (useDimName)
				tdims.add(dim.valueAsString());
			else
				// even if dim is of known dimension this is the symbolic name
				tdims.add(TypeUtils.matrixDimName(exprNode, i));

			// skip ones
			if (/* removeOnes && */dim.hasValue() && dim.value() == 1)
				continue;
			if (useDimName)
				sdims.add(dim.valueAsString());
			else
				// even if dim is of known dimension this is the symbolic name
				sdims.add(TypeUtils.matrixDimName(exprNode, i));
		}
		if (!plainDims) {
			if (sdims.size() == 1 && dims.length > 1) {
				// 1xN vectors sometimes are passed in as Nx1 matrices.
				// to be sure not to get the wrong dimension, make a max of both
				sdims.clear();
				sdims.add("max(" + String.join(", ", tdims) + ")");
			}
		}

		if (includeOnes)
			return tdims;
		else {
			if (sdims.isEmpty())
				sdims.add("1");
			return sdims;
		}
	}

	/**
	 * translate [a,b] = c
	 * 
	 * @param lhsNode     the left-hand-side node
	 * @param lhs_symbols the translated left-hand-side variabes
	 * @param i           the ith lhs element
	 * @param rhsNode     the right-hand-side node
	 * @param rhs_symbol  the translated right-hand-side variable
	 * @param assign      true if also assignment to translatedOutMatrix should be
	 *                    part of the translation of the node. NOTE: if the params
	 *                    involve slicing the assignment to translatedOutMatrix is
	 *                    unavoidable.
	 * @return the translation for the ith assignment
	 * @throws UndefinedTranslationException
	 * @throws TypeException
	 */
	@StepType(method = StepType.Function.MATRIX_ACCESS)
	public String translateMatrixAccess(AASTNode curApplyNode, AASTNode outMatrix, String translatedOutMatrix,
			AASTNode inMatrix, String translatedInMatrix, List<AASTNode> params, List<String> paramNames,
			List<GType> paramTypes, String[] paramsTranslated, boolean assign)
			throws UndefinedTranslationException, TypeException {

		if (params != null) {
			// compute them autonomously
			paramNames = new ArrayList<String>(params.size());
			paramTypes = new ArrayList<GType>();
			GType ptype;
			for (int p = 0; p < params.size(); ++p) {
				AASTNode param = TypeUtils.getIDNode(params.get(p));
				ptype = getExprGeneralized(param);
				paramTypes.add(ptype);
				// pass just the symbol
				paramNames.add(param.symbol());
			}
		}

		GType outType = getExprGeneralized(outMatrix);
		DimensionType outMatrixType = (outType instanceof DimensionType) ? (DimensionType) outType : null;
		List<String> outDims = null;
		if (outMatrixType != null)
			outDims = nodeDimsToInternalReference(outMatrix);
		GType inType = getExprGeneralized(inMatrix);
		if (inType.isCastableToScalar()) {
			// trying to access to a scalar as if it was a matrix
			// a = 2;
			// a(1)
			String out = translatedOutMatrix;
			if (assign) {
				String assignScalar = assignMatrixAccessScalarValue(outMatrix, translatedOutMatrix, translatedInMatrix);
				newComment(curApplyNode);
				newTranslation(outMatrix, assignScalar);
			}
			return out;
		}

		DimensionType inMatrixType = (DimensionType) inType;
		List<String> dims = nodeDimsToInternalReference(inMatrix);

		GType stype = inMatrixType.of();
		// out is 1xn but cur is nx1.. TODO
		GType outStype = (outMatrixType == null) ? null : outMatrixType.of();

		List<String> inMatrixParamDims = new ArrayList<String>();

		if (paramTypes == null || dims == null)
			throw new UndefinedTranslationException(CErrorMessage.INTERNAL_MATRIX_DIMENSIONS, inMatrix,
					translatedInMatrix);

		if (paramTypes.size() > dims.size())
			throw new UndefinedTranslationException(
					CErrorMessage.TODO_USER_ACCESS_INDEXES_GREATER_THAN_MATRIX_DIMENSIONS, inMatrix, inMatrix.code(),
					dims.size(), paramTypes.size());

		// list of array symbols in C that contains the list of indexes to be
		// accessed foreach dimension with their length
		List<Triple<String, String, String>> plain_indexes = new ArrayList<Triple<String, String, String>>();
		List<Quintuple<String, String, String, String, String>> indexes = new ArrayList<Quintuple<String, String, String, String, String>>();
		// used only in case slice of boolean:
		// slice of boolean can result in an output matrix with less element than the
		// slice paramter.
		// these are the paramters (and thus the output dimensions) that are subjected
		// to this behaviour
		// and thus they should be re-assigned after slicing
		List<Integer> outMatrixDimensionsToBeChanged = new ArrayList<Integer>();
		int mat_num = 0;
		boolean contains_copy = false;
		String outMatrixSymbol = translatedOutMatrix;
		StringBuffer res = new StringBuffer();
		// number of dimensions/params
		// if dimensions > params, and last access param :
		// last output dimension will contain also all the dimensions-params dimensions.
		int n;
		int copyfrom = -1;
		Tuple<Integer, Integer> copyBlock = null;
		boolean compatible_matrices = outMatrixType != null && inMatrixType.equals(BType.MATRIX)
				&& outStype.type().equals(stype.type());
		/* true if this access can be optimized as a single memcpy */
		boolean blockOnly = true;
		/*
		 * true if this access can be optimized as a single memcpy or a single for like
		 * a(10:20)
		 */
		boolean singleSlice = false;
		/*
		 * if result will be applied to a statistical function we can optimize as a
		 * memcpy even if rowmajor
		 */
		/* because the order is not important */
		boolean order_not_important = curApplyNode.parentExists(NodeType.APPLY)
				&& curApplyNode.parent(NodeType.APPLY).child(NodeType.ID).hasAttr(NodeAttr.REF_CORE_FUNCTION)
				&& ((IFunction) curApplyNode.parent(NodeType.APPLY).child(NodeType.ID).attr(NodeAttr.REF_CORE_FUNCTION))
						.type().equals(CompilerFrontend.FunctionType.STATISTICAL);
		List<String> accessParameters = new ArrayList<String>(paramTypes.size());
		boolean[] needsCast = new boolean[paramTypes.size()];
		for (n = 0; n < paramTypes.size(); ++n) {
			GType param = paramTypes.get(n);

			if (copyfrom == -1 && param.equals(BType.MATRIX_ACCESS_ALL))
				copyfrom = n;
			else if (copyfrom != -1 && !param.equals(BType.MATRIX_ACCESS_ALL)) {
				// consider a block at least two access_all in sequence :,:,.. that starts from
				// the first position
				// OR a single :
				// this requirement is mandatory because otherwise the memory is non-contiguous
				if (compatible_matrices && n - 2 >= copyfrom && copyfrom == 0)
					copyBlock = new Tuple<Integer, Integer>(copyfrom, n);
				copyfrom = -1;
			}

			needsCast[n] = false;
			List<String> index_dim = new ArrayList<String>(1);
			// scalar types
			if (param.equals(BType.INT) || param.equals(BType.BOOL)) {
				String valueAsString = null;
				if (((ValuedType) param).hasValue()) {
					valueAsString = ((ValuedType) param).valueAsString();
				} else {
					valueAsString = paramNames.get(n);
				}
				plain_indexes.add(new Triple<String, String, String>(valueAsString, "1", valueAsString));
				accessParameters.add(valueAsString);
				indexes.add(null);
				index_dim.add("1");
				singleSlice = false;
			} else if (param.equals(BType.SCALAR)) {
				// add cast and emit warning
				TypeException ex = new TypeException(CErrorMessage.WARN_MATRIX_ACCESS_WITH_SCALAR, curApplyNode,
						inMatrix.name(), params.get(n).name());
				curApplyNode.compilationUnit().addWarning(ex);
				String cast = "(int) ";

				String valueAsString = null;
				if (((ValuedType) param).hasValue()) {
					valueAsString = ((ValuedType) param).valueAsString();
				} else {
					valueAsString = paramNames.get(n);
				}
				plain_indexes.add(new Triple<String, String, String>(cast + valueAsString, "1", cast + valueAsString));
				accessParameters.add(cast + valueAsString);
				indexes.add(null);
				index_dim.add("1");
				needsCast[n] = true;
				singleSlice = false;
				// end scalar types
			} else if (param.equals(BType.MATRIX_ACCESS_LAST)) {
				plain_indexes.add(new Triple<String, String, String>(dims.get(n), "1", dims.get(n)));
				accessParameters.add(dims.get(n));
				indexes.add(null);
				index_dim.add("1");
				singleSlice = false;
			} else if (param.equals(BType.MATRIX_ACCESS_SLICE)) {
				SliceType slicetype = ((SliceType) param);
				Triple<GType, GType, GType> slices = slicetype.slices();
				if (slices == null)
					throw new UndefinedTranslationException(CErrorMessage.INTERNAL_ACCESS_WITH_UNDEFINED_SLICE,
							curApplyNode);

				ValuedType start = (ValuedType) slices.first();
				ValuedType step = (ValuedType) slices.second();
				ValuedType end = (ValuedType) slices.third();
				String[] slicingParameters = sliceTypesToSliceStr((SliceType) param);

				plain_indexes.add(new Triple<String, String, String>(slicingParameters[0], slicingParameters[1],
						slicingParameters[2]));
				blockOnly = false; /* can't optimize as single copy */
				indexes.add(null);
				String type;

				if (start.equals(BType.SCALAR)) {
					type = "d";
					// add cast and emit warning
					TypeException ex = new TypeException(CErrorMessage.WARN_MATRIX_ACCESS_WITH_SCALAR, curApplyNode,
							inMatrix.name(), params.get(n).name());
					curApplyNode.compilationUnit().addWarning(ex);
				} else if (start.equals(BType.INT))
					type = "i";
				else
					throw new UndefinedTranslationException(CErrorMessage.TODO_INTERNAL_SLICE_TYPE, curApplyNode,
							start);
				if (!slicetype.isVirtual())
					index_dim.add(slicetype.dim().valueAsString());
				else if (slicetype.dim().hasValue())
					index_dim.add(Integer.toString(slicetype.dim().valueAsInt()));
				else
					throw new UndefinedTranslationException(CErrorMessage.INTERNAL_ACCESS_WITH_UNDEFINED_SLICE,
							curApplyNode);
				// if this is the only parameter we can optimize the access avoiding the
				// expensive & generic sliceMatrix
				singleSlice = n == 0; // only if a(1:10,:,..,:)
				contains_copy = true;
			} else if (GType.get(BType.MATRIX_ACCESS_ALL).equals(param)) {
				if (n == paramTypes.size() - 1) {
					// if last is a matrix access all, the slice goes until end
					// of all dimensions as a flat index
					String[] remainingDims = new String[dims.size() - n];
					for (int k = n; k < dims.size(); ++k)
						remainingDims[k - n] = dims.get(k);
					index_dim.add(String.join("*", remainingDims));
				} else
					index_dim.add(dims.get(n));

				plain_indexes.add(new Triple<String, String, String>("1", "1", index_dim.get(index_dim.size() - 1)));
				accessParameters.add("1"); /* used in optimization single copy */
				indexes.add(null);
				contains_copy = true;
			} else if (param.equals(BType.MATRIX)) {
				MatrixType mtype = ((MatrixType) param);
				GType ofType = mtype.of();
				IntType[] matAccessDims = TypeUtils.realDims(mtype.dims());
				if (matAccessDims.length == 0)
					matAccessDims = ((MatrixType) param).dims();

				Integer dim = 1;
				List<String> tmpindexes = new ArrayList<String>(matAccessDims.length);
				for (IntType matAccessDim : matAccessDims) {
					if (dim != null && matAccessDim.hasIntValue())
						dim *= matAccessDim.valueAsInt();
					else
						// dimension not known
						dim = null;
					tmpindexes.add(matAccessDim.valueAsString());
				}

				// if more than 1 parameter it means that the param matrix should be flattened
				// along
				// the dimension n and the n-th dimension of the output matrix is the dimension
				// of the flattened param matrix
				String dimAsString = String.join(" * ", tmpindexes);
				if (paramTypes.size() > 1)
					index_dim.add(dimAsString);
				else if (mtype.of().equals(BType.BOOL))
					index_dim.add(dimAsString);
				else
					index_dim.addAll(tmpindexes);

				String originalStructMat = paramNames.get(n);
				String originalMat = originalStructMat + STRUCT_ACCESS + "matrix";
				String indexMat = null;
				if (ofType.isCastableToBool())
					indexMat = originalMat;
				else if (ofType.isCastableToScalar()) {
					// add cast and emit warning
					TypeException ex = new TypeException(CErrorMessage.WARN_MATRIX_ACCESS_WITH_SCALAR, curApplyNode,
							inMatrix.name(), params.get(n).name());
					curApplyNode.compilationUnit().addWarning(ex);
					// cast to int
					indexMat = getStructFreeNodeSymbol(originalStructMat) + "_slice_array"
							+ Integer.toString(SLICE_ARRAY_DIMS);
					if (dim != null) {
						res.append("int ").append(indexMat).append("[").append(dim).append("] = {").append(NL);

						List<String> strLst = new ArrayList<String>();
						for (int i = 0; i < dim; ++i)
							strLst.add("(int) " + originalMat + "[" + i + "]");
						res.append(TAB).append(String.join(", ", strLst)).append(NL);
						res.append("};").append(NL);

					} else if (dimAsString != null) {
						res.append("int ").append(indexMat).append("[").append(dimAsString).append("];").append(NL);

						// populates the matrix in a for cycle
						res.append("#pragma omp smid").append(NL);
						res.append("for(int " + EXPR_INDEX + "=0; " + EXPR_INDEX + " < ").append(dimAsString)
								.append("; ++" + EXPR_INDEX + "){").append(NL);
						res.append(TAB).append(indexMat).append("[" + EXPR_INDEX + "] = (int)").append(originalMat)
								.append("[" + EXPR_INDEX + "];").append(NL);
						res.append("}").append(NL);
					} else {
						throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_MATRIX_ACCESS, curApplyNode);
					}
				} else
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_MATRIX_ACCESS, curApplyNode);

				String sliceArrayDimsName = getStructFreeNodeSymbol(originalStructMat) + "_slice_array_dims"
						+ Integer.toString(SLICE_ARRAY_DIMS++);
				// dimensions
				res.append("int ").append(sliceArrayDimsName).append("[").append(tmpindexes.size()).append("] = {")
						.append(NL);
				res.append(TAB).append(String.join(", ", tmpindexes)).append(NL);
				res.append("};").append(NL);

				String type = null;
				if (mtype.of().equals(BType.BOOL)) {
					// set to applyNode 'slice of boolean matrix' flag
					// needed in case this apply is then in used in an expression loop
					// (in that case it shouldn't use the canonical EXPR_INDEX iterator
					// but a custom one since it can be shorter than other matrices)
					curApplyNode.attr(CAttr.APPLY_OF_BOOLEAN_MATRIX, params.get(n));
					outMatrixDimensionsToBeChanged.add(n);
					type = "2";
				} else if (mtype.of().equals(BType.INT))
					type = "1";
				else if (mtype.of().equals(BType.SCALAR))
					// was casted to int before
					type = "1";

				indexes.add(new Quintuple<String, String, String, String, String>(indexMat, sliceArrayDimsName,
						originalStructMat + STRUCT_ACCESS + "poly_basis", Integer.toString(tmpindexes.size()), type));
				blockOnly = false;
				mat_num++;
				plain_indexes.add(null);
				contains_copy = true;
				singleSlice = false;
			} else
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_MATRIX_ACCESS, curApplyNode);

			inMatrixParamDims.addAll(index_dim);
		}
		// true if we are in case x = .. a(:) ..
		boolean copyAll = false;
		// close block check last param
		GType lastParamType = paramTypes.get(paramTypes.size() - 1);

		if (copyfrom != -1)
			if ((n == 1 && lastParamType.equals(BType.MATRIX_ACCESS_ALL))
					|| !lastParamType.equals(BType.MATRIX_ACCESS_ALL)) {
				// consider a block at least two access_all in sequence :,:,.. that starts from
				// the first position
				// OR a single :
				// this last requirement is mandatory because otherwise the memory is
				// non-contiguous
				if (compatible_matrices)
					if (n - 2 >= copyfrom && copyfrom == 0)
						copyBlock = new Tuple<Integer, Integer>(copyfrom, n);
					else if (n == 1)
						copyAll = true;
				copyfrom = -1;
				// case a(1,:)
			} else if (n == 2 && ROWMAJOR && paramTypes.get(0).isCastableToAccessIndex()) {
				if (compatible_matrices)
					// copy full row
					copyBlock = new Tuple<Integer, Integer>(copyfrom - 1, copyfrom);
				copyfrom = -1;
			}

		// ensure output matrix is allocated

		if (!contains_copy) {
			String matAccess;
			String[] castedParams = new String[paramsTranslated.length];
			for (int c = 0; c < needsCast.length; ++c)
				if (needsCast[c])
					castedParams[c] = "(int) " + paramsTranslated[c];
				else
					castedParams[c] = paramsTranslated[c];
			IntType[] realInDims = TypeUtils.realDims(inMatrixType.dims());
			if (castedParams.length == 1
					&& (params != null && getExprGeneralized(params.get(0)).isCastableToAccessIndex())) {
				if (realInDims.length == 1)
					matAccess = castedParams[0] + "-1";
				else {
					// flat index on multi-dimensional matrix, use colmajor2rowmajor functions
					if (ROWMAJOR) {
						matAccess = "colMajor2RowMajor(" + castedParams[0] + "-1, " + dims.get(0) + ", " + dims.get(1)
								+ ")";
					} else {
						// was counterToBeUsed, I think this was wrong
						matAccess = castedParams[0] + "-1";
					}
				}
			} else {
				if (castedParams.length == inMatrixType.dims().length) {
					String[] accessPosition = new String[castedParams.length];
					for (int ap = 0; ap < inMatrixType.dims().length; ++ap)
						accessPosition[ap] = "(" + castedParams[ap] + "-1) * " + translatedInMatrix + STRUCT_ACCESS
								+ "poly_basis[" + ap + "]";
					matAccess = String.join(" + ", accessPosition);
				} else {
					// in case one of the child_results is not an integer this is wrong.
					// don't care because assign will ignore this result in this case.
					String[] strdims = matrixDimsToStr(inMatrix, inMatrixType);
					matAccess = "matrixAccess(" + inMatrix.symbol() + STRUCT_ACCESS + "poly_basis, "
							+ inMatrixType.dims().length + ", " + castedParams.length + ", "
							+ String.join(", ", strdims) + ", " + String.join(",", castedParams) + ")";
				}
			}

			String toBeReturned;

			if (inMatrixType.equals(BType.MATRIX))
				toBeReturned = translatedInMatrix + STRUCT_ACCESS + "matrix[" + matAccess + "]";
			else if (inMatrixType.equals(BType.MATRIX_ACCESS_SLICE))
				toBeReturned = getSequenceAccess(inMatrix, matAccess);
			else
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_APPLY_TO_NON_MATRIX, curApplyNode,
						inMatrixType);

			if (assign) {
				String assignScalar = assignMatrixAccessScalarValue(outMatrix, translatedOutMatrix, toBeReturned);
				newComment(curApplyNode);
				newTranslation(outMatrix, assignScalar);
				return outMatrixSymbol;
			} else {
				// prealloc check should be done outside
				return toBeReturned;
			}
		} else {
			GType outmType = getExprGeneralized(outMatrix);
			if (!outmType.equals(BType.MATRIX))
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_ASSIGN_INCOMPATIBLE_TYPES, curApplyNode,
						outMatrix.code(), outmType, curApplyNode.code());

			// assign the result matrix
			GType inmType = getExprGeneralized(inMatrix);
			if (inmType != null && inmType.isCastableToMatrix()) {
				MatrixType curMatrixType = (MatrixType) curApplyNode.expr();
				if (curMatrixType == null)
					curMatrixType = (MatrixType) outmType;
				if (curMatrixType.dims().length > inMatrixParamDims.size()) {

					// add the starting ones untouched
					int ndim = 0;
					for (; ndim < curMatrixType.dims().length; ++ndim) {
						IntType d = curMatrixType.dims()[ndim];
						if (d.hasValue() && d.valueAsInt().equals(1))
							inMatrixParamDims.add(0, "1");
						else
							// stop at first non-one
							break;
					}
					// add the ending ones untouched
					int edim = curMatrixType.dims().length - 1;
					for (; ndim < edim; --edim) {
						IntType d = curMatrixType.dims()[edim];
						if (d.hasValue() && d.valueAsInt().equals(1))
							inMatrixParamDims.add("1");
						else
							// stop at first non-one
							break;
					}
					// if still greater, check the last param type
					// that should take into account the extra edim-ndim dimensions
					if (curMatrixType.dims().length > inMatrixParamDims.size())
						if (lastParamType.equals(BType.MATRIX_ACCESS_ALL)) {
							// all subsequent dimensions should be squeezed in the last one
							MatrixType intype = ((MatrixType) inmType);
							int lastidx = inMatrixParamDims.size() - 1;
							String lastDim = inMatrixParamDims.get(lastidx);
							int dimstosqueeze = Math.min(curMatrixType.dims().length - inMatrixParamDims.size() + 1,
									intype.dims().length);
							String[] squeezedDims = new String[dimstosqueeze];
							squeezedDims[0] = lastDim;
							for (int d = 1; d < dimstosqueeze; ++d)
								squeezedDims[d] = intype.dims()[d].valueAsString();
							inMatrixParamDims.set(lastidx, String.join(" * ", squeezedDims));
							// fill the remaining with ones
							for (int d = inMatrixParamDims.size(); d < curMatrixType.dims().length; ++d)
								inMatrixParamDims.add("1");
						} else if (lastParamType.equals(BType.MATRIX_ACCESS_SLICE))
							// case b = a(2:end)
							// b is 1D while a is 2D
							for (int d = inMatrixParamDims.size(); d < curMatrixType.dims().length; ++d)
								// all the subsequent dimension should be just copied
								inMatrixParamDims.add(((MatrixType) inmType).dims()[d].valueAsString());
						else
							throw new UndefinedTranslationException(CErrorMessage.INTERNAL_MISMATCHED_MATRIX_ACCESS,
									outMatrix, inMatrix.code(), inMatrixType, outMatrixSymbol, curMatrixType);
				}

				if (curMatrixType.dims().length < inMatrixParamDims.size()) {
					// try to remove pointwise access (ex :,:,2 has a pointwise access in position
					// 2)
					// and fake ones added in the previous step
					List<Integer> toBeRemoved = new LinkedList<>();
					for (int m = 0; m < inMatrixParamDims.size(); ++m)
						if ("1".equals(inMatrixParamDims.get(m))
								&& curMatrixType.dims().length < inMatrixParamDims.size() - toBeRemoved.size())
							toBeRemoved.add(m);
						else if (curMatrixType.dims().length >= inMatrixParamDims.size() - toBeRemoved.size())
							// we removed a sufficient number of ones
							break;

					for (int m = 0; m < toBeRemoved.size(); ++m)
						inMatrixParamDims.remove(m);
					// re-check condition

					if (curMatrixType.dims().length < inMatrixParamDims.size())
						throw new UndefinedTranslationException(CErrorMessage.INTERNAL_MISMATCHED_MATRIX_ACCESS,
								outMatrix, inMatrix.code(), inMatrixType, outMatrixSymbol, curMatrixType);
				}
				StringBuffer allocatingBuffer = new StringBuffer();
				MatrixType cmtype = (MatrixType) curMatrixType;
				ensureMatrixMemory(allocatingBuffer, outMatrix, outMatrixSymbol, (MatrixType) outmType,
						inMatrixParamDims.toArray(new String[inMatrixParamDims.size()]), false, false, false,
						(cmtype.isSparse()) ? translatedInMatrix + STRUCT_ACCESS + "usedCells" : null);

				newComment(outMatrix,
						"Modifying memory for variable " + outMatrixSymbol + " at line " + outMatrix.lineNumber());
				newTranslation(outMatrix, allocatingBuffer.toString());

			}
		}
		// we need to copy a portion of the matrix
		// createUninitializedMatrix(outMatrixSymbol, SymbolType.SCALAR,
		// outDims);
		StringBuffer sliceMatrixParams = new StringBuffer();
		String plain_symbol = Symbols.getSymbolFromType(outMatrixSymbol, BType.MATRIX_ACCESS_SLICE);
		List<String> plain_rows = new ArrayList<String>();
		List<String> is_mat_idx = new ArrayList<String>();
		// true if A(NxMx....xZ) is accessed with exactly N..Z 1D vectors
		boolean access_with_1d_matrices = true;
		boolean sparseMatrix;
		// access translation for scalar indices
		for (int i = 0; i < indexes.size(); i++) {
			// matrix values, matrix dimensions, number of matrix dimensions
			Quintuple<String, String, String, String, String> mat_index = indexes.get(i);
			if (mat_index == null) {
				is_mat_idx.add("0");
				// this is a simple_indexes (a triple of start,step,end)
				Triple<String, String, String> plain_idx = plain_indexes.get(i);
				GType partype = paramTypes.get(i);
				AASTNode param = (params != null) ? params.get(i) : null;
				if (partype != null && partype.isCastableToScalar() && i < paramsTranslated.length
				// should also be an expression ora an apply
						&& param != null && (TypeUtils.expressionNodes().contains(param.type())
								|| param.type().equals(NodeType.APPLY))) {
					// translate first expression
					String translation = paramsTranslated[i];
					String symbol = plain_idx.first();
					createVariable(partype, symbol, NodeType.ID, null, false, false,
							VarVisibility.LOCAL_TO_BLOCK.context(curApplyNode), false);
					newComment(param);
					newTranslation(param, symbol + " = " + translation + ";" + NL);
				}
				plain_rows.add("{ " + plain_idx.first() + ", " + plain_idx.second() + ", " + plain_idx.third() + " }");
				access_with_1d_matrices = false;
			} else {
				String mat_type = mat_index.fifth();
				is_mat_idx.add(mat_type);
				if (sliceMatrixParams.length() != 0)
					sliceMatrixParams.append(", ");

				sliceMatrixParams.append(mat_index.first() + ", " + mat_index.second() + ", " + mat_index.third() + ", "
						+ mat_index.fourth());

				// if 1D vector
				DimensionType ptype = (DimensionType) paramTypes.get(i);
				access_with_1d_matrices &= TypeUtils.realDims(ptype.dims()).length == 1 && ptype.of().equals(BType.INT);
			}
		}
		if (access_with_1d_matrices)
			access_with_1d_matrices &= indexes.size() == inMatrixType.dims().length
					|| indexes.size() == TypeUtils.realDims(inMatrixType.dims()).length;

		if (inMatrixType.equals(BType.MATRIX_ACCESS_SLICE)) {
			// if input is a slice, convert it to a matrix first
			translatedInMatrix = castSliceTypeToMatrixType(res, outMatrix, translatedInMatrix, inMatrixType);
			sparseMatrix = false;
			dims.clear();
			for (int i = 1; i <= inMatrixType.dims().length; ++i)
				dims.add(TypeUtils.matrixDimName(translatedInMatrix, i));
		} else
			sparseMatrix = ((MatrixType) inMatrixType).isSparse();

		String outSliceType = null;
		if (outStype.equals(BType.SCALAR))
			outSliceType = "double";
		else if (outStype.equals(BType.INT) || stype.equals(BType.BOOL))
			outSliceType = "int";
		else
			throw new UndefinedTranslationException(CErrorMessage.TODO_INTERNAL_SLICE_TYPE, inMatrix,
					getExprGeneralized(inMatrix));

		if (sparseMatrix && access_with_1d_matrices) {
			String functionCode = "slice";
			String functionName = null;
			try {
				functionName = getCFunctionImplementationManagerInstance().getTranslation(curApplyNode, functionCode);
			} catch (Exception e) {
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_PREFERRED_AND_ALTERNATIVE_TRANSLATION,
						curApplyNode, functionCode);
			}
			char prefix;
			if (outMatrixType.of().equals(BType.SCALAR)) {
				prefix = 'd';
			} else {
				prefix = 'i';
			}

			functionName = functionName.replace('?', prefix);
			// we can optimize matrix access if we have just 1 array of rows and 1 array of
			// columns
			res.append(functionName).append("(");
			res.append("&" + outMatrixSymbol + ", ");
			res.append("&" + translatedInMatrix + ", ");
			// access params
			List<String> paramsAsStr = new ArrayList<String>(indexes.size());
			if (indexes.size() == 1 && inMatrixType.dims()[0].hasValue()
					&& inMatrixType.dims()[0].valueAsInt().equals(1))
				// add 1 for rows
				paramsAsStr.add("(int []){1}, 1");
			for (int i = 0; i < indexes.size(); i++) {
				// matrix values, matrix dimensions, number of matrix dimensions
				Quintuple<String, String, String, String, String> mat_index = indexes.get(i);
				paramsAsStr.add(mat_index.first());
				paramsAsStr.add(String.join("*",
						nodeDimsToStr(params.get(i), ((DimensionType) paramTypes.get(i)).dims(), true, false)));
			}
			if (indexes.size() == 1 && inMatrixType.dims()[1].hasValue()
					&& inMatrixType.dims()[1].valueAsInt().equals(1))
				// add 1 for cols
				paramsAsStr.add("(int []){1}, 1");
			res.append(String.join(", ", paramsAsStr)).append(");").append(NL);
		} else if (blockOnly && (copyBlock != null || copyAll)) {
			// use memcpy
			/* build access to input matrix */
			if ((copyBlock == null && ROWMAJOR && !order_not_important) && (copyAll || compatible_matrices))
				/* only single access parameters, change from 1 to expr_index */
				/* because we will use a loop */
				accessParameters.set(0, EXPR_INDEX);

			StringBuffer inAccess = new StringBuffer();
			if (accessParameters.size() == 1) {
				if (ROWMAJOR && dims.size() >= 2) {
					inAccess.append("colMajor2RowMajor(");
					inAccess.append(accessParameters.get(0)).append("-1").append(", ");
					inAccess.append(dims.get(0)).append(", ");
					inAccess.append(dims.get(1));
					inAccess.append(")");
				} else {
					// was counterToBeUsed, I think this was wrong
					inAccess.append(accessParameters.get(0)).append("-1");
				}
			} else {
				if (accessParameters.size() == dims.size()) {
					String[] accessPosition = new String[accessParameters.size()];
					for (int ap = 0; ap < dims.size(); ++ap)
						accessPosition[ap] = "(" + accessParameters.get(ap) + "-1) * " + translatedInMatrix
								+ STRUCT_ACCESS + "poly_basis[" + ap + "]";
					inAccess.append(String.join(" + ", accessPosition));
				} else {
					inAccess.append("matrixAccess(").append(translatedInMatrix).append(STRUCT_ACCESS)
							.append("poly_basis").append(", ");
					// n dimensions
					inAccess.append(dims.size()).append(", ");
					// n access numbers
					inAccess.append(accessParameters.size()).append(", ");
					// dimensions
					inAccess.append(String.join(", ", dims)).append(", ");
					// access parameters
					inAccess.append(String.join(", ", accessParameters));
					inAccess.append(")");
				}
			}

			if (copyBlock != null || !ROWMAJOR || order_not_important) {
				/* memcpy */
				res.append("memcpy(");
				/* dest */
				res.append(outMatrixSymbol).append(STRUCT_ACCESS).append("matrix ").append(", ");
				/* src */
				res.append(translatedInMatrix).append(STRUCT_ACCESS).append("matrix + ").append(inAccess.toString())
						.append(", ");
				/* howmany */
				res.append(String.join(" * ", outDims)).append(" * sizeof(").append(exprTypeToCType(inMatrixType.of()))
						.append("));").append(NL);
			} else if (copyAll || compatible_matrices) {
				// can't use memcpy because we are in case a(:) and using rowmajor layout
				// while MATLAB uses colmajor (memcpy will keep the layout, while we need to
				// change it)
				// optimize as a single for
				// res.append("#pragma omp parallel for if(" + String.join(" * ", outDims)
				// + " > PARALLELIZABLE_FOR_TRESHOLD)").append(NL);
				/* STARTS FROM 1 FOR COMPATIBILITY WITH ALL THE OTHER CASES */
				res.append("#pragma omp smid").append(NL);
				res.append("for(int " + EXPR_INDEX + "=1; " + EXPR_INDEX + " <= " + String.join(" * ", outDims)
						+ " ; ++" + EXPR_INDEX + ")").append(NL);
				res.append(TAB).append(outMatrixSymbol).append(STRUCT_ACCESS).append("matrix[").append(EXPR_INDEX)
						.append("-1] = ").append("(").append(exprTypeToCType(outMatrixType.of())).append(") ");
				res.append(translatedInMatrix).append(STRUCT_ACCESS).append("matrix[").append(inAccess.toString())
						.append("];").append(NL);
			}
		} else if (singleSlice && outMatrixType != null) {
			// case a(2:10, :, : ..)
			// optimize as a single memcpy or as a single for depending on the step of the
			// slice
			Triple<String, String, String> slice = plain_indexes.get(0);
			boolean memcpy;
			try {
				memcpy = Integer.parseInt(slice.second()) == 1;
			} catch (Exception e) {
				try {
					memcpy = (Double.parseDouble(slice.second()) - 1) <= Double.MIN_VALUE;
				} catch (Exception e1) {
					memcpy = false;
				}
			}

			IntType[] idims = TypeUtils.realDims(outMatrixType.dims());
			boolean onedimmatrix = idims.length == 1;
			List<String> subDims = dims.subList(1, dims.size());
			String subDimsStr = Strings.join(subDims, '*');
			boolean inouttypeequal = outStype.type().equals(stype.type());
			if (memcpy && inouttypeequal) {
				// the slice is continuous (step == 1) and in and out types are the same.
				// -> we can use memcpy
				/* memcpy */
				res.append("memcpy(");
				/* dest */
				res.append(outMatrixSymbol).append(STRUCT_ACCESS).append("matrix ").append(", ");
				/* src */
				res.append(translatedInMatrix).append(STRUCT_ACCESS).append("matrix");
				/* src start copying */
				res.append(" + ((int) ").append(slice.first()).append(" - 1)");
				if (!onedimmatrix)
					// start from 1, skip first dimension
					res.append(" * ").append(subDimsStr);
				res.append(", ");
				/* howmany */
				res.append("((int) ").append(slice.third() + " - " + slice.first()).append(" + 1)");
				if (!onedimmatrix)
					// start from 1, skip first dimension
					res.append(" * ").append(subDimsStr);
				/* single-element dimension */
				res.append(" * sizeof(").append(exprTypeToCType(inMatrixType.of())).append("));").append(NL);
			} else {
				// the slice is not continuous we should use a for loop
				// optimize as a single for
				String autoinc = plain_symbol + "_autoinc";
				String outtype = exprTypeToCType(outStype);
				createVariable(GType.get(BType.INT), autoinc, null, NodeType.ID, "0", false, false,
						VarVisibility.LOCAL_TO_BLOCK, res, false);
				// res.append("#pragma omp parallel for if(" + String.join(" * ", outDims)
				// + " > PARALLELIZABLE_FOR_TRESHOLD)").append(NL);
				/* STARTS FROM 1 FOR COMPATIBILITY WITH ALL THE OTHER CASES */
				if (onedimmatrix)
					res.append("#pragma omp smid").append(NL);
				res.append("for(" + outtype + " " + EXPR_INDEX + " = " + slice.first() + "; " + EXPR_INDEX + " <= "
						+ slice.third() + " ; " + EXPR_INDEX + " += " + slice.second() + ")").append(NL);

				if (inouttypeequal) {
					if (onedimmatrix) {
						res.append(TAB).append(outMatrixSymbol).append(STRUCT_ACCESS).append("matrix[").append(autoinc)
								.append("++] = ").append("(").append(outtype).append(") ");
						res.append(translatedInMatrix).append(STRUCT_ACCESS).append("matrix[(int) ").append(EXPR_INDEX)
								.append(" - 1];").append(NL);
					} else {
						res.append(TAB).append("memcpy(");
						/* dest */
						res.append(outMatrixSymbol).append(STRUCT_ACCESS).append("matrix");
						res.append(" + ").append(autoinc).append("++");
						res.append(" * ").append(subDimsStr);
						res.append(", ");
						/* src */
						res.append(translatedInMatrix).append(STRUCT_ACCESS).append("matrix");
						res.append(" + ((int) ").append(EXPR_INDEX).append(" - 1)");
						res.append(" * ").append(subDimsStr);
						res.append(", ");
						/* howmany */
						// 1 row
						res.append(subDimsStr);
						/* single-element dimension */
						res.append(" * sizeof(").append(exprTypeToCType(inMatrixType.of())).append("));").append(NL);
					}
				} else {
					/* add missing for one for each dimension */
					int d = 0;
					String counterPrefix = "_d";
					String[] accessPosition = new String[1 + subDims.size()];
					accessPosition[0] = "((int) " + EXPR_INDEX + " - 1) * " + translatedInMatrix + STRUCT_ACCESS
							+ "poly_basis[0]";
					for (; d < subDims.size(); ++d) {
						StringBuffer buf = new StringBuffer();
						buf.append("for(int ").append(counterPrefix + Integer.toString(d)).append("=1; ");
						buf.append(counterPrefix + Integer.toString(d)).append("<=").append(subDims.get(d)).append(";");
						buf.append(counterPrefix + Integer.toString(d)).append("++");
						buf.append(")").append(NL);

						for (int t = 0; t < d + 1; ++t)
							res.append(TAB);
						res.append(buf);
						// flat matrix index
						accessPosition[d + 1] = "(" + counterPrefix + Integer.toString(d) + "-1) * "
								+ translatedInMatrix + STRUCT_ACCESS + "poly_basis[" + (d + 1) + "]";
					}
					for (int t = 0; t < d + 1; ++t)
						res.append(TAB);
					res.append(outMatrixSymbol).append(STRUCT_ACCESS).append("matrix[").append(autoinc).append("++] = ")
							.append("(").append(outtype).append(") ");
					res.append(translatedInMatrix).append(STRUCT_ACCESS).append("matrix[")
							.append(String.join(" + ", accessPosition)).append("];").append(NL);

				}
			}
		} else {
			// initialize list of start/step/end
			String slicearrayname = getStructFreeNodeSymbol(plain_symbol) + "_slice_array";
			String slicearraytypename = getStructFreeNodeSymbol(plain_symbol) + "_slice_array_type";

			res.append("/* Initialize array of start,step,end slicing. */").append(NL);
			res.append("int ").append(slicearrayname).append("[").append(plain_rows.size()).append("][3] = ").append(NL)
					.append("{").append(NL).append(String.join("," + NL, plain_rows)).append(NL).append("};")
					.append(NL);

			res.append(
					"/* Initialize array that allow algorithm to use mixed start,step,end slicing and index matrix slicing."
							+ NL + indexes.size() + ": the total number of indexes (both start,step,end and matrix) */")
					.append(NL);
			res.append("char ").append(slicearraytypename).append("[").append(is_mat_idx.size()).append("] = ")
					.append("{").append(NL).append(String.join(", ", is_mat_idx)).append(NL).append("};").append(NL);

			String sliceType = null;
			if (stype.equals(BType.SCALAR))
				sliceType = "double";
			else if (stype.equals(BType.INT) || stype.equals(BType.BOOL))
				sliceType = "int";
			else
				throw new UndefinedTranslationException(CErrorMessage.TODO_INTERNAL_SLICE_TYPE, inMatrix,
						getExprGeneralized(inMatrix));

			boolean booleanSlice = !outMatrixDimensionsToBeChanged.isEmpty();
			String newdimsSymbol = "_newdims" + outMatrixSymbol;
			String newdimsCnt = "_newdims" + outMatrixSymbol + "_cnt";
			if (booleanSlice) {
				createVariable(GType.get(BType.INT), newdimsCnt, null, NodeType.ID, "0", false, false,
						VarVisibility.LOCAL_TO_BLOCK, res, false);
				res.append("int *").append(newdimsSymbol).append(" = ");
			}

			String inMatrixParam, outMatrixParam;
			if (!sparseMatrix) {
				inMatrixParam = translatedInMatrix + STRUCT_ACCESS + "matrix, " + translatedInMatrix + STRUCT_ACCESS
						+ "poly_basis," + " NULL, NULL";
				outMatrixParam = "&" + outMatrixSymbol + STRUCT_ACCESS + "matrix, " + outMatrixSymbol + STRUCT_ACCESS
						+ "poly_basis, NULL, NULL, NULL, NULL";
			} else {
				inMatrixParam = translatedInMatrix + STRUCT_ACCESS + "matrix, " + "NULL," + translatedInMatrix
						+ STRUCT_ACCESS + "rowIndex" + ", " + translatedInMatrix + STRUCT_ACCESS + "columns";
				outMatrixParam = "&" + outMatrixSymbol + STRUCT_ACCESS + "matrix, " + outMatrixSymbol + STRUCT_ACCESS
						+ "poly_basis, " + "&" + outMatrixSymbol + STRUCT_ACCESS + "rowIndex" + ", " + "&"
						+ outMatrixSymbol + STRUCT_ACCESS + "columns, &" + outMatrixSymbol + STRUCT_ACCESS
						+ "usedCells, &" + outMatrixSymbol + STRUCT_ACCESS + "__realsize";
			}
			res.append(outSliceType + sliceType + "SliceMatrix(" + outMatrixParam + ", " + inMatrixParam + ", "
					+ dims.size() + ", " + outDims.size() + ", " + slicearrayname + ", " + plain_rows.size() + ", "
					+ slicearraytypename + ", " + mat_num + ", " + String.join(", ", dims) + ", "
					+ String.join(", ", outDims) + ((mat_num != 0) ? ", " + sliceMatrixParams : "") + ")").append(";")
					.append(NL);

			if (booleanSlice) {
				List<String> newMatrixOutDims = new ArrayList<String>(inMatrixParamDims.size());
				for (int no = 0; no < inMatrixParamDims.size(); ++no)
					if (outMatrixDimensionsToBeChanged.contains(no)) {
						// replace with _newdims computed by slice function
						newMatrixOutDims.add(newdimsSymbol + "[" + newdimsCnt + "++]");
					} else
						newMatrixOutDims.add(inMatrixParamDims.get(no));

				res.append(
						"/* Re-ensure output matrix memory since boolean slice can lead to less elements in output matrix than input slicing parameter */")
						.append(NL);
				ensureMatrixMemory(res, outMatrix, outMatrixSymbol, (MatrixType) outMatrixType,
						newMatrixOutDims.toArray(new String[newMatrixOutDims.size()]), false, false, false, null);
				res.append("qspcc_free(" + newdimsSymbol + ");").append(NL);
			}
		}
		// if we created a new matrix starting from a function parameter
		// we should update the inner matrix dimension map
		// updateMatrixDims(outMatrixSymbol, outDims);
		newComment(curApplyNode);
		newTranslation(curApplyNode, res.toString());
		return outMatrixSymbol;
	}

	protected CFunctionImplementationManager getCFunctionImplementationManagerInstance() throws Exception {
		return CFunctionImplementationManager.getInstance();
	}

	private String assignMatrixAccessScalarValue(AASTNode outMatrix, String lhs, String rhs)
			throws TypeException, UndefinedTranslationException {
		StringBuffer localBuf = new StringBuffer();
		GType outType = getExprGeneralized(outMatrix);
		if (outType.isCastableToMatrix()) {
			IntType[] dims = ((DimensionType) outType).dims();
			// ensurecapacity for out matrix
			String[] ndims = new String[dims.length];
			for (int j = 0; j < dims.length; ++j)
				ndims[j] = "1";
			// we need just 1 element
			ensureMatrixMemory(localBuf, outMatrix, ndims);
			localBuf.append(lhs).append(STRUCT_ACCESS).append("matrix[0] = ");
		} else {
			// also output is scalar
			localBuf.append(lhs + " = ");
		}
		localBuf.append(rhs + ";").append(NL);
		return localBuf.toString();
	}

	private String[] sliceTypesToSliceStr(SliceType param) {
		return sliceTypesToSliceStr(param, null);
	}

	private String[] sliceTypesToSliceStr(SliceType param, String baseStruct) {
		Triple<GType, GType, GType> slices = param.slices();
		ValuedType start = (ValuedType) slices.first();
		ValuedType step = (ValuedType) slices.second();
		ValuedType end = (ValuedType) slices.third();

		String[] slicingParameters = new String[3];

		// get the slicing type parameters (from the type or parameterically if there is
		// no
		// dimension codified by the middleend)
		slicingParameters[0] = null;
		if (baseStruct != null)
			slicingParameters[0] = baseStruct + STRUCT_ACCESS + "start";
		else if (param.isVirtual())
			slicingParameters[0] = Integer.toString(start.valueAsInt());
		else if (!start.hasName() && start.hasValue())
			slicingParameters[0] = start.valueAsString();
		else
			slicingParameters[0] = param.name() + STRUCT_ACCESS + "start";

		slicingParameters[1] = null;
		if (baseStruct != null)
			slicingParameters[1] = baseStruct + STRUCT_ACCESS + "step";
		else if (param.isVirtual())
			slicingParameters[1] = Integer.toString(step.valueAsInt());
		else if (!start.hasName() && step.hasValue())
			slicingParameters[1] = step.valueAsString();
		else
			slicingParameters[1] = param.name() + STRUCT_ACCESS + "step";

		slicingParameters[2] = null;
		if (baseStruct != null)
			slicingParameters[2] = baseStruct + STRUCT_ACCESS + "end";
		else if (param.isVirtual())
			slicingParameters[2] = Integer.toString(end.valueAsInt());
		else if (!start.hasName() && end.hasValue())
			slicingParameters[2] = end.valueAsString();
		else
			slicingParameters[2] = param.name() + STRUCT_ACCESS + "end";

		return slicingParameters;
	}

	/**
	 * 
	 * @param lhsNode
	 * @param lhs_symbol
	 * @param lhs_params
	 * @param rhsNode
	 * @param rhs_symbol
	 * @param is_delete, deletes lhs elements accessed by lhs_params. equivalent to
	 *                   lhsNode = lhsNode(<contrary of lhs_params>)
	 * @return
	 * @throws UndefinedTranslationException
	 * @throws TypeException
	 */
	private String translateAssign(AASTNode curRoot, AASTNode lhsNode, String lhs_symbol, String[] lhs_params_symbols,
			List<AASTNode> lhs_params, AASTNode rhsNode, String rhs_symbol)
			throws UndefinedTranslationException, TypeException {
		StringBuffer preamble = new StringBuffer();
		StringBuffer buf = new StringBuffer();
		GType lhsType = getExprGeneralized(lhsNode);
		GType rhsType = getExprGeneralized(rhsNode);
		// indices that should be used on the RHS
		// only in case of access with matrix, or slice, or ALL this will be populated
		List<String> counterToBeUsed = new LinkedList<String>();
		boolean rhs_expression = (TypeUtils.expressionNodes().contains(rhsNode.type())
				&& !rhsNode.hasAttr(CAttr.MATRIX_EXPR));

		String rhsVariable = rhs_symbol;
		AASTNode rhsIterNode = rhsNode;
		if (rhsType instanceof MatrixType && rhs_expression) {
			rhsIterNode = getIterNode(rhsNode);
			if (rhsIterNode == null)
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_ASSIGN_INCOMPATIBLE_TYPES, curRoot,
						rhsNode.code());
			rhsVariable = rhsIterNode.symbol();
		}

		if (lhsType instanceof MatrixType && !lhs_params.isEmpty()) {

			// TODO check if here a class cast exception could occurr
			MatrixType matrixType = (MatrixType) lhsType;
			Tuple<IntType[], List<Integer>> tp = TypeUtils.realDimsWithPos(matrixType.dims());
			IntType[] matrixDimensions = tp.first();
			List<Integer> nonOnesInMatrixPos = tp.second();

			int counterSuffix = 0;
			String counterPrefix = "_i";
			// stringified dimensions of matrix accessed
			List<String> accessDimensions = new LinkedList<String>();
			// list of dimensions used in for loops added, or empty if index-based (of the
			// form a(1,2,3)=b)
			List<String> forLoopDimensions = new LinkedList<String>();
			// list of non-1 dimensions used in for loops
			List<String> assignDimensions = new LinkedList<String>();
			// list of non-1 dimensions of matrix accessed, the mul should match the mul of
			// assignDimensions
			List<String> lhsDimensions = new LinkedList<String>();
			List<Boolean> isAccessIndex = new LinkedList<Boolean>();
			boolean hasLoops = false;
			boolean hasParamMatrices = false;
			// indices to be used on LHS
			List<String> accessParameters = new LinkedList<String>();
			// accessParameters that requires a non-null check
			List<String> checkNonNull = new LinkedList<String>();

			int innestedLevel = 0;
			int ndim = 0;
			int nrhsdim = 0;
			List<AASTNode> params = lhs_params;
			// list of non-statically 1 parameters (equivalent of nonOnesPos but for
			// parameters)
			List<Integer> nonOnesParam = new LinkedList<Integer>();
			// if matrixDimensions > 1 linear index on a multidimensional matrix
			boolean linearIndex = params.size() == 1;
			// true if this slice is just a copy of entire dimensions (through access all
			// operator :)
			int copyfrom = -1;
			Tuple<Integer, Integer> copyBlock = null;
			boolean compatible_matrices = rhsType instanceof MatrixType
					&& matrixType.of().type().equals(((MatrixType) rhsType).of().type());
			StringBuffer allTmpBuf = new StringBuffer();
			for (int j = 0; j < params.size(); j++) {
				String tabsToBeAppended = "";
				for (int innestedCounter = 0; innestedCounter < innestedLevel; innestedCounter++) {
					tabsToBeAppended += TAB;
				}

				AASTNode paramNode = getExprNode(params.get(j));
				GType paramType = paramNode.expr();
				if (copyfrom == -1 && paramType.equals(BType.MATRIX_ACCESS_ALL))
					copyfrom = j;
				else if (copyfrom != -1 && !paramType.equals(BType.MATRIX_ACCESS_ALL)) {
					// consider a block at least two access_all in sequence :,:,..
					// starting from 0-th position, otherwise the memory to be copied will be
					// non-contiguous
					if (!rhs_expression && compatible_matrices && j - 2 >= copyfrom && copyfrom == 0) {
						copyBlock = new Tuple<Integer, Integer>(copyfrom, j);
						/* single block instead of for(), this allows to redefine variables */
						String blockTabs = "";
						for (int innestedCounter = 0; innestedCounter < copyfrom; innestedCounter++) {
							blockTabs += TAB;
						}

						for (int t = 0; t < j; ++t) {
							buf.append(blockTabs).append("{").append(NL);
							blockTabs += TAB;
						}
					} else
						// if just 1 all, then use the loop
						buf.append(allTmpBuf);
					copyfrom = -1;
				}

				if (paramType instanceof DimensionType) {
					hasParamMatrices = true;
					hasLoops = true;
					// create the length string
					DimensionType dtype = ((DimensionType) paramType);
					IntType[] dims = TypeUtils.realDims(dtype.dims());
					// param should always be an int (if double, cast to int and give warning)
					GType ofType = dtype.of();
					// add for loops
					String[] counterVariableNames = new String[dims.length];
					String[] dimsAsString = new String[dims.length];
					int start = dims.length - 1;
					int stop = 0;
					int step = -1;
					if (!ROWMAJOR) {
						// actually C layout is colmajor, so iterate in order
						start = 0;
						stop = dims.length - 1;
						step = 1;
					}
					// iterate in inverse order, the first dimension should be the inner-most for
					// rowmajor layout
					for (int d = start; d >= stop; d += step) {
						String length = dims[d].valueAsString();
						dimsAsString[d] = length;
						String counterVariableName = counterPrefix + counterSuffix;
						counterVariableNames[d] = counterVariableName;

						buf.append(tabsToBeAppended).append("for(int ").append(counterVariableName).append("=1; ");
						buf.append(counterVariableName).append("<=").append(length).append(";");
						buf.append(counterVariableName).append("++");
						buf.append("){").append(NL);

						innestedLevel++;
						counterSuffix++;
						if (linearIndex) {
							// k = 3x3, a = 3x3, a(k) = 3x3
							forLoopDimensions.add(length);
							assignDimensions.add(length);
							isAccessIndex.add(false);
						}
					}
					// k = 3x3, a = 3x3, a(:,k) = 3x9
					if (!linearIndex) {
						String mul = String.join(" * ", dimsAsString);
						forLoopDimensions.add(mul);
						assignDimensions.add(mul);
						isAccessIndex.add(false);
					}

					if (paramType instanceof MatrixType) {
						if (ofType.equals(BType.BOOL)) {
							// if matrix of bool, use the indices of the non-zero entries of the matrix
							// as indices
							String checkPositive;
							if (dims.length == 1) {
								checkPositive = paramNode.symbol() + ".matrix[" + counterVariableNames[0] + "-1]";
								accessParameters.add(counterVariableNames[0]);
							} else {
								String[] accessPosition = new String[counterVariableNames.length];
								for (int ap = 0; ap < counterVariableNames.length; ++ap)
									accessPosition[ap] = "(" + counterVariableNames[ap] + "-1) * " + paramNode.symbol()
											+ STRUCT_ACCESS + "poly_basis[" + ap + "]";

								checkPositive = paramNode.symbol() + STRUCT_ACCESS + "matrix["
										+ String.join(" + ", accessPosition) + "]";

								for (String counterVariable : counterVariableNames)
									accessParameters.add(counterVariable);
							}
							// index of this accessParameter in accessParameters
							checkNonNull.add(checkPositive);
						} else if (ofType.isCastableToScalar()) {
							String cast = "";
							if (ofType.equals(BType.SCALAR)) {
								// add cast and emit warning
								TypeException ex = new TypeException(CErrorMessage.WARN_MATRIX_ACCESS_WITH_SCALAR,
										curRoot, lhs_symbol, params.get(j).name());
								curRoot.compilationUnit().addWarning(ex);
								cast = "(int) ";
							}
							// if matrix of non-bool, use the values of the matrix as indices
							if (dims.length == 1)
								accessParameters
										.add(cast + paramNode.symbol() + ".matrix[" + counterVariableNames[0] + "-1]");
							else {
								String[] accessPosition = new String[counterVariableNames.length];
								for (int ap = 0; ap < counterVariableNames.length; ++ap)
									accessPosition[ap] = "(" + counterVariableNames[ap] + "-1) * " + paramNode.symbol()
											+ STRUCT_ACCESS + "poly_basis[" + ap + "]";
								accessParameters.add(cast + paramNode.symbol() + STRUCT_ACCESS + "matrix["
										+ String.join(" + ", accessPosition) + "]");
							}
						}
					} else if (paramType instanceof SliceType) {
						String cast = "";
						if (ofType.equals(BType.SCALAR)) {
							// add cast and emit warning
							TypeException ex = new TypeException(CErrorMessage.WARN_MATRIX_ACCESS_WITH_SCALAR, curRoot,
									lhs_symbol, params.get(j).name());
							curRoot.compilationUnit().addWarning(ex);
							cast = "(int) ";
						}
						accessParameters.add(cast + getSequenceAccess(paramNode, counterVariableNames[0] + "-1"));
					} else
						throw new UndefinedTranslationException(CErrorMessage.INTERNAL_SLICE_UNSUPPORTED, lhsNode,
								paramType);

					nonOnesParam.add(j);

					for (String s : counterVariableNames) {
						counterToBeUsed.add(s);
						if (ndim >= matrixDimensions.length) {
							accessDimensions.add(matrixDimensions[matrixDimensions.length - 1].valueAsString());
							lhsDimensions.add(matrixDimensions[matrixDimensions.length - 1].valueAsString());
							lhsNode.compilationUnit().addWarning(new TypeException(
									CErrorMessage.WARN_MATRIX_DIMENSIONS_EXCEDED, lhsNode.parent(), paramNode.name()));

						} else {
							accessDimensions.add(matrixDimensions[ndim].valueAsString());
							lhsDimensions.add(matrixDimensions[ndim++].valueAsString());
						}
					}

				} else if (paramType.equals(BType.MATRIX_ACCESS_ALL)) {
					String dimensionTypeName;
					// matrix dimension to consider, based on rhs type
					IntType[] matrixaccessAllDims;
					IntType curDim;// = ((IndexType) paramType).dimension();

					if (rhsType.isCastableToMatrix()) {
						// use rhstype dimension here
						DimensionType drhsType = (DimensionType) rhsType;
						matrixaccessAllDims = TypeUtils.realDims(drhsType.dims());
						if (matrixDimensions.length > matrixaccessAllDims.length)
							curDim = matrixaccessAllDims[nrhsdim++];
						else
							curDim = matrixaccessAllDims[ndim];
					} else {
						matrixaccessAllDims = matrixDimensions;
						curDim = matrixaccessAllDims[ndim];
					}
					if (!linearIndex)
						dimensionTypeName = curDim.valueAsString();
					else {
						List<String> dimsList = new ArrayList<String>(matrixaccessAllDims.length);
						for (IntType matrixDim : matrixaccessAllDims)
							dimsList.add(matrixDim.valueAsString());
						// beware: later we can split on ' * ' don't change this if you don't change
						// that
						dimensionTypeName = String.join(" * ", dimsList);
					}

					String counterVariableName = counterPrefix + counterSuffix;

					counterToBeUsed.add(counterVariableName);

					// this will then converge to the buf if block copy won't apply
					allTmpBuf.append(tabsToBeAppended).append("for(int ").append(counterVariableName).append("=1; ");
					allTmpBuf.append(counterVariableName).append("<=").append(dimensionTypeName).append(";");
					allTmpBuf.append(counterVariableName).append("++");
					allTmpBuf.append("){").append(NL);

					hasLoops = true;
					forLoopDimensions.add(dimensionTypeName);
					assignDimensions.add(dimensionTypeName);
					// if linearIndex is true (case a(:) = ...), the memory should be only enlarged,
					// not shrinked
					// otherwise it is a dimension-wide access and can be shrinked
					isAccessIndex.add(linearIndex);
					innestedLevel++;
					counterSuffix++;

					accessParameters.add(counterVariableName);
					nonOnesParam.add(j);
					if (ndim >= matrixDimensions.length) {
						accessDimensions.add(matrixDimensions[matrixDimensions.length - 1].valueAsString());
						lhsDimensions.add(matrixDimensions[matrixDimensions.length - 1].valueAsString());
						lhsNode.compilationUnit().addWarning(new TypeException(
								CErrorMessage.WARN_MATRIX_DIMENSIONS_EXCEDED, lhsNode.parent(), paramNode));
					} else {
						accessDimensions.add(matrixDimensions[ndim].valueAsString());
						lhsDimensions.add(matrixDimensions[ndim++].valueAsString());
					}
				} else if (paramType.equals(BType.MATRIX_ACCESS_LAST)) {
					String lastAccessName;
					if (ndim >= matrixDimensions.length) {
						lastAccessName = matrixDimensions[matrixDimensions.length - 1].valueAsString();
						lhsNode.compilationUnit().addWarning(new TypeException(
								CErrorMessage.WARN_MATRIX_DIMENSIONS_EXCEDED, lhsNode.parent(), paramNode));
					} else
						lastAccessName = matrixDimensions[ndim++].valueAsString();

					forLoopDimensions.add(lastAccessName);
					isAccessIndex.add(true);

					accessParameters.add(lastAccessName);
					nonOnesParam.add(j);
					accessDimensions.add(lastAccessName);
				} else if (paramType instanceof ValuedType) {
					String cast = "";
					if (paramType.equals(BType.SCALAR)) {
						// add cast and emit warning
						TypeException ex = new TypeException(CErrorMessage.WARN_MATRIX_ACCESS_WITH_SCALAR, curRoot,
								lhs_symbol, params.get(j).name());
						curRoot.compilationUnit().addWarning(ex);
						cast = "(int) ";
					}

					// this can be an expression..
					String value;
					if (lhs_params_symbols == null || lhs_params_symbols.length < j)
						value = paramNode.symbol();
					else
						value = lhs_params_symbols[j];
					accessParameters.add(cast + value);
					forLoopDimensions.add(cast + value);
					isAccessIndex.add(true);
					if (!((ValuedType) paramType).hasIntValue() || !((ValuedType) paramType).valueAsInt().equals(1))
						nonOnesParam.add(j);
					if (ndim >= matrixDimensions.length) {
						// if access with scalar check for value
						accessDimensions.add(cast + params.get(j).symbol());

						ValuedType<?> valuedType = ((ValuedType) paramType);
						if (valuedType.hasIntValue() && valuedType.valueAsInt() > 1)
							curRoot.compilationUnit().addWarning(new TypeException(
									CErrorMessage.WARN_MATRIX_DIMENSIONS_EXCEDED, lhsNode.parent(), paramNode));

					} else {
						accessDimensions.add(matrixDimensions[ndim++].valueAsString());
					}
				}
			}

			boolean sliceWithBooleanMatrix = !checkNonNull.isEmpty();
			if (!sliceWithBooleanMatrix && !assignDimensions.isEmpty() && !lhsDimensions.isEmpty()) {
				// add check on dimension (debug only)
				String lhsm = String.join("*", lhsDimensions);
				String am = String.join("*", assignDimensions);
				buf.append("ASSERT(" + am + " <= " + lhsm
						+ ", \"left-hand-side (%d) dimension is lower than right-hand-side (%d)\", " + lhsm + ", " + am
						+ ");" + NL);
			}

			if (copyfrom != -1) {
				// consider a block at least two access_all in sequence :,:,..
				// starting from 0-th position
				if (!rhs_expression && compatible_matrices && params.size() - 2 >= copyfrom && copyfrom == 0) {
					copyBlock = new Tuple<Integer, Integer>(copyfrom, params.size());
					/* single block instead of for(), this allows to redefine variables */
					buf.append("{");
				} else
					// if just 1 all, then use the loop
					buf.append(allTmpBuf);
				copyfrom = -1;
			}

			String tabsToBeAppended = "";
			for (int innestedCounter = 0; innestedCounter < innestedLevel; innestedCounter++) {
				tabsToBeAppended += TAB;
			}

			// write the body of the assign
			if (sliceWithBooleanMatrix) {
				buf.append(tabsToBeAppended).append("if(");
				// check if checkNonNull indexes are non-zero
				List<String> nonNullCheckList = new LinkedList<>();
				for (String nonNull : checkNonNull)
					nonNullCheckList.add(nonNull + " > 0");
				buf.append(String.join(" && ", nonNullCheckList));
				buf.append("){").append(NL);
				buf.append(tabsToBeAppended).append(TAB);

				// since this for will assign only when the condition is met,
				// the RHS indices should be replaced with contiguously incremented value
				// otherwise it won't be accessed linearly
				List<String> newCounterToBeUsed = new ArrayList<String>(counterToBeUsed.size());
				String prefix = "_rhs";
				String newcnt;
				for (String cnt : counterToBeUsed) {
					newcnt = prefix + cnt;
					boolean created = createVariable(GType.get(BType.INT), newcnt, null, NodeType.ID, "1", false, false,
							VarVisibility.LOCAL_TO_BLOCK, null, false);
					if (!created)
						buf.insert(0, newcnt + " = 1;" + NL);
					// add ++ so that it will be automatically incremented after each use
					newCounterToBeUsed.add(newcnt);
				}
				// replace
				counterToBeUsed = newCounterToBeUsed;
			}

			// fill-in missing variables set them to 1 because the flat index to the first
			// position only
			// will be computed prior to the memcpy
			if (copyBlock != null)
				for (int b = copyBlock.first(); b < copyBlock.second(); ++b)
					buf.append(tabsToBeAppended).append("int " + counterPrefix + Integer.toString(b)).append(" = 1;")
							.append(NL);

			if (!enablePreallocatedCheck && lhsNode.hasAttr(CAttr.PREALLOCATED)) {
				// do nothing already pre-allocated
			} else if (hasLoops && !hasParamMatrices) {
				// !hasParamMatrices because we can't pre-allocate based
				// on a parameter matrix (we don't know the maximum value of the index parameter
				// matrix)
				// add a single ensure-capacity before everything
				// (more efficient than multiple checkCapacity)
				// -- DIMENSIONS TO BE CHECKED BECAUSE ACCESS IS POINTWISE (EX. a(..., 3, ...)
				String checkDimensions[] = new String[forLoopDimensions.size()];
				// -- DIMENSIONS TO BE ENSURED BECAUSE ACCESS COPY A WHOLE DIMENSION (EX. a(...,
				// vec.dim3, ...)
				String ensureDimensions[] = new String[forLoopDimensions.size()];
				IntType[] plainMatrixDims = matrixType.dims();
				boolean allAccessIndex = true;
				boolean allDimensionUpdate = true;

				// generic case
				for (int d = 0; d < forLoopDimensions.size(); ++d) {
					if (isAccessIndex.get(d)) {
						allDimensionUpdate = false;
						// in the ensure part the access indexes should be ignored
						// we only want to update the whole dimension accesses, that should be updated
						ensureDimensions[d] = TypeUtils.matrixDimName(matrixType.name(), d + 1);
					} else {
						allAccessIndex = false;
						ensureDimensions[d] = forLoopDimensions.get(d);
					}
					// in check dimension we want just to check if the access indexes
					// are within the dimension they refer to and update the matrix dimension
					// if they are higher
					checkDimensions[d] = forLoopDimensions.get(d);
				}

				if (linearIndex && getExprGeneralized(params.get(0)).equals(BType.MATRIX_ACCESS_ALL)) {
					// special case of a(:) = ...
					// in this case we shouldn't check anything because updating dimensions is not
					// allowed in this context
				} else if (allAccessIndex)
					// do only the check (checkOnly = true)
					ensureMatrixMemory(preamble, lhsNode, null, matrixType, checkDimensions, true, false, false, null);
				else if (allDimensionUpdate)
					// do only the ensure (checkOnly = false)
					ensureMatrixMemory(preamble, lhsNode, null, matrixType,
							forLoopDimensions.toArray(new String[forLoopDimensions.size()]), false, false, false, null);
				else {
					// if a combination, do both
					// do the check (checkOnly = true)
					ensureMatrixMemory(preamble, lhsNode, null, matrixType, checkDimensions, true, false, false, null);
					// do the ensure (checkOnly = false) that eventually reduce the dimension
					ensureMatrixMemory(preamble, lhsNode, null, matrixType, ensureDimensions, false, false, false,
							null);
				}
			} else
				// NOTE: last true parameter is checkOnly!
				// meaning that if this access won't overcome matrix dimension, the matrix
				// dimension won't be changed
				ensureMatrixMemory(buf, lhsNode, null, matrixType,
						accessParameters.toArray(new String[accessParameters.size()]), true, false, false, null);

			if (sliceWithBooleanMatrix)
				buf.append(tabsToBeAppended).append(TAB);

			// build current position in matrix
			StringBuffer lhsPos = new StringBuffer();
			if (matrixDimensions.length == 1) {
				// look for the non-trivial dimension position according to input type
				// if 1d matrix it's more convenient to access directly as a flat array
				Integer pos;
				if (!nonOnesParam.isEmpty())
					pos = nonOnesParam.get(0);
				else if (!nonOnesInMatrixPos.isEmpty())
					pos = (nonOnesInMatrixPos.get(0) < accessParameters.size()) ? nonOnesInMatrixPos.get(0) : 0;
				else
					pos = 0;
				lhsPos.append(accessParameters.get(pos)).append("-1");
			} else if (accessParameters.size() > 1) {
				if (matrixDimensions.length != accessParameters.size()) {
					lhsPos.append("matrixAccess(").append(lhs_symbol + STRUCT_ACCESS + "poly_basis").append(", ")
							.append(matrixDimensions.length).append(", ");
					lhsPos.append(accessParameters.size()).append(", ");

					List<String> dimensionsList = new ArrayList<String>();
					for (int k = 0; k < accessDimensions.size(); k++) {
						dimensionsList.add(accessDimensions.get(k));
					}

					String dimensionsWithSeparator = String.join(", ", dimensionsList);
					String accessParamsWithSeparator = String.join(", ", accessParameters);

					lhsPos.append(dimensionsWithSeparator).append(", ");
					lhsPos.append(accessParamsWithSeparator).append(")");
				} else {
					String[] accessPosition = new String[accessParameters.size()];
					for (int ap = 0; ap < matrixDimensions.length; ++ap)
						accessPosition[ap] = "(" + accessParameters.get(ap) + "-1) * " + lhs_symbol + STRUCT_ACCESS
								+ "poly_basis[" + ap + "]";
					lhsPos.append(String.join(" + ", accessPosition));
				}
			} else {
				// if nd matrix but just 1 counter use it as flat access
				// assume source program uses colmajor
				if (ROWMAJOR) {
					lhsPos.append("colMajor2RowMajor(");
					lhsPos.append(accessParameters.get(0)).append("-1").append(", ");
					lhsPos.append(matrixDimensions[0].valueAsString()).append(", ");
					lhsPos.append(matrixDimensions[1].valueAsString());
					lhsPos.append(")");
				} else {
					// was counterToBeUsed, I think this was wrong
					lhsPos.append(accessParameters.get(0)).append("-1");
				}
			}

			// right hand position
			StringBuffer rhsPos = new StringBuffer();
			if (rhsType instanceof MatrixType) {
				MatrixType rMatrixType = (MatrixType) rhsType;
				IntType[] rMatrixTypeDimensions = TypeUtils.realDims(rMatrixType.dims());

				if (counterToBeUsed.size() > 1) {
					if (rMatrixTypeDimensions.length != counterToBeUsed.size()) {
						if (rMatrixTypeDimensions.length == 1) {
							// if rhs is just 1D then the last element vary and all the others should be
							// fixed to 1
							rhsPos.append("(" + counterToBeUsed.get(counterToBeUsed.size() - 1) + " - 1) * "
									+ rhsVariable + STRUCT_ACCESS + "poly_basis[0]");
						} else {
							rhsPos.append("matrixAccess(").append(rhsVariable + STRUCT_ACCESS + "poly_basis")
									.append(", ").append(rMatrixTypeDimensions.length).append(",");
							rhsPos.append(counterToBeUsed.size()).append(",");

							List<String> rMatrixTypeDimensionAsStrings = new ArrayList<String>();
							for (int k = 0; k < rMatrixTypeDimensions.length; k++) {
								rMatrixTypeDimensionAsStrings.add(rMatrixTypeDimensions[k].valueAsString());
							}

							String rDimensionsWithSeparator = Strings.join(rMatrixTypeDimensionAsStrings, ',');
							String rAccessParamsWithSeparator = Strings.join(counterToBeUsed, ',');

							rhsPos.append(rDimensionsWithSeparator).append(",");
							rhsPos.append(rAccessParamsWithSeparator).append(")");
						}
					} else {
						String[] accessPosition = new String[counterToBeUsed.size()];
						for (int ap = 0; ap < rMatrixTypeDimensions.length; ++ap)
							accessPosition[ap] = "(" + counterToBeUsed.get(ap) + "-1) * " + rhsVariable + STRUCT_ACCESS
									+ "poly_basis[" + ap + "]";
						rhsPos.append(String.join(" + ", accessPosition));
					}
				} else if (rMatrixTypeDimensions.length == 1)
					// if 1d matrix it's more convenient to access directly as a flat array
					rhsPos.append(counterToBeUsed.get(0)).append("-1");
				else if(counterToBeUsed.size() == 0)
					// expected to have some indices to access a non-vector matrix (at least nxn)
					// but none available.
					// this is probably an error in the type identification of the RHS upstream
					throw new TypeException(CErrorMessage.INTERNAL_MISMATCHED_MATRIX_ACCESS, 
							rhsNode, rhsNode.code());
				else {
					// if nd matrix but just 1 counter use it as flat access
					// assume source program uses colmajor
					if (ROWMAJOR) {
						rhsPos.append("colMajor2RowMajor(");
						rhsPos.append(counterToBeUsed.get(0)).append("-1").append(", ");
						rhsPos.append(rMatrixTypeDimensions[0].valueAsString()).append(", ");
						rhsPos.append(rMatrixTypeDimensions[1].valueAsString());
						rhsPos.append(")");
					} else {
						rhsPos.append(counterToBeUsed.get(0)).append("-1");
					}
				}
			}

			/* remember: if you are here it means lhs is matrix */
			/* ## BLOCK COPY WHEN MULTIPLE "ALL" ACCESS ARE PRESENT ## */
			if (copyBlock != null) {
				/* DEST */
				buf.append(tabsToBeAppended).append("memcpy(").append(lhs_symbol).append(STRUCT_ACCESS).append("matrix")
						.append(" + ").append(lhsPos.toString());
				buf.append(", ");
				/* SRC */
				buf.append(rhs_symbol).append(STRUCT_ACCESS).append("matrix").append(" + ").append(rhsPos.toString());
				buf.append(", ");
				/* HOW MANY */
				buf.append(String.join(" * ", forLoopDimensions.subList(copyBlock.first(), copyBlock.second())));
				/* size of every single cell */
				buf.append(" * sizeof(").append(exprTypeToCType(matrixType.of())).append(")");
				buf.append(");").append(NL);
			} else {
				/* ## POINT TO POINT COPY, DEFAULT IF NO OTHER OPTIMIZATION IS POSSIBLE ## */
				// write LHS side
				buf.append(tabsToBeAppended).append(lhs_symbol).append(STRUCT_ACCESS).append("matrix[")
						.append(lhsPos.toString()).append("]=");
				if (rhsType.isCastableToScalar() || rhs_expression) {
					// rhs scalar or complex expression
					buf.append(rhs_symbol.replaceAll(EXPR_INDEX, rhsPos.toString())).append(";").append(NL);
				} else if (rhsType instanceof MatrixType) {
					if (!compatible_matrices)
						buf.append(" (").append(exprTypeToCType(matrixType.of())).append(") ");
					buf.append(rhs_symbol).append(STRUCT_ACCESS).append("matrix[");
					buf.append(rhsPos.toString());
					buf.append("];").append(NL);
				} else
					throw new TypeException(CErrorMessage.UNSUPPORTED_TYPE, lhsNode.parent(), rhsType,
							lhsNode.parent().name());
			}

			if (sliceWithBooleanMatrix) {
				// increment counters
				// the last for
				GType rhsIterNodeExpr = getExprGeneralized(rhsIterNode);
				if (rhsIterNodeExpr instanceof DimensionType) {
					buf.append(tabsToBeAppended).append(TAB).append(counterToBeUsed.get(counterToBeUsed.size() - 1))
							.append("++").append(";").append(NL);

					if (counterToBeUsed.size() > 1) {
						// all the other for.. should be incremented only when needed
						DimensionType rhsNodeType = (DimensionType) rhsIterNodeExpr;

						// if (rhsNodeType.dims().length >= counterToBeUsed.size()) the first
						// counterToBeUsed.size() - rhsNodeType.dims().length
						// rhs counters should remain set to 1 for the complete assignment
						for (int c = counterToBeUsed.size() - 2; c >= counterToBeUsed.size()
								- rhsNodeType.dims().length; --c) {
							// if prev dimension exceeded the boundary
							buf.append(tabsToBeAppended).append(TAB).append("if(").append(counterToBeUsed.get(c + 1))
									.append(" > ").append(TypeUtils.matrixDimName(rhsVariable, c + 2)).append("){")
									.append(NL);
							// increment this dimension
							buf.append(tabsToBeAppended).append(TAB).append(TAB).append(counterToBeUsed.get(c))
									.append("++").append(";").append(NL);
							// set to 1 the previous one
							buf.append(tabsToBeAppended).append(TAB).append(TAB).append(counterToBeUsed.get(c + 1))
									.append(" = 1;").append(NL);
							// close if
							buf.append(tabsToBeAppended).append(TAB).append("}").append(NL);
						}
					}
				}
				// close if
				buf.append(tabsToBeAppended).append("}").append(NL);
			}
			for (int j = 0; j < counterSuffix; j++) {

				innestedLevel--;

				tabsToBeAppended = "";
				for (int innestedCounter = 0; innestedCounter < innestedLevel; innestedCounter++) {
					tabsToBeAppended += TAB;
				}
				buf.append(tabsToBeAppended).append("}").append(NL);
			}
			// DIRECT ASSIGNMENT
		} else if (
		// scalar case
		(lhsType.isCastableToString() || (lhsType.isCastableToScalar() && rhsType.isCastableToScalar())
		// slice case
				|| (lhsType.equals(BType.MATRIX_ACCESS_SLICE) && rhsType.equals(BType.MATRIX_ACCESS_SLICE))))
			buf.append(lhs_symbol).append(" = ").append(rhs_symbol).append(";");
		else if (lhsType.equals(BType.MATRIX_ACCESS_SLICE) && rhsType.isCastableToScalar())

		{
			// slice = number -> create a slice with start=number, end=number, step=1
			buf.append(sliceInit(lhs_symbol, (SliceType) lhsType, rhs_symbol, "1", rhs_symbol, "1", true));
		} else if (lhsType.equals(BType.MATRIX) && rhsType.isCastableToScalar()) {
			String[] dimsToCheck = new String[((DimsType) lhsType).dims().length];
			for (int i = 0; i < ((DimsType) lhsType).dims().length; ++i)
				dimsToCheck[i] = "1";
			// direct assign of scalar to matrix. assign to the first element.
			ensureMatrixMemory(buf, lhsNode, null, (MatrixType) lhsType, dimsToCheck);
			buf.append(lhs_symbol).append(STRUCT_ACCESS + "matrix[0] = ").append(rhs_symbol).append(";");
		} else if ((lhsType.equals(BType.MATRIX)) && (rhsType.equals(BType.MATRIX))) {
			List<String> inMatrixDims = nodeDimsToSymbolicReference(rhsIterNode);
			boolean ensured = ensureMatrixMemory(buf, lhsNode, inMatrixDims.toArray(new String[inMatrixDims.size()]));

			String totalDimension = Strings.join(inMatrixDims, '*');

			GType lof = ((MatrixType) lhsType).of();
			GType rof = ((MatrixType) rhsType).of();
			String cType = exprTypeToCType(lof);

			if (rhsNode.hasAttr(CAttr.PREALLOCATED)) {
				// if a = b and b preallocated, also a is preallocated
				lhsNode.parent(NodeType.STATEMENT_LIST).propagateAttribute(CAttr.PREALLOCATED, true, lhs_symbol);
			}

			if (!rhs_expression && lof.equals(rof.type()))
				// optimize with memcpy
				buf.append("memcpy(" + lhs_symbol + STRUCT_ACCESS + "matrix," + rhs_symbol + STRUCT_ACCESS
						+ "matrix, sizeof(" + cType + ")*" + totalDimension + ");").append(NL);
			else {
				// if matrices of different type fallback to for loop
				// buf.append("#pragma omp parallel for if(" + totalDimension + " >
				// PARALLELIZABLE_FOR_TRESHOLD)")
				// .append(NL);
				buf.append("#pragma omp smid").append(NL);
				buf.append("for(int " + EXPR_INDEX + "=0; " + EXPR_INDEX + " < " + totalDimension + "; ++" + EXPR_INDEX
						+ ")").append(NL);
				buf.append(TAB).append(lhs_symbol + STRUCT_ACCESS + "matrix[").append(EXPR_INDEX).append("] = (")
						.append(cType).append(") (").append(rhs_symbol).append(")");
				if (!rhs_expression)
					buf.append(STRUCT_ACCESS + "matrix[").append(EXPR_INDEX).append("]");
				buf.append(";").append(NL);
			}

			if (!ensured) {
				DimensionType lhsDimType = (DimensionType) lhsType;
				// just update dims, this happens when the LHS matrix is statically defined
				for (int i = 1; i <= inMatrixDims.size() && i <= lhsDimType.dims().length; ++i)
					buf.append(TypeUtils.matrixDimName(lhs_symbol, i)).append(" = ").append(inMatrixDims.get(i - 1))
							.append(";").append(NL);
			}
		} else if (lhsType.equals(BType.STRUCT) && (rhsType.equals(BType.STRUCT))) {
			buf.append(lhs_symbol).append(" = ").append(rhs_symbol).append(";");
		} else
			throw new UndefinedTranslationException(CErrorMessage.INTERNAL_ASSIGN_INCOMPATIBLE_TYPES, lhsNode, lhsType,
					rhsType);

		preamble.append(buf);
		return preamble.toString();
	}

	private List<String> getChildResults(Deque<String> results, int howmany) {
		LinkedList<String> child_results = new LinkedList<String>();
		// get my params from stack
		String res;
		for (int i = 0; i < howmany; ++i) {
			res = results.poll();
			if (res != null)
				child_results.addFirst(res);
		}
		return child_results;
	}

	private boolean createVariable(AASTNode curRoot, boolean onlyReferences, VarVisibility v)
			throws UndefinedTranslationException, TypeException {
		return createVariable(curRoot, null, onlyReferences, false, v);
	}

	protected boolean createVariable(GType type, String symbol, NodeType nodeType, String value, boolean onlyReferences,
			VarVisibility v) throws UndefinedTranslationException, TypeException {
		return createVariable(type, symbol, null, nodeType, value, onlyReferences, false, v, false);
	}

	protected boolean createVariable(GType type, String symbol, NodeType nodeType, String value, boolean onlyReferences,
			boolean forceStatic, VarVisibility v, boolean extern) throws UndefinedTranslationException, TypeException {
		return createVariable(type, symbol, null, nodeType, value, onlyReferences, forceStatic, v, null, extern);
	}

	protected boolean createVariable(GType type, String symbol, String assignSymbol, NodeType nodeType, String value,
			boolean onlyReferences, boolean forceStatic, VarVisibility v, boolean extern)
			throws UndefinedTranslationException, TypeException {
		return createVariable(type, symbol, assignSymbol, nodeType, value, onlyReferences, forceStatic, v, null,
				extern);
	}

	protected boolean createVariable(GType type, String symbol, NodeType nodeType, String value, boolean onlyReferences,
			boolean forceStatic, VarVisibility v, StringBuffer buf, boolean extern)
			throws UndefinedTranslationException, TypeException {
		return createVariable(type, symbol, null, nodeType, value, onlyReferences, forceStatic, v, buf, extern);
	}

	protected boolean createVariable(GType type, String symbol, String assignSymbol, NodeType nodeType, String value,
			boolean onlyReferences, boolean forceStatic, VarVisibility v, StringBuffer buf, boolean extern)
			throws UndefinedTranslationException, TypeException {
		return createVariable(type, symbol, assignSymbol, nodeType, value, onlyReferences, forceStatic, v, buf, null,
				extern);
	}

	/**
	 * 
	 * @param type
	 * @param symbol
	 * @param nodeType
	 * @param value
	 * @param onlyReferences
	 * @param forceStatic
	 * @param v              buffer to use, ignored if buf given
	 * @param buf            buffer to use for translation
	 * @param initBuf        used to write initialization code for the variables
	 *                       defined in buf (used only for global variables, where
	 *                       initialization code can't be in the same place of
	 *                       declaration code)
	 * @param extern:        has effect only if varvisibility=global, dictates that
	 *                       this variable is defined in an external compilation
	 *                       unit.
	 * @return
	 * @throws UndefinedTranslationException
	 * @throws TypeException
	 */
	protected boolean createVariable(GType type, String symbol, String assignSymbol, NodeType nodeType, String value,
			boolean onlyReferences, boolean forceStatic, VarVisibility v, StringBuffer buf, StringBuffer initBuf,
			boolean extern) throws UndefinedTranslationException, TypeException {

		boolean alreadyDeclared = false;
		boolean onlyRefs = onlyReferences;
		if (curFun != null) {
			// if it's the function output, you don't have to redeclare it!!
			List<String> outputSymbols = curFun.getFunctionOutputSymbols();
			List<String> inputSymbols = curFun.getFunctionParams();
			if (outputSymbols.size() == 1 && outputSymbols.get(0).equals(symbol)) {
				// if in output symbol it's already defined, but should be initialized
				alreadyDeclared = true;
				onlyRefs = false;
			} else if (inputSymbols.contains(symbol)) {
				// if true call itself with onlyReferences = true, alreadyDeclared = true
				// to avoid 1: allocation of static/dynamic memory, 2 variable declaration
				alreadyDeclared = true;
				onlyRefs = true;
			}
		}

		// in case of v = GLOBAL assignBuffer should be different from the declaration
		// buffer
		StringBuffer finalBuffer, assignBuffer = null;
		boolean indent = !v.isGlobal();
		Set<String> symbolsCtx = null;
		if (buf == null) {
			Tuple<StringBuffer, Set<String>> bufCtx = getDeclarationBuffer(v);
			finalBuffer = bufCtx.first();
			symbolsCtx = bufCtx.second();
			if (symbolsCtx.contains(symbol))
				return false;
			symbolsCtx.add(symbol);
		} else {
			finalBuffer = buf;
		}

		if (initBuf == null) {
			if (v.isGlobal())
				// assignment should happen inside function
				assignBuffer = getDeclarationBuffer(VarVisibility.LOCAL_TO_FUNCTION).first();
		} else
			assignBuffer = initBuf;

		StringBuffer finalDeclaration = new StringBuffer();
		StringBuffer finalAssignment = new StringBuffer();

		if (extern && v.isGlobal())
			finalDeclaration.append("extern ");

		GType stype;
		boolean isDynamicMatrix = false;
		MatrixType mtype = null;
		switch (type.type()) {
		case INT:
			if (alreadyDeclared)
				// don't redeclare
				break;

			if (value != null)
				finalDeclaration.append(createIntScalar(symbol, value, onlyRefs, v));
			else
				finalDeclaration.append(createIntScalar(symbol, onlyRefs, v));
			break;
		case BOOL:
			if (alreadyDeclared)
				// don't redeclare
				break;

			if (value != null)
				finalDeclaration.append(createBoolScalar(symbol, value, onlyRefs, v));
			else
				finalDeclaration.append(createBoolScalar(symbol, onlyRefs, v));
			break;
		case SCALAR:
			if (alreadyDeclared)
				// don't redeclare
				break;

			if (value != null)
				finalDeclaration.append(createScalar(symbol, value, onlyRefs, v));
			else
				finalDeclaration.append(createScalar(symbol, onlyRefs, v));
			break;
		case MATRIX_ACCESS_SLICE:
			if (alreadyDeclared)
				// don't redeclare
				break;

			stype = ((SliceType) type).of();
			// symbol = ((SliceType) type).name();
			finalDeclaration.append(createSlice(symbol, stype, value, v));
			break;
		case MATRIX:
			mtype = ((MatrixType) type);
			isDynamicMatrix = nodeDimsToInt(mtype.dims()) == null;
			// symbol = mtype.name();
			stype = mtype.of();

			String defaultValue = exprTypeToDefaultValue(mtype);
			Tuple<String, String> created;
			switch (stype.type()) {
			case INT:
				created = createMatrixRef(symbol, "int", value, mtype.dims(), mtype.isSparse(), onlyRefs, forceStatic,
						v, alreadyDeclared, defaultValue, extern && v.isGlobal());
				break;
			case BOOL:
				created = createMatrixRef(symbol, "int", value, mtype.dims(), mtype.isSparse(), onlyRefs, forceStatic,
						v, alreadyDeclared, defaultValue, extern && v.isGlobal());
				break;
			case SCALAR:
				created = createMatrixRef(symbol, "double", value, mtype.dims(), mtype.isSparse(), onlyRefs,
						forceStatic, v, alreadyDeclared, defaultValue, extern && v.isGlobal());
				break;
			default:
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_MATRIX_ELEMENT_TYPE, null, stype,
						symbol);
			}
			finalDeclaration.append(created.first());
			// if assign buffer is different from declare buffer
			if (assignBuffer != null)
				finalAssignment.append(created.second());
			else
				finalDeclaration.append(created.second());
			break;
		case STRUCT:
			if (alreadyDeclared)
				// don't redeclare
				break;
			boolean pointer = onlyRefs;
			if (nodeType != null && nodeType.equals(NodeType.APPLY)) {
				pointer = true;
			}
			try {
				finalDeclaration.append(createStruct(symbol, (StructType) type, v, pointer));
				if (type.isInput() && !symbol.equals(type.inputName())
						&& (symbolsCtx == null || !symbolsCtx.contains(type.inputName()))) {
					finalDeclaration.append(createStruct(type.inputName(),
							(StructType) GType.get(BType.STRUCT).name(INPUT_TYPE).setInput(type.inputName()).setKnown(),
							v, true));
					if (symbolsCtx != null)
						symbolsCtx.add(type.inputName());
				}
			} catch (TypeException e) {
				throw new UndefinedTranslationException(e);
			} catch (IOException e) {
				throw new UndefinedTranslationException(e);
			}

			break;
		case STRING:
			if (alreadyDeclared)
				// don't redeclare
				break;

			pointer = true;
			finalDeclaration.append(createString(symbol, value, forceStatic, v, pointer));
			break;
		case FUNCTION:
			// this is not the place to create functions
			return false;
		default:
			throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TYPE, null, type, symbol);
		}
		// -- PERSISTENT VARIABLES --

		if (!alreadyDeclared && !finalDeclaration.substring(0, 6).equals("static")
				&& (v.isPersistent() || isDynamicMatrix))
			finalDeclaration.insert(0, "static __thread ");

		if (assignSymbol != null)
			finalDeclaration.append(assignSymbol).append(" = ").append(symbol).append(";").append(NL);

		/*
		 * NOT SURE THIS IS REALLY NEEDED, can't find an example where a previous value
		 * affects the next execution
		 */
		/*
		 * if (!alreadyDeclared && isDynamicMatrix) {
		 * finalDeclaration.append("if(").append(symbol).append(STRUCT_ACCESS).
		 * append("matrix != NULL) ");
		 * finalDeclaration.append("memset(").append(symbol).append(STRUCT_ACCESS).
		 * append("matrix, 0, "); List<String> dimsStr = new
		 * ArrayList<String>(mtype.dims().length); for (int d = 1; d <=
		 * mtype.dims().length; ++d) dimsStr.add(TypeUtils.matrixDimName(symbol, d));
		 * finalDeclaration.append(String.join("*", dimsStr));
		 * finalDeclaration.append(" * sizeof(").append(exprTypeToCType(mtype.of())).
		 * append("));").append(NL); }
		 */

		newTranslation(finalBuffer, finalDeclaration.toString(), indent);
		if (assignBuffer != null)
			newTranslation(assignBuffer, finalAssignment.toString(), true);
		return true;
	}

	protected String exprTypeToDefaultValue(GType type) throws UndefinedTranslationException {
		switch (type.type()) {
		case MATRIX_ACCESS_SLICE: {
			SliceType mtype = (SliceType) type;
			if (mtype.of().equals(BType.SCALAR))
				return "{doubleNull, doubleNull, doubleNull, intNull, true}";
			else
				return "{intNull, intNull, intNull, intNull, true}";
		}
		case MATRIX: {
			MatrixType mtype = (MatrixType) type;
			List<String> defaultParameters = getMatrixDefaultValues(mtype.dims().length, mtype.isSparse());
			return "{" + Strings.join(defaultParameters, ',') + "}";
		}
		case SCALAR:
			return "doubleNull";
		case MATRIX_ACCESS_LAST:
		case INT:
		case BOOL:
			return "intNull";
		case STRING:
			return "\"\"";
		case STRUCT:
			StringBuffer sb = new StringBuffer();
			StructType structType = (StructType) type;
			sb.append("{");
			List<String> def = new ArrayList<String>(structType.numberOfFields());
			Iterator<Tuple<List<GType>, String>> it = structType.iterFields();
			while (it.hasNext()) {
				Tuple<List<GType>, String> field = it.next();
				def.add(exprTypeToDefaultValue(((LinkedList<GType>) field.first()).peekLast()));
			}
			sb.append(String.join(", ", def));
			sb.append("}");
			return sb.toString();
		default:
			throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TYPE, null, type);
		}
	}

	private boolean createVariable(AASTNode curRoot, String value) throws UndefinedTranslationException, TypeException {
		return createVariable(curRoot, value, false, false, VarVisibility.LOCAL_TO_FUNCTION);
	}

	private boolean createVariable(AASTNode curRoot) throws UndefinedTranslationException, TypeException {
		return createVariable(curRoot, null, false, false, VarVisibility.LOCAL_TO_FUNCTION);
	}

	protected boolean createVariable(AASTNode curRoot, String value, boolean onlyReferences, boolean forceStatic,
			VarVisibility v) throws UndefinedTranslationException, TypeException {
		GType type = getExprGeneralized(curRoot);
		VarVisibility v2;
		boolean global = v.isGlobal() || curRoot.hasAttr(NodeAttr.GLOBAL_VAR);
		boolean globalVariableDefined = true;
		if (global) {
			v2 = VarVisibility.GLOBAL;
			globalVariableDefined = globalVariables.contains(curRoot.symbol());
		} else if (v.isPersistent() || curRoot.hasAttr(NodeAttr.PERSISTENT_VAR)
				|| curRoot.hasAttr(CAttr.PERSISTENT_VAR))
			v2 = VarVisibility.PERSISTENT;
		else
			v2 = v;
		/*
		 * if(type == null && curRoot.type().equals(NodeType.APPLY)) { type =
		 * curRoot.child(NodeType.ID).expr(); if(type instanceof DimensionType) type =
		 * ((DimensionType)type).of(); else if ( type instanceof FunctionType ) type =
		 * ((FunctionType)type).outputs()[((FunctionType)type).outputs().length-1]; }
		 */
		boolean created = false;
		if (type != null) {
			String symbol;
			String assignSymbol = null;

			if (curRoot.symbol().contains(GType.STRUCT_SEP)) {
				String[] structElements = curRoot.symbol().split("\\" + GType.STRUCT_SEP);
				// initialize just the base struct (that will initialize everything else
				symbol = structElements[0];
				// assignSymbol = curRoot.symbol();
			} else
				symbol = curRoot.symbol();

			if (global) {
				if (!globalVariableDefined) {
					// create in main script, all the others will inherit it through variables.h
					StringBuffer globalBuffer, initBuffer;
					if (isFunctionalProgram()) {
						Function mainFun = getMainFunction();
						globalBuffer = mainFun.getGlobalBuffer();
						initBuffer = mainFun.getInitializationBuffer();
					} else {
						C main = ((C) getMainCompilationUnit());
						globalBuffer = main.getGlobalBuffer();
						initBuffer = main.getInitializationBuffer();
					}
					created = createVariable(type, symbol, assignSymbol, curRoot.type(), value, onlyReferences,
							forceStatic, v2, globalBuffer, initBuffer, false);
				} // else don't do anything
			} else
				created = createVariable(type, symbol, assignSymbol, curRoot.type(), value, onlyReferences, forceStatic,
						v2, false);

			if (created) {
				logger.debug("Defined variable " + curRoot.symbol() + " of type " + type.toDebugString() + " in file "
						+ this.outputFilePath().toAbsolutePath().toString());
				// export it to other modules through variables.h header
				// don't export twice a variable declared global in two functions
				if (!globalVariableDefined)
					try {
						vwriter.write("extern " + exprTypeToCType(type) + " " + curRoot.symbol() + ";" + NL);
						globalVariables.add(curRoot.symbol());
					} catch (IOException e) {
						logger.error("Cannot write global variable on file 'variables.h'");
						logger.debug("Cannot write global variable on file 'variables.h': " + e.getMessage(), e);
					}

				AASTNode fieldAccess = curRoot.parent(NodeType.FIELDACCESS);
				AASTNode dynFieldAccess = curRoot.parent(NodeType.DYNFIELDACCESS);
				if (curRoot.type().equals(NodeType.ID) && !curRoot.parentExists(NodeType.LHS) && type.isInput()
				// not a field access, or if fieldaccess, i'm the last element. ex. csv.field1 I
				// should be field1 not csv
						&& (fieldAccess == null
								|| fieldAccess.childs().get(fieldAccess.childs().size() - 1).equals(curRoot))
						// not a field access, or if fieldaccess, i'm the last element. ex. csv.field1 I
						// should be field1 not csv
						&& (dynFieldAccess == null
								|| dynFieldAccess.childs().get(dynFieldAccess.childs().size() - 1).equals(curRoot))) {
					// if input type, load it directly
					StringBuffer sb = new StringBuffer();
					accessToInputStruct(sb, curRoot.name(), curRoot.name(), type.inputName());
					newComment(curRoot, "Assuming '" + curRoot.name() + "' as loaded from in input structure at line "
							+ curRoot.lineNumber());
					newTranslation(curRoot, sb.toString());
				}
			}
		}
		return created;
	}

	public StringBuffer getGlobalBuffer() {
		return globalVars;
	}

	public StringBuffer getInitializationBuffer() {
		return translationInitialization;
	}

	private void initMatrix(AASTNode curRoot) throws UndefinedTranslationException, TypeException {
		initMatrix(curRoot, null);
	}

	private void initMatrix(AASTNode curRoot, String defaultValue) throws UndefinedTranslationException, TypeException {
		AASTNode outNode = getOutputNode(curRoot);
		String mname = getNodeSymbol(outNode);
		MatrixType mtype = (MatrixType) getExprGeneralized(curRoot);
		switch (mtype.of().type()) {
		case INT:
			createIndexMatrixRef(mname, mtype.dims(), VarVisibility.LOCAL_TO_FUNCTION, defaultValue);
			break;
		case BOOL:
			createBoolMatrixRef(mname, mtype.dims(), VarVisibility.LOCAL_TO_FUNCTION, defaultValue);
			break;
		case SCALAR:
			createMatrixRef(mname, mtype.dims(), VarVisibility.LOCAL_TO_FUNCTION, defaultValue);
			break;
		default:
			throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_MATRIX_ELEMENT_TYPE, curRoot, mtype.of(),
					mname);
		}
	}

	private String getUnaryOperand(AASTNode curRoot) throws TypeException {
		switch (curRoot.type()) {
		case PLUS:
			return "+";
		case MINUS:
			return "-";
		case ELEMENTWISE_TIMES:
		case TIMES:
		case ELEMENTWISE_LEFTDIV:
		case LEFTDIV:
		case ELEMENTWISE_EXP:
			throw new TypeException(CErrorMessage.INTERNAL_NOT_UNARY_OPERAND, curRoot, curRoot);
		default:
			throw new TypeException(CErrorMessage.UNSUPPORTED_OPERAND, curRoot);
		}

	}

	/**
	 * 
	 * @param topLevel
	 * @param curRoot
	 * @param left
	 * @param left_translation
	 * @param outSparse:       if top-level output matrix will be sparse (and thus
	 *                         all substeps will accumulate to a sparse matrix)
	 * @param paramSparse:     if actual parameter is sparse
	 * @return
	 * @throws TypeException
	 * @throws UndefinedTranslationException
	 */
	@StepType(method = StepType.Function.UNARYSCALAREXPRESSION)
	public String translateUnaryScalarExpression(AASTNode topLevel, AASTNode curRoot, AASTNode left,
			String left_translation, boolean sparseParam) throws TypeException, UndefinedTranslationException {
		String operand = getUnaryOperand(curRoot);
		boolean containsSparseSubExpr = topLevel.hasAttr(CAttr.CONTAINS_SPARSE_SUBEXPRESSION);
		GType type = getExprGeneralized(left);
		String[] optranslations = left_translation.split("\\" + SPARSE_EXPR_SEPARATOR);
		String leftTranslation = optranslations[optranslations.length - 1];
		String lexpr = leftTranslation;
		GType topType = getExprGeneralized(topLevel);

		if (TypeUtils.getExprGeneralized(curRoot).equals(BType.MATRIX)) {
			String lindex = null;
			List<String> leftDims = nodeDimsToStr(left, ((MatrixType) getExprGeneralized(left)).dims(), true, true);
			if (sparseParam) {
				lindex = sparseAccess(leftTranslation);
			} else if (TypeUtils.isDegeneratedMatrix(type))
				lindex = "0";
			else if (containsSparseSubExpr)
				lindex = get2DAccessIndex(EXPR_INDEX, SPARSE_ROW_INDEX, leftDims);
			else
				lindex = EXPR_INDEX;

			lexpr = indexDimensionType(left, leftTranslation, lindex, left.symbol().equals(leftTranslation));
		}

		if (type instanceof SliceType) {

			// get the result type of
			GType typeOf = ((DimensionType) type).of();
			String prefix = null;
			if (typeOf instanceof ScalarType) {
				prefix = "d";
			} else {
				prefix = "i";
			}

			String[] toBeSetParameters = new String[4];
			// return the new slice
			toBeSetParameters[0] = operand + left_translation + STRUCT_ACCESS + "start";
			toBeSetParameters[1] = operand + left_translation + STRUCT_ACCESS + "step";
			toBeSetParameters[2] = operand + left_translation + STRUCT_ACCESS + "end";
			toBeSetParameters[3] = left_translation + STRUCT_ACCESS + "dim1";
			String slicetype = prefix + "slice";
			// call this only when you are sure you want to translate now
			AASTNode outNode = getOutputNode(curRoot);
			String newMatrix = getNodeSymbol(outNode);
			newComment(curRoot);
			newTranslation(curRoot,
					outNode.symbol() + " = ((" + slicetype + "){.start = " + toBeSetParameters[0] + ", .step = "
							+ toBeSetParameters[1] + ", .end = " + toBeSetParameters[2] + ", .dim1 = "
							+ toBeSetParameters[3] + ", .linear = true});" + NL);
			return outNode.symbol();
		} else {
			if (topLevelRHSExpression(curRoot) && TypeUtils.getExprGeneralized(curRoot).isCastableToMatrix()) {
				// call this only when you are sure you want to translate now
				AASTNode outNode = getOutputNode(curRoot);
				String newMatrix = getNodeSymbol(outNode);
				StringBuffer sb = new StringBuffer();
				sb = finalizeExpression(topLevel, outNode, curRoot, sb, lexpr, operand, null, null, false, true, false);

				newComment(curRoot);
				newTranslation(curRoot, sb.toString());
				return newMatrix;
			} else {
				// translate only this piece of code (top level operation will wrap-up)
				if (topLevel.expr().equals(BType.MATRIX) && topLevel.mexpr().isSparse()) {
					// purely sparse case
					char typec = 0;
					// don't set translate=false
					AASTNode outNode = getOutputNode(topLevel, true, false, false, null);
					GType outType = getExprGeneralized(outNode);
					DimensionType outDimType = (DimensionType) outType;

					String outSymbol = null;
					if (!outNode.hasAttr(NodeAttr.DYNAMIC_ID))
						outSymbol = outNode.symbol();
					else
						outSymbol = topLevel.symbol();

					if (topLevel.mexpr().of().equals(BType.SCALAR))
						typec = 'd';
					else
						typec = 'i';
					String nz = SPARSE_ELEMENT_COUNT;
					String x = (outDimType.of().equals(BType.SCALAR)) ? SPARSE_TMP_DOUBLE_COLUMN
							: SPARSE_TMP_INT_COLUMN;
					String w = (outDimType.of().equals(BType.SCALAR)) ? SPARSE_TMP_DOUBLE_MARKER
							: SPARSE_TMP_INT_MARKER;

					return nz + " = " + typec + "SparsePlus(&" + leftTranslation + ", " + SPARSE_ROW_INDEX + ", "
							+ SPARSE_ROW_INDEX + " + 1, " + operand + "1, " + w + ", " + x + ", " + nz + ", &"
							+ outSymbol + ");" + NL;
				} else
					// non-sparse or mixed sparse case
					return operand + lexpr;
			}
		}
	}

	private StringBuffer processPureSparseOperation(AASTNode topLevel, AASTNode translateNode, String translateSymbol,
			String fullOperation) throws TypeException, UndefinedTranslationException {
		StringBuffer accBuf = new StringBuffer();
		String ri = SPARSE_ROW_INDEX;
		GType outType = getExprGeneralized(translateNode);
		DimensionType outDimType = (DimensionType) outType;
		String ofTypeStr = exprTypeToCType(outDimType.of());
		AASTNode iterNode = getIterNode(topLevel);
		GType iterType = getExprGeneralized(iterNode);
		MatrixType iterMatrix = (MatrixType) iterType;
		List<String> iterNodeDims = nodeDimsToStr(iterNode, iterMatrix.dims(), true, true);

		String nz = SPARSE_ELEMENT_COUNT;
		createVariable(GType.get(BType.INT), nz, null, NodeType.ID, null, false, true, VarVisibility.LOCAL_TO_FUNCTION,
				false);
		/* counts the total number of elements (cumulatively) */
		accBuf.append(nz).append(" = 0;").append(NL);

		// length of w and x is the same as the number of columns of sparse matrix
		// they are dense vectors
		String x = (outDimType.of().equals(BType.SCALAR)) ? SPARSE_TMP_DOUBLE_COLUMN : SPARSE_TMP_INT_COLUMN;
		createVariable(outDimType.of(), x, null, NodeType.ID, "NULL", true, true, VarVisibility.LOCAL_TO_FUNCTION,
				false);

		// allocation
		accBuf.append("if(").append(x).append(" == NULL)").append(NL);
		accBuf.append(TAB).append(x).append(" = ").append("(" + ofTypeStr + "*) qspcc_calloc( "
				+ outDimType.dims()[1].valueAsString() + ", sizeof(" + ofTypeStr + "));").append(NL);
		accBuf.append("else").append(NL);
		accBuf.append(TAB).append(x).append(" = ").append("(" + ofTypeStr + "*) qspcc_realloc(" + x + ", ("
				+ outDimType.dims()[1].valueAsString() + ")*sizeof(" + ofTypeStr + "));").append(NL);

		String w = (outDimType.of().equals(BType.SCALAR)) ? SPARSE_TMP_DOUBLE_MARKER : SPARSE_TMP_INT_MARKER;
		createVariable(GType.get(BType.INT), w, null, NodeType.ID, "NULL", true, true, VarVisibility.LOCAL_TO_FUNCTION,
				false);
		// allocation
		accBuf.append("if(").append(w).append(" == NULL)").append(NL);
		accBuf.append(TAB).append(w).append(" = ")
				.append("(int *) qspcc_calloc( " + outDimType.dims()[1].valueAsString() + ", sizeof(int));").append(NL);
		accBuf.append("else{").append(NL);
		accBuf.append(TAB).append(w).append(" = ")
				.append("(int *) qspcc_realloc(" + w + ", (" + outDimType.dims()[1].valueAsString() + ")*sizeof(int));")
				.append(NL);
		accBuf.append(TAB).append("memset(").append(w).append(", 0, ").append(outDimType.dims()[1].valueAsString())
				.append("*sizeof(int));").append(NL);
		accBuf.append("}").append(NL);

		accBuf.append(NL);

		accBuf.append("#pragma omp smid").append(NL);
		accBuf.append("for(int ").append(ri).append(" = 0; ").append(ri).append(" < ").append(iterNodeDims.get(0))
				.append("; ++").append(ri).append("){").append(NL);
		for (String operation : fullOperation.split("\\" + NL))
			accBuf.append(TAB).append(operation).append(NL);
		accBuf.append("}").append(NL);

		// set END of LAST row
		accBuf.append(translateSymbol).append(STRUCT_ACCESS)
				.append("rowIndex[" + iterNodeDims.get(0) + "] = " + nz + "+1;").append(NL);

		if (translateNode.hasAttr(NodeAttr.DYNAMIC_ID)) {
			// translateSymbol is a temporary ID, because the real
			// out symbol is involved in the expression
			// now that the computation is over, copy back to the original output symbol
			String osymbol = translateNode.symbol();
			accBuf.append("/* copy back to " + osymbol
					+ ", we used a temporary variable to avoid overwriting it while evaluating the expression */")
					.append(NL);
			// allocate output matrix
			ensureMatrixMemory(accBuf, translateNode, translateNode.symbol(), topLevel.mexpr().dims(), nz + " + 1");
			accBuf.append("memcpy(").append(osymbol).append(STRUCT_ACCESS).append("rowIndex").append(", ");
			accBuf.append(translateSymbol).append(STRUCT_ACCESS).append("rowIndex")
					.append(", (" + iterNodeDims.get(0) + " + 1)*sizeof(QSPCC_INT));").append(NL);

			accBuf.append("memcpy(").append(osymbol).append(STRUCT_ACCESS).append("columns").append(", ");
			accBuf.append(translateSymbol).append(STRUCT_ACCESS).append("columns")
					.append(", (" + nz + " + 1)*sizeof(QSPCC_INT));").append(NL);

			accBuf.append("memcpy(").append(osymbol).append(STRUCT_ACCESS).append("matrix").append(", ");
			accBuf.append(translateSymbol).append(STRUCT_ACCESS).append("matrix")
					.append(", (" + nz + " + 1)*sizeof(" + ofTypeStr + "));").append(NL);
		}

		return accBuf;
	}

	/*
	 * processing to perform when a sparse operation is encountered, both if final
	 * result is sparse or if it is a plain matrix.
	 */
	private StringBuffer processMixedSparseOperation(AASTNode topLevel, AASTNode translateNode, String translateSymbol,
			String fullOperation) throws TypeException, UndefinedTranslationException {
		AASTNode outNode = getOutputNode(topLevel, false, false);
		GType outType = getExprGeneralized(outNode);
		List<AASTNode> subExprs = (List<AASTNode>) topLevel.attr(CAttr.CONTAINS_SPARSE_SUBEXPRESSION);
		// if this is true, all the involved matrices are sparse
		boolean outSparse = outType.equals(BType.MATRIX) && ((MatrixType) outType).isSparse();
		// if just -x, +x sparse matrix, optimize re-using the same structure since this
		// operation
		// won't change the matrix layout
		boolean oneSparseMatrix = unaryOperation(topLevel);
		AASTNode iterNode = getIterNode(topLevel);
		GType iterType = getExprGeneralized(iterNode);
		MatrixType iterMatrix = (MatrixType) iterType;
		List<String> iterNodeDims = nodeDimsToStr(iterNode, iterMatrix.dims(), true, true);
		StringBuffer accBuf = new StringBuffer();
		String ri = SPARSE_ROW_INDEX;
		String ci = SPARSE_COL_INDEX;
		String c = EXPR_INDEX;
		String rowStart = iterNode.symbol() + STRUCT_ACCESS + "rowIndex[" + ri + "]";
		String rowEnd = iterNode.symbol() + STRUCT_ACCESS + "rowIndex[" + ri + "+1]";
		String colAccess = iterNode.symbol() + STRUCT_ACCESS + "columns[" + ci + "-1]";
		String usedCells = subExprs.get(0).symbol() + STRUCT_ACCESS + "usedCells";
		String rows = null, columns = null, values = null, counter = null, value = null;

		if (outSparse) {
			DimensionType outDimType = (DimensionType) outType;
			value = Symbols.getSymbolFromType("value", outDimType.of().type());
			createVariable(outDimType.of(), value, null, NodeType.ID, null, false, false, VarVisibility.LOCAL_TO_BLOCK,
					accBuf, false);

			if (oneSparseMatrix) {
				ensureMatrixMemory(accBuf, translateNode, translateSymbol, (MatrixType) outType,
						iterNodeDims.toArray(new String[iterNodeDims.size()]), false, false, false, usedCells);
				// pre-set usedCells
				accBuf.append(translateSymbol + STRUCT_ACCESS + "usedCells = " + usedCells + ";" + NL);
			} else {
				IntType[] outTypeDims = outDimType.dims();
				boolean static_alloc = true;
				for (IntType outDim : outTypeDims)
					static_alloc &= outDim.hasIntValue();
				List<String> outTypeDimsStr = nodeDimsToStr(outNode, outTypeDims, true, true);

				rows = Symbols.getSymbolFromType("rows", BType.MATRIX);
				columns = Symbols.getSymbolFromType("columns", BType.MATRIX);
				values = Symbols.getSymbolFromType("values", BType.MATRIX);
				counter = Symbols.getSymbolFromType("cnt", BType.INT);

				String ofTypeStr = exprTypeToCType(outDimType.of());
				// at most we need the sum of usedcells of every sparse subnode
				List<String> subExprCells = new ArrayList<String>(subExprs.size());
				for (AASTNode subExpr : subExprs)
					subExprCells.add(subExpr.symbol() + STRUCT_ACCESS + "usedCells");

				accBuf.append("static __thread ").append(ofTypeStr).append("*" + values).append(" = NULL;").append(NL);
				accBuf.append("if(").append(values).append(" == NULL)").append(NL);
				accBuf.append(TAB).append(values).append(" = ").append("(" + ofTypeStr + "*) qspcc_malloc( ("
						+ String.join("+", subExprCells) + ")*sizeof(" + ofTypeStr + "));").append(NL);
				accBuf.append("else").append(NL);
				accBuf.append(TAB).append(values).append(" = ").append("(" + ofTypeStr + "*) qspcc_realloc(" + values
						+ ", (" + String.join("+", subExprCells) + ")*sizeof(" + ofTypeStr + "));").append(NL);

				accBuf.append("static __thread int *" + rows).append(" = NULL;").append(NL);
				accBuf.append("if(").append(rows).append(" == NULL)").append(NL).append(TAB).append(rows).append(" = ")
						.append("(int *) qspcc_malloc( (" + String.join("+", subExprCells) + ")*sizeof(int));")
						.append(NL);
				accBuf.append("else").append(NL);
				accBuf.append(TAB).append(rows).append(" = ").append(
						"(int *) qspcc_realloc(" + rows + ", (" + String.join("+", subExprCells) + ")*sizeof(int));")
						.append(NL);

				accBuf.append("static __thread int *" + columns).append(" = NULL;").append(NL);
				accBuf.append("if(").append(columns).append(" == NULL)").append(NL).append(TAB).append(columns)
						.append(" = ")
						.append("(int *) qspcc_malloc( (" + String.join("+", subExprCells) + ")*sizeof(int));")
						.append(NL);
				accBuf.append("else").append(NL);
				accBuf.append(TAB).append(columns).append(" = ").append(
						"(int *) qspcc_realloc(" + columns + ", (" + String.join("+", subExprCells) + ")*sizeof(int));")
						.append(NL);

				createVariable(GType.get(BType.INT), counter, null, NodeType.ID, "0", false, false,
						VarVisibility.LOCAL_TO_BLOCK, accBuf, false);
			}
		} else
			ensureMatrixMemory(accBuf, translateNode, iterNodeDims.toArray(new String[iterNodeDims.size()]));
		boolean doublefor = iterNodeDims.size() == 2;
		if (doublefor) {
			// iterate over rows
			accBuf.append("for(int ").append(ri).append(" = 0; ").append(ri).append(" < ").append(iterNodeDims.get(0))
					.append("; ++").append(ri).append("){").append(NL);
			accBuf.append("#pragma omp smid").append(NL);
			if (outSparse && oneSparseMatrix)
				// iterate over cols, rowIndex is 1-based
				accBuf.append(TAB).append("for(int ").append(ci).append(" = ").append(rowStart)
						.append(", " + c + " = " + colAccess + " ; ").append(ci).append(" < ").append(rowEnd)
						.append(" ; ++").append(ci).append(", " + c + " = " + colAccess + "){").append(NL);
			else
				accBuf.append(TAB).append("for(int ").append(ci).append(" = 1, " + c + "=" + ci + " ; ").append(ci)
						.append(" <= ").append(iterNodeDims.get(1)).append(" ; ++").append(ci)
						.append(", " + c + "=" + ci + "){").append(NL);
		} else {
			String n = String.join(" * ", iterNodeDims);
			// complete iteration over the only dimension
			accBuf.append("#pragma omp smid").append(NL);
			accBuf.append(TAB).append("for(int ").append(ci).append(" = 1, " + c + "=" + ci + " ; ").append(ci)
					.append(" <= ").append(n).append(" ; ++").append(ci).append(", " + c + "=" + ci + "){").append(NL);
		}

		if (!outSparse) {
			String accessIndex = get2DAccessIndex(c, ri, iterNodeDims);

			accBuf.append(TAB).append(TAB).append(translateSymbol).append(STRUCT_ACCESS)
					.append("matrix[" + accessIndex + "] = ");
			// VALUE
			accBuf.append(fullOperation).append(";").append(NL);
		} else {
			accBuf.append(TAB).append(TAB).append(value).append(" = ").append(fullOperation).append(";").append(NL);
			accBuf.append(TAB).append(TAB).append("if(fabs( (double)").append(value)
					.append(" ) > DOUBLE_EQUALITY_TOL){").append(NL);
			if (!oneSparseMatrix) {
				// sparse output update list of rows, cols and values
				// that will be initialized at top-level
				// generate
				// rows
				accBuf.append(TAB).append(TAB).append(TAB).append(rows).append("[").append(counter).append("] = ")
						.append(ri + "+1;").append(NL);
				// cols
				accBuf.append(TAB).append(TAB).append(TAB).append(columns).append("[").append(counter).append("] = ")
						.append(c).append(";").append(NL);
				// values
				accBuf.append(TAB).append(TAB).append(TAB).append(values).append("[").append(counter).append("++] = ");
				// VALUE
				accBuf.append(value).append(";").append(NL);
			} else {
				// outsparse and oneSparseMatrix is true
				// just change matrix and copy everything else
				// rows
				accBuf.append(TAB).append(TAB).append(TAB).append(translateSymbol).append(STRUCT_ACCESS)
						.append("matrix[" + ci + "-1] = ").append(fullOperation).append(";").append(NL);
				accBuf.append(TAB).append(TAB).append(TAB).append(translateSymbol).append(STRUCT_ACCESS)
						.append("columns[" + ci + "-1] = ").append(c).append(";").append(NL);
			}
			// close if
			accBuf.append(TAB).append(TAB).append("}").append(NL);

		}
		accBuf.append(TAB).append("}").append(NL);
		if (outSparse && oneSparseMatrix) {
			// copy row
			accBuf.append(TAB).append(translateSymbol).append(STRUCT_ACCESS).append("rowIndex[" + ri + "] = ")
					.append(rowStart).append(";").append(NL);
			accBuf.append(TAB).append(translateSymbol).append(STRUCT_ACCESS).append("rowIndex[" + ri + "+1] = ")
					.append(rowEnd).append(";").append(NL);
		}

		if (doublefor)
			accBuf.append("}").append(NL);

		// initialize out matrix
		if (outSparse && !oneSparseMatrix) {
			// allocate now just the memory needed
			ensureMatrixMemory(accBuf, translateNode, translateSymbol, (MatrixType) outType,
					iterNodeDims.toArray(new String[iterNodeDims.size()]), false, false, false, counter);
			// actual type
			String actualType;
			if (iterMatrix.of().equals(BType.SCALAR))
				actualType = "\"double\"";
			else
				actualType = "\"int\"";
			// output type
			accBuf.append(exprTypeToCType(((MatrixType) outType).of()).trim());
			accBuf.append("InitSparseMatrix(&").append(translateSymbol).append(", ").append(rows).append(", ")
					.append(columns).append(", ").append(values).append(", ").append(actualType).append(", ")
					.append(counter).append(", ").append(iterNodeDims.get(0)).append(", ").append(iterNodeDims.get(1))
					.append(");").append(NL);
		}
		return accBuf;
	}

	private boolean unaryOperation(AASTNode topLevel) {
		if (topLevel.childs().isEmpty())
			return true;
		else if (topLevel.childs().size() == 1)
			return unaryOperation(topLevel.childs().get(0));
		else
			return false;
	}

	private String get2DAccessIndex(String ci, String ri, List<String> dims) {
		if (dims.size() == 1)
			return ci + "-1";
		else
		// row always starts from 0
		// col always starts from 1
		if (ROWMAJOR)
			return "(" + ci + "-1) + " + ri + "*" + dims.get(1);
		else
			return ri + " + (" + ci + "-1)*" + dims.get(0);
	}

	/**
	 * double search look first for an idnode, then checks for coherence in output
	 * dimensions. If dimensions are not coherent with original toplevel node, then
	 * search upwards in the ancestors the first node with the correct dimensions
	 * and return it, otherwise return the leaf id or matrix node.
	 * 
	 * @param topLevel
	 * @return
	 */
	private AASTNode getIterNode(AASTNode topLevel) {
		List<AASTNode> blackList = new LinkedList<AASTNode>();
		return getIterNodeImpl(topLevel, topLevel, blackList);
	}

	private AASTNode getIterNodeImpl(AASTNode original, AASTNode current, List<AASTNode> blackList) {
		if (!blackList.contains(current)) {
			// id/apply/colon (for the slices) are the bottom-level iterators
			if ((current.type().equals(NodeType.ID) || current.type().equals(NodeType.APPLY)
					|| current.type().equals(NodeType.COLON) || current.type().equals(NodeType.MATRIX)
					|| current.type().equals(NodeType.TRANSPOSE) || current.type().equals(NodeType.ELEMENTWISE_CCT))) {
				return getParentNodeImpl(original, current, blackList);
			} else if (current.type().equals(NodeType.FIELDACCESS)) {
				// get from last id only
				blackList.add(current);
				return getParentNodeImpl(original, current.childs().get(current.childs().size() - 1), blackList);
			} else
				return getIterNodeChilds(original, current, blackList);
		} else
			return getIterNodeChilds(original, current, blackList);
	}

	private AASTNode getIterNodeChilds(AASTNode original, AASTNode current, List<AASTNode> blackList) {
		for (AASTNode child : current.childs()) {
			if (!blackList.contains(child)) {
				AASTNode s = getIterNodeImpl(original, child, blackList);
				if (s != null)
					return s;
			}
		}
		return null;
	}

	private AASTNode getParentNodeImpl(AASTNode original, AASTNode current, List<AASTNode> blackList) {
		GType origExpr = getExprGeneralized(original);
		GType curExpr = getExprGeneralized(current);
		// looks for intermediate node between original and current with the same
		// dimensions of original
		if (origExpr.canRepresent(curExpr) || curExpr.canRepresent(origExpr)) {
			if (!(origExpr instanceof DimensionType) || !(curExpr instanceof DimensionType))
				return current;
			DimensionType oexpr = (DimensionType) origExpr;
			DimensionType cexpr = (DimensionType) curExpr;
			if (TypeUtils.realDims(oexpr.dims()).length == TypeUtils.realDims(cexpr.dims()).length) {
				String origElements = nodeDimsToInt(oexpr.dims());
				String curElements = nodeDimsToInt(cexpr.dims());
				// if some dimension is not available or both are availale and orig is equal to
				// cur
				if (origElements == null || curElements == null
						|| Integer.parseInt(origElements) <= Integer.parseInt(curElements)) {
					return current;
				} else {
					blackList.add(current);
					// restart blacklisting this node
					return getIterNodeImpl(original, original, blackList);
				}
			} else {
				blackList.add(current);
				// restart blacklisting this node
				return getIterNodeImpl(original, original, blackList);
			}
		} else if (curExpr instanceof FunctionType)
			// look for top apply
			return getParentNodeImpl(original, current.parent(NodeType.APPLY), blackList);
		else {
			blackList.add(current);
			// restart blacklisting this node
			return getIterNodeImpl(original, original, blackList);
		}
	}

	@StepType(method = StepType.Function.SCALAREXPRESSION)
	public String translateScalarExpression(AASTNode curRoot, AASTNode left, AASTNode right, List<String> child_results)
			throws TypeException, UndefinedTranslationException {
		String operand = null;
		boolean funcall = false;
		boolean needToBeCasted = false;

		switch (curRoot.type()) {
		case PLUS:
			operand = "+";
			break;
		case MINUS:
			operand = "-";
			break;
		case TIMES:
		case ELEMENTWISE_TIMES:
			operand = "*";
			break;
		case ELEMENTWISE_LEFTDIV:
		case LEFTDIV:
			operand = "/";
			break;
		case EXP:
		case ELEMENTWISE_EXP:
			operand = "pow";
			funcall = true;
			// If top-level operations are scalar, doesn't need
			// otherwise it needs
			AASTNode topLevel = getTopLevelExpression(curRoot);
			if (topLevel != null) {
				GType topLevelType = getExprGeneralized(topLevel);
				needToBeCasted = topLevelType != null && topLevelType.isCastableToBool();
			} else
				needToBeCasted = true;
			break;
		default:
			throw new TypeException(CErrorMessage.UNSUPPORTED_OPERAND, curRoot);
		}

		String cType = null;
		GType type = getExprGeneralized(curRoot);
		if (type != null) {
			if (!(type instanceof DimensionType)) {
				cType = exprTypeToCType(type);
			} else {
				cType = exprTypeToCType(((DimensionType) type).of());
			}
		}

		String leftStr, rightStr;
		String leftT = child_results.get(0);
		String rightT = child_results.get(1);
		if (!curRoot.expr().equals(left.expr()))
			// add cast
			leftStr = "(" + exprTypeToCType(curRoot) + ")" + leftT;
		else
			leftStr = leftT;

		if (!curRoot.expr().equals(right.expr()))
			// add cast
			rightStr = "(" + exprTypeToCType(curRoot) + ")" + rightT;
		else
			rightStr = rightT;

		if (!funcall)
			return leftStr + " " + operand + " " + rightStr;
		else {
			if (needToBeCasted && cType != null) {
				return "(" + cType + ")" + operand + "(" + leftStr + ", " + rightStr + ")";
			} else {
				return operand + "(" + leftStr + ", " + rightStr + ")";
			}
		}
	}

	@StepType(method = StepType.Function.MATRIXPOINTWISEEXPRESSION)
	public String translateMatrixMatrixPointwiseExpression(AASTNode topLevel, AASTNode curRoot, AASTNode left,
			AASTNode right, String leftT, String rightT, boolean[] sparseParams)
			throws TypeException, UndefinedTranslationException {

		String operand = null;
		String sparseOperand = null;
		Boolean needToBeCasted = false;
		boolean containsSparseSubExpr = topLevel.hasAttr(CAttr.CONTAINS_SPARSE_SUBEXPRESSION);

		boolean funcall = false;

		GType leftType = getExprGeneralized(left);
		GType rightType = getExprGeneralized(right);

		IntType[] leftRealDims = TypeUtils.realDims(((DimensionType) leftType).dims());
		IntType[] rightRealDims = TypeUtils.realDims(((DimensionType) rightType).dims());

		char typec = '0';
		if (((DimensionType) leftType).of().equals(BType.SCALAR)
				&& ((DimensionType) rightType).of().equals(BType.SCALAR))
			typec = 'd';
		else
			typec = 'i';

		switch (curRoot.type()) {
		case PLUS:
			operand = "+";
			sparseOperand = typec + "SparsePlus";
			break;
		case MINUS:
			operand = "-";
			sparseOperand = typec + "SparseMinus";
			break;
		case TIMES:
		case ELEMENTWISE_TIMES:
			operand = "*";
			sparseOperand = typec + "SparseTimes";
			break;
		case LEFTDIV:
		case RIGHTDIV:
		case ELEMENTWISE_LEFTDIV:
		case ELEMENTWISE_RIGHTDIV:
			// in rightdiv case left and right are already inverted at call time.
			operand = "/";
			sparseOperand = typec + "SparseDivide";
			break;
		case EXP:
		case ELEMENTWISE_EXP:
			operand = "pow";
			sparseOperand = typec + "SparsePow";
			funcall = true;
			// If top-level operations are scalar, doesn't need
			// otherwise it needs
			if (topLevel != null) {
				GType topLevelType = getExprGeneralized(topLevel);
				needToBeCasted = topLevelType != null && topLevelType.isCastableToBool();
			} else
				needToBeCasted = true;
			break;
		default:
			throw new TypeException(CErrorMessage.UNSUPPORTED_OPERAND, curRoot);
		}

		// extract eventual sparse accumulators (intermediate for)
		String[] lefttranslations = leftT.split("\\" + SPARSE_EXPR_SEPARATOR);
		String leftTranslation = lefttranslations[lefttranslations.length - 1];
		String[] righttranslations = rightT.split("\\" + SPARSE_EXPR_SEPARATOR);
		String rightTranslation = righttranslations[righttranslations.length - 1];

		String rindex = null;
		String lindex = null;
		AASTNode iterNode = null;
		boolean leftdegenere = TypeUtils.isDegeneratedMatrix(leftType);
		boolean rightdegenere = TypeUtils.isDegeneratedMatrix(rightType);
		if (sparseParams[0]) {
			lindex = sparseAccess(leftTranslation);
		} else if (leftdegenere) {
			// replace iterator variable with 0 since this is a degenere matrix
			lindex = "0";
		} else if (containsSparseSubExpr) {
			List<String> leftDims = nodeDimsToStr(left, ((DimensionType) leftType).dims(), true, true);
			lindex = get2DAccessIndex(EXPR_INDEX, SPARSE_ROW_INDEX, leftDims);
		} else {
			lindex = EXPR_INDEX;
		}

		if (sparseParams[1]) {
			rindex = sparseAccess(rightTranslation);
			;
		} else if (rightdegenere) {
			// replace iterator variable with 0 since this is a degenere matrix
			rindex = "0";
		} else if (containsSparseSubExpr) {
			List<String> rightDims = nodeDimsToStr(left, ((DimensionType) rightType).dims(), true, true);
			rindex = get2DAccessIndex(EXPR_INDEX, SPARSE_ROW_INDEX, rightDims);
		} else {
			rindex = EXPR_INDEX;
		}

		if (leftdegenere)
			iterNode = right;
		else
			iterNode = left;

		AASTNode leftNode = TypeUtils.getIDNode(curRoot.childs().get(0));
		String lexpr = indexDimensionType(leftNode, leftTranslation, lindex, leftNode.symbol().equals(leftTranslation));
		AASTNode rightNode = TypeUtils.getIDNode(curRoot.childs().get(1));
		String rexpr = indexDimensionType(rightNode, rightTranslation, rindex,
				rightNode.symbol().equals(rightTranslation));

		String cType = null;
		GType type = getExprGeneralized(curRoot);
		if (type != null) {
			if (!(type instanceof DimensionType)) {
				cType = exprTypeToCType(type);
			} else {
				cType = exprTypeToCType(((DimensionType) type).of());
			}
		}

		// translate only if toplevel sparse operations, otherwise ASSIGN will do it
		if (topLevelRHSExpression(curRoot)) {
			AASTNode outNode = getOutputNode(curRoot);
			String matrixName = getNodeSymbol(outNode);
			StringBuffer sb = new StringBuffer();

			// add accumulators
			for (int opi = 0; opi < lefttranslations.length - 1; ++opi)
				sb.append(lefttranslations[opi]);
			for (int opi = 0; opi < righttranslations.length - 1; ++opi)
				sb.append(righttranslations[opi]);

			sb = finalizeExpression(curRoot, outNode, iterNode, sb, lexpr, operand, rexpr, cType, needToBeCasted, false,
					funcall);

			newComment(curRoot);
			newTranslation(curRoot, sb.toString());
			return matrixName;
		} else {
			String finalTranslation = "";
			// part of translation
			if (topLevel.expr().equals(BType.MATRIX) && topLevel.mexpr().isSparse()) {
				// sparse expression
				// don't set translate=false
				AASTNode outNode = getOutputNode(topLevel, true, false, false, null);
				GType outType = getExprGeneralized(outNode);
				DimensionType outDimType = (DimensionType) outType;
				String outSymbol = null;
				if (!outNode.hasAttr(NodeAttr.DYNAMIC_ID))
					outSymbol = outNode.symbol();
				else
					outSymbol = topLevel.symbol();
				String nz = SPARSE_ELEMENT_COUNT;
				String x = (outDimType.of().equals(BType.SCALAR)) ? SPARSE_TMP_DOUBLE_COLUMN : SPARSE_TMP_INT_COLUMN;
				String w = (outDimType.of().equals(BType.SCALAR)) ? SPARSE_TMP_DOUBLE_MARKER : SPARSE_TMP_INT_MARKER;
				finalTranslation = nz + " = " + sparseOperand + "(&" + leftTranslation + ", " + SPARSE_ROW_INDEX + ", "
						+ SPARSE_ROW_INDEX + " + 1, 1, " + w + ", " + x + ", " + nz + ", &" + outSymbol + ");" + NL;
				finalTranslation += nz + " = " + sparseOperand + "(&" + rightTranslation + ", " + SPARSE_ROW_INDEX
						+ ", " + SPARSE_ROW_INDEX + " + 1, 1, " + w + ", " + x + ", " + nz + ", &" + outSymbol + ");"
						+ NL;
			} else if (!funcall) {
				finalTranslation = lexpr + " " + operand + " " + rexpr;
			} else {
				if (needToBeCasted && cType != null) {
					finalTranslation = "(" + cType + ") ";
				}
				finalTranslation += operand + "(" + lexpr + ", " + rexpr + ")";
			}

			// translate only this piece of code (top level operation will wrap-up)
			return finalTranslation;
		}
	}

	private String sparseAccess(String rightTranslation) {
		// rowindex index start from 0, column index starts from 1
		return "sparseAccess(" + SPARSE_ROW_INDEX + ", " + EXPR_INDEX + ", " + rightTranslation + STRUCT_ACCESS
				+ "rowIndex, " + rightTranslation + STRUCT_ACCESS + "columns, " + rightTranslation + STRUCT_ACCESS
				+ "usedCells)";
	}

	@StepType(method = StepType.Function.MATRIXMATRIXEXPRESSION)
	public String translateMatrixMatrixExpression(AASTNode topLevel, AASTNode curRoot, AASTNode left, AASTNode right,
			String leftT, String rightT, boolean[] sparseParams) throws TypeException, UndefinedTranslationException {

		StringBuffer sb = new StringBuffer();
		curRoot.attr(CAttr.MATRIX_EXPR, true);
		AASTNode outNode = curRoot;
		if (topLevelExpression(curRoot))
			outNode = getOutputNode(curRoot);
		String newSymbol = outNode.symbol();
		boolean pointwise = matrixMatrixPointwiseOp(curRoot, left, right);
		DimensionType leftType = (DimensionType) getExprGeneralized(left);
		IntType[] matrixLeftDims = TypeUtils.realDims(leftType.dims());
		DimensionType rightType = (DimensionType) getExprGeneralized(right);
		IntType[] matrixRightDims = TypeUtils.realDims(rightType.dims());
		GType outType = TypeUtils.getExprGeneralized(curRoot);

		if (pointwise) {
			if (curRoot.type().equals(NodeType.TIMES)
					&& (!matrixLeftDims[0].hasValue() || !matrixLeftDims[0].valueAsInt().equals(1))
					&& (!matrixRightDims[0].hasValue() || !matrixRightDims[0].valueAsInt().equals(1))) {
				// vector-vector multiplication that results in a scalar
				String index = EXPR_INDEX;
				AASTNode lnode = curRoot.childs().get(0);
				AASTNode rnode = curRoot.childs().get(1);
				String lexpr = indexDimensionType(lnode, leftT, index, lnode.symbol().equals(leftT));
				String rexpr = indexDimensionType(rnode, rightT, index, rnode.symbol().equals(rightT));
				String op = " * ";
				if (!outType.isCastableToScalar())
					throw new TypeException(
							ErrorMessage.UNSUPPORTED_MATRIX_MATRIX_OPERATION_BETWEEN_INCOMPATIBLE_MATRICES, curRoot,
							lexpr, rexpr);

				boolean created = createVariable(outNode, "0");
				if (!created)
					// initialize here
					sb.append(newSymbol).append(" = 0;").append(NL);

				// simple element-wise product of vectors
				sb.append("#pragma omp smid").append(NL);
				sb.append("for(int " + index + " = 0 ; " + index + " < " + matrixLeftDims[0].valueAsString() + " ; ++"
						+ index + ")").append(NL);
				sb.append(TAB).append(newSymbol).append(" += ").append(lexpr).append(op).append(rexpr).append(";")
						.append(NL);
				newComment(curRoot);
				newTranslation(curRoot, sb.toString());
				return newSymbol;
			} else {
				// other operations like leftdiv rightdiv
				return translateMatrixMatrixPointwiseExpression(topLevel, curRoot, left, right, leftT, rightT,
						sparseParams);
			}
		}
		// -- CASE MATRIX MATRIX OPERATION THAT RESULTS IN A MATRIX --
		// if left / right are expressions, first finalize them and then use the results
		// as single symbols
		// subsequent code assume left / right are symbols and not expressions
		AASTNode leftOutNode = getOutputNode(left);
		if (TypeUtils.getIDNode(left).hasType(TypeUtils.expressionNodes()) && !leftOutNode.symbol().equals(leftT)) {
			String newLeftT = getNodeSymbol(leftOutNode);
			StringBuffer buf = new StringBuffer();
			buf = finalizeExpression(leftOutNode, leftOutNode, null, buf, leftT, null, null, null, false, true, false);

			newComment(curRoot);
			newTranslation(curRoot, buf.toString());
			leftT = newLeftT;

		}
		AASTNode rightOutNode = getOutputNode(right);
		if (TypeUtils.getIDNode(right).hasType(TypeUtils.expressionNodes()) && !rightOutNode.symbol().equals(rightT)) {
			String newRightT = getNodeSymbol(rightOutNode);
			StringBuffer buf = new StringBuffer();
			buf = finalizeExpression(rightOutNode, rightOutNode, null, buf, rightT, null, null, null, false, true,
					false);

			newComment(curRoot);
			newTranslation(curRoot, buf.toString());
			rightT = newRightT;
		}
		// enlarge 1d matrices with ones
		if (matrixLeftDims.length < 2)
			matrixLeftDims = leftType.dims();
		if (matrixRightDims.length < 2)
			matrixRightDims = rightType.dims();
		// create out matrix if doesn't exist
		createVariable(outNode);

		if (curRoot.type().equals(NodeType.TIMES)) {
			String outSymbol = newSymbol;
			createVariable(curRoot, false, VarVisibility.LOCAL_TO_FUNCTION);

			if (matrixLeftDims.length != 2 || matrixRightDims.length > 2)
				throw new TypeException(CErrorMessage.INTERNAL_DOT_PRODUCT_BETWEEN_NON_2D_MATRICES,
						curRoot.parent(NodeType.EXPRESSION), left.name(), right.name());

			// use inner definitions first
			// matrixLeftDims = matrixDims(leftMatrix, matrixLeftDims);
			// matrixRightDims = matrixDims(rightMatrix, matrixRightDims);

			IntType leftrows = matrixLeftDims[0];
			IntType leftcols = matrixLeftDims[1];
			IntType rightcols = null;
			if (matrixRightDims.length > 1)
				rightcols = matrixRightDims[1];
			else
				rightcols = (IntType) GType.get(BType.INT, 1);

			// final matrix dimension: mxn
			String m = leftrows.valueAsString();
			String k = leftcols.valueAsString();
			String n = rightcols.valueAsString();

			// createUninitializedMatrix(newSymbol, SymbolType.SCALAR,
			// newSymbolDims);
			String functionCode = "matrix_product";
			String functionName = null;
			try {
				functionName = getCFunctionImplementationManagerInstance().getTranslation(curRoot, functionCode);
			} catch (Exception e) {
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_PREFERRED_AND_ALTERNATIVE_TRANSLATION,
						curRoot.parent(NodeType.EXPRESSION), functionCode);
			}
			// get the result type of -should we check both the sides?-
			GType ltypeOf = ((DimensionType) leftType).of();
			GType rtypeOf = ((DimensionType) rightType).of();
			GType typeOf = null;
			boolean leftGreater = true;
			if (ltypeOf.canRepresent(rtypeOf))
				typeOf = ltypeOf;
			else {
				typeOf = rtypeOf;
				leftGreater = false;
			}
			char prefix, mtype;
			// on scalar or on integers
			if (typeOf.equals(BType.SCALAR)) {
				prefix = 'd';
			} else {
				prefix = 'i';
			}
			// sparse or dense
			boolean sparseSystem = ((MatrixType) leftType).isSparse();
			if (sparseSystem)
				mtype = 'S';
			else
				mtype = 'D';

			if (!ltypeOf.type().equals(rtypeOf.type())) {
				if (leftGreater)
					throw new TypeException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
							curRoot.parent(NodeType.EXPRESSION), functionCode,
							new String[] { rightType.toShortString(), leftType.toShortString() },
							new String[] { "Scalar[n x k]", "Scalar[k x m]" });
				else
					throw new TypeException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
							curRoot.parent(NodeType.EXPRESSION), functionCode,
							new String[] { leftType.toShortString(), rightType.toShortString() },
							new String[] { "Scalar[n x k]", "Scalar[k x m]" });
			}

			if (prefix == 'i' && mtype == 'S')
				throw new TypeException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
						curRoot.parent(NodeType.EXPRESSION), functionCode,
						new String[] { leftType.toShortString(), rightType.toShortString() },
						new String[] { "Scalar[n x k]", "Scalar[k x m]" });

			if (((MatrixType) leftType).isSparse() && ((MatrixType) rightType).isSparse())
				throw new TypeException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
						curRoot.parent(NodeType.EXPRESSION), functionCode,
						new String[] { leftType.toShortString(), rightType.toShortString() },
						new String[] { "Scalar[n x k]", "Scalar[k x m]" },
						"Hint: convert " + right.name() + "to a non-sparse matrix");

			if (!((MatrixType) leftType).isSparse() && ((MatrixType) rightType).isSparse())
				throw new TypeException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
						curRoot.parent(NodeType.EXPRESSION), functionCode,
						new String[] { leftType.toShortString(), rightType.toShortString() },
						new String[] { "Scalar[n x k]", "Scalar[k x m]" },
						"Hint: convert " + left.name() + "to a non-sparse matrix");

			functionName = functionName.replace('?', prefix);
			if (sparseSystem && !functionName.contains("#"))
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_SPARSE_MATRIX_OPERATION, curRoot,
						functionCode);
			functionName = functionName.replace('#', mtype);
			sb.append(functionName).append("(").append(leftT + STRUCT_ACCESS + "matrix, ");
			if (sparseSystem) {
				// add rowIndex, columns and matrix_type
				sb.append(leftT + STRUCT_ACCESS + "rowIndex, ");
				sb.append(leftT + STRUCT_ACCESS + "columns, ");
				sb.append(sparseMatrixType).append(", ");
			}
			sb.append(rightT + STRUCT_ACCESS + "matrix, ").append(newSymbol + STRUCT_ACCESS + "matrix, ")
					.append(m + ",").append(n + ",").append(k).append(");").append(NL);

			// allocating memory for the product expression
			String[] targetDimensions = new String[] { m, n };
			StringBuffer ensureMemoryAllocation = new StringBuffer();
			ensureMatrixMemory(ensureMemoryAllocation, outNode, targetDimensions);

			newTranslation(curRoot, ensureMemoryAllocation.toString());
		} else if ((curRoot.type().equals(NodeType.LEFTDIV) || curRoot.type().equals(NodeType.RIGHTDIV))
				&& leftType.equals(BType.MATRIX)) {
			// in case of RIGHTDIV the inputs were already swapped at call time
			if (matrixLeftDims.length != 2 || matrixRightDims.length > 2)
				throw new TypeException(CErrorMessage.INTERNAL_LINEAR_SOLVER_BETWEEN_NON_2D_MATRICES, curRoot, leftT,
						rightT);
			IntType rightrows = matrixRightDims[0];
			IntType rightcols = null;
			if (matrixRightDims.length > 1)
				rightcols = matrixRightDims[1];
			else
				rightcols = (IntType) GType.get(BType.INT, 1);
			IntType leftcols = matrixLeftDims[1];

			// final matrix dimension: mxn
			String rows = rightrows.valueAsString();
			String acols = leftcols.valueAsString();
			String cols = rightcols.valueAsString();

			String functionCode = "solve_linear_system";
			String functionName = null;
			try {
				functionName = getCFunctionImplementationManagerInstance().getTranslation(curRoot, functionCode);
			} catch (Exception e) {
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_PREFERRED_AND_ALTERNATIVE_TRANSLATION,
						curRoot, functionCode);
			}
			// get the result type of -should we check both the sides?-
			// get the result type of -should we check both the sides?-
			GType ltypeOf = ((DimensionType) leftType).of();
			GType rtypeOf = ((DimensionType) rightType).of();
			GType typeOf = null;
			boolean leftGreater = true;
			if (ltypeOf.canRepresent(rtypeOf))
				typeOf = ltypeOf;
			else {
				typeOf = rtypeOf;
				leftGreater = false;
			}
			char prefix, sparseOrPlain;
			if (typeOf.equals(BType.SCALAR)) {
				prefix = 'd';
			} else {
				prefix = 'i';
			}
			boolean sparseSystem = ((MatrixType) leftType).isSparse();
			boolean sparseRHS = ((MatrixType) rightType).isSparse();
			if (sparseSystem)
				// sparse
				if (!sparseRHS)
					sparseOrPlain = 'S';
				else
					// next of S
					sparseOrPlain = 'P';
			else
				// dense
				sparseOrPlain = 'D';

			if (!ltypeOf.type().equals(rtypeOf.type())) {
				if (leftGreater)
					new TypeException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
							curRoot.parent(NodeType.EXPRESSION), functionCode,
							new String[] { rightType.toShortString(), leftType.toShortString() },
							new String[] { "Scalar[n x k]", "Scalar[k x m]" });
				else
					throw new TypeException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
							curRoot.parent(NodeType.EXPRESSION), functionCode,
							new String[] { leftType.toShortString(), rightType.toShortString() },
							new String[] { "Scalar[n x k]", "Scalar[k x m]" });
			}

			functionName = functionName.replace('?', prefix);
			if (sparseSystem && !functionName.contains("#"))
				throw new TypeException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
						curRoot.parent(NodeType.EXPRESSION), functionCode,
						new String[] { leftType.toShortString(), rightType.toShortString() },
						new String[] { "Scalar[n x k]", "Scalar[k x m]" },
						"Hint: Convert " + left.name() + "to a non-sparse matrix");
			functionName = functionName.replace('#', sparseOrPlain);

			// allocating memory for the solution expression
			String[] targetDimensions = new String[] { rows, cols };
			StringBuffer ensureMemoryAllocation = new StringBuffer();
			ensureMatrixMemory(ensureMemoryAllocation, outNode, targetDimensions);

			newTranslation(curRoot, ensureMemoryAllocation.toString());

			sb.append(functionName).append("(").append(leftT + STRUCT_ACCESS + "matrix, ");
			if (sparseSystem) {
				// add rowIndex, columns and matrix_type
				sb.append(leftT + STRUCT_ACCESS + "rowIndex, ");
				sb.append(leftT + STRUCT_ACCESS + "columns, ");
				sb.append(sparseMatrixType).append(", ");
			}
			sb.append(rightT + STRUCT_ACCESS + "matrix, ");
			if (sparseRHS) {
				sb.append(rightT + STRUCT_ACCESS + "rowIndex, ");
				sb.append(rightT + STRUCT_ACCESS + "columns, ");
			}
			sb.append(newSymbol + STRUCT_ACCESS + "matrix, ").append(rows + ",").append(acols + ",").append(cols)
					.append(");").append(NL);
		} else
			throw new TypeException(CErrorMessage.UNSUPPORTED_MATRIX_MATRIX_OPERATION, curRoot);

		newComment(curRoot);
		newTranslation(curRoot, sb.toString());
		// update matrix dimensions map kept in C class because C-specific
		// updateMatrixDims(newSymbol, newSymbolDims);
		return newSymbol;
	}

	private boolean matrixMatrixPointwiseOp(AASTNode curRoot, AASTNode left, AASTNode right) {
		GType leftType = getExprGeneralized(left);
		GType rightType = getExprGeneralized(right);
		GType outType = TypeUtils.getExprGeneralized(curRoot);
		if ((leftType instanceof DimensionType) && (rightType instanceof DimensionType)) {
			IntType[] matrixLeftDims = TypeUtils.realDims(((DimensionType) leftType).dims());
			IntType[] matrixRightDims = TypeUtils.realDims(((DimensionType) rightType).dims());

			return outType.isCastableToScalar() && (TypeUtils.pointwiseExpressionNodes().contains(curRoot.type())
					|| (matrixLeftDims.length == 1 && matrixRightDims.length == 1));
		} else
			return true;
	}

	private boolean pointwiseOp(AASTNode curRoot, AASTNode left, AASTNode right) {
		GType leftType = getExprGeneralized(left);
		GType rightType = getExprGeneralized(right);
		GType outType = TypeUtils.getExprGeneralized(curRoot);
		if ((leftType instanceof DimensionType) && (rightType instanceof DimensionType)) {
			IntType[] matrixLeftDims = TypeUtils.realDims(((DimensionType) leftType).dims());
			IntType[] matrixRightDims = TypeUtils.realDims(((DimensionType) rightType).dims());

			return TypeUtils.pointwiseExpressionNodes().contains(curRoot.type())
					|| (matrixLeftDims.length == 1 && matrixRightDims.length == 1);
		} else
			return true;
	}

	@StepType(method = StepType.Function.MATRIXSCALAREXPRESSION)
	public String translateMatrixScalarExpression(AASTNode topLevel, AASTNode curRoot, AASTNode matrixNode,
			AASTNode scalarNode, String matrixName, String scalarName, boolean toBeInverted, boolean matrixParamSparse)
			throws TypeException, UndefinedTranslationException {
		boolean containsSparseSubExpr = topLevel.hasAttr(CAttr.CONTAINS_SPARSE_SUBEXPRESSION);
		String operand = null, sparseOperand = null;
		boolean funcall = false;
		boolean needToBeCasted = false;
		// serialize only if at toplevel of operations a*(2+1) -> TIMES
		String cType = null;
		GType type = getExprGeneralized(curRoot);

		if (type != null) {
			if (!(type instanceof DimensionType)) {
				cType = exprTypeToCType(type);
			} else {
				cType = exprTypeToCType(((DimensionType) type).of());
			}
		}

		char typec = '0';
		if (((DimensionType) type).of().equals(BType.SCALAR))
			typec = 'd';
		else
			typec = 'i';

		switch (curRoot.type()) {
		case PLUS:
			operand = "+";
			sparseOperand = typec + "SparsePlus";
			break;
		case MINUS:
			operand = "-";
			sparseOperand = typec + "SparseMinus";
			break;
		case TIMES:
		case ELEMENTWISE_TIMES:
			operand = "*";
			sparseOperand = typec + "SparseTimes";
			break;
		case ELEMENTWISE_LEFTDIV:
		case LEFTDIV:
			operand = "/";
			sparseOperand = typec + "SparseDivide";
			needToBeCasted = true;
			cType = "double";
			break;
		case ELEMENTWISE_EXP:
		case EXP:
			operand = "pow";
			sparseOperand = typec + "SparsePow";
			// If top-level operations are scalar, doesn't need
			// otherwise it needs
			if (topLevel != null) {
				GType topLevelType = getExprGeneralized(topLevel);
				needToBeCasted = topLevelType != null && topLevelType.isCastableToBool();
			} else
				needToBeCasted = true;
			funcall = true;
			break;
		default:
			throw new TypeException(CErrorMessage.UNSUPPORTED_OPERAND, curRoot);
		}
		// NOTE: this can be replaced if this translation happen to end up
		// in a matrix definition [1 2 2*x+[1 2 3] 4 5]
		// get the matrix dimension array
		GType matrixType = getExprGeneralized(matrixNode);
		String index = null;

		if (matrixParamSparse) {
			index = sparseAccess(matrixName);
		} else if (TypeUtils.isDegeneratedMatrix(matrixType)) {
			// replace iterator variable with 0 since this is a degenere matrix
			index = "0";
		} else if (containsSparseSubExpr) {
			List<String> leftDims = nodeDimsToStr(matrixNode, ((DimensionType) matrixType).dims(), true, true);
			index = get2DAccessIndex(EXPR_INDEX, SPARSE_ROW_INDEX, leftDims);
		} else {
			index = EXPR_INDEX;
		}

		String indexDimensionType = indexDimensionType(matrixNode, matrixName, index,
				matrixNode.symbol().equals(matrixName));

		StringBuffer sb = new StringBuffer();

		if (matrixType instanceof SliceType && !funcall) {

			// get the result type of
			GType typeOf = ((DimensionType) type).of();
			String prefix = null;
			if (typeOf instanceof ScalarType) {
				prefix = "d";
			} else {
				prefix = "i";
			}

			String[] toBeSetParameters = new String[4];
			switch (curRoot.type()) {
			case PLUS:
				// return the new slice
				toBeSetParameters[0] = matrixName + STRUCT_ACCESS + "start+" + scalarName;
				toBeSetParameters[1] = matrixName + STRUCT_ACCESS + "step";
				toBeSetParameters[2] = matrixName + STRUCT_ACCESS + "end+" + scalarName;
				toBeSetParameters[3] = matrixName + STRUCT_ACCESS + "dim1";

				break;
			case MINUS:
				// return the new slice
				toBeSetParameters[0] = matrixName + STRUCT_ACCESS + "start-" + scalarName;
				toBeSetParameters[1] = matrixName + STRUCT_ACCESS + "step";
				toBeSetParameters[2] = matrixName + STRUCT_ACCESS + "end-" + scalarName;
				toBeSetParameters[3] = matrixName + STRUCT_ACCESS + "dim1";

				break;
			case LEFTDIV:
			case ELEMENTWISE_LEFTDIV:
				// return the new slice
				toBeSetParameters[0] = matrixName + STRUCT_ACCESS + "start/((" + cType + ") " + scalarName + ")";
				toBeSetParameters[1] = matrixName + STRUCT_ACCESS + "step/((" + cType + ") " + scalarName + ")";
				toBeSetParameters[2] = matrixName + STRUCT_ACCESS + "end/((" + cType + ") " + scalarName + ")";
				toBeSetParameters[3] = matrixName + STRUCT_ACCESS + "dim1";

				break;
			case TIMES:
			case ELEMENTWISE_TIMES:
				// return the new slice
				toBeSetParameters[0] = matrixName + STRUCT_ACCESS + "start*" + scalarName;
				toBeSetParameters[1] = matrixName + STRUCT_ACCESS + "step*" + scalarName;
				toBeSetParameters[2] = matrixName + STRUCT_ACCESS + "end*" + scalarName;
				toBeSetParameters[3] = matrixName + STRUCT_ACCESS + "dim1";

				break;
			default:
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_SCALAR_SLICE_OPERATION, curRoot);
			}
			String slicetype = prefix + "slice";
			AASTNode outNode = getOutputNode(curRoot);
			newComment(curRoot);
			newTranslation(curRoot,
					outNode.symbol() + " = ((" + slicetype + "){.start = " + toBeSetParameters[0] + ", .step = "
							+ toBeSetParameters[1] + ", .end = " + toBeSetParameters[2] + ", .dim1 = "
							+ toBeSetParameters[3] + ", .linear = true});" + NL);
			return outNode.symbol();
		} else {
			// if there are already dimensions computed
			if (topLevelRHSExpression(curRoot)) {
				AASTNode outNode = getOutputNode(curRoot);

				String newMatrix = getNodeSymbol(outNode);
				sb = finalizeExpression(curRoot, outNode, curRoot, sb, indexDimensionType, operand, scalarName, cType,
						needToBeCasted, toBeInverted, funcall);

				newComment(curRoot);
				newTranslation(curRoot, sb.toString());
				return newMatrix;
			} else {
				// TODO
				String tmp, finalTranslation;
				if (toBeInverted) {
					tmp = indexDimensionType;
					indexDimensionType = scalarName;
					scalarName = tmp;
				}
				if (topLevel.expr().equals(BType.MATRIX) && topLevel.mexpr().isSparse()) {
					// sparse expression
					// don't set translate=false
					AASTNode outNode = getOutputNode(topLevel, true, false, false, null);
					GType outType = getExprGeneralized(outNode);
					DimensionType outDimType = (DimensionType) outType;
					String outSymbol = null;
					if (!outNode.hasAttr(NodeAttr.DYNAMIC_ID))
						outSymbol = outNode.symbol();
					else
						outSymbol = topLevel.symbol();
					String nz = SPARSE_ELEMENT_COUNT;
					String x = (outDimType.of().equals(BType.SCALAR)) ? SPARSE_TMP_DOUBLE_COLUMN
							: SPARSE_TMP_INT_COLUMN;
					String w = (outDimType.of().equals(BType.SCALAR)) ? SPARSE_TMP_DOUBLE_MARKER
							: SPARSE_TMP_INT_MARKER;
					finalTranslation = nz + " = " + sparseOperand + "(&" + matrixName + ", " + SPARSE_ROW_INDEX + ", "
							+ SPARSE_ROW_INDEX + " + 1, " + scalarName + ", " + w + ", " + x + ", " + nz + ", &"
							+ outSymbol + ");" + NL;
				} else if (!funcall)
					// put on stack only this operation
					if (needToBeCasted && cType != null) {
						finalTranslation = "(((" + cType + ")" + indexDimensionType + ") " + operand + " " + scalarName
								+ ")";
					} else {
						finalTranslation = "(" + indexDimensionType + " " + operand + " " + scalarName + ")";
					}
				else {
					if (needToBeCasted && cType != null) {
						finalTranslation = "(" + cType + ")" + operand + "(" + indexDimensionType + ", " + scalarName
								+ ")";
					} else {
						finalTranslation = operand + "(" + indexDimensionType + ", " + scalarName + ")";
					}
				}
				return finalTranslation;
			}
		}
	}

	/**
	 * this method is used to decide when (at which level of the tree) an expression
	 * should be translated
	 * 
	 * @param curRoot
	 * @return
	 */
	protected boolean topLevelExpression(AASTNode curRoot) {
		List<NodeType> exprNodes = TypeUtils.expressionNodes();
		// in C also VECTOR is able to handle complex elements
		exprNodes.add(NodeType.VECTOR);
		// not toplevel if do have parent if, the if/while statement is the ultimate
		// place for translation
		exprNodes.add(NodeType.IF);
		exprNodes.add(NodeType.ELSEIF);
		exprNodes.add(NodeType.WHILE);
		return !curRoot.parentExists(exprNodes, TypeUtils.expressionTreeLimit(curRoot));
	}

	/**
	 * an expression that can be translated in the right hand side directly
	 * 
	 * @param curRoot
	 * @return
	 */
	protected boolean topLevelRHSExpression(AASTNode curRoot) {
		return topLevelExpression(curRoot) && translatableExpression(curRoot);
	}

	/**
	 * an expression that should wait for ASSIGN node to be translated (thus the
	 * rhs_symbol in ASSIGN represents an expression)
	 * 
	 * @param curRoot
	 * @return
	 */
	protected boolean topLevelAssignExpression(AASTNode curRoot) {
		return topLevelExpression(curRoot) && !translatableExpression(curRoot);
	}

	/**
	 * a non-matrix expression is already translated as it is (doesn't need for
	 * loop) -> translatable a sparse matrix should always be translated at every
	 * step for the way they are encoded -> translatable a non-sparse matrix is
	 * translatable if canBeTranslatedByRHS is false
	 * 
	 * @param curRoot
	 * @return
	 */
	protected boolean translatableExpression(AASTNode curRoot) {
		return ((!getExprGeneralized(curRoot).equals(BType.MATRIX)
				|| ((MatrixType) getExprGeneralized(curRoot)).isSparse()) || !canBeTranslatedByRHS(curRoot));
	}

	/**
	 * a non-sparse matrix is translatable if it's the right hand side of an
	 * assignment and doesn't have a top apply/matrix/transpose
	 * 
	 * @param outSymbol
	 * @return true if the node is ready to be translated
	 */
	protected boolean canBeTranslatedByRHS(AASTNode outSymbol) {
		return !outSymbol.parentExists(TypeUtils.expressionNodes())
				// symbols that doesn't have parent apply can be translated
				&& !outSymbol.parentExists(NodeType.APPLY)
				// symbols that doesn't have parent matrix can be translated
				&& !outSymbol.parentExists(NodeType.MATRIX)
				/*
				 * transpose of known matrix has already the dimensions swapped, create an extra
				 * node.
				 */
				/*
				 * this can cause problems (a 1x4 matrix that is filled like it was a 4x1
				 * matrix)
				 */
				&& !((outSymbol.parentExists(NodeType.TRANSPOSE) || outSymbol.parentExists(NodeType.ELEMENTWISE_CCT))
						&& outSymbol.hasAttr(NodeAttr.FIXED_MATRIX)
						&& !((Boolean) outSymbol.attr(NodeAttr.FIXED_MATRIX))
						&& TypeUtils.matrixTotalDimensions((DimensionType) getExprGeneralized(outSymbol)) >= 0)
				&& outSymbol.parentExists(NodeType.RHS);
	}

	protected AASTNode getTopLevelExpression(AASTNode curRoot) {
		List<NodeType> exprNodes = TypeUtils.expressionNodes();
		// in C also VECTOR is able to handle complex elements
		exprNodes.add(NodeType.VECTOR);
		exprNodes.remove(NodeType.PARENS);
		// search the topmost expression that is not between matrices as a whole
		// (example solve of linear system or dot product)
		AASTNode prevCandidate = curRoot;
		AASTNode topLevelCandidate = prevCandidate;
		while (topLevelCandidate != null && (topLevelCandidate.childs().size() == 1 || pointwiseOp(topLevelCandidate,
				topLevelCandidate.childs().get(0), topLevelCandidate.childs().get(1)))) {
			prevCandidate = topLevelCandidate;
			topLevelCandidate = topLevelCandidate.parent(exprNodes, TypeUtils.expressionTreeLimit(topLevelCandidate));
		}
		return (topLevelCandidate == null || (topLevelCandidate.childs().size() > 1 && !pointwiseOp(topLevelCandidate,
				topLevelCandidate.childs().get(0), topLevelCandidate.childs().get(1)))) ? prevCandidate
						: topLevelCandidate;
	}

	/**
	 * note: if !funcall and toBeInverted then indexDimensionType and operand can be
	 * null, allowing to perform a single-assignment for
	 * 
	 * @param outNode
	 * @param iterDimsNode
	 * @param sb
	 * @param scalarName
	 * @param operand
	 * @param indexDimensionType
	 * @param cType
	 * @param needToBeCasted
	 * @param toBeInverted
	 * @param funcall
	 * @return
	 * @throws UndefinedTranslationException
	 * @throws TypeException
	 */
	private StringBuffer finalizeExpression(AASTNode topLevel, AASTNode outNode, AASTNode iterDimsNode, StringBuffer sb,
			String leftT, String operand, String rightT, String cType, boolean needToBeCasted, Boolean toBeInverted,
			boolean funcall) throws UndefinedTranslationException, TypeException {
		String newMatrix = getNodeSymbol(outNode);
		boolean containsSparseSubExpr = topLevel.hasAttr(CAttr.CONTAINS_SPARSE_SUBEXPRESSION);
		GType topExpr = getExprGeneralized(topLevel);
		boolean sparseSubExpr = topExpr.equals(BType.MATRIX) && ((MatrixType) topExpr).isSparse();
		String index = EXPR_INDEX;
		// extract eventual sparse accumulators (intermediate for)
		String[] lefttranslations = new String[] {};
		String leftTranslation = "";
		GType leftType = null;
		if (leftT != null) {
			leftType = getExprGeneralized(topLevel.childs().get(0));
			lefttranslations = leftT.split("\\" + SPARSE_EXPR_SEPARATOR);
			leftTranslation = lefttranslations[lefttranslations.length - 1];
		}

		String[] righttranslations = new String[] {};
		String rightTranslation = "";
		GType rightType = null;
		if (rightT != null) {
			rightType = getExprGeneralized(topLevel.childs().get(1));
			righttranslations = rightT.split("\\" + SPARSE_EXPR_SEPARATOR);
			rightTranslation = righttranslations[righttranslations.length - 1];
		}

		if (toBeInverted) {
			String tmp = leftTranslation;
			leftTranslation = rightTranslation;
			rightTranslation = tmp;
		}
		// List<String> dims = getParametricDimensions(newMatrix, ((DimensionType)
		// outNode.expr()).dims());
		List<String> dims;
		if (iterDimsNode != null) {
			GType iterDimsNodeType = getExprGeneralized(iterDimsNode);
			// if 2 or less dimensions, preserve dimensions
			IntType[] iterDims = TypeUtils.realDims(((DimensionType) iterDimsNodeType).dims(), 2);
			GType outNodeExpr = getExprGeneralized(outNode);
			dims = nodeDimsToStr(iterDimsNode, iterDims, true);
		} else
			dims = nodeDimsToSymbolicReference(outNode);

		// ---- assemble last operation ----
		String fullOperation = null;
		StringBuffer accBuf = new StringBuffer();
		if (sparseSubExpr) {
			StringBuffer ssb = new StringBuffer();
			AASTNode allocNode = null;
			if (outNode.hasAttr(NodeAttr.DYNAMIC_ID)) {
				allocNode = topLevel;
				createVariable(topLevel);
				// compute max sparse dimension
				newMatrix = topLevel.symbol();
			} else
				allocNode = outNode;

			// compute max sparse dimension
			List<AASTNode> subExprs = (List<AASTNode>) topLevel.attr(CAttr.CONTAINS_SPARSE_SUBEXPRESSION);
			List<String> subExprDims = new ArrayList<String>(subExprs.size());
			for (AASTNode subexpr : subExprs)
				subExprDims.add(subexpr.symbol() + STRUCT_ACCESS + "usedCells");
			ensureMatrixMemory(accBuf, allocNode, allocNode.symbol(), topLevel.mexpr().dims(),
					String.join(" + ", subExprDims));

			GType outType = getExprGeneralized(outNode);
			DimensionType outDimType = (DimensionType) outType;

			String nz = SPARSE_ELEMENT_COUNT;
			String x = (outDimType.of().equals(BType.SCALAR)) ? SPARSE_TMP_DOUBLE_COLUMN : SPARSE_TMP_INT_COLUMN;

			// set start of row
			ssb.append(newMatrix).append(STRUCT_ACCESS).append("rowIndex[" + SPARSE_ROW_INDEX + "] = " + nz + "+1;")
					.append(NL);

			if (!leftType.isCastableToScalar())
				ssb.append(leftTranslation);
			if (!rightType.isCastableToScalar())
				ssb.append(rightTranslation);

			String colIndex = newMatrix + "_ci";
			// final for to write back the values in output->matrix
			ssb.append("#pragma omp smid").append(NL);
			ssb.append("for (int " + colIndex + " = " + newMatrix + STRUCT_ACCESS + "rowIndex[" + SPARSE_ROW_INDEX
					+ "]-1; " + colIndex + " < " + nz + " ; ++" + colIndex + ") " + newMatrix + STRUCT_ACCESS
					+ "matrix[" + colIndex + "] = ");
			if (leftType.isCastableToScalar())
				ssb.append(leftTranslation).append(" ").append(operand).append(" ");
			ssb.append(x + "[" + newMatrix + STRUCT_ACCESS + "columns[" + colIndex + "]-1]");
			if (rightType.isCastableToScalar())
				ssb.append(" ").append(operand).append(" ").append(rightTranslation);
			ssb.append(";").append(NL);

			fullOperation = ssb.toString();
		} else {
			if (!funcall) {
				fullOperation = ((leftTranslation != null) ? leftTranslation : "") + " "
						+ ((operand != null) ? operand : "") + " " + rightTranslation;
			} else
			// matrix is the left side of operator by default. it does not
			// have a meaning to make 2^A where A is a matrix..
			if (needToBeCasted && cType != null) {
				fullOperation = "(" + cType + ")" + operand + "(" + leftTranslation + " " + ", " + rightTranslation
						+ ")";
			} else {
				fullOperation = operand + "(" + leftTranslation + ", " + rightTranslation + ")";
			}
		}
		// output accumulated operations
		for (int opi = 0; opi < lefttranslations.length - 1; ++opi)
			accBuf.append(lefttranslations[opi]);
		for (int opi = 0; opi < righttranslations.length - 1; ++opi)
			accBuf.append(righttranslations[opi]);
		sb.append(accBuf);

		if (!containsSparseSubExpr) {
			ensureMatrixMemory(sb, outNode, dims.toArray(new String[dims.size()]));
			String n = String.join(" * ", dims);

			sb.append("#pragma omp smid").append(NL);
			sb.append("for (int " + index + " = 0; " + index + " < " + n + "; ++" + index + ")" + NL);
			sb.append(TAB + newMatrix + STRUCT_ACCESS + "matrix[" + index + "] = " + fullOperation + ";" + NL);
		} else if (sparseSubExpr) {
			// if toplevel is sparse, this is an operation between sparse and scalars,
			// optimize loop
			// mixed sparse/non-sparse operation, fallback to full loop
			StringBuffer topLevelOp = processPureSparseOperation(topLevel, outNode, newMatrix, fullOperation);
			sb.append(topLevelOp);
		} else {
			// mixed sparse/non-sparse operation, fallback to full loop
			StringBuffer topLevelOp = processMixedSparseOperation(topLevel, outNode, newMatrix, fullOperation);
			sb.append(topLevelOp);
		}

		return sb;
	}

	// returns the sequence access with iSequenceAcess or dSequenceAccess
	private String getSequenceAccess(AASTNode node, String intAccess) {

		GType sliceType = getExprGeneralized(node);
		String sliceName = sliceType.name();

		String prefix = null;
		DimensionType type = (DimensionType) getExprGeneralized(node);

		if (type.of().equals(BType.SCALAR)) {
			prefix = "d";
		} else {
			prefix = "i";
		}

		String result = "v" + prefix + "SliceAccess(&" + sliceName + ", " + intAccess + ")";
		return result;
	}

	private String getSequenceDimension(AASTNode node) throws UndefinedTranslationException {
		GType sliceType = getExprGeneralized(node);
		return getSequenceDimension(sliceType);
	}

	// returns the sequence dimension with iSequenceDimension or dSequenceDimension
	private String getSequenceDimension(GType sliceType) throws UndefinedTranslationException {
		String sliceName = sliceType.name();

		String[] sliceParameters = new String[3];

		sliceParameters[0] = sliceName + STRUCT_ACCESS + "start";
		sliceParameters[1] = sliceName + STRUCT_ACCESS + "step";
		sliceParameters[2] = sliceName + STRUCT_ACCESS + "end";

		return getSequenceDimension(sliceType, sliceParameters);
	}

	private String getSequenceDimension(GType sliceType, String[] sliceParameters) {
		String prefix = null;
		DimensionType type = (DimensionType) sliceType;

		if (type.of().equals(BType.SCALAR)) {
			prefix = "d";
		} else {
			prefix = "i";
		}

		String result = "v" + prefix + "SequenceDimension(" + sliceParameters[0] + ", " + sliceParameters[1] + ", "
				+ sliceParameters[2] + ")";
		return result;

	}

	public boolean isCoreFunction(AASTNode fun) {
		return fun.hasAttr(NodeAttr.REF_CORE_FUNCTION);
	}

	protected IFunction getCoreFunctionSpec(AASTNode curApplyNode, String functionName)
			throws UndefinedTranslationException {
		IFunction fenum = (IFunction) curApplyNode.attr(NodeAttr.REF_CORE_FUNCTION);
		if (fenum == null && curApplyNode.type().equals(NodeType.APPLY))
			fenum = (IFunction) curApplyNode.child(NodeType.ID).attr(NodeAttr.REF_CORE_FUNCTION);
		if (fenum == null)
			throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION, curApplyNode,
					functionName);
		return fenum;
	}

	@StepType(method = StepType.Function.COREFUNCTION)
	public String translateCoreFunction(AASTNode curApplyNode, String functionName, String paramsTranslated)
			throws IllegalArgumentException, UndefinedTranslationException, TypeException, MissingNodeException,
			InputException, CastException {

		IFunction fenum = getCoreFunctionSpec(curApplyNode, functionName);

		List<AASTNode> params = curApplyNode.childs(NodeType.FUNCTION_PARAMETER_LIST);
		// if preamble needed (a.k.a. output symbol needed)
		// use apply node as output node.
		AASTNode outSymbol = curApplyNode;

		AASTNode first;
		switch (fenum.getName().toLowerCase()) {
		// symbolic types functions
		case "class":
			// class returns a type used to instruct middle-end that a core function should
			// return a different type, so it's transparent to C translation
			// until is passed to an user-defined function..but this is not supported yet.
			return "";
		case "squeeze": {
			String inMatrix = paramsTranslated;
			if (params.size() == 0)
				throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS, curApplyNode,
						"squeeze", "", "Matrix");
			AASTNode param = params.get(0);
			if (curApplyNode.parentExists(new NodeType[] { NodeType.GASSIGN, NodeType.ASSIGN })) {
				AASTNode assign = curApplyNode.parent(new NodeType[] { NodeType.GASSIGN, NodeType.ASSIGN });
				AASTNode squeezeLhs = getIDNode(assign.child(NodeType.LHS).childs().get(0));
				if (squeezeLhs.type().equals(NodeType.ID) && squeezeLhs.name().equals(inMatrix)) {
					// do nothing because a=squeeze(a) has effect only on type not on data.
					// the final type was already decided by middle end
					assign.attr(CAttr.TRANSLATE, false);
					return "";
				}
			}
			// not of the form a = squeeze(a), always assign
			AASTNode outNode = getOutputNode(outSymbol, true, false);
			String outVarName = outNode.symbol();
			StringBuffer sb = new StringBuffer();

			DimensionType dtype = (DimensionType) getExprGeneralized(param);
			if (dtype.equals(BType.MATRIX_ACCESS_SLICE))
				throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS, curApplyNode,
						"squeeze", "", "Matrix");

			MatrixType mtype = (MatrixType) dtype;
			Tuple<IntType[], List<Integer>> tp = TypeUtils.realDimsWithPos(mtype.dims());
			IntType[] outDims = tp.first();
			List<Integer> inNonOnes = tp.second();
			int outId = 1;
			for (Integer inId : inNonOnes)
				sb.append(TypeUtils.matrixDimName(outVarName, outId++)).append(" = ")
						.append(TypeUtils.matrixDimName(inMatrix, inId + 1)).append(";").append(NL);

			sb.append(copyMatrixFields(outVarName, inMatrix));
			newComment(curApplyNode);
			newTranslation(curApplyNode, sb.toString());
			return outVarName;
		}
		// variables
		case "tic":
			if (!tic_defined) {
				getDeclarationBuffer(VarVisibility.GLOBAL).first()
						.append("static __thread clock_t " + TIC_GLOBAL_VARIABLE + ";").append(NL);
				tic_defined = true;
			}
			newComment(curApplyNode);
			newTranslation(curApplyNode, TIC_GLOBAL_VARIABLE + " = clock();" + NL);
			// if tic in an expression statement don't push anything on stack, otherwise do
			// it
			if (curApplyNode.parentExists(NodeType.EXPR_STMT, 3))
				return null;
			else
				return TIC_GLOBAL_VARIABLE;
		case "toc": {
			if (!toc_defined) {
				getDeclarationBuffer(VarVisibility.GLOBAL).first()
						.append("static __thread double " + TOC_GLOBAL_VARIABLE + ";").append(NL);
				toc_defined = true;
			}
			newComment(curApplyNode);
			String start;
			if (params != null && !params.isEmpty())
				start = paramsTranslated;
			else
				start = TIC_GLOBAL_VARIABLE;
			newTranslation(curApplyNode,
					TOC_GLOBAL_VARIABLE + " = ((double)clock() - " + start + ")/CLOCKS_PER_SEC;" + NL);
			// reinitialize tic
			newTranslation(curApplyNode, TIC_GLOBAL_VARIABLE + " = clock();" + NL);
			String tocResult = TOC_GLOBAL_VARIABLE;
			if (curApplyNode.parentExists(NodeType.EXPR_STMT, 3)) {
				tocResult = "printf(\"Time elapsed %2f s\\n\", " + tocResult + ");" + NL;
				newComment(outSymbol);
				newTranslation(curApplyNode, tocResult);
				return null;
			} else
				return tocResult;
		}
		case "nargin": {
			List<AASTNode> funParams = curApplyNode.parent(NodeType.FUNCTION).childs(NodeType.PARAMETER_LIST);
			// base case
			if (funParams.size() == 0 || (funParams.size() == 1 && funParams.get(0).type().equals(NodeType.VOID)))
				return "0";
			// compute non-null number of arguments
			String[] funParamNames = new String[funParams.size()];
			int i = 0;
			for (AASTNode p : funParams)
				funParamNames[i++] = "!" + getNullCheckMacro(p) + "(" + p.name() + ")";
			// number of arguments not null in this function call
			String narginValue = "(" + String.join(" + ", funParamNames) + ")";
			createVariable(GType.get(BType.INT), "__nargin", NodeType.EXPRESSION, narginValue, false,
					VarVisibility.LOCAL_TO_FUNCTION);
			return "__nargin";
		}
		case "pi":
			ScalarType piType = (ScalarType) curApplyNode.expr();
			return piType.valueAsString();
		case "inf":
			if (params == null || params.isEmpty())
				return "INFINITY";
			else
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, "inf", params.get(0).name());
		case "break":
			newComment(curApplyNode);
			newTranslation(curApplyNode, "break;");
			return null;
		case "continue":
			newComment(curApplyNode);
			newTranslation(curApplyNode, "continue;");
			return null;
		case "fprintf": {
			String[] translatedParams = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			if (translatedParams.length < 2)
				throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS, curApplyNode,
						"fprintf", translatedParams, new String[] { "format", "sequence of variables" });
			// we don't support file specifier
			String translatedParamsAsFunParams = String.join(", ", translatedParams);
			StringBuffer sb = new StringBuffer();
			sb.append("printf(").append(translatedParamsAsFunParams).append(");").append(NL);
			newComment(curApplyNode);
			newTranslation(curApplyNode, sb.toString());
			// translate here
			return null;
		}
		case "sprintf": {
			String[] translatedParams = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			if (translatedParams.length < 2)
				throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS, curApplyNode,
						"sprintf", translatedParams, new String[] { "format", "sequence of variables" });
			// strings are always static, so that in loops that print we don't leak memory
			AASTNode outNode = getOutputNode(outSymbol, false, false, true, "NULL");
			String outVarName = outNode.symbol();
			String translatedParamsAsFunParams = String.join(", ", translatedParams);
			StringBuffer sb = new StringBuffer();
			sb.append("if( ").append(outVarName).append(" == NULL )").append(NL);
			sb.append(TAB).append(outVarName).append(
					" = qspcc_malloc(sizeof(char) * (snprintf(NULL, 0, " + translatedParamsAsFunParams + ")+1));")
					.append(NL);
			sb.append("else").append(NL);
			sb.append(TAB).append(outVarName).append(" = qspcc_realloc(" + outVarName
					+ ", sizeof(char) * (snprintf(NULL, 0, " + translatedParamsAsFunParams + ")+1));").append(NL);
			sb.append("int ").append(outVarName + "_nchars = ").append(" sprintf(").append(outVarName).append(", ")
					.append(translatedParamsAsFunParams).append(");").append(NL);
			sb.append("assert(").append(outVarName + "_nchars > 0 ").append(" && \"Failed apply sprintf function\");")
					.append(NL);

			newComment(outSymbol);
			newTranslation(curApplyNode, sb.toString());

			return outVarName;
		}
		case "error": {
			String toBeWritten = "printf (\"ERROR: " + paramsTranslated.replaceAll("\"|'", "")
					+ "\\nat line %d of file %s (function %s)\",  __LINE__, __FILE__, __func__);";
			// defer translation to EXPR_STMT node
			return toBeWritten;
		}
		case "disp":
			String toBeWritten = null;
			if (params.isEmpty())
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_WITHOUT_ARGUMENT,
						curApplyNode, "disp");
			if (params.size() != 1)
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_WITHOUT_ARGUMENT,
						curApplyNode, "disp");

			// defer translation to EXPR_STMT node
			first = params.get(0);
			GType expr = getExprGeneralized(first);
			toBeWritten = printType(curApplyNode, expr, paramsTranslated);
			return toBeWritten;
		case "find": {
			// iterate over all elements and store in outvarname the non-zero indexes
			if (params.size() > 3 || params.isEmpty()) {
				String[] given = new String[params.size()];
				int i = 0;
				for (AASTNode param : params)
					given[i++] = param.code();
				String[] expected = new String[1];
				expected[0] = "Matrix";
				throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS, curApplyNode,
						functionName, given, expected);
			}
			String[] translatedParams = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			String inMatrixTranslated = translatedParams[0];
			AASTNode param = params.get(0);
			String limit = null, direction = null;
			String howmanyvar = "_cnt_" + inMatrixTranslated;
			String howmanyvarCounter = EXPR_INDEX + "_cnt_" + inMatrixTranslated;
			String foundElements = "_cnt_found_" + inMatrixTranslated;
			AASTNode outNode = null;
			GType applyType = getExprGeneralized(outSymbol);
			// always assign
			outNode = getOutputNode(outSymbol, true, false);
			String outVarName = outNode.symbol();

			if (params.size() > 1)
				limit = (String) getIDNode(params.get(1)).symbol();
			if (params.size() > 2)
				direction = translatedParams[2];

			if (!(getExprGeneralized(param) instanceof MatrixType))
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_WARN_PARAMETER_TYPE_UNSUPPORTED,
						curApplyNode, param.code(), param.expr(), functionName);

			MatrixType mtype = (MatrixType) getExprGeneralized(param);
			String[] dimsAsStr = new String[mtype.dims().length];
			String[] translatedParamDims = new String[mtype.dims().length];
			int d;
			for (d = 0; d < mtype.dims().length; ++d) {
				dimsAsStr[d] = mtype.dims()[d].valueAsString();
				translatedParamDims[d] = TypeUtils.matrixDimName(inMatrixTranslated, d + 1);
			}
			String indexName = EXPR_INDEX;
			String totalDim = String.join(" * ", dimsAsStr);
			String translatedParamDim = String.join(" * ", translatedParamDims);
			String inMatrixValue;
			String inMatrixIndex;
			String inMatrixDim1 = null, inMatrixDim2 = null;
			inMatrixDim1 = translatedParamDims[0];
			if (translatedParamDims.length > 1)
				inMatrixDim2 = translatedParamDims[1];
			else
				inMatrixDim2 = "1";
			inMatrixIndex = indexName;
			inMatrixValue = inMatrixTranslated + STRUCT_ACCESS + "matrix[" + inMatrixIndex + "]";

			createVariable(GType.get(BType.INT), foundElements, NodeType.ID, "0", false,
					VarVisibility.LOCAL_TO_BLOCK.context(curApplyNode));
			StringBuffer sb = new StringBuffer();

			// Compute number of matching elements first
			createVariable(GType.get(BType.INT), howmanyvar, NodeType.ID, "0", false,
					VarVisibility.LOCAL_TO_BLOCK.context(curApplyNode));
			sb.append("#pragma omp smid").append(NL);
			sb.append("for(int ").append(indexName).append(" = 0; ").append(indexName).append(" < ").append(totalDim);
			sb.append("; ++").append(indexName).append(")").append(NL);
			sb.append(TAB);

			TypeUtils.matrixDimName(inMatrixTranslated, 2);
			sb.append("if(").append(inMatrixValue).append(" > 0").append("){").append(NL);
			// then
			// count how many are they
			sb.append(TAB).append(TAB).append(howmanyvar).append("++;").append(NL);
			if (limit != null) {
				sb.append(TAB).append(TAB).append("if(").append(howmanyvar).append(" >= ").append(limit).append(")")
						.append(NL);
				sb.append(TAB).append(TAB).append(TAB).append("break;").append(NL);
			}
			sb.append(TAB).append("}");

			// ACTUALLY ASSIGN
			StringBuffer sbassign = new StringBuffer();
			if (applyType.isCastableToMatrix()) {
				// ENSURE MATRIX DIMENSION
				// make sure that custom dimensions match the output node number of dimensions
				DimensionType outNodeType = (DimensionType) getExprGeneralized(outNode);
				List<String> outDimsAsStr = new LinkedList<String>();
				outDimsAsStr.add(howmanyvar);
				for (int on = 1; on < outNodeType.dims().length; ++on)
					outDimsAsStr.add("1");
				boolean allocated = ensureMatrixMemory(sbassign, outNode,
						outDimsAsStr.toArray(new String[outDimsAsStr.size()]));
				if (!allocated) {
					// force update at dimensions (needed for successive isempty check or to avoid
					// segfault)
					List<String> symbolicDims = nodeDimsToSymbolicReference(outNode, outNodeType.dims());
					for (int i = 0; i < symbolicDims.size(); ++i)
						sbassign.append(symbolicDims.get(i)).append(" = ").append(outDimsAsStr.get(i)).append(";")
								.append(NL);
				}
			}

			sbassign.append("if(").append(howmanyvar).append(" > 0){").append(NL);
			sbassign.append("#pragma omp smid").append(NL);
			sbassign.append(TAB).append("for(int ").append(indexName);
			boolean forwardDirection = direction == null || direction.equals("first") || direction.equals("\"first\"");
			if (forwardDirection) {
				// readd all the for loops and things before, the difference is the variable
				// over which we are iterating
				sbassign.append(" = 0; ").append(indexName).append(" < ").append(translatedParamDim);
			} else {
				// case last, iterate in reverse order
				sbassign.append(" = ").append(translatedParamDim).append("; ").append(indexName).append(" >= 0");
			}
			sbassign.append(" && ");
			sbassign.append(foundElements).append(" < ").append(howmanyvar);
			if (forwardDirection) {
				sbassign.append("; ++").append(indexName);
			} else {
				sbassign.append("; --").append(indexName);
			}
			sbassign.append(")").append(NL);

			sbassign.append(TAB).append(TAB);
			if (ROWMAJOR)
				sbassign.append("if(")
						.append(inMatrixValue.replaceAll(indexName,
								"colMajor2RowMajor(" + indexName + ", " + inMatrixDim1 + ", " + inMatrixDim2 + ")"))
						.append(" > 0");
			else
				sbassign.append("if(").append(inMatrixValue).append(" > 0");
			sbassign.append(")").append(NL);
			// then
			sbassign.append(TAB).append(TAB).append(TAB);

			// assign
			if (applyType.isCastableToMatrix())
				sbassign.append(TAB).append(outVarName).append(STRUCT_ACCESS).append("matrix")
						.append("[" + foundElements + "++] = " + indexName + "+1;").append(NL);
			else {
				// case scalar output
				sbassign.append(TAB).append(outVarName).append(" = " + indexName + "+1;").append(NL);
			}

			if (!forwardDirection) {
				// reverse results
				sbassign.append(TAB).append("{").append(NL);
				sbassign.append(TAB).append("int ").append(indexName).append(" = 0;").append(NL);
				sbassign.append(TAB).append(foundElements).append("--;").append(NL);
				sbassign.append(TAB).append("while(").append(foundElements).append(" > ").append(indexName).append("){")
						.append(NL);
				sbassign.append(TAB).append(TAB).append("int temp = ").append(outVarName).append(STRUCT_ACCESS)
						.append("matrix").append("[").append(foundElements).append("];").append(NL);
				sbassign.append(TAB).append(TAB).append(outVarName).append(STRUCT_ACCESS).append("matrix").append("[")
						.append(foundElements).append("] = ").append(outVarName).append(STRUCT_ACCESS).append("matrix")
						.append("[").append(indexName).append("];").append(NL);
				sbassign.append(TAB).append(TAB).append(outVarName).append(STRUCT_ACCESS).append("matrix").append("[")
						.append(indexName).append("] = temp;").append(NL);
				sbassign.append(TAB).append(TAB).append(foundElements).append("--;").append(NL);
				sbassign.append(TAB).append(TAB).append(indexName).append("++;").append(NL);
				sbassign.append(TAB).append("}").append(NL);
				sbassign.append(TAB).append("}").append(NL);
			}

			sbassign.append("}").append(NL);

			newComment(curApplyNode);
			if (applyType.isCastableToMatrix())
				newTranslation(curApplyNode, sb.toString() + NL + sbassign.toString());
			else
				newTranslation(curApplyNode, sbassign.toString());
			return outVarName;
		}
		case "numel":

			DimensionType dimType = (DimensionType) getExprGeneralized(params.get(0));
			if (dimType instanceof SliceType) {

				SliceType sliceType = (SliceType) dimType;

				GType typeof = sliceType.of();
				String prefix = "";
				if (typeof.equals(BType.INT)) {
					prefix = "i";
				} else {
					prefix = "d";
				}

				Triple<GType, GType, GType> slice = sliceType.slices();

				GType firstSlice = slice.first();
				String firstTypeAsString = null;
				if (firstSlice instanceof ValuedType) {
					if (((ValuedType) firstSlice).hasValue())
						firstTypeAsString = ((ValuedType) firstSlice).valueAsString();
				}

				GType secondSlice = slice.second();
				String secondTypeAsString = null;
				if (secondSlice instanceof ValuedType) {
					if (((ValuedType) secondSlice).hasValue())
						secondTypeAsString = ((ValuedType) secondSlice).valueAsString();
				}

				GType thirdSlice = slice.third();
				String thirdTypeAsString = null;
				if (thirdSlice instanceof ValuedType) {
					if (((ValuedType) thirdSlice).hasValue())
						thirdTypeAsString = ((ValuedType) thirdSlice).valueAsString();
				}

				String[] sliceParameters = new String[3];

				AASTNode colonNode = params.get(0).child(NodeType.COLON);
				if (colonNode != null) {
					List<AASTNode> nodes = colonNode.childs();

					AASTNode firstNode = nodes.get(0);
					if (firstNode.equals(NodeType.COLON)) {
						List<AASTNode> subNode = nodes.get(0).childs();
						sliceParameters[0] = subNode.get(0).name();
						sliceParameters[1] = subNode.get(1).name();
					} else {
						sliceParameters[0] = firstNode.name();
					}

					if (firstNode.equals(NodeType.COLON)) {
						sliceParameters[2] = nodes.get(1).name();
					} else {
						sliceParameters[1] = "1";
						sliceParameters[2] = nodes.get(2).name();
					}
				} else {
					String sliceName = params.get(0).name();

					sliceParameters[0] = sliceName + STRUCT_ACCESS + "start";
					sliceParameters[1] = sliceName + STRUCT_ACCESS + "step";
					sliceParameters[2] = sliceName + STRUCT_ACCESS + "end";
				}

				firstTypeAsString = (firstTypeAsString == null) ? sliceParameters[0] : firstTypeAsString;
				secondTypeAsString = (secondTypeAsString == null) ? sliceParameters[1] : secondTypeAsString;
				thirdTypeAsString = (thirdTypeAsString == null) ? sliceParameters[2] : thirdTypeAsString;

				String result = "v" + prefix + "SequenceDimension(" + firstTypeAsString + ", " + secondTypeAsString
						+ ", " + thirdTypeAsString + ")";
				return result;
			} else if (dimType instanceof MatrixType) {

				IntType[] dimensions = ((DimensionType) getExprGeneralized(params.get(0))).dims();
				List<String> matrixCompleteDimensions = nodeDimsToStr(params.get(0), dimensions, false);

				if (matrixCompleteDimensions.isEmpty()) {
					return "0";
				} else {
					String result = Strings.join(matrixCompleteDimensions, '*');
					return result;
				}
			}
		case "kron": {
			// kronecker product
			AASTNode outNode = getOutputNode(outSymbol);
			GType outSymbolType = getExprGeneralized(outSymbol);
			if (!outSymbolType.canRepresent(BType.MATRIX))
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_OUTPUT_TYPE, curApplyNode,
						fenum.getName(), outSymbolType);
			String outVarName = getNodeSymbol(outNode);
			String[] inMatricesTranslated = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			String[] inMatricesData = new String[inMatricesTranslated.length];
			List<String> inMatricesDims = new ArrayList<String>(inMatricesTranslated.length * 2);
			int i = 0;
			for (String inMatrix : inMatricesTranslated) {
				GType inMatrixType = TypeUtils.getExprGeneralized(params.get(i));
				if (!inMatrixType.canRepresent(BType.MATRIX))
					throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
							curApplyNode, fenum.getName(), new String[] { inMatrixType.toShortString() },
							new String[] { "Matrix" });

				inMatricesData[i] = inMatricesTranslated[i] + STRUCT_ACCESS + "matrix";

				IntType[] dims = ((DimensionType) inMatrixType).dims();
				int dimslen = dims.length;
				if (dimslen == 2)
					for (IntType dim : ((DimensionType) inMatrixType).dims())
						inMatricesDims.add(dim.valueAsString());
				else if (dimslen == 1) {
					inMatricesDims.add("1");
					inMatricesDims.add(dims[0].valueAsString());
				} else
					throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
							curApplyNode, fenum.getName(), new String[] { inMatrixType.toShortString() },
							new String[] { "2D Matrix" });
				i++;
			}

			int paramsize;
			StringBuffer sb = new StringBuffer();

			functionName = null;

			try {
				functionName = getCFunctionImplementationManagerInstance().getTranslation(curApplyNode,
						fenum.getName());
			} catch (Exception e) {
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_PREFERRED_AND_ALTERNATIVE_TRANSLATION,
						curApplyNode, fenum.getName());
			}

			char char_to_sub;
			if (((DimensionType) outSymbolType).of().isLessGenericThanInt()) {
				char_to_sub = 'i';
			} else {
				char_to_sub = 'd';
			}
			// int or bool
			functionName = functionName.replace('?', char_to_sub);

			sb.append(functionName).append("(");
			// inputs
			sb.append(String.join(", ", inMatricesData));
			// output
			sb.append(", ").append(outVarName).append(STRUCT_ACCESS).append("matrix, ");
			// inputs dimensions
			sb.append(String.join(", ", inMatricesDims));
			sb.append(");").append(NL);

			newComment(curApplyNode);
			newTranslation(curApplyNode, sb.toString());

			return outVarName;
		}
		case "repmat": {
			// repeat a matrix N,M,K... times along his N,M,K... dimensions
			AASTNode outNode = getOutputNode(outSymbol);
			GType outSymbolType = getExprGeneralized(outSymbol);
			String outVarName = getNodeSymbol(outNode);
			String[] inTranslated = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			StringBuffer sb = new StringBuffer();

			if (!outSymbolType.canRepresent(BType.MATRIX))
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_OUTPUT_TYPE, curApplyNode,
						fenum.getName(), outSymbolType);
			GType matType = getExprGeneralized(params.get(0));
			DimensionType dtype = (DimensionType) matType;
			if (matType.equals(BType.MATRIX_ACCESS_SLICE)) {
				// if input is a slice, convert it to a matrix first
				inTranslated[0] = castSliceTypeToMatrixType(sb, outNode, inTranslated[0], dtype);
			} else if (!matType.canRepresent(BType.MATRIX))
				throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS, curApplyNode,
						fenum.getName(), new String[] { matType.toShortString() }, new String[] { "Matrix" });

			IntType[] paramDims = dtype.dims();
			String[] inRepeatValues = new String[inTranslated.length - 1];
			for (int r = 1; r < inTranslated.length; ++r)
				inRepeatValues[r - 1] = "(int)" + inTranslated[r];
			// build out dimension
			IntType[] outDims = new IntType[paramDims.length];
			String[] inMatricesDims = new String[paramDims.length];

			String origname = null;
			for (int pd = 0; pd < paramDims.length; ++pd) {
				IntType dim = (IntType) GType.get(paramDims[pd]);
				origname = dim.valueAsString();
				if (pd < inRepeatValues.length) {
					String tmpvarname = outVarName + "_dim" + Integer.toString(pd + 1);
					// create (before ensurecapacity) the input dimension of variables
					// this allows to resolve the issue when a = repmat(a...)
					origname = "orig_" + tmpvarname;
					createVariable(GType.get(BType.INT), origname, NodeType.ID, dim.valueAsString(), false, false,
							VarVisibility.LOCAL_TO_BLOCK.context(curApplyNode), false);
					// create (before ensurecapacity) the output dimension variables
					// this allows to resolve the issue when a = repmat(a...)
					createVariable(GType.get(BType.INT), tmpvarname, NodeType.ID,
							dim.valueAsString() + " * " + inRepeatValues[pd], false, false,
							VarVisibility.LOCAL_TO_BLOCK.context(curApplyNode), false);
					dim.name(tmpvarname);
					if (dim.hasValue())
						dim.deleteValue();
				}
				inMatricesDims[pd] = origname;
				outDims[pd] = dim;
			}
			int i = 0;
			functionName = "?RepeatMatrix";
			char char_to_sub;
			if (((DimensionType) outSymbolType).of().isLessGenericThanInt()) {
				char_to_sub = 'i';
			} else {
				char_to_sub = 'd';
			}
			// int or bool
			functionName = functionName.replace('?', char_to_sub);

			// ensure output variable is allocated
			ensureMatrixMemory(sb, outNode, outDims);

			sb.append(functionName).append("(");
			// output
			sb.append(outVarName).append(STRUCT_ACCESS).append("matrix, ");
			sb.append(outVarName).append(STRUCT_ACCESS).append("poly_basis, ");
			// input matrix
			sb.append(inTranslated[0] + STRUCT_ACCESS + "matrix").append(", ");
			sb.append(inTranslated[0] + STRUCT_ACCESS + "poly_basis").append(", ");
			// number of matrix dimensions
			sb.append(Integer.toString(inMatricesDims.length)).append(", ");
			// number of repetitions
			// input matrix dimensions
			sb.append(Integer.toString(inMatricesDims.length + inRepeatValues.length)).append(", ");
			// input dims
			sb.append(String.join(", ", inMatricesDims)).append(", ");
			// repetitions
			sb.append(String.join(", ", inRepeatValues));
			sb.append(");").append(NL);

			newComment(curApplyNode);
			newTranslation(curApplyNode, sb.toString());

			return outVarName;
		}
		case "factorial":
		case "power":
		case "exp":
		case "log":
		case "log10":
		case "floor":
		case "ceil":
		case "round":
		case "sin":
		case "cos":
		case "tan":
		case "atan2":
		case "abs":
		case "sign":
			switch (params.size()) {
			case 0:
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_WITHOUT_ARGUMENT,
						curApplyNode, functionName);
			case 1:
			case 2:
				String sequenceToVectorFun = SEQUENCE_TO_MATRIX_FUN;
				first = params.get(0);
				GType param0Type = getExprGeneralized(first);
				String param0Name = first.symbol();

				// a scalar is a matrix 1x1
				if (GType.get(BType.SCALAR).equals(param0Type) || GType.get(BType.INT).equals(param0Type)) {
					String fun_str;
					try {
						fun_str = getCFunctionImplementationManagerInstance().getScalarTranslation(fenum.getName());
					} catch (Exception e) {
						throw new UndefinedTranslationException(
								CErrorMessage.UNSUPPORTED_PREFERRED_AND_ALTERNATIVE_TRANSLATION, curApplyNode);
					}
					// if it is a scalar
					return fun_str + "(" + String.join(", ", paramsTranslated.split("\\" + PARAMS_SEPARATOR)) + ")";
				} else if (param0Type != null && param0Type.isCastableToMatrix()) {
					DimensionType mtype = (DimensionType) param0Type;
					// special cases
					if ((fenum.getName().equals("ceil") || fenum.getName().equals("floor")
							|| fenum.getName().equals("round")) && mtype.of().isLessGenericThanInt()) {
						// if floor/ceil/round of int, return himself.
						return paramsTranslated;
					} else if (fenum.getName().equals("unique") && mtype.equals(BType.MATRIX_ACCESS_SLICE)) {
						// unique on slice is slice itself
						return paramsTranslated;
					}

					AASTNode outNode = getOutputNode(outSymbol, true, false);
					String outVarName = outNode.symbol();
					IntType[] paramDims = ((DimensionType) param0Type).dims();
					List<String> matrixDims = nodeDimsToStr(first, paramDims, true);

					StringBuffer sb = new StringBuffer();
					// use our implemented functions -ending with Impl-
					String mfun_str = null;
					try {
						mfun_str = getCFunctionImplementationManagerInstance().getTranslation(curApplyNode,
								fenum.getName());
					} catch (Exception e) {
						throw new UndefinedTranslationException(
								CErrorMessage.UNSUPPORTED_PREFERRED_AND_ALTERNATIVE_TRANSLATION, curApplyNode,
								fenum.getName());
					}

					char char_to_sub;
					if (mtype.of().isLessGenericThanInt()) {
						char_to_sub = 'i';
					} else {
						char_to_sub = 'd';
					}
					// int or bool
					mfun_str = mfun_str.replace('?', char_to_sub);
					sequenceToVectorFun = sequenceToVectorFun.replace('?', char_to_sub);

					if (char_to_sub != mfun_str.charAt(1)) {
						// mfun_str doesn't support operating on integers/double,
						// change function, use the alternative function
						try {
							mfun_str = getCFunctionImplementationManagerInstance()
									.getAlternativeTranslation(fenum.getName());
						} catch (Exception e) {
							throw new UndefinedTranslationException(
									CErrorMessage.UNSUPPORTED_PREFERRED_AND_ALTERNATIVE_TRANSLATION, curApplyNode);
						}
						mfun_str = mfun_str.replace('?', char_to_sub);
					}
					if (char_to_sub != mfun_str.charAt(1))
						throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
								curApplyNode, fenum.getName(), new String[] { mtype.toShortString() },
								new String[] { mtype.of().toShortString() + "[ ]" });

					// the matrix function supports execution over a sequence?
					boolean seq_supported = mfun_str.contains("!");
					boolean on_sequence = false;
					// acts on matrix or sequence?
					if (mtype.equals(BType.MATRIX))
						mfun_str = mfun_str.replace('!', 'v');
					else if (mtype.equals(BType.MATRIX_ACCESS_SLICE)) {
						mfun_str = mfun_str.replace('!', 's');
						on_sequence = true;
					} else
						throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
								curApplyNode, functionName, param0Type);

					String[] paramsTranslatedList = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
					String[] paramsTranslatedListAccess = new String[paramsTranslatedList.length];
					int i = 0;
					for (String param : paramsTranslatedList)
						paramsTranslatedListAccess[i++] = param + STRUCT_ACCESS + "matrix";

					// ensure output variable is allocated
					ensureMatrixMemory(sb, outNode, paramDims);
					String outMatrixName = outVarName + STRUCT_ACCESS + "matrix";
					String outMatrixDimSize = outVarName + STRUCT_ACCESS + "dim1";
					if (on_sequence && !seq_supported) {
						String newParamName = castSliceTypeToMatrixType(sb, outNode, param0Name, mtype);
						// then apply function
						paramsTranslatedListAccess[0] = newParamName + STRUCT_ACCESS + "matrix";
					} else if (on_sequence && seq_supported)
						// apply directly on sequence
						paramsTranslatedListAccess[0] = paramsTranslatedList[0];

					sb.append(mfun_str)
							.append("(" + String.join(" * ", matrixDims) + ", "
									+ String.join(", ", paramsTranslatedListAccess) + ", " + outMatrixName + ");")
							.append(NL);
					newComment(curApplyNode);
					newTranslation(curApplyNode, sb.toString());
					// -------- CODE -------------
					return outVarName;
				} else
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
							curApplyNode, functionName, param0Type);
			default:
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_NUMBER_OF_ARGUMENTS,
						curApplyNode, functionName, params.size());
			}
		case "mod":
			String[] parameterVariableNames = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			if (getExprGeneralized(params.get(0)).isCastableToBool()
					&& getExprGeneralized(params.get(1)).isCastableToBool())
				return parameterVariableNames[0] + " % " + parameterVariableNames[1];
			else if (getExprGeneralized(params.get(0)).isCastableToScalar()
					&& getExprGeneralized(params.get(1)).isCastableToScalar())
				return "fmod(" + parameterVariableNames[0] + ", " + parameterVariableNames[1] + ")";
		case "randi":
		case "rand":
		case "randn": {
			String from = "0.0";
			String to = "1.0";
			AASTNode outNode = getOutputNode(outSymbol);
			GType outSymbolType = getExprGeneralized(outSymbol);
			String outVarName = getNodeSymbol(outNode);
			String[] dims;
			int paramsize;
			String outmatrixin, outmatrixout;
			StringBuffer sb = new StringBuffer();
			String randfunctionName = null;

			try {
				randfunctionName = getCFunctionImplementationManagerInstance().getTranslation(curApplyNode,
						fenum.getName());
			} catch (Exception e) {
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_PREFERRED_AND_ALTERNATIVE_TRANSLATION,
						curApplyNode, fenum.getName());
			}

			if (paramsTranslated != null) {
				GType firstParamType = getExprGeneralized(params.get(0));

				dims = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
				if (functionName.equals("randi")) {
					if (firstParamType.isCastableToScalar()) {
						// randi generates in interval 1:dims[0]
						from = "1";
						to = "(int) " + dims[0];
					} else if (firstParamType.isCastableToMatrix()) {
						// read first two elements of matrix
						from = "(int) " + dims[0] + STRUCT_ACCESS + "matrix[0]";
						to = "(int) " + dims[0] + STRUCT_ACCESS + "matrix[1]";
					} else
						throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
								curApplyNode, functionName);
					if (dims.length > 1) {
						String[] tmpdims = null;
						if (dims.length == 2) {
							tmpdims = new String[dims.length];
							// dims[1] appears twice, because randi works like this
							tmpdims[0] = dims[1];
							tmpdims[1] = dims[1];
						} else {
							tmpdims = new String[dims.length - 1];
							for (int i = 1; i < dims.length; ++i)
								tmpdims[i - 1] = dims[i];
						}
						// remove first element, that is the to of the range
						dims = tmpdims;
					} else
						dims = new String[] { "1" };
				}

				if (dims.length == 1)
					// nxn matrix if just 1 dimension given
					dims = new String[] { dims[0], dims[0] };

				paramsize = dims.length;
				if (outSymbolType != null && outSymbolType.isCastableToScalar()) {
					outmatrixin = "&" + outVarName;
					outmatrixout = null;
				} else {
					outmatrixin = outmatrixout = outVarName + STRUCT_ACCESS + "matrix";
					// ensure matrix memory has to be called before
					ensureMatrixMemory(sb, outNode, dims);
				}

			} else {
				// generate a single random number
				dims = new String[] { "1" };
				paramsize = 1;
				randfunctionName = "*" + randfunctionName;
				outmatrixin = "&" + outVarName;
				outmatrixout = outVarName;
			}

			// force cast to int of dims
			for (int d = 0; d < paramsize; ++d)
				dims[d] = "(int) " + dims[d];

			if (outmatrixout != null)
				sb.append(outmatrixout).append(" = ");
			sb.append(randfunctionName).append("(" + outmatrixin + ", " + from + ", " + to + ", " + paramsize + ", "
					+ String.join(", ", dims) + ")").append(";").append(NL);

			initializeRandomNumberGenerator();
			newComment(curApplyNode);
			newTranslation(curApplyNode, sb.toString());

			return outVarName;
		}
		case "ismember": {
			AASTNode outNode = getOutputNode(outSymbol);
			GType outSymbolType = getExprGeneralized(outSymbol);
			String outVarName = getNodeSymbol(outNode);
			String[] dims;
			int paramsize;
			String outmatrixin, outmatrixout;
			StringBuffer sb = new StringBuffer();
			String containedFunctionName = null;

			switch (params.size()) {
			case 0:
			case 1:
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_WITHOUT_ARGUMENT,
						curApplyNode, functionName, paramsTranslated);
			case 2:
				first = getIDNode(params.get(0));
				GType param0Type = getExprGeneralized(first);

				AASTNode toBeSearchedIn = getIDNode(params.get(1));
				DimensionType toBeSearchedInType = (DimensionType) getExprGeneralized(toBeSearchedIn);

				boolean toBeSearchedInIsSlice = toBeSearchedInType instanceof SliceType;

				GType toBeSearchedInBaseType = toBeSearchedInType.of();
				List<String> toBeSearchedInDims = nodeDimsToStr(toBeSearchedIn, toBeSearchedInType.dims(), true);
				String toBeSearchedInFlattenedDims = Strings.join(toBeSearchedInDims, '*');

				String typeSuffix;
				// case first parameter scalar and second matrix -of the same type of the the
				// matrix I have to search in-
				if ((param0Type.isCastableToScalar()) && toBeSearchedInBaseType.equals(param0Type)) {
					// if it is a scalar
					typeSuffix = "i";
					if (GType.get(BType.SCALAR).equals(param0Type)) {
						typeSuffix = "d";
					}

					// case slice
					if (toBeSearchedInIsSlice) {
						SliceType toBeSearchedInSlice = (SliceType) toBeSearchedInType;

						String sliceFirst = ((ValuedType) toBeSearchedInSlice.slices().first()).valueAsString();
						String sliceSecond = ((ValuedType) toBeSearchedInSlice.slices().second()).valueAsString();
						String sliceThird = ((ValuedType) toBeSearchedInSlice.slices().third()).valueAsString();

						sb.append(outVarName).append(" = ");
						sb.append(typeSuffix).append("ContainedInSlice(");
						sb.append(sliceFirst).append(",");
						sb.append(sliceSecond).append(",");
						sb.append(sliceThird).append(",");
						sb.append(first.symbol()).append(");").append(NL);

						// case matrix
					} else {
						String toBeSearchedInName = toBeSearchedIn.symbol();
						String toBeSearchedInMatrixName = toBeSearchedInName + STRUCT_ACCESS + "matrix";

						sb.append(outVarName).append(" = ");
						sb.append(typeSuffix).append("ContainedInArray(").append(toBeSearchedInFlattenedDims)
								.append(",");
						sb.append(toBeSearchedInMatrixName).append(",").append(first.name()).append(");").append(NL);
					}

					break;

					// case first parameter matrix and second matrix too -of the same type of the
					// the matrix I have to search in-
				} else if ((param0Type != null && param0Type.isCastableToMatrix())
						&& toBeSearchedInBaseType.equals(((DimensionType) param0Type).of())) {
					DimensionType firstType = (DimensionType) getExprGeneralized(first);
					List<String> firstDims = nodeDimsToStr(first, firstType.dims(), true);
					String firstFlattenedDimensions = Strings.join(firstDims, '*');

					boolean inputIsSlice = firstType instanceof SliceType;

					typeSuffix = "i";
					// check if it's the case of int or scalar
					if (firstType.of().equals(BType.SCALAR)) {
						typeSuffix = "d";
					}

					// first of all we should ensure the output matrix memory
					ensureMatrixMemory(sb, outNode, firstType.dims());
					String outMatrixName = outVarName + STRUCT_ACCESS + "matrix";

					// then invoke the the vectorial version of the function
					if (inputIsSlice) {
						sb.append("s");
					} else {
						sb.append("v");
					}
					sb.append(typeSuffix).append("ContainedIn");
					if (toBeSearchedInIsSlice) {
						sb.append("Slice");
					} else {
						sb.append("Array");
					}
					sb.append("(");
					if (toBeSearchedInIsSlice) {
						SliceType toBeSearchedInSlice = (SliceType) toBeSearchedInType;

						String sliceFirst = ((ValuedType) toBeSearchedInSlice.slices().first()).valueAsString();
						String sliceSecond = ((ValuedType) toBeSearchedInSlice.slices().second()).valueAsString();
						String sliceThird = ((ValuedType) toBeSearchedInSlice.slices().third()).valueAsString();

						sb.append(sliceFirst).append(",");
						sb.append(sliceSecond).append(",");
						sb.append(sliceThird).append(",");
					} else {
						String toBeSearchedInName = toBeSearchedIn.symbol();
						String toBeSearchedInMatrixName = toBeSearchedInName + STRUCT_ACCESS + "matrix";

						sb.append(toBeSearchedInFlattenedDims).append(",").append(toBeSearchedInMatrixName).append(",");
					}

					if (inputIsSlice) {
						SliceType inputTypeAsSlice = (SliceType) firstType;

						String sliceFirst = ((ValuedType) inputTypeAsSlice.slices().first()).valueAsString();
						String sliceSecond = ((ValuedType) inputTypeAsSlice.slices().second()).valueAsString();
						String sliceThird = ((ValuedType) inputTypeAsSlice.slices().third()).valueAsString();

						sb.append(sliceFirst).append(",");
						sb.append(sliceSecond).append(",");
						sb.append(sliceThird).append(",");
					} else {
						String firstMatrixName = first.symbol() + STRUCT_ACCESS + "matrix";

						sb.append(firstFlattenedDimensions).append(",").append(firstMatrixName).append(",");
					}

					sb.append(outMatrixName).append(");").append(NL);
					break;
				} else {
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
							curApplyNode, functionName,
							String.join(", ", paramsTranslated.split("\\" + PARAMS_SEPARATOR)));
				}
			default:
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_NUMBER_OF_ARGUMENTS,
						curApplyNode, functionName);
			}

			newComment(curApplyNode);
			newTranslation(curApplyNode, sb.toString());

			return outVarName;
		}
		case "triu":
		case "tril": {
			AASTNode outNode = getOutputNode(outSymbol);
			String outVarName = getNodeSymbol(outNode);

			if (params.size() < 1 || params.size() > 2)
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, functionName, "expected 2D matrix");
			GType type = getExprGeneralized(params.get(0));
			if (!type.isCastableToMatrix())
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, functionName, "expected 2D matrix");
			if (type.isCastableToSlice())
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, functionName, "slice not supported");

			DimensionType inSymbolType = (DimensionType) type;
			IntType[] inDims = inSymbolType.dims();
			if (inDims.length != 2)
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, functionName, "expected 2D matrix");

			IntType row = inDims[0];
			IntType col = inDims[1];
			StringBuffer sb = new StringBuffer();
			String fromDiagonal = null;
			int intFromDiagonal = -1;
			String inVarName = null;
			if (params.size() > 1) {
				String[] arrParamsTranslated = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
				inVarName = arrParamsTranslated[0];
				fromDiagonal = arrParamsTranslated[1];
				try {
					intFromDiagonal = Integer.parseInt(fromDiagonal);
				} catch (Exception e) {
				}
			} else {
				inVarName = paramsTranslated;
				intFromDiagonal = 0;
			}
			String idx = outVarName + "_idx";
			createVariable(GType.get(BType.INT), idx, NodeType.ID, "0", false, false,
					VarVisibility.LOCAL_TO_BLOCK.context(curApplyNode), false);

			if (row.hasIntValue() && col.hasIntValue() && intFromDiagonal >= 0) {
				// static
				int rowvalue = row.valueAsInt();
				int colvalue = col.valueAsInt();
				for (int i = 1; i <= rowvalue; ++i)
					if (functionName.equals("triu"))
						for (int j = i + intFromDiagonal; j <= colvalue; ++j) {
							sb.append(idx + " = matrixAccess(" + inVarName + STRUCT_ACCESS + "poly_basis, 2, 2, "
									+ rowvalue + ", " + colvalue + ", " + Integer.toString(i) + ", "
									+ Integer.toString(j) + ");").append(NL);
							sb.append(outVarName + STRUCT_ACCESS + "matrix[" + idx + "] = ");
							sb.append(inVarName + STRUCT_ACCESS + "matrix[" + idx + "];").append(NL);
						}
					else
						// tril
						for (int j = 1; j <= i - intFromDiagonal; ++j) {
							sb.append(idx + " = matrixAccess(" + inVarName + STRUCT_ACCESS + "poly_basis, 2, 2, "
									+ rowvalue + ", " + colvalue + ", " + Integer.toString(i) + ", "
									+ Integer.toString(j) + ");").append(NL);
							sb.append(outVarName + STRUCT_ACCESS + "matrix[" + idx + "] = ");
							sb.append(inVarName + STRUCT_ACCESS + "matrix[" + idx + "];").append(NL);
						}
			} else {
				List<String> dimsStr = nodeDimsToStr(outNode, inDims, true, true);
				// calloc = false (last parameter)
				ensureMatrixMemory(sb, outNode, outVarName, (MatrixType) inSymbolType,
						dimsStr.toArray(new String[dimsStr.size()]), false, false, false, null);

				String i = outVarName + "_i";
				String j = outVarName + "_j";
				// do it dynamically
				sb.append("for (int " + i + " = 1 ; " + i + " <= " + row.valueAsString() + "; ++" + i + ")").append(NL);
				if (functionName.equals("triu"))
					if (fromDiagonal != null)
						sb.append("for (int " + j + " = " + i + " + " + fromDiagonal + "; " + j + " <= "
								+ col.valueAsString() + "; ++" + j + "){").append(NL);
					else
						sb.append("for (int " + j + " = " + i + " ; " + j + " <= " + col.valueAsString() + "; ++" + j
								+ "){").append(NL);
				else {
					// tril
					if (fromDiagonal != null)
						sb.append(
								"for (int " + j + " = 1; " + j + " <= " + i + " - " + fromDiagonal + "; ++" + j + "){")
								.append(NL);
					else
						sb.append("for (int " + j + " = 1; " + j + " <= " + i + "; ++" + j + "){").append(NL);
				}
				sb.append(TAB)
						.append(idx + " = matrixAccess(" + inVarName + STRUCT_ACCESS + "poly_basis, 2, 2, "
								+ row.valueAsString() + ", " + col.valueAsString() + ", " + i + ", " + j + ");")
						.append(NL);
				sb.append(TAB).append(outVarName + STRUCT_ACCESS + "matrix[" + idx + "] = ");
				sb.append(inVarName + STRUCT_ACCESS + "matrix[" + idx + "];").append(NL);

				sb.append("}").append(NL);
			}

			newComment(curApplyNode);
			newTranslation(curApplyNode, sb.toString());
			return outVarName;
		}
		case "norm": {
			AASTNode outNode = getOutputNode(outSymbol);
			String outVarName = getNodeSymbol(outNode);
			GType inType = getExprGeneralized(params.get(0));
			GType outType = getExprGeneralized(outNode);
			String[] arrParamsTranslated = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			StringBuffer sb = new StringBuffer();
			String mklfunName;
			DimensionType inSymbolType = (DimensionType) inType;
			if (inType.isCastableToScalar()) {
				return paramsTranslated;
			} else if (inType.isCastableToMatrix()) {
				if (inType.isCastableToSlice())
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
							curApplyNode, functionName, "expected 1D or 2D matrix");
				MatrixType dtype = (MatrixType) inType;
				IntType[] realdims = TypeUtils.realDims(dtype.dims());

				if (realdims.length == 1) {
					// vector norm
					String acc = outVarName + "_acc";
					String i = outVarName + "_i";
					createVariable(inSymbolType.of(), acc, NodeType.ID, "0", false, false,
							VarVisibility.LOCAL_TO_BLOCK.context(curApplyNode), false);
					sb.append("#pragma omp smid").append(NL);
					sb.append("for(int " + i + "=0; " + i + " < " + realdims[0].valueAsString() + "; ++" + i + ")")
							.append(NL);
					sb.append(TAB).append(acc).append(" += ")
							.append("pow(" + arrParamsTranslated[0] + STRUCT_ACCESS + "matrix[" + i + "], 2);")
							.append(NL);
					sb.append(outVarName).append(" = ").append("sqrt(").append(acc).append(");").append(NL);
				} else
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
							curApplyNode, functionName, "expected 1D or 2D matrix");
			} else {
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, functionName, "expected 1D or 2D matrix");
			}
			newComment(curApplyNode);
			newTranslation(curApplyNode, sb.toString());
			return outVarName;
		}
		case "diag": {
			AASTNode outNode = getOutputNode(outSymbol);
			String outVarName = getNodeSymbol(outNode);
			if (params.size() != 1)
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, functionName, "expected 2D matrix");
			GType type = getExprGeneralized(params.get(0));
			if (!type.isCastableToMatrix())
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, functionName, "expected 2D matrix");
			DimensionType inSymbolType = (DimensionType) type;
			IntType[] inDims = inSymbolType.dims();
			if (inDims.length != 2)
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, functionName, "expected 2D matrix");
			IntType row = inDims[0];
			IntType col = inDims[1];
			String inVarName = paramsTranslated;
			StringBuffer sb = new StringBuffer();

			boolean oneDinput = row.hasValue() && row.valueAsInt() == 1 || col.hasValue() && col.valueAsInt() == 1;

			if (oneDinput) {
				// 1d input, generate 2d output
				// TODO: implement static case as in 2d input case
				MatrixType outType = (MatrixType) getExprGeneralized(outNode);
				// dynamic
				String min;
				String i = outVarName + "_i";
				if (row.hasValue())
					min = col.valueAsString();
				else
					min = row.valueAsString();
				// it's an rowxrow matrix
				String[] dimsStr = new String[] { row.valueAsString(), row.valueAsString() };
				// calloc = true! (last parameter)
				ensureMatrixMemory(sb, outNode, outVarName, outType, dimsStr, false, true, false, null);
				sb.append("#pragma omp smid").append(NL);
				sb.append("for (int " + i + " = 1; " + i + " <= " + min + "; ++" + i + ")").append(NL);
				sb.append(TAB).append(outVarName + STRUCT_ACCESS + "matrix[matrixAccess(" + outVarName + STRUCT_ACCESS
						+ "poly_basis, 2, 2, " + min + ", " + min + ", " + i + ", " + i + ")] = ");
				sb.append(TAB).append(inVarName + STRUCT_ACCESS + "matrix[" + i + "-1];").append(NL);

			} else {
				// 2d input, genrrate 1d output
				if (row.hasIntValue() && col.hasIntValue()) {
					// static
					int rowvalue = row.valueAsInt();
					int colvalue = col.valueAsInt();
					int min = Math.min(rowvalue, colvalue);
					for (int i = 0; i < min; ++i) {
						sb.append(outVarName + STRUCT_ACCESS + "matrix[" + i + "] = ");
						sb.append(inVarName + STRUCT_ACCESS + "matrix[matrixAccess(" + inVarName + STRUCT_ACCESS
								+ "poly_basis, 2, 2, " + rowvalue + ", " + colvalue + ", " + Integer.toString(i + 1)
								+ ", " + Integer.toString(i + 1) + ")];").append(NL);
					}
				} else {
					// dynamic
					MatrixType outType = (MatrixType) getExprGeneralized(outNode);
					String min = outVarName + "_len";
					String i = outVarName + "_i";
					createVariable(GType.get(BType.INT), min, NodeType.ID,
							"min(" + row.valueAsString() + ", " + col.valueAsString() + ")", false, false,
							VarVisibility.LOCAL_TO_BLOCK.context(curApplyNode), false);
					// result is rowx1
					String[] dimsStr = new String[] { row.valueAsString(), "1" };
					// calloc = true! (last parameter)
					ensureMatrixMemory(sb, outNode, outVarName, outType, dimsStr, false, true, false, null);
					sb.append("for (int " + i + " = 1; " + i + " <= " + min + "; ++" + i + ")").append(NL);
					sb.append(TAB).append(outVarName + STRUCT_ACCESS + "matrix[" + i + "-1] = ");
					sb.append(TAB)
							.append(inVarName + STRUCT_ACCESS + "matrix[matrixAccess(" + inVarName + STRUCT_ACCESS
									+ "poly_basis, 2, 2, " + row.valueAsString() + ", " + col.valueAsString() + ", " + i
									+ ", " + i + ")];")
							.append(NL);
				}
			}
			newComment(curApplyNode);
			newTranslation(curApplyNode, sb.toString());
			return outVarName;
		}
		case "reshape": {
			AASTNode outNode = getOutputNode(outSymbol);
			DimensionType outSymbolType = (DimensionType) getExprGeneralized(outSymbol);
			IntType[] outDims = outSymbolType.dims();
			String outVarName = getNodeSymbol(outNode);
			String[] dims;
			int paramsize;
			String outmatrixin, outmatrixout;
			StringBuffer sb = new StringBuffer();
			String containedFunctionName = null;

			switch (params.size()) {
			case 0:
			case 1:
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_WITHOUT_ARGUMENT,
						curApplyNode, functionName);
			// just more than one parameter case is managed
			default:
				first = getIDNode(params.get(0));
				DimensionType inputDimensionType = (DimensionType) getExprGeneralized(first);

				boolean isSlice = inputDimensionType instanceof SliceType;

				IntType[] inputMatrixDimensions = inputDimensionType.dims();

				String outputMatrixName = outVarName + STRUCT_ACCESS + "matrix";

				String prefix = "i";
				if (inputDimensionType.of().equals(BType.SCALAR)) {
					prefix = "d";
				}
				// dimension of output matrix as prescribed by inputs
				String[] inputParamDimsAsString = new String[outDims.length];
				String[] arrParamsTranslated = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
				for (int i = 1; i < outDims.length + 1; i++) {
					AASTNode paramNode = getIDNode(params.get(i));
					GType paramType = getExprGeneralized(paramNode);
					if (paramType.isCastableToScalar()) {
						inputParamDimsAsString[i - 1] = "(int) " + arrParamsTranslated[i];
					} else if (paramType instanceof DimensionType
							&& TypeUtils.isScalarMatrix(((DimensionType) paramType).dims())) {
						// get the access to the parameter
						inputParamDimsAsString[i - 1] = "(int) " + paramNode.symbol() + STRUCT_ACCESS + "matrix[0]";
					}
				}
				// dimensions of input matrix BEFORE
				String[] inputDimensionAsString = new String[inputMatrixDimensions.length];
				String[] symbolicOutputDimensionAsString = new String[inputMatrixDimensions.length];
				String[] origInputDimsAsString = new String[inputMatrixDimensions.length];
				for (int i = 0; i < inputMatrixDimensions.length; i++) {
					String curDim = inputMatrixDimensions[i].valueAsString();
					inputDimensionAsString[i] = curDim;
					symbolicOutputDimensionAsString[i] = TypeUtils.matrixDimName(outVarName, i + 1);
					String initialDimSymbol = first.symbol() + "_dim_before_" + i;
					createVariable(GType.get(BType.INT), initialDimSymbol, NodeType.ID, curDim, false, false,
							VarVisibility.LOCAL_TO_BLOCK.context(curApplyNode), sb, false);
					origInputDimsAsString[i] = initialDimSymbol;
				}

				// ensure the matrix memory
				// dimension of output matrix AFTER
				boolean allocated = ensureMatrixMemory(sb, outNode, inputParamDimsAsString);

				// call the C functions that reshape the vector
				if (!isSlice) {
					String inputMatrixName = first.symbol() + STRUCT_ACCESS + "matrix";

					if (((MatrixType) inputDimensionType).isSparse())
						throw new TypeException(CErrorMessage.UNSUPPORTED_SPARSE_MATRIX_OPERATION, curApplyNode,
								"reshape");
					// if allocated with ensureMemory and in/out are the same, avoid calling an
					// useless function
					// since ensureMemory already reshapes the input matrix.
					else {
						sb.append("v").append(prefix).append("Reshape(").append(inputMatrixName);
						sb.append(",").append(outputMatrixName).append(",");
						sb.append(inputMatrixDimensions.length).append(",");
						sb.append(outDims.length + inputMatrixDimensions.length).append(",");
						sb.append(String.join(", ", origInputDimsAsString)).append(",");
						sb.append(String.join(", ", inputParamDimsAsString));
						sb.append(");").append(NL);
					}

				} else {
					SliceType inputAsSlice = (SliceType) inputDimensionType;
					String sliceFirst = ((ValuedType) inputAsSlice.slices().first()).valueAsString();
					String sliceSecond = ((ValuedType) inputAsSlice.slices().second()).valueAsString();
					String sliceThird = ((ValuedType) inputAsSlice.slices().third()).valueAsString();

					sb.append("s").append(prefix).append("Reshape(");
					sb.append(outputMatrixName).append(",");
					sb.append(sliceFirst).append(",");
					sb.append(sliceThird).append(",");
					sb.append(sliceSecond).append(",");
					sb.append(outDims.length).append(",");
					sb.append(String.join(", ", inputParamDimsAsString));
					sb.append(");").append(NL);
				}
				// actually swap dimensions
				for (int d = 0; d < inputParamDimsAsString.length; ++d)
					sb.append(symbolicOutputDimensionAsString[d]).append(" = ").append(inputParamDimsAsString[d])
							.append(";").append(NL);

				break;
			}

			newComment(curApplyNode);
			newTranslation(curApplyNode, sb.toString());

			return outVarName;
		}
		case "cumsum": {
			AASTNode outNode = getOutputNode(outSymbol);
			DimensionType outSymbolType = (DimensionType) getExprGeneralized(outSymbol);
			IntType[] outDims = outSymbolType.dims();
			String outVarName = getNodeSymbol(outNode);
			String[] dims;
			int paramsize;
			String outmatrixin, outmatrixout;
			StringBuffer sb = new StringBuffer();
			String containedFunctionName = null;

			switch (params.size()) {
			case 0:
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_WITHOUT_ARGUMENT,
						curApplyNode, functionName);
			// just one
			case 1:
				first = getIDNode(params.get(0));
				DimensionType inputSliceType = (DimensionType) getExprGeneralized(first);

				boolean isSlice = inputSliceType instanceof SliceType;

				IntType[] inputMatrixDimensions = inputSliceType.dims();

				String outputMatrixName = outVarName + STRUCT_ACCESS + "matrix";

				String prefix = "i";
				if (inputSliceType.of().equals(BType.SCALAR)) {
					prefix = "d";
				}

				// ensure the matrix memory
				ensureMatrixMemory(sb, outNode, inputMatrixDimensions);

				List<String> inputDimensionAsString = new ArrayList<String>();
				for (int i = 0; i < inputMatrixDimensions.length; i++) {
					inputDimensionAsString.add(inputMatrixDimensions[i].valueAsString());
				}

				// call the C functions that cumulatively sum the matrix
				if (!isSlice) {
					String inputMatrixName = first.symbol() + STRUCT_ACCESS + "matrix";
					sb.append("v").append(prefix).append("CumSum(").append(inputMatrixName).append(",");
					sb.append(outputMatrixName).append(",");
					sb.append(inputMatrixDimensions.length).append(",");
					sb.append(Strings.join(inputDimensionAsString, ','));
					sb.append(");").append(NL);
				} else {
					SliceType inputAsSlice = (SliceType) inputSliceType;
					String sliceFirst = ((ValuedType) inputAsSlice.slices().first()).valueAsString();
					String sliceSecond = ((ValuedType) inputAsSlice.slices().second()).valueAsString();
					String sliceThird = ((ValuedType) inputAsSlice.slices().third()).valueAsString();

					sb.append("s").append(prefix).append("CumSum(");
					sb.append(outputMatrixName).append(",");
					sb.append(sliceFirst).append(",");
					sb.append(sliceThird).append(",");
					sb.append(sliceSecond);
					sb.append(");").append(NL);
				}

				break;
			default:
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, functionName);
			}

			newComment(curApplyNode);
			newTranslation(curApplyNode, sb.toString());

			return outVarName;
		}
		case "logspace":
		case "linspace": {
			boolean log = false;
			if (functionName.contains("log"))
				log = true;

			StringBuffer sb = new StringBuffer();
			String[] space_specs = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			String start, stop, n_points;
			if (space_specs.length >= 2) {
				start = space_specs[0];
				stop = space_specs[1];
				if (space_specs.length >= 3)
					n_points = space_specs[2];
				else
					n_points = "100";
			} else
				throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS, curApplyNode,
						(log) ? "logspace" : "linspace", space_specs, new String[] { "start", "end", "n_points" });

			AASTNode outNode = getOutputNode(outSymbol);
			String outNodeName = outNode.symbol();
			GType outType = getExprGeneralized(outNode);
			if (outType instanceof SliceType) {
				String step;
				SliceType stype = (SliceType) outType;
				String fakeStart = start;
				if (((ValuedType) stype.slices().second()).hasValue())
					step = ((ValuedType) stype.slices().second()).valueAsString();
				else {
					String op = "-";
					if (start.startsWith("-")) {
						// avoid -- in case start is negative
						fakeStart = start.substring(1);
						op = "+";
					}

					step = "(" + stop + op + fakeStart + ")/((double)" + n_points + "-1)";
				}
				newComment(curApplyNode);
				newTranslation(curApplyNode, sliceInit(outNodeName, stype, start, step, stop, null, !log));
			} else {
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_OUTPUT_TYPE, curApplyNode, functionName,
						outType);
			}
			return outSymbol.symbol();
		}
		case "false":
		case "true":
		case "eye":
		case "ones":
		case "nan":
		case "zeros": {
			String num = null;
			String funname = fenum.getName().toLowerCase();
			boolean canMemset = false;
			if ("ones".equals(funname) || "eye".equals(funname) || "true".equals(funname))
				num = "1";
			else if ("zeros".equals(funname) || "false".equals(funname)) {
				num = "0";
				canMemset = true;
			} else if ("nan".equals(funname))
				num = "NAN";
			if (params == null || params.isEmpty()) {
				return num;
			} else {
				int elems = 1;
				List<Integer> singleElems = new LinkedList<Integer>();
				boolean dynamic = false;
				String[] origArrParamsTranslated = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
				List<String> newArrParamsTranslated = new ArrayList<>(origArrParamsTranslated.length);
				int pnumber = 0;
				StringBuffer sb = new StringBuffer();
				for (AASTNode param : params) {
					GType pexpr = getExprGeneralized(param);

					if (pexpr == null)
						throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
								curApplyNode, functionName, "non-scalar");
					else if (pexpr.isCastableToScalar()) {
						ValuedType pvalue = ((ValuedType) pexpr);
						if (pvalue.hasIntValue()) {
							elems *= pvalue.valueAsInt();
							singleElems.add(pvalue.valueAsInt());
						} else
							dynamic = true;

						if (!param.childExists(TypeUtils.expressionNodes()))
							newArrParamsTranslated.add(origArrParamsTranslated[pnumber]);
						else {
							// create a variable to store the result of computation to save time
							AASTNode paramID = TypeUtils.getIDNode(param);
							createVariable(paramID);
							// the assignment should be here not at definition time.
							newTranslation(curApplyNode,
									paramID.symbol() + " = " + origArrParamsTranslated[pnumber] + ";" + NL);
							newArrParamsTranslated.add(param.symbol());
						}
					} else if (pexpr.isCastableToMatrix()) {
						// in this case we use the matrix values as dimensions
						DimensionType dtype = (DimensionType) pexpr;
						boolean matrixParamDimsKnown = true;
						int matrixParamDims = 1;
						String[] matrixParamDimsStr = new String[dtype.dims().length];
						int mdimnum = 0;
						for (IntType dim : dtype.dims()) {
							if (dim.hasIntValue())
								matrixParamDims *= dim.valueAsInt();
							else
								matrixParamDimsKnown = false;
							matrixParamDimsStr[mdimnum++] = dim.valueAsString();
						}

						dynamic = true;
						// don't print out more than 100 equal statements or the compiler will become
						// extremely slow
						if (matrixParamDimsKnown && matrixParamDims < 100) {
							for (int i = 0; i < matrixParamDims; ++i)
								newArrParamsTranslated
										.add(origArrParamsTranslated[pnumber] + STRUCT_ACCESS + "matrix[" + i + "]");
						} else {
							// add code to buffer to compute it at runtime and store result in out
							String out = "_" + param.name() + "_" + funname + "_n";
							createVariable(GType.get(BType.INT, 1), out, null, "1", false,
									VarVisibility.LOCAL_TO_BLOCK.context(curApplyNode));
							String i = "_" + param.name() + "_" + funname + "_i";
							sb.append("#pragma omp smid").append(NL);
							sb.append("for(int " + i + " = 0; " + i + " < ")
									.append(String.join(" * ", matrixParamDimsStr)).append("; ++").append(i).append(")")
									.append(NL);
							sb.append(TAB).append(out).append(" *= ")
									.append(origArrParamsTranslated[pnumber] + STRUCT_ACCESS + "matrix[" + i + "]")
									.append(";").append(NL);
							// at runtime out will contain the number of elements of the new funname matrix
							newArrParamsTranslated.add(out);
						}

					} else if (pexpr.equals(BType.TYPE)) {
						// skip type: used in returntypeupdater of frontend to define the output type of
						// this function call
						// we don't care
					} else
						throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
								curApplyNode, functionName, "non-scalar");

					pnumber++;
				}

				AASTNode outNode = getOutputNode(outSymbol);
				MatrixType outType = (MatrixType) getExprGeneralized(outNode);
				String outMatrixName = getNodeSymbol(outNode);
				if (outNode.parentExists(NodeType.LHS))
					// mark this and subsequent nodes as "preallocated"
					// this allows to skip the call to vdcheckCapacity
					outNode.parent(NodeType.STATEMENT_LIST).propagateAttribute(CAttr.PREALLOCATED, true, outMatrixName);

				// at least one dimension
				if (newArrParamsTranslated.isEmpty()) {
					newArrParamsTranslated.add("1");
					singleElems.add(1);
				} else if (params.size() == 1 && newArrParamsTranslated.size() == 1) {
					// that's because ones(3) is a 3x3 matrix
					newArrParamsTranslated.add(newArrParamsTranslated.get(0));
					if (singleElems.size() > 0) {
						singleElems.add(singleElems.get(0));
						elems *= elems;
					}
				}

				if (outType.dims().length > newArrParamsTranslated.size()) {
					// add extra 1 to match outType
					int n = outType.dims().length - newArrParamsTranslated.size();
					for (int k = 0; k < n; ++k)
						newArrParamsTranslated.add("1");
				} else if (outType.dims().length < newArrParamsTranslated.size()) {
					// remove useless ones
					List<String> reducedArrParamsTranslated = new ArrayList<String>(newArrParamsTranslated.size());
					for (String param : newArrParamsTranslated)
						if (!param.equals("1"))
							reducedArrParamsTranslated.add(param);
					newArrParamsTranslated = reducedArrParamsTranslated;
				}

				String[] arrParamsTranslated = newArrParamsTranslated
						.toArray(new String[newArrParamsTranslated.size()]);
				// do ensure even if !dynamic, because this zeros can be decidable at compile
				// time
				// but the LHS may be not
				// ex.
				// a = zeros(100)
				// while(i < x*2) a = a * x;
				ensureMatrixMemory(sb, outNode, null, outType, arrParamsTranslated, false, false, canMemset, null);
				// don't generate more than 100 different statements or the compiler will
				// complain
				if (!dynamic && elems < 100) {
					if (curApplyNode.parent().type().equals(NodeType.VECTOR)) {
						String[] strElems = new String[elems];
						for (int i = 0; i < elems; ++i)
							strElems[i] = num;
						// if inside a vector generate only the list of numbers
						return String.join(", ", strElems);
					} else {
						// generate C initialization vector
						// int a[N] = {1,1,1,1,1,....}

						if (funname.equals("eye")) {
							if (singleElems.size() == 2) {
								Integer row = singleElems.get(0);
								Integer col = singleElems.get(1);
								int k = 0;
								for (int i = 0; i < row; ++i)
									for (int j = 0; j < col; ++j)
										if (i == j)
											sb.append(outNode.symbol() + STRUCT_ACCESS + "matrix[" + k++ + "] = " + num
													+ "; ");
										else
											sb.append(outNode.symbol() + STRUCT_ACCESS + "matrix[" + k++ + "] = 0; ");
							} else
								throw new UndefinedTranslationException(
										CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT, curApplyNode,
										functionName, "maximum number of parameters is 2");
						} else {
							for (int i = 0; i < elems; ++i)
								sb.append(outNode.symbol() + STRUCT_ACCESS + "matrix[" + i + "] = " + num + "; ");
						}
						newComment(outSymbol);
						newTranslation(curApplyNode, sb.toString());

						// if it's function output you don't have to create the variable, neither to
						// allocate it
						return outNode.symbol();
					}
				} else {
					// split paramsTranslated and create integer for each one of
					// them
					// update int type with name of that integer.
					IntType[] outDims = outType.dims();
					if (outDims.length != arrParamsTranslated.length)
						throw new TypeException(CErrorMessage.INTERNAL_OUTPUT_MATRIX_DIMENSIONS, curApplyNode,
								functionName, outDims.length, params.size());

					GType ofType = outType.of();

					String[] access_params = new String[2 + outDims.length * 2];
					int apindex = 0;
					access_params[apindex++] = Integer.toString(outDims.length);
					access_params[apindex++] = Integer.toString(outDims.length);
					// add dimensions
					for (int w = 0; w < outDims.length; ++w) {
						access_params[apindex++] = outDims[w].valueAsString();
					}

					String totalDimensionString = String.join("*", arrParamsTranslated);

					// allocate output matrix
					String ofTypeStr = symbolType2Ctype(ofType);

					newComment(outSymbol);

					if (funname.equals("eye")) {
						if (arrParamsTranslated.length == 2) {
							String row = arrParamsTranslated[0];
							String col = arrParamsTranslated[1];
							String k = outMatrixName + "_k";
							createVariable(GType.get(BType.INT), k, NodeType.ID, "0", false, false,
									VarVisibility.LOCAL_TO_BLOCK.context(curApplyNode), false);
							sb.append("for (int i = 0; i < ").append(row).append("; ++i)").append(NL);
							sb.append(TAB).append("for (int j = 0; j < ").append(col).append("; ++j)").append(NL);
							sb.append(TAB).append(TAB).append("if (i == j)").append(NL);
							sb.append(TAB).append(TAB).append(TAB)
									.append(outMatrixName + STRUCT_ACCESS + "matrix[" + k + "++] = " + num + "; ")
									.append(NL);
							sb.append(TAB).append(TAB).append("else").append(NL);
							sb.append(TAB).append(TAB).append(TAB)
									.append(outMatrixName + STRUCT_ACCESS + "matrix[" + k + "++] = 0; ").append(NL);
						} else
							throw new UndefinedTranslationException(
									CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT, curApplyNode, functionName,
									"maximum number of parameters is 2");
					} else if (canMemset) {
						// use memset instead of loop
						sb.append("memset(").append(outMatrixName + STRUCT_ACCESS + "matrix, ").append(num).append(", ")
								.append(totalDimensionString).append(" * sizeof(").append(exprTypeToCType(outType.of()))
								.append("));").append(NL);
					} else {
						// with just a cycle initializes the matrix elements
						sb.append("#pragma omp smid").append(NL);
						sb.append("for(int i=0; i<").append(totalDimensionString).append("; i++){").append(NL);
						sb.append(TAB).append(outMatrixName + STRUCT_ACCESS + "matrix").append("[i] = ").append(num)
								.append(";").append(NL);
						sb.append("}").append(NL);
					}
					newTranslation(curApplyNode, sb.toString());
					return outSymbol.symbol();
				}

			}
		}
		case "strcmp":
			String[] arrParamsTranslated = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			StringBuffer sb = new StringBuffer();

			return "!strcmp(" + arrParamsTranslated[0] + "," + arrParamsTranslated[1] + ")";
		case "size": {
			AASTNode param = params.get(0);
			GType paramType = getExprGeneralized(param);
			sb = new StringBuffer();
			if (paramType.isCastableToMatrix()) {
				DimensionType dtype = (DimensionType) paramType;
				GType outType = getExprGeneralized(outSymbol);
				boolean isOutMatrix = outType.isCastableToMatrix();
				boolean isScalar = outType.isCastableToScalar();
				AASTNode outNode = getOutputNode(outSymbol, isOutMatrix || isScalar, false);
				String[] paramsTranslatedArr = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
				String paramName = paramsTranslatedArr[0];
				String strOutSymbol = outNode.symbol();
				if (isOutMatrix) {
					// case 1 output
					MatrixType outMatType = (MatrixType) outType;
					createVariable(outNode);
					ensureMatrixMemory(sb, outSymbol, strOutSymbol, outMatType.dims());
					// fill out matrix with the dimension of the param matrix
					for (int i = 1; i <= dtype.dims().length; ++i)
						sb.append(indentTabs()).append(strOutSymbol).append(STRUCT_ACCESS)
								.append("matrix[" + Integer.toString(i - 1) + "]").append(" = ")
								.append(TypeUtils.matrixDimName(paramName, i)).append(";").append(NL);
				} else if (isScalar && paramsTranslatedArr.length > 1) {
					String s = paramsTranslatedArr[1];
					int i;
					try {
						i = Integer.parseInt(s);
					} catch (Exception e) {
						throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
								curApplyNode, functionName, s);
					}
					sb.append(strOutSymbol).append(" = ").append(TypeUtils.matrixDimName(paramName, i)).append(";")
							.append(NL);
				} else if (outType.equals(BType.STRUCT)) {
					// case multiple outputs
					StructType outSType = (StructType) outType;
					AASTNode assignNode = null;
					if (outNode.parentExists(NodeType.ASSIGN))
						assignNode = outNode.parent(NodeType.ASSIGN);
					else if (outNode.parentExists(NodeType.ASSIGN))
						assignNode = outNode.parent(NodeType.GASSIGN);

					if (assignNode == null)
						throw new UndefinedTranslationException(CErrorMessage.INTERNAL_FRONTEND_FUNCTION_OUTPUT,
								curApplyNode, functionName, outType);
					// don't assign, already done here
					assignNode.attr(CAttr.TRANSLATE, false);

					List<AASTNode> nodes = assignNode.child(NodeType.LHS).child(NodeType.MATRIX).childs().get(0)
							.childs();

					for (int i = 1; i <= nodes.size(); ++i) {
						AASTNode idNode = getIDNode(nodes.get(i - 1));
						if (!idNode.type().equals(NodeType.VOID)) {
							sb.append(idNode.symbol()).append(" = ").append(TypeUtils.matrixDimName(paramName, i))
									.append(";").append(NL);
						}
					}

				} else
					throw new UndefinedTranslationException(CErrorMessage.INTERNAL_FRONTEND_FUNCTION_OUTPUT,
							curApplyNode, functionName, outType);
				newComment(curApplyNode);
				newTranslation(curApplyNode, sb.toString());
				return strOutSymbol;
			} else
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_SIZE_OF_NON_MATRIX, curApplyNode,
						param.name(), paramType);
		}
		case "length": {
			AASTNode param = params.get(0);
			String scalarSymbol = getOutputNode(outSymbol).symbol();
			GType paramType = getExprGeneralized(param);
			sb = new StringBuffer();

			if (paramType.equals(BType.STRING)) {
				sb.append(scalarSymbol).append(" = strlen(").append(paramsTranslated).append(");").append(NL);
			} else if (paramType.isCastableToScalar()) {
				// length of scalar is 1 (interpret as a matrix of a single value)
				sb.append(scalarSymbol).append(" = 1;").append(NL);
			} else if (paramType instanceof DimensionType) {
				DimensionType dtype = (DimensionType) paramType;
				IntType[] dims = dtype.dims();
				int idim = -1;
				if (dims != null)
					for (IntType dim : dims)
						if (dim.hasValue() && dim.valueAsInt() > idim)
							idim = dim.valueAsInt();
						else if (!dim.hasValue()) {
							// if I find another dimension without known value
							// the return value is an int without known value
							idim = -2;
							break;
						}
				if (idim < 0) {
					sb.append(scalarSymbol).append(" = ");
					if (dims.length > 1) {
						for (int i = 0; i < dims.length - 1; ++i) {
							IntType dim = dims[i];
							if (i < dims.length - 2)
								sb.append("max(").append(symbolicValue(param, i)).append(", ");
							else
								sb.append("max(").append(symbolicValue(param, i)).append(", ")
										.append(symbolicValue(param, i + 1)).append(")");
						}
						for (int i = 0; i < dims.length - 2; ++i)
							sb.append(")");
						sb.append(";").append(NL);
					} else
						sb.append(symbolicValue(param, 0)).append(";").append(NL);
				} else
					sb.append(scalarSymbol).append(" = ").append(idim).append(";").append(NL);
			}
			newComment(outSymbol);
			newTranslation(curApplyNode, sb.toString());
			return scalarSymbol;
		}
		case "sqrt": {
			sb = new StringBuffer();

			String[] paramsTranslatedArray = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			GType ptype = getExprGeneralized(params.get(0));
			if (ptype.isCastableToScalar())
				sb.append("sqrt(").append(paramsTranslatedArray[0]).append(")");
			else if (ptype.isCastableToMatrix() && !topLevelExpression(curApplyNode))
				// sqrt of matrix, but we are inside an expression
				sb.append("sqrt(").append(paramsTranslatedArray[0] + STRUCT_ACCESS + "matrix[" + EXPR_INDEX + "]")
						.append(")");
			else if (ptype.isCastableToMatrix()) {
				// this will set cattr.TRANSLATE=true if outNode is on LHS
				AASTNode outNode = getOutputNode(outSymbol);
				GType outType = getExprGeneralized(outNode);
				String outputSym = outNode.symbol();

				String i = outputSym + "_i";
				List<String> dims = nodeDimsToStr(outNode, ((DimensionType) outType).dims(), true, false);
				// allocate space for outSym
				ensureMatrixMemory(sb, outNode, outputSym, ((DimensionType) ptype).dims());
				sb.append("#pragma omp smid").append(NL);
				sb.append("for(int " + i + " = 0 ; " + i + " < " + String.join(" * ", dims) + "; ++" + i + ")")
						.append(NL);
				sb.append(TAB).append(outputSym).append(STRUCT_ACCESS).append("matrix[" + i + "] = ").append("sqrt(")
						.append(paramsTranslatedArray[0] + STRUCT_ACCESS + "matrix[" + i + "]").append(");").append(NL);

				newComment(outSymbol);
				newTranslation(curApplyNode, sb.toString());
				sb = new StringBuffer();
				sb.append(outputSym);
			} else
				throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS, curApplyNode,
						"sqrt", new String[] { ptype.toShortString() }, new String[] { "Scalar" });

			return sb.toString();
		}
		case "sparse": {
			sb = new StringBuffer();
			String[] paramsTranslatedArray = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			List<GType> paramTypes = new ArrayList<GType>(paramsTranslatedArray.length);
			for (AASTNode param : params)
				paramTypes.add(TypeUtils.getExprGeneralized(param));
			AASTNode outNode = getOutputNode(outSymbol);
			GType outType = getExprGeneralized(outNode);
			if (!outType.equals(BType.MATRIX))
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_OUTPUT_TYPE, curApplyNode, "sparse",
						outType, "Scalar[n x n]");
			MatrixType moutType = (MatrixType) outType;
			String outputSym = outNode.symbol();

			if (paramTypes.size() == 1 && paramTypes.get(0).isCastableToMatrix()) {
				// this should make the input matrix sparse.
				// TODO
				sb.append(outputSym).append(" = ").append(paramsTranslatedArray[0]).append(";").append(NL);

			} else if (paramTypes.size() == 2 && paramTypes.get(0).isCastableToScalar()
					&& paramTypes.get(1).isCastableToScalar()) {
				// CREATE AN EMPTY 2D MATRIX
				IntType row = (IntType) paramTypes.get(0).castTo(BType.INT);
				IntType col = (IntType) paramTypes.get(1).castTo(BType.INT);
				IntType[] dims = new IntType[] { row, col };
				List<String> dimsStr = nodeDimsToStr(outNode, dims, true, true);
				// last -1 let algorithm decide the dimension
				ensureMatrixMemory(sb, outNode, outputSym, (MatrixType) outType,
						dimsStr.toArray(new String[dimsStr.size()]), false, false, false, "-1");
			} else if (paramTypes.size() >= 3) {
				if (paramTypes.get(0).isCastableToScalar() && paramTypes.get(1).isCastableToScalar()
						&& paramTypes.get(2).isCastableToScalar()) {
					// i,j,v a(i,j) = v with i,j,v scalars
					IntType[] dims = new IntType[2];
					int row = 0;
					int col = 1;
					if (paramTypes.size() == 5) {
						row = 3;
						col = 4;
					}
					dims[0] = (IntType) paramTypes.get(row).castTo(BType.INT);
					dims[1] = (IntType) paramTypes.get(col).castTo(BType.INT);
					String i, j, dim1, dim2;
					if (paramTypes.size() == 5) {
						i = ((IntType) paramTypes.get(0).castTo(BType.INT)).valueAsString();
						j = ((IntType) paramTypes.get(1).castTo(BType.INT)).valueAsString();
					} else {
						i = dims[0].valueAsString();
						j = dims[1].valueAsString();
					}
					dim1 = dims[0].valueAsString();
					dim2 = dims[1].valueAsString();

					boolean hasValue = ((ValuedType<?>) paramTypes.get(2)).hasValue();
					String value = ((ValuedType<?>) paramTypes.get(2)).valueAsString();
					String oftype = exprTypeToCType(moutType.of()).trim();
					String actualType;
					switch (paramTypes.get(2).type()) {
					case BOOL:
					case INT:
						actualType = "\"int\"";
						break;
					case SCALAR:
						actualType = "\"double\"";
						break;
					default:
						throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_MATRIX_ELEMENT_TYPE,
								curApplyNode, paramTypes.get(2), outputSym);
					}

					// sb.append(createMatrixRef(outputSym, oftype, null, dims, false, false,
					// VarVisibility.LOCAL_TO_BLOCK, false,
					// null));
					ensureMatrixMemory(sb, outNode, outputSym, (MatrixType) outType, new String[] { dim1, dim2 }, false,
							false, false, "-1");

					sb.append(oftype).append("InitSparseScalarMatrix(");
					sb.append("&" + outputSym).append(", ");
					sb.append(i).append(", ");
					sb.append(j).append(", ");
					if (!hasValue)
						sb.append("&").append(value);
					else
						sb.append("(" + actualType.replaceAll("\"", "") + "[]) { " + value + " }");
					sb.append(", ");
					sb.append(actualType).append(", ");
					sb.append(dim1).append(", ");
					sb.append(dim2).append(");").append(NL);
				} else if (paramTypes.get(0).isCastableToMatrix() && paramTypes.get(1).isCastableToMatrix()
						&& paramTypes.get(2).isCastableToMatrix()) {
					// (i,j,v) a(i,j) = v with (i,j,v) vectors
					// return a matrix with dimension max(i) * max(j)
					GType ofType = GType.get(((DimensionType) paramTypes.get(2)).of());
					AASTNode xparam = TypeUtils.getIDNode(params.get(0));
					String xName = xparam.symbol();
					AASTNode yparam = TypeUtils.getIDNode(params.get(1));
					String yName = yparam.symbol();
					AASTNode vnode = TypeUtils.getIDNode(params.get(2));
					String actualType;
					switch (((MatrixType) paramTypes.get(2)).of().type()) {
					case BOOL:
					case INT:
						actualType = "\"int\"";
						break;
					case SCALAR:
						actualType = "\"double\"";
						break;
					default:
						throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_MATRIX_ELEMENT_TYPE,
								curApplyNode, paramTypes.get(2), outputSym);
					}
					String vName = vnode.symbol();
					DimensionType xtype = (DimensionType) paramTypes.get(0);
					DimensionType ytype = (DimensionType) paramTypes.get(1);
					DimensionType vtype = (DimensionType) paramTypes.get(2);
					String vDims = String.join("*", nodeDimsToStr(vnode, vtype.dims(), true, true));
					String imax, jmax;
					if (paramTypes.size() < 5) {
						// will be automatically computed
						imax = "-1";
						jmax = "-1";
					} else {
						// use parameter 3,4
						imax = TypeUtils.getIDNode(params.get(3)).symbol();
						jmax = TypeUtils.getIDNode(params.get(4)).symbol();
					}
					// create matrix with dimension imax * jmax with values set by third paramater
					IntType[] dims = new IntType[2];
					dims[0] = (IntType) GType.get(BType.INT).name(imax);
					dims[1] = (IntType) GType.get(BType.INT).name(jmax);
					String[] dimsStr = new String[2];
					dimsStr[0] = dims[0].valueAsString();
					dimsStr[1] = dims[1].valueAsString();
					// ofType = ((DimensionType) paramTypes.get(2)).of();
					ofType = moutType.of();
					String xdim = String.join("*", nodeDimsToStr(xparam, xtype.dims(), true, true));
					String ydim = String.join("*", nodeDimsToStr(yparam, ytype.dims(), true, true));
					String i = outputSym + "_i";

					sb.append(exprTypeToCType(ofType).trim()).append("InitSparseMatrix(");
					sb.append("&" + outputSym).append(", ");
					sb.append(xName + STRUCT_ACCESS + "matrix").append(", ");
					sb.append(yName + STRUCT_ACCESS + "matrix").append(", ");
					sb.append(vName + STRUCT_ACCESS + "matrix").append(", ");
					sb.append(actualType).append(", ");
					sb.append(vDims).append(", ");
					sb.append(imax).append(", ");
					sb.append(jmax).append(");").append(NL);
				} else
					throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
							curApplyNode, "sparse", new String[] { paramTypes.get(0).toString(),
									paramTypes.get(1).toString(), paramTypes.get(2).toString() },
							new String[] { "Int[n]", "Int[n]", "Scalar[n]" });
			} else {
				List<String> paramTypesStr = new ArrayList<String>(paramTypes.size());
				for (GType paramType : paramTypes)
					paramTypesStr.add(paramType.toString());
				throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS, curApplyNode,
						"sparse", paramTypesStr.toArray(new String[paramTypesStr.size()]),
						new String[] { "Int[n]", "Int[n]", "Scalar[n]" });
			}
			newComment(outSymbol);
			newTranslation(outSymbol, sb.toString());
			return outputSym;
		}
		case "sort":
		case "setdiff":
		case "union":
		case "unique": {
			if (params.isEmpty())
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_WARN_PARAMETER_TYPE_UNSUPPORTED,
						curApplyNode, "''", "VOID", functionName);

			sb = new StringBuffer();
			GType paramsType = null;
			paramsType = getExprGeneralized(params.get(0));
			AASTNode outNode = getOutputNode(outSymbol);
			GType outType = getExprGeneralized(outNode);
			String outputSym = outNode.symbol();
			DimensionType mtype = (DimensionType) paramsType;
			IntType[] paramDims = mtype.dims();
			String[] paramDimsStr = new String[paramDims.length];
			String[] initialDimsStr = paramDimsStr;
			String[] actualDims = new String[paramDims.length];
			int actualDim = -1;
			String[] paramsTranslatedArray = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			// update translation
			for (int i = 0; i < params.size(); ++i)
				if (getExprGeneralized(params.get(i)).equals(BType.MATRIX))
					paramsTranslatedArray[i] = paramsTranslatedArray[i] + STRUCT_ACCESS + "matrix";

			for (int i = 1; i <= paramDims.length; ++i) {
				paramDimsStr[i - 1] = paramDims[i - 1].valueAsString();
				// if value != 1 or we don't know the value
				if (actualDim == -1 && (paramDims[i - 1].hasValue() && !paramDims[i - 1].value().equals(1)
						|| !paramDims[i - 1].hasValue())) {
					actualDim = i - 1;
					String tmpRealDims = Symbols.getSymbolFromType(functionName + "_realdims", BType.INT);
					createVariable(GType.get(BType.INT), tmpRealDims, NodeType.ID, null, false,
							VarVisibility.LOCAL_TO_BLOCK.context(curApplyNode));

					actualDims[actualDim] = tmpRealDims;
				} else
					actualDims[i - 1] = "1";
			}

			String fun = null;
			boolean changeSize = false;
			// default for 1-param only function, the 2-param functions accept void * as
			// every non-first parameter
			// the cast will be done at execution time inside the function
			boolean intFun = mtype.of().isLessGenericThanInt();
			if (fenum.getName().toLowerCase().equals("unique")) {
				fun = "v?Unique";
				changeSize = true;
			} else if (fenum.getName().toLowerCase().equals("setdiff")
					|| fenum.getName().toLowerCase().equals("union")) {
				changeSize = true;
				// add linear dimension of diff element
				if (params.size() <= 1)
					throw new UndefinedTranslationException(
							CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_NUMBER_OF_ARGUMENTS, curApplyNode,
							fenum.getName(), params.size());

				GType diffType = getExprGeneralized(params.get(1));
				DimensionType difftype = (DimensionType) diffType;
				IntType[] diffDims = difftype.dims();
				List<String> diffDimsStr = nodeDimsToStr(params.get(1), diffDims, true);
				// cast of second parameter
				GType otherType = getExprGeneralized(params.get(1));
				DimensionType othertype = (DimensionType) otherType;

				String[] tmp = new String[paramsTranslatedArray.length + 2];
				int i = 0;
				for (; i < params.size(); ++i)
					tmp[i] = paramsTranslatedArray[i];
				// type that should be casted to type identified by the first parameter mtype
				tmp[i] = '"' + exprTypeToCType(otherType).trim() + '"';
				// dimension of diff matrix
				tmp[i + 1] = String.join(" * ", diffDimsStr);
				paramsTranslatedArray = tmp;

				if (fenum.getName().toLowerCase().equals("setdiff")) {
					fun = "!?Setdiff";
					String str_to_sub;
					if (mtype.equals(BType.MATRIX_ACCESS_SLICE) && othertype.equals(BType.MATRIX_ACCESS_SLICE))
						str_to_sub = "ss";
					else if (mtype.equals(BType.MATRIX_ACCESS_SLICE) && othertype.equals(BType.MATRIX))
						str_to_sub = "sv";
					else if (mtype.equals(BType.MATRIX) && othertype.equals(BType.MATRIX))
						str_to_sub = "vv";
					else if (mtype.equals(BType.MATRIX) && othertype.equals(BType.MATRIX_ACCESS_SLICE))
						str_to_sub = "vs";
					else
						throw new UndefinedTranslationException(CErrorMessage.INTERNAL_WARN_PARAMETER_TYPE_UNSUPPORTED,
								curApplyNode, params.get(1).name(), othertype, functionName);

					fun = fun.replaceFirst("!", str_to_sub);
				} else {
					fun = "v?Union";
					initialDimsStr = new String[paramDims.length];
					for (int k = 0; k < paramDims.length; ++k)
						if (paramDims[k].hasIntValue() && paramDims[k].valueAsInt().equals(1))
							initialDimsStr[k] = "1";
						else if (k < diffDims.length)
							initialDimsStr[k] = paramDims[k].valueAsString() + " + " + diffDims[k].valueAsString();
						else
							initialDimsStr[k] = paramDims[k].valueAsString();
				}
			} else if (fenum.getName().toLowerCase().equals("sort")) {
				fun = "v?Sort";
				if (params.size() == 1) {
					// add extra parameter 'direction'
					String[] tmp = new String[paramsTranslatedArray.length + 1];
					int i = 0;
					for (; i < params.size(); ++i)
						tmp[i] = paramsTranslatedArray[i];
					tmp[i] = "\"ascend\"";
					paramsTranslatedArray = tmp;
				}
			}

			// get actual function name to call (see matrixLib.h and user_structures.c/h)
			char char_to_sub;
			if (intFun) {
				char_to_sub = 'i';
			} else {
				char_to_sub = 'd';
			}
			// int or bool
			fun = fun.replace('?', char_to_sub);

			AASTNode assignNode = null;
			if (outNode.parentExists(NodeType.ASSIGN))
				assignNode = outNode.parent(NodeType.ASSIGN);
			else if (outNode.parentExists(NodeType.ASSIGN))
				assignNode = outNode.parent(NodeType.GASSIGN);

			if (assignNode != null)
				// don't assign, already done here
				assignNode.attr(CAttr.TRANSLATE, false);

			if (outType.isCastableToMatrix()) {

				// ensure capacity for output matrix, based on input (max possible)
				ensureMatrixMemory(sb, outNode, initialDimsStr);

				if (changeSize)
					sb.append(actualDims[actualDim]).append(" = ");

				sb.append(fun).append("(").append(String.join(", ", paramsTranslatedArray)).append(", ")
						// n dimensions 1 output matrix
						.append(paramDimsStr.length).append(", ")
						.append(Integer.toString(paramDimsStr.length) + "+" + "1").append(", ")
						// output matrix
						.append(outputSym + STRUCT_ACCESS + "matrix").append(", ")
						// dimensions
						.append(String.join(", ", paramDimsStr)).append(");").append(NL);

				if (changeSize)
					// ensure capacity for output matrix (correct value)
					ensureMatrixMemory(sb, outNode, actualDims);
			} else if (outType.equals(BType.STRUCT)) {
				// variant with multiple outputs
				StructType stype = (StructType) outType;
				if (stype.numberOfFields() > 2) {
					Iterator<Tuple<List<GType>, String>> it = stype.iterFields();
					it.next();
					it.next();
					Tuple<List<GType>, String> unsupported = it.next();
					throw new UndefinedTranslationException(CErrorMessage.INTERNAL_FRONTEND_FUNCTION_OUTPUT,
							curApplyNode, functionName, unsupported.second(), unsupported.first());
				}

				if (assignNode == null)
					throw new UndefinedTranslationException(CErrorMessage.INTERNAL_FRONTEND_FUNCTION_OUTPUT,
							curApplyNode, functionName, "[]", "VOID");
				// ensure capacity for output matrix, based on input (max possible)
				// get elements between [] in [a,b] = ...
				List<AASTNode> nodes = assignNode.child(NodeType.LHS).child(NodeType.MATRIX).childs().get(0).childs();
				AASTNode values = getIDNode(nodes.get(0));
				AASTNode mappings = getIDNode(nodes.get(1));

				ensureMatrixMemory(sb, values, initialDimsStr);
				// always n x 1
				ensureMatrixMemory(sb, mappings, initialDimsStr);

				if (changeSize)
					sb.append(actualDims[actualDim]).append(" = ");

				sb.append(fun).append("(").append(String.join(", ", paramsTranslatedArray)).append(", ")
						// n dimensions 1 output matrix
						.append(paramDimsStr.length).append(", ")
						.append(Integer.toString(paramDimsStr.length) + "+" + "2").append(", ")
						// output matrix
						.append(values.symbol() + STRUCT_ACCESS + "matrix").append(", ")
						.append(mappings.symbol() + STRUCT_ACCESS + "matrix").append(", ")
						// dimensions
						.append(String.join(", ", paramDimsStr)).append(");").append(NL);

				if (changeSize) {
					// ensure capacity for output matrix (correct value)
					ensureMatrixMemory(sb, values, actualDims);
					// always n x 1
					actualDims[0] = actualDims[actualDim];
					actualDims[actualDim] = "1";
					ensureMatrixMemory(sb, mappings, actualDims);
				}
			} else
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, functionName, paramsType);

			newComment(curApplyNode);
			newTranslation(curApplyNode, sb.toString());
			return outputSym;
		}
		// compute the maximum between two scalar
		case "sum":
		case "mean":
		case "std":
		case "var":
		case "min":
		case "max":
			// get the parameter name vector
			String[] paramsTranslatedArray = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			// functions that operate similarly, and "other"
			boolean statisticalFunction;
			String scalarfun, pointwiseMatrixFun, matrixFun, matrixScalarFun, scalarMatrixFun;

			if (functionName.toLowerCase().equals("max")) {
				// in case inputs are scalar
				scalarfun = "max";
				// in case inputs are two matrices
				pointwiseMatrixFun = "*Max";
				// in case input is a single matrix (max by columns)
				matrixFun = "*Col?Max";
				// in case input is a single matrix with 1 dimension
				matrixScalarFun = "*ScalarMax";
				// in case input is a matrix and a scalar, max/min of every element of matrix
				// and the scalar
				scalarMatrixFun = "*ScalarMatrixMax";
				statisticalFunction = true;
			} else if (functionName.toLowerCase().equals("min")) {
				scalarfun = "min";
				pointwiseMatrixFun = "*Min";
				// in case input is a single matrix (max by columns)
				matrixFun = "*Col?Min";
				// in case input is a single matrix with 1 dimension
				matrixScalarFun = "*ScalarMin";
				// in case input is a matrix and a scalar, max/min of every element of matrix
				// and the scalar
				scalarMatrixFun = "*ScalarMatrixMin";
				statisticalFunction = true;
			} else if (functionName.toLowerCase().equals("sum")) {
				scalarfun = "sum";
				pointwiseMatrixFun = "*Sum";
				// in case input is a single matrix (max by columns)
				matrixFun = "*Col?Sum";
				// in case input is a single matrix with 1 dimension
				matrixScalarFun = "*ScalarSum";
				// in case input is a matrix and a scalar, max/min of every element of matrix
				// and the scalar
				scalarMatrixFun = null;
				statisticalFunction = false;
			} else if (functionName.toLowerCase().equals("mean")) {
				scalarfun = "mean";
				pointwiseMatrixFun = "*Mean";
				// in case input is a single matrix (max by columns)
				matrixFun = "*Col?Mean";
				// in case input is a single matrix with 1 dimension
				matrixScalarFun = "*ScalarMean";
				// in case input is a matrix and a scalar, max/min of every element of matrix
				// and the scalar
				scalarMatrixFun = null;
				statisticalFunction = false;
			} else if (functionName.toLowerCase().equals("std")) {
				scalarfun = "std";
				pointwiseMatrixFun = "*Std";
				// in case input is a single matrix (max by columns)
				matrixFun = "*Col?Std";
				// in case input is a single matrix with 1 dimension
				matrixScalarFun = "*ScalarStd";
				// in case input is a matrix and a scalar, max/min of every element of matrix
				// and the scalar
				scalarMatrixFun = null;
				statisticalFunction = false;
			} else if (functionName.toLowerCase().equals("var")) {
				scalarfun = "var";
				pointwiseMatrixFun = "*Var";
				// in case input is a single matrix (max by columns)
				matrixFun = "*Col?Var";
				// in case input is a single matrix with 1 dimension
				matrixScalarFun = "*ScalarVar";
				// in case input is a matrix and a scalar, max/min of every element of matrix
				// and the scalar
				scalarMatrixFun = null;
				statisticalFunction = false;
			} else
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION, curApplyNode,
						fenum);

			GType paramsType = null;
			if (params.isEmpty())
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_WARN_PARAMETER_TYPE_UNSUPPORTED,
						curApplyNode, "''", "VOID", functionName);

			paramsType = getExprGeneralized(params.get(0));
			GType secondParamType = null;
			if (params.size() > 1)
				secondParamType = getExprGeneralized(params.get(1));

			AASTNode outNode = getOutputNode(outSymbol);
			GType outNodeType = getExprGeneralized(outNode);
			String outputSym = outNode.symbol();
			String outputParam;
			StringBuffer resultBuf = new StringBuffer();
			if (paramsType.isCastableToMatrix()) {
				String fun, funparams;
				// non-statistical function accepts a matrix and optionally a columns over which
				// the function should be applied
				// statistical function accepts
				// - two matrix, or a matrix and a scalar, and apply pointwise the statistic
				// - two matrix (second empty) and a column identifier over which you want to
				// compute the max/min
				int reduceCol = -1;
				if (!statisticalFunction || (paramsTranslatedArray.length > 2 || paramsTranslatedArray.length == 1)) {
					// in this case min by column, the output is the input with
					// dimension reduced by 1
					DimensionType dparamType = (DimensionType) paramsType;
					IntType[] paramTypeDims = dparamType.dims();
					IntType[] ptypeRealDims = TypeUtils.realDims(paramTypeDims);
					boolean reduceMatrixDimension = ptypeRealDims.length > 1;
					if (ptypeRealDims.length > 1)
						fun = matrixFun;
					else
						fun = matrixScalarFun;

					if (paramsTranslatedArray.length > 1) {
						if (!secondParamType.isCastableToBool()) {
							if (statisticalFunction) {
								// skip second matrix because empty
								secondParamType = getExprGeneralized(params.get(2));
								if (!secondParamType.isCastableToBool())
									throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TYPE,
											curApplyNode, secondParamType);
							} else
								throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TYPE, curApplyNode,
										secondParamType);
						}
						ValuedType sumBy = (ValuedType) secondParamType;
						if (sumBy.hasValue()) {
							reduceCol = sumBy.valueAsInt();
							fun = fun.replace("?", Integer.toString(sumBy.valueAsInt()));
						} else
							throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TYPE, curApplyNode,
									secondParamType);
					} else
						reduceCol = 1;

					if (reduceMatrixDimension) {
						fun = matrixFun;
						// ensure capacity for output matrix
						// create a matrix with non-1 elements taken from parameter
						IntType[] outNodeTypeDims = ((DimensionType) outNodeType).dims();
						IntType[] outNodeParamDims = new IntType[outNodeTypeDims.length];
						for (int it = 0; it < outNodeTypeDims.length; ++it)
							if (outNodeTypeDims[it].hasValue() && outNodeTypeDims[it].valueAsInt() == 1)
								outNodeParamDims[it] = outNodeTypeDims[it];
							else
								outNodeParamDims[it] = paramTypeDims[it];

						ensureMatrixMemory(resultBuf, outNode, outNodeParamDims);
						if (outNodeTypeDims.length < paramTypeDims.length) {
							// we need to wrap output parameter to be the same type of input parameter
							IntType[] formaldims = new IntType[paramTypeDims.length];
							// create a matrix with the same dimension as input param but with a 1 in place
							// of the reducecol
							// needed to make checkFunctionTypes work
							for (int pd = 0; pd < paramTypeDims.length; ++pd)
								if (pd == reduceCol - 1)
									formaldims[pd] = (IntType) GType.get(BType.INT, 1);
								else
									formaldims[pd] = (IntType) GType.get(paramTypeDims[pd]);
							MatrixType formalParamType = (MatrixType) GType.get(BType.MATRIX, dparamType.name(),
									GType.get(dparamType.of()), formaldims);
							String[] outSymbolModified = new String[] { outputSym };
							List<GType> formalList = new ArrayList<GType>(1);
							formalList.add(formalParamType);
							checkFunctionTypes(resultBuf, curApplyNode, formalList, new GType[] { outNodeType },
									outSymbolModified);
							outputSym = outSymbolModified[0];
						}
						outputParam = outputSym;
					} else {
						// return type is a scalar
						fun = matrixScalarFun;
						// pass in reference
						outputParam = "&" + outputSym;
					}
					List<String> dimensionAsStrings = nodeDimsToSymbolicReference(params.get(0), paramTypeDims);

					funparams = paramsTranslatedArray[0] + ", " + String.join(" * ", dimensionAsStrings);
				} else {
					// paramsTranslatedArray.length <= 2 && statisticalFunction
					// in this case point-wise min between the matrices passed, or matrix/scalar
					// passed
					outputParam = outputSym;
					List<String> funparamsArray = new LinkedList<>();
					List<String> dimensionAsStrings;

					// ensure capacity for output matrix, based on first param
					// create a matrix with non-1 elements taken from parameter
					IntType[] paramsTypeDims = ((DimensionType) paramsType).dims();
					ensureMatrixMemory(resultBuf, outNode, paramsTypeDims);

					if (secondParamType.isCastableToMatrix()) {
						fun = pointwiseMatrixFun;
						for (int i = 0; i < paramsTranslatedArray.length; ++i) {
							dimensionAsStrings = nodeDimsToStr(params.get(i), ((MatrixType) paramsType).dims(), false);
							funparamsArray.add(paramsTranslatedArray[i]);
							funparamsArray.add(String.join(" * ", dimensionAsStrings));
						}
					} else if (secondParamType.isCastableToScalar()) {
						fun = scalarMatrixFun;
						dimensionAsStrings = nodeDimsToStr(params.get(0), ((MatrixType) paramsType).dims(), false);
						// matrix and dimension
						funparamsArray.add(paramsTranslatedArray[0]);
						funparamsArray.add(String.join(" * ", dimensionAsStrings));
						// scalar
						funparamsArray.add(paramsTranslatedArray[1]);
					} else
						throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
								curApplyNode, functionName, new String[] { secondParamType.toShortString() },
								new String[] { "Matrix or Scalar" });
					funparams = String.join(", ", funparamsArray);
					reduceCol = 1;
				}
				// get actual function name to call (see matrixLib.h and user_structures.c/h)
				fun = fun.replace("*", exprTypeToCType(paramsType));
				// by default operates on first dimension
				fun = fun.replace("?", Integer.toString(reduceCol));
				// remember for end of translation
				// where we need to generate all the possibile functions that do this
				summarizeByCol.add(reduceCol);
				// TODO CHECK DIMENSIONS
				if (paramsType instanceof MatrixType) {
					// compute min/max
					resultBuf.append(fun).append("(").append(outputParam).append(", ").append(funparams).append(");")
							.append(NL);
				} else
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TYPE, curApplyNode, paramsType);

			} else if (paramsType.isCastableToScalar()) {
				if (!statisticalFunction && paramsTranslatedArray.length > 1)
					throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
							curApplyNode, functionName, new String[] { paramsType.toShortString() },
							new String[] { "Matrix" });

				if (secondParamType != null) {
					// max of scalar is the scalar itself
					if (secondParamType.isCastableToScalar())
						resultBuf.append(outputSym).append(" = ").append(scalarfun).append("(")
								.append(String.join(", ", paramsTranslatedArray)).append(");").append(NL);
					else if (secondParamType.isCastableToMatrix()) {
						// second param is a matrix
						String fun = scalarMatrixFun;
						List<String> dimensionAsStrings = nodeDimsToStr(params.get(1),
								((MatrixType) secondParamType).dims(), false);

						fun = fun.replace("*", exprTypeToCType(secondParamType));
						resultBuf.append(fun).append("(").append(outputSym).append(", ")
								// matrix
								.append(paramsTranslatedArray[1]).append(",")
								.append(String.join(" * ", dimensionAsStrings)).append(", ")
								// scalar
								.append(paramsTranslatedArray[0]).append(");").append(NL);
					} else
						throw new UndefinedTranslationException(CErrorMessage.USER_FUNCTION_UNEXPECTED_PARAMETERS,
								curApplyNode, functionName, new String[] { secondParamType.toShortString() },
								new String[] { "Matrix" });
				} else
					resultBuf.append(outputSym).append(" = ").append(paramsTranslatedArray[0]).append(";").append(NL);
			} else
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TYPE, curApplyNode, paramsType);
			newComment(curApplyNode);
			newTranslation(curApplyNode, resultBuf.toString());
			return outputSym;
		case "odeset":
			if (params.size() % 2 != 0)
				throw new UndefinedTranslationException(
						CErrorMessage.TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_WRONG_ARGUMENTS, curApplyNode);

			StringBuffer bufferStmt = new StringBuffer("");

			// parameters to the odeset are in couples: the first specify the
			// name
			// of the parameter to be set, the second one is the value
			String absoluteTol = String.valueOf(OdeParameter.ABS_TOL.defaultValue);
			String relativeTol = String.valueOf(OdeParameter.REL_TOL.defaultValue);
			String maxStep = String.valueOf(OdeParameter.MAX_STEP.defaultValue);
			String initialStep = String.valueOf(OdeParameter.INITIAL_STEP.defaultValue);
			String maxOrder = String.valueOf(OdeParameter.MAX_ORDER.defaultValue);

			String[] paramsTranslatedArr = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			for (int i = 0; i < paramsTranslatedArr.length; i += 2) {
				String strparam = paramsTranslatedArr[i];

				// cut the quotes if they're present
				strparam = strparam.replaceAll("\"", "");

				AASTNode paramNode = params.get(i);
				GType paramType = getExprGeneralized(paramNode);

				if (!GType.get(BType.STRING).equals(paramType))
					throw new UndefinedTranslationException(
							CErrorMessage.TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_NONSTRING_ARGUMENT, curApplyNode,
							strparam);
				// actual string value if present
				String strValue = strparam;
				if (strValue == null)
					throw new UndefinedTranslationException(
							CErrorMessage.TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_NONSTRING_ARGUMENT, curApplyNode);

				if (OdeParameter.ABS_TOL.is(strValue)) {
					if (i + 1 >= params.size())
						throw new UndefinedTranslationException(
								CErrorMessage.TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_WRONG_ARGUMENTS, curApplyNode);
					AASTNode paramValNode = params.get(i + 1);
					GType paramValType = getExprGeneralized(paramValNode);
					if (!GType.get(BType.SCALAR).equals(paramValType.type()))
						throw new UndefinedTranslationException(
								CErrorMessage.TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_NONSCALAR_ARGUMENT, curApplyNode);
					absoluteTol = paramsTranslatedArr[i + 1];
				}
				if (OdeParameter.REL_TOL.is(strValue)) {
					if (i + 1 >= params.size())
						throw new UndefinedTranslationException(
								CErrorMessage.TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_WRONG_ARGUMENTS, curApplyNode);
					AASTNode paramValNode = params.get(i + 1);
					GType paramValType = getExprGeneralized(paramValNode);
					if (!GType.get(BType.SCALAR).equals(paramValType.type()))
						throw new UndefinedTranslationException(
								CErrorMessage.TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_NONSCALAR_ARGUMENT, curApplyNode);
					relativeTol = paramsTranslatedArr[i + 1];
				}
				if (OdeParameter.MAX_STEP.is(strValue)) {
					if (i + 1 >= params.size())
						throw new UndefinedTranslationException(
								CErrorMessage.TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_WRONG_ARGUMENTS, curApplyNode);
					AASTNode paramValNode = params.get(i + 1);
					GType paramValType = getExprGeneralized(paramValNode);
					if (!GType.get(BType.SCALAR).equals(paramValType.type()))
						throw new UndefinedTranslationException(
								CErrorMessage.TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_NONSCALAR_ARGUMENT, curApplyNode);
					maxStep = paramsTranslatedArr[i + 1];
				}
				if (OdeParameter.INITIAL_STEP.is(strValue)) {
					if (i + 1 >= params.size())
						throw new UndefinedTranslationException(
								CErrorMessage.TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_WRONG_ARGUMENTS, curApplyNode);
					AASTNode paramValNode = params.get(i + 1);
					GType paramValType = getExprGeneralized(paramValNode);
					if (!paramValType.isCastableToScalar())
						throw new UndefinedTranslationException(
								CErrorMessage.TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_NONSCALAR_ARGUMENT, curApplyNode);
					initialStep = paramsTranslatedArr[i + 1];
				}
				if (OdeParameter.MAX_ORDER.is(strValue)) {
					if (i + 1 >= params.size())
						throw new UndefinedTranslationException(
								CErrorMessage.TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_WRONG_ARGUMENTS, curApplyNode);
					AASTNode paramValNode = params.get(i + 1);
					GType paramValType = getExprGeneralized(paramValNode);
					if (!paramValType.isCastableToInt())
						throw new UndefinedTranslationException(
								CErrorMessage.TODO_INTERNAL_FRONTEND_FUNCTION_ODESET_NONINT_ARGUMENT, curApplyNode);
					maxOrder = paramsTranslatedArr[i + 1];
				}

			}
			outNode = getOutputNode(outSymbol);
			String outNodeName = outNode.symbol();
			// instantiate the INTEGRATION_OPTS struct
			bufferStmt.append(outNodeName).append("=setIntegrationOptions(");

			bufferStmt.append(absoluteTol).append(",");
			bufferStmt.append(relativeTol).append(",");
			bufferStmt.append(maxStep).append(",");
			bufferStmt.append(initialStep).append(",");
			bufferStmt.append(maxOrder);
			bufferStmt.append(");").append(NL);

			newComment(outSymbol);
			newTranslation(curApplyNode, bufferStmt.toString());

			return outNodeName;

		case "isinf":
			bufferStmt = new StringBuffer();

			// get the Array that has to be checked
			paramsTranslatedArr = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			String paramName = paramsTranslatedArr[0];

			outNode = getOutputNode(outSymbol);
			outNodeName = getNodeSymbol(outNode);

			AASTNode parameter = params.get(0);
			// check the type
			GType parameterType = getExprGeneralized(parameter);

			if (parameterType.isCastableToScalar()) {
				// separate the case integer and scalar
				bufferStmt.append(outNodeName).append("=isinf(").append(paramName).append(");").append(NL);
			} else if (parameterType.isCastableToMatrix()) {
				IntType[] dims = ((DimensionType) parameterType).dims();
				List<String> dimensionsAsString = nodeDimsToStr(parameter, dims, false);

				String numDimension = Integer.toString(dimensionsAsString.size());
				String dimensionParameters = Strings.join(dimensionsAsString, ',');

				ensureMatrixMemory(bufferStmt, outNode, dims);

				String outMatrix = outNodeName + ".matrix";
				String paramMatrix = paramName + ".matrix";

				GType ofType = ((DimensionType) parameterType).of();

				// matrix type
				String prefix;
				if (!ofType.equals(BType.SCALAR)) {
					int idim = 1;
					for (IntType dim : dims)
						if (dim.hasValue())
							idim *= dim.valueAsInt();
						else {
							idim = -1;
							break;
						}
					if (idim > 0) {
						for (int i = 0; i < idim; ++i)
							bufferStmt.append(outMatrix).append("[" + i + "] = 0;").append(NL);
					} else {
						bufferStmt.append("#pragma omp smid").append(NL);
						bufferStmt.append("for(int i = 0; i < " + String.join("*", dimensionsAsString) + " ; ++i)")
								.append(NL);
						bufferStmt.append(TAB).append(outMatrix).append("[i] = 0;").append(NL);
					}
				} else {
					prefix = "d";
					bufferStmt.append(prefix).append("mIsInf(").append(outMatrix).append(",").append(paramMatrix)
							.append(",").append(numDimension);
					bufferStmt.append(dimensionParameters).append(");").append(NL);
				}

			} else
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, functionName, parameterType);

			newComment(curApplyNode);
			newTranslation(curApplyNode, bufferStmt.toString());

			return outNodeName;
		case "isnan":
			bufferStmt = new StringBuffer();

			// get the Array that has to be checked
			paramsTranslatedArr = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			paramName = paramsTranslatedArr[0];

			outNode = getOutputNode(outSymbol);
			outNodeName = getNodeSymbol(outNode);

			parameter = params.get(0);
			// check the type
			parameterType = getExprGeneralized(parameter);

			if (parameterType.isCastableToScalar()) {
				// separate the case integer and scalar
				bufferStmt.append(outNodeName).append("=isnan(").append(paramName).append(");").append(NL);
			} else if (parameterType.isCastableToMatrix()) {
				IntType[] dims = ((DimensionType) parameterType).dims();
				List<String> dimensionsAsString = nodeDimsToStr(parameter, dims, false);

				String numDimension = Integer.toString(dimensionsAsString.size());
				String dimensionParameters = Strings.join(dimensionsAsString, ',');

				ensureMatrixMemory(bufferStmt, outNode, dims);

				String outMatrix = outNodeName + ".matrix";
				String paramMatrix = paramName + ".matrix";

				GType ofType = ((DimensionType) parameterType).of();
				// matrix type
				String prefix;
				if (!ofType.equals(BType.SCALAR)) {
					logger.warn("Matrix of " + ofType + " " + outSymbol.name() + " cannot contains 'nan' values at "
							+ outSymbol.location() + " of " + outSymbol.compilationUnit().sourcePath());
					int idim = 1;
					for (IntType dim : dims)
						if (dim.hasValue())
							idim *= dim.valueAsInt();
						else {
							idim = -1;
							break;
						}
					if (idim > 0) {
						for (int i = 0; i < idim; ++i)
							bufferStmt.append(outMatrix).append("[" + i + "] = 0;").append(NL);
					} else {
						bufferStmt.append("#pragma omp smid").append(NL);
						bufferStmt.append("for(int i = 0; i < " + String.join("*", dimensionsAsString) + " ; ++i)")
								.append(NL);
						bufferStmt.append(TAB).append(outMatrix).append("[i] = 0;").append(NL);
					}
				} else {
					prefix = "d";
					// matrix type
					bufferStmt.append(prefix).append("mIsNan(").append(outMatrix).append(",").append(paramMatrix)
							.append(",").append(numDimension).append(",");
					bufferStmt.append(dimensionParameters).append(");").append(NL);
				}

			} else
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, functionName, parameterType);

			newComment(curApplyNode);
			newTranslation(curApplyNode, bufferStmt.toString());

			return outNodeName;
		case "isempty":
			if (params.size() == 1 && getExprGeneralized(params.get(0)).isCastableToMatrix()) {
				paramsTranslatedArr = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
				paramName = paramsTranslatedArr[0];
				parameter = params.get(0);

				return getEmptyCheckMacro(parameter) + "(" + paramName + ") || " + getNullCheckMacro(parameter) + "("
						+ paramName + ")";
			} else {
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_WITHOUT_ARGUMENT,
						curApplyNode, fenum.getName(), params.size());
			}
			// saves generic results to file
		case "writematrix":
		case "csvwrite":
		case "save":
			if (params.size() != 2)
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_NUMBER_OF_ARGUMENTS,
						curApplyNode, fenum.getName(), params.size());
			// get the path of the file I've to write
			paramsTranslatedArr = paramsTranslated.split("\\" + PARAMS_SEPARATOR);

			String savepath = paramsTranslatedArr[0];
			// get variable name
			String variableToSaveName = paramsTranslatedArr[1];
			AASTNode variableNode = params.get(1);

			if (fenum.getName().equals("writematrix")) {
				// in writematrix first you specify the variable, second the path
				savepath = variableToSaveName;
				variableToSaveName = paramsTranslatedArr[0];
				variableNode = params.get(0);
			}

			GType type = getExprGeneralized(variableNode);

			StringBuffer stmtBufferSave = new StringBuffer("");

			if (type instanceof BoolType || type instanceof IntType) {
				stmtBufferSave.append("siSave(").append(savepath).append(", ").append(variableToSaveName).append(");")
						.append(NL);
			} else if (type instanceof ScalarType) {
				stmtBufferSave.append("sdSave(").append(savepath).append(", ").append(variableToSaveName).append(");")
						.append(NL);
			} else if (type instanceof StringType) {
				stmtBufferSave.append("ssSave(").append(savepath).append(", ").append(variableToSaveName).append(");")
						.append(NL);
			} else if (type instanceof MatrixType) {

				GType typeOf = ((MatrixType) type).of();

				List<String> matrixDim = nodeDimsToStr(variableNode, ((DimensionType) type).dims(), false);
				String matDimensions = "";
				for (int i = 0; i < matrixDim.size(); ++i)
					matDimensions += ", " + matrixDim.get(i);

				if (typeOf instanceof BoolType || typeOf instanceof IntType) {
					stmtBufferSave.append("viSave(").append(savepath).append(", ").append(variableToSaveName)
							.append(".matrix").append(", ").append(variableToSaveName).append(".poly_basis")
							.append(", ").append(matrixDim.size()).append(matDimensions).append(");").append(NL);
				} else if (typeOf instanceof ScalarType) {
					stmtBufferSave.append("vdSave(").append(savepath).append(", \"w\", ").append(variableToSaveName)
							.append(".matrix").append(", ").append(variableToSaveName).append(".poly_basis")
							.append(", ").append(matrixDim.size()).append(matDimensions).append(");").append(NL);
				} else
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
							curApplyNode, functionName, typeOf);
			} else
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, functionName, type);

			newComment(curApplyNode);
			newTranslation(curApplyNode, stmtBufferSave.toString());

			return null;
		// load generic cvs/mat file
		case "readmatrix":
		case "csvread": {
			if (params.size() < 1)
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_NUMBER_OF_ARGUMENTS,
						curApplyNode, fenum.getName(), params.size());
			bufferStmt = new StringBuffer();

			// get the file name (it could be store both in a previous variable or in a
			// literal)
			// getting the parameters
			paramsTranslatedArray = paramsTranslated.split("\\" + PARAMS_SEPARATOR);
			// implicit assumption that it is a string
			paramsTranslatedArray[0] = paramsTranslatedArray[0].replaceAll("\"|'", "\"");
			outNode = getOutputNode(outSymbol);
			outNodeName = outNode.symbol();
			if (!storeInput) {
				String funName = "csvLoad";
				if (paramsTranslatedArray.length > 1) {
					String[] extraParams = new String[paramsTranslatedArray.length - 1];
					for (int i = 1; i < paramsTranslatedArray.length; ++i)
						extraParams[i - 1] = paramsTranslatedArray[i];
					bufferStmt.append(outNodeName).append(" = *").append(funName).append("(")
							.append(paramsTranslatedArray[0]).append(", ").append(extraParams.length).append(", ")
							.append(String.join(", ", extraParams)).append(");").append(NL);
				} else
					bufferStmt.append(outNodeName).append(" = *").append(funName).append("(")
							.append(paramsTranslatedArray[0]).append(", ").append("0").append(");").append(NL);
			} else {
				// writes the header file containing the constants
				newComment(curApplyNode, "the csv has been stored in the loaded-csv.h");

				try {

					String fileName = paramsTranslatedArray[0].replaceAll("\"", "");
					Path filePath = basePath.resolve(fileName);

					// stores the csv value in the store-csv header
					StoredCsv storedCsv = null;
					if (paramsTranslatedArray.length == 1) {
						// just file
						storedCsv = storeCsv(filePath);
					} else {
						// file name and begin, end
						Integer firstRow = Integer.parseInt(paramsTranslatedArray[1]);
						Integer firstColumn = Integer.parseInt(paramsTranslatedArray[2]);
						Integer lastRow = null;
						Integer lastColumn = null;
						if (paramsTranslatedArray.length > 3) {
							lastRow = Integer.parseInt(paramsTranslatedArray[3]);
							lastColumn = Integer.parseInt(paramsTranslatedArray[4]);
						}

						storedCsv = storeCsv(filePath, firstRow, firstColumn, lastRow, lastColumn);
					}

					String[] dims = new String[2];
					dims[0] = String.valueOf(storedCsv.rowNumber);
					dims[1] = String.valueOf(storedCsv.columnNumber);

					// ensure matrix memory has to be called before
					ensureMatrixMemory(bufferStmt, outNode, dims);

					// assign to the current variable
					bufferStmt.append(outNodeName).append(STRUCT_ACCESS).append("matrix = ")
							.append(storedCsv.storedCsvVariableName).append(";").append(NL);
				} catch (Exception e) {
					throw new InputException(CErrorMessage.USER_IO_INVALID_INPUT_EXCEPTION, curApplyNode, e);
				}
			}
			newComment(outNode);
			newTranslation(curApplyNode, bufferStmt.toString());
			// write on the output file
			return bufferStmt.toString();
		}
		// ode solver
		case "ode23s":
		case "ode45s":
		case "ode15s":
		case "ode23":
		case "ode45":
		case "ode15":
			if (params.size() < 3)
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_NUMBER_OF_ARGUMENTS,
						curApplyNode, fenum.getName(), params.size());

			bufferStmt = new StringBuffer();

			// the first parameter is the anonimous function to integrate
			AASTNode anonimousFun = params.get(0);
			// get the child FUNCTION under the Expression
			AASTNode origFunctionNode = anonimousFun.child(NodeType.FUNCTION);
			if (origFunctionNode == null) {
				// maybe is a reference to a function
				AASTNode refNode = anonimousFun.child(NodeType.AT);
				if (!refNode.hasAttr(NodeAttr.REF_FUNCTION))
					throw new MissingNodeException(CErrorMessage.INTERNAL_FUNCTION_REFERENCE_WITHOUT_REFERENCE,
							curApplyNode, anonimousFun.name());
				origFunctionNode = (AASTNode) refNode.attr(NodeAttr.REF_FUNCTION);
			}
			AASTNode parameterListNode = origFunctionNode.child(NodeType.PARAMETER_LIST);
			List<AASTNode> parameterNodes = parameterListNode.childs();
			AASTNode functionNode = origFunctionNode;
			// detect if it's possible to avoid the call to the anonymous function
			// (in case it's just calling another function)
			if (functionNode.hasAttr(NodeAttr.APPLY_FUNCTION)) {
				AASTNode applyNode = (AASTNode) functionNode.attr(NodeAttr.APPLY_FUNCTION);
				AAST cu = functionNode.compilationUnit();
				AASTNode funIdNode = applyNode.child(NodeType.ID);
				if (cu.functionSymbolExists(funIdNode)) {
					AASTNode functionNodeCandidate = cu.functionSymbolNode(funIdNode);
					List<AASTNode> candidateParams = functionNodeCandidate.childs(NodeType.PARAMETER_LIST);
					// check if the parameter types are equal
					if (candidateParams.size() == parameterNodes.size()) {
						boolean paramTypesMatch = true;
						for (int i = 0; i < parameterNodes.size(); ++i) {
							AASTNode origParam = parameterNodes.get(i);
							AASTNode candidateParam = candidateParams.get(i);
							GType origType = getExprGeneralized(origParam);
							GType candidateType = getExprGeneralized(origParam);
							if (origType == null || candidateType == null || !origType.equals(candidateType.type())) {
								paramTypesMatch = false;
								break;
							}
						}

						if (paramTypesMatch)
							// this function can be replaced by this candidate
							functionNode = functionNodeCandidate;
					}
				}
			}

			List<AASTNode> envParams = new ArrayList<AASTNode>();
			// iterates over the parameter list to look for environment variables
			for (AASTNode parameterNode : parameterNodes)
				if (parameterNode.hasAttr(NodeAttr.IS_FUNCTION_ENV_PARAM))
					envParams.add(parameterNode);

			// getting the parameters
			paramsTranslatedArray = paramsTranslated.split("\\" + PARAMS_SEPARATOR);

			// parameters I pass to the method
			String tSpanSymbol = paramsTranslatedArray[1];
			String y0Symbol = paramsTranslatedArray[2];

			String integrationOptionsSymbol = "NULL";
			if (params.size() > 3) {
				GType integrationOptsExpr = getExprGeneralized(params.get(3));
				if (GType.get(BType.STRUCT).equals(integrationOptsExpr))
					integrationOptionsSymbol = paramsTranslatedArray[3];
				else {
					// leave null, array given as integration options
					curApplyNode.compilationUnit()
							.addWarning(new TypeException(CErrorMessage.TODO_USER_WARN_PARAMETER_TYPE, curApplyNode,
									params.get(3).name(), integrationOptsExpr, GType.get(BType.STRUCT),
									fenum.getName()));
				}
			}
			// parameters nodes
			AASTNode tSpanNode = params.get(1);
			AASTNode y0Node = params.get(2);

			// dimensions as string
			int tspanDims = ((DimensionType) getExprGeneralized(tSpanNode)).dims().length;
			int y0Dims = ((DimensionType) getExprGeneralized(y0Node)).dims().length;
			List<String> tspanDimsAsStrings = new ArrayList<String>(tspanDims);
			for (int i = 1; i <= tspanDims; ++i)
				tspanDimsAsStrings.add(TypeUtils.matrixDimName(tSpanSymbol, i));
			List<String> y0DimsAsStrings = new ArrayList<String>(y0Dims);
			for (int i = 1; i <= y0Dims; ++i)
				y0DimsAsStrings.add(TypeUtils.matrixDimName(y0Symbol, i));

			// checks whether the tspan is a slice, an array with just tin and tend or a
			// real array -with more than two elements-
			Boolean slicedTimes = false;
			Boolean userSpecifiedTimes = false;
			GType tSpanExpr = getExprGeneralized(tSpanNode);
			IntType[] tSpanRealDims = TypeUtils.realDims(((DimensionType) tSpanExpr).dims());
			if (getExprGeneralized(tSpanNode) instanceof SliceType) {
				slicedTimes = true;
			} else if (tSpanExpr.isCastableToMatrix() && tSpanRealDims.length == 1
			// if == 2 we should use sd_sun_eval and the defaultStepNumber
					&& (!tSpanRealDims[0].hasIntValue() || tSpanRealDims[0].valueAsInt() > 2)) {
				userSpecifiedTimes = true;
			}

			// Writes the environment variable -allowed in Matlab and R but not in C- in the
			// user data struct, with the name slightly modified in order to avoid contrasts
			String structUserDataName = "NULL";
			String structUserDataPointer = "NULL";
			// iterates over the function parameters to find the environment variables
			// and include also extra parameters to ode call
			boolean extra_ode_params = params.size() > 4;
			List<AASTNode> datastructEntries = new ArrayList<AASTNode>();
			if (!envParams.isEmpty() || extra_ode_params) {
				// name of the userData struct
				// declares the user data struct
				structUserDataName = USER_DATA_VARIABLE_PREFIX + (++user_data_variable_number);
				structUserDataPointer = "&" + structUserDataName;
				if (!envParams.isEmpty())
					datastructEntries.addAll(envParams);
				List<AASTNode> extra_params = null;
				if (extra_ode_params) {
					extra_params = new ArrayList<AASTNode>();
					for (AASTNode p : params.subList(4, params.size()))
						extra_params.add(getExprNode(p));
					datastructEntries.addAll(extra_params);
				}
				createUserDataStruct(datastructEntries);
				// initialize the struct -allocates the memory-
				bufferStmt.append(USER_DATA_STRUCT_PREFIX).append(user_data_struct_number).append(" ")
						.append(USER_DATA_VARIABLE_PREFIX).append(user_data_variable_number).append(";").append(NL);

				if (!envParams.isEmpty())
					for (AASTNode environmentVariable : envParams) {
						String name = environmentVariable.symbol();
						GType eexpr = environmentVariable.expr();
						bufferStmt.append(USER_DATA_VARIABLE_PREFIX).append(user_data_variable_number)
								.append(STRUCT_ACCESS).append(name).append("=").append(name).append(";").append(NL);

						if (eexpr != null && eexpr.equals(BType.STRUCT) && eexpr.isInput()) {
							// add also field with input structure this structure depends on
							bufferStmt.append(USER_DATA_VARIABLE_PREFIX).append(user_data_variable_number)
									.append(STRUCT_ACCESS).append(eexpr.inputName()).append("=")
									.append(eexpr.inputName()).append(";").append(NL);
						}
					}

				if (extra_ode_params)
					for (AASTNode extParam : extra_params) {
						String name = extParam.symbol();

						bufferStmt.append(USER_DATA_VARIABLE_PREFIX).append(user_data_variable_number)
								.append(STRUCT_ACCESS).append(name).append("=").append(name).append(";").append(NL);
					}

			} else {
				++user_data_variable_number;
			}

			// create a wrapper for the anonimous function so that it can be handled by
			// Sundials
			String wrappedFunctionName = wrapForSundials(origFunctionNode.symbol(), functionNode, params.get(2),
					structUserDataName, datastructEntries);

			// here you should call just the wrapping functions -different depending on how
			// times are specified-
			// gets the type of y-values -depending on this I have to call different
			// sun_init functions-
			String tspan_type;
			GType basicTspanType = ((DimensionType) getExprGeneralized(tSpanNode)).of();
			if (basicTspanType.equals(BType.INT)) {
				tspan_type = "i";
			} else if (basicTspanType.equals(BType.SCALAR)) {
				tspan_type = "d";
			} else
				throw new UndefinedTranslationException(CErrorMessage.TODO_INTERNAL_SLICE_TYPE, curApplyNode,
						basicTspanType);

			// if we have basic tspan (sliced) with int
			if (tspan_type.equals("i") && !userSpecifiedTimes) {
				// call siOde45
				// sdOde45(double *result, int num_row, int num_col, int (*f)(realtype t,
				// N_Vector y, N_Vector ydot, void *user_data), double *y, int y_len, int start,
				// int step, int end, int times_length, INTEGRATION_OPTS* opts, void *userdata)
				// bufferStmt.append("sdOde45(").append("").append(NL);
			} else if (tspan_type.equals("d") && !userSpecifiedTimes) {
				// call sdOde45
				// sdOde45(double *result, int num_row, int num_col, int (*f)(realtype t,
				// N_Vector y, N_Vector ydot, void *user_data), double *y, int y_len, double
				// start, double step, double end, int times_length, INTEGRATION_OPTS* opts,
				// void *userdata)
				// bufferStmt.append("svOde45(").append("").append(NL);
			} else if (userSpecifiedTimes) {
				// call vOde45
				// vOde45(double *result, int num_row, int num_col, int (*f)(realtype t,
				// N_Vector y, N_Vector ydot, void *user_data), double *y, int y_len,
				// INTEGRATION_TIMES *times_s, INTEGRATION_OPTS* opts, void *userdata)
				// bufferStmt.append("vOde45(").append("").append(NL);
			}

			GType typeOfY0 = ((DimensionType) getExprGeneralized(y0Node)).of();
			String suffix = null;
			if (typeOfY0.equals(BType.INT)) {
				suffix = "i";
			} else if (typeOfY0.equals(BType.SCALAR)) {
				suffix = "d";
			} else {
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_MATRIX_ELEMENT_TYPE, curApplyNode,
						typeOfY0);
			}

			// initializes and populates the SUN_INIT structure
			String sundialInitializerName = SUN_INIT_PREFIX.concat(String.valueOf(sun_init_number++));
			// STATIC because it's never freed, this allows reuse/reinit of memory for speed
			// efficiency
			bufferStmt.append("static __thread SUN_INIT *").append(sundialInitializerName).append(" = NULL;")
					.append(NL);

			// gets start and end time
			String startTime = null;
			if (slicedTimes) {
				startTime = tSpanSymbol + STRUCT_ACCESS + "start";
			} else
				startTime = getArrayContent(getPointerSymbol(tSpanSymbol), "0", false);

			String stepTime = tSpanSymbol + STRUCT_ACCESS + "step";

			String endTime = null;
			if (slicedTimes) {
				endTime = tSpanSymbol + STRUCT_ACCESS + "end";
			} else
				endTime = getArrayContent(getPointerSymbol(tSpanSymbol),
						tspanDimsAsStrings.get(tspanDimsAsStrings.size() - 1) + "-1", false);

			// here you should solve the ODE, the first thing you have to do is
			// to call
			// the ODE init, with which you can initialize the solver

			String yLength = String.join("*", y0DimsAsStrings);

			bufferStmt.append(sundialInitializerName).append(" = sun_init_" + suffix + "(")
					.append(sundialInitializerName).append(", ").append(wrappedFunctionName).append(", ")
					.append(getPointerSymbol(y0Symbol)).append(", ").append(yLength).append(", ").append(startTime)
					.append(", ").append(endTime).append(", ").append(integrationOptionsSymbol).append(",")
					.append(structUserDataPointer).append(")").append(";").append(NL);

			String vectorSymbol = Symbols.getSymbolFromType(fenum.getName(), BType.STRUCT);
			// build, if necessary, the integration time array
			String integration_times_struct = vectorSymbol + "_times_s";

			if (slicedTimes) {
				bufferStmt.append("INTEGRATION_TIMES *").append(integration_times_struct)
						.append(" = v" + tspan_type + "SlicedIntegrationTimes(").append(startTime).append(",")
						.append(stepTime).append(",").append(endTime).append(");").append(NL);
			} else {
				bufferStmt.append("INTEGRATION_TIMES *").append(integration_times_struct)
						.append(" = v" + tspan_type + "IntegrationTimes(").append(getPointerSymbol(tSpanSymbol))
						.append(",");
				if (tspanDimsAsStrings.size() == 1) {
					bufferStmt.append(tspanDimsAsStrings.get(0));
				} else if (tspanDimsAsStrings.size() == 2) {
					bufferStmt.append("max(").append(tspanDimsAsStrings.get(0)).append(", ")
							.append(tspanDimsAsStrings.get(1)).append(")");
				} else
					throw new TypeException(CErrorMessage.INTERNAL_SLICE_WITH_MORE_THAN_2_DIMENSIONS, tSpanNode,
							tspanDimsAsStrings.size());
				bufferStmt.append(",").append(DEFAULT_NUMBER_STEPS).append(");").append(NL);
			}

			// build the output matrix
			// column number = number of function of the system plus 2 (to store
			// the iteration number and the time)
			String num_col = vectorSymbol + "_num_col";
			VarVisibility context = VarVisibility.LOCAL_TO_BLOCK.context(curApplyNode);
			createVariable(GType.get(BType.INT), num_col, NodeType.ID, null, false, context);
			bufferStmt.append(num_col).append("=").append(yLength).append("+1").append(";").append(NL);
			// number of times we've to integrate on
			String num_row = vectorSymbol + "_num_row";
			createVariable(GType.get(BType.INT), num_row, NodeType.ID, null, false, context);
			bufferStmt.append(num_row).append("=").append(integration_times_struct).append("->len").append(";")
					.append("\n");

			// lookup in the tree to find the output matrix
			List<AASTNode> resultMatrixNames = new ArrayList<AASTNode>(2);
			IntType oneDimensionType = (IntType) GType.get(BType.INT, 1).name("1");
			IntType firstDimensionType = (IntType) GType.get(BType.INT).name(num_row);
			IntType secondDimensionType = (IntType) GType.get(BType.INT).name(num_col);
			IntType[] dimensionTypes = { firstDimensionType, secondDimensionType };
			if (!curApplyNode.parentExists(NodeType.ASSIGN) && !curApplyNode.parentExists(NodeType.GASSIGN)) {
				String mname = curApplyNode.symbol();
				// intialization of the result structure
				AASTNode fake = new AASTNode(curApplyNode.compilationUnit(), NodeType.ID, mname);
				fake.symbol(mname);
				fake.expr(GType.get(BType.MATRIX, mname, GType.get(BType.SCALAR), dimensionTypes));
				createMatrixRef(mname, "double", null, dimensionTypes, false, false, false, context, false, null,
						false);
				resultMatrixNames.add(fake);
			} else if (curApplyNode.parentExists(NodeType.ASSIGN) || curApplyNode.parentExists(NodeType.GASSIGN)) {
				AASTNode matrix;
				if (curApplyNode.parentExists(NodeType.ASSIGN)) {
					matrix = curApplyNode.parent(NodeType.ASSIGN).childs(NodeType.LHS).get(0);
					// don't assign, already done here
					curApplyNode.parent(NodeType.ASSIGN).attr(CAttr.TRANSLATE, false);
				} else {
					matrix = curApplyNode.parent(NodeType.GASSIGN).childs(NodeType.LHS).get(0);
					// don't assign, already done here
					curApplyNode.parent(NodeType.GASSIGN).attr(CAttr.TRANSLATE, false);
				}
				List<AASTNode> vectors = matrix.childs();
				if (vectors.size() > 1)
					throw new UndefinedTranslationException(CErrorMessage.INTERNAL_ASSIGN_TO_MATRIX_WITH_MULTIPLE_ROWS,
							matrix);
				if (NodeType.MATRIX.equals(matrix.type())) {
					AASTNode vector = vectors.get(0);
					for (AASTNode assignMatrixExpr : vector.childs()) {
						AASTNode assignMatrix = getExprNode(assignMatrixExpr);
						// multiple outputs
						resultMatrixNames.add(assignMatrix);
					}
				} else {
					AASTNode idNode = getExprNode(matrix);
					resultMatrixNames.add(idNode);
				}

			}
			int n_matrices = resultMatrixNames.size();
			String[] matrixNames = new String[resultMatrixNames.size()];
			for (int i = 0; i < n_matrices; ++i) {
				AASTNode resultMatrixName = resultMatrixNames.get(i);
				matrixNames[i] = resultMatrixName.symbol() + STRUCT_ACCESS + "matrix";
				if (i == n_matrices - 1) {
					String minus = "";
					if (n_matrices > 1)
						minus = " - " + Integer.toString(n_matrices - 1);
					ensureMatrixMemory(bufferStmt, resultMatrixName, new IntType[] { firstDimensionType,
							(IntType) GType.get(BType.INT).name(secondDimensionType.valueAsString() + minus) });
				} else
					ensureMatrixMemory(bufferStmt, resultMatrixName, new IntType[] {
							(IntType) GType.get(firstDimensionType), (IntType) GType.get(oneDimensionType) });

			}

			// transpose, always true in the case of a matlab frontend!
			String transpose = "0";

			// call the sun_eval, that actually performs the numeric integration
			// sun_eval(SUN_INIT* s, realtype *dest, int dest_rows, int
			// dest_columns, int tighten, realtype tinc, int iteration_number,
			// realtype max_step, int transpose)
			bufferStmt.append("sd_sun_eval(").append(sundialInitializerName).append(", ").append(num_row).append(", ")
					.append(num_col).append(", ").append(integration_times_struct).append(", ").append(transpose)
					.append(", ").append(Integer.toString(matrixNames.length)).append(", ")
					.append(String.join(", ", matrixNames)).append(")").append(";").append(NL);

			// deallocate integration_times_struct
			bufferStmt.append("qspcc_free(").append(integration_times_struct).append(");").append(NL);
			// deallocate the sundial-used memory with SunFree
			bufferStmt.append(sundialInitializerName).append(" = sun_free(").append(sundialInitializerName).append(")")
					.append(";").append(NL);

			// assign the output node value
			outNode = getOutputNode(outSymbol);
			outNodeName = getNodeSymbol(outNode);

			// cast to double2dMatrix because it's the return type
			// of the ode23 translator method
			// bufferStmt.append(outNodeName+".matrix=(double
			// *)"+resultMatrixName+".matrix;").append(NL);
			// bufferStmt.append(outNodeName+".dim1="+resultMatrixName+".dim1;").append(NL);
			// bufferStmt.append(outNodeName+".dim2="+resultMatrixName+".dim2;").append(NL);

			// write on the output file
			newComment(outSymbol);
			newTranslation(curApplyNode, bufferStmt.toString());

			return outNodeName;
		case "interp1":
			// interp1 is a matlab function that can have 4 parameters

			bufferStmt = new StringBuffer();
			String[] paramsName = paramsTranslated.split("\\" + PARAMS_SEPARATOR);

			// mandatory parameters
			String originalXVectorName = paramsName[0];
			String originalYVectorName = paramsName[1];
			String xToBeInterpolateName = paramsName[2];
			String interpolationType = "Linear";
			if (paramsName.length > 3) {
				String method = paramsName[3].substring(1, paramsName[3].length() - 1);
				interpolationType = Character.toUpperCase(method.charAt(0)) + method.toLowerCase().substring(1);
			}
			// others parameters haven't been taken into account so far
			AASTNode xVectorNode = params.get(0);
			AASTNode yVectorNode = params.get(1);
			AASTNode xToBeInterpoledNode = params.get(2);
			GType xToBeInterpolatedNodeType = getExprGeneralized(xToBeInterpoledNode);
			GType xVectorType = getExprGeneralized(xVectorNode);

			// assign the output node value
			outNode = getOutputNode(outSymbol);
			outNodeType = getExprGeneralized(outNode);
			outNodeName = outNode.symbol();
			DimensionType yVectorType = (DimensionType) getExprGeneralized(yVectorNode);
			IntType[] yVectorDims = yVectorType.dims();

			if (!(xVectorType instanceof DimensionType) || !((DimensionType) xVectorType).of().equals(BType.SCALAR))
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, "interp1",
						"sample points '" + xVectorNode.code() + "' should be specified as a vector of scalars");

			List<String> originalXDimensionsASString = nodeDimsToStr(xVectorNode, ((DimensionType) xVectorType).dims(),
					false);
			List<String> originalYDimensionsASString = nodeDimsToStr(yVectorNode, yVectorDims, false);

			boolean onedMatrix = TypeUtils.realDims(yVectorDims).length == 1;
			if (onedMatrix && xToBeInterpolatedNodeType.equals(BType.SCALAR))
				bufferStmt.append(outNodeName).append("=").append("dInterp(").append(originalXVectorName)
						.append(STRUCT_ACCESS).append("matrix, ").append(String.join("+", originalXDimensionsASString))
						.append(",").append(originalYVectorName).append(STRUCT_ACCESS).append("matrix, ")
						.append(String.join("+", originalYDimensionsASString)).append(", ").append(xToBeInterpolateName)
						.append(");").append(NL);
			else {
				List<String> xToBeInterpoledASString = null;

				if (xToBeInterpolatedNodeType instanceof DimensionType) {
					if (!((DimensionType) xToBeInterpolatedNodeType).of().equals(BType.SCALAR))
						throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
								curApplyNode, "interp1", "query points '" + xToBeInterpoledNode.code()
										+ "' should be specified as a vector of scalars");
					xToBeInterpoledASString = nodeDimsToStr(xToBeInterpoledNode,
							((DimensionType) xToBeInterpolatedNodeType).dims(), false);
				} else {
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
							curApplyNode, "interp1", "query points '" + xToBeInterpoledNode.code()
									+ "' should be specified as a vector of scalars");
				}
				// be sure vector is allocated
				ensureMatrixMemory(bufferStmt, outSymbol, ((DimensionType) outNodeType).dims());
				String fun = "v" + exprTypeToCType(yVectorType) + "Interp" + interpolationType;
				// do it
				bufferStmt.append(fun).append("(").append(outNodeName).append(", ").append(originalXVectorName)
						.append(STRUCT_ACCESS).append("matrix, ").append(String.join("+", originalXDimensionsASString))
						.append(",").append(originalYVectorName).append(", ").append(xToBeInterpolateName)
						.append(STRUCT_ACCESS).append("matrix, ").append(String.join("+", xToBeInterpoledASString))
						.append(");").append(NL);
			}
			// write on the output file b
			newComment(outSymbol);
			newTranslation(curApplyNode, bufferStmt.toString());

			return outNodeName;
		case "figure":
		case "subplot":
		case "semilogx":
		case "errorbar":
		case "legend":
		case "ylabel":
		case "xlabel":
		case "ylim":
		case "xlim":
		case "title":
		case "plot":
			// emit warning for unimplemented functions
			curApplyNode.compilationUnit().addWarning(
					new UndefinedTranslationException(CErrorMessage.WARN_VOID_IMPLEMENTATION, curApplyNode));
		case "clearvars":
			// there is no use in doing anything
			// in matlab it simply set to a default value
			// the variable value
			newComment(curApplyNode);
			return null;
		default:
			// ask user to implement this function.
			// provide a generic implementation template
			// get output nodes
			/*
			 * if (curApplyNode.parentExists(NodeType.ASSIGN) ||
			 * curApplyNode.parentExists(NodeType.GASSIGN)) { AASTNode matrix; if
			 * (curApplyNode.parentExists(NodeType.ASSIGN)) { matrix =
			 * curApplyNode.parent(NodeType.ASSIGN).childs(NodeType.LHS).get(0); // don't
			 * assign, already done here
			 * curApplyNode.parent(NodeType.ASSIGN).attr(CAttr.TRANSLATE, false); } else {
			 * matrix = curApplyNode.parent(NodeType.GASSIGN).childs(NodeType.LHS).get(0);
			 * // don't assign, already done here
			 * curApplyNode.parent(NodeType.GASSIGN).attr(CAttr.TRANSLATE, false); }
			 * List<AASTNode> vectors = matrix.childs(); if (vectors.size() > 1) throw new
			 * UndefinedTranslationException(CErrorMessage.
			 * ASSIGN_TO_MATRIX_WITH_MULTIPLE_ROWS, matrix); if
			 * (NodeType.MATRIX.equals(matrix.type())) { AASTNode vector = vectors.get(0);
			 * for (AASTNode assignMatrixExpr : vector.childs()) { AASTNode assignMatrix =
			 * getExprNode(assignMatrixExpr); // multiple outputs
			 * resultMatrixNames.add(assignMatrix); } } else { AASTNode idNode =
			 * getExprNode(matrix); resultMatrixNames.add(idNode); } }
			 */
			throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION, curApplyNode, fenum);
		}

	}

	/**
	 * inject initialization code for random number generator
	 */
	protected void initializeRandomNumberGenerator() {
		if (isMainCompilationUnit())
			initRandNumbers = true;
		else {
			C mainCompilationUnit = (C) getMainCompilationUnit();
			mainCompilationUnit.initializeRandomNumberGenerator();
		}
	}

	private String printType(AASTNode curApplyNode, GType expr, String paramsTranslated)
			throws UndefinedTranslationException {
		return printType(curApplyNode, expr, paramsTranslated, new LinkedList<String>());
	}

	private String printType(AASTNode curApplyNode, GType expr, String paramsTranslated, LinkedList<String> depth)
			throws UndefinedTranslationException {
		List<String> tabsList = new LinkedList<String>();
		for (int s = 0; s < depth.size(); ++s) {
			tabsList.add(TAB);
		}
		String tabs = String.join("", tabsList);

		if (expr.type().equals(BType.FUNCTION)) {
			// take first return value..
			expr = ((FunctionType) expr).outputs().get(0);
		}

		String fullVariableName = paramsTranslated;
		// remember the position in the nested structure where we are
		if (!depth.isEmpty()) {
			fullVariableName = String.join(STRUCT_ACCESS, depth) + STRUCT_ACCESS + paramsTranslated;
		}

		String toBeWritten;
		switch (expr.type()) {
		case INT:
		case BOOL:
			toBeWritten = "printf(\"%d\\n\", " + fullVariableName + ");";
			return toBeWritten;
		case STRING:
			toBeWritten = "printf(\"%s\\n\", " + fullVariableName + ");";
			return toBeWritten;
		case SCALAR:
			toBeWritten = "printf(\"%.17e\\n\", " + fullVariableName + ");";
			return toBeWritten;
		case MATRIX_ACCESS_SLICE:
			String printfFormat = null;
			SliceType sltype = (SliceType) expr;
			GType stype = sltype.of();
			if (stype.equals(BType.SCALAR))
				printfFormat = "%.17e";
			else if (stype.equals(BType.INT))
				printfFormat = "%d";
			else
				throw new UndefinedTranslationException(CErrorMessage.TODO_INTERNAL_SLICE_TYPE, curApplyNode, stype);

			toBeWritten = "printf(\"(slice){ start = " + printfFormat + ", step = " + printfFormat + ", end = "
					+ printfFormat + ", linear = %d }\\n\", " + fullVariableName + STRUCT_ACCESS + "start" + ", "
					+ fullVariableName + STRUCT_ACCESS + "step" + ", " + fullVariableName + STRUCT_ACCESS + "end" + ", "
					+ fullVariableName + STRUCT_ACCESS + "linear" + ");";

			return toBeWritten;

		case STRUCT:
			StructType struct = (StructType) expr;
			// the name of the variable not the name of the struct type
			String structName = paramsTranslated;
			Map<String, Tuple<SymbolType, List<String>>> map = new HashMap<String, Tuple<SymbolType, List<String>>>();
			IntType[] curDimensions = null;
			String curMatrixName = null;
			StringBuffer globalTranslation = new StringBuffer();
			List<String> hashKeys = new LinkedList<String>();
			hashKeys.add("#");
			for (int s = 0; s < depth.size(); ++s) {
				hashKeys.add("#");
			}
			String hashKey = String.join("", hashKeys);
			if (!depth.isEmpty())
				globalTranslation.append("printf(\"\\n" + tabs + "\");").append(NL);
			globalTranslation.append("printf(\"" + hashKey + " Structure '" + structName + "'\\n\");").append(NL);
			// needed for next round of sub-fields.. push current struct
			depth.add(structName);
			Iterator<Tuple<List<GType>, String>> fields = struct.iterFields();
			for (int i = 0; fields.hasNext(); ++i) {
				Tuple<List<GType>, String> field = fields.next();
				// full struct access
				List<GType> ith_rhs_type = field.first();

				globalTranslation.append("printf(\"" + tabs + TAB + " Field " + field.second() + ": \");").append(NL);
				if (ith_rhs_type == null || ith_rhs_type.size() == 0)
					globalTranslation.append("printf(\"" + tabs + "Field " + field.second() + " of " + structName
							+ " has unknown type, skipping\\n\");").append(NL);
				else {
					GType curType = ith_rhs_type.get(0);
					// add current struct name, needed for low-level recursive call
					globalTranslation.append(printType(curApplyNode, curType, field.second(), depth)).append(NL);
				}
			}
			// once we completed the list of fields of this structure, remove from depth
			// field lists
			depth.removeLast();

			if (!depth.isEmpty())
				globalTranslation.append("printf(\"" + tabs + "\");").append(NL);
			globalTranslation.append("printf(\"" + hashKey + " Structure '" + structName + "' end\\n\");").append(NL);
			return globalTranslation.toString();
		case MATRIX:
			String applyNodeString = curApplyNode.name();
			MatrixType mtype = (MatrixType) expr;
			String matrixSymbol = fullVariableName + STRUCT_ACCESS + "matrix";
			String poly_basis = fullVariableName + STRUCT_ACCESS + "poly_basis";
			IntType[] matrixDim = mtype.dims();
			stype = mtype.of();

			if (GType.get(BType.INT).equals(stype) || GType.get(BType.BOOL).equals(stype)
					|| GType.get(BType.SCALAR).equals(stype)) {
				String printf = "";
				switch (stype.type()) {
				case BOOL:
				case INT:
					printf = "(void (*)(FILE *, char *)) printInt";
					break;
				case SCALAR:
					printf = "(void (*)(FILE *, char *)) printScalar";
					break;
				default:
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
							curApplyNode, "disp", paramsTranslated);
				}
				String actualParams = matrixSymbol + ", " + poly_basis + ", sizeof(" + symbolType2Ctype(stype) + ") , "
						+ printf + ", " + matrixDim.length;
				for (int w = 1; w <= matrixDim.length; ++w) {
					actualParams += ", " + TypeUtils.matrixDimName(fullVariableName, w);
				}
				// print whole matrix
				toBeWritten = "printMatrix(" + actualParams + ");";
				return toBeWritten;
			} else
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT,
						curApplyNode, "disp", mtype, stype.type());
		case VOID:
			throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT, curApplyNode,
					"disp", "VOID");
		default:
			throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_FRONTEND_FUNCTION_ARGUMENT, curApplyNode,
					"disp", fullVariableName);
		}
	}

	/**
	 * convert the param0Name, representing a slice, into an equivalent
	 * explicit-matrix. writes on current translation buffer through sb.
	 * 
	 * @param sb
	 * @param param0Name
	 * @param param0Type
	 * @return
	 * @throws TypeException
	 * @throws UndefinedTranslationException
	 */
	protected String castSliceTypeToMatrixType(StringBuffer sb, AASTNode curNode, String param0Name,
			DimensionType param0Type) throws UndefinedTranslationException, TypeException {
		String newParamName = param0Name + "_asMatrix";
		createVariable(GType.get(BType.MATRIX, newParamName, param0Type.of(), param0Type.dims()), newParamName,
				NodeType.APPLY, null, false, VarVisibility.LOCAL_TO_BLOCK.context(curNode));
		// execution on sequence and sequence is not supported
		ensureMatrixMemory(sb, curNode, newParamName, ((DimensionType) param0Type).dims());

		char char_to_sub;
		if (param0Type.of().isLessGenericThanInt()) {
			char_to_sub = 'i';
		} else {
			char_to_sub = 'd';
		}
		// int or bool
		String sequenceToVectorFun = SEQUENCE_TO_MATRIX_FUN.replace('?', char_to_sub);

		String linearSequenceToVectorFun = sequenceToVectorFun.replace("!", "");
		String logSequenceToVectorFun = sequenceToVectorFun.replace("!", "Log");

		StringBuffer paramsBuf = new StringBuffer();
		paramsBuf.append("(").append(newParamName).append(STRUCT_ACCESS).append("matrix").append(", ")
				.append(param0Name).append(STRUCT_ACCESS).append("start, ").append(param0Name).append(STRUCT_ACCESS)
				.append("step, ").append(param0Name).append(STRUCT_ACCESS).append("end").append(");").append(NL);

		if (param0Type.of().isLessGenericThanInt()) {
			// neglect logspace for integer slices
			sb.append(linearSequenceToVectorFun).append(paramsBuf.toString());
		} else {
			// first convert to normal matrix (TAKING CARE OF LOG/LINEAR SPACE)
			sb.append("if(").append(param0Name).append(STRUCT_ACCESS).append("linear").append(")").append(NL);
			sb.append(TAB).append(linearSequenceToVectorFun).append(paramsBuf.toString());
			sb.append("else").append(NL);
			sb.append(TAB).append(logSequenceToVectorFun).append(paramsBuf.toString());
		}
		return newParamName;
	}

	protected boolean ensureMatrixMemory(StringBuffer buffer, AASTNode matrixToBeAllocated,
			IntType[] customizeDimensions) throws TypeException, UndefinedTranslationException {
		return ensureMatrixMemory(buffer, matrixToBeAllocated, null, customizeDimensions);
	}

	protected boolean ensureMatrixMemory(StringBuffer buffer, AASTNode matrixToBeAllocated, String matrixName,
			IntType[] customizeDimensions) throws TypeException, UndefinedTranslationException {
		return ensureMatrixMemory(buffer, matrixToBeAllocated, matrixName, customizeDimensions, null);
	}

	protected boolean ensureMatrixMemory(StringBuffer buffer, AASTNode matrixToBeAllocated, String matrixName,
			IntType[] customizeDimensions, String sparseDimToAllocate)
			throws TypeException, UndefinedTranslationException {
		GType type = getExprGeneralized(matrixToBeAllocated);
		if (!type.equals(BType.MATRIX))
			// do nothing, this is not a matrix
			return false;

		MatrixType matrixType = (MatrixType) type;
		String[] cdims = new String[customizeDimensions.length];
		for (int i = 0; i < customizeDimensions.length; ++i)
			cdims[i] = customizeDimensions[i].valueAsString();
		return ensureMatrixMemory(buffer, matrixToBeAllocated, matrixName, matrixType, cdims, false,
				sparseDimToAllocate);
	}

	protected boolean ensureMatrixMemory(StringBuffer buffer, AASTNode matrixToBeAllocated, String matrixName,
			IntType[] customizeDimensions, boolean calloc) throws TypeException, UndefinedTranslationException {
		GType type = getExprGeneralized(matrixToBeAllocated);
		if (!type.equals(BType.MATRIX))
			// do nothing, this is not a matrix
			return false;

		MatrixType matrixType = (MatrixType) type;
		String[] cdims = new String[customizeDimensions.length];
		for (int i = 0; i < customizeDimensions.length; ++i)
			cdims[i] = customizeDimensions[i].valueAsString();
		return ensureMatrixMemory(buffer, matrixToBeAllocated, matrixName, matrixType, cdims, calloc);
	}

	protected boolean ensureMatrixMemory(StringBuffer buffer, AASTNode matrixToBeAllocated,
			String[] customizeDimensions) throws TypeException, UndefinedTranslationException {
		return ensureMatrixMemory(buffer, matrixToBeAllocated, null, customizeDimensions);
	}

	protected boolean ensureMatrixMemory(StringBuffer buffer, AASTNode matrixToBeAllocated, String matrixName,
			String[] customizeDimensions) throws TypeException, UndefinedTranslationException {
		GType type = getExprGeneralized(matrixToBeAllocated);
		if (!type.equals(BType.MATRIX))
			// do nothing, this is not a matrix
			return false;
		MatrixType matrixType = (MatrixType) type;
		return ensureMatrixMemory(buffer, matrixToBeAllocated, matrixName, matrixType, customizeDimensions);
	}

	// allocates the matrix it hasn't been done yet
	// if dimension string are passed to the function, takes this one instead of the
	// original dimension
	protected boolean ensureMatrixMemory(StringBuffer buffer, AASTNode matrixNode, String matrixSymbol,
			MatrixType matrixType, String[] customizeDimensions) throws TypeException, UndefinedTranslationException {
		return ensureMatrixMemory(buffer, matrixNode, matrixSymbol, matrixType, customizeDimensions, false, false,
				false, (matrixType.isSparse()) ? "-1" : null);
	}

	protected boolean ensureMatrixMemory(StringBuffer buffer, AASTNode matrixNode, String matrixSymbol,
			MatrixType matrixType, String[] customizeDimensions, boolean calloc)
			throws TypeException, UndefinedTranslationException {
		return ensureMatrixMemory(buffer, matrixNode, matrixSymbol, matrixType, customizeDimensions, calloc, null);
	}

	protected boolean ensureMatrixMemory(StringBuffer buffer, AASTNode matrixNode, String matrixSymbol,
			MatrixType matrixType, String[] customizeDimensions, boolean calloc, String sparseDimToAllocate)
			throws TypeException, UndefinedTranslationException {
		return ensureMatrixMemory(buffer, matrixNode, matrixSymbol, matrixType, customizeDimensions, false, calloc,
				false, (sparseDimToAllocate == null && matrixType.isSparse()) ? "-1" : sparseDimToAllocate);
	}

	/**
	 * 
	 * @param buffer
	 * @param matrixNode
	 * @param matrixSymbol
	 * @param matrixType:          type of output matrix
	 * @param customizeDimensions: stringified list of dimensions out matrix should
	 *                             have
	 * @param checkOnly:           when true, don't update memory/dimensions if not
	 *                             needed
	 * @throws TypeException
	 * @throws UndefinedTranslationException
	 */
	protected boolean ensureMatrixMemory(StringBuffer buffer, AASTNode matrixNode, String matrixSymbol,
			MatrixType matrixType, String[] customizeDimensions, boolean checkOnly, boolean calloc, boolean skip_memcpy,
			String sparseDimToAllocate) throws TypeException, UndefinedTranslationException {

		Boolean alreadyBeenAllocated = isStaticAllocation(matrixType);
		boolean flatCheck = false;
		// get the name of the variable!
		String matrixName;
		if (matrixSymbol == null)
			matrixName = matrixNode.symbol();
		else
			matrixName = matrixSymbol;

		boolean flatMatrixAccess = false;
		// if the number of dimensions is not sufficient, throw an exception
		if (customizeDimensions.length != matrixType.dims().length) {
			// remove ones
			List<String> cd = new ArrayList<>();
			for (String cdel : customizeDimensions)
				if (!"1".equals(cdel))
					cd.add(cdel);

			IntType[] rdims = TypeUtils.realDims(matrixType.dims(), 2);
			if (cd.size() == matrixType.dims().length)
				customizeDimensions = cd.toArray(new String[cd.size()]);
			// if removing ones they are equal
			else if (customizeDimensions.length == rdims.length) {
				// add missing ones
				String[] newDimensions = new String[matrixType.dims().length];
				for (int i = 0, j = 0; i < matrixType.dims().length; ++i) {
					IntType dim = matrixType.dims()[i];
					if (dim.hasIntValue() && dim.valueAsInt().equals(1))
						newDimensions[i] = "1";
					else
						newDimensions[i] = customizeDimensions[j++];

				}
				customizeDimensions = newDimensions;
				/*
				 * } else if (customizeDimensions.length < rdims.length) { // if not equal but
				 * realdims are more than the one provided.. add extra "matrixType.dimN to fill
				 * the missing ones String[] newDimensions = new
				 * String[matrixType.dims().length]; int k = 0; for (String cdim :
				 * customizeDimensions) newDimensions[k++] = cdim; for (int i = k; i <
				 * rdims.length; ++i) newDimensions[i] = rdims[i].valueAsString();
				 * 
				 * customizeDimensions = newDimensions;
				 */
			} else {
				if (customizeDimensions.length == 1 && rdims.length > 1)
					flatMatrixAccess = true;

				flatCheck = flatMatrixAccess && checkOnly;
				if (!flatCheck) {
					logger.debug("Matrix shape changed from " + matrixType + " to " + customizeDimensions.length
							+ "D Matrix.");
					// add missing ones
					String[] newDimensions = new String[matrixType.dims().length];
					for (int i = 0; i < matrixType.dims().length; ++i) {
						if (i < customizeDimensions.length)
							newDimensions[i] = customizeDimensions[i];
						else
							newDimensions[i] = "1";
					}
					customizeDimensions = newDimensions;
				} // else flat check index
			}
		}
		// subelements of sub-structures should always be allocated even if the number
		// of elements is known at compile time. maybe future optimization here.
		if (alreadyBeenAllocated && !checkOnly && matrixNode != null && !matrixNode.type().equals(NodeType.FIELDACCESS)
				&& !matrixNode.parentExists(NodeType.FIELDACCESS)) {
			createVariable(matrixNode);
			return false;
		}

		// centralized memory access with the ensureMemory methods
		String ensureMethodName = getMatrixCopyOnFunctionName(matrixNode, matrixType, checkOnly, calloc || skip_memcpy,
				flatMatrixAccess, sparseDimToAllocate != null);
		// cast as int just to be sure (sometimes it can be a scalar)
		String[] castedCustomizeDimensions = new String[customizeDimensions.length];
		for (int c = 0; c < customizeDimensions.length; ++c)
			castedCustomizeDimensions[c] = "(int) " + customizeDimensions[c];
		String dimensionsAsParameter = String.join(", ", castedCustomizeDimensions);

		buffer.append(ensureMethodName).append("(");
		if (flatMatrixAccess && checkOnly) {
			// debug information
			buffer.append("__FILE__,__LINE__,__func__,");
		}
		buffer.append(matrixType.dims().length).append(", ");
		if (!flatCheck && sparseDimToAllocate != null)
			buffer.append(sparseDimToAllocate).append(", ");
		buffer.append("&").append(matrixName).append(", ");
		buffer.append(dimensionsAsParameter).append(");").append(NL);

		if (calloc) {
			// add memset to ensure initialization to 0
			String type;
			if (matrixType.of().isCastableToBool())
				type = "int";
			else
				type = "double";

			buffer.append("memset(").append(matrixName + STRUCT_ACCESS + "matrix").append(", 0, ");
			buffer.append("((int) " + String.join("*", customizeDimensions)).append(")*sizeof(" + type + "));")
					.append(NL);
		}
		return true;
	}

	protected Boolean isStaticAllocation(MatrixType matrixType) {
		if (matrixType.isSparse())
			return false;
		Boolean alreadyBeenAllocated = true;
		for (ValuedType matrixDimensionType : matrixType.dims()) {
			if (!matrixDimensionType.hasValue()) {
				alreadyBeenAllocated = false;
			}
		}
		return alreadyBeenAllocated;
	}

	/*
	 * wraps the function in a sundial-compatible function. There could be more
	 * parameters in the original function, that are fixed (e.g. f(y,t,A,B) in which
	 * A e B are fixed in the environment) In such a case, if we fix the value of
	 * the exceeding parameter, we can nonetheless consider it as a function of the
	 * right parameter number. We store their value in the user data struct
	 */
	private String wrapForSundials(String origFunctionNodeName, AASTNode fun, AASTNode y0, String userDataStructName,
			List<AASTNode> extraParameterNodes) throws UndefinedTranslationException, TypeException {

		StringBuffer bufferToWriteOn = new StringBuffer();

		String wrapFunction = origFunctionNodeName + "_wrap_sundials";
		// avoid creating it twice
		if (wrapFunctions.contains(wrapFunction))
			return wrapFunction;

		wrapFunctions.add(wrapFunction);
		bufferToWriteOn.append("static int ").append(wrapFunction)
				.append("(realtype t, N_Vector y, N_Vector ydot, void *user_data){").append(NL);
		// call translated fun

		// the trick you have to adopt here is taking the values of environment
		// from the user data struct
		// but you have to call the function with the right parameters (we have
		// them in java)

		// cast t from realtype to double
		bufferToWriteOn.append(indentTabs()).append("double t_as_double = (double) t").append(";").append(NL);

		// transform the N_vector y in a matrix that can be handled
		// by the function fn (in this case, the N_Vector is implemented as a
		// NVECTOR_SERIAL
		bufferToWriteOn.append(indentTabs()).append("realtype* y_as_realtype_matrix = NV_DATA_S(y)").append(";")
				.append(NL);
		bufferToWriteOn.append(indentTabs()).append("double* y_as_matrix= (double*) y_as_realtype_matrix").append(";")
				.append(NL);

		bufferToWriteOn.append(indentTabs()).append("int dimension = NV_LENGTH_S(y)").append(";").append(NL);

		// get the size of x
		GType inputType = ((FunctionType) fun.expr()).inputs().get(1);

		GType typeOf = null;
		if (inputType instanceof DimensionType) {
			typeOf = ((DimensionType) inputType).of();
		} else {
			typeOf = inputType;
		}

		String stringType = exprTypeToCType(typeOf);

		int dims = -1;
		if (inputType.isCastableToScalar())
			dims = 1;
		else if (inputType.isCastableToMatrix())
			dims = ((DimensionType) inputType).dims().length;
		else
			throw new TypeException(CErrorMessage.INTERNAL_SUNDIALS_WRONG_INTEGRAND_FUNCTION_TYPE, fun, inputType);

		String structName = getMatrixTypeName(stringType, dims, false);
		bufferToWriteOn.append(indentTabs()).append(structName + " ").append("y_as_matrixDouble_2D;").append(NL);
		// populate the struct
		bufferToWriteOn.append(indentTabs()).append("y_as_matrixDouble_2D.matrix=y_as_matrix;").append(NL);
		String[] ones = new String[dims];
		ones[0] = "dimension";
		for (int j = 1; j < dims; j++) {
			ones[j] = "1";
			bufferToWriteOn.append(indentTabs()).append(TypeUtils.matrixDimName("y_as_matrixDouble_2D", j))
					.append("=1;").append(NL);
		}
		bufferToWriteOn.append(indentTabs()).append(TypeUtils.matrixDimName("y_as_matrixDouble_2D", dims))
				.append("=dimension;").append(NL);
		bufferToWriteOn.append(indentTabs()).append("int poly_basis_vals[" + dims + "] = {")
				.append(String.join(", ", ones)).append("};").append(NL);
		bufferToWriteOn.append(indentTabs())
				.append("y_as_matrixDouble_2D" + STRUCT_ACCESS + "poly_basis = poly_basis_vals;").append(NL);
		bufferToWriteOn.append(indentTabs()).append("y_as_matrixDouble_2D" + STRUCT_ACCESS + "__realsize = dimension;")
				.append(NL);

		if (userDataStructName != null && currentUserDataStructName != null) {
			bufferToWriteOn.append(indentTabs()).append(currentUserDataStructName).append(" *temp=").append("(")
					.append(currentUserDataStructName).append(" *) user_data;").append(NL);
		}

		// separates cases in which
		GType outputType = ((FunctionType) getExprGeneralized(fun)).outputs().get(0);
		if (outputType.type().equals(BType.SCALAR)) {

			// iterates over the env variables seeking for names and dimensions
			// calculate the output of fn
			// populate the N_Vector ydot with result
			String outputTypeOfAsCString = exprTypeToCType(outputType);

			bufferToWriteOn.append(indentTabs()).append(outputTypeOfAsCString).append(" s = ");
			bufferToWriteOn.append(fun.symbol() + "(");

			bufferToWriteOn.append("t_as_double, y_as_matrixDouble_2D");
			// write the env parameter
			Boolean firstEnvironmentParameter = true;
			for (AASTNode extraParam : extraParameterNodes) {
				if (firstEnvironmentParameter) {
					bufferToWriteOn.append(",");
					firstEnvironmentParameter = false;
				}
				bufferToWriteOn.append("temp->" + extraParam.symbol() + ",");
				GType pexpr = extraParam.expr();
				if (pexpr != null && pexpr.equals(BType.STRUCT) && pexpr.isInput()) {
					// add input symbol this structure depends on
					bufferToWriteOn.append(" temp->" + pexpr.inputName() + ",");
				}
			}

			// removes the last comma
			if (bufferToWriteOn.toString().endsWith(",")) {
				bufferToWriteOn.setLength(bufferToWriteOn.toString().length() - 1);
			}
			bufferToWriteOn.append(")").append(";").append(NL);

			bufferToWriteOn.append(indentTabs()).append("Ith(ydot, 0) = s").append(";").append(NL);
			// anonimous function now returns a struct matrixDouble
		} else if (outputType.type().equals(BType.MATRIX)) {
			// calculate the output of fn
			// populate the N_Vector ydot with result

			IntType[] outDims = ((DimensionType) outputType).dims();
			int dimensionNumber = outDims.length;
			GType outputTypeOf = ((DimensionType) outputType).of();
			String outputTypeOfAsCString = exprTypeToCType(outputTypeOf);

			// iterates over the env variables seeking for names and dimensions
			// calculate the output of fn
			// populate the N_Vector ydot with result
			structName = getMatrixTypeName(outputTypeOfAsCString, dimensionNumber, false);

			bufferToWriteOn.append(indentTabs()).append(structName + " s= ");
			bufferToWriteOn.append(fun.symbol() + "(");

			bufferToWriteOn.append("t_as_double, y_as_matrixDouble_2D");

			// write the env parameter
			AASTNode parameterListNode = fun.child(NodeType.PARAMETER_LIST);
			// iterates over the parameter list to look for environment variables
			List<AASTNode> parameterNodes = parameterListNode.childs();
			Boolean firstEnvironmentAParameter = true;
			for (AASTNode parameterNode : extraParameterNodes) {
				if (firstEnvironmentAParameter) {
					bufferToWriteOn.append(",");
					firstEnvironmentAParameter = false;
				}
				bufferToWriteOn.append("temp->" + parameterNode.symbol() + ",");
				GType pexpr = parameterNode.expr();
				if (pexpr != null && pexpr.isInput()) {
					// add input symbol this structure depends on
					bufferToWriteOn.append(" temp->" + pexpr.inputName() + ",");
				}
			}
			// removes the last comma
			if (bufferToWriteOn.toString().endsWith(",")) {
				bufferToWriteOn.setLength(bufferToWriteOn.length() - 1);
			}
			bufferToWriteOn.append(")").append(";").append(NL);

			List<String> dimensionAsStrings = new ArrayList<String>();
			for (int dimensionCounter = 0; dimensionCounter < dimensionNumber; dimensionCounter++) {
				dimensionAsStrings.add("s.dim" + (dimensionCounter + 1));
			}
			String totalDimensionAsString = Strings.join(dimensionAsStrings, '*');

			// CODE TO CHECK FOR NAN/INF VALUES THAT ARE WRONG FOR SURE
			bufferToWriteOn.append("#ifdef DEBUG").append(NL);
			bufferToWriteOn.append(indentTabs()).append("for(int k = 1; k < " + totalDimensionAsString + "; ++k){")
					.append(NL);
			// check for nan
			bufferToWriteOn.append(indentTabs()).append(TAB).append("if(isnan(s.matrix[k])){").append(NL);
			bufferToWriteOn.append(indentTabs()).append(TAB).append(TAB).append(
					"fprintf(stderr, \"ERROR: The %d th element of dydt ( = f(t,y) ) is NAN at time t = %f\\n\", k+1, t);")
					.append(NL);
			bufferToWriteOn.append(indentTabs()).append(TAB).append("}").append(NL);
			// check for inf
			bufferToWriteOn.append(indentTabs()).append(TAB).append("if(isinf(s.matrix[k])){").append(NL);
			bufferToWriteOn.append(indentTabs()).append(TAB).append(TAB).append(
					"fprintf(stderr, \"ERROR: The %d th element of dydt ( = f(t,y) ) is INFINITY at time t = %f\\n\", k+1, t);")
					.append(NL);
			bufferToWriteOn.append(indentTabs()).append(TAB).append("}").append(NL);

			bufferToWriteOn.append(indentTabs()).append("}").append(NL);

			bufferToWriteOn.append("#endif").append(NL);

			// CODE TO DUMP Y AND DYDT VALUES IF DUMP_Y IS DEFINED
			bufferToWriteOn.append("#ifdef DUMP_Y").append(NL);
			// bufferToWriteOn.append(indentTabs())
			// .append("printf(\"Dumping y and dydt at time %f, type any key to
			// continue\\n\", t);").append(NL);
			bufferToWriteOn.append(indentTabs()).append("static int cnt = 0;").append(NL);

			bufferToWriteOn.append(indentTabs()).append("if(cnt++ % DUMP_Y == 0){").append(NL);
			bufferToWriteOn.append(indentTabs()).append("FILE *f = fopen(\"_sundials_y_timeseries.csv\", \"a\");")
					.append(NL);
			bufferToWriteOn.append(indentTabs()).append("assert (f != NULL && \"Error opening file\");").append(NL);
			bufferToWriteOn.append(indentTabs()).append("fprintf(f, \"%.17e,\", t);").append(NL);
			bufferToWriteOn.append(indentTabs()).append("fclose(f);").append(NL);

			bufferToWriteOn.append(indentTabs()).append(
					"vdSave(\"_sundials_y_timeseries.csv\", \"a\", y_as_matrixDouble_2D.matrix, y_as_matrixDouble_2D.poly_basis, 2, ");

			boolean rowvector = outDims.length == 1 || (outDims[0].hasValue() && outDims[0].valueAsInt().equals(1));
			// if (rowvector)
			bufferToWriteOn.append("y_as_matrixDouble_2D.dim1, y_as_matrixDouble_2D.dim2);").append(NL);
			// else
			// bufferToWriteOn.append("y_as_matrixDouble_2D.dim2,
			// y_as_matrixDouble_2D.dim1);").append(NL);

			bufferToWriteOn.append(indentTabs()).append("f = fopen(\"_sundials_dydt_timeseries.csv\", \"a\");")
					.append(NL);
			bufferToWriteOn.append(indentTabs()).append("assert (f != NULL && \"Error opening file\");").append(NL);
			bufferToWriteOn.append(indentTabs()).append("fprintf(f, \"%.17e,\", t);").append(NL);
			bufferToWriteOn.append(indentTabs()).append("fclose(f);").append(NL);
			bufferToWriteOn.append(indentTabs())
					.append("vdSave(\"_sundials_dydt_timeseries.csv\", \"a\", s.matrix, s.poly_basis, 2, ");

			if (rowvector)
				bufferToWriteOn.append("s.dim1, s.dim2);").append(NL);
			else
				bufferToWriteOn.append("s.dim2, s.dim1);").append(NL);

			// END IF CNT % DUMP_Y
			bufferToWriteOn.append(indentTabs()).append("}").append(NL);
			// bufferToWriteOn.append(indentTabs()).append("getchar();").append(NL);
			bufferToWriteOn.append("#endif").append(NL);

			// populates results structure
			bufferToWriteOn.append(indentTabs()).append("memcpy(NV_DATA_S(ydot), s.matrix, sizeof(double)*")
					.append(totalDimensionAsString).append(")").append(";").append(NL);
		} else
			throw new UndefinedTranslationException(
					CErrorMessage.INTERNAL_SUNDIALS_WRONG_INTEGRAND_FUNCTION_RETURN_TYPE, fun);
		bufferToWriteOn.append(indentTabs()).append("return 0;").append(NL);
		bufferToWriteOn.append("}").append(NL).append(NL);

		// append to the right buffer
		StringBuffer targetBuffer = (curFun == null) ? getLocalFunctionsBuffer() : curFun.getLocalFunctionsBuffer();
		targetBuffer.append(bufferToWriteOn);

		return wrapFunction;
	}

	/**
	 * returns the output symbol of this operation. if a lhs part is present and
	 * unique, takes the lhs symbol and set a 'don't translate' flag for the assign
	 * node. If an lhs symbol doesn't exist or is not unique, then the symbol of the
	 * input node is returned, and it makes sure that the variable exists.
	 * 
	 * == WARNING == : this function will set CAttr.TRANSLATE=false if a LHS exists.
	 * be sure to call this only if you actually want to translate the assignment
	 * RIGHT NOW, and not later on in the ASSIGN node.
	 * 
	 * @param outSymbol
	 * @return
	 * @throws UndefinedTranslationException
	 * @throws TypeException
	 */
	protected AASTNode getOutputNode(AASTNode outSymbol) throws UndefinedTranslationException, TypeException {
		// pay attention, this will set CAttr.TRANSLATE=false
		// don't call it if you are not planning to translate also the output node

		return getOutputNode(outSymbol, true, false, false, null);
	}

	protected AASTNode getOutputNode(AASTNode outSymbol, String value)
			throws UndefinedTranslationException, TypeException {
		// pay attention, this will set CAttr.TRANSLATE=false
		// don't call it if you are not planning to translate also the output node

		return getOutputNode(outSymbol, true, false, false, value);
	}

	private AASTNode getOutputNode(AASTNode outSymbol, boolean translateAssignment, boolean onlyReferences)
			throws UndefinedTranslationException, TypeException {
		// pay attention, this will set CAttr.TRANSLATE=false
		// don't call it if you are not planning to translate also the output node

		return getOutputNode(outSymbol, translateAssignment, onlyReferences, false, null);
	}

	private AASTNode getOutputNode(AASTNode outSymbol, boolean translateAssignment, boolean onlyReferences,
			boolean forceStatic, String value) throws UndefinedTranslationException, TypeException {
		// pay attention, this will set CAttr.TRANSLATE=false
		// don't call it if you are not planning to translate also the output node

		// check if it's a matrix access and it attempts to modify
		// the same variable (i.e. X = X(I))
		AASTNode whereToApplyTheAccess = null;
		if (outSymbol.type().equals(NodeType.APPLY) && outSymbol.hasAttr(NodeAttr.MATACCESS)) {
			whereToApplyTheAccess = outSymbol.child(new NodeType[] { NodeType.ID, NodeType.FIELDACCESS });
		}

		AASTNode outSymbolName = outSymbol;
		// pay attention won't change this check unless you change also
		// translateMatrixAccess
		// because it uses it to know if matrix access should translate the whole
		// statement or not
		if (canBeTranslatedByRHS(outSymbol)) {
			AASTNode assignNode;

			if (outSymbol.parentExists(NodeType.ASSIGN))
				assignNode = outSymbol.parent(NodeType.ASSIGN);
			else
				assignNode = outSymbol.parent(NodeType.GASSIGN);

			AASTNode lhsNode = assignNode.child(NodeType.LHS);
			AASTNode idNode = lhsNode.childs().get(0);

			if (translateAssignment) {
				AASTNode outSymbolNameCandidate = null;
				if (idNode.type().equals(NodeType.ID_NODE))
					outSymbolNameCandidate = idNode.childs().get(0);
				else if (idNode.type().equals(NodeType.ID) || idNode.type().equals(NodeType.FIELDACCESS)) {
					outSymbolNameCandidate = idNode;
				}

				if (outSymbolNameCandidate != null && (whereToApplyTheAccess == null
						|| !(whereToApplyTheAccess.symbol().equals(outSymbolNameCandidate.symbol())))) {
					// function output cannot be used directly since it isn't
					// allocated so don't skip translation for them,
					// and the translation will
					// do a simple assignment out = allocated;
					outSymbolName = outSymbolNameCandidate;
					// don't reallocate
					assignNode.attr(CAttr.TRANSLATE, false);
				}
			}
		}

		// pointer equality
		if ((outSymbolName == outSymbol && !outSymbol.parentExists(NodeType.LHS, 2) && outSymbol.expr() != null
				&& !outSymbol.expr().equals(BType.VOID))) {
			// if lhs not found
			if (value == null)
				createVariable(outSymbol, null, onlyReferences, forceStatic, VarVisibility.LOCAL_TO_FUNCTION);
			else
				createVariable(outSymbol, value, onlyReferences, forceStatic, VarVisibility.LOCAL_TO_FUNCTION);
		}

		return outSymbolName;
	}

	@StepType(method = StepType.Function.STRUCT)
	public String createStruct(String newSymbol, StructType struct, VarVisibility v, boolean pointer)
			throws TypeException, IOException, UndefinedTranslationException {
		StringBuffer sb = new StringBuffer();
		String structName = "";
		// CREATE ACTUAL STRUCTURE, UNLESS THIS IS A LOAD STATEMENT
		if (userDefinedStructure(struct)) {
			structName = getStructName(struct);
			// define struct only if not already defined
			StructType structType = (StructType) struct;
			createStructType(structName, structType);
			sb.append(indentTabs());
			sb.append(structName).append(" ");
			if (pointer) {
				sb.append("*");
			}
			sb.append(newSymbol);
			if (!pointer && structType.numberOfFields() != 0) {
				sb.append(" = {");
				List<String> def = new ArrayList<String>(structType.numberOfFields());
				Iterator<Tuple<List<GType>, String>> it = structType.iterFields();
				while (it.hasNext()) {
					Tuple<List<GType>, String> field = it.next();
					def.add(exprTypeToDefaultValue(((LinkedList<GType>) field.first()).peekLast()));
				}
				sb.append(String.join(", ", def));
				sb.append("}");
			}
			sb.append(";").append(NL);
		} else {
			structName = getStructName(struct);
			sb.append(indentTabs()).append(structName).append(" *").append(newSymbol).append(";").append(NL);
		}
		return sb.toString();
	}

	private boolean userDefinedStructure(StructType struct) {
		return (!struct.isInput() && !struct.isKnown()) || (!struct.isKnown() && !struct.name().equals(INPUT_TYPE));
	}

	protected String getStructName(StructType struct) {
		if (userDefinedStructure(struct))
			return struct.name() + STRUCT_SUFFIX;
		else
			return struct.fullname();
	}

	protected String getStructParameterType(StructType struct) {
		if (userDefinedStructure(struct))
			return struct.name() + STRUCT_SUFFIX;
		else
			return struct.fullname() + "*";
	}

	/**
	 * write structure declaration in user-structures.h
	 * 
	 * @param structName
	 * @param structType
	 * @throws IOException
	 * @throws TypeException
	 * @throws UndefinedTranslationException
	 */
	protected void createStructType(String structName, StructType structType)
			throws TypeException, IOException, UndefinedTranslationException {
		if (!structs.contains(structName)) {
			// here you have to write the struct declaration
			writeStructDeclaration(ushwriter, structName, structType);
			writeStructNulls(ushwriter, structName, structType);
			structs.add(structName);
		}
	}

	private void writeDynamicMatrixFunctions() throws IOException, UndefinedTranslationException {
		// write out last dynamic things
		for (Triple<String, String, Integer> matrixType : matricesDefined) {
			String typeName = matrixType.first();
			Integer typeDim = matrixType.third();
			String retType;
			if (typeName.contains("double"))
				retType = "double";
			else if (typeName.contains("int"))
				retType = "int";
			else
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TYPE, null, typeName);

			// define dynamic functions that operate on N-dimensional matrices
			uscwriter.write("__Max(" + typeName + ")" + NL);
			uscwriter.write("__scalarMax(" + retType + ", " + typeName + ")" + NL);
			uscwriter.write("__ScalarMatrixMax(" + retType + ", " + typeName + ")" + NL);
			uscwriter.write("__Min(" + typeName + ")" + NL);
			uscwriter.write("__scalarMin(" + retType + ", " + typeName + ")" + NL);
			uscwriter.write("__ScalarMatrixMin(" + retType + ", " + typeName + ")" + NL);
			uscwriter.write("__Sum(" + typeName + ")" + NL);
			uscwriter.write("__scalarSum(" + retType + ", " + typeName + ")" + NL);
			uscwriter.write("__Mean(" + typeName.replace("int", "double") + ", " + typeName + ")" + NL);
			uscwriter.write("__scalarMean(double, " + typeName + ")" + NL);
			uscwriter.write("__Std(" + typeName.replace("int", "double") + ", " + typeName + ")" + NL);
			uscwriter.write("__scalarStd(double, " + typeName + ")" + NL);
			uscwriter.write("__Var(" + typeName.replace("int", "double") + ", " + typeName + ")" + NL);
			uscwriter.write("__scalarVar(double, " + typeName + ")" + NL);

			ushwriter.write("extern void " + typeName + "Max(" + typeName + " result, " + typeName
					+ " first, int dimfirst, " + typeName + " second, int dimsecond);" + NL);
			ushwriter.write("extern void " + typeName + "ScalarMax(" + retType + " *result, " + typeName
					+ " first, int nelems);" + NL);
			ushwriter.write("extern void " + typeName + "ScalarMatrixMax(" + typeName + " result, " + typeName
					+ " first, int dimfirst, " + retType + " second);" + NL);
			ushwriter.write("extern void " + typeName + "Min(" + typeName + " result, " + typeName
					+ " first, int dimfirst, " + typeName + " second, int dimsecond);" + NL);
			ushwriter.write("extern void " + typeName + "ScalarMin(" + retType + " *result, " + typeName
					+ " first, int nelems);" + NL);
			ushwriter.write("extern void " + typeName + "ScalarMatrixMin(" + typeName + " result, " + typeName
					+ " first, int dimfirst, " + retType + " second);" + NL);
			ushwriter.write("extern void " + typeName + "Sum(" + typeName + " result, " + typeName
					+ " first, int dimfirst, " + typeName + " second, int dimsecond);" + NL);
			ushwriter.write("extern void " + typeName + "ScalarSum(" + retType + " *result, " + typeName
					+ " first, int nelems);" + NL);
			ushwriter.write("extern void " + typeName + "Mean(" + typeName.replace("int", "double") + " result, "
					+ typeName + " first, int dimfirst, " + typeName + " second, int dimsecond);" + NL);
			ushwriter.write(
					"extern void " + typeName + "ScalarMean(double *result, " + typeName + " first, int nelems);" + NL);
			ushwriter.write("extern void " + typeName + "Std(" + typeName.replace("int", "double") + " result, "
					+ typeName + " first, int dimfirst, " + typeName + " second, int dimsecond);" + NL);
			ushwriter.write(
					"extern void " + typeName + "ScalarStd(double *result, " + typeName + " first, int nelems);" + NL);
			ushwriter.write("extern void " + typeName + "Var(" + typeName.replace("int", "double") + " result, "
					+ typeName + " first, int dimfirst, " + typeName + " second, int dimsecond);" + NL);
			ushwriter.write(
					"extern void " + typeName + "ScalarVar(double *result, " + typeName + " first, int nelems);" + NL);

			if (typeDim > 1) {
				// interp
				Tuple<String, String> ti = genInterpFunction("Linear", typeName, typeName.replace("int", "double"),
						retType, typeDim);
				uscwriter.write(ti.first());
				ushwriter.write(ti.second());
				Tuple<String, String> ti1 = genInterpFunction("Nearest", typeName, typeName.replace("int", "double"),
						retType, typeDim);
				uscwriter.write(ti1.first());
				ushwriter.write(ti1.second());
				Tuple<String, String> ti2 = genInterpFunction("Previous", typeName, typeName.replace("int", "double"),
						retType, typeDim);
				uscwriter.write(ti2.first());
				ushwriter.write(ti2.second());
				Tuple<String, String> ti3 = genInterpFunction("Next", typeName, typeName.replace("int", "double"),
						retType, typeDim);
				uscwriter.write(ti3.first());
				ushwriter.write(ti3.second());

				Tuple<String, String> tp = genSummarizeFunctionByCol("Min", "<", null, typeName, typeName, retType,
						typeDim, 1, true, null, null, null);
				uscwriter.write(tp.first());
				ushwriter.write(tp.second());
				Tuple<String, String> tp2 = genSummarizeFunctionByCol("Max", ">", null, typeName, typeName, retType,
						typeDim, 1, true, null, null, null);
				uscwriter.write(tp2.first());
				ushwriter.write(tp2.second());
				for (Integer col : summarizeByCol)
					if (col <= typeDim) {
						Tuple<String, String> tp3 = genSummarizeFunctionByCol("Sum", "scalarSum", null, typeName,
								typeName, retType, typeDim, col, false, null, null, null);
						uscwriter.write(tp3.first());
						ushwriter.write(tp3.second());
						Tuple<String, String> tp4 = genSummarizeFunctionByCol("Mean", "scalarSum", "scalarDiv",
								typeName, typeName.replace("int", "double"), retType, typeDim, col, false, null, null,
								null);
						uscwriter.write(tp4.first());
						ushwriter.write(tp4.second());
						// this computes mean.. but then also std deviation in 1 pass with formula
						// E[x^2] - E[x]^2
						Tuple<String, String> tp5 = genSummarizeFunctionByCol("Var", "scalarSum", null, typeName,
								typeName.replace("int", "double"), retType, typeDim, col, false,
								(StringBuffer sb, String tabs) -> {
									// declaration of second moment accumulator
									sb.append(tabs).append(retType).append(" secondAcc = 0;").append(NL);
									return 0;
								}, (StringBuffer sb, String tabs) -> {
									// update second moment accumulator
									sb.append(tabs).append(
											"secondAcc = secondMoment(secondAcc, in.matrix[curidx], " + retType + ");")
											.append(NL);
									return 0;
								}, (StringBuffer sb, Triple<String, String, Integer> tuple) -> {
									String tabs = tuple.first();
									String flatIdx = tuple.second();
									Integer curDim = tuple.third();

									// final computation of standard deviation, override result.matrix
									String resultVar = "result.matrix[" + flatIdx + "]";
									// compute average
									sb.append(tabs).append(resultVar).append(
											" = scalarDiv(acc, in.dim" + Integer.toString(curDim) + ", double);")
											.append(NL);
									// before this operation resultVar contains the average
									sb.append(tabs).append(resultVar).append(" = divMomentSquared(secondAcc, in.dim"
											+ Integer.toString(curDim) + ", " + resultVar + ", double);").append(NL);
									return 0;
								});
						uscwriter.write(tp5.first());
						ushwriter.write(tp5.second());
						// this computes mean.. but then also std deviation in 1 pass with formula
						// E[x^2] - E[x]^2
						Tuple<String, String> tp6 = genSummarizeFunctionByCol("Std", "scalarSum", null, typeName,
								typeName.replace("int", "double"), retType, typeDim, col, false,
								(StringBuffer sb, String tabs) -> {
									// declaration of second moment accumulator
									sb.append(tabs).append(retType).append(" secondAcc = 0;").append(NL);
									return 0;
								}, (StringBuffer sb, String tabs) -> {
									// update second moment accumulator
									sb.append(tabs).append(
											"secondAcc = secondMoment(secondAcc, in.matrix[curidx], " + retType + ");")
											.append(NL);
									return 0;
								}, (StringBuffer sb, Triple<String, String, Integer> tuple) -> {
									String tabs = tuple.first();
									String flatIdx = tuple.second();
									Integer curDim = tuple.third();

									// final computation of standard deviation, override result.matrix
									String resultVar = "result.matrix[" + flatIdx + "]";
									// compute average
									sb.append(tabs).append(resultVar).append(
											" = scalarDiv(acc, in.dim" + Integer.toString(curDim) + ", double);")
											.append(NL);
									// before this operation resultVar contains the average
									sb.append(tabs).append(resultVar)
											.append(" = divMomentSquaredStd(secondAcc, in.dim"
													+ Integer.toString(curDim) + ", " + resultVar + ", double);")
											.append(NL);
									return 0;
								});
						uscwriter.write(tp6.first());
						ushwriter.write(tp6.second());
					}
			}

			// write the empty macro
			writeStructEmpty(ushwriter, typeName, typeDim);
		}
	}

	private Tuple<String, String> genInterpFunction(String interpolationType, String typeName, String typeOutName,
			String retType, Integer typeDim) {
		StringBuffer sb = new StringBuffer();
		sb.append("void v").append(typeName).append("Interp" + interpolationType + "(").append(typeOutName)
				.append(" yValuesInterp, double *xValues, int xValuesLen, ").append(typeName)
				.append(" yValues, double *xsToBeInterpolated, int xsDim){").append(NL);
		sb.append(TAB).append("int curidx, finalFlatIdx;").append(NL);
		sb.append(TAB).append("assert(yValues.dim1 == xValuesLen);").append(NL);
		Integer[] forOrder = new Integer[typeDim];
		Integer col = 1;
		Integer curDim;
		for (int i = 1; i <= typeDim; ++i)
			forOrder[i - 1] = i;
		// change order of fors to keep the last one at the end
		forOrder[col - 1] = typeDim;
		forOrder[typeDim - 1] = col;
		// skip last one
		for (int i = 0; i < forOrder.length - 1; ++i) {
			for (int t = 0; t <= i; ++t)
				sb.append(TAB);
			curDim = forOrder[i];
			sb.append("for(int i" + curDim + "=0; i" + curDim + " < yValues.dim" + curDim + " ; ++i" + curDim + "){")
					.append(NL);
		}
		// write last for to compute minimum on col dimension
		curDim = forOrder[forOrder.length - 1];
		String tabs = "";
		for (int t = 0; t <= typeDim - 1; ++t)
			tabs += TAB;

		// compute next position for current column (having fixed all the N-1 dimensions
		// except the first)
		sb.append(tabs).append("curidx = ");
		// access in the correct order (this is important to allow the for order to be
		// different than the matrixAccess array order
		String[] flatIdx = new String[typeDim - 1];
		for (int i = 2; i <= typeDim; ++i)
			flatIdx[i - 2] = "i" + i + " * yValues" + STRUCT_ACCESS + "poly_basis[" + (i - 1) + "]";
		sb.append(String.join(" + ", flatIdx));
		sb.append(";").append(NL);

		// all subsequent access indexes
		for (int i = 2; i <= typeDim; ++i)
			flatIdx[i - 2] = "i" + i + " * yValuesInterp.poly_basis[" + (i - 1) + "]";
		String finalFlatIdx = String.join(" + ", flatIdx);
		sb.append(tabs).append("finalFlatIdx = ").append(finalFlatIdx).append(";").append(NL);

		sb.append(tabs).append("for(int j=0; j < xsDim ; ++j){").append(NL);
		tabs += TAB;

		// LOOK FOR J-TH ELEMENT OF xsToBeInterpolated AMONG ALL I-TH values in xValues
		sb.append("#pragma omp smid").append(NL);
		sb.append(tabs)
				.append("for(int i" + curDim + "=0; i" + curDim + " < yValues.dim" + curDim + " ; ++i" + curDim + "){")
				.append(NL);
		tabs += TAB;
		// lucky case equality
		sb.append(tabs).append("if( fabs(xsToBeInterpolated[j]  - xValues[i" + curDim + "]) < DOUBLE_EQUALITY_TOL )")
				.append(NL);
		sb.append(tabs).append(TAB).append("yValuesInterp" + STRUCT_ACCESS + "matrix[finalFlatIdx + j * yValuesInterp"
				+ STRUCT_ACCESS + "poly_basis[" + (curDim - 1) + "] ] = ");
		sb.append("yValues" + STRUCT_ACCESS + "matrix[ curidx + i" + curDim + " * yValues" + STRUCT_ACCESS
				+ "poly_basis[" + (curDim - 1) + "] ];").append(NL);
		// normal case, assume xValues ordered
		sb.append(tabs).append("else if( xsToBeInterpolated[j]  < xValues[i" + curDim + "] ){").append(NL);

		if (interpolationType.equalsIgnoreCase("linear")) {
			// LINEAR INTERPOLATION
			sb.append(tabs).append(TAB)
					.append("yValuesInterp" + STRUCT_ACCESS + "matrix[finalFlatIdx + j * yValuesInterp" + STRUCT_ACCESS
							+ "poly_basis[" + (curDim - 1) + "] ] = ");
			// formula complete
			// (END - START) * ( (XTOBEINTERPOLATED-XVALUES[I-1]) /
			// (XVALUES[I]-XVALUES[I-1]) ) + START
			// ( END -
			sb.append("( yValues" + STRUCT_ACCESS + "matrix[ curidx + i" + curDim + " * yValues" + STRUCT_ACCESS
					+ "poly_basis[" + (curDim - 1) + "] ] - ");
			// START ) *
			sb.append("yValues" + STRUCT_ACCESS + "matrix[ curidx + (i" + curDim + " - 1) * yValues" + STRUCT_ACCESS
					+ "poly_basis[" + (curDim - 1) + "] ] ) * ");
			// ( ( XTOBEINTERPOLATED - XVALUES[I-1]) /
			sb.append("( ( xsToBeInterpolated[j] - xValues[i" + curDim + " - 1] ) / ");
			// (XVALUES[I]-XVALUES[I-1]) ) +
			sb.append("( xValues[i" + curDim + "] - xValues[i" + curDim + " - 1]  ) ) + ");
			// START
			sb.append("yValues" + STRUCT_ACCESS + "matrix[ curidx + (i" + curDim + " - 1) * yValues" + STRUCT_ACCESS
					+ "poly_basis[" + (curDim - 1) + "] ]");
			sb.append(";").append(NL);

		} else if (interpolationType.equalsIgnoreCase("nearest")) {
			// TAKE THE NEAREST
			sb.append(tabs).append(TAB).append("if( (xsToBeInterpolated[j] - xValues[i" + curDim + " - 1]) < (xValues[i"
					+ curDim + "] - xsToBeInterpolated[j]) )").append(NL);
			// take the previous
			sb.append(tabs).append(TAB).append(TAB)
					.append("yValuesInterp" + STRUCT_ACCESS + "matrix[finalFlatIdx + j * yValuesInterp" + STRUCT_ACCESS
							+ "poly_basis[" + (curDim - 1) + "] ] = ")
					.append("yValues" + STRUCT_ACCESS + "matrix[ curidx + (i" + curDim + "-1) * yValues" + STRUCT_ACCESS
							+ "poly_basis[" + (curDim - 1) + "] ];")
					.append(NL);
			sb.append(tabs).append(TAB).append("else").append(NL);
			// take the next
			sb.append(tabs).append(TAB).append(TAB)
					.append("yValuesInterp" + STRUCT_ACCESS + "matrix[finalFlatIdx + j * yValuesInterp" + STRUCT_ACCESS
							+ "poly_basis[" + (curDim - 1) + "] ] = ")
					.append("yValues" + STRUCT_ACCESS + "matrix[ curidx + i" + curDim + " * yValues" + STRUCT_ACCESS
							+ "poly_basis[" + (curDim - 1) + "] ];")
					.append(NL);
		} else if (interpolationType.equalsIgnoreCase("previous")) {
			// TAKE THE PREVIOUS
			sb.append(tabs).append(TAB)
					.append("yValuesInterp" + STRUCT_ACCESS + "matrix[finalFlatIdx + j * yValuesInterp" + STRUCT_ACCESS
							+ "poly_basis[" + (curDim - 1) + "] ] = ")
					.append("yValues" + STRUCT_ACCESS + "matrix[ curidx + (i" + curDim + "-1) * yValues" + STRUCT_ACCESS
							+ "poly_basis[" + (curDim - 1) + "] ];")
					.append(NL);
		} else if (interpolationType.equalsIgnoreCase("next")) {
			// TAKE THE NEXT
			sb.append(tabs).append(TAB)
					.append("yValuesInterp" + STRUCT_ACCESS + "matrix[finalFlatIdx + j * yValuesInterp" + STRUCT_ACCESS
							+ "poly_basis[" + (curDim - 1) + "] ] = ")
					.append("yValues" + STRUCT_ACCESS + "matrix[ curidx + i" + curDim + " * yValues" + STRUCT_ACCESS
							+ "poly_basis[" + (curDim - 1) + "] ];")
					.append(NL);
		}

		sb.append(tabs).append(TAB).append("break;").append(NL);
		sb.append(tabs).append("}").append(NL);
		sb.append(NL);

		for (int i = typeDim; i >= 0; --i) {
			for (int t = 0; t <= i; ++t)
				sb.append(TAB);
			sb.append("}").append(NL);
		}
		sb.append("}").append(NL).append(NL);
		return new Tuple<String, String>(sb.toString(),
				"extern void v" + typeName + "Interp" + interpolationType + "(" + typeOutName
						+ " yValuesInterp, double *xValues, int xValuesLen, " + typeName
						+ " yValues, double *xsToBeInterpolated, int xsDim);" + NL);
	}

	private Tuple<String, String> genSummarizeFunctionByCol(String funEnd, String funOp, String finalFunOp,
			String typeName, String typeOutName, String retType, Integer typeDim, Integer col, boolean conditional,
			BiFunction<StringBuffer, String, Integer> extraAccumulatorDeclareFun,
			BiFunction<StringBuffer, String, Integer> extraAccumulatorApplyFun,
			BiFunction<StringBuffer, Triple<String, String, Integer>, Integer> extraAccumulatorSummarizeFun) {
		StringBuffer sb = new StringBuffer();
		sb.append("void ").append(typeName).append("Col" + Integer.toString(col) + funEnd + "(").append(typeOutName)
				.append(" result, ").append(typeName).append(" in, int in_dims){").append(NL);
		sb.append(TAB).append("int curidx;").append(NL);
		Integer[] forOrder = new Integer[typeDim];
		for (int i = 1; i <= typeDim; ++i)
			forOrder[i - 1] = i;
		// change order of fors to keep the last one at the end
		forOrder[col - 1] = typeDim;
		forOrder[typeDim - 1] = col;
		Integer curDim;
		// skip last one
		for (int i = 0; i < forOrder.length - 1; ++i) {
			for (int t = 0; t <= i; ++t)
				sb.append(TAB);
			curDim = forOrder[i];
			sb.append("for(int i" + curDim + "=0; i" + curDim + " < in.dim" + curDim + " ; ++i" + curDim + "){")
					.append(NL);
		}
		// write last for to compute minimum on col dimension
		curDim = forOrder[forOrder.length - 1];
		String tabs = "";
		for (int t = 0; t <= typeDim - 1; ++t)
			tabs += TAB;

		sb.append(tabs).append(retType).append(" acc = 0;").append(NL);
		if (extraAccumulatorDeclareFun != null)
			extraAccumulatorDeclareFun.apply(sb, tabs);
		sb.append(tabs).append("for(int i" + Integer.toString(curDim) + "=0; i" + Integer.toString(curDim) + " < in.dim"
				+ Integer.toString(curDim) + " ; ++i" + Integer.toString(curDim) + "){").append(NL);
		String bodytabs = tabs + TAB;

		// compute next position for current column (having fixed all the N-1 dimensions
		// except the first)
		sb.append(bodytabs).append("curidx = ");
		// access in the correct order (this is important to allow the for order to be
		// different than the matrixAccess array order
		String[] flatIdx = new String[typeDim];
		for (int i = 1; i <= typeDim; ++i)
			flatIdx[i - 1] = "i" + i + " * in.poly_basis[" + (i - 1) + "]";
		sb.append(String.join(" + ", flatIdx));
		sb.append(";").append(NL);

		// algorithm to compute aggregation (min/max) based on condition
		if (conditional) {
			sb.append(bodytabs).append("if (i1 == 1)").append(NL);
			sb.append(bodytabs).append(TAB).append("acc = in.matrix[curidx];").append(NL);
			sb.append(bodytabs).append("else if (in.matrix[curidx] " + funOp + " acc)").append(NL);
			sb.append(bodytabs).append(TAB).append("acc = in.matrix[curidx];").append(NL);
		} else {
			// aggregation based on function applied to all elements to be aggregated
			sb.append(bodytabs).append("acc = " + funOp + "(acc, in.matrix[curidx], " + retType + ");").append(NL);
		}

		if (extraAccumulatorApplyFun != null)
			extraAccumulatorApplyFun.apply(sb, bodytabs);

		sb.append(tabs).append("}").append(NL);
		// now minimum is stored in variable min, save it in output matrix
		String finalFlatIdx = null;
		if (typeDim > 2) {
			// generic way
			sb.append(tabs).append("result.matrix[");
			// all subsequent access indexes
			for (int i = 1; i <= typeDim; ++i)
				if (i == col)
					// first access index, meaning times (1-1) * poly_basis[i] == 0 -> don't add
					// anything
					flatIdx[i - 1] = "0";
				else
					flatIdx[i - 1] = "i" + i + " * result.poly_basis[" + (i - 1) + "]";
			finalFlatIdx = String.join(" + ", flatIdx);
			sb.append(finalFlatIdx);
			if (finalFunOp != null)
				sb.append("] = " + finalFunOp + "(acc, in.dim" + Integer.toString(curDim) + ", " + retType + ");")
						.append(NL);
			else
				sb.append("] = acc;").append(NL);
		} else {
			finalFlatIdx = "i" + forOrder[0];
			// avoid useless function call
			if (finalFunOp != null)
				sb.append(tabs).append("result.matrix[" + finalFlatIdx + "] = " + finalFunOp + "(acc, in.dim"
						+ Integer.toString(curDim) + ", " + retType + ");").append(NL);
			else
				sb.append(tabs).append("result.matrix[" + finalFlatIdx + "] = acc;").append(NL);
		}
		if (extraAccumulatorSummarizeFun != null)
			extraAccumulatorSummarizeFun.apply(sb, new Triple<String, String, Integer>(tabs, finalFlatIdx, curDim));

		for (int i = typeDim - 2; i >= 0; --i) {
			for (int t = 0; t <= i; ++t)
				sb.append(TAB);
			sb.append("}").append(NL);
		}
		// end function
		sb.append("}").append(NL).append(NL);
		String header = "extern void " + typeName + "Col" + Integer.toString(col) + funEnd + "(" + typeOutName
				+ " result, " + typeName + " in, int in_dims);" + NL;
		return new Tuple<String, String>(sb.toString(), header);
	}

	private void writeMatrixNulls() throws IOException {
		for (Triple<String, String, Integer> matrixType : matricesDefined) {
			String typeName = matrixType.first();
			Integer typeDim = matrixType.third();
			String[] mdefValues = new String[typeDim.intValue()];
			String[] nullvalues = new String[typeDim.intValue()];
			for (int i = 0; i < typeDim.intValue(); ++i) {
				mdefValues[i] = TypeUtils.matrixDimName("__x", i + 1) + " == intNull";
				nullvalues[i] = "intNull";
			}
			// null value
			uscwriter.write(typeName + " " + typeName + "Null = {NULL, NULL, intNull, " + String.join(", ", nullvalues)
					+ "};" + NL);
			ushwriter.write("extern " + typeName + " " + typeName + "Null;" + NL);
			// null check macro
			ushwriter.write("#define " + typeName.toUpperCase()
					+ "_IS_NULL(__x) (__x.matrix == NULL && __x.__realsize == intNull && "
					+ String.join(" && ", mdefValues) + ")" + NL);
		}
	}

	private void writeStructNulls(BufferedWriter writer, String structName, StructType structType)
			throws TypeException, IOException, UndefinedTranslationException {
		String[] fieldsCheck = new String[structType.numberOfFields()];
		String[] fieldsNull = new String[structType.numberOfFields()];
		int i = 0;
		Iterator<Tuple<List<GType>, String>> it = structType.iterFields();
		while (it.hasNext()) {
			Tuple<List<GType>, String> field = it.next();
			List<String> structNames = new ArrayList<>();
			structNames.add(field.second());
			Tuple<String, String> tp = nullValueCheck(field.first().get(0), structNames);
			fieldsNull[i] = tp.first();
			fieldsCheck[i++] = tp.second();
		}
		// null value
		uscwriter.write(structName + " " + structName + "Null = {" + String.join(", ", fieldsNull) + "};" + NL);
		writer.write("extern " + structName + " " + structName + "Null;" + NL);
		// null check macro
		writer.write("#define " + structName.toUpperCase() + "_IS_NULL(__x) (" + String.join(" && ", fieldsCheck) + ")"
				+ NL);
	}

	/**
	 * 
	 * @param writer
	 * @param structName
	 * @param structType
	 * @throws TypeException
	 * @throws IOException
	 */
	private void writeStructEmpty(BufferedWriter writer, String structName, Integer dimensions) throws IOException {
		String[] emptyCheck = new String[dimensions];
		for (int i = 1; i <= dimensions; i++) {
			emptyCheck[i - 1] = "__x.dim" + i + "==0";
		}
		// null check macro
		writer.write("#define " + structName.toUpperCase() + "_IS_EMPTY(__x) (" + String.join(" || ", emptyCheck) + ")"
				+ NL);
	}

	/**
	 * 
	 * @param type
	 * @param structNames
	 * @return the null values, and the null checks
	 * @throws TypeException
	 * @throws UndefinedTranslationException
	 */
	private Tuple<String, String> nullValueCheck(GType type, List<String> structNames)
			throws TypeException, UndefinedTranslationException {
		String defaultValue = null;
		String nullvalues = null;
		if (type.equals(BType.MATRIX)) {
			List<String> defaultParameters = new ArrayList<String>();
			defaultParameters.add("__x." + String.join(".", structNames) + ".matrix == NULL");
			defaultParameters.add("__x." + String.join(".", structNames) + ".__realsize == intNull");
			for (int i = 0; i < ((DimensionType) type).dims().length; ++i)
				defaultParameters
						.add(TypeUtils.matrixDimName("__x." + String.join(".", structNames), i + 1) + " == intNull");
			defaultValue = String.join(" && ", defaultParameters);
			if (type.equals(BType.MATRIX))
				nullvalues = "{" + String.join(", ",
						getMatrixDefaultValues(((DimensionType) type).dims().length, ((MatrixType) type).isSparse()))
						+ "}";
			else
				nullvalues = "{"
						+ String.join(", ", getMatrixDefaultValues(((DimensionType) type).dims().length, false)) + "}";
		} else if (type.equals(BType.MATRIX_ACCESS_SLICE)) {
			nullvalues = exprTypeToDefaultValue(type);
			if (((SliceType) type).of().equals(BType.SCALAR))
				defaultValue = "__x." + String.join(".", structNames) + ".start == doubleNull && __x."
						+ String.join(".", structNames) + ".step == doubleNull && __x." + String.join(".", structNames)
						+ ".end == doubleNull && __x." + String.join(".", structNames) + ".dim1 == intNull";
			else
				defaultValue = "__x." + String.join(".", structNames) + ".start == intNull && __x."
						+ String.join(".", structNames) + ".step == intNull && __x." + String.join(".", structNames)
						+ ".end == intNull && __x." + String.join(".", structNames) + ".dim1 == intNull";
		} else if (type.isCastableToInt()) {
			defaultValue = "__x." + String.join(".", structNames) + " == intNull";
			nullvalues = "intNull";
		} else if (type.isCastableToScalar()) {
			defaultValue = "__x." + String.join(".", structNames) + " == doubleNull";
			nullvalues = "doubleNull";
		} else if (type.isCastableToString()) {
			defaultValue = "__x." + String.join(".", structNames) + " == NULL";
			nullvalues = "NULL";
		} else if (type.equals(BType.STRUCT)) {
			StructType stype = (StructType) type;
			// recursion
			structNames.add(stype.name());
			Tuple<String, String> dv = structNullValueCheck(structNames, (StructType) type);
			defaultValue = dv.second();
			// add a list
			nullvalues = dv.first();
			// pop stack
			structNames.remove(stype.name());
		} else
			throw new TypeException(CErrorMessage.INTERNAL_NULL_VALUE, null, type);

		return new Tuple<String, String>(nullvalues, defaultValue);
	}

	private Tuple<String, String> structNullValueCheck(List<String> structNames, StructType structType)
			throws TypeException, UndefinedTranslationException {
		List<String> checks = new ArrayList<String>(structType.numberOfFields());
		List<String> values = new ArrayList<String>(structType.numberOfFields());
		Iterator<Tuple<List<GType>, String>> it = structType.iterFields();
		while (it.hasNext()) {
			Tuple<List<GType>, String> field = it.next();
			String defaultValue = null;
			GType type = field.first().get(0);
			Tuple<String, String> dv = nullValueCheck(type, structNames);
			values.add(dv.first());
			checks.add(dv.second());
		}
		return new Tuple<String, String>("{" + String.join(", ", values) + "}", String.join(" && ", checks));
	}

	@StepType(method = StepType.Function.SCALAR)
	public String createScalar(String newSymbol, boolean onlyRefs, VarVisibility v)
			throws UndefinedTranslationException {
		StringBuffer sb = new StringBuffer();
		sb.append("double ");
		if (onlyRefs)
			sb.append("*");
		sb.append(newSymbol).append(";").append(NL);
		return sb.toString();
	}

	@StepType(method = StepType.Function.SLICE)
	public String createSlice(String var, GType stype, String value, VarVisibility v)
			throws UndefinedTranslationException {
		StringBuffer sb = new StringBuffer();
		if (stype.equals(BType.INT)) {
			sb.append(SLICE_TYPE_I).append(" ").append(var);
		} else if (stype.equals(BType.SCALAR))
			sb.append(SLICE_TYPE_D).append(" ").append(var);

		if (value != null)
			sb.append(" = ").append(value);

		sb.append(";").append(NL);

		return sb.toString();
	}

	public String createBoolScalar(String newSymbol, boolean onlyRefs, VarVisibility v)
			throws UndefinedTranslationException {
		return createIntScalar(newSymbol, onlyRefs, v);
	}

	public String createBoolScalar(String newSymbol, String value, boolean onlyRefs, VarVisibility v)
			throws UndefinedTranslationException {
		return createIntScalar(newSymbol, value, onlyRefs, v);
	}

	public String createString(String symbol, String value, boolean forceStatic, VarVisibility v, boolean pointer)
			throws UndefinedTranslationException {
		if (curFun != null) {
			return curFun.createString(symbol, value, forceStatic, v, pointer);
		} else {
			StringBuffer sb = new StringBuffer();
			if (forceStatic)
				sb.append("static __thread ");
			// define symbol
			sb.append("char *").append(symbol);
			if (value != null) {
				if (value.toLowerCase().equals("null"))
					sb.append("=").append(value);
				else
					sb.append("=\"").append(value).append("\"");
			}

			sb.append(";").append(NL);

			return sb.toString();
		}
	}

	public String createIntScalar(String newSymbol, boolean onlyRefs, VarVisibility v)
			throws UndefinedTranslationException {
		if (curFun != null) {
			return curFun.createIntScalar(newSymbol, onlyRefs, v);
		}
		StringBuffer sb = new StringBuffer();
		// define symbol
		sb.append("int ");
		if (onlyRefs)
			sb.append("*");
		sb.append(newSymbol).append(";").append(NL);
		return sb.toString();
	}

	public String createIntScalar(String newSymbol, String value, boolean onlyRefs, VarVisibility v)
			throws UndefinedTranslationException {
		if (curFun != null) {
			return curFun.createIntScalar(newSymbol, value, onlyRefs, v);
		}

		StringBuffer sb = new StringBuffer();
		// define symbol
		sb.append("int ");
		if (onlyRefs)
			sb.append("*");
		sb.append(newSymbol).append(" = ").append(value).append(";").append(NL);

		return sb.toString();
	}

	@StepType(method = StepType.Function.SCALARV)
	public String createScalar(String newSymbol, String value, boolean onlyRefs, VarVisibility v)
			throws UndefinedTranslationException {
		if (curFun != null) {
			return curFun.createScalar(newSymbol, value, onlyRefs, v);
		}
		StringBuffer sb = new StringBuffer();
		// define symbol
		sb.append("double ");
		if (onlyRefs)
			sb.append("*");
		sb.append(newSymbol).append(" = ").append(value).append(";").append(NL);
		return sb.toString();
	}

	public String createScalar(String newSymbol, String value, StringBuffer buff) {
		// define symbol
		buff.append(indentTabs()).append("double ").append(newSymbol).append(" = ").append(value).append(";")
				.append(NL);
		return buff.toString();
	}

	public Tuple<String, String> createMatrixRef(String refName, IntType[] dimensions, VarVisibility v)
			throws UndefinedTranslationException, TypeException {
		return createMatrixRef(refName, dimensions, v, null);
	}

	public Tuple<String, String> createMatrixRef(String refName, IntType[] dimensions, VarVisibility v,
			String defaultValue) throws UndefinedTranslationException, TypeException {
		return createMatrixRef(refName, "double", null, dimensions, false, false, false, v, false, null, false);
	}

	public Tuple<String, String> createIndexMatrixRef(String refName, IntType[] dimensions, VarVisibility v,
			String defaultValue) throws UndefinedTranslationException, TypeException {
		return createMatrixRef(refName, "int", null, dimensions, false, false, false, v, false, defaultValue, false);
	}

	public Tuple<String, String> createIndexMatrixRef(String refName, IntType[] dimensions, VarVisibility v)
			throws UndefinedTranslationException, TypeException {
		return createMatrixRef(refName, dimensions, v, null);
	}

	public Tuple<String, String> createMatrixRef(String refName, String type, String value, IntType[] dimensions,
			boolean sparse, boolean onlyReferences, boolean forceStatic, VarVisibility v, boolean alreadyDeclared,
			String defaultValue, boolean extern) throws UndefinedTranslationException, TypeException {
		if (curFun != null) {
			return curFun.createMatrixRef(refName, type, value, dimensions, sparse, onlyReferences, forceStatic, v,
					alreadyDeclared, defaultValue, extern);
		}
		StringBuffer declare = new StringBuffer();
		StringBuffer assign = new StringBuffer();
		createMatrixType(type, dimensions);
		if (!"double".equals(type))
			// always create also double counterpart, since it is used in std avg and mean
			// functions even if they don't appear in the program
			createMatrixType("double", dimensions);

		String structType = getMatrixTypeName(type, dimensions.length, sparse);
		List<String> dimensionsStr = nodeDimsToStr(null, dimensions, true, false);
		String completeDim = nodeDimsToInt(dimensions);
		boolean isInt = completeDim != null;
		if (forceStatic && completeDim == null) {
			completeDim = String.join("*", dimensionsStr);
		}

		if (!alreadyDeclared) {
			/*
			 * define every matrix as static, so to optimize memory management inside
			 * functions (since we never free)
			 */
			declare.append(structType + " " + refName);

			if (!extern && (sparse || (value == null && completeDim == null)) && defaultValue != null) {
				declare.append("=").append(defaultValue);
			}

			declare.append(";").append(NL);
		}
		String staticMatrixName = getStaticMatrixArrayName(refName);
		String staticPolyBasisName = getStaticMatrixPolyBasisName(refName);
		if (!extern && !onlyReferences && !sparse && completeDim != null) {
			if (isInt)
				// if dimension constant
				declare.append("static __thread ");
			declare.append(type).append(" ").append(staticMatrixName).append("[" + completeDim + "]");
			if (value != null)
				declare.append(" = ").append(value);
			declare.append(";").append(NL);

			if (isInt)
				// if dimension constant
				declare.append("static __thread ");
			declare.append("int ").append(staticPolyBasisName).append("[" + dimensions.length + "]");
			declare.append(" = {");
			if (dimensions.length == 1)
				declare.append("1");
			else {
				IntType[] realDimensions = TypeUtils.realDims(dimensions);

				if (ROWMAJOR) {
					if (realDimensions.length == 1)
						declare.append("1");
					else
						declare.append(dimensions[1].valueAsString());
					declare.append(", ");
					declare.append("1");
				} else {
					declare.append("1").append(", ");
					if (realDimensions.length == 1)
						declare.append("1");
					else
						declare.append(dimensions[0].valueAsString());
				}

				if (dimensions.length > 2) {
					String nextDim = dimensions[0].valueAsString() + " * " + dimensions[1].valueAsString();
					declare.append(", ");
					if (dimensions.length == 3)
						declare.append(nextDim);
					else {
						String[] missingDims = new String[dimensions.length - 3];
						for (int n = 3; n < dimensions.length; ++n) {
							missingDims[n - 3] = nextDim + " * " + dimensions[n].valueAsString();
							nextDim = nextDim + " * " + dimensions[n].valueAsString();
						}
						declare.append(String.join(", ", missingDims));
					}
				}
			}
			declare.append("}");
			declare.append(";").append(NL);
			// begin assignment
			assign.append(refName).append(STRUCT_ACCESS).append("matrix = ").append(staticMatrixName).append(";")
					.append(NL);
			assign.append(refName).append(STRUCT_ACCESS).append("poly_basis = ").append(staticPolyBasisName).append(";")
					.append(NL);
			assign.append(refName).append(STRUCT_ACCESS).append("__realsize = ").append(String.join("*", dimensionsStr))
					.append(";").append(NL);

			for (int i = 1; i <= dimensions.length; ++i) {
				// define symbolic value for this dimension (and store it in the
				// structure)
				assign.append(TypeUtils.matrixDimName(refName, i)).append(" = ")
						.append(typeToStrValue(dimensions[i - 1])).append(";").append(NL);
			}
			assign.append(NL);
		}
		return new Tuple<String, String>(declare.toString(), assign.toString());
	}

	protected String getStaticMatrixArrayName(String refName) {
		return refName.replace(STRUCT_ACCESS, "_").replace(STRUCT_POINTER_ACCESS, "_") + "_vals";
	}

	protected String getStaticMatrixPolyBasisName(String refName) {
		return Symbols.getSymbolFromType(
				refName.replace(STRUCT_ACCESS, "_").replace(STRUCT_POINTER_ACCESS, "_") + "_poly_basis", BType.MATRIX);
	}

	private Tuple<StringBuffer, Set<String>> getDeclarationBuffer(VarVisibility v)
			throws UndefinedTranslationException {
		Tuple<StringBuffer, Set<String>> ret;
		List<Triple<AASTNode, StringBuffer, Set<String>>> curTb;
		StringBuffer globalBuffer;
		if (curFun != null) {
			curTb = curFun.getTranslationBufferList();
			globalBuffer = curFun.getGlobalBuffer();
		} else {
			curTb = getTranslationBufferList();
			globalBuffer = getGlobalBuffer();
		}
		switch (v) {
		case GLOBAL:
			ret = new Tuple<>(globalBuffer, curTb.get(0).third());
			break;
		case LOCAL_TO_FUNCTION:
		case PERSISTENT:
			ret = new Tuple<>(getInitialTranslationBuffer().second(), curTb.get(0).third());
			break;
		case LOCAL_TO_BLOCK:
			Triple<AASTNode, StringBuffer, Set<String>> curBufCtx = getCurrentTranslationBuffer(v.context());
			ret = new Tuple<>(curBufCtx.second(), curBufCtx.third());
			break;
		default:
			throw new UndefinedTranslationException(CErrorMessage.INTERNAL_UNDEFINED_VISIBILITY_LEVEL, null, v);
		}
		return ret;
	}

	private void createGenericMatrix(int dim) {
		StringBuilder sb = new StringBuilder();
		sb.append("typedef struct {" + NL);
		sb.append(TAB + "double *matrix;" + NL);
		sb.append(TAB + "int *poly_basis;" + NL);
		sb.append(TAB).append("int").append(" __realsize;").append(NL);
		for (int i = 1; i <= dim; i++) {
			sb.append(TAB + "int dim" + i + ";" + NL);
		}
		sb.append("} dgenMatrix;" + NL);
		sb.append(NL);
		sb.append("typedef struct {" + NL);
		sb.append(TAB + "int *matrix;" + NL);
		sb.append(TAB + "int *poly_basis;" + NL);
		sb.append(TAB).append("int").append(" __realsize;").append(NL);
		for (int i = 1; i <= dim; i++) {
			sb.append(TAB + "int dim" + i + ";" + NL);
		}
		sb.append("} igenMatrix;" + NL);
		try {
			ushwriter.write(sb.toString());
		} catch (IOException e) {
			logger.error("Error adding generic: " + e.getMessage());
			logger.debug("Error adding generic matrix: " + e.getMessage(), e);
		}

	}

	private void createMatrixType(String stype, IntType[] dimensions)
			throws UndefinedTranslationException, TypeException {
		StringBuffer sb = new StringBuffer();
		String structName = getMatrixTypeName(stype, dimensions.length, false);
		if (matrixTypes.contains(structName)) {
			// create also the version without ones
			IntType[] realdims = TypeUtils.realDims(dimensions);
			if (realdims.length < dimensions.length)
				createMatrixType(stype, realdims);
			return;
		}
		matricesDefined.add(new Triple<String, String, Integer>(structName, stype, dimensions.length));
		if (maxMatrixDimension < dimensions.length)
			maxMatrixDimension = dimensions.length;

		sb.append("typedef struct {").append(NL);
		sb.append(TAB).append(stype).append(" *matrix;").append(NL);
		sb.append(TAB + "int *poly_basis;" + NL);
		sb.append(TAB).append("int").append(" __realsize;").append(NL);
		for (int i = 1; i <= dimensions.length; ++i)
			sb.append(TAB).append("int ").append(" dim" + i + ";").append(NL);
		sb.append("} ").append(structName).append(";").append(NL);
		// so that preprocessor knows if this is defined
		sb.append("#define ").append(structName.toUpperCase()).append(NL);
		try {
			ushwriter.write(sb.toString());
		} catch (IOException e) {
			logger.error("Error adding matrix type: " + e.getMessage());
			logger.debug("Error adding matrix type: " + e.getMessage(), e);
		}
		matrixTypes.add(structName);
		// create also the version without ones
		createMatrixType(stype, TypeUtils.realDims(dimensions));
	}

	/**
	 * at the end of program, create the macros needed to operated on generic ND
	 * matrices within C code.
	 */
	private void writeDimensionUpdaterMatrices() {
		StringBuffer sb = new StringBuffer();
		createGenericMatrix(maxMatrixDimension);
		// base macro used by memory management
		sb.append("void update_matrix_dims(void* matrix, int newdims[], int ndims){").append(NL);
		sb.append("   switch(ndims) {").append(NL);
		for (int i = maxMatrixDimension; i >= 1; --i) {
			sb.append("	   case " + i + " :");
			sb.append(" ((dgenMatrix *)matrix)->dim" + i + " = (newdims[" + (i - 1) + "] > ((dgenMatrix *)matrix)->dim"
					+ i + ")? newdims[" + (i - 1) + "] : ((dgenMatrix *)matrix)->dim" + i + "; ");
			sb.append(NL);
		}
		sb.append("   }").append(NL).append("}").append(NL);

		sb.append("void force_update_matrix_dims(void* matrix, int newdims[], int ndims){").append(NL);
		sb.append("   switch(ndims) {").append(NL);
		for (int i = maxMatrixDimension; i >= 1; --i) {
			sb.append("	   case " + i + " :");
			sb.append(" ((dgenMatrix" + " *)matrix)->dim" + i + " = newdims[" + (i - 1) + "]; ");
			sb.append(NL);
		}
		sb.append("   }").append(NL).append("}").append(NL);

		sb.append("void get_matrix_dims(void* matrix, int olddims[], int ndims){").append(NL);
		sb.append("   switch(ndims) {").append(NL);
		for (int i = maxMatrixDimension; i >= 1; --i) {
			sb.append("	   case " + i + " :");
			sb.append(" olddims[" + (i - 1) + "] = ((dgenMatrix *)matrix)->dim" + i + ";");
			sb.append(NL);
		}
		sb.append("   }").append(NL).append("}").append(NL);

		sb.append("int update_matrix_mul(void* matrix, int ndims) {").append(NL);
		sb.append("   int ret = 1;").append(NL);
		sb.append("   dgenMatrix *m = (dgenMatrix *)matrix;").append(NL);
		sb.append("   switch(ndims) {").append(NL);
		String type = null;
		for (Triple<String, String, Integer> tp : matricesDefined)
			if (tp.third().equals(maxMatrixDimension))
				type = tp.second();
		for (int i = maxMatrixDimension; i >= 1; --i) {
			sb.append("      case " + i + " : ");
			sb.append(" if(INT_IS_NULL(m->dim" + i + ")){ return -1; }else{ ret *= m->dim" + i + "; }");
			sb.append(NL);
		}
		sb.append("   }").append(NL).append("   return ret;").append(NL).append("}");
		sb.append(NL);

		try {
			uscwriter.write(sb.toString());
		} catch (IOException e) {
			logger.error("Error writing matrix manipulation matrices: " + e.getMessage());
			logger.debug("Error writing matrix manipulation matrices: " + e.getMessage(), e);
		}
		try {
			ushwriter.write("extern void update_matrix_dims(void* matrix, int newdims[], int ndims);" + NL);
			ushwriter.write("extern void force_update_matrix_dims(void* matrix, int newdims[], int ndims);" + NL);
			ushwriter.write("extern void get_matrix_dims(void* matrix, int olddims[], int ndims);" + NL);
			ushwriter.write("extern int update_matrix_mul(void* matrix, int ndims);" + NL);
		} catch (IOException e) {
			logger.error("Error writing matrix manipulation matrices: " + e.getMessage());
			logger.debug("Error writing matrix manipulation matrices: " + e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param matrixNode
	 * @param mt
	 * @return
	 * @throws UndefinedTranslationException
	 */
	// for now just have this function return a string that is the name of the
	// function hardcoded
	private String getMatrixCopyOnFunctionName(AASTNode matrixNode, MatrixType mt, boolean checkOnly,
			boolean skip_values, boolean flatMatrixAccess, boolean sparse) throws UndefinedTranslationException {

		String ch;
		if (mt.of().isCastableToBool())
			ch = "i";
		else
			ch = "d";

		if (flatMatrixAccess)
			if (checkOnly)
				return "v" + ch + "CheckFlatCapacity";
			else if (skip_values)
				return "v" + ch + "ensureignoreCapacity";
			else
				return "v" + ch + "ensurekeepCapacity";
		else if (checkOnly)
			if (sparse)
				return "v" + ch + "CheckSparseCapacity";
			else if (skip_values)
				return "v" + ch + "checkignoreCapacity";
			else
				return "v" + ch + "checkkeepCapacity";
		else if (sparse)
			return "v" + ch + "EnsureSparseCapacity";
		else if (skip_values)
			return "v" + ch + "ensureignoreCapacity";
		else
			return "v" + ch + "ensurekeepCapacity";
	}

	public boolean emptyStatements() {
		return "".equals(translation.toString());
	}

	private String getMatrixTypeName(String stype, int dimensions, boolean sparse) {
		if (!sparse)
			return stype.trim() + Integer.toString(dimensions) + "dMatrix";
		else
			return stype.trim() + "SparseMatrix";
	}

	private String getSparseMatrixTypeName(String stype) {
		return stype.trim() + "Matrix";
	}

	private List<String> getMatrixDefaultValues(int dims, boolean sparse) {
		List<String> defaultParameters = new ArrayList<String>();
		defaultParameters.add("NULL");
		/* poly_basis */
		defaultParameters.add("NULL");
		defaultParameters.add("intNull");
		for (int i = 0; i < dims; ++i) {
			defaultParameters.add("intNull");
		}
		if (sparse) {
			// initialize also cols, rowindex, usedCells
			defaultParameters.add("NULL");
			defaultParameters.add("NULL");
			defaultParameters.add("intNull");
		}
		return defaultParameters;
	}

	public void createBoolMatrixRef(String refName, IntType[] dimensions, VarVisibility v, String defaultValue)
			throws UndefinedTranslationException, TypeException {
		createIndexMatrixRef(refName, dimensions, v, defaultValue);
	}

	public void createBoolMatrixRef(String refName, IntType[] dimensions, VarVisibility v)
			throws UndefinedTranslationException, TypeException {
		createIndexMatrixRef(refName, dimensions, v);
	}

	/**
	 * returns true if this block represents a function with the given param
	 * 
	 * @param param
	 * @return
	 */
	protected boolean isFunctionWithParam(Quadruple<GType, SymbolType, String, List<String>> param) {
		// overridden by CFunction that can return true
		return false;
	}

	/// ----------- THOSE TWO SHOULD BE CHANGED TOGETHER -----------------
	private List<String> getDimensionFieldsForUnknownStruct(String fieldName) {
		String dim1 = StructSymbols.matrixDimensionLabel(fieldName, 1);
		String dim2 = StructSymbols.matrixDimensionLabel(fieldName, 2);
		List<String> dims = new ArrayList<String>();
		dims.add(dim1);
		dims.add(dim2);
		return dims;
	}

	private String symbolType2Ctype(GType type) throws UndefinedTranslationException {
		switch (type.type()) {
		case BOOL:
		case INT:
			return "int";
		case SCALAR:
			return "double";
		case STRING:
			return "char";
		default:
			throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TYPE, null, type);
		}
	}

	protected String exprTypeToCType(AASTNode node) throws TypeException, UndefinedTranslationException {
		return exprTypeToCType(getExprGeneralized(node));
	}

	protected String exprTypeToCType(GType type) throws TypeException, UndefinedTranslationException {
		if (type == null)
			throw new TypeException(CErrorMessage.INTERNAL_NULL_ARGUMENT, null, "exprTypeToCType");

		switch (type.type()) {
		case INT:
		case BOOL:
			return "int ";
		case SCALAR:
			return "double ";
		case STRING:
			return "char *";
		case VOID:
			return "void ";
		case MATRIX_ACCESS_SLICE:
			SliceType sltype = (SliceType) type;
			GType start = sltype.slices().first();
			if (GType.get(BType.INT).equals(start)) {
				return SLICE_TYPE_I + " ";
			} else if (GType.get(BType.SCALAR).equals(start))
				return SLICE_TYPE_D + " ";
			else
				throw new TypeException(CErrorMessage.UNSUPPORTED_TYPE, null, type, BType.MATRIX_ACCESS_SLICE);
		case MATRIX:
			MatrixType mtype = (MatrixType) type;
			switch (mtype.of().type()) {
			case SCALAR:
				createMatrixType("double", mtype.dims());
				return getMatrixTypeName("double", mtype.dims().length, mtype.isSparse());
			case INT:
			case BOOL:
				createMatrixType("int", mtype.dims());
				// always create also double counterpart, since it is used in std avg and mean
				// functions even if they don't appear in the program
				createMatrixType("double", mtype.dims());
				return getMatrixTypeName("int", mtype.dims().length, mtype.isSparse());
			default:
				throw new TypeException(CErrorMessage.UNSUPPORTED_TYPE, null, type, BType.MATRIX);
			}
		case STRUCT:
			return getStructParameterType((StructType) type) + " ";
		case UNKNOWN:
		default:
			throw new TypeException(CErrorMessage.UNSUPPORTED_TYPE, null, type);
		}
	}

	private void writeStructDeclaration(BufferedWriter bf, String structName, StructType outputSymbols)
			throws TypeException, UndefinedTranslationException, IOException {

		StringBuffer buf = new StringBuffer();
		List<StructType> subStructs = new LinkedList<StructType>();

		buf.append("typedef struct ").append(structName).append("_internal_name").append("{").append(NL);
		Iterator<Tuple<List<GType>, String>> it = outputSymbols.iterFields();
		while (it.hasNext()) {
			Tuple<List<GType>, String> outputSymbol = it.next();
			String cType = "";
			List<GType> otype = outputSymbol.first();
			GType curType = ((LinkedList<GType>) otype).peekLast();
			if (curType instanceof StructType)
				subStructs.add((StructType) curType);
			cType = exprTypeToCType(curType);
			String fieldName = outputSymbol.second();

			buf.append(cType).append(" ").append(fieldName);
			buf.append(";").append(NL);
		}
		buf.append("} ").append(structName).append(";").append(NL);

		if (!subStructs.isEmpty())
			for (StructType s : subStructs) {
				String sName = getStructName(s);
				createStructType(sName, s);
			}

		try {
			bf.write(buf.toString());
		} catch (Exception e) {
		}
	}

	protected String exprTypeToCType(String funName, boolean handle_as_struct_ptr, List<AASTNode> outputSymbols)
			throws TypeException, UndefinedTranslationException {

		if (handle_as_struct_ptr) {
			// only a struct as output
			GType outType = getExprGeneralized(outputSymbols.get(0));
			if (outputSymbols.size() == 1 && GType.get(BType.STRUCT).equals(outType))
				return getStructName((StructType) outType) + " *";
			// a list of non-struct types. return a struct, assume struct name
			// is the name of the function.

			String structName = funName + STRUCT_SUFFIX;
			return structName + " *";
		} else if (outputSymbols.size() == 1)
			return exprTypeToCType(getExprGeneralized(outputSymbols.get(0)));
		else
			throw new TypeException(CErrorMessage.INTERNAL_MULTIPLE_OUTPUT_SYMBOLS_NOT_STRUCT_POINTER, null);
	}

	@Override
	public void prepareOutput() {
	}

	@Override
	public void finalizeOutput() {
		try {
			for (Function cfun : storedFun) {
				// if inside function
				try {
					hwriter.write(addUserFunction(cfun));
				} catch (IOException | TypeException e) {
					logger.error("Error creating header file functions.h: " + e.getMessage());
					logger.debug("Error creating header file functions.h: " + e.getMessage(), e);
				}
			}

		} catch (Exception e) {
			logger.error("Error creating header file functions.h: " + e.getMessage());
			logger.debug("Error creating header file functions.h: " + e.getMessage(), e);
		}
	}

	public String addUserFunction(Function cfun) throws TypeException, UndefinedTranslationException {
		String ret = null;
		try {
			ret = cfun.genFunctionDefinition().first();
		} catch (TypeException e) {
			logger.debug("Failed to find symbol: " + e.getMessage(), e);
			throw e;
		}

		return "extern " + ret + ";" + NL;
	}

	public void addUserStruct(String symbol, Triple<GType, SymbolType, List<String>> symbolEnv) {
		// called only in the main backend. Main backend will be the only one
		// with a global structure filled
		globalStructs.put(symbol, symbolEnv);
	}

	@Override
	public String extension() {
		return "c";
	}

	/**
	 * Code generation phase
	 * 
	 * @throws SyntaxException
	 */
	@Override
	public void genCode(AAST program) throws TypeException, UnboundException, SyntaxException {
		// Get the file reference
		Path path = Paths.get(outputFilePath);

		try {
			BufferedWriter writer;
			writer = Files.newBufferedWriter(path);
			StringBuffer sb = new StringBuffer();

			outputHeaders(sb, path);
			writeGlobals(sb);
			// writeGlobalStructures(swriter);

			outputMainCode(sb, program, path);

			writer.write(sb.toString());
			writer.close();
		} catch (IOException | GException e) {
			logger.error(new OutputException(CErrorMessage.USER_IO_ERROR_WRITING_LIBRARIES, null, e).stringify());
		}

		// gen sub-blocks
		for (Function cfun : storedFun)
			cfun.genCode(program);
	}

	protected String getCopyright() {
		StringBuffer sb = new StringBuffer();
		sb.append(TAB + "printf(\"" + Constants.programName
				+ ": Copyright (c) 2021, Fondazione The Microsoft Research - University of Trento Centre for Computational and Systems Biology (COSBI). All rights reserved.\\n\");"
				+ NL);

		sb.append("#ifdef TCMALLOC" + NL);
		sb.append(TAB + "printf(\"TC Malloc: Copyright (c) 2005, Google Inc. All rights reserved.\\n\");" + NL);
		sb.append("#endif" + NL);

		sb.append("#ifdef QSPCC_SUNDIALS_VERSION" + NL);
		sb.append(TAB
				+ "printf(\"Sundials: Copyright (c) 2002-2019, Lawrence Livermore National Security and Southern Methodist University. All rights reserved.\\n\");"
				+ NL);
		sb.append("#endif" + NL);
		return sb.toString();
	}

	protected void outputMainCode(StringBuffer writer, AAST program, Path path) throws IOException, GException {

		// output main code
		writer.append("int main() {" + NL);
		// copyright notice
		writer.append(getCopyright());

		writer.append(TAB + "printf(\"\\n\");" + NL);

		writer.append("#ifndef DEBUG" + NL);
		writer.append(TAB
				+ "printf(\"INFO: Optimized execution:\\n - Extra run-time checks disabled.\\n   To enable use `make sanitychecks=on', see "
				+ ErrorMessage.getCompilationOptionsLink(program.rootNode())
				+ "\\n - Timeseries dynamics not saved.\\n   To enable use `make dump=1000', see "
				+ ErrorMessage.getCompilationOptionsLink(program.rootNode()) + "\\n\");" + NL);
		writer.append("#endif" + NL);
		// write initialization
		pendingInitializationToBuffer();
		writer.append(getInitializationBuffer().toString());
		// write body
		String main = getBody();
		// actual write
		writer.append(main);
		writer.append("}" + NL + NL);
	}

	protected String getBody() {
		String main = translation.toString();
		if (isFunctionalProgram()) {
			// call the first storedFun (first function defined in this script)
			Function cfun = storedFun.get(storedFun.size() - 1);
			// empty strings are void nodes
			if (cfun.getFunctionParams().isEmpty() || cfun.getFunctionParams().get(0).equals(""))
				main = TAB + cfun.functionName() + "();" + NL;
		}
		return main;
	}

	public boolean isFunctionalProgram() {
		return emptyStatements() && is_maincompilationunit && (!storedFun.isEmpty() || !inlineFun.isEmpty());
	}

	protected void outputHeaders(StringBuffer writer, Path path) throws IOException {
		// disclaimer
		writer.append("/* " + path.getFileName() + " -- autogenerated " + NL);
		writer.append("* COSBI, 2017. " + NL);
		writer.append("* " + NL);
		writer.append("* Autogenerated using QSP-cc " + NL);
		writer.append("* a COSBI compiler-based translator tool " + NL);
		writer.append("* by Danilo Tomasoni, Rosario Lombardo. " + NL);
		writer.append("* " + NL);
		writer.append("* For more info, see COSBI web page: " + NL);
		writer.append("* " + NL);
		writer.append("* https://www.cosbi.eu/research/prototypes/qspcc" + NL);
		writer.append("*/ " + NL);

		// header...
		writer.append("#ifndef __HEADERS__" + NL);
		writer.append("#define __HEADERS__" + NL);
		writer.append("#include <stdio.h>" + NL);
		writer.append("#include <time.h>" + NL);
		writer.append("#include <string.h>" + NL);
		writer.append("#include <stdlib.h>" + NL);
		writer.append("#include <nvector/nvector_serial.h>" + NL);
		writer.append("#include <sundials/sundials_types.h>" + NL);
		writer.append("#include \"structures.h\"" + NL);
		writer.append("#include \"load-structures.h\"" + NL);
		writer.append("#include \"user-structures.h\"" + NL);
		writer.append("#include \"user-sundials-structures.h\"" + NL);
		if (storeInput)
			writer.append("#include \"loaded-csv.h\"" + NL);
		// NOTICE: creating this header is responsibility of the caller
		writer.append("#include \"loadLib.h\"" + NL);
		writer.append("#include \"sundialsLib.h\"" + NL);
		writer.append("#include \"memoryLib.h\"" + NL);
		writer.append("#include \"matrixLib.h\"" + NL);
		writer.append("#include \"sparseLib.h\"" + NL);
		writer.append("#include \"saveLib.h\"" + NL);
		writer.append("#include \"variables.h\"" + NL);
		writer.append("#include \"functions.h\"" + NL);
		// writer.append("#include \"extern-inline-functions.h\"" + NL);
		writer.append("#endif" + NL + NL);

	}

	protected String currentUserDataStructName;
	protected boolean initRandNumbers;

	/*
	 * creates the struct used to populate user_data (sundial object) with the
	 * global variables
	 */
	protected void createUserDataStruct(List<AASTNode> environmentVariableNodes) throws UndefinedTranslationException {
		currentUserDataStructName = USER_DATA_STRUCT_PREFIX + (String.valueOf(++user_data_struct_number));
		StringBuffer localStruct = new StringBuffer();

		localStruct.append("typedef struct ").append(currentUserDataStructName).append("{").append(NL);

		for (AASTNode environmentVariableNode : environmentVariableNodes) {
			String fieldName = environmentVariableNode.symbol();
			GType curType = getExprGeneralized(environmentVariableNode);
			switch (curType.type()) {
			case STRING:
				localStruct.append(TAB).append("char *");
				localStruct.append(fieldName).append(";").append(NL);
				break;
			case INT:
			case BOOL:
				localStruct.append(TAB).append("int ");
				localStruct.append(fieldName).append(";").append(NL);
				break;
			case SCALAR:
				localStruct.append(TAB).append("double ");
				localStruct.append(fieldName).append(";").append(NL);
				break;
			case STRUCT:
				localStruct.append(TAB).append(getStructName((StructType) curType)).append(" ");
				localStruct.append(fieldName).append(";").append(NL);
				if (curType.isInput())
					localStruct.append(TAB).append("INPUT").append(" *").append(curType.inputName()).append(";")
							.append(NL);
				break;
			case MATRIX_ACCESS_SLICE:
				SliceType sltype = (SliceType) curType;
				if (sltype.slices() == null)
					throw new UndefinedTranslationException(CErrorMessage.INTERNAL_CREATE_UNDEFINED_SLICE, null,
							fieldName);
				GType start = sltype.slices().first();
				if (GType.get(BType.INT).equals(start))
					localStruct.append(TAB).append(SLICE_TYPE_I + " ");
				else if (GType.get(BType.SCALAR).equals(start))
					localStruct.append(TAB).append(SLICE_TYPE_D + " ");

				else
					throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TYPE, null, curType);
				localStruct.append(fieldName).append(";").append(NL);

				// write also the dimensions, just in case of a matrix
				localStruct.append(TAB).append("int ").append(fieldName).append(MATRIX_DIMS_SUFFIX).append(";")
						.append(NL);
				break;
			case MATRIX:
				MatrixType mtype = ((MatrixType) curType);

				String type = "double";
				if (mtype.of().equals(BType.INT) || mtype.of().equals(BType.BOOL)) {
					type = "int";
				}

				String matrixWrapperName = getMatrixTypeName(type, mtype.dims().length, mtype.isSparse());

				localStruct.append(matrixWrapperName).append(TAB).append(" ");
				localStruct.append(fieldName).append(";").append(NL);
				break;
			default:
				throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TYPE, null, curType);
			}
		}

		localStruct.append("} ").append(currentUserDataStructName).append(";").append(NL);

		try {
			ussunwriter.write(localStruct.toString());
		} catch (IOException e) {
			logger.error("Error adding user structure: " + e.getMessage());
			logger.debug("Error adding user structure " + currentUserDataStructName + ": " + e.getMessage(), e);
		}
	}

	protected void pendingInitializationToBuffer() throws UndefinedTranslationException, IOException {
		if (initRandNumbers)
			newTranslation(getInitializationBuffer(), "srand(time(NULL));");
	}

	protected void writeGlobals(StringBuffer writer) throws IOException, TypeException {
		// global variables
		writer.append(getGlobalBuffer().toString());
		writer.append(NL);
		// local struct
		writer.append(getLocalStructBuffer().toString());
		writer.append(NL);
		// local functions (this block only)
		writer.append(getLocalFunctionsBuffer().toString());
		writer.append(NL);
	}

	/**
	 * Transform an abstract version of a slice <start,step,end> to an actual index
	 * matrix that contains all the values that belongs to the slice.
	 * 
	 * @param slice
	 * @param idxsMatrix
	 * @throws UndefinedTranslationException
	 */

	/**
	 * outputs on the current function
	 * 
	 * @param string
	 */
	public void newTranslation(AASTNode node, String string) {
		newTranslation(getCurrentTranslationBuffer(node).second(), string);
	}

	public void newTranslation(StringBuffer buff, String string) {
		newTranslation(buff, string, true);
	}

	public void newTranslation(StringBuffer buff, String string, boolean indent) {
		if (curFun == null)
			translation(buff, string, indent);
		else
			curFun.translation(buff, string, indent);
	}

	public void translation(StringBuffer buff, String string) {
		translation(buff, string, true);
	}

	public void translation(StringBuffer buff, String string, boolean should_indent) {
		String[] parts = string.split("\\" + NL);
		// a new translation that comes from walker
		for (String str : parts) {
			if (should_indent)
				buff.append(indentTabs(indent));
			buff.append(str);
			buff.append(NL);
		}
	}

	private Triple<AASTNode, StringBuffer, Set<String>> getCurrentTranslationBuffer(AASTNode curRoot) {
		List<Triple<AASTNode, StringBuffer, Set<String>>> tblist = null;
		if (curFun != null) {
			tblist = curFun.getTranslationBufferList();
		} else
			tblist = translationBufferList;

		int last_el_idx = tblist.size() - 1;
		Triple<AASTNode, StringBuffer, Set<String>> curtb = tblist.get(last_el_idx);
		// if buffer of if statement
		if (curtb.first().parentExists(NodeType.IF)
				// and translating expression inside an elseif
				&& curRoot != null && curRoot.parentExists(NodeType.ELSEIF)
				&& curRoot.parent(NodeType.ELSEIF).child(NodeType.EXPRESSION).parentOf(curRoot)) {
			AASTNode ifStmtList = curtb.first().parent(NodeType.IF).child(NodeType.STATEMENT_LIST);
			int i;
			// search top-if buffer
			for (i = last_el_idx; i >= 0; --i)
				if (tblist.get(i).first().equals(ifStmtList))
					break;
			if (i > 0)
				return tblist.get(i - 1);
			else
				return tblist.get(0);
		} else
			return curtb;
	}

	protected Triple<AASTNode, StringBuffer, Set<String>> getInitialTranslationBuffer() {
		if (curFun != null) {
			return curFun.getTranslationBufferList().get(0);
		}
		return translationBufferList.get(0);
	}

	private void addTranslationBuffer(AASTNode curRoot) {
		List<Triple<AASTNode, StringBuffer, Set<String>>> curBufList = null;
		if (curFun == null) {
			curBufList = translationBufferList;
		} else {
			curBufList = curFun.getTranslationBufferList();
		}
		// avoid re-adding the same node twice
		boolean add = true;
		for (Triple<AASTNode, StringBuffer, Set<String>> buf : curBufList)
			add &= !buf.first().equals(curRoot);

		if (add)
			curBufList
					.add(new Triple<AASTNode, StringBuffer, Set<String>>(curRoot, new StringBuffer(), new HashSet<>()));
	}

	/**
	 * reset the translation buffer to the content given as-is
	 * 
	 * @param content
	 */
	public void clearCurrentTranslationBuffer(AASTNode node, String content) {
		clearCurrentTranslationBuffer();
		getCurrentTranslationBuffer(node).second().append(content);
	}

	public void clearCurrentTranslationBuffer() {
		if (curFun == null) {
			int idx = translationBufferList.size() - 1;
			translationBufferList.set(idx, new Triple<AASTNode, StringBuffer, Set<String>>(
					translationBufferList.get(idx).first(), new StringBuffer(), new HashSet<String>()));
		} else
			curFun.clearCurrentTranslationBuffer();
	}

	private void removeTranslationBuffer() {
		if (curFun == null)
			removeLastTranslationBuffer();
		else
			curFun.removeLastTranslationBuffer();
	}

	public void removeLastTranslationBuffer() {
		translationBufferList.remove(translationBufferList.size() - 1);
	}

	/**
	 * outputs on the current function
	 * 
	 * @param string
	 */
	public void newComment(AASTNode curRoot, String multiline) {
		if (curFun == null)
			translateComment(curRoot, multiline);
		else
			curFun.translateComment(curRoot, multiline);
	}

	public void newComment(AASTNode curRoot) {
		AASTNode topNode;
		if (curRoot.parentExists(NodeType.ASSIGN)) {
			topNode = curRoot.parent(NodeType.ASSIGN);
		} else if (curRoot.parentExists(NodeType.GASSIGN)) {
			topNode = curRoot.parent(NodeType.GASSIGN);
		} else
			topNode = curRoot;
		newComment(curRoot, topNode.code() + " at line " + topNode.lineNumber());
	}

	@StepType(method = StepType.Function.COMMENT)
	public void translateComment(AASTNode curRoot, String multiline) {
		StringBuffer sb = getCurrentTranslationBuffer(curRoot).second();
		sb.append(indentTabs(indent)).append("/*").append(NL);
		for (String line : multiline.split("\\r?\\n"))
			sb.append(indentTabs(indent)).append(" * ").append(line).append(NL);
		sb.append(indentTabs(indent)).append(" */").append(NL);
	}

	/**
	 * set field values based on command-line options or backend-specific options
	 * 
	 * @throws InputException
	 */
	protected void populateFieldsFromOptions() throws InputException {

		List<String> preallocMemCheck = backendSpecificOptions.get(ENABLE_PREALLOCATED_MEMORY_CHECK);
		if (preallocMemCheck != null)
			enablePreallocatedCheck = true;
		else
			enablePreallocatedCheck = defaultEnablePreallocatedCheck;

		List<String> mklSymLin = backendSpecificOptions.get(NONSYMM_SPARSE_LINEAR_PROBLEM);
		if (mklSymLin != null && !mklSymLin.isEmpty()) {
			sparseMatrixType = mklSymLin.get(0);

			if (supportedSparseMatrixTypes.indexOf(sparseMatrixType) < 0)
				throw new InputException(CErrorMessage.USER_CLI_PARAM_EXCEPTION, null, NONSYMM_SPARSE_LINEAR_PROBLEM,
						supportedSparseMatrixTypes, sparseMatrixType);
		} else
			sparseMatrixType = defaultUseSparseLinear;

		// reads whether is the case of storing input or not
		List<String> storeInputValues = backendSpecificOptions.get(STORE_INPUT);
		if (storeInputValues != null)
			storeInput = true;
		else
			storeInput = defaultStoreInput;

		List<String> templatevalues = backendSpecificOptions.get(SUN_VER_OPTION);
		if (templatevalues != null && !templatevalues.isEmpty()) {
			sundialsVersion = templatevalues.get(0);
			try {
				Integer ver = Integer.parseInt(sundialsVersion);
				if (supportedSundialsVersions.indexOf(sundialsVersion) < 0)
					throw new InputException(CErrorMessage.USER_CLI_PARAM_EXCEPTION, null, SUN_VER_OPTION,
							supportedSundialsVersions, sundialsVersion);
			} catch (Exception e) {
				throw new InputException(CErrorMessage.USER_CLI_PARAM_EXCEPTION, null, SUN_VER_OPTION, sundialsVersion);
			}
		} else
			sundialsVersion = defaultSunVer;

		templatevalues = backendSpecificOptions.get(SUN_INCLUDE);
		if (templatevalues != null && !templatevalues.isEmpty())
			sunincludepath = Paths.get(templatevalues.get(0));
		else
			sunincludepath = defaultSunPath;

		templatevalues = backendSpecificOptions.get(SUN_LIB);
		if (templatevalues != null && !templatevalues.isEmpty())
			sunlibpath = Paths.get(templatevalues.get(0));
		else
			sunlibpath = defaultSunLibPath;

		templatevalues = backendSpecificOptions.get(STD_LIB);
		if (templatevalues != null && !templatevalues.isEmpty())
			stdlibpath = Paths.get(templatevalues.get(0));
		else
			stdlibpath = defaultStdLibPath;

		templatevalues = backendSpecificOptions.get(TCMALLOC);
		if (templatevalues != null && !templatevalues.isEmpty())
			tcmallocpath = Paths.get(templatevalues.get(0));
		else
			tcmallocpath = defaultTCMallocPath;

		templatevalues = backendSpecificOptions.get(TCMALLOCINCLUDE);
		if (templatevalues != null && !templatevalues.isEmpty())
			tcmallocinclude = Paths.get(templatevalues.get(0));
		else
			tcmallocinclude = defaultTCMallocInclude;
	}

	@Override
	public void onTranslationStart(Program program) throws Exception {
		programName = program.mainCompilationUnit().name();
		// clear all
		getCFunctionImplementationManagerInstance().clearInstance();
		structs = new HashSet<String>();
		matrixTypes = new HashSet<String>();
		matrixCopyOnFunctions = new HashSet<String>();
		alreadyAllocatedInputArrays = new HashSet<String>();
		maxMatrixDimension = 0;
		matricesDefined = new ArrayList<Triple<String, String, Integer>>(3);
		summarizeByCol = new HashSet<Integer>(3);
		globalVariables = new HashSet<String>();
		wrapFunctions = new HashSet<String>();
		skip_function_names = new HashSet<String>();

		populateFieldsFromOptions();

		// called only in ismaincompilationunit = true
		vwriter = Files.newBufferedWriter(mainPath().resolve("variables.h"));
		hwriter = Files.newBufferedWriter(mainPath().resolve("functions.h"));
		ushwriter = Files.newBufferedWriter(mainPath().resolve("user-structures.h"));
		ussunwriter = Files.newBufferedWriter(mainPath().resolve("user-sundials-structures.h"));
		uscwriter = Files.newBufferedWriter(mainPath().resolve("user_structures.c"));
		storedcsvhwriter = Files.newBufferedWriter(mainPath().resolve("loaded-csv.h"));
		storedcsvcwriter = Files.newBufferedWriter(mainPath().resolve("loaded-csv.c"));

		// write global variables
		vwriter.write("#ifndef __GLOBAL_VARIABLES__" + NL);
		vwriter.write("#define __GLOBAL_VARIABLES__" + NL);
		vwriter.write("/* user-defined global variables */" + NL);

		// write header only in main
		hwriter.write("#ifndef __USER_FUNCTIONS__" + NL);
		hwriter.write("#define __USER_FUNCTIONS__" + NL);
		hwriter.write("/* user-defined functions */" + NL);

		// user-defined sundials structures to hold extra parameters
		// to pass to right-hand-side function
		ussunwriter.write("#ifndef __USER_STRUCT_SUN__" + NL);
		ussunwriter.write("#define __USER_STRUCT_SUN__" + NL);
		ussunwriter.write("/* user-defined structures for Sundials */" + NL);
		ussunwriter.write("#include \"load-structures.h\"" + NL);
		ussunwriter.write("#include \"user-structures.h\"" + NL);
		ussunwriter.write("#include \"matrixLib.h\"" + NL + NL);

		// write header only in main
		ushwriter.write("#ifndef __USER_STRUCT__" + NL);
		ushwriter.write("#define __USER_STRUCT__" + NL);
		ushwriter.write("/* base structures */" + NL);
		ushwriter.write("#include \"structures.h\"" + NL);
		ushwriter.write("/* user-defined structures */" + NL);
		ushwriter.write("#include \"load-structures.h\"" + NL + NL);

		uscwriter.write("#ifndef __USER_IMPLEMENTED_STRUCT_FUNCTIONS__" + NL);
		uscwriter.write("#define __USER_IMPLEMENTED_STRUCT_FUNCTIONS__" + NL);
		uscwriter.write("/*user implemented struct functions*/" + NL);

		if (storeInput) {
			storedcsvhwriter.write("#ifndef __STORED_CSV__" + NL);
			storedcsvhwriter.write("#define __STORED_CSV__" + NL);
			storedcsvhwriter.write("/*constants loaded from csv*/" + NL);

			storedcsvcwriter.write("#ifndef __STORED_CSV_C__" + NL);
			storedcsvcwriter.write("#define __STORED_CSV_C__" + NL);
			storedcsvcwriter.write("#include <math.h>" + NL);
			storedcsvcwriter.write("#include \"loaded-csv.h\"" + NL);
		}

		// headers
		// header...
		uscwriter.write("#ifndef __HEADERS__" + NL);
		uscwriter.write("#define __HEADERS__" + NL);
		uscwriter.write("#include <stdio.h>" + NL);
		uscwriter.write("#include <time.h>" + NL);
		uscwriter.write("#include <string.h>" + NL);
		uscwriter.write("#include <stdlib.h>" + NL);
		uscwriter.write("#include <nvector/nvector_serial.h>" + NL);
		uscwriter.write("#include <sundials/sundials_types.h>" + NL);
		uscwriter.write("#include \"structures.h\"" + NL);
		uscwriter.write("#include \"load-structures.h\"" + NL);
		uscwriter.write("#include \"user-structures.h\"" + NL);
		uscwriter.write("#include \"user-sundials-structures.h\"" + NL);
		if (storeInput)
			uscwriter.write("#include \"loaded-csv.h\"" + NL);
		// NOTICE: creating this header is responsibility of the caller
		uscwriter.write("#include \"loadLib.h\"" + NL);
		uscwriter.write("#include \"sundialsLib.h\"" + NL);
		uscwriter.write("#include \"memoryLib.h\"" + NL);
		uscwriter.write("#include \"matrixLib.h\"" + NL);
		uscwriter.write("#include \"saveLib.h\"" + NL);
		uscwriter.write("#include \"functions.h\"" + NL);
		uscwriter.write("#endif" + NL + NL);
	}

	protected List<String> sanitizeFunNames(List<String> fun_names) {
		if (fun_names.contains(programName))
			fun_names.remove(programName);
		fun_names.removeAll(skip_function_names);
		return fun_names;
	}

	/**
	 * Copy to translation folder all the accessory files both from MIST (if
	 * translation in C) and other custom libraries used in translation
	 * 
	 * @author tomasoni
	 */
	@Override
	public void onTranslationEnd(Program program, List<String> fun_names) throws Exception {

		// avoid circular dependency
		fun_names = sanitizeFunNames(fun_names);

		// write list of macros to set dimensions for a generic matrix
		writeDimensionUpdaterMatrices();
		writeMatrixNulls();
		writeDynamicMatrixFunctions();

		try {
			Path execPath = Paths.get(destFolder);

			closeFileHandles();

			String glbalmkfile = getGlobalMakefile(getGlobalMakefileInputStream(), programName, fun_names);

			// write it
			BufferedWriter mkwriter = Files.newBufferedWriter(execPath.resolve("Makefile"));
			mkwriter.write(glbalmkfile);

			mkwriter.close();

			Path destpath = mainPath();

			// WRITING SBATCH for slurm
			String slurm = getSlurmBatch(getSlurmInputStream(), programName, fun_names);

			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("simulation.sbatch"));
			mkwriter.write(slurm);

			mkwriter.close();

			// WRITING plot facility (with gnuplot) can be used when compiled with dump=on
			String plot = getPlotBatch(getPlotInputStream(), programName, fun_names);

			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("plot.sh"));
			mkwriter.write(plot);

			mkwriter.close();

			// WRITING MAKEFILE for linux
			String makefile = getMakefile(getMakefileInputStream(), programName, fun_names);

			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("Makefile"));
			mkwriter.write(makefile);

			mkwriter.close();

			// WRITING MAKEFILE for mac
			makefile = getMacMakefile(getMacMakefileInputStream(), programName, fun_names);

			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("Makefile.mac"));
			mkwriter.write(makefile);

			mkwriter.close();

			// WRITING C/H files
			String headerLib = getMemoryLibH(getMemoryLibHInputStream(), programName, fun_names);

			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("memoryLib.h"));
			mkwriter.write(headerLib);

			mkwriter.close();

			String headerLibImpl = getMemoryLibC(getMemoryLibCInputStream(), programName, fun_names);
			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("memoryLib.c"));
			mkwriter.write(headerLibImpl);

			mkwriter.close();

			headerLib = getMatrixLibH(getMatrixLibHInputStream(), programName, fun_names);

			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("matrixLib.h"));
			mkwriter.write(headerLib);

			mkwriter.close();

			headerLibImpl = getMatrixLibC(getMatrixLibCInputStream(), programName, fun_names);

			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("matrixLib.c"));
			mkwriter.write(headerLibImpl);

			mkwriter.close();

			headerLib = getSparseLibH(getSparseLibHInputStream(), programName, fun_names);

			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("sparseLib.h"));
			mkwriter.write(headerLib);

			mkwriter.close();

			headerLibImpl = getSparseLibC(getSparseLibCInputStream(), programName, fun_names);

			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("sparseLib.c"));
			mkwriter.write(headerLibImpl);

			mkwriter.close();

			headerLibImpl = getQsortLibH(getQsortLibHInputStream(), programName, fun_names);

			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("qsort-inline.h"));
			mkwriter.write(headerLibImpl);

			mkwriter.close();

			headerLibImpl = getSaveLibC(getSaveLibCInputStream(), programName, fun_names);
			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("saveLib.c"));
			mkwriter.write(headerLibImpl);

			mkwriter.close();

			headerLib = getSaveLibH(getSaveLibHInputStream(), programName, fun_names);
			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("saveLib.h"));
			mkwriter.write(headerLib);

			mkwriter.close();

			String sundialsLib = getSundialsLibH(getSundialsLibHInputStream(), programName, fun_names);

			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("sundialsLib.h"));
			mkwriter.write(sundialsLib);

			mkwriter.close();

			String sundialsLibImpl = getSundialsLibC(getSundialsLibCInputStream(), programName, fun_names);

			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("sundialsLib.c"));
			mkwriter.write(sundialsLibImpl);

			mkwriter.close();

			headerLibImpl = getLoadStructuresLibH(getLoadStructuresHInputStream(), programName, fun_names);
			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("load-structures.h"));
			mkwriter.write(headerLibImpl);

			mkwriter.close();

			headerLib = getLoadLibH(getLoadLibHInputStream(), programName, fun_names);
			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("loadLib.h"));
			mkwriter.write(headerLib);

			mkwriter.close();

			headerLibImpl = getLoadLibC(getLoadLibCInputStream(), programName, fun_names);
			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("loadLib.c"));
			mkwriter.write(headerLibImpl);

			mkwriter.close();

			String structures = getStructuresH(getStructuresHInputStream(), programName, fun_names);
			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("structures.h"));
			mkwriter.write(structures);

			mkwriter.close();

			String uthash = getUthashH(getUthashHInputStream(), programName, fun_names);
			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("uthash.h"));
			mkwriter.write(uthash);

			mkwriter.close();

			// write mat2csv.m
			String mat2csv = getMat2CSV(getMat2CSVInputStream(), programName, fun_names);
			// write it
			mkwriter = Files.newBufferedWriter(destpath.resolve("mat2csv.m"));
			mkwriter.write(mat2csv);

			mkwriter.close();

		} catch (IOException e) {
			OutputException ex = new OutputException(CErrorMessage.USER_IO_ERROR_WRITING_LIBRARIES, null, e);
			logger.error(ex.stringify());
			logger.debug(ex.stringify(), e);
		}
	}

	protected String getSundialsLibC(InputStream fStream, String programName, List<String> fun_names)
			throws IOException {
		String sundialsLibImpl = IOUtils.toString(fStream, "UTF-8");
		return sundialsLibImpl;
	}

	protected InputStream getSundialsLibCInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader()
				.getResourceAsStream("resources/sundialsLib_" + sundialsVersion + ".c");

		if (fStream == null) {
			String beforepath = getBeforePath("sundialsLib_" + sundialsVersion + ".c");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("sundialsLib_" + sundialsVersion + ".c").toString());
		}
		return fStream;
	}

	protected String getBeforePath(String csvName) {
		// hack for in-eclipse execution
		return "";
	}

	protected String getSparseLibH(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		return IOUtils.toString(fStream, "UTF-8");
	}

	protected String getSparseLibC(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		return IOUtils.toString(fStream, "UTF-8");
	}

	protected String getQsortLibH(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		return IOUtils.toString(fStream, "UTF-8");
	}

	protected String getLoadStructuresLibH(InputStream fStream, String programName, List<String> fun_names)
			throws IOException {
		return IOUtils.toString(fStream, "UTF-8");
	}

	protected String getSundialsLibH(InputStream fStream, String programName, List<String> fun_names)
			throws IOException {
		String sundialsLib = IOUtils.toString(fStream, "UTF-8");
		sundialsLib = sundialsLib.replace("<QSPCC_SUNDIALS_VERSION>", sundialsVersion);
		return sundialsLib;
	}

	protected String getSaveLibC(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		return IOUtils.toString(fStream, "UTF-8");
	}

	protected String getSaveLibH(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		return IOUtils.toString(fStream, "UTF-8");
	}

	protected String getLoadLibC(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		return IOUtils.toString(fStream, "UTF-8");
	}

	protected String getLoadLibH(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		return IOUtils.toString(fStream, "UTF-8");
	}

	protected InputStream getQsortLibHInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/qsort-inline.h");

		if (fStream == null) {
			String beforepath = getBeforePath("qsort-inline.h");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("qsort-inline.h").toString());
		}
		return fStream;
	}

	protected InputStream getSaveLibHInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/saveLib.h");

		if (fStream == null) {
			String beforepath = getBeforePath("saveLib.h");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("saveLib.h").toString());
		}
		return fStream;
	}

	protected InputStream getStructuresHInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/structures.h");

		if (fStream == null) {
			String beforepath = getBeforePath("structures.h");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("structures.h").toString());
		}
		return fStream;
	}

	protected InputStream getUthashHInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/uthash.h");

		if (fStream == null) {
			String beforepath = getBeforePath("uthash.h");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("uthash.h").toString());
		}
		return fStream;
	}

	protected InputStream getMat2CSVInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/mat2csv.m");

		if (fStream == null) {
			String beforepath = getBeforePath("mat2csv.m");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("mat2csv.m").toString());
		}
		return fStream;
	}

	protected InputStream getLoadLibHInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/loadLib.h");

		if (fStream == null) {
			String beforepath = getBeforePath("loadLib.h");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("loadLib.h").toString());
		}
		return fStream;
	}

	protected InputStream getSaveLibCInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/saveLib.c");

		if (fStream == null) {
			String beforepath = getBeforePath("saveLib.h");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("saveLib.c").toString());
		}
		return fStream;
	}

	protected InputStream getLoadLibCInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/loadLib.c");

		if (fStream == null) {
			String beforepath = getBeforePath("loadLib.c");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("loadLib.c").toString());
		}
		return fStream;
	}

	protected InputStream getLoadStructuresHInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/load-structures.h");

		if (fStream == null) {
			String beforepath = getBeforePath("load-structures.h");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("load-structures.h").toString());
		}
		return fStream;
	}

	protected InputStream getSundialsLibHInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/sundialsLib.h");

		if (fStream == null) {
			String beforepath = getBeforePath("sundialsLib.h");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("sundialsLib.h").toString());
		}
		return fStream;
	}

	protected InputStream getSparseLibHInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/sparseLib.h");

		if (fStream == null) {
			String beforepath = getBeforePath("sparseLib.h");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("sparseLib.h").toString());
		}
		return fStream;
	}

	protected InputStream getSparseLibCInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/sparseLib.c");

		if (fStream == null) {
			String beforepath = getBeforePath("sparseLib.c");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("sparseLib.c").toString());
		}
		return fStream;
	}

	protected String getMat2CSV(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		String headerLibImpl = IOUtils.toString(fStream, "UTF-8");
		return headerLibImpl;
	}

	protected String getMatrixLibC(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		String headerLibImpl = IOUtils.toString(fStream, "UTF-8");
		return headerLibImpl;
	}

	protected String getStructuresH(InputStream fStream, String programName, List<String> fun_names)
			throws IOException {
		return IOUtils.toString(fStream, "UTF-8");
	}

	protected String getUthashH(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		return IOUtils.toString(fStream, "UTF-8");
	}

	protected String getMatrixLibH(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		String headerLib = IOUtils.toString(fStream, "UTF-8");
		headerLib = headerLib.replace("<ROW_COL_MAJOR>", ((ROWMAJOR) ? "ROWMAJOR" : "COLMAJOR"));
		return headerLib;
	}

	protected InputStream getMatrixLibCInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/matrixLib.c");

		if (fStream == null) {
			String beforepath = getBeforePath("matrixLib.c");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("matrixLib.c").toString());
		}
		return fStream;
	}

	protected InputStream getMatrixLibHInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/matrixLib.h");

		if (fStream == null) {
			String beforepath = getBeforePath("matrixLib.h");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("matrixLib.h").toString());
		}
		return fStream;
	}

	protected String getMemoryLibH(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		String headerLib = IOUtils.toString(fStream, "UTF-8");
		return headerLib;
	}

	protected String getMemoryLibC(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		String headerLib = IOUtils.toString(fStream, "UTF-8");
		return headerLib;
	}

	protected InputStream getMemoryLibHInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/memoryLib.h");

		if (fStream == null) {
			String beforepath = getBeforePath("memoryLib.h");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("memoryLib.h").toString());
		}
		return fStream;
	}

	protected InputStream getMemoryLibCInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/memoryLib.c");

		if (fStream == null) {
			String beforepath = getBeforePath("memoryLib.c");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("memoryLib.c").toString());
		}
		return fStream;
	}

	protected String getMacMakefile(InputStream fStream, String programName, List<String> fun_names)
			throws IOException {
		String makefile = IOUtils.toString(fStream, "UTF-8");
		makefile = makefile.replace("<SUN_INCLUDE_PATH>", sunincludepath.toString());
		makefile = makefile.replace("<SUN_LIB_PATH>", sunlibpath.toString());
		makefile = makefile.replace("<STD_LIB_PATH>", stdlibpath.toString());
		makefile = makefile.replace("<TCMALLOC_LIB_PATH>", tcmallocpath.toString());
		makefile = makefile.replace("<TCMALLOC_INCLUDE_PATH>", tcmallocinclude.toString());
		makefile = makefile.replace("<PROGRAM_NAME>", programName);
		makefile = makefile.replace("<DEPENDENCIES>", String.join(" ", fun_names));
		return makefile;
	}

	protected InputStream getMacMakefileInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/Makefile.mac.template");

		if (fStream == null) {
			String beforepath = getBeforePath("Makefile.mac.template");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("Makefile.mac.template").toString());
		}
		return fStream;
	}

	protected String getMakefile(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		String makefile = IOUtils.toString(fStream, "UTF-8");
		makefile = makefile.replace("<SUN_INCLUDE_PATH>", sunincludepath.toString());
		makefile = makefile.replace("<SUN_LIB_PATH>", sunlibpath.toString());
		makefile = makefile.replace("<STD_LIB_PATH>", stdlibpath.toString());
		makefile = makefile.replace("<TCMALLOC_LIB_PATH>", tcmallocpath.toString());
		makefile = makefile.replace("<TCMALLOC_INCLUDE_PATH>", tcmallocinclude.toString());
		makefile = makefile.replace("<PROGRAM_NAME>", programName);
		makefile = makefile.replace("<DEPENDENCIES>", String.join(" ", fun_names));
		return makefile;
	}

	protected InputStream getMakefileInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/Makefile.template");

		if (fStream == null) {
			String beforepath = getBeforePath("Makefile.template");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("Makefile.template").toString());
		}
		return fStream;
	}

	protected InputStream getGlobalMakefileInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/Makefile.global");

		if (fStream == null) {
			String beforepath = getBeforePath("Makefile.global");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("Makefile.global").toString());
		}
		return fStream;
	}

	protected InputStream getSlurmInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/simulation.sbatch.template");

		if (fStream == null) {
			String beforepath = getBeforePath("simulation.sbatch.template");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("simulation.sbatch.template").toString());
		}
		return fStream;
	}

	protected InputStream getPlotInputStream() throws FileNotFoundException {
		InputStream fStream = C.class.getClassLoader().getResourceAsStream("resources/plot.sh");

		if (fStream == null) {
			String beforepath = getBeforePath("plot.sh");
			fStream = new FileInputStream(Paths.get(beforepath + "backends/CBackend/resources").toAbsolutePath()
					.resolve("plot.sh").toString());
		}
		return fStream;
	}

	protected String getPlotBatch(InputStream fStream, String programName, List<String> fun_names) throws IOException {
		String plot = IOUtils.toString(fStream, "UTF-8");
		return plot;
	}

	protected String getSlurmBatch(InputStream fStream, String programName, List<String> fun_names) throws IOException {

		String slurm = IOUtils.toString(fStream, "UTF-8");
		slurm = slurm.replace("<SUN_INCLUDE_PATH>", sunincludepath.toString());
		slurm = slurm.replace("<SUN_LIB_PATH>", sunlibpath.toString());
		slurm = slurm.replace("<TCMALLOC_LIB_PATH>", tcmallocpath.toString());
		slurm = slurm.replace("<TCMALLOC_INCLUDE_PATH>", tcmallocinclude.toString());
		slurm = slurm.replace("<PROGRAM_NAME>", programName);
		slurm = slurm.replace("<DEPENDENCIES>", String.join(" ", fun_names));
		return slurm;
	}

	protected String getGlobalMakefile(InputStream fStream, String programName, List<String> fun_names)
			throws IOException {
		// WRITING GLOBAL makefile

		String glbalmkfile = IOUtils.toString(fStream, "UTF-8");
		glbalmkfile = glbalmkfile.replace("<SUN_INCLUDE_PATH>", sunincludepath.toString());
		glbalmkfile = glbalmkfile.replace("<SUN_LIB_PATH>", sunlibpath.toString());
		glbalmkfile = glbalmkfile.replace("<TCMALLOC_LIB_PATH>", tcmallocpath.toString());
		glbalmkfile = glbalmkfile.replace("<TCMALLOC_INCLUDE_PATH>", tcmallocinclude.toString());
		glbalmkfile = glbalmkfile.replace("<PROGRAM_NAME>", programName);
		glbalmkfile = glbalmkfile.replace("<DEPENDENCIES>", String.join(" ", fun_names));

		return glbalmkfile;
	}

	protected void closeFileHandles() throws IOException {
		vwriter.write("#endif" + NL + NL);
		vwriter.close();

		hwriter.write("#endif" + NL + NL);
		hwriter.close();

		ussunwriter.write("#endif" + NL + NL);
		ussunwriter.close();

		ushwriter.write("#endif" + NL + NL);
		ushwriter.close();

		uscwriter.write("#endif" + NL + NL);
		uscwriter.close();

		if (storeInput)
			storedcsvhwriter.write("#endif" + NL + NL);
		storedcsvhwriter.close();

		if (storeInput)
			storedcsvcwriter.write("#endif" + NL + NL);
		storedcsvcwriter.close();
	}

	protected Function getMainFunction() {
		Function mainFun = null;
		if (getFunctionalEntryPoint() != null) {
			for (Function fun : storedFun)
				if (fun.functionName().equals(getFunctionalEntryPoint())) {
					mainFun = fun;
					break;
				}
		}

		if (emptyStatements() && isMainCompilationUnit() && !inlineFun.isEmpty())
			// get first inline function, if this is the main script
			mainFun = inlineFun.get(0);

		return mainFun;
	}

	private String getPointerSymbol(String matrixStructSymbol) {
		return matrixStructSymbol + ".matrix";
	}

	/** access to an array with an index **/
	private String getArrayContent(String arrayName, String position, Boolean isMatrix) {
		StringBuffer result = new StringBuffer("");
		result.append(arrayName);
		// needed because arrays are translated into matrix in C
		if (isMatrix) {
			result.append("[0]");
		}
		result.append("[").append(position).append("]");
		return result.toString();
	}

	@StepType(method = StepType.Function.ABSTRACT_INDEX_MATRIX)
	public String translateSliceOperator(AASTNode sliceNode, List<String> childrenResults)
			throws UndefinedTranslationException, TypeException {
		SliceType sliceType = (SliceType) getExprGeneralized(sliceNode);

		GType start = sliceType.slices().first();
		String startv = childrenResults.get(0);

		GType step = sliceType.slices().second();
		String stepv = childrenResults.get(1);

		GType end = sliceType.slices().third();
		String endv = childrenResults.get(2);

		// write it just the same
		AASTNode outputNode = getOutputNode(sliceNode);
		String outNodeName = getNodeSymbol(outputNode);
		// define only if value not known at compile time
		// dim can be "++"

		newComment(sliceNode);
		newTranslation(sliceNode, sliceInit(outNodeName, sliceType, startv, stepv, endv, true));

		return outNodeName;
	}

	private String sliceInit(String outNodeName, String inNodeName, SliceType sliceType, boolean linearSlice)
			throws UndefinedTranslationException {
		String[] slicingParameters = sliceTypesToSliceStr(sliceType, inNodeName);
		String end = TypeUtils.matrixDimName(inNodeName, 1);
		return sliceInit(outNodeName, sliceType, slicingParameters[0], slicingParameters[1], slicingParameters[2], end,
				linearSlice);
	}

	private String sliceInit(String outNodeName, SliceType sliceType, String startv, String stepv, String endv,
			boolean sliceLinear) throws UndefinedTranslationException {
		String dim = null;
		if (sliceType.dim().hasValue())
			dim = Integer.toString(sliceType.dim().valueAsInt());
		String progressionType = null;
		return sliceInit(outNodeName, sliceType, startv, stepv, endv, dim, sliceLinear);
	}

	private String sliceInit(String outNodeName, SliceType sliceType, String startv, String stepv, String endv,
			String dim, boolean sliceLinear) throws UndefinedTranslationException {
		GType start = sliceType.slices().first();
		String slicetype = null;
		if (start.equals(BType.INT)) {
			slicetype = SLICE_TYPE_I;
		} else if (start.equals(BType.SCALAR)) {
			slicetype = SLICE_TYPE_D;
		} else
			throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_TYPE, null, start,
					BType.MATRIX_ACCESS_SLICE);

		StringBuffer sb = new StringBuffer();
		if (dim == null) {
			sb.append(outNodeName).append(" = ").append("((" + slicetype + "){.start = " + startv + ", .step = " + stepv
					+ ", .end = " + endv + ", .dim1 = -1, .linear = " + Boolean.toString(sliceLinear) + "})")
					.append(";").append(NL);
			sb.append(outNodeName).append(STRUCT_ACCESS).append("dim1 = ").append(getSequenceDimension(sliceType))
					.append(";").append(NL);
		} else
			sb.append(outNodeName).append(" = ")
					.append("((" + slicetype + "){.start = " + startv + ", .step = " + stepv + ", .end = " + endv
							+ ", .dim1 = " + dim + ", .linear = " + Boolean.toString(sliceLinear) + "})")
					.append(";").append(NL);

		return sb.toString();
	}

	/**
	 * translate and define in environment through callbacks a symbol representing
	 * the output value of a function that returns a matrix using only locally
	 * available symbols
	 * 
	 * @param matrixDim    dimension of the output matrix in symbols available only
	 *                     in the function
	 * @param formalParams formal parameters of the function called
	 * @param actualParams actual parameters of the function called
	 * @return
	 */
	protected void genRetMatrixFromFunctionCall(String newSymbol, List<String> matrixDim,
			List<Quadruple<GType, SymbolType, String, List<String>>> formalParams,
			List<Quadruple<GType, SymbolType, String, List<String>>> actualParams) {

		List<String> newSymbolDim = new ArrayList<String>();
		for (String dim : matrixDim) {
			Matcher m = StructSymbols.matchMatrixDimensionLabel(dim);
			String ndim = dim;
			if (m.find()) {
				// if match was found..
				String varName = m.group();
				String paramName = m.group(1);
				Integer varNumber = Integer.parseInt(m.group(2));
				// search for a match in formal parameters names
				for (Quadruple<GType, SymbolType, String, List<String>> formalParam : formalParams)
					if (paramName.equals(formalParam.third()))
						// if found reference to formal dimension, replace it
						// with new actual dimension of this block
						// -1 because the numbers here start from 1
						ndim = dim.replace(varName, formalParam.fourth().get(varNumber - 1));
			}
			newSymbolDim.add(ndim);
		}

	}

	public String translateUnaryBooleanExpression(AASTNode node, List<String> child_results)
			throws UndefinedTranslationException, TypeException {
		AASTNode left = node.childs().get(0);
		GType leftType = getExprGeneralized(left);
		String leftStr = child_results.get(0);

		if (leftType.isCastableToScalar()) {
			if (leftType.isCastableToInt())
				return nodeTypeToScalar2ScalarExpression(node) + leftStr;
			else if (leftType.equals(BType.SCALAR))
				return "fabs(" + leftStr + ") < 10e-10";
			else
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_BOOLEAN_EXPRESSION_TYPES, node,
						nodeTypeToScalar2ScalarExpression(node), leftType);
		} else if (leftType.isCastableToMatrix()) {
			return unary2matrix(node, ((DimensionType) leftType).of(), leftStr);
		} else
			throw new UndefinedTranslationException(CErrorMessage.INTERNAL_BOOLEAN_EXPRESSION_TYPES, node,
					nodeTypeToScalar2ScalarExpression(node), leftType);

	}

	@StepType(method = StepType.Function.BOOLEXPR)
	public String translateBooleanExpression(AASTNode node, List<String> child_results)
			throws UndefinedTranslationException, TypeException {

		List<AASTNode> child_nodes = node.childs();
		// get id_node and then ID
		AASTNode left = child_nodes.get(0);

		AASTNode right = child_nodes.get(1);

		GType leftType = getExprGeneralized(left);
		GType rightType = getExprGeneralized(right);
		String leftStr = child_results.get(0);
		String rightStr = child_results.get(1);

		switch (leftType.type()) {
		// in C boolean is int and scalar is double
		case SCALAR:
		case INT:
		case BOOL:
			if (rightType.isCastableToScalar()) {
				return scalar2scalar(node, leftStr, rightStr);
			} else if (rightType.isCastableToMatrix()) {
				return scalar2matrix(node, leftStr, rightStr, leftType, rightType, false);
			} else
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_BOOLEAN_EXPRESSION_TYPES, node,
						nodeTypeToScalar2ScalarExpression(node), leftType, rightType);
		case MATRIX:
			if (rightType.isCastableToScalar()) {
				return scalar2matrix(node, rightStr, leftStr, rightType, leftType, true);
			} else if (rightType.isCastableToMatrix()) {
				return matrix2matrix(node, leftStr, rightStr, leftType, rightType);
			} else
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_BOOLEAN_EXPRESSION_TYPES, node,
						nodeTypeToScalar2ScalarExpression(node), leftType, rightType);
		default:
			throw new UndefinedTranslationException(CErrorMessage.INTERNAL_BOOLEAN_EXPRESSION_TYPES, node,
					nodeTypeToScalar2ScalarExpression(node), leftType, rightType);
		}

	}

	private String matrix2matrix(AASTNode node, String left, String right, GType leftType, GType rightType)
			throws UndefinedTranslationException, TypeException {
		String i = EXPR_INDEX;
		String li = (TypeUtils.isDegeneratedMatrix(leftType)) ? "0" : EXPR_INDEX;
		String ri = (TypeUtils.isDegeneratedMatrix(rightType)) ? "0" : EXPR_INDEX;
		AASTNode lnode = node.childs().get(0);
		AASTNode rnode = node.childs().get(1);
		String lexpr = indexDimensionType(lnode, left, li, lnode.symbol().equals(left));
		String rexpr = indexDimensionType(node.childs().get(1), right, ri, rnode.symbol().equals(right));
		// translate only if toplevel of operations
		if (topLevelRHSExpression(node)) {
			AASTNode outputNode = getOutputNode(node);
			IntType[] dims = TypeUtils.realDims(node, 2);
			String booleanSymbol = getNodeSymbol(outputNode);
			String oexpr = indexDimensionType(outputNode, booleanSymbol, i, true);
			// create matrix with dim of right matrix
			// initMatrix(node);
			createVariable(outputNode);
			GType outNodeExpr = outputNode.expr();
			StringBuffer localBf = new StringBuffer();
			List<String> strDims = nodeDimsToStr(outputNode, dims, true);
			ensureMatrixMemory(localBf, outputNode, strDims.toArray(new String[strDims.size()]));

			// for loop for initialization..
			localBf.append("#pragma omp smid").append(NL);
			localBf.append("for( int ").append(i).append("=0 ; ").append(i).append(" < ")
					.append(String.join(" * ", strDims)).append(" ; ").append("++").append(i).append("){").append(NL);

			localBf.append(TAB).append(oexpr).append(" = ")
					.append(lexpr + " " + nodeTypeToScalar2ScalarExpression(node) + " " + rexpr).append(";").append(NL);

			localBf.append("}").append(NL);

			newComment(node);
			newTranslation(node, localBf.toString());

			return booleanSymbol;
		} else {
			// part of translation
			return lexpr + " " + nodeTypeToScalar2ScalarExpression(node) + " " + rexpr;
		}

		// update matrix dimensions map kept in C class because C-specific
		// updateMatrixDims(booleanSymbol, dims);
	}

	private String scalar2matrix(AASTNode node, String scalar, String matrix, GType scalarType, GType matrixType,
			boolean inverted) throws UndefinedTranslationException, TypeException {

		String rexpr, scalarName, matrixName, lhs, rhs, i;
		// access only with 0 if a degenerated matrix
		i = (TypeUtils.isDegeneratedMatrix(matrixType)) ? "0" : EXPR_INDEX;
		if (!inverted) {
			scalarName = lhs = scalar;
			matrixName = matrix;
			AASTNode rnode = node.childs().get(1);
			rhs = indexDimensionType(rnode, matrixName, i, rnode.symbol().equals(matrixName));
		} else {
			matrixName = matrix;
			scalarName = rhs = scalar;
			AASTNode lnode = node.childs().get(0);
			lhs = indexDimensionType(lnode, matrixName, i, lnode.symbol().equals(matrixName));
		}

		// translate only if toplevel of operations
		if (topLevelRHSExpression(node)) {
			AASTNode outputNode = getOutputNode(node);
			IntType[] dims = TypeUtils.realDims(node, 2);
			String booleanSymbol = getNodeSymbol(outputNode);
			String oexpr = indexDimensionType(outputNode, booleanSymbol, i, true);

			// create matrix with dim of right matrix
			// initMatrix(node);
			createVariable(outputNode);
			StringBuffer current = new StringBuffer();
			List<String> strDims = nodeDimsToStr(outputNode, dims, true);
			ensureMatrixMemory(current, outputNode, strDims.toArray(new String[strDims.size()]));

			// for loop for initialization..

			current.append("#pragma omp smid").append(NL);
			current.append("for( int " + i + "=0; " + i + " < ").append(String.join(" * ", strDims)).append(" ; ")
					.append("++" + i + "){").append(NL);
			current.append(TAB).append(oexpr).append(" = ")
					.append(lhs + " " + nodeTypeToScalar2ScalarExpression(node) + " " + rhs).append(";").append(NL);

			current.append("}").append(NL);

			newTranslation(node, current.toString());
			// updateMatrixDims(booleanSymbol, dims);
			return booleanSymbol;
		} else {
			// part of translation
			return lhs + " " + nodeTypeToScalar2ScalarExpression(node) + " " + rhs;
		}
	}

	private String unary2matrix(AASTNode node, GType ofType, String matrix)
			throws UndefinedTranslationException, TypeException {

		String rexpr, matrixName, rhs, index, i;
		AASTNode mnode = getIDNode(node);
		boolean containsSparseSubExpr = mnode.hasAttr(CAttr.CONTAINS_SPARSE_SUBEXPRESSION);
		GType mexpr = mnode.expr();
		matrixName = matrix;
		i = EXPR_INDEX;
		if (mexpr.equals(BType.MATRIX) && ((MatrixType) mexpr).isSparse()) {
			index = sparseAccess(matrixName);
		} else if (TypeUtils.isDegeneratedMatrix(mexpr))
			index = "0";
		else if (containsSparseSubExpr) {
			List<String> leftDims = nodeDimsToStr(mnode, ((MatrixType) mexpr).dims(), true, true);
			index = get2DAccessIndex(EXPR_INDEX, SPARSE_ROW_INDEX, leftDims);
		} else
			index = EXPR_INDEX;
		// unary operations should always have the .matrix
		rhs = indexDimensionType(node.childs().get(0), matrixName, index, true);

		// translate only if toplevel of operations
		if (topLevelRHSExpression(node)) {
			AASTNode outputNode = getOutputNode(node);
			IntType[] dims = TypeUtils.realDims(outputNode, 2);
			String booleanSymbol = getNodeSymbol(outputNode);
			String oexpr = indexDimensionType(outputNode, booleanSymbol, index, true);

			// create matrix with dim of right matrix
			// initMatrix(node);
			createVariable(outputNode);
			StringBuffer current = new StringBuffer();
			List<String> strDims = nodeDimsToStr(outputNode, dims, true);
			ensureMatrixMemory(current, outputNode, strDims.toArray(new String[strDims.size()]));

			// for loop for initialization..

			current.append("#pragma omp smid").append(NL);
			current.append("for( int " + i + "=0; " + i + " < ").append(String.join(" * ", strDims)).append(" ; ")
					.append("++" + i + "){").append(NL);
			current.append(TAB).append(oexpr).append(" = ");
			if (ofType.isCastableToInt())
				current.append(nodeTypeToScalar2ScalarExpression(node) + rhs);
			else if (ofType.equals(BType.SCALAR))
				current.append("fabs(" + rhs + ") < 10e-10");
			else
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_BOOLEAN_EXPRESSION_TYPES, node,
						nodeTypeToScalar2ScalarExpression(node), ofType);

			current.append(";").append(NL);
			current.append("}").append(NL);

			newTranslation(node, current.toString());
			// updateMatrixDims(booleanSymbol, dims);
			return booleanSymbol;
		} else {
			// part of translation
			if (ofType.isCastableToInt())
				return nodeTypeToScalar2ScalarExpression(node) + rhs;
			else if (ofType.equals(BType.SCALAR))
				return "fabs(" + rhs + ") < 10e-10";
			else
				throw new UndefinedTranslationException(CErrorMessage.INTERNAL_BOOLEAN_EXPRESSION_TYPES, node,
						nodeTypeToScalar2ScalarExpression(node), ofType);
		}
	}

	private String scalar2scalar(AASTNode node, String left, String right) throws UndefinedTranslationException {
		// translate only if toplevel of operations
		return left + " " + nodeTypeToScalar2ScalarExpression(node) + " " + right;
	}

	private String indexDimensionType(AASTNode mnode, String matrix, String index, boolean skipExpression)
			throws TypeException, UndefinedTranslationException {
		if (!skipExpression)
			// if skipexpression false matrix != mnode.name()
			// -> function call was not translated in a previous portion of code
			if (mnode.type().equals(NodeType.APPLY))
				return matrix;
			else
				for (NodeType skip : TypeUtils.expressionNodes()) {
					AASTNode nchild = mnode.nestedChild(skip);
					// if is a binary nested operation
					if (nchild != null)
						// don't add index if this is a nested operation.
						return matrix;
				}
		GType mnodeType = getExprGeneralized(mnode);

		if (mnodeType.equals(BType.MATRIX))
			if (mnode.hasAttr(CAttr.APPLY_OF_BOOLEAN_MATRIX)) {
				// if this is inside RHS of assignment remember also at ASSIGN node
				// that can have an if statement in it.
				if (mnode.parentExists(NodeType.RHS))
					mnode.parent(new NodeType[] { NodeType.ASSIGN, NodeType.GASSIGN })
							.attr(CAttr.APPLY_OF_BOOLEAN_MATRIX, mnode);
				String maxdim = String.join("*", nodeDimsToStr(mnode, ((MatrixType) mnodeType).dims(), false, false));
				return matrix + STRUCT_ACCESS + "matrix[(" + index + " < " + maxdim + ")? " + index + " : " + maxdim
						+ "-1]";
			} else
				return matrix + STRUCT_ACCESS + "matrix[" + index + "]";
		else if (mnodeType.equals(BType.MATRIX_ACCESS_SLICE)) {
			SliceType stype = (SliceType) mnodeType;
			if (stype.of().equals(BType.INT) || stype.of().equals(BType.BOOL))
				return "viSliceAccess(&" + matrix + ", " + index + ")";
			else if (stype.of().equals(BType.SCALAR))
				return "vdSliceAccess(&" + matrix + ", " + index + ")";
			else
				throw new TypeException(CErrorMessage.INTERNAL_SLICE_UNSUPPORTED, mnode, mnodeType);
		} else
			throw new TypeException(CErrorMessage.INTERNAL_SLICE_UNSUPPORTED, mnode, mnodeType);
	}

	private String nodeTypeToScalar2ScalarExpression(AASTNode node) throws UndefinedTranslationException {

		switch (node.type()) {
		case DOUBLE_EQ:
			return "==";
		case GRT:
			return ">";
		case GRTE:
			return ">=";
		case BIN_AND:
		case LOG_AND:
			return "&&";
		case BIN_OR:
		case LOG_OR:
			return "||";
		case LST:
			return "<";
		case LSTE:
			return "<=";
		case NEQ:
			return "!=";
		case NEG:
			return "!";
		default:
			throw new UndefinedTranslationException(CErrorMessage.UNSUPPORTED_OPERAND, node, node.type());

		}
	}

	@StepType(method = StepType.Function.JUMP)
	public void translateUnconditionalJump(String programName) {
		// not needed
	}

	public String nodeDimsToInt(IntType[] dims) {
		if (dims == null)
			return null;
		int dimValue = 1;
		for (IntType dim : dims)
			if (dim.hasValue())
				dimValue *= dim.valueAsInt();
			else
				return null;
		return Integer.toString(dimValue);
	}

	public String typeToStrValue(GType t) throws UndefinedTranslationException {
		if (t instanceof ValuedType) {
			ValuedType v = ((ValuedType) t);
			if (v.hasValue())
				return Integer.toString(v.valueAsInt());
			else
				return v.valueAsString();
		} else
			throw new UndefinedTranslationException(CErrorMessage.INTERNAL_METHOD_TYPE_UNSUPPORTED, null,
					"typeToStrValue", t);
	}

	protected boolean[] getSurvivedDimensions(AASTNode node) {
		boolean[] result = null;
		if (node.hasAttr(CAttr.SURVIVED_DIMESIONS)) {
			result = (boolean[]) node.attr(CAttr.SURVIVED_DIMESIONS);
		}

		if (node.type().equals(NodeType.EXPRESSION) || node.type().equals(NodeType.ID_NODE)) {
			return getSurvivedDimensions(node.childs().get(0));
		} else
			return result;

	}

	protected GType getExprGeneralized(AASTNode node) {
		GType exprResult = node.expr();
		if (exprResult == null && node.childs().size() == 1) {
			return getExprGeneralized(node.childs().get(0));
		} else if (node.type().equals(NodeType.FIELDACCESS)) {
			List<AASTNode> childs = node.childs();
			return getExprGeneralized(childs.get(childs.size() - 1));
		} else {
			return exprResult;
		}

	}

	protected AASTNode getExprNode(AASTNode node) {
		GType exprResult = node.expr();
		if (exprResult == null && node.childs().size() == 1) {
			return getExprNode(node.childs().get(0));
		} else if (node.type().equals(NodeType.FIELDACCESS)) {
			List<AASTNode> childs = node.childs();
			return getExprNode(childs.get(childs.size() - 1));
		} else {
			return node;
		}
	}

	protected AASTNode getIDNode(AASTNode node) {
		return TypeUtils.getIDNode(node);
	}

	protected String getNodeSymbol(AASTNode node) throws UndefinedTranslationException, TypeException {
		/*
		 * GType type = getExprGeneralized(node); if(type instanceof MatrixType || type
		 * instanceof SliceType){ return type.name(); }else{
		 */
		createVariable(node, false, VarVisibility.LOCAL_TO_FUNCTION);
		return node.symbol();
		/* } */
	}

	private String getStructFreeNodeSymbol(AASTNode node) throws UndefinedTranslationException, TypeException {
		String symbol = getNodeSymbol(node);
		return getStructFreeNodeSymbol(symbol);
	}

	private String getStructFreeNodeSymbol(String symbol) throws UndefinedTranslationException, TypeException {
		if (symbol.contains(GType.STRUCT_SEP))
			symbol = symbol.replaceAll("\\" + GType.STRUCT_SEP, "__");
		return symbol;
	}

	// reads the csv file and returns the name of the Matrix2D that stores
	// the values
	private StoredCsv storeCsv(Path csvFilePath) throws Exception {
		return storeCsv(csvFilePath, 0, 0);
	}

	// reads the csv file from line firstRow and column firstColumn and returns the
	// name of the Matrix2D that stores
	// the values
	private StoredCsv storeCsv(Path csvFilePath, Integer firstRow, Integer firstColumn) throws Exception {
		return storeCsv(csvFilePath, firstRow, firstColumn, null, null);
	}

	// reads the csv file from line firstRow-lastRow and column
	// firstColumn-lastColumn and returns the name of the Matrix2D that stores
	// the values
	private StoredCsv storeCsv(Path csvFilePath, Integer firstRow, Integer firstColumn, Integer lastRow,
			Integer lastColumn) throws Exception {
		Double[][] csvValues = CsvUtils.readCsv(csvFilePath, firstRow, firstColumn, lastRow, lastColumn);

		int rowNumber = csvValues.length;
		int columnNumber = csvValues[0].length;

		String storedMatrixName = CSV_LOADED_PREFIX + (csv_loaded_number++);

		// get the X matrix declaration
		String constFlattenedMatrixDeclaration = CsvUtils.printToCArrays(csvValues);

		StringBuffer storedCsvcBuffer = new StringBuffer();
		StringBuffer storedCsvhBuffer = new StringBuffer();

		storedCsvcBuffer.append(NL);

		storedCsvcBuffer.append("double ").append(storedMatrixName).append("[]");
		storedCsvcBuffer.append("=");
		storedCsvcBuffer.append(constFlattenedMatrixDeclaration).append(";").append(NL);

		storedCsvhBuffer.append("extern double ").append(storedMatrixName).append("[];").append(NL);

		storedcsvcwriter.write(storedCsvcBuffer.toString());
		storedcsvhwriter.write(storedCsvhBuffer.toString());

		return new StoredCsv(storedMatrixName, rowNumber, columnNumber);
	}

	protected Tuple<String, StringBuffer> getConditionalCondition(AASTNode node, GType type, String name)
			throws TypeException, UndefinedTranslationException {
		String condition = name;
		// matlab behaviour: check if all the elements of the matrix are greater than 0
		StringBuffer sb = null;
		if (type.isCastableToMatrix()) {
			sb = new StringBuffer();
			List<String> dims = nodeDimsToStr(node, ((DimensionType) type).dims(), true, true);
			String conditionVariable = Symbols.getSymbolFromType("ifcond", BType.BOOL);
			node.symbol(conditionVariable);
			createVariable(GType.get(BType.BOOL), conditionVariable, NodeType.ID, "1", false, false,
					VarVisibility.LOCAL_TO_BLOCK.context(node), sb, false);
			sb.append("#pragma omp smid").append(NL);
			sb.append("for(int " + EXPR_INDEX + "= 0; " + EXPR_INDEX + " < " + String.join("*", dims) + " && "
					+ conditionVariable + " ; ++" + EXPR_INDEX + " )").append(NL);
			if (type instanceof MatrixType) {
				AASTNode mnode = getIDNode(node.child(NodeType.EXPRESSION));
				boolean containsSparseSubExpr = mnode.hasAttr(CAttr.CONTAINS_SPARSE_SUBEXPRESSION);
				GType mexpr = mnode.expr();
				String index;
				if (mexpr.equals(BType.MATRIX) && ((MatrixType) mexpr).isSparse()) {
					index = sparseAccess(condition);
				} else if (TypeUtils.isDegeneratedMatrix(type))
					index = "0";
				else if (containsSparseSubExpr) {
					List<String> leftDims = nodeDimsToStr(mnode, ((MatrixType) mexpr).dims(), true, true);
					index = get2DAccessIndex(EXPR_INDEX, SPARSE_ROW_INDEX, leftDims);
				} else
					index = EXPR_INDEX;

				String rexpr = indexDimensionType(mnode, condition, index, mnode.symbol().equals(condition));
				sb.append(TAB).append(conditionVariable).append(" &= ").append(rexpr).append(";").append(NL);
				condition = conditionVariable;
			} else if (type instanceof SliceType) {
				String sliceAccessFun = "v?SliceAccess";
				if (((DimensionType) type).of().equals(BType.SCALAR)) {
					sliceAccessFun = sliceAccessFun.replace('?', 'd');
				} else {
					sliceAccessFun = sliceAccessFun.replace('?', 'i');
				}
				String matAccess = sliceAccessFun + "( &" + name + ", " + EXPR_INDEX + ")";

				sb.append(TAB).append(conditionVariable).append(" &= ").append(matAccess).append(";").append(NL);
				condition = conditionVariable;
			} else
				condition = null;
		} else if (type.isCastableToScalar()) {
			condition = "fabs(" + name + ")>" + CONDITION_DOUBLE_ZERO;
			node.compilationUnit().addWarning(new TypeException(CErrorMessage.WARN_IF_SCALAR, node));
		} else {
			condition = null;
		}
		return new Tuple<>(condition, sb);
	}
}

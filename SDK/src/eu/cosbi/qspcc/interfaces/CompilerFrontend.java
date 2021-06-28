package eu.cosbi.qspcc.interfaces;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import eu.cosbi.qspcc.ast.AASTNode;
import eu.cosbi.qspcc.ast.Program;
import eu.cosbi.qspcc.exceptions.ListException;
import eu.cosbi.qspcc.exceptions.SyntaxException;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.expressions.type.TypeDefinition.BType;
import eu.cosbi.utils.Tuple;

public abstract class CompilerFrontend implements CompilerModule {
    // tree walk listeners
    protected Path basePath;
    protected String mainScript;

    public CompilerFrontend(String programPath) {
	Path spath = Paths.get(programPath);
	this.basePath = spath.getParent();
	if (basePath == null)
	    basePath = Paths.get(System.getProperty("user.dir"));

	this.mainScript = programPath;
    }

    public enum FunctionType {
	ND, /* NOT DEFINED */
	VARIABLE, /* function without parameters, like a variable */
	STATISTICAL /* statistical function, like sum, max, min, avg.. */
    }

    public interface IFunction {

	// get name of this enum value
	public String getName();

	/**
	 * This is the default list of parameter types (will be overridden by the output of `paramTypesUpdater()` if it's not null). 
	 * If the number of parameter types is not known a priori, this should return a list with a single element of type `BType.UNKNOWN`. 
	 * In this case QSPcc will automatically assign the right types based on the actual parameters.
	 */
	public List<GType> getParamTypes();

	/**
	 * @return a map with the formal/actual output type of the function, based on
	 *         the input (some functions returns SCALAR if the input is SCALAR
	 *         MATRIX is input is MATRIX). The frontend walker will use the output
	 *         type that matches at least one input parameter, or raise an error if
	 *         none matches.
	 * 
	 *         If only one element is present in the map, that one will be used
	 *         regardless of the input parameters.
	 */
	public Map<BType, GType> getOutType();

	/*
	 * true if it returns a single value (scalar/matrix..) without input arguments
	 */
	public FunctionType type();

	/**
	 * function that takes the APPLY node on which this IFunction name is found (ex.
	 * a(1) where this.getName().equals("a")) and the list of actual arguments (ex.
	 * INT in the previous example) and returns an array of alternative return types
	 * (usually 1 is sufficient) for this APPLY call (ex. in the previous case can
	 * be anything, INT, MATRIX, SCALAR..) This function is used by compiler
	 * middle-end to infer types for functions defined externally.
	 * 
	 * Set to null if not needed.
	 * 
	 * @return
	 */
	BiFunction<AASTNode, List<GType>, List<GType>> returnTypeUpdater();

	/**
	 * function that takes the APPLY node on which this IFunction name is found (ex.
	 * a(@(t,x) x*2, 1) where this.getName().equals("a")) and the list of actual
	 * arguments (ex. FUNCTION, INT in the previous example) and returns an array of
	 * new formal parameter types for this APPLY call (ex. in the previous case can
	 * be FUNCTION, INT) This function is used by compiler middle-end to infer
	 * formal types of externally-defined functions, based on actual parameters.
	 * 
	 * Set to null if not needed.
	 * 
	 * @return
	 */
	BiFunction<AASTNode, List<GType>, List<GType>> paramTypesUpdater();

    }

    public IFunction getEnum(String name, Class<? extends IFunction>... classes) {
	for (Class<? extends IFunction> CLass : Arrays.asList(classes)) {
	    for (IFunction c : CLass.getEnumConstants()) {
		if (name.equals(c.getName())) {
		    return c;
		}
	    }
	}
	return null;
    }

    /**
     * Produce a Program instance (an AST of compilation units, each unit is again an AST)
     * for the input program in basePath
     * 
     * @param coreFunctions:
     *            the set of coreFunctions defined in this run.
     * @param gvars:
     *            the set of global variables defined in this run.
     * @return a Program instance
     * @throws SyntaxException
     */
    public abstract Program walk(IFunction[] coreFunctions, List<Tuple<GType, String>> gvars)
	    throws SyntaxException, ListException;

    /**
     * This is the first function you should implement. 
     * It defines the list of source language functions globally defined, meaning a list of functions/variables that the user should not provide an implementation for.
     * This list is dependent on the target language configured 
     * through the `-t` command-line switch (since the same function can be supported in C and not suppported in R or vice-versa).
     * Examples in MATLAB are *pi* or *e*  if we talk about variables, or *disp* *sum* if we talk about functions.
     * See an example implementation in the MATLAB frontend here: frontends/MatlabFrontend/src/eu/cosbi/translator/frontend/Matlab.java#L95
     * 
     * @param backend
     * @return
     */
    public abstract IFunction[] globalFunctionsFor(String backendClassName);
}

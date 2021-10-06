package eu.cosbi.qspcc.interfaces;

import eu.cosbi.qspcc.exceptions.ListException;
import eu.cosbi.qspcc.expressions.type.GType;
import eu.cosbi.qspcc.interfaces.CompilerFrontend.IFunction;

/**
 * Generic interface for the MiddleEnd annotator.
 * It should annotate the tree with types and annotations present in NodeAttr.
 * It should contain a constructor with a single argument of type Program to be annotated.
 * Default implementation: DefaultMiddleEndImpl
 * @author tomasoni
 *
 */
public interface MiddleEnd extends CompilerModule {
    public ListException annotate(IFunction[] coreFunctions, boolean stopOnError);

    /**
     * default type for the parameters of function programs (without an input script)
     * if not specified, default is (input)STRUCT
     * @param stopOnError
     */
    public void defaultMainFunctionInputsType(GType defaultType);

    /**
     * 
     * @return the function that acts as entry point of the program, if this program doesn't start from a script
     */
    public String getFunctionalEntryPoint();

}

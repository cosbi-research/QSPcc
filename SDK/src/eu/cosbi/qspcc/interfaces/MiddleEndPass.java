package eu.cosbi.qspcc.interfaces;

import eu.cosbi.qspcc.exceptions.GException;

/**
 * Generic interface for the MiddleEnd single-pass annotator.
 * It should annotate the tree with types and annotations present in NodeAttr.
 * It should contain a constructor with a single argument of type Program to be annotated.
 * @author tomasoni
 *
 */
public interface MiddleEndPass {
    public void annotate(boolean stopOnError) throws GException;
}

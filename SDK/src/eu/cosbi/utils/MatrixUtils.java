package eu.cosbi.utils;

import java.util.ArrayList;
import java.util.List;

import eu.cosbi.qspcc.exceptions.UndefinedTranslationException;

public class MatrixUtils {
    /**
     * 
     * @param access_numbers
     *            M(3,2) [with M 5x5 matrix]
     * @return the equivalent flatten-array index M[10]
     * @throws UndefinedTranslationException
     */
    public static Integer flattenMatrixAccessStr(List<Integer> dims, List<String> access_numbers)
	    throws UndefinedTranslationException {
	List<Integer> nints = new ArrayList<Integer>();
	for (String n : access_numbers)
	    nints.add(Integer.parseInt(n));
	return flattenMatrixAccess(dims, nints);
    }

    /**
     * flatten basic matrix access, return the index corresponding to the matrix
     * access indexes
     * 
     * @param dims
     * @param access_numbers
     * @return
     * @throws UndefinedTranslationException
     */
    public static Integer flattenMatrixAccess(List<Integer> dims, List<Integer> access_numbers) {
	Integer flat_idx = 0;
	for (int n = 0; n < access_numbers.size(); ++n) {
	    if (n != dims.size() - 1) {
		// access_number * Dimension1 * Dimension2 * ...
		Integer multiplier = 1;
		for (int i = n + 1; i < dims.size(); ++i)
		    multiplier = multiplier * dims.get(i);
		flat_idx += multiplier * (access_numbers.get(n) - 1);
	    } else
		// last access number
		flat_idx += (access_numbers.get(n) - 1);
	}
	return flat_idx;
    }

}

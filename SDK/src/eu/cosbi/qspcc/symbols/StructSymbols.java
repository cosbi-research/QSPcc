package eu.cosbi.qspcc.symbols;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StructSymbols {
    // the pattern for the C-specific matrix parameters dimension
    static Pattern dim_vars_pattern = Pattern.compile("(.*?)_dim([1-9])");

    public static String sliceConcreteMatrix(String matrixName) {
	return matrixName + "_concrete";
    }

    public static String matrixDimensionLabel(String matrixName, int matrix_dim_i) {
	return matrixName + "_dim" + matrix_dim_i;
    }

    public static boolean isMatrixDimensionLabel(String dimName) {
	Matcher m = dim_vars_pattern.matcher(dimName);
	return m.find();
    }

    public static Matcher matchMatrixDimensionLabel(String dimName) {
	Matcher m = dim_vars_pattern.matcher(dimName);
	return m;
    }
}

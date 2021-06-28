package eu.cosbi.qspcc.backend.utils;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {

    private final static String CSV_CELL_SEPARATOR = ",";
    private final static String CSV_HEADER_PREFIX = "header_";

    //reads a csv-file and store it into matrix of double
    public static Double[][] readCsv(Path filePath, Integer firstRow, Integer firstColumn, Integer lastRow,
	    Integer lastColumn) throws Exception {

	//read all the lines 
	List<String> lines = Files.readAllLines(filePath, Charset.defaultCharset());

	Double[][] res = null;

	if (!lines.isEmpty()) {
	    //the first line contains headers?
	    String[] firstLineElements = lines.get(0).split(CSV_CELL_SEPARATOR);

	    if (lastRow == null) {
		lastRow = lines.size() - 1;
	    }

	    if (lastColumn == null) {
		lastColumn = firstLineElements.length - 1;
	    }

	    res = new Double[lastRow - firstRow + 1][lastColumn - firstColumn + 1];

	    List<String[]> lineCells = new ArrayList<String[]>();
	    for (int i = 0; i < lines.size(); i++) {
		lineCells.add(lines.get(i).split(CSV_CELL_SEPARATOR));
	    }

	    Double value;
	    for (int j = firstRow; j <= lastRow; j++) {
		for (int k = firstColumn; k <= lastColumn; k++) {
		    value = Double.parseDouble(lineCells.get(j)[k]);
		    res[j - firstRow][k - firstColumn] = value;
		}
	    }
	}

	return res;
    }

    //prints the loaded csv into a flattened String that declares 
    //the matrix in C
    public static String printToCArrays(Double[][] csvLoaded) {
	StringBuffer res = new StringBuffer();

	//declaration
	res.append("{");

	int rowNumber = csvLoaded.length;
	int columnNumber = csvLoaded[0].length;

	for (int i = 0; i < rowNumber; i++) {
	    for (int j = 0; j < columnNumber; j++) {
		String value = String.valueOf(csvLoaded[i][j]);
		if (csvLoaded[i][j].isNaN()) {
		    value = "NAN";
		}

		res.append(value).append(",");
	    }
	}
	res.deleteCharAt(res.length() - 1);
	res.append("}");

	return res.toString();
    }
}

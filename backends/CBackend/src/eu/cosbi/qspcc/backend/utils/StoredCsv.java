package eu.cosbi.qspcc.backend.utils;

// bean to store the variable name
// after storing csv, line numbers and column numbers
public class StoredCsv {

    public String storedCsvVariableName;
    public int rowNumber;
    public int columnNumber;

    public StoredCsv(String storedCsvVariableName, int rowNumber, int columnNumber) {
	this.storedCsvVariableName = storedCsvVariableName;
	this.rowNumber = rowNumber;
	this.columnNumber = columnNumber;
    }
}

package eu.cosbi.qspcc.tests.cases;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TestCsvLoading extends TestBase {
    public TestCsvLoading(String targetLanguage, String sunver, String suninclude, String sunlib, String stdlib,
	    String tcmallocinclude, String tcmalloclib, Boolean storeInput) {
	super(targetLanguage, sunver, suninclude, sunlib, stdlib, tcmallocinclude, tcmalloclib, storeInput);
    }

    @Test
    public void testCsvLoadHeader() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "csv_load", "csv_load_header");
	String programName = "main.m";

	// translateMatlab2C(programName, folderpath);
	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

}

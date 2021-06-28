package eu.cosbi.qspcc.tests.cases;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TestStructures extends TestBase {
    public TestStructures(String targetLanguage, String sunver, String suninclude, String sunlib, String stdlib,
	    String tcmallocinclude, String tcmalloclib, Boolean storeInput) {
	super(targetLanguage, sunver, suninclude, sunlib, stdlib, tcmallocinclude, tcmalloclib, storeInput);
    }

    @Test
    public void testBasicSaveMatrix() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "userdefstruct");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }
}

package eu.cosbi.qspcc.tests.cases;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TestEnv extends TestBase {

    public TestEnv(String targetLanguage, String sunver, String suninclude, String sunlib, String stdlib,
	    String tcmallocinclude, String tcmalloclib, Boolean storeInput) {
	super(targetLanguage, sunver, suninclude, sunlib, stdlib, tcmallocinclude, tcmalloclib, storeInput);
    }

    @Test
    public void testTypeGeneralization() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "env", "type_generalization");
	String programName = "main.m";
	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;
	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

}

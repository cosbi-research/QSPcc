package eu.cosbi.qspcc.tests.cases;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TestIntegration extends TestBase {

    public TestIntegration(String targetLanguage, String sunver, String suninclude, String sunlib, String stdlib,
	    String tcmallocinclude, String tcmalloclib, Boolean storeInput) {
	super(targetLanguage, sunver, suninclude, sunlib, stdlib, tcmallocinclude, tcmalloclib, storeInput);
    }

    @Test
    public void testGenericIntegration() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "integration", "ordinary_integration");
	String programName = "main.m";
	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testIntegrationWithOdeset() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "integration", "odeset");
	String programName = "main.m";
	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testIntegrationAtSecificTimes() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "integration", "integration_in_specified_times");
	String programName = "main.m";
	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }
}

package eu.cosbi.qspcc.tests.cases;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TestMocassin extends TestBase {

    public TestMocassin(String targetLanguage, String sunver, String suninclude, String sunlib, String stdlib,
	    String tcmallocinclude, String tcmalloclib, Boolean storeInput) {
	super(targetLanguage, sunver, suninclude, sunlib, stdlib, tcmallocinclude, tcmalloclib, storeInput);
    }

    @Test
    public void testNudelman() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "mocassin", "nudelman");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testHayot() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "mocassin", "hayot");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testHandel() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "mocassin", "handel");
	String programName = "main.m";
	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testSBMLToolbox() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "mocassin", "sbmltoolbox");
	String programName = "main.m";
	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }
}

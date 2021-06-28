package eu.cosbi.qspcc.tests.cases;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TestSBML extends TestBase {
    public TestSBML(String targetLanguage, String sunver, String suninclude, String sunlib, String stdlib,
	    String tcmallocinclude, String tcmalloclib, Boolean storeInput) {
	super(targetLanguage, sunver, suninclude, sunlib, stdlib, tcmallocinclude, tcmalloclib, storeInput);
    }

    @Test
    public void testBertram2004() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "sbml", "bertram2004");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testBrannmark2013() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "sbml", "brannmark2013");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testCapuani2015() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "sbml", "capuani2015");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testGiordano2020() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "sbml", "giordano2020");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMuraro2011() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "sbml", "muraro2011");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testPathak2013() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "sbml", "pathak2013");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testWinter2017() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "sbml", "winter2017");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }
}

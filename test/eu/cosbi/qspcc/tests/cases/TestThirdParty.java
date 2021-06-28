package eu.cosbi.qspcc.tests.cases;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TestThirdParty extends TestBase {
    public TestThirdParty(String targetLanguage, String sunver, String suninclude, String sunlib, String stdlib,
	    String tcmallocinclude, String tcmalloclib, Boolean storeInput) {
	super(targetLanguage, sunver, suninclude, sunlib, stdlib, tcmallocinclude, tcmalloclib, storeInput);
    }

    @Test
    public void testTaylorWangetal2014() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "TaylorWangetal2014");

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	String programName = "main.m";
	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testNeuralModelPD() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "neural_model_PD");

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	String programName = "main.m";
	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testFIRM() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "FIRM");

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	String programName = "main.m";
	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testODEWrap() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "ODEWrap", "simple");

	String programName = "main.m";
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testCartesian2Geodetic() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "cartesian_to_geodetic");

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	String programName = "main.m";
	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testLorenz() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "julia", "lorenz");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testAvgFilter() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "matlabCoder", "averaging_filter");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;
	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatlab2CPP() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "third-party", "matlab2cpp");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

}

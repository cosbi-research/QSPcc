package eu.cosbi.qspcc.tests.cases;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TestUserFunction extends TestBase {

    public TestUserFunction(String targetLanguage, String sunver, String suninclude, String sunlib, String stdlib,
	    String tcmallocinclude, String tcmalloclib, Boolean storeInput) {
	super(targetLanguage, sunver, suninclude, sunlib, stdlib, tcmallocinclude, tcmalloclib, storeInput);
    }

    @Test
    public void testMatrixUserFunction() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "fun", "matrix_fun");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testAnonymousUserFunction() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "fun", "anonymous_fun_env");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testUserInlineSubroutine() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "subroutine");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testUserFunctionWithEnvironmentVariables() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "fun", "anonymous_fun_env");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testUserNestedFunction() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "fun", "fun_concatenation");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testUserMultiValueReturnFunction() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "fun", "fun_multiple_returns");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

}

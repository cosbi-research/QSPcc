package eu.cosbi.qspcc.tests.cases;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TestExpression extends TestBase {

    public TestExpression(String targetLanguage, String sunver, String suninclude, String sunlib, String stdlib,
	    String tcmallocinclude, String tcmalloclib, Boolean storeInput) {
	super(targetLanguage, sunver, suninclude, sunlib, stdlib, tcmallocinclude, tcmalloclib, storeInput);
    }

    @Test
    public void testBaseExpression() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "expr", "scalar");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testBaseAssign() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "expr", "assign");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testBooleanExpression() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "expr", "boolean");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testModExpression() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "expr", "mod");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }
    /*
     * @Test public void testTrigoExpression() throws Exception { Path folderpath =
     * Paths.get(basePath, "tests", "expr", "trigo"); String programName = "main.m";
     * 
     * translate(programName, folderpath); //
     * compileAndRunC(programName.replace(".m", ""), folderpath); }
     */

}

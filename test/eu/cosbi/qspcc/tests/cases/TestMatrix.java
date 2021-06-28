package eu.cosbi.qspcc.tests.cases;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TestMatrix extends TestBase {

    public TestMatrix(String targetLanguage, String sunver, String suninclude, String sunlib, String stdlib,
	    String tcmallocinclude, String tcmalloclib, Boolean storeInput) {
	super(targetLanguage, sunver, suninclude, sunlib, stdlib, tcmallocinclude, tcmalloclib, storeInput);
    }

    @Test
    public void testAssignMultiplyPrintValue() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatrixSlicing() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "slice");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatrixInterp() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "interp1");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatrixDelete() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "matrix_delete");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;
	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatrixSlicingWithMatrix() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "matrix_slice");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMultiMatrix() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "multimatrix");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testSolveLinearSystem() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "linear_solve");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatrixElementarOperations() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "elementar_operations");
	String programName = "main.m";
	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatrixConcatenation() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "concatenation");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatrixRandom() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "random");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatrixIsMember() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "ismember");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatrixReshape() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "reshape");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatrixCumSum() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "cumsum");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatrixIsEmpty() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "isempty");
	String programName = "main.m";

	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatrixTransposition() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "transpose");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testMatrixMultipleAssignment() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "multipleassign");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testUnknownMatrixDim() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "nodim");
	String programName = "main.m";

	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testKron() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "kron");
	String programName = "main.m";
	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;
	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testRepmat() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "repmat");
	String programName = "main.m";
	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;
	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }

    @Test
    public void testSparse() throws Exception {
	Path folderpath = Paths.get(basePath, "tests", "matrix_operations", "sparse");
	String programName = "main.m";
	// TODO Alessio doesn't work in R
	if (targetLanguage.equalsIgnoreCase("R"))
	    return;
	translate(programName, folderpath);
	// compileAndRunC(programName.replace(".m", ""), folderpath);
    }
    /*
     * @Test public void testTrigoMatrixDim() throws Exception { Path folderpath =
     * Paths.get(basePath, "tests", "matrix_operations", "trigo_operations"); String
     * programName = "main.m";
     * 
     * translate(programName, folderpath); //
     * compileAndRunC(programName.replace(".m", ""), folderpath); }
     */
}

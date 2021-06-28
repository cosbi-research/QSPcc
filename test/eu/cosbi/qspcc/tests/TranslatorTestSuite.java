package eu.cosbi.qspcc.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite.SuiteClasses;

import com.github.peterwippermann.junit4.parameterizedsuite.ParameterizedSuite;

import eu.cosbi.qspcc.tests.cases.TestControls;
import eu.cosbi.qspcc.tests.cases.TestCsvLoading;
import eu.cosbi.qspcc.tests.cases.TestEnv;
import eu.cosbi.qspcc.tests.cases.TestExpression;
import eu.cosbi.qspcc.tests.cases.TestIntegration;
import eu.cosbi.qspcc.tests.cases.TestMatrix;
import eu.cosbi.qspcc.tests.cases.TestMocassin;
import eu.cosbi.qspcc.tests.cases.TestSBML;
import eu.cosbi.qspcc.tests.cases.TestStructures;
import eu.cosbi.qspcc.tests.cases.TestThirdParty;
import eu.cosbi.qspcc.tests.cases.TestTime;
import eu.cosbi.qspcc.tests.cases.TestUserFunction;

@RunWith(ParameterizedSuite.class)
@SuiteClasses({ TestSBML.class, TestControls.class, TestExpression.class, TestIntegration.class, TestMatrix.class,
	TestStructures.class, TestTime.class, TestUserFunction.class, TestMocassin.class, TestCsvLoading.class,
	TestThirdParty.class, TestEnv.class })
public class TranslatorTestSuite {

    @Parameter(0)
    public String targetLanguage;
    @Parameter(1)
    public String sunver;
    @Parameter(2)
    public String suninclude;
    @Parameter(3)
    public String sunlib;
    @Parameter(4)
    public String stdlib;
    @Parameter(5)
    public String tcmallocinclude;
    @Parameter(6)
    public String tcmalloclib;
    @Parameter(7)
    public Boolean storecsv;

    @Parameters(name = "Parameters are {0} {1} {2} {3} {4} {5} {6} {7}")
    public static Object[] params() {

	return new Object[][] {
		/*
		{
		// target language
		"c",
		// sundials version
		"2",
		// fixed parameters
		"[SUN_INCLUDE_PATH]", "[SUN_LIB_PATH]", "[STD_LIB_PATH]", "[TCMALLOC_INCLUDE_PATH]",
		"[TCMALLOC_LIB_PATH]", false },
		{
			// target language
			"c",
			// sundials version
			"2",
			// fixed parameters
			"[SUN_INCLUDE_PATH]", "[SUN_LIB_PATH]", "[STD_LIB_PATH]", "[TCMALLOC_INCLUDE_PATH]",
			"[TCMALLOC_LIB_PATH]", true },
		*/
		{
			// target language
			"c",
			// sundials version
			"5",
			// fixed parameters
			"[SUN_INCLUDE_PATH]", "[SUN_LIB_PATH]", "[STD_LIB_PATH]", "[TCMALLOC_INCLUDE_PATH]",
			"[TCMALLOC_LIB_PATH]", false },
		/*
		{
			// target language
			"c",
			// sundials version
			"5",
			// fixed parameters
			"[SUN_INCLUDE_PATH]", "[SUN_LIB_PATH]", "[STD_LIB_PATH]", "[TCMALLOC_INCLUDE_PATH]",
			"[TCMALLOC_LIB_PATH]", true },
			*/
		{ "r", null, null, null, null, null, null, null } };
    }
}

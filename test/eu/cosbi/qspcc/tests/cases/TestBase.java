package eu.cosbi.qspcc.tests.cases;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.peterwippermann.junit4.parameterizedsuite.ParameterContext;

import eu.cosbi.logger.configurator.LoggerConfiguratorFactory;
import eu.cosbi.qspcc.CompilerController;

@RunWith(Parameterized.class)
public class TestBase {
    // the path of the installation directory 
    protected String basePath;
    protected String targetLanguage;
    protected String sunver;
    protected String suninclude;
    protected String sunlib;
    protected String stdlib;
    protected String tcmallocinclude;
    protected String tcmalloclib;
    protected Boolean storeInput;
    protected Map<String, List<String>> outBackendOptions;

    public TestBase(String targetLanguage, String sunver, String suninclude, String sunlib, String stdlib,
	    String tcmallocinclude, String tcmalloclib, Boolean storeInput) {
	super();
	this.targetLanguage = targetLanguage;
	this.sunver = sunver;
	this.suninclude = suninclude;
	this.sunlib = sunlib;
	this.stdlib = stdlib;
	this.tcmallocinclude = tcmallocinclude;
	this.tcmalloclib = tcmalloclib;
	this.storeInput = storeInput;

	String logdir = System.getProperty("user.dir");
	// initialize logger programmatically
	LoggerConfiguratorFactory.setInstallPath(logdir);
	ConfigurationFactory.setConfigurationFactory(new LoggerConfiguratorFactory());

	outBackendOptions = new HashMap<String, List<String>>();
	List<String> l;
	if (suninclude != null) {
	    l = new ArrayList<String>(1);
	    l.add(suninclude);
	    outBackendOptions.put("suninclude", l);
	}
	if (sunlib != null) {
	    l = new ArrayList<String>(1);
	    l.add(sunlib);
	    outBackendOptions.put("sunlib", l);
	}
	if (sunver != null) {
	    l = new ArrayList<String>(1);
	    l.add(sunver);
	    outBackendOptions.put("sunver", l);
	}
	if (stdlib != null) {
	    l = new ArrayList<String>(1);
	    l.add(stdlib);
	    outBackendOptions.put("stdlib", l);
	}
	if (tcmallocinclude != null) {
	    l = new ArrayList<String>(1);
	    l.add(tcmallocinclude);
	    outBackendOptions.put("tcmallocinclude", l);
	}
	if (tcmalloclib != null) {
	    l = new ArrayList<String>(1);
	    l.add(tcmalloclib);
	    outBackendOptions.put("tcmalloclib", l);
	}
	if (storeInput != null && storeInput) {
	    l = new ArrayList<String>();
	    outBackendOptions.put("si", l);
	}

    }

    @Parameters(name = "Parameters are {0} {1} {2} {3} {4} {5} {6} {7} {8} {9} {10}")
    public static Iterable<Object[]> params() {
	// if (ParameterContext.isParameterSet()) {
	return Collections.singletonList(ParameterContext.getParameter(Object[].class));
	// } else {
	// if the test case is not executed as part of a ParameterizedSuite, you can
	// define fallback parameters
	// }

    }

    @Before
    public void setUp() throws Exception {
	basePath = new java.io.File(TestBase.class.getProtectionDomain().getCodeSource().getLocation().toURI())
		.getParentFile().getParentFile().getPath();
	System.setErr(new PrintStream(new OutputStream() {

	    @Override
	    public void write(int b) throws IOException { // ignore
	    }
	}));
    }

    public void translate(String programName, Path folderpath) throws Exception {
	translateProgram(programName, null, folderpath, "matlab", targetLanguage, outBackendOptions, null);
    }

    public void translateProgram(String programName, String inFile, Path folderpath, String fromLanguage,
	    String toLanguage, Map<String, List<String>> backendOptions, Map<String, List<String>> frontendOptions)
	    throws Exception {
	// don't translate matlab to matlab
	if (toLanguage.toLowerCase().equals("matlab"))
	    return;
	Path programPath = folderpath.resolve(programName).toAbsolutePath();
	String projectPath = folderpath.toString();
	String args = programPath.toString();
	String outPath;
	String storeInputUsage = null;
	String sunversion = null;
	String tempdir = System.getProperty("java.io.tmpdir");
	if (toLanguage.toLowerCase().equals("c")) {
	    storeInputUsage = storeInput ? "_store_input" : "";
	    if ("2".equals(sunver))
		sunversion = "sundials_v2";
	    else if ("3".equals(sunver))
		sunversion = "sundials_v3";
	    else if ("4".equals(sunver))
		sunversion = "sundials_v4";
	    else if ("5".equals(sunver))
		sunversion = "sundials_v5";
	    else
		sunversion = "sundials_v" + sunver;

	    outPath = Paths
		    .get(tempdir, "tests", projectPath, toLanguage.toUpperCase() + "_" + sunversion + storeInputUsage)
		    .toAbsolutePath().toString();
	} else {
	    outPath = Paths.get(tempdir, "tests", projectPath, toLanguage.toUpperCase()).toAbsolutePath().toString();
	}
	new File(outPath).mkdirs();

	CompilerController c = new CompilerController(fromLanguage, toLanguage, inFile, args, outPath, backendOptions,
		frontendOptions);

	StringBuffer extraparams = new StringBuffer();
	if (sunver != null)
	    extraparams.append(" sundials_version=" + sunver);
	if (storeInput != null)
	    extraparams.append(" store_input=" + Boolean.toString(storeInput).toUpperCase());

	System.out.println("Tested parameters: from=" + fromLanguage.toUpperCase() + " to=" + toLanguage.toUpperCase()
		+ extraparams);
	try {
	    c.translate(false, true, true, true);
	    System.out.println("OK");
	} catch (Exception e) {
	    System.out.println("KO");
	}
    }

    public void compileAndRunC(String execName, Path folderpath) throws IOException, InterruptedException {

	// go in the folder, compile execute Path outputFolder =
	Path outputFolder = folderpath.resolve("C").toAbsolutePath(); // ----
								      // COMPILE
								      // ----
	ProcessBuilder builder = new ProcessBuilder("/usr/bin/make");
	builder.directory(outputFolder.toFile());
	Process pr = builder.start();
	pr.waitFor();

	BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	String line = "";
	StringBuffer output = new StringBuffer();
	while ((line = reader.readLine()) != null) {
	    output.append(line + "\n");
	}

	if (pr.exitValue() != 0)
	    throw new IOException("Error compiling output program:\n" + output.toString());

	builder = new ProcessBuilder("bash", "-c", "./" + execName);
	builder.directory(outputFolder.toFile());
	pr = builder.start();
	pr.waitFor();

	reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	line = "";
	output = new StringBuffer();
	while ((line = reader.readLine()) != null) {
	    output.append(line + "\n");
	}

	reader = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
	line = "";
	StringBuffer error = new StringBuffer();
	while ((line = reader.readLine()) != null) {
	    error.append(line + "\n");
	}

	if (pr.exitValue() != 0)
	    throw new IOException("Error running output program:\n" + output.toString() + "\n" + error.toString());

	return;

    }
}

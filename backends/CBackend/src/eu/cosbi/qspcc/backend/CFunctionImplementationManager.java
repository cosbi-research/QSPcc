package eu.cosbi.qspcc.backend;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import eu.cosbi.qspcc.ast.AASTNode;

public class CFunctionImplementationManager {
    protected static CFunctionImplementationManager INSTANCE;

    private final static String FUNCTION_IMPLEMENTATION_CSV = "functions_implementation.csv";

    private Map<String, String> functionNameAndScalarTranslation = new HashMap<String, String>();
    private Map<String, String> functionNamePreferredTranslation = new HashMap<String, String>();
    private Map<String, String> functionNameAndAlternativeTranslation = new HashMap<String, String>();

    protected CFunctionImplementationManager() throws Exception {
	// reads the file and fill the maps
	parsePreferredAndAlternativeCsv(FUNCTION_IMPLEMENTATION_CSV);
    }

    protected InputStream getPreferredAndAlternativeCsvInputStream(String csvName) throws FileNotFoundException {
	InputStream csvStream = CFunctionImplementationManager.class.getClassLoader()
		.getResourceAsStream("resources/" + csvName);
	if (csvStream == null) {
	    csvStream = new FileInputStream(
		    Paths.get("backends/CBackend/resources").toAbsolutePath().resolve(csvName).toString());
	}
	return csvStream;
    }

    // parse the csv file and populates the maps
    protected void parsePreferredAndAlternativeCsv(String csvName) throws IOException, URISyntaxException {

	InputStream csvStream = getPreferredAndAlternativeCsvInputStream(csvName);

	String content = IOUtils.toString(csvStream, Charset.defaultCharset());
	csvStream.close();

	String[] lines = content.split("\n");

	// skip the first row (headers)
	String[] elements = null;
	for (int i = 1; i < lines.length; i++) {
	    elements = lines[i].split(",");

	    if (elements.length < 3) {
		throw new IOException(
			"Functions implementation csv file is not as expected (" + elements.length + " elements)");
	    } else {
		// the first element is the function to be translated
		String functionToTranslate = elements[0];
		// the second element is the preferred translation
		String preferredTranslation = elements[1];
		// the third element is the alternative translation
		String alternativeTranslation = elements[2];
		String scalarTranslation = null;
		if (elements.length > 3)
		    scalarTranslation = elements[3];

		functionNamePreferredTranslation.put(functionToTranslate, preferredTranslation);
		functionNameAndAlternativeTranslation.put(functionToTranslate, alternativeTranslation);
		functionNameAndScalarTranslation.put(functionToTranslate, scalarTranslation);
	    }
	}

    }

    public static CFunctionImplementationManager getInstance() throws Exception {
	if (INSTANCE == null) {
	    INSTANCE = new CFunctionImplementationManager();
	}
	return INSTANCE;
    }

    public static void clearInstance() throws Exception {
	INSTANCE = null;
    }

    public String getTranslation(AASTNode curRoot, String functionToTranslate) {
	String translationFun = getPreferredTranslation(functionToTranslate);
	if (translationFun != null && !"null".equals(translationFun) && !"".equals(translationFun)) {
	    return translationFun;
	} else {
	    return getAlternativeTranslation(functionToTranslate);
	}
    }

    public String getPreferredTranslation(String function) {
	return functionNamePreferredTranslation.get(function);
    }

    public String getAlternativeTranslation(String function) {
	return functionNameAndAlternativeTranslation.get(function);
    }

    public String getScalarTranslation(String function) {
	return functionNameAndScalarTranslation.get(function);
    }
}

package eu.cosbi.qspcc.backend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

enum Tag {

	OTHER_PARAMS("ALL_OTHER_PARAMETERS"),
	// number of other params found
	N_OTHER_PARAMS("NUMBER_OF_OTHER_PARAMETERS"),
	// SPECIES -> VARIABLES IN ODE
	SPECIES_NAMES("SPECIES_NAMES"),
	// number of species found
	N_SPECIES_NAMES("NUMBER_OF_SPECIES"),
	// FLUXES -> RHS IN ODE DEFINITION
	FLUXES("FLUXES"),
	// REGEX TAG
	FLUXES_SECTION("FLUXES_SECTION:(.*)", true),
	// ODES -> assignment Y = fluxes
	ODES("ODES"),
	// REGEX TAG
	ODES_SECTION("ODES_SECTION:(.*)", true),
	// COLUMNS TAG
	INIT_CONDITION_NAMES("INIT_COND_COLUMN_NAME"), PARAM_NAME("PARAMETERS_COLUMN_NAME");

	private static Logger logger = LogManager.getLogger(Tag.class);

	private String tagName;
	private Pattern tagPattern;
	private boolean opened;

	private String tagVariant;
	private static Tag curOpenedTag;

	Tag(String tagName) {
		this(tagName, false);
	}

	Tag(String tagName, boolean contains_re) {
		this.tagName = tagName;
		opened = false;
		if (contains_re)
			this.tagPattern = Pattern.compile(tagName);
	}

	public static Tag fromString(String tagName) {
		for (Tag t : Tag.values())
			if (t.tagPattern == null) {
				if (t.tagName.equals(tagName))
					return t;
			} else {
				Matcher m = t.tagPattern.matcher(tagName);
				if (m.find())
					return t;
			}
		return null;
	}

	public static void open(Tag curTag, String tagName, boolean opening) {
		curTag.opened = opening;
		if (opening) {
			curTag.variant(tagName);
			curOpenedTag = curTag;
			// if we are opening a tag make sure to close all the others
			for (Tag t : Tag.values())
				if (t == curTag)
					continue;
				else if (t.opened) {
					logger.warn("implicitly closing tag '" + t + "'");
					t.opened = false;
				}
		} else
			curOpenedTag = null;

	}

	public static Tag opened() {
		return curOpenedTag;
	}

	@Override
	public String toString() {
		return tagName;
	}

	public String variant() {
		return tagVariant;
	}

	public String variant(String tagName) {
		if (tagPattern != null) {
			Matcher m = tagPattern.matcher(tagName);
			if (m.find())
				tagVariant = m.group(1);
		}
		return tagVariant;
	}

	public boolean isFlux() {
		return this.equals(Tag.FLUXES) || this.equals(Tag.FLUXES_SECTION);
	}

	public boolean isODE() {
		return this.equals(Tag.ODES) || this.equals(Tag.ODES_SECTION);
	}

}

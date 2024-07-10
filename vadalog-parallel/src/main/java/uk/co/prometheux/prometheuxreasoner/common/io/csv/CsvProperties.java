package uk.co.prometheux.prometheuxreasoner.common.io.csv;

import org.apache.commons.csv.QuoteMode;
import uk.co.prometheux.prometheuxreasoner.common.ConfigurationManager;
import uk.co.prometheux.prometheuxreasoner.common.io.DataSourceProperties;
import java.util.HashMap;

/**
 * Reads and stores the properties of the CSV.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 */
public class CsvProperties extends DataSourceProperties {

	private String NULL_STRING_DEFAULT = "\\N";
	private String RECORD_SEPARATOR_DEFAULT = "\n";
	private char DELIMITER_DEFAULT = ',';
	private QuoteMode QUOTE_MODE_DEFAULT = QuoteMode.NON_NUMERIC;

	private String USE_HEADERS_OPTION = "useHeaders";
	private String DELIMITER_OPTION = "delimiter";
	private String RECORD_SEPARATOR_OPTION = "recordSeparator";
	private String QUOTE_MODE_OPTION = "quoteMode";
	private String NULL_STRING_OPTION = "nullString";
	private String WITH_HEADER_NAME = "withHeader";
	/*
	 * Used when @chase annotation is enabled. It allows storing the chase in a
	 * format compatible with the neo4j bulk import command
	 */
	private String FOR_NEO4J_BULK_IMPORT_NAME = "forNeo4jBulkImport";

	private String datasource;

	public CsvProperties(String datasource, String propertyString) {
		this.properties = new HashMap<>();
		this.datasource = datasource;
		if (propertyString != null && !propertyString.isEmpty()) {
			parsePropertyString(propertyString);
		}
	}

	public Boolean getWithHeader() {
		if (properties.containsKey(USE_HEADERS_OPTION)) {
			return Boolean.valueOf(properties.get(USE_HEADERS_OPTION));
		}
		// if the header has not been set manually, use the config file
		return ConfigurationManager.getInstance()
				.getBoolProperty(datasource + ConfigurationManager.KEY_PATH_SEPARATOR + WITH_HEADER_NAME, true);
	}

	public char getDelimiter() {
		if (properties.containsKey(DELIMITER_OPTION)) {
			if (properties.get(DELIMITER_OPTION).equals("\t")) {
				return '\t';
			}
			return properties.get(DELIMITER_OPTION).charAt(0);
		}
		return DELIMITER_DEFAULT;
	}

	public String getRecordSeparator() {
		if (properties.containsKey(RECORD_SEPARATOR_OPTION)) {
			return properties.get(RECORD_SEPARATOR_OPTION);
		}
		return RECORD_SEPARATOR_DEFAULT;
	}

	public QuoteMode getQuoteMode() {
		if (properties.containsKey(QUOTE_MODE_OPTION)) {
			switch (properties.get(QUOTE_MODE_OPTION)) {
			case ("NON_NUMERIC"):
				return QuoteMode.NON_NUMERIC;
			case ("ALL"):
				return QuoteMode.ALL;
			case ("MINIMAL"):
				return QuoteMode.MINIMAL;
			case ("NONE"):
				return QuoteMode.NONE;
			default:
				return QUOTE_MODE_DEFAULT;
			}
		}
		return QUOTE_MODE_DEFAULT;
	}

	public String getNullString() {
		if (properties.containsKey(NULL_STRING_OPTION)) {
			return properties.get(NULL_STRING_OPTION);
		}
		return NULL_STRING_DEFAULT;
	}
	
	public Boolean getForNeo4jBulkImport() {
		if (properties.containsKey(FOR_NEO4J_BULK_IMPORT_NAME)) {
			return Boolean.valueOf(properties.get(FOR_NEO4J_BULK_IMPORT_NAME));
		}
		return ConfigurationManager.getInstance()
				.getBoolProperty(FOR_NEO4J_BULK_IMPORT_NAME, false);
	}
	
}

package uk.co.prometheux.prometheuxreasoner.common.io;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.spark.sql.SaveMode;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;
import uk.co.prometheux.prometheuxreasoner.common.ConfigurationManager;

/**
 * A class for handling datasource properties
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public abstract class DataSourceProperties {
	
	/**
	 * DB properties
	 */
	protected String PROTOCOL = "protocol";
	protected String HOST = "host";
	protected String PORT = "port";
	protected String USERNAME = "username";
	protected String PASSWORD = "password";
	protected String DATABASE = "database";

	protected String SAVE_MODE = "saveMode";
	protected String COALESCE = "coalesce";
	protected String COMPRESSION = "compression";
	protected String SELECTED_COLUMNS_OPTION = "selectedColumns";
	protected SaveMode SAVE_MODE_DEFAULT = SaveMode.Overwrite;	
	protected HashMap<String, String> properties;
	
	protected void parsePropertyString(String propertyString) {
		try {
			// escape cases when we have commas to cleanly separate the propertyString
			String propertyStringCleanArguments = propertyString.replaceAll("','", "commaTemp").replaceAll("[\n\t]",
					"");

			// split into entries and rewrite escaped commas
			String[] propertiesArray = Arrays.stream(propertyStringCleanArguments.split(","))
					.map(s -> s.replaceAll("commaTemp", ",").trim()).toArray(size -> new String[size]);

			for (String argument : propertiesArray) {
				String key = argument.substring(0, argument.lastIndexOf("="));
				String valueRaw = StringEscapeUtils.unescapeJava(argument.substring(argument.lastIndexOf("=") + 1));
				String value = valueRaw.startsWith("'") && valueRaw.endsWith("'")
						? valueRaw.substring(1, valueRaw.length() - 1)
						: valueRaw;
				properties.put(key, value);
			}
		} catch (Exception e) {
			throw new PrometheuxRuntimeException("Datasource commands are not written correctly "+e.getMessage());
		}
	}
	
	public String[] getSelectedColumns(String[] columns) {
		SelectedColumnHelper helper;
		if (properties.containsKey(SELECTED_COLUMNS_OPTION)) {
			String selectedColumnsList = properties.get(SELECTED_COLUMNS_OPTION);
			helper = new SelectedColumnHelper(columns, selectedColumnsList);
		} else {
			// if we do not select columns we return the original columns
			// and all the columns to select
			helper = new SelectedColumnHelper(columns);
		}
		return helper.getSelectedColumnsExprArr();

	}

	public Boolean getToCoalesce() {
		if (properties.containsKey(COALESCE)) {
			return Boolean.valueOf(properties.get(COALESCE));
		}
		return ConfigurationManager.getInstance().getBoolProperty("coalescePartitions", false);
	}
	
	public String getCompression() {
		if (properties.containsKey(COMPRESSION)) {
			return String.valueOf(properties.get(COMPRESSION));
		}
		return ConfigurationManager.getInstance().getProperty("compression","none");
	}
	
	public SaveMode getSaveMode() {
		if (properties.containsKey(SAVE_MODE)) {
			switch (properties.get(SAVE_MODE)) {
			case ("overwrite"):
				return SaveMode.Overwrite;
			case ("errorIfExists"):
				return SaveMode.ErrorIfExists;
			case ("append"):
				return SaveMode.Append;
			case ("ignore"):
				return SaveMode.Ignore;
			default:
				return SAVE_MODE_DEFAULT;
			}
		}
		return SAVE_MODE_DEFAULT;
	}

}

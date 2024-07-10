package uk.co.prometheux.prometheuxreasoner.common.io.parquet;

import uk.co.prometheux.prometheuxreasoner.common.io.DataSourceProperties;
import java.util.HashMap;

/**
 * Reads and stores the properties of the CSV.
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 */
public class ParquetProperties extends DataSourceProperties {

	public ParquetProperties(String propertyString) {
		this.properties = new HashMap<>();
		if (propertyString != null && !propertyString.isEmpty()) {
			parsePropertyString(propertyString);
		}
	}
	
	@Override
	public String getCompression() {
		return "snappy";
	}

}

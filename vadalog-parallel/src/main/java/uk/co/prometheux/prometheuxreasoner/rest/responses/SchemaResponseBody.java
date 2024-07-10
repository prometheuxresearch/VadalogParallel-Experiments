package uk.co.prometheux.prometheuxreasoner.rest.responses;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * A Response for the schema
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class SchemaResponseBody implements PrometheuxResponseBody {
	
	private Map<String, List<Set<Entry<String, Integer>>>> schema;
	
	public SchemaResponseBody(Map<String, List<Set<Entry<String, Integer>>>> schema) {
		this.setSchema(schema);
	}

	public Map<String, List<Set<Entry<String, Integer>>>> getSchema() {
		return schema;
	}

	public void setSchema(Map<String, List<Set<Entry<String, Integer>>>> schema) {
		this.schema = schema;
	}

}

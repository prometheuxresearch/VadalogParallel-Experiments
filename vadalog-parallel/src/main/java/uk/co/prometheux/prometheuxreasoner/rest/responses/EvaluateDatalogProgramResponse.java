package uk.co.prometheux.prometheuxreasoner.rest.responses;

import java.util.List;
import java.util.Map;

import uk.co.prometheux.prometheuxreasoner.model.annotations.TypeEnum;

/**
 * A response to the evaluation of a program.
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class EvaluateDatalogProgramResponse implements PrometheuxResponseBody {

	private final long id; // the id of the result
	/* the result set contains for each predicate */
	/* marked as @output, the list of the rows */

	public long getId() {
		return this.id;
	}

	private final Map<String, List<List<Object>>> resultSet;

	public Map<String, List<List<Object>>> getResultSet() {
		return this.resultSet;
	}

	/**
	 * Types are derived from the first values of all the derived facts.
	 */
	private final Map<String, List<TypeEnum>> types;

	public Map<String, List<TypeEnum>> getTypes() {
		return this.types;
	}

	private final Map<String, List<String>> columnNames;

	public Map<String, List<String>> getColumnNames() {
		return columnNames;
	}

	public EvaluateDatalogProgramResponse(long id, Map<String, List<List<Object>>> resultSet,
			Map<String, List<TypeEnum>> types, Map<String, List<String>> columnNames) {
		this.id = id;
		this.resultSet = resultSet;
		this.types = types;
		this.columnNames = columnNames;
	}
}
package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * It binds a predicate to a dataSource, a schema and a table/query.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 */
public class BindAnnotation extends DatalogAnnotation implements Serializable {

	private static final String BIND_ANNOTATION_NAME = "bind";
	private static final String QBIND_ANNOTATION_NAME = "qbind";

	private static final long serialVersionUID = 1L;
	private final String predicateName;
	private final String dataSource;
	private final String schema;
	private final String tableOrQuery;
	/* isQuery represents whether tableOrQuery */
	/* is a query or directly a table name. */
	private final boolean isQuery;

	public BindAnnotation(String predicateName, String dataSource, String schema, String table, boolean isQuery) {
		super(isQuery ? QBIND_ANNOTATION_NAME : BIND_ANNOTATION_NAME,
				Arrays.asList(predicateName, dataSource, schema, table));
		this.predicateName = predicateName;
		this.dataSource = dataSource;
		this.schema = schema;
		this.tableOrQuery = table;
		this.isQuery = isQuery;
	}

	public BindAnnotation(String predicateName) {
		this(predicateName, "", "", "", false);
	}

	public String getPredicateName() {
		return predicateName;
	}

	public String getDataSource() {
		return dataSource;
	}

	public String getSchema() {
		return schema;
	}

	public String getTableOrQuery() {
		return tableOrQuery;
	}

	public boolean isQuery() {
		return isQuery;
	}

	/**
	 * With the support of parameterisation, this function returns {@literal true},
	 * if the current {@literal @qbind} is parametric. See
	 * {@linkplain ParametricQueryManager} for more details.
	 * 
	 * @return {@literal true}, if the qbind annotation represent a parametric
	 *         qbind.
	 */
	public boolean isParametric() {
		if (this.isQuery) {
//			return ParametricQueryManager.isParametric(getTableOrQuery());
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hashCode(dataSource);
		result = prime * result + (isQuery ? 1231 : 1237);
		result = prime * result + Objects.hashCode(predicateName);
		result = prime * result + Objects.hashCode(schema);
		result = prime * result + Objects.hashCode(tableOrQuery);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		BindAnnotation other = (BindAnnotation) obj;
		return Objects.equals(this.dataSource, other.dataSource) && Objects.equals(this.isQuery, other.isQuery)
				&& Objects.equals(this.predicateName, other.predicateName) && Objects.equals(this.schema, other.schema)
				&& Objects.equals(this.tableOrQuery, other.tableOrQuery);
	}
}

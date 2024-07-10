package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.io.Serializable;
import java.util.Arrays;

/**
 * It maps a term of a bound predicate (used in a bind annotation) to a specific
 * column of the respective table in the data source.
 * 
 * \@mapping("pred", pos, "colName", "colType") e.g. \@mapping("p", 3,
 * "aColumn", "colType").
 * 
 * It is used together with InputAnnotation to read a predicate from the input.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 */
public class MappingAnnotation extends DatalogAnnotation implements Serializable {

	private static final long serialVersionUID = 1L;
	/* the name of the predicate */
	private final String predicateName;
	/* the position of the argument in the predicate it is mapping */
	/* argPos starts from 0 */
	private final int argPos;
	/* the name of the column in the source */
	private final String colName;
	/* the type of the column in the source */
	private final TypeEnum colType;
	/*
	 * whether this annotation has been inferred (and hence is not reliable in terms
	 * of name and type).
	 */
	private boolean isInferred = false;

	public MappingAnnotation(String predicateName, int argPos, String colName, TypeEnum colType) {
		super("mapping", Arrays.asList(predicateName, argPos, colName, colType.toString()));
		this.predicateName = predicateName;
		this.argPos = argPos;
		this.colName = colName;
		this.colType = colType;
	}

	MappingAnnotation(String predicateName, int argPos, String colName, String colType) {
		super("mapping", Arrays.asList(predicateName, argPos, colName, colType));
		this.predicateName = predicateName;
		this.argPos = argPos;
		this.colName = colName;

		if (colType.equalsIgnoreCase("string"))
			this.colType = TypeEnum.STRING;
		else if (colType.equalsIgnoreCase("int"))
			this.colType = TypeEnum.INT;
		else if (colType.equalsIgnoreCase("double"))
			this.colType = TypeEnum.DOUBLE;
		else if (colType.equalsIgnoreCase("boolean"))
			this.colType = TypeEnum.BOOLEAN;
		else if (colType.equalsIgnoreCase("date"))
			this.colType = TypeEnum.DATE;
		else if (colType.equalsIgnoreCase("set"))
			this.colType = TypeEnum.SET;
		else
			this.colType = TypeEnum.UNKNOWN;
	}

	public String getPredicateName() {
		return predicateName;
	}

	public int getArgPos() {
		return argPos;
	}

	public String getColName() {
		return colName;
	}

	public TypeEnum getColType() {
		return colType;
	}

	public boolean isInferred() {
		return isInferred;
	}

	public void setInferred(boolean isInferred) {
		this.isInferred = isInferred;
	}

}

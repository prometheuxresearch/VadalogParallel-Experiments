package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.util.HashSet;
import java.util.Set;

/**
 * The possible types for the columns in the mapping annotation
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public enum TypeEnum {
	STRING("string", "str"),
	INT("integer", "int"),
	DOUBLE("double"),
	BOOLEAN("boolean", "bool"),
	SET("set"),
	LIST("list"),
	DATE("date"),
	UNKNOWN("unknown"),
	;
	
	private Set<String> names;
	
	TypeEnum(String... names) {
		this.names = new HashSet<>();
		this.names.add(this.name().toLowerCase());
		for (String name : names) this.names.add(name.toLowerCase());
	}

	@Override
	public String toString() {
		if(this.equals(TypeEnum.STRING) )
			return "STRING";
		else if(this.equals(TypeEnum.INT))
			return "INT";
		else if(this.equals(TypeEnum.DOUBLE))
			return "DOUBLE";
		else if(this.equals(TypeEnum.BOOLEAN))
			return "BOOLEAN";
		else if(this.equals(TypeEnum.DATE))
			return "DATE";
		else if(this.equals(TypeEnum.SET))
			return "{}";
		else if(this.equals(TypeEnum.LIST))
			return "[]";
		
		else return "none";
	}

	public Set<String> getNames() {
		return names;
	}

	public String getName() {
		return this.names.iterator().next();
	}
	
	public String getType() {
		if(this.equals(TypeEnum.STRING) )
			return "string";
		else if(this.equals(TypeEnum.INT))
			return "integer";
		else if(this.equals(TypeEnum.DOUBLE))
			return "double";
		else if(this.equals(TypeEnum.BOOLEAN))
			return "boolean";
		else if(this.equals(TypeEnum.DATE))
			return "date";
		else if(this.equals(TypeEnum.SET))
			return "array<string>";
		else if(this.equals(TypeEnum.LIST))
			return "array<string>";
		/*
		 * Unknown type is used for mapping used as placeholder. In case of read, the
		 * mapping is simply skipped. In case of write, the column name of the mapping
		 * is _ci and the column type is unknown so the case will not be applied
		 */
		else if(this.equals(TypeEnum.UNKNOWN))
			return "unknown";
		else return "none";
	}
	
}

package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.util.List;

/**
 * An abstract class for DatalogAnnotations having aliases
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public abstract class AliasAnnotation extends DatalogAnnotation {

	private final String alias;

	AliasAnnotation(String alias, String annotationName, List<Object> annotationArguments) {
		super(annotationName, annotationArguments);
		assert (alias != null);
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}
}

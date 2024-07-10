package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.util.Collections;

/**
 * 
 * It defines the dependency between modules.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class IncludeAnnotation extends DatalogAnnotation {
	
	static final String ANNOTATION_NAME = "include";
	
	IncludeAnnotation(String moduleName) {
		super(ANNOTATION_NAME, Collections.singletonList(moduleName));
	}

	public String getModuleName() {
		return (String)getArguments().get(0);
	}

}
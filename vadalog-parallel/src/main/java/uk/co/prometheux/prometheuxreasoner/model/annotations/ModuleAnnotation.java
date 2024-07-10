package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.util.Collections;

/**
 * 
 * Module annotation defines the name of the module which is represented by the
 * Vadalog program.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class ModuleAnnotation extends DatalogAnnotation {

	static final String ANNOTATION_NAME = "module";

	ModuleAnnotation(String moduleName) {
		super(ANNOTATION_NAME, Collections.singletonList(moduleName));
	}

	public String getModuleName() {
		return (String) getArguments().get(0);
	}

}
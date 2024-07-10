package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.util.Collections;

/**
 * It declares that a predicate is an output e.g. @output("p")
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 */
public class OutputAnnotation extends DatalogAnnotation implements InputOutputAnnotation {

	private final String predicateName;

	public OutputAnnotation(String predicateName) {
		super("output", Collections.singletonList(predicateName));
		this.predicateName = predicateName;
	}

	@Override
	public String getPredicateName() {
		return predicateName;
	}

}
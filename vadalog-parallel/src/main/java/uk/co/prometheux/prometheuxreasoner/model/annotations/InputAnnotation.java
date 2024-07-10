package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.util.Collections;

/**
 * It declares that a predicate is an input
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class InputAnnotation extends DatalogAnnotation implements InputOutputAnnotation {
	
	private final String predicateName;

	InputAnnotation(String predicateName) {
		super("input", Collections.singletonList(predicateName));
		this.predicateName = predicateName;
	}

	@Override
	public String getPredicateName() {
		return predicateName;
	}

}

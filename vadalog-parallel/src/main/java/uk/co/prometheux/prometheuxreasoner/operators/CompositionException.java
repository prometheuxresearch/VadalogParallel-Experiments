package uk.co.prometheux.prometheuxreasoner.operators;

/**
 * It represents that two rules cannot be composed with one another
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
class CompositionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	CompositionException(String message) {
		super(message);
	}

}

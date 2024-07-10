package uk.co.prometheux.prometheuxreasoner.operators;

/**
 * This exception is raised if an expression uses a non implemented operator.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class UnsupportedOperatorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnsupportedOperatorException(String message) {
		super(message);
	}
}
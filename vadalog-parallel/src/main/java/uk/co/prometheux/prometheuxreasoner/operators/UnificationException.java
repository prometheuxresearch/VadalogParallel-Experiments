package uk.co.prometheux.prometheuxreasoner.operators;

/**
 * 
 * This Class is an exception to handle Unification issues
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

public class UnificationException extends Exception {
	private static final long serialVersionUID = 1L;

	UnificationException(String message) {
		super(message);
	}
}
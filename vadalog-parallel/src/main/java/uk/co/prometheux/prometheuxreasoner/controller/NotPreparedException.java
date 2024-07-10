package uk.co.prometheux.prometheuxreasoner.controller;

/**
 * This exception is thrown when we are trying to set placeholders
 * in a program that has not been prepared.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 */
class NotPreparedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	NotPreparedException(String message) {
		super(message);
	}

}

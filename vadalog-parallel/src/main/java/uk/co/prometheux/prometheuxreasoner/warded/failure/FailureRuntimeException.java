package uk.co.prometheux.prometheuxreasoner.warded.failure;

/**
 * An Exception to handle failure rule
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class FailureRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FailureRuntimeException(String message) {
		super(message);
	}

}

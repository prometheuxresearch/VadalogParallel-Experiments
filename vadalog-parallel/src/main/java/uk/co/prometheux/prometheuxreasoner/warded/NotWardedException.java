package uk.co.prometheux.prometheuxreasoner.warded;

/**
 * This exception is thrown if the fragment is not warded.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class NotWardedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotWardedException(String message) {
		super(message);
	}

}

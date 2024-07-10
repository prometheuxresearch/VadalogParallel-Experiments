package uk.co.prometheux.prometheuxreasoner.warded.egdHarmless;

/**
 * This exception is thrown if the sufficient condition for a set of harmless
 * EGDs is not satisfied.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class HarmlessEGDSufficientConditionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HarmlessEGDSufficientConditionException(String message) {
		super(message);
	}

}

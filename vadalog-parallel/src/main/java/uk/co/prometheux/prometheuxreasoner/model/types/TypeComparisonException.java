package uk.co.prometheux.prometheuxreasoner.model.types;

/**
 * This exception is raised when two vada values are compared but they do not
 * have the same data type or no type at all.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class TypeComparisonException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	TypeComparisonException(String message) {
		super(message);
	}
}
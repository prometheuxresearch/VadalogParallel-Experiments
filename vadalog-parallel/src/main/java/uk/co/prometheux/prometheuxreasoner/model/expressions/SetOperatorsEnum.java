package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * Constants for the operations on sets.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public enum SetOperatorsEnum {
	UNION, INTERSECTION;

	public String toString() {
		switch (this) {
		case UNION:
			return "|";
		case INTERSECTION:
			return "&";
		default:
			return "none";
		}
	}
}
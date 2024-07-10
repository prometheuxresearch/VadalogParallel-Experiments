package uk.co.prometheux.prometheuxreasoner.model;

/**
 * It is a comparison operator that can be used in expressions.

 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public enum ComparisonOperatorsEnum {
	GT, GE, LT, LE, EQ, NEQ, IN, NOTIN;
	
	public String toString() {
		switch (this) {
			case LT:
				return "<";
			case GT:
				return ">";
			case EQ:
				return "=";
			case LE:
				return "<=";
			case GE:
				return ">=";
			case NEQ:
				return "<>";
			case IN:
				return "in";
			case NOTIN:
				return "!in";
		}
		return "none";
	}

}
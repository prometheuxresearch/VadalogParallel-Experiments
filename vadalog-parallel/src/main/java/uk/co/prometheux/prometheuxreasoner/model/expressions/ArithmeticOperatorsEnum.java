package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * The operators used in the expressions.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 */
enum ArithmeticOperatorsEnum {
	PLUS, MINUS, PROD, DIV, NOT, LT, LE, GT, GE, EQEQ, NEQ, AND, OR;
	
		public String toString() {
			switch (this) {
				case PLUS:
					return "+";
				case MINUS:
					return "-";
				case PROD:
					return "*";
				case DIV:
					return "/";
				case NOT:
					return "!";
				case LT:
					return "<";
				case LE:
					return "<=";
				case GT:
					return ">";
				case GE:
					return ">=";
				case EQEQ:
					return "==";
				case NEQ:
					return "!=";
				case AND:
					return "&&";
				case OR:
					return "||";
				default:
					return null;
			}
	}
}
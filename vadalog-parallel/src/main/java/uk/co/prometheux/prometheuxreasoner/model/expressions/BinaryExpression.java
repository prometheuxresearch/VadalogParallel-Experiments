package uk.co.prometheux.prometheuxreasoner.model.expressions;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * An abstract Class providing default implementations for binary expressions
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public abstract class BinaryExpression extends Expression {

	private static final long serialVersionUID = 1L;

	protected final Expression lhsOperand;
	protected final Expression rhsOperand;

	/**
	 * A constructor initialising the expression using its two operands
	 * 
	 * @param firstOperand  is the first of the expression's operands
	 * @param secondOperand is the second of the expression's operands
	 */
	BinaryExpression(Expression firstOperand, Expression secondOperand) {
		super();
		this.lhsOperand = firstOperand;
		this.rhsOperand = secondOperand;
	}

	/**
	 * Returns the arity of the expression
	 * 
	 * @return the expression's arity
	 */
	@Override
	public int getArity() {
		return 2;
	}

	/**
	 * Returns whether the operation uses the infix notation
	 * 
	 * @return whether the operation uses the infix notation
	 */
	public abstract boolean isInfix();

	/**
	 * Returns the first operand of the expression
	 * 
	 * @return the first operand of the expression
	 */
	public Expression getLhsOperand() {
		return lhsOperand;
	}

	/**
	 * Returns the second operand of the expression
	 * 
	 * @return the second operand of the expression
	 */
	public Expression getRhsOperand() {
		return rhsOperand;
	}

	/**
	 * Returns a default string representation for binary expressions
	 * 
	 * @return teh default string representation for binary expressions
	 */
	@Override
	public String toString() {
		if (isInfix())
			return lhsOperand.toString() + getOperationName() + rhsOperand.toString();
		else
			return getOperationName() + "(" + Objects.toString(lhsOperand) + ", " + Objects.toString(rhsOperand) + ")";

	}

	/**
	 * A default implementation for binary expressions that returns the variables of
	 * the two operands
	 * 
	 * @return the variables of the two operands
	 */
	@Override
	public Set<Variable> getAllVariables() {
		Set<Variable> aVars = new HashSet<>(lhsOperand.getAllVariables());
		aVars.addAll(rhsOperand.getAllVariables());
		return aVars;
	}

	/**
	 * A default implementation combining the hashcodes of the operation name and
	 * the hash codes of the two arguments
	 * 
	 * @return the computed hash code
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getOperationName().hashCode();
		result = prime * result + Objects.hashCode(lhsOperand);
		result = prime * result + Objects.hashCode(rhsOperand);
		return result;
	}

	/**
	 * A default implementation for equals comparing the operation names and the two
	 * operands recursively
	 * 
	 * @param obj is the object for comparison
	 * @return returns if the two objects represent the same expression
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		BinaryExpression other = (BinaryExpression) obj;
		return Objects.equals(this.getOperationName(), other.getOperationName())
				&& Objects.equals(this.lhsOperand, other.lhsOperand)
				&& Objects.equals(this.rhsOperand, other.rhsOperand);
	}

}

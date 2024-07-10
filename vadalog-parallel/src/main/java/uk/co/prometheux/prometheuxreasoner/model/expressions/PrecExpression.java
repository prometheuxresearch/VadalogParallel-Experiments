package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * This expression is used to enforce precedence (i.e. bracketing)
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class PrecExpression extends UnaryExpression {

	private static final long serialVersionUID = 1L;

	public PrecExpression(Expression operand) {
		super(operand);
	}

	@Override
	public String getOperationName() {
		return "PREC";
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new PrecExpression(operand.clone(renameVariables));
	}

	@Override
	public String toString() {
		return "(" + operand.toString() + ")";
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

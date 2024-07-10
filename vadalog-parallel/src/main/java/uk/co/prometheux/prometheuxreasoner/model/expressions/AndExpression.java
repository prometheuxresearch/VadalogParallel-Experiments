package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * This Class represents the And Binary expression
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class AndExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;

	public AndExpression(Expression firstOperand, Expression secondOperand) {
		super(firstOperand, secondOperand);
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	@Override
	public String getOperationName() {
		return ArithmeticOperatorsEnum.AND.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new AndExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

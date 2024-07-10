package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * Expression representing the string operation 'starts with'
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class StartsWithExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;

	public StartsWithExpression(Expression firstOperand, Expression secondOperand) {
		super(firstOperand, secondOperand);
	}

	@Override
	public boolean isInfix() {
		return false;
	}

	@Override
	public String getOperationName() {
		return StringOperatorsEnum.STARTS_WITH.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new StartsWithExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

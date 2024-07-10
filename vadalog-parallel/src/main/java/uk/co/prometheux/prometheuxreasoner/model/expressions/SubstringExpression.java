package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * Represents the string operation computing the substring of a string
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class SubstringExpression extends TernaryExpression {
	
	private static final long serialVersionUID = 1L;

	public SubstringExpression(Expression firstExpression, Expression secondExpression, Expression thirdExpression) {
		super(firstExpression, secondExpression, thirdExpression);
	}

	@Override
	public String getOperationName() {
		return StringOperatorsEnum.SUBSTRING.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new SubstringExpression(firstOperand, secondOperand, thirdOperand);
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

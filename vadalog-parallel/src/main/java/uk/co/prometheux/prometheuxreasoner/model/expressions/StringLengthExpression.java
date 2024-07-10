package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * Represents the string operation computing the length of a string
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class StringLengthExpression extends UnaryExpression {

	private static final long serialVersionUID = 1L;

	public StringLengthExpression(Expression operand) {
		super(operand);
	}

	@Override
	public String getOperationName() {
		return StringOperatorsEnum.STRING_LENGTH.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new StringLengthExpression(operand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

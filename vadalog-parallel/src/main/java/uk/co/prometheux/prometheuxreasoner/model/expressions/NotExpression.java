package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * This Class implements the Not UnaryExpression
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class NotExpression extends UnaryExpression {
	
	private static final long serialVersionUID = 1L;
	
	public NotExpression(Expression operand) {
		super(operand);
	}

	@Override
	public String getOperationName() {
		return ArithmeticOperatorsEnum.NOT.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new NotExpression(this.operand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

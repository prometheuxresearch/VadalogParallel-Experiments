package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * Represents the numeric comparison operation <=
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class LeExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;

	public LeExpression(Expression lex, Expression rex) {
		super(lex, rex);
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	@Override
	public String getOperationName() {
		return ArithmeticOperatorsEnum.LE.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new LeExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * Represents the numeric comparison operation <
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class LtExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;
	
	public LtExpression(Expression lex, Expression rex) {
		super(lex, rex);
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	@Override
	public String getOperationName() {
		return ArithmeticOperatorsEnum.LT.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new LtExpression(this.lhsOperand.clone(renameVariables), this.rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}
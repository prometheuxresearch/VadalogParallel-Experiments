package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * The binary sum expression.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class PlusExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;

	public PlusExpression(Expression lex, Expression rex) {
		super(lex, rex);
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	@Override
	public String getOperationName() {
		return ArithmeticOperatorsEnum.PLUS.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new PlusExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

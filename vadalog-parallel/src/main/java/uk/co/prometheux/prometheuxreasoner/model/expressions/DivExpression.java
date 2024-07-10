package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * The binary division expression.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class DivExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;

	public DivExpression(Expression lex, Expression rex) {
		super(lex, rex);
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	public Expression clone(boolean renameVariables) {
		return new DivExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public String getOperationName() {
		return ArithmeticOperatorsEnum.DIV.toString();
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

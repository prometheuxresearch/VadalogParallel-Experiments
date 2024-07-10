package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * An expression representing the numeric product
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class ProdExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;

	public ProdExpression(Expression lex, Expression rex) {
		super(lex, rex);
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	@Override
	public String getOperationName() {
		return ArithmeticOperatorsEnum.PROD.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new ProdExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

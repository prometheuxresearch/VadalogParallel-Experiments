package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * An expression representing the numeric comparison >=
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class GeExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;

	public GeExpression(Expression lex, Expression rex) {
		super(lex, rex);
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	@Override
	public String getOperationName() {
		return ArithmeticOperatorsEnum.GE.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new GeExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

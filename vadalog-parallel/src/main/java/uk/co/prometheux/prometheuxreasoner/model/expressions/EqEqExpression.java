package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * An expression representing equality comparison
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class EqEqExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;

	public EqEqExpression(Expression lex, Expression rex) {
		super(lex, rex);
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	@Override
	public String getOperationName() {
		return ArithmeticOperatorsEnum.EQEQ.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new EqEqExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

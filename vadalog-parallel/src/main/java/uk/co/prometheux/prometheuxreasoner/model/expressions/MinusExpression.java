package uk.co.prometheux.prometheuxreasoner.model.expressions;

import java.io.Serializable;

/**
 * The binary minus expression
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class MinusExpression extends BinaryExpression implements Serializable {

	private static final long serialVersionUID = 1L;

	public MinusExpression(Expression lex, Expression rex) {
		super(lex, rex);
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	@Override
	public String getOperationName() {
		return ArithmeticOperatorsEnum.MINUS.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new MinusExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

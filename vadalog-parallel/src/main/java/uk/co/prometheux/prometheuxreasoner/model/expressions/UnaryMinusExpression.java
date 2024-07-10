package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * This Class implements the Minus Unary Expression
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class UnaryMinusExpression extends UnaryExpression {
	
	private static final long serialVersionUID = 1L;

	public UnaryMinusExpression(Expression lex) {
		super(lex);
	}

	@Override
	public String getOperationName() {
		return ArithmeticOperatorsEnum.MINUS.toString();
	}

	public Expression clone(boolean renameVariables) {
		return new UnaryMinusExpression(this.operand.clone(renameVariables));
	}
	
	public String toString() {
		return ArithmeticOperatorsEnum.MINUS + operand.toString();
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

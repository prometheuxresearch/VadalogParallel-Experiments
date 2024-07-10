package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * Represents the operation 'not equal'
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class NeqExpression extends BinaryExpression {
	
	private static final long serialVersionUID = 1L;

	public NeqExpression(Expression lex, Expression rex) {
		super(lex, rex);
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	@Override
	public String getOperationName() {
		return ArithmeticOperatorsEnum.NEQ.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new NeqExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

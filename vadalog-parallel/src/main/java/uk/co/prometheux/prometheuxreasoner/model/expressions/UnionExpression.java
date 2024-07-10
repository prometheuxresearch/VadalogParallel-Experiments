package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * It represents the union of two sets or the addition of an element to a set.
 * The syntax is a | b where either a or b is a set. It automatically upcasts a
 * or b to a set and returns the union of them.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class UnionExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;

	public UnionExpression(Expression lex, Expression rex) {
		super(lex, rex);
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	@Override
	public String getOperationName() {
		return SetOperatorsEnum.UNION.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new UnionExpression(this.lhsOperand.clone(renameVariables), this.rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}
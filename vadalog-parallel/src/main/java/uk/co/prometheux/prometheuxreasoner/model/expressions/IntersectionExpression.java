package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * It represents the intersection of two sets.
 * The syntax is a ^ b where a and b are a sets.
 *  
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class IntersectionExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;
	/**
	 * It builds an intersection expression.
	 * @param lex the left operand of the expression
	 * @param rex the right operand of the expression
	 */
	public IntersectionExpression(Expression lex, Expression rex) {
		super(lex, rex);
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	@Override
	public String getOperationName() {
		return SetOperatorsEnum.INTERSECTION.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new IntersectionExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}
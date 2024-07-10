package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * An expression representing set containment
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class ContainsExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs ContainsExpression from its two operands
	 * 
	 * @param firstOperand  the first operand of the expression
	 * @param secondOperand the second operand of the expression
	 */
	public ContainsExpression(Expression firstOperand, Expression secondOperand) {
		super(firstOperand, secondOperand);
	}

	@Override
	public boolean isInfix() {
		return false;
	}

	@Override
	public String getOperationName() {
		return StringOperatorsEnum.CONTAINS.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new ContainsExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

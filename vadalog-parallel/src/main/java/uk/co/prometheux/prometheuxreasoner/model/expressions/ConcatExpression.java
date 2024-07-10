package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * An expression representing string concatenation
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class ConcatExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the ConcatExpression using its two operands
	 * 
	 * @param firstOperand  the first operand of the expression
	 * @param secondOperand the second operand of the expression
	 */
	public ConcatExpression(Expression firstOperand, Expression secondOperand) {
		super(firstOperand, secondOperand);
	}

	@Override
	public boolean isInfix() {
		return false;
	}

	@Override
	public String getOperationName() {
		return StringOperatorsEnum.CONCAT.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new ConcatExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

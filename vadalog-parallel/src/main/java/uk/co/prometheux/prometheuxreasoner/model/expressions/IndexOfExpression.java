package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * An expression representing the binary string operation 'index of'
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class IndexOfExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;
	
	public IndexOfExpression(Expression firstOperand, Expression secondOperand) {
		super(firstOperand, secondOperand);
	}

	@Override
	public boolean isInfix() {
		return false;
	}

	@Override
	public String getOperationName() {
		return StringOperatorsEnum.INDEX_OF.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new IndexOfExpression(lhsOperand.clone(renameVariables), rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

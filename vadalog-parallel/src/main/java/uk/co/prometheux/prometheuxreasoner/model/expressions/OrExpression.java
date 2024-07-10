package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * An expression that represents Boolean disjunction
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class OrExpression extends BinaryExpression {

	private static final long serialVersionUID = 1L;

	public OrExpression(Expression lex, Expression rex) {
		super(lex, rex);
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	@Override
	public String getOperationName() {
		return ArithmeticOperatorsEnum.OR.toString();
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new OrExpression(this.lhsOperand.clone(renameVariables), this.rhsOperand.clone(renameVariables));
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

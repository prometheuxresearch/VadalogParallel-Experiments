package uk.co.prometheux.prometheuxreasoner.model.expressions;

import java.util.List;

/**
 * Expression representing a skolem function call
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class SkolemExpression extends NaryExpression {

	private static final long serialVersionUID = 1L;

	public SkolemExpression(String functionName, List<Expression> operandList) {
		super("#" + functionName, operandList);
	}

	@Override
	public Expression clone(boolean renameVariables) {
		return new SkolemExpression(operationName, this.getOperandList());
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

}

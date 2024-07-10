package uk.co.prometheux.prometheuxreasoner.model.expressions;

import uk.co.prometheux.prometheuxreasoner.model.Constant;
import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * A default implementation of the ExpressionVisitor interface that recursively
 * traverses expressions without returning argument
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class ExpressionVisitorDefault<Input> implements ExpressionVisitor<Void, Input> {

	@Override
	public Void visit(AndExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(ImplementationExpression expression, Input input) {
		expression.operandList.forEach(x -> x.accept(this, input));
		return null;
	}

	@Override
	public Void visit(ConcatExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(Constant<?> expression, Input input) {
		return null;
	}

	@Override
	public Void visit(ContainsExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(DivExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(EndsWithExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(EqEqExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(GeExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(GtExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(IndexOfExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(IntersectionExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(LeExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(LtExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

//	@Override
//	public Void visit(AggregationExpression expression, Input input) {
//		expression.getOperand().accept(this, input);
//		return null;
//	}

	@Override
	public Void visit(MinusExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(NeqExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(NotExpression expression, Input input) {
		expression.operand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(OrExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(PlusExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(PrecExpression expression, Input input) {
		expression.operand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(ProdExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(StringLengthExpression expression, Input input) {
		expression.operand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(SkolemExpression expression, Input input) {
		expression.operandList.forEach(x -> x.accept(this, input));
		return null;
	}

	@Override
	public Void visit(StartsWithExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(SubstringExpression expression, Input input) {
		expression.firstOperand.accept(this, input);
		expression.secondOperand.accept(this, input);
		expression.thirdOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(UnaryMinusExpression expression, Input input) {
		expression.operand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(UnionExpression expression, Input input) {
		expression.lhsOperand.accept(this, input);
		expression.rhsOperand.accept(this, input);
		return null;
	}

	@Override
	public Void visit(Variable expression, Input input) {
		return null;
	}

}

package uk.co.prometheux.prometheuxreasoner.model.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import uk.co.prometheux.prometheuxreasoner.model.Constant;
import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * A default implementation of the ExpressionVisitor interface that takes a variable substitution and recursively traverses expressions 
 * and performs the substitution
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 */
public class ExpressionVisitorSubstitutor implements ExpressionVisitor<Expression, Map<Variable,Variable>> {

	@Override
	public Expression visit(AndExpression expression, Map<Variable, Variable> input) {
		return new AndExpression(expression.lhsOperand.accept(this, input), expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(ImplementationExpression expression, Map<Variable, Variable> input) {
		return new ImplementationExpression(expression.operationName, expression.operandList.stream().map(x -> x.accept(this, input)).collect(Collectors.toList()));
	}

	@Override
	public Expression visit(ConcatExpression expression,
			Map<Variable, Variable> input) {
		return new ConcatExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(Constant<?> expression,
			Map<Variable, Variable> input) {
		return expression;
	}

	@Override
	public Expression visit(ContainsExpression expression,
			Map<Variable, Variable> input) {
		return new ContainsExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(DivExpression expression,
			Map<Variable, Variable> input) {
		return new DivExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(EndsWithExpression expression,
			Map<Variable, Variable> input) {
		return new EndsWithExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(EqEqExpression expression,
			Map<Variable, Variable> input) {
		return new EqEqExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(GeExpression expression,
			Map<Variable, Variable> input) {
		return new GeExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(GtExpression expression,
			Map<Variable, Variable> input) {
		return new GtExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(IndexOfExpression expression,
			Map<Variable, Variable> input) {
		return new IndexOfExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(IntersectionExpression expression,
			Map<Variable, Variable> input) {
		return new IntersectionExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(LeExpression expression,
			Map<Variable, Variable> input) {
		return new LeExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(LtExpression expression,
			Map<Variable, Variable> input) {
		return new LtExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

//	@Override
//	public Expression visit(AggregationExpression expression,
//			Map<Variable, Variable> input) {
//		return new AggregationExpression(expression.getAggregationOperator(),
//				expression.getOperand().accept(this, input),
//				substituteList(expression.getContributions(),input),
//				substituteList(expression.getGroupByVariables(),input));
//	}

	private List<Variable> substituteList(List<Variable> oldlist,
			Map<Variable, Variable> input) {
		List<Variable> newList = new ArrayList<>();
		for (Variable var : oldlist)
			newList.add(input.getOrDefault(var, var));
		return newList;
	}

	@Override
	public Expression visit(MinusExpression expression,
			Map<Variable, Variable> input) {
		return new MinusExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(NeqExpression expression,
			Map<Variable, Variable> input) {
		return new NeqExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(NotExpression expression,
			Map<Variable, Variable> input) {
		return new NotExpression(expression.accept(this, input));
	}

	@Override
	public Expression visit(OrExpression expression,
			Map<Variable, Variable> input) {
		return new OrExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(PlusExpression expression,
			Map<Variable, Variable> input) {
		return new PlusExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(PrecExpression expression,
			Map<Variable, Variable> input) {
		return new PrecExpression(expression.accept(this, input));
	}

	@Override
	public Expression visit(ProdExpression expression,
			Map<Variable, Variable> input) {
		return new ProdExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(StringLengthExpression expression,
			Map<Variable, Variable> input) {
		return new StringLengthExpression(expression.accept(this, input));
	}

	@Override
	public Expression visit(SkolemExpression expression,
			Map<Variable, Variable> input) {
		return new SkolemExpression(expression.operationName,expression.getOperandList().stream().map(x -> x.accept(this, input)).collect(Collectors.toList()));
	}

	@Override
	public Expression visit(StartsWithExpression expression,
			Map<Variable, Variable> input) {
		return new StartsWithExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(SubstringExpression expression,
			Map<Variable, Variable> input) {
		return new SubstringExpression(expression.firstOperand.accept(this, input),
	    expression.secondOperand.accept(this, input),
	    expression.thirdOperand.accept(this, input));
	}

	@Override
	public Expression visit(UnaryMinusExpression expression,
			Map<Variable, Variable> input) {
		return new UnaryMinusExpression(expression.operand.accept(this, input));
	}

	@Override
	public Expression visit(UnionExpression expression,
			Map<Variable, Variable> input) {
		return new UnionExpression(expression.lhsOperand.accept(this, input),expression.rhsOperand.accept(this, input));
	}

	@Override
	public Expression visit(Variable expression, Map<Variable, Variable> input) {
		return input.getOrDefault(expression, expression);
	}
	
}

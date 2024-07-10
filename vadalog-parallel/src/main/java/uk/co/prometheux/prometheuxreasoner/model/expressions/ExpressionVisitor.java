package uk.co.prometheux.prometheuxreasoner.model.expressions;

import uk.co.prometheux.prometheuxreasoner.model.Constant;
import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * A visitor interface that can be used to traverse an expression
 * 
 * @param <Result> The return type of the visiting method
 * @param <Input>  The argument type of the visiting method
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public interface ExpressionVisitor<Result, Input> {

	/**
	 * Visits an AndExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(AndExpression expression, Input input);

	/**
	 * Visits an ImplementationExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(ImplementationExpression expression, Input input);

	/**
	 * Visits an ConcatExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(ConcatExpression expression, Input input);

	/**
	 * Visits an Constant<?>
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(Constant<?> expression, Input input);

	/**
	 * Visits an ContainsExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(ContainsExpression expression, Input input);

	/**
	 * Visits an DivExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(DivExpression expression, Input input);

	/**
	 * Visits an EndsWithExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(EndsWithExpression expression, Input input);

	/**
	 * Visits an EqEqExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(EqEqExpression expression, Input input);

	/**
	 * Visits an GeExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(GeExpression expression, Input input);

	/**
	 * Visits an GtExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(GtExpression expression, Input input);

	/**
	 * Visits an IndexOfExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(IndexOfExpression expression, Input input);

	/**
	 * Visits an IntersectionExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(IntersectionExpression expression, Input input);

	/**
	 * Visits an LeExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(LeExpression expression, Input input);

	/**
	 * Visits an LtExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(LtExpression expression, Input input);

	/**
	 * Visits an AggregationExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
//	Result visit(AggregationExpression expression, Input input);

	/**
	 * Visits an MinusExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(MinusExpression expression, Input input);

	/**
	 * Visits an NeqExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(NeqExpression expression, Input input);

	/**
	 * Visits an NotExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(NotExpression expression, Input input);

	/**
	 * Visits an OrExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(OrExpression expression, Input input);

	/**
	 * Visits an PlusExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(PlusExpression expression, Input input);

	/**
	 * Visits an PrecExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(PrecExpression expression, Input input);

	/**
	 * Visits an ProdExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(ProdExpression expression, Input input);

	/**
	 * Visits an StringLengthExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(StringLengthExpression expression, Input input);

	/**
	 * Visits an SkolemExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(SkolemExpression expression, Input input);

	/**
	 * Visits an StartsWithExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(StartsWithExpression expression, Input input);

	/**
	 * Visits an SubstringExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(SubstringExpression expression, Input input);

	/**
	 * Visits an UnaryMinusExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(UnaryMinusExpression expression, Input input);

	/**
	 * Visits an UnionExpression
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(UnionExpression expression, Input input);

	/**
	 * Visits an Variable
	 * 
	 * @param expression is the expression to be visited
	 * @param input      the value passed to the visiting method
	 * @return the result of the visit
	 */
	Result visit(Variable expression, Input input);

}

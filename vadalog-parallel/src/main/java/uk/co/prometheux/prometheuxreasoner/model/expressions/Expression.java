package uk.co.prometheux.prometheuxreasoner.model.expressions;

import java.io.Serializable;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * It represents a tuple-wise arithmetic expression to be used in the body in
 * the RHS of a condition. It is a composite of other expressions. The base case
 * is that of a Term.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public abstract class Expression implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Returns the arity of the Expression
	 * 
	 * @return Returns the arity of the Expression
	 */
	public abstract int getArity();

	/**
	 * Returns the name of the expression's operation
	 * 
	 * @return the name of the expression's operation
	 */
	public abstract String getOperationName();

	/**
	 * It builds an Expression by cloning an existing one.
	 * 
	 * @param renameVariables determines whether the new clone should use fresh
	 *                        variables
	 */
	public abstract Expression clone(boolean renameVariables);

	/**
	 * It returns all the variables in the expression.
	 * 
	 * @return the set of variables
	 */
	public abstract Set<Variable> getAllVariables();

	/**
	 * Expressions implement the visitors design pattern
	 * 
	 * @param visitor  the type of the visitor
	 * @param input    the input passed during the visit
	 * @param <Result> the type of the result
	 * @param <Input>  the type of the input
	 * @return the result of the call to the visitor
	 */
	public abstract <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input);

}
package uk.co.prometheux.prometheuxreasoner.model;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;

import uk.co.prometheux.prometheuxreasoner.model.expressions.Expression;

/**
 * It represents a condition in the body of a Datalog rule.
 * A condition is a formula where the LHS is a variable and the RHS
 * is an expression. The two parts are connected by an assignment operator
 * or by a comparison operator (>,<,>=,<=,=,!=,in,!in). There is ambiguity
 * on the assignment, which is also a comparison. In case the variable
 * in the LHS appears in the body, the operator has to be interpreted as
 * comparison, assignment otherwise.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class Condition {

	private final Variable lhs;
	private final ComparisonOperatorsEnum compOp;
	private final Expression rhs;

	public Condition(Variable lhs, ComparisonOperatorsEnum compOp, Expression rhs) {
		super();
		this.lhs = lhs;
		this.compOp = compOp;
		this.rhs = rhs;
	}

	public Condition(Condition c) {
		this.lhs = c.getLhs().clone(false);
		this.rhs = c.getRhs().clone(false);
		this.compOp = c.getCompOp();
	}
	
	Condition(Condition c, boolean renameVariables) {
		this.lhs = c.getLhs().clone(renameVariables);
		this.rhs = c.getRhs().clone(renameVariables);
		this.compOp = c.getCompOp();
	}

	public Variable getLhs() {
		return lhs;
	}

	/**
	 * Returns an immutable set containing the variables in this condition
	 * @return the set of variables
	 */
	public Set<Variable> getAllVariables() {
		return Sets.union(Collections.singleton(lhs), getAllExpressionVariables());
	}

	/**
	 * It returns all the variables in the expression in this condition.
	 * @return the set of variables
	 */
	public Set<Variable> getAllExpressionVariables() {
		return this.rhs.getAllVariables();
	}

	/**
	 * It replaces varName with intoName
	 * @param varName the name of variable to replace
	 * @param intoName the new variable name
	 */
	public void renameVariableAs(String varName, String intoName) {
		this.getAllExpressionVariables().forEach(x -> x.renameIfEquals(varName, intoName));
		this.getLhs().renameIfEquals(varName, intoName);
	}

	public Expression getRhs() {
		return rhs;
	}

	public ComparisonOperatorsEnum getCompOp() {
		return compOp;
	}

	public String toString() {
		String space = "";
		if(this.compOp==ComparisonOperatorsEnum.IN||this.compOp==ComparisonOperatorsEnum.NOTIN)
			space = " ";
		return this.lhs.toString() + space + this.compOp.toString() + space + this.rhs.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((compOp == null) ? 0 : compOp.hashCode());
		result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
		result = prime * result + ((rhs == null) ? 0 : rhs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Condition that = (Condition) obj;
		return Objects.equals(this.compOp, that.compOp) &&
				Objects.equals(this.lhs, that.lhs) &&
				Objects.equals(this.rhs, that.rhs);
	}

	/**
	 * It returns the string version of rhs without the variables
	 * @return the partial toString of rhs
	 */
	public String toStringRhsWithoutVars() {
		String partString = this.getRhs().toString();
		Set<Variable> varsRhs = this.getAllExpressionVariables();
		for(Variable var : varsRhs){
			partString = partString.replaceAll(var.getName(), "");
		}
		return partString;
	}

}

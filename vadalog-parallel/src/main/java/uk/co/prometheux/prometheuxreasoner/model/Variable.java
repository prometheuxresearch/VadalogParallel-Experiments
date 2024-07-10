package uk.co.prometheux.prometheuxreasoner.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.expressions.ExpressionVisitor;

/**
 * This Class implements a Variable
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

public class Variable extends Term implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;

	public Variable(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getOperationName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * It clones the variable and applies a renaming
	 * 
	 * @param renameVariable whether the variable has to be renamed
	 * @return the new variable
	 */
	public Variable clone(boolean renameVariable) {
		if (renameVariable)
			return new Variable(this.name + "1");
		else
			return new Variable(this.name);
	}

	public Variable clone() {
		return this.clone(false);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof Variable) {
			return name.equals(((Variable) obj).name);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public Set<Variable> getAllVariables() {
		Set<Variable> vars = new HashSet<>();
		vars.add(this);
		return vars;
	}

	@Override
	public <Result, Input> Result accept(ExpressionVisitor<Result, Input> visitor, Input input) {
		return visitor.visit(this, input);
	}

	/**
	 * It renames this variable, if its name is varName, into intoName
	 * 
	 * @param varName  the name to search
	 * @param intoName the name to rename into
	 */
	void renameIfEquals(String varName, String intoName) {
		if (this.name.equals(varName))
			this.name = intoName;
	}
}
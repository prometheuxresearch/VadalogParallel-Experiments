package uk.co.prometheux.prometheuxreasoner.operators;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Condition;
import uk.co.prometheux.prometheuxreasoner.model.Term;
import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * It represents a unifier between two conditions, that is, the set of
 * substitutions for all the involved variables.
 *
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
class ConditionToConditionUnifier {

	private Condition c1;
	private Condition c2;

	Map<Variable, Term> mapping = new HashMap<Variable, Term>();

	ConditionToConditionUnifier(Condition c1, Condition c2) {
		super();
		this.c1 = c1;
		this.c2 = c2;
	}

	public Set<Variable> getVariables() {
		return this.mapping.keySet();
	}

	public Condition getC1() {
		return c1;
	}

	public void setC1(Condition c1) {
		this.c1 = c1;
	}

	public Condition getC2() {
		return c2;
	}

	public void setC2(Condition c2) {
		this.c2 = c2;
	}

	/**
	 * It adds a correspondence from a variable v of c1 or c2 to a term t.
	 * 
	 * @param v The variable.
	 * @param t The term.
	 */
	void addCorrespondence(Variable v, Term t) {

		Map<Variable, Term> newMap = new HashMap<Variable, Term>();
		/* if not other additions needed */
		boolean done = false;

		/*
		 * OPT: this could be optimized by implementing another map in the other
		 * direction
		 */
		for (Map.Entry<Variable, Term> varTerm : this.mapping.entrySet()) {
			/* v -> t (in : a -> v), add only : a -> t */
			if (varTerm.getValue().equals(v)) {
				newMap.put(varTerm.getKey(), t);
				/* v -> t (in: t->k) extra add only v->k */
			}
			if (varTerm.getKey().equals(t)) {
				newMap.put(v, varTerm.getValue());
				done = true;
			}
		}

		/* add the inferred substitutions */
		/* to avoid concurrent modification exception */
		newMap.entrySet().forEach(x -> this.mapping.put(x.getKey(), x.getValue()));

		if (!done)
			this.mapping.put(v, t);
	}

	/**
	 * It returns the current correspondence for a specific Variable. Null if none
	 * is defined.
	 * 
	 * @param v The variable for which to look for the correspondence
	 * @return The correspondence for the input variable
	 */
	Term getCorrespondenceByVariable(Variable v) {
		return mapping.get(v);
	}

	public String toString() {
		return this.mapping.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mapping == null) ? 0 : mapping.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConditionToConditionUnifier other = (ConditionToConditionUnifier) obj;
		if (mapping == null) {
			if (other.mapping != null)
				return false;
		} else if (!mapping.equals(other.mapping))
			return false;
		return true;
	}

}

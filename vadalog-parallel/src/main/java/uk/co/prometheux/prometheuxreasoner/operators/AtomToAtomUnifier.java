package uk.co.prometheux.prometheuxreasoner.operators;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Condition;
import uk.co.prometheux.prometheuxreasoner.model.Constant;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Term;
import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * It represents a unifier between two atoms, that is, the set of substitutions
 * for all the involved variables.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class AtomToAtomUnifier {

	private Atom a1;
	private Atom a2;

	Map<Variable, Term> mapping = new HashMap<Variable, Term>();

	AtomToAtomUnifier(Atom a1, Atom a2) {
		super();
		this.a1 = a1;
		this.a2 = a2;
	}

	public Set<Variable> getVariables() {
		return this.mapping.keySet();
	}

	public Atom getA1() {
		return a1;
	}

	public void setA1(Atom a1) {
		this.a1 = a1;
	}

	public Atom getA2() {
		return a2;
	}

	public void setA2(Atom a2) {
		this.a2 = a2;
	}

	/**
	 * It adds a correspondence from a variable v of a1 or a2 to a term t.
	 * 
	 * @param v The variable.
	 * @param t The term.
	 */
	public void addCorrespondence(Variable v, Term t) {

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
	 * It applies this unification as a mapping, replacing in an atom all the
	 * variables x, with y, whenever x to y occurs in the unifier.
	 * 
	 * @param r the rule to apply the mapping to
	 * @return a new rule with the applied mapping
	 */
	Rule applyAsAmapping(Rule r) {
		Rule r1 = new Rule(r);
		r1.setSingleHead(this.applyAsAmapping(r1.getSingleHead()));
		r1.clearBody();
		for (Literal l : r.getBody()) {
			Literal l1 = new Literal(l);
			l1.setAtom(this.applyAsAmapping(l.getAtom()));
			r1.getBody().add(l1);
		}

		return r1;
	}

	/**
	 * It applies this unification as a mapping, replacing in an atom or a condition
	 * all the variables x, with y, whenever x to y occurs in the unifier.
	 * 
	 * @param r the rule to apply the mapping to
	 * @return a new rule with the applied mapping
	 */
	public Rule applyAsMappingWithConditions(Rule r) {
		Rule r1 = new Rule(r);
		r1.setSingleHead(this.applyAsAmapping(r1.getSingleHead()));
		r1.clearBody();
		for (Literal l : r.getBody()) {
			Literal l1 = new Literal(l);
			l1.setAtom(this.applyAsAmapping(l.getAtom()));
			r1.getBody().add(l1);
		}
		for (Condition c : r.getConditions()) {
			Condition c1 = this.applyAsMapping(c);
			r1.getConditions().remove(c);
			r1.getConditions().add(c1);
		}

		return r1;
	}

	/**
	 * It applies this unification as a mapping, replacing in a condition all the
	 * variables x, with y, whenever x to y occurs in the unifier.
	 * 
	 * @param c the condition to modify
	 * @return a copy of the conditions with the replacements applied
	 */
	private Condition applyAsMapping(Condition c) {
		Condition c2 = new Condition(c);
		/* we rename the lhs */
		Variable lhs = c2.getLhs();
		Term nlhs = this.mapping.get(lhs);
		if (lhs != null && nlhs != null)
			c2.renameVariableAs(lhs.getName(), nlhs.getName());
		/* we rename the rhs */
		for (Variable v : c2.getAllExpressionVariables()) {
			Term nv = this.mapping.get(v);
			if (nv != null)
				c2.renameVariableAs(v.getName(), this.mapping.get(v).getName());
		}
		return c2;
	}

	/**
	 * It applies this unification as a mapping, replacing in an atom all the
	 * variables x, with y, whenever x to y occurs in the unifier.
	 * 
	 * @param a the atom whose variables must be replaced
	 * @return an atom with the replacements applied
	 */
	public Atom applyAsAmapping(Atom a) {
		Atom a1 = new Atom(a.getSimpleName());
		a1.setSkolemAtom(a.isSkolemAtom());

		/* if it is a Skolem atom, the calculated variable must be */
		/* mapped as well. */
		if (a.isSkolemAtom()) {
			a1.setCalculatedVariable((Variable) (this.getCorrespondenceByVariable(a.getCalculatedVariable())));
			if (a1.getCalculatedVariable() == null)
				a1.setCalculatedVariable(a.getCalculatedVariable());

		}

		for (Term t : a.getArguments()) {
			/* constants are not transformed */
			if (t instanceof Constant)
				a1.getArguments().add(t);
			/* variables are mapped */
			else if (t instanceof Variable) {
				Term t2 = this.getCorrespondenceByVariable((Variable) t);
				/* if there is a mapping, use it */
				if (t2 != null)
					a1.getArguments().add(t2);
				/* otherwise, simply ignore */
				else
					a1.getArguments().add(t);
			}
		}

		return a1;
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
		AtomToAtomUnifier other = (AtomToAtomUnifier) obj;
		if (mapping == null) {
			if (other.mapping != null)
				return false;
		} else if (!mapping.equals(other.mapping))
			return false;
		return true;
	}

}
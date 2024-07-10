package uk.co.prometheux.prometheuxreasoner.operators;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Condition;
import uk.co.prometheux.prometheuxreasoner.model.Constant;
import uk.co.prometheux.prometheuxreasoner.model.Term;
import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * This operator takes as input a pair of Atoms or Conditions and returns a
 * homomorphism that represents the most general unifier among them. It returns
 * null if none exists.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class Unification {

	private Atom a1;

	private Condition c1;

	Unification(Atom a1) {
		this.a1 = a1;
	}

	Unification(Condition c1) {
		this.c1 = c1;
	}

	/**
	 * It unifies a1 and a2 and returns the respective AtomToAtomHomomorphism
	 * 
	 * @param a2 The second Atom to consider in the unification
	 * @return it returns the homomorphism representing the unification
	 * @throws UnificationException when no unifiers exist
	 */
	public AtomToAtomUnifier with(Atom a2) throws UnificationException {

		/* we initialize the basic homomorphism */
		/* which we then modify during the matching */
		AtomToAtomUnifier h = new AtomToAtomUnifier(a1, a2);
		List<Term> termList1 = a1.getArguments();
		List<Term> termList2 = a2.getArguments();

		if (termList1.size() != termList2.size())
			throw new UnificationException("Atoms " + a1 + " and " + a2 + " do not unify. Different arity.");

		/*
		 * we handle the case of Skolem atoms: we only check their basic name, without
		 * the calculated variable e.g.: f_0_Var_4(Var_1) and f_0_Var_2(Var_1), we do
		 * not check f_0_Var_i but only f_0
		 */
		if (a1.isSkolemAtom() && a2.isSkolemAtom()) {
			String partNameA1 = a1.getName().replace(a1.getCalculatedVariable().toString(), "");
			String partNameA2 = a2.getName().replace(a2.getCalculatedVariable().toString(), "");
			if (!partNameA1.equals(partNameA2))
				throw new UnificationException("Atoms " + a1 + "and " + a2 + "do not unify. Different predicates");
//			/* then we add the calculated variables as new terms to unify */
		} else {
			if (!h.getA1().getName().equals(a2.getName()))
				throw new UnificationException("Atoms " + a1 + "and " + a2 + "do not unify. Different predicates");
		}

		Iterator<Term> it1 = termList1.iterator();
		Iterator<Term> it2 = termList2.iterator();

		Term t1, t2;

		/* unify all the terms */
		while (it1.hasNext()) {
			t1 = it1.next();
			t2 = it2.next();
			unifyTerms(h, t1, t2);
		}

		/* simplify the transitive substitutions */

		return h;
	}

	/**
	 * It returns whether a1 unifies with a2
	 * 
	 * @param a2 the atom to check against
	 * @return whether the two unify
	 */
	public boolean does(Atom a2) {
		try {
			this.with(a2);
		} catch (UnificationException e) {
			return false;
		}
		return true;
	}

	/**
	 * This method enriches an AtomToAtomUnifier, by unifying two more terms
	 * 
	 * @param h  the AtomToAtomUnifier to enrich
	 * @param t1 The first term
	 * @param t2 The second term
	 * @throws UnificationException
	 */
	private void unifyTerms(AtomToAtomUnifier h, Term t1, Term t2) throws UnificationException {
		Term tCorr;
		if (t1 instanceof Variable) {
			/* if the first term is a variable and the second is a constant */
			if (t2 instanceof Constant) {
				/* if there is no correspondence, set v1 -> c */
				tCorr = h.getCorrespondenceByVariable((Variable) t1);
				if (tCorr == null)
					h.addCorrespondence((Variable) t1, t2);
				/*
				 * else, if the correspondence is different, but assigned to a constant, they
				 * don't unify
				 */
				else if (!tCorr.equals(t2) && tCorr instanceof Constant)
					throw new UnificationException("Atoms do not unify on: <" + t1 + "," + t2 + ">");
				/* else, if the correspondence is different, but assigned to a variable */
				/* (v1 -> tCorr), then unify (tCorr, c) */
				else if (!tCorr.equals(t2) && tCorr instanceof Variable)
					this.unifyTerms(h, tCorr, t2);
				/* else if the first term is a variable the second term is a variable */
			} else if (t2 instanceof Variable) {
				/* if there is no correspondence, set v1 -> v2 */
				tCorr = h.getCorrespondenceByVariable((Variable) t1);
				if (tCorr == null) {
					if (!t1.equals(t2)) // avoid X=X
						h.addCorrespondence((Variable) t1, t2);
					/* else, if the correspondence is different but to a variable */
					/* (v1 -> v3), set v3 -> v2 */
				} else if (!tCorr.equals(t2) && tCorr instanceof Variable)
					/* add the correspondence and update the closure */
					this.unifyTerms(h, tCorr, t2);
				/* else, if the correspondence is different, but to a Constant v1 -> c */
				else if (!tCorr.equals(t2) && tCorr instanceof Constant)
					/* unify (c, v2) with the constant */
					this.unifyTerms(h, tCorr, t2);
			}
		}
		/*
		 * else, t1 is a constant, then simply invoke the method swapping the parameters
		 */
		else if (t1 instanceof Constant && t2 instanceof Variable)
			this.unifyTerms(h, t2, t1);
		/* in the case of constants to constants, just verify */
		else {
			if (!t1.equals(t2))
				throw new UnificationException("Atoms do not unify on: <" + t1 + "," + t2 + ">");
		}

	}

	/**
	 * It unifies c1 and c2 and returns the respective ConditionToConditionUnifier
	 * 
	 * @param c2 The second Condition to consider in the unification
	 * @return it returns the homomorphism representing the unification
	 * @throws UnificationException when no unifiers exist
	 */
	ConditionToConditionUnifier with(Condition c2) throws UnificationException {
		/* we initialize the basic homomorphism */
		/* which we then modify during the matching */
		ConditionToConditionUnifier h = new ConditionToConditionUnifier(c1, c2);

		if (!h.getC1().getCompOp().equals(c2.getCompOp()))
			throw new UnificationException("Conditions " + c1 + "and " + c2 + "do not unify. Different operators");

		Set<Variable> varSet1 = c1.getRhs().getAllVariables();
		Set<Variable> varSet2 = c2.getRhs().getAllVariables();
		Iterator<Variable> it1 = varSet1.iterator();
		Iterator<Variable> it2 = varSet2.iterator();
		Variable s1, s2;

		/* unify lhs and rhs terms respectively */
		unifyTerms(h, c1.getLhs(), c2.getLhs());
		/* unify all the terms in rhs */
		while (it1.hasNext()) {
			s1 = it1.next();
			s2 = it2.next();
			unifyTerms(h, s1, s2);
		}
		/*
		 * check if the rest of the rhs is the same, otherwise conditions cannot unify
		 */
		if (!h.getC1().toStringRhsWithoutVars().equals(c2.toStringRhsWithoutVars()))
			throw new UnificationException("Conditions " + c1 + "and " + c2 + "do not unify. Different rhs");

		return h;
	}

	/**
	 * This method enriches a ConditionToConditionUnifier, by unifying two more
	 * terms
	 * 
	 * @param h  the ConditionToConditionUnifier to enrich
	 * @param t1 The first term
	 * @param t2 The second term
	 * @throws UnificationException
	 */
	private void unifyTerms(ConditionToConditionUnifier h, Term t1, Term t2) throws UnificationException {
		Term tCorr;
		if (t1 instanceof Variable) {
			/* if the first term is a variable and the second is a constant */
			if (t2 instanceof Constant) {
				/* if there is no correspondence, set v1 -> c */
				tCorr = h.getCorrespondenceByVariable((Variable) t1);
				if (tCorr == null)
					h.addCorrespondence((Variable) t1, t2);
				/*
				 * else, if the correspondence is different, but assigned to a constant, they
				 * don't unify
				 */
				else if (!tCorr.equals(t2) && tCorr instanceof Constant)
					throw new UnificationException("Conditions do not unify on: <" + t1 + "," + t2 + ">");
				/* else, if the correspondence is different, but assigned to a variable */
				/* (v1 -> tCorr), then unify (tCorr, c) */
				else if (!tCorr.equals(t2) && tCorr instanceof Variable)
					this.unifyTerms(h, tCorr, t2);
				/* else if the first term is a variable the second term is a variable */
			} else if (t2 instanceof Variable) {
				/* if there is no correspondence, set v1 -> v2 */
				tCorr = h.getCorrespondenceByVariable((Variable) t1);
				if (tCorr == null) {
					if (!t1.equals(t2)) // avoid X=X
						h.addCorrespondence((Variable) t1, t2);
					/* else, if the correspondence is different but to a variable */
					/* (v1 -> v3), set v3 -> v2 */
				} else if (!tCorr.equals(t2) && tCorr instanceof Variable)
					/* add the correspondence and update the closure */
					this.unifyTerms(h, tCorr, t2);
				/* else, if the correspondence is different, but to a Constant v1 -> c */
				else if (!tCorr.equals(t2) && tCorr instanceof Constant)
					/* unify (c, v2) with the constant */
					this.unifyTerms(h, tCorr, t2);
			}
		}
		/*
		 * else, t1 is a constant, then simply invoke the method swapping the parameters
		 */
		else if (t1 instanceof Constant && t2 instanceof Variable)
			this.unifyTerms(h, t2, t1);
		/* in the case of constants to constants, just verify */
		else {
			if (!t1.equals(t2))
				throw new UnificationException("Conditions do not unify on: <" + t1 + "," + t2 + ">");
		}

	}

}

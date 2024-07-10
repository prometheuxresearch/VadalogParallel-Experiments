package uk.co.prometheux.prometheuxreasoner.operators;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Condition;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Term;

/**
 * An object to check whether a rule is homomorphic to another given one.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class HomomorphismChecker {

	private Rule rule;
	private AtomToAtomUnifier unifierForRule;
	private Set<String> alsoToAvoid;
	private Atom headAfterHomomorphism = null;

	/**
	 * A comparator to sort Literals according to their name
	 */
	private class LiteralComparator implements Comparator<Literal> {
		@Override
		public int compare(Literal o1, Literal o2) {
			if (o1.equals(o2))
				return 0;
			int compNames = o1.getAtom().getName().compareTo(o2.getAtom().getName());
			// if(compNames==0){
			// return
			// o1.getAtom().getArguments().toString().compareTo(o2.getAtom().getArguments().toString());
			// }
			return compNames;
		}
	}

	/**
	 * A comparator to sort Conditions according to the name of the argument
	 */
	private class ConditionComparator implements Comparator<Condition> {
		@Override
		public int compare(Condition o1, Condition o2) {
			if (o1.equals(o2))
				return 0;
			/* check if lhs names are Var_i, */
			/* in which case adopt a different approach to compare */
			String o1Name = o1.getLhs().getName();
			String o2Name = o1.getLhs().getName();
			int compNames;
			/* this is due to the fact that simply comparing string names */
			/* does not allow to properly compare the index values i,j in Var_i, Var_j */
			if (o1Name.length() > 4 && o2Name.length() > 4 && o1Name.substring(0, 4).equals("Var_")
					&& o2Name.substring(0, 4).equals("Var_")) {
				int o1Index = Integer.parseInt(o1Name.substring(4));
				int o2Index = Integer.parseInt(o2Name.substring(4));
				compNames = o1Index - o2Index;
			} else
				compNames = o1.getLhs().getName().compareTo(o2.getLhs().getName());
			/* if same lhs, compare string rhs without considering the variables */
			if (compNames == 0) {
				return o1.toStringRhsWithoutVars().compareTo(o2.toStringRhsWithoutVars());
			}
			return compNames;
		}
	}

	public HomomorphismChecker(Rule r) {
		this.rule = r;
		this.alsoToAvoid = new HashSet<String>();
	}

	HomomorphismChecker(Rule r, Set<String> alsoToAvoid) {
		this.rule = r;
		this.alsoToAvoid = alsoToAvoid;
	}

	/**
	 * It returns whether rule is homomorphic to r. We say that the two rules are
	 * homomorphic if they have the same number of atoms in the body, and they
	 * globally unify. Once unified, also the conditions must be the same.
	 * 
	 * @param r the rule to check
	 * 
	 * @return whether the homomorphism holds
	 */
	public boolean to(Rule r) {
		/* we create a clone of the rule */
		Rule rule = new Rule(this.rule);

		/* first of all, we avoid random homonym symbols */
		Set<String> namesToAvoid = new HashSet<>();
		for (Atom a : r.getAllAtoms()) {
			for (Term arg : a.getArguments()) {
				namesToAvoid.add(arg.toString());
			}
		}
		namesToAvoid.addAll(alsoToAvoid);
		rule.renameVariables(namesToAvoid);

		List<Literal> ruleBody = rule.getBody();
		List<Literal> rBody = r.getBody();
		Rule r2 = rule;
		List<Condition> ruleConds = rule.getConditions();
		List<Condition> rConds = r.getConditions();

		/* if the two bodies have a different number */
		/* of literals, they do not unify. */
		if (ruleBody.size() != rBody.size())
			return false;
		/* if one is dom* and the other is not */
		if (rule.isDomStar() != r.isDomStar())
			return false;
		/* if the two rules have a different number */
		/* of conditions, they do not unify */
		if (ruleConds.size() != rConds.size())
			return false;

		/* sort the body */
		ruleBody.sort(new LiteralComparator());
		rBody.sort(new LiteralComparator());

		int atomPos = 0;

		AtomOperatorDecorator aod;
		AtomToAtomUnifier aau;
		ConjunctsToConjunctsUnifier ctu = new ConjunctsToConjunctsUnifier();
		Literal l1, l2;

		try {
			do {
				/* get the literal at position atomPos */
				l1 = ruleBody.get(atomPos);
				l2 = rBody.get(atomPos);
				if (l1.isPositive() != l2.isPositive() || l1.getAtom().isSkolemAtom() != l2.getAtom().isSkolemAtom())
					return false;

				if (!l1.equals(l2)) {
					aod = new AtomOperatorDecorator(l1.getAtom());
					aau = aod.unify().with(l2.getAtom());
					/* if the extension with the new correspondences */
					/* is not possible, the homomorphism does not hold */
					if (!ctu.extendWithUnifier(aau))
						return false;
				}

				rule = r2;
				ruleBody = r2.getBody();
				atomPos++;

			} while (atomPos < ruleBody.size());
		} catch (UnificationException e) {
			return false;
		}

		/* then we check if conditions unify */
		/* sort the conditions */
		ruleConds.sort(new ConditionComparator());
		rConds.sort(new ConditionComparator());

		int condPos = 0;

		ConditionOperatorDecorator cod;
		ConditionToConditionUnifier ccu;
		Condition c1, c2;

		if (!ruleConds.isEmpty()) {
			try {
				do {
					/* get the condition at position condPos */
					c1 = ruleConds.get(condPos);
					c2 = rConds.get(condPos);

					if (!c1.equals(c2)) {
						cod = new ConditionOperatorDecorator(c1);
						ccu = cod.unify().with(c2);
						/* if the extension with the new correspondences */
						/* is not possible, the homomorphism does not hold */
						if (!ctu.checkWithUnifier(ccu))
							return false;
					}

					rule = r2;
					ruleConds = r2.getConditions();
					condPos++;

				} while (condPos < ruleConds.size());
			} catch (UnificationException e) {
				return false;
			}
		}

		/* now it is sufficient to see if the heads unify */
		Atom h = rule.getSingleHead();
		Atom head = r.getSingleHead();

		aod = new AtomOperatorDecorator(h);
		try {
			aau = aod.unify().with(head);
		} catch (UnificationException e) {
			return false;
		}

		return ctu.extendWithUnifier(aau);

	}

	/**
	 * It returns whether body of rule is homomorphic to body of r. We say that the
	 * two bodies are homomorphic if they have the same number of atoms in the body,
	 * with the same predicate names and they globally unify. Once unified, also the
	 * conditions must be the same.
	 * 
	 * @param r the rule to check
	 * 
	 * @return whether the homomorphic holds
	 */
	public boolean toBody(Rule r) {
		/* we create a clone of the rule */
		Rule rule = new Rule(this.rule);

		/* first of all, we avoid random homonym symbols */
		Set<String> namesToAvoid = new HashSet<>();
		for (Atom a : r.getAllAtoms()) {
			for (Term arg : a.getArguments()) {
				namesToAvoid.add(arg.toString());
			}
		}
		namesToAvoid.addAll(alsoToAvoid);
		rule.renameVariables(namesToAvoid);

		List<Literal> ruleBody = rule.getBody();
		List<Literal> rBody = r.getBody();
		Rule r2 = rule;
		List<Condition> ruleConds = rule.getConditions();
		List<Condition> rConds = r.getConditions();

		/* if the two bodies have a different number */
		/* of literals, they do not unify. */
		if (ruleBody.size() != rBody.size())
			return false;
		/* if one is dom* and the other is not */
		if (rule.isDomStar() != r.isDomStar())
			return false;
		/* if the two rules have a different number */
		/* of conditions, they do not unify */
		if (ruleConds.size() != rConds.size())
			return false;

		/* sort the body */
		ruleBody.sort(new LiteralComparator());
		rBody.sort(new LiteralComparator());

		int atomPos = 0;

		AtomOperatorDecorator aod;
		AtomToAtomUnifier aau;
		ConjunctsToConjunctsUnifier ctu = new ConjunctsToConjunctsUnifier();
		Literal l1, l2;

		try {
			do {
				/* get the literal at position atomPos */
				l1 = ruleBody.get(atomPos);
				l2 = rBody.get(atomPos);

				if (l1.isPositive() != l2.isPositive() || l1.getAtom().isSkolemAtom() != l2.getAtom().isSkolemAtom())
					return false;

				if (!l1.equals(l2)) {
					aod = new AtomOperatorDecorator(l1.getAtom());
					aau = aod.unify().with(l2.getAtom());
					/* if the extension with the new correspondences */
					/* is not possible, the isomorphism does not hold */
					if (!ctu.extendWithUnifier(aau)) {
						return false;
					}
				}

				rule = r2;
				ruleBody = r2.getBody();
				atomPos++;

			} while (atomPos < ruleBody.size());
		} catch (UnificationException e) {
			return false;
		}

		/* then we check if conditions unify */
		/* sort the conditions */
		ruleConds.sort(new ConditionComparator());
		rConds.sort(new ConditionComparator());

		int condPos = 0;

		ConditionOperatorDecorator cod;
		ConditionToConditionUnifier ccu;
		Condition c1, c2;

		if (!ruleConds.isEmpty()) {
			try {
				do {
					/* get the condition at position condPos */
					c1 = ruleConds.get(condPos);
					c2 = rConds.get(condPos);

					if (!c1.equals(c2)) {
						cod = new ConditionOperatorDecorator(c1);
						ccu = cod.unify().with(c2);
						/* if the extension with the new correspondences */
						/* is not possible, the homomorphism does not hold */
						if (!ctu.checkWithUnifier(ccu))
							return false;
					}

					rule = r2;
					ruleConds = r2.getConditions();
					condPos++;

				} while (condPos < ruleConds.size());
			} catch (UnificationException e) {
				return false;
			}
		}

		ctu.createGeneralUnifier();
		unifierForRule = ctu.getGeneralUnifier();

		headAfterHomomorphism = rule.getSingleHead();

		return true;

	}

	public AtomToAtomUnifier getGeneralUnifier() {
		return unifierForRule;
	}

	boolean toBodyWithConditions(Rule r) {

		/* we create a clone of the rule */
		Rule rule = new Rule(this.rule);

		/* first of all, we avoid random homonym symbols */
		Set<String> namesToAvoid = new HashSet<>();
		for (Atom a : r.getAllAtoms()) {
			for (Term arg : a.getArguments()) {
				namesToAvoid.add(arg.toString());
			}
		}
		namesToAvoid.addAll(alsoToAvoid);
		rule.renameVariables(namesToAvoid);

		List<Literal> ruleBody = rule.getBody();
		List<Literal> rBody = r.getBody();
		Rule r2 = rule;
		List<Condition> ruleConds = rule.getConditions();
		List<Condition> rConds = r.getConditions();

		/* if the two bodies have a different number */
		/* of literals, they do not unify. */
		if (ruleBody.size() != rBody.size())
			return false;
		/* if one is dom* and the other is not */
		if (rule.isDomStar() != r.isDomStar())
			return false;

		/* sort the body */
		ruleBody.sort(new LiteralComparator());
		rBody.sort(new LiteralComparator());

		int atomPos = 0;

		AtomOperatorDecorator aod;
		AtomToAtomUnifier aau;
		ConjunctsToConjunctsUnifier ctu = new ConjunctsToConjunctsUnifier();
		Literal l1, l2;

		try {
			do {
				/* get the literal at position atomPos */
				l1 = ruleBody.get(atomPos);
				l2 = rBody.get(atomPos);

				if (l1.isPositive() != l2.isPositive() || l1.getAtom().isSkolemAtom() != l2.getAtom().isSkolemAtom())
					// || !l1.getAtom().getName().equals(l2.getAtom().getName()))
					// //<-------------------------------------------
					return false;

				if (!l1.equals(l2)) {
					aod = new AtomOperatorDecorator(l1.getAtom());
					aau = aod.unify().with(l2.getAtom());
					/* if the extension with the new correspondences */
					/* is not possible, the isomorphism does not hold */
					if (!ctu.extendWithUnifier(aau)) {
						return false;
					}
				}

				rule = r2;
				ruleBody = r2.getBody();
				atomPos++;

			} while (atomPos < ruleBody.size());
		} catch (UnificationException e) {
			return false;
		}

		/* then we check if conditions unify */
		/* sort the conditions */
		ruleConds.sort(new ConditionComparator());
		rConds.sort(new ConditionComparator());

		rConds.subList(ruleConds.size(), rConds.size()).clear();

		int condPos = 0;

		ConditionOperatorDecorator cod;
		ConditionToConditionUnifier ccu;
		Condition c1, c2;

		if (!ruleConds.isEmpty()) {
			try {
				do {
					/* get the condition at position condPos */
					c1 = ruleConds.get(condPos);
					c2 = rConds.get(condPos);

					if (!c1.equals(c2)) {
						cod = new ConditionOperatorDecorator(c1);
						ccu = cod.unify().with(c2);
						/* if the extension with the new correspondences */
						/* is not possible, the homomorphism does not hold */
						if (!ctu.extendWithConditionUnifier(ccu))
							return false;
					}

					rule = r2;
					ruleConds = r2.getConditions();
					condPos++;

				} while (condPos < ruleConds.size());
			} catch (UnificationException e) {
				return false;
			}
		}

		ctu.createGeneralUnifier();
		AtomToAtomUnifier bodyToBodyUnifier = ctu.getGeneralUnifier();

		Rule mappedRule = bodyToBodyUnifier.applyAsMappingWithConditions(rule);

		List<Condition> mappedRuleConditions = mappedRule.getConditions().stream().collect(Collectors.toList());
		List<Condition> partialConditions = r.getConditions().stream().collect(Collectors.toList());

		for (Condition condition : mappedRuleConditions) {
			if (!partialConditions.contains(condition)) {
				return false;
			}
		}

		unifierForRule = ctu.getGeneralUnifier();

		return true;

	}

	/**
	 * @return the head of this.rule after homomorphism is applied.
	 */
	public Atom getHeadAfterHomomorphism() {
		return headAfterHomomorphism;
	}
}
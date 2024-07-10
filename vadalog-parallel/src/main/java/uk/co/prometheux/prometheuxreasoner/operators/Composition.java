package uk.co.prometheux.prometheuxreasoner.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import uk.co.prometheux.prometheuxreasoner.common.IdGenerator;
import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Term;
import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * This operator takes as input a pair of rules R1, R2, such that the head of R1
 * appears in R2 and returns a new rule that is the composition of the first
 * two. This operator only composes the two rules, but does not handle the
 * generation of further rule needed to make the composition correct (e.g.
 * duplication of the operands, introduction of dom*)
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class Composition {

	/* the first rule in the composition */
	private Rule r1;
	/* the position in the second operand to compose */
	/* with the head of the first one. */
	private int composePosition = -1;
	/* whether the composition has to be done only with the first */
	/* matching atom of the second operand. */
	private boolean withFirstOnly = false;
	/* list of literals added by the compose */
	private List<Literal> delta = new ArrayList<>();

	/**
	 * It builds an instance of Composition from the first operand
	 * 
	 * @param r1 the rule to compose
	 */
	Composition(Rule r1) {
		this.r1 = r1;
	}

	/**
	 * Sets the second operand of the composition and returns the result, without
	 * explicit Skolem Atoms.
	 * 
	 * @param r2 the second operand of the composition
	 * @return the result of the composition
	 */
	public Rule with(Rule r2) {
		return this.with(r2, false);
	}

	/**
	 * It explicitly sets the specific position in the second operand that will be
	 * composed with the first one. In absence of this specification, the
	 * composition will be applied to all the literals with which the head of the
	 * first operand matches. If a position is specified, it is implied that the
	 * atom at that position unifies with the head of the second operand, otherwise
	 * an exception is thrown.
	 * 
	 * @param position the position in the first operand to be composed
	 */
	public Composition atPosition(int position) {
		this.composePosition = position;
		return this;
	}

	/**
	 * Sets the second operand of the composition and returns the result
	 * 
	 * @param r2                  the second operand of the composition
	 * @param explicitSkolemAtoms whether to generate explicit SkolemAtoms
	 * @return the result of the composition
	 */
	public Rule with(Rule r2, boolean explicitSkolemAtoms) {

		/* we start from a clone of r1, where we rename all the variables */
		Rule r1c = new Rule(r1, true);

		Set<String> namesToAvoid = r2.getVariables().stream().map(x -> x.getName()).collect(Collectors.toSet());

		/* also the names of the calculated variables must be avoided */
		namesToAvoid.addAll(r2.getLiterals().stream().filter(x -> x.getAtom().isSkolemAtom())
				.map(x -> x.getAtom().getCalculatedVariable().getName()).collect(Collectors.toSet()));

		/* we must be sure that the new names in r1c do not conflict with */
		/* any name in r2 */

		r1c.renameVariables(namesToAvoid);

		/* we start from a clone of r2, which will be the result and we modify */
		Rule r2c = new Rule(r2);
		r2c.clearBody();

		/* the head to unify */
		Atom head = r1c.getSingleHead();

		boolean isComposable = false;
		String nonComposeMessage = "Head and body do not unify.";
		AtomToAtomUnifier aau = null;

		/* the position of the considered literal */
		int curPos = -1;

		/* and find all the positive atoms of r2 that unify with the head of r1 */
		for (Literal l : r2.getPositiveLiterals()) {
			/* we move to the next literal */
			curPos++;
			AtomOperatorDecorator aod = new AtomOperatorDecorator(l.getAtom());
			try {
				/* If we set a composePosition and we are at it, then compose. */
				/* Also if we did not set a composePosition, we try to compose anyway. */
				/* In both cases, if the composition is not feasible, we throw an exception. */
				if ((this.composePosition > -1 && this.composePosition == curPos)
						|| (this.composePosition == -1) && (!this.withFirstOnly || !isComposable)) { // plus, either we
																										// have to
																										// compose on
																										// all the atoms
																										// or it has not
																										// been composed
																										// yet
					aau = aod.unify().with(head);
					/* and replace them with the body of r1c */
					r2c.getLiterals().addAll(r1c.getLiterals());
					/* replace also conditions */
					r2c.getConditions().addAll(r1c.getConditions());
					/* shall we apply the unification? */
					r2c = aau.applyAsAmapping(r2c);
					isComposable = true;

					/* save added literals inside the delta */
					for (Literal ll : r1c.getLiterals()) {
						delta.add(new Literal(aau.applyAsAmapping(ll.getAtom()), ll.isPositive()));
					}

				} else {
					/* if we are not at the established position */
					/* we keep the original literal. */
					r2c.getLiterals().add(l);
				}
			} catch (UnificationException e) {
				r2c.getLiterals().add(l);
				// this atom does not unify, just keep the original
			}
		}

		if (!isComposable) {
			throw new CompositionException("Cannot compose, if there is no match with the head");
		}
		/* now we must check that aau does not impose the equivalence of two */
		/* existentially quantified variables, which would cause */
		/* impossibility to compose, as the Skolem ranges are disjoint. */
		Set<Variable> exVariables = r1c.getExistentiallyQuantifiedVariables();
		for (Variable var : exVariables)
			if (exVariables.contains(aau.getCorrespondenceByVariable(var))) {
				isComposable = false;
				nonComposeMessage = "Two distinct existentially quantified varaibles (" + var + ", "
						+ aau.getCorrespondenceByVariable(var) + ") cannot be equated.";
			}
		// If there exists no AtomToAtomUnifier, then the computation cannot continue.
		// Abort.
		if (!isComposable)
			throw new CompositionException("Rules " + r1 + " does not compose with " + r2 + " " + nonComposeMessage);

		/* now, if the substitution was ok, we have to unify the variables */
		/* that have been renamed, according to the unification aau we have */
		/* calculated. */
		r2c = aau.applyAsMappingWithConditions(r2c);

		if (explicitSkolemAtoms) {
			/* Here we handle the existential quantifications in r1. */
			/* If a variable y of r2 is mapped into x of r1 by the aau */
			/* and x is existentially quantified, then we must add the atom */
			/* f_i_x(x_1, ... x_n) to the body of the composition, */
			/* where x_1, ... x_n are the variables in the body of the cause */
			/* and f_i depends on r1. */
			Set<Variable> bodyVars = r2.getBodyVariables();
			for (Variable v : bodyVars) {
				/*
				 * in case there has been some binding with an existentially quantified variable
				 */
				if (exVariables.contains(aau.getCorrespondenceByVariable(v))) {
					/* we create the functional atom */
					String atomName = "f_" + IdGenerator.getRuleFunction(r1);

					Atom skolemAtom = new Atom(atomName);
					skolemAtom.setSkolemAtom(true);
					skolemAtom.setCalculatedVariable((Variable) aau.getCorrespondenceByVariable(v));

					/* the variables of this atom, i.e., operands */
					/* of the Skolem function. */
					List<Term> depVars = r1c.getBodyVariablesList().stream().map(x -> (Term) x)
							.collect(Collectors.toList());
					skolemAtom.setArguments(depVars);
					r2c.getBody().add(new Literal(skolemAtom, true));

				}
			}
		}

		return r2c;
	}

	/**
	 * Returns the list of added atoms during the composition.
	 * 
	 * @return delta, the list of added atoms during the composition
	 */
	public List<Literal> getDelta() {
		return delta;
	}
}
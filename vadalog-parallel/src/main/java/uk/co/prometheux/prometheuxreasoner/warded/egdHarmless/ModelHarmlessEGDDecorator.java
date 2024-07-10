package uk.co.prometheux.prometheuxreasoner.warded.egdHarmless;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Constant;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Term;
import uk.co.prometheux.prometheuxreasoner.model.Variable;
import uk.co.prometheux.prometheuxreasoner.warded.ModelWardedDecorator;
import uk.co.prometheux.prometheuxreasoner.warded.Position;
import uk.co.prometheux.prometheuxreasoner.warded.RuleWardedDecorator;

/**
 * A decorator to support EGDs functionalities. It checks the sufficient
 * condition for Harmless EGDs.
 * 
 * A set of EGDs is harmless if for each TGD in the program the following
 * conditions hold: -Every variable that appears in a tainted position appears
 * only once in the body of every TGD; -There are no ground tainted positions.
 * 
 * A position pos is tainted if: - the model contains an EGD where a variable X
 * appears in the position pos in the body, X is harmful and appears in an
 * equality atom in the head; - pos is the position of the variable X in the
 * head and X appears in a tainted position in the body (forward propagation);
 * -pos is the position of the variable X in the body and X appears in a tainted
 * position in the head (backward propagation).
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 *
 */
public class ModelHarmlessEGDDecorator {

	private Model model;
	/* To compute harmful variables and affected positions */
	private ModelWardedDecorator modelWardedDecorator;

	public ModelHarmlessEGDDecorator(Model model) {
		this.model = model;
		this.modelWardedDecorator = new ModelWardedDecorator(model);
	}

	/**
	 * It returns the Model used to instantiate this decorator
	 * 
	 * @return the original Model
	 */
	public Model getModel() {
		return this.model;
	}

	/**
	 * It returns the set of tainted positions of the program. A position pos is
	 * tainted if: - the model contains an EGD where a variable X appears in the
	 * position pos in the body, X is harmful and appears in an equality atom in the
	 * head; - pos is the position of the variable X in the head and X appears in a
	 * tainted position in the body (forward propagation); - pos is the position of
	 * the variable X in the body and X appears in a tainted position in the head
	 * (backward propagation).
	 * 
	 * @return The set of tainted positions.
	 */

	public Set<Position> getTaintedPositions() {
		Set<Position> taintedPositions = new HashSet<>();
		/* For each rule we find the harmful variables */
		Map<Rule, Set<Variable>> harmfulVariablesByRule = this.modelWardedDecorator.getHarmfulVariablesByRule(false);

		/* We inspect the EGDs first, detecting the tainted positions */
		for (Rule r : this.model.getEGDs()) {
			/* We find the harmful variables in the rule */
			Set<Variable> harmfulVariables = harmfulVariablesByRule.get(r);
			RuleWardedDecorator rwd = new RuleWardedDecorator(r);
			for (Variable hv : harmfulVariables) {
				/*
				 * If it is contained in the equality atom in the head we find a tainted
				 * variable
				 */
				if (r.getHeadVariablesAsSet().contains(hv)) {
					taintedPositions.addAll(rwd.getBodyPositionsByVariable(hv));
				}
			}

		}
		/*
		 * The algorithm that finds the tainted positions in the TGDs works in the
		 * following way:
		 * 
		 * it iterates over all the TGDs and checks if there is a forward or a backward
		 * propagation of a tainted variable already computed. If the latter iteration
		 * (over all the TGDs) has not produced any tainted position, a fixpoint is
		 * reached and the algorithm stops otherwise it will produce another iteration
		 * over all the TGDs
		 */

		/*
		 * A variable that denotes if a fixpoint is reached in discovering tainted
		 * positions
		 */
		boolean anythingChanged = true;

		/*
		 * If we have new tainted positions in the latter iteration we need to inspect
		 * all the rules again
		 */
		while (anythingChanged) {
			anythingChanged = false;

			/* Inspects all the TDGs */
			for (Rule r : this.model.getTDGs()) {
				RuleWardedDecorator rwd = new RuleWardedDecorator(r);
				/* Looks for the backward propagation first (from the head to the body) */
				for (Atom singleHead : r.getHead()) {
					Set<Position> newTaintedPositions = new HashSet<>();
					for (Position taintedPosition : taintedPositions) {
						/* If we have a match between the tainted atoms computed so far */
						if (taintedPosition.getAtom().equals(singleHead.getName())) {
							/* We find the tainted variable */
							Term t = singleHead.getArguments().get(taintedPosition.getPosition());
							if (t instanceof Variable) {
								Variable taintedVariable = (Variable) t;
								/* We add all the positions in the body */
								for (Position pos : rwd.getBodyPositionsByVariable(taintedVariable)) {
									/* Checks whether it was computed this pos in another iteration */
									if (!taintedPositions.contains(pos)) {
										newTaintedPositions.add(pos);
										/* We need to iterate over the tgds again */
										anythingChanged = true;
									}

								}
							}

						}
					}
					taintedPositions.addAll(newTaintedPositions);

				}
				/* Looks for the forward propagation */
				for (Atom bodyAtom : r.getBodyAtomsList()) {
					Set<Position> newTaintedPositions = new HashSet<>();
					for (Position taintedPosition : taintedPositions) {
						/* If we have a match between the tainted atoms computed so far */
						if (taintedPosition.getAtom().equals(bodyAtom.getName())) {
							/* We find the tainted variable */
							Term t = bodyAtom.getArguments().get(taintedPosition.getPosition());
							if (t instanceof Variable) {
								Variable taintedVariable = (Variable) t;
								/* We add all the positions in the head */
								for (Position pos : rwd.getHeadPositionByVariable(taintedVariable)) {
									/* Checks whether it was computed this pos in another iteration */
									if (!taintedPositions.contains(pos)) {
										newTaintedPositions.add(pos);
										/* We need to iterate over the tgds again */
										anythingChanged = true;
									}

								}
							}

						}
					}
					taintedPositions.addAll(newTaintedPositions);

				}
			}
		}
		return taintedPositions;

	}

	/**
	 * It returns the set of tainted variables grouped by rule
	 * 
	 * @param egdsOnly whether to return only the EGDs
	 * @return the set of tainted variables grouped by rule
	 */
	public Map<Rule, Set<Variable>> getTaintedVariablesByRule(boolean egdsOnly) {
		Map<Rule, Set<Variable>> taintedVariablesByRule = new HashMap<>();

		Set<Position> taintedPositions = getTaintedPositions();
		for (Rule r : this.model.getRules()) {
			if (egdsOnly && r.isEGD()) {
				taintedVariablesByRule.put(r, new HashSet<>());
				RuleWardedDecorator rwd = new RuleWardedDecorator(r);
				for (Atom a : r.getBodyAtomsList()) {
					for (Position p : taintedPositions) {
						if (a.getName().equals(p.getAtom())) {
							rwd.getTermAtPosition(p).stream().filter(t -> t instanceof Variable).map(t -> (Variable) t)
									.forEach(v -> taintedVariablesByRule.get(r).add(v));

						}
					}
				}
			} else {
				if (!egdsOnly) {
					taintedVariablesByRule.put(r, new HashSet<>());
					RuleWardedDecorator rwd = new RuleWardedDecorator(r);
					for (Atom a : r.getBodyAtomsList()) {
						for (Position p : taintedPositions) {
							if (a.getName().equals(p.getAtom())) {
								rwd.getTermAtPosition(p).stream().filter(t -> t instanceof Variable)
										.map(t -> (Variable) t).forEach(v -> taintedVariablesByRule.get(r).add(v));

							}
						}
					}

				}
			}
		}

		return taintedVariablesByRule;
	}

	/**
	 * It checks if the EGDs in the program satisfy harmless sufficient condition.
	 * If so we can say that the set of EGDs is harmless. A set of EGDs is harmless
	 * if for each TGD in the program the following conditions hold: -Every variable
	 * that appears in a tainted position appears only once in the body; -There is
	 * no ground tainted position.
	 * 
	 * @return true if the sufficient condition for harmless EGDs holds otherwise
	 *         false.
	 */
	public boolean checkEGDHarmlessSufficientCondition() {

		/* It computes the tainted positions */
		Set<Position> taintedPositions = this.getTaintedPositions();

		/*
		 * We iterate over the TGD rules to check that every variable that appears in a
		 * tainted position appears only once in the body
		 */
		for (Rule r : this.model.getTDGs()) {
			RuleWardedDecorator rwd = new RuleWardedDecorator(r);
			/* Iterates over the variable in the body of the rules */
			for (Variable v : r.getBodyVariables()) {
				/* If v appears in a tainted position in the body of the rule */
				boolean isInATaintedPosition = false;
				/* it gets all the positions of v in the body */
				Set<Position> varpos = rwd.getBodyPositionsByVariable(v);
				for (Position p : varpos) {
					/* Checks if there is any match */
					if (taintedPositions.contains(p)) {
						isInATaintedPosition = true;
						break;
					}
				}
				/*
				 * If the variable is in a tainted position computes the occurrences in the body
				 * and if they are more than one we fail
				 */
				if (isInATaintedPosition && (getVariableOccurrencesInTheBodyOfARule(v, r) > 1)) {
					return false;
				}

			}
		}

		/* We check the the second condition */
		return checkGroundTaintedPositions(taintedPositions);
	}

	/**
	 * It rewrites the EGD rules. e.g. X=Y :- p(X,Z),p(Y,Z). becomes egdBody_1(X,Y)
	 * :- p(X,Z),p(Y,Z). X=Y :- egdBody_1(X,Y). Warning: the rewritten model it is
	 * not Warded most of the time
	 * 
	 * @return a clone model with the EGDs rewritten
	 */
	public Model egdRulesRewriting() {
		Model m = new Model(this.model);
		m.getRules().clear();
		Integer index = 0;
		for (Rule r : this.model.getRules()) {
			if (r.isEGD()) {
				index++;
				List<Rule> rules = new ArrayList<>();
				Atom newHead = new Atom("egdBody_" + index, new ArrayList<>(r.getHeadVariablesAsSet()));
				Literal newBody = new Literal(newHead, true);
				rules.add(new Rule(newHead, r.getBody(), r.getConditions(), r.getAnnotations()));
				rules.add(new Rule(r.getHead(), Collections.singletonList(newBody), new ArrayList<>(),
						r.getAnnotations()));
				m.getRules().addAll(rules);
			} else {
				m.getRules().add(r);
			}
		}
		return m;
	}

	/**
	 * It removes the EGDs from the model.
	 * 
	 * @return the list of EGDs that have been removed.
	 */
	public List<Rule> removeEGDs() {
		List<Rule> egds = this.model.getEGDs();
		for (Rule r : egds) {
			this.model.removeRule(r);
		}
		return egds;
	}

	/**
	 * It computes the occurrences of a variable in the body of a rule
	 * 
	 * @param v is a variable
	 * @param r is a rule
	 * @return the number of times v appears in the body of r
	 */
	private int getVariableOccurrencesInTheBodyOfARule(Variable v, Rule r) {
		int occurrences = 0;
		for (Atom a : r.getBodyAtomsList()) {
			for (Variable var : a.getVariableList()) {
				if (var.equals(v)) {
					occurrences++;
				}
			}
		}
		return occurrences;
	}

	/**
	 * It verifies if there aren't tainted positions in the ground atoms, i.e. there
	 * aren'conditions over tainted position or tainted variables since it could
	 * trigger a TGDs.
	 * 
	 * @return true if there aren't ground tainted positions false otherwise
	 */
	private boolean checkGroundTaintedPositions(Set<Position> taintedPositions) {
		for (Rule r : this.model.getTDGs()) {
			for (Atom singleHead : r.getHead()) {
				for (Position taintedPosition : taintedPositions) {
					if (taintedPosition.getAtom().equals(singleHead.getName())) {
						Term t = singleHead.getArguments().get(taintedPosition.getPosition());
						/* We look if there is any constants in the tainted positions */
						if (t instanceof Constant<?>) {
							return false;
						} else {
							/* We look if there is any conditions */
							Variable taintedVariable = (Variable) t;
							if (r.getConditionsVariables().contains(taintedVariable))
								return false;
						}
					}
				}
			}
			for (Atom bodyAtom : r.getBodyAtomsList()) {
				for (Position taintedPosition : taintedPositions) {
					/* If we have a match between the tainted atoms computed so far */
					if (taintedPosition.getAtom().equals(bodyAtom.getName())) {
						/* We find the tainted variable */
						Term t = bodyAtom.getArguments().get(taintedPosition.getPosition());
						/* We look if there is any constants in the tainted positions */
						if (t instanceof Constant<?>) {
							return false;
						} else {
							/* We look if there is any conditions over the tainted variables */
							Variable taintedVariable = (Variable) t;
							if (r.getConditionsVariables().contains(taintedVariable))
								return false;
						}
					}
				}
			}
		}
		return true;
	}

}

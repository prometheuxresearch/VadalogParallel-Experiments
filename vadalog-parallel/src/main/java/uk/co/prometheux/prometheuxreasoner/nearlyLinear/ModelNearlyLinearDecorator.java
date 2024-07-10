package uk.co.prometheux.prometheuxreasoner.nearlyLinear;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.prometheux.prometheuxreasoner.common.IdGenerator;
import uk.co.prometheux.prometheuxreasoner.datalog.RuleDatalogDecorator;
import uk.co.prometheux.prometheuxreasoner.model.ComparisonOperatorsEnum;
import uk.co.prometheux.prometheuxreasoner.model.Condition;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Variable;
import uk.co.prometheux.prometheuxreasoner.operators.AtomOperatorDecorator;
import uk.co.prometheux.prometheuxreasoner.operators.ModelOperatorDecorator;
import uk.co.prometheux.prometheuxreasoner.operators.RuleOperatorDecorator;
import uk.co.prometheux.prometheuxreasoner.warded.ModelWardedDecorator;
import uk.co.prometheux.prometheuxreasoner.warded.Position;
import uk.co.prometheux.prometheuxreasoner.warded.RuleWardedDecorator;

/**
 * A decorator with functionalities for nearly-linear programs.
 * 
 * A nearly-linear program is such that, every rule:
 * 
 * - is either a linear rule - is a Datalog rule, which means (all safe
 * variables, no existential quantifiers)
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class ModelNearlyLinearDecorator {

	private Model model;

	private Logger log = LoggerFactory.getLogger(ModelNearlyLinearDecorator.class);

	public ModelNearlyLinearDecorator(Model model) {
		this.model = model;
	}

	/**
	 * It returns whether this program is nearly-linear or not.
	 * 
	 * @return whether the program is nearly-linear or not.
	 */
	public boolean isNearlyLinear() {
		log.info("BEGIN checking program for nearly-linearity");
		ModelWardedDecorator mwd = new ModelWardedDecorator(model);

		/* we get the set of the harmful variables of the program */
		Map<Rule, Set<Variable>> harmful = mwd.getHarmfulVariablesByRule(false);

		for (Rule r : this.model.getRules()) {
			/* if the rule is not linear */
			if (!r.isLinear()) {
				RuleDatalogDecorator rdd = new RuleDatalogDecorator(r);
				/* if it is Datalog */
				if (rdd.isDatalog()) {
					/* then it should not contain harmful variables */
					if (harmful.get(r).size() > 0) {
						if (log.isInfoEnabled()) {
							log.info("Rule " + r + " is not linear and contains the following harmful variables: "
									+ harmful.get(r));
							log.info("Nearly linearity test failed.");
							log.info("END checking program for nearly-linearity.");
						}
						return false;
					}
				} else // the rule is neither linear nor datalog, therefore the program is not
						// nearly-linear
					return false;
			}
		}
		log.info("Nearly linearity test ok.");
		log.info("END checking program for nearly-linearity.");

		return true;
	}

	/**
	 * It applies the "SimplifyAffectedJoins" algorithm. For each Datalog rule in
	 * the program, all the variables are expected to be harmless. For a variable to
	 * be harmless, it is sufficient for it to occur in a non-affected position. We
	 * refer to the cases in which a variables occurs in multiple atoms.
	 * 
	 * - If it always occurs in non-affected positions, then the joins will include
	 * only ground values.
	 * 
	 * - If it occurs both in affected and non-affected positions, then the joins
	 * may include null values. This method addresses this last case and simplifies
	 * the affected-non affected joins with the following technique.
	 * 
	 * It detects the affected atoms (the atom with the affected variable in join)
	 * and composes back all of them with the "causes", that is, the rule that
	 * caused them to be affected.
	 * 
	 * @return a new Model with the simplifications applied
	 */
	public Model simplifyAffectedJoins() {
		/*
		 * For the affected atoms we know the cause of affectedness, since we memorize
		 * it
		 * 
		 * 1. the atom appears in the RHS of a linear rule with an existential
		 * quantification on the join variable.
		 * 
		 * 2. the atom appears in the RHS of a linear rule, and the cause of
		 * affectedness is in the LHS (there is a dangerous variable).
		 *
		 * 3. both causes (1) and (2)
		 * 
		 * in all the cases we:
		 * 
		 * 1. compose back and insert the generated rule 2. rename the affected literal
		 * L in the original rule to L' 3. rename all the rules heads to L into L'
		 * (except for the linear with which we composed) 4. add L' -> L 5. add L dom*
		 * -> L'
		 * 
		 * - the base case is that there are no affected positions in all the Datalog
		 * rules.
		 * 
		 */

		log.info("BEGIN simplifying nearly-linear affected joins");

		/* let's start from an empty model */
		Model m = new Model(this.model);
		/* we individuate the affected positions */
		ModelWardedDecorator mwd = new ModelWardedDecorator(m);

		/* for each Rule we get all the causes of affectedness */
		Set<Position> affectedPositions = mwd.getAffectedPositions();
		Map<Rule, Set<Rule>> joinAffectedRulesAndCauses = this.getJoinAffectednessCausesByRule(m, affectedPositions);
		if (log.isDebugEnabled())
			log.debug("Causes of affectedness: " + joinAffectedRulesAndCauses);

		/* while there are affected positions with joins */
		while (joinAffectedRulesAndCauses.size() > 0) {

			/* for each affected rules, we compose back along all the causes */
			for (Map.Entry<Rule, Set<Rule>> ruleCauses : joinAffectedRulesAndCauses.entrySet()) {
				Rule rDatalog = ruleCauses.getKey(); // the Datalog rule to simplify
				// if it is the first cause for this rule
				boolean firstCause = true;

				/* for all the causes */
				for (Rule cause : ruleCauses.getValue()) {

					/* we compose all of them */
					RuleOperatorDecorator rod = new RuleOperatorDecorator(cause);

					/* we try to compose */
					if (log.isDebugEnabled())
						log.debug("Trying to compose: " + cause + " and " + rDatalog);
					Rule rDatalogSimpl = rod.compose().with(rDatalog);

					/* if now the composition is successful, here we should have */
					/* a simplified rule, maybe still affected. */

					/* now, we add this new rule to the program */
					m.getRules().add(rDatalogSimpl);
					if (log.isDebugEnabled())
						log.debug("Rules correctly composed into: " + rDatalogSimpl);

					/* as for the original one we need to rename the composed atom, */
					/* to detach it from the danger and add various */
					/* rewriting rules, so first we detect where we composed */
					/* this needs to be done only for the first cause of every */
					/* single rule */
					if (firstCause) {
						detachFromDanger(m, ruleCauses.getValue(), rDatalog, cause,
								rDatalog.getLiterals().indexOf(this.getCompositionLiteral(rDatalog, cause)), null);

						/* one cause has been analyzed. The next one */
						/* will not be the first. */
						firstCause = false;
					}
				}

				/* after all the simplifications */
				/* we calculate again the affected positions */
				affectedPositions = mwd.getAffectedPositions();
				if (log.isDebugEnabled())
					log.debug("Affected positions: " + affectedPositions);
				/* for each Rule we get all the causes of affectedness */
				joinAffectedRulesAndCauses = this.getJoinAffectednessCausesByRule(m, affectedPositions);
				if (log.isDebugEnabled())
					log.debug("Causes of affectedness: " + joinAffectedRulesAndCauses);
			}
		}
		;

		/* for the extract */
		ModelOperatorDecorator mod = new ModelOperatorDecorator(m);
		log.debug("Let's extract the model");
		/* do an extract of all the model */
		m = mod.extract().getModel();

		log.info("END simplifying nearly-linear affected joins");
		return m;
	}

	/**
	 * It detaches rule RDatalog from the danger caused by cause.
	 * 
	 * @param m             the program
	 * @param ruleCauses    the causes
	 * @param rDatalog      the rule
	 * @param cause         the cause
	 * @param litPos        the position of the literal to detach
	 * @param correspHAtoms the map with correspondences <originalAtom, hatom>
	 * 
	 * @return correspHAtoms updated
	 */
	public Map<String, String> detachFromDanger(Model m, Set<Rule> ruleCauses, Rule rDatalog, Rule cause, int litPos,
			Map<String, String> correspHAtoms) {
		if (log.isDebugEnabled())
			log.debug("Detaching from danger rule " + rDatalog);
		Literal unificationLiteral = rDatalog.getLiterals().get(litPos);
		AtomOperatorDecorator aod = new AtomOperatorDecorator(unificationLiteral.getAtom());

		/* and detach from the danger by renaming */
		RuleNearlyLinearDecorator rnld = new RuleNearlyLinearDecorator(rDatalog);
		String newName = IdGenerator.getNewAtomSymbol("hatom_");
		Rule rDatalogRenamed = rnld.detachFromDanger(unificationLiteral, newName);

		/* replace the rule with the renamed one */
		m.getRules().remove(rDatalog);
		m.getRules().add(rDatalogRenamed);

		/* find all the rules but the ones in the causes whose head */
		/* unifies with L and rename the head to newL */
		for (Rule r : m.getRules().stream().filter(x -> aod.unify().does(x.getSingleHead()))
				.collect(Collectors.toSet())) {

			/* we rename the head of all the rules whose head unifies */
			/* except for the ones that are causes, which will be composed */
			if (!ruleCauses.contains(r)) {
				Rule newRule = new Rule(r);
				newRule.getSingleHead().setName(newName);
				m.getRules().remove(r);
				m.getRules().add(newRule);
			}
		}

		/* add newL -> L */
		/* create the body */
		Literal bodyLiteral = new Literal(unificationLiteral);
		bodyLiteral.getAtom().setName(newName);
		/* create the head */
		Literal headLiteral = new Literal(unificationLiteral);
		/* and the rule */
		Rule newLtoL = new Rule(headLiteral.getAtom(), bodyLiteral, null);
		m.getRules().add(newLtoL);

		/* update correspHAtoms */
		if (correspHAtoms != null)
			correspHAtoms.put(unificationLiteral.getAtom().getSimpleName(), bodyLiteral.getAtom().getSimpleName());

		/* add dom* L -> new L to read from input (then we prune it if not needed) */
		bodyLiteral = new Literal(unificationLiteral);
		headLiteral = new Literal(unificationLiteral);
		headLiteral.getAtom().setName(newName);
		Rule domLToNewL = new Rule(headLiteral.getAtom(), bodyLiteral, null);
		domLToNewL.setDomStar(true);
		m.getRules().add(domLToNewL);

		return correspHAtoms;

	}

	/**
	 * It returns the literal of rDatalog that unifies with cause
	 * 
	 * @param rDatalog the Rule to simplify
	 * @param cause    the second operand of the composition
	 * @return the literal of rDatalog that unifies with cause
	 */
	private Literal getCompositionLiteral(Rule rDatalog, Rule cause) {
		Literal unificationLiteral = null;
		for (Literal l : rDatalog.getLiterals()) {
			AtomOperatorDecorator aod = new AtomOperatorDecorator(l.getAtom());
			/* if the literal unifies with the head it means that we composed along it */
			/*
			 * (could be optimized, since we already know what unifies, when the composition
			 */
			/* is done. */
			if (aod.unify().does(cause.getSingleHead()))
				unificationLiteral = l;
		}
		return unificationLiteral;
	}

	/**
	 * It calculates and returns the for each Rule, the set of causes that hamper
	 * their joins, introducing an affected variables. In Datalog an affected
	 * position can occur either in case of join or in case or repeated variable in
	 * the same atom. Here we are interested only in case of joins. The other cases
	 * are not to be simplified here.
	 * 
	 * @param the               model to operate upon
	 * @param affectedPositions the affected positions
	 * @return a map from rules to sets of affected literals.
	 */
	private Map<Rule, Set<Rule>> getJoinAffectednessCausesByRule(Model m, Set<Position> affectedPosition) {
		return this.getAffectednessCausesByRule(m, affectedPosition, true);
	}

	/**
	 * It calculates and returns for each Rule, the set of affected variables. It is
	 * possible to limit the result to the affected variables appearing in joins
	 * only.
	 * 
	 * @param the               model to operate upon
	 * @param affectedPositions the affected positions
	 * @param if                we are only interested in affected positions in
	 *                          joins or Skolem conditions
	 * @param if                we are applying the method for a HH join
	 *                          simplification
	 * @return a map from rules to sets of affected literals.
	 */
	public Map<Rule, Set<Rule>> getAffectednessCausesByRule(Model m, Set<Position> affectedPositions,
			boolean joinsOnly) {

		Map<Rule, Set<Rule>> affectedRulesAndCauses = new HashMap<>();

		/*
		 * we find all the non-linear (hence Datalog) rules having atoms with variables
		 */
		/* in the affected positions that is, affected atoms. */

		/* While in Datalog an affected position can occur only in case of join */
		/*
		 * with a non-affected position, in the intermediate rewriting steps it can be
		 * no the case.
		 */
		/* Therefore we collect only the causes for affectedness in joins. */

		for (Rule r : m.getRules().stream().filter(x -> !x.isLinear()).collect(Collectors.toList())) {

			RuleWardedDecorator rwd = new RuleWardedDecorator(r);

			Set<Position> rulePositions = null;

			if (joinsOnly) {
				/*
				 * we need to know which affected literal positions appear in the rule in joins
				 * or in Skolem conditions
				 */
				rulePositions = affectedPositions.stream()
						.filter(x -> rwd.getAllBodyVariablePositionsWithJoins().contains(x))
						.collect(Collectors.toSet());

				/* we add the affected positions corresponding to variables */
				/* calculated in Skolem atoms as they are somehow joins. */
				rulePositions.addAll(affectedPositions.stream()
						.filter(x -> rwd.getAllBodyVariablePositionsWithSkolemCalculatedVariables().contains(x))
						.collect(Collectors.toSet()));

			} else {
				/* we need to know which affected positions appear in the rule */
				rulePositions = affectedPositions.stream().filter(x -> rwd.getAllBodyVariablePositions().contains(x))
						.collect(Collectors.toSet());
			}

			/* from the rule positions we get the causes */
			Set<Rule> causes = new HashSet<Rule>();
			for (Position p : rulePositions)
				causes.addAll(p.getCauses());

			/* and return them */
			if (causes.size() > 0)
				affectedRulesAndCauses.put(r, causes);

		}

		return affectedRulesAndCauses;
	}

	/**
	 * evaluateCalculatedVariables transform the model with conditions evaluated on
	 * calculated variables, in equal nearly linear model.
	 * 
	 * @return The new model.
	 */
	public Model evaluateCalculatedVariables() {
		Model m = new Model(this.model);
		for (Rule r : this.model.getRules()) {
			if (this.checkCondition(r)) {
				RuleNearlyLinearDecorator rnld = new RuleNearlyLinearDecorator(r);
				m.getRules().addAll(rnld.evaluateCalculatedVariables());
				m.getRules().remove(r);
			}
		}
		return m;
	}

	/**
	 * checkCondition check if the rule r contains two conditions, one with EQ, and
	 * one with another operator.
	 * 
	 * @param r: Current rule
	 * @return boolean
	 */
	private boolean checkCondition(Rule r) {
		for (Condition c : r.getConditions()) {
			if (c.getCompOp().equals(ComparisonOperatorsEnum.EQ)) {
				for (Condition c2 : r.getConditions()) {
					if (!(c2.getCompOp().equals(ComparisonOperatorsEnum.EQ)) && c.getLhs().equals(c2.getLhs())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
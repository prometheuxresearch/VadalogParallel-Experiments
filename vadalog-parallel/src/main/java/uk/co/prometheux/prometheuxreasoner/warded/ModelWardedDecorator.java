package uk.co.prometheux.prometheuxreasoner.warded;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Term;
import uk.co.prometheux.prometheuxreasoner.model.Variable;
import uk.co.prometheux.prometheuxreasoner.operators.AtomOperatorDecorator;
import uk.co.prometheux.prometheuxreasoner.precedenceGraph.Edge;
import uk.co.prometheux.prometheuxreasoner.precedenceGraph.Node;
import uk.co.prometheux.prometheuxreasoner.precedenceGraph.PrecedenceGraph;
import uk.co.prometheux.prometheuxreasoner.warded.ModelWardedDecorator.XWardedModelAnalysisResult.XWardedModelAnalysisResultStatus;
import uk.co.prometheux.prometheuxreasoner.warded.RuleWardedDecorator.XWardedRuleAnalysisResult;
import uk.co.prometheux.prometheuxreasoner.warded.RuleWardedDecorator.XWardedRuleAnalysisResult.XWardedRuleAnalysisResultStatus;

/**
 * A decorator with functionalities for a Warded program.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class ModelWardedDecorator {

	private Logger log = LoggerFactory.getLogger(ModelWardedDecorator.class);

	private Model model;

	public ModelWardedDecorator(Model model) {
		this.model = model;
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
	 * It returns the set of affected positions of the program. A position pos of
	 * the program is affected if: - there is an existentially quantified variable
	 * in some rule at that position; - there is some rule for which a variable X
	 * always appears in an affected position and X appears in the head, then the
	 * corresponding position in the head is affected as well.
	 * 
	 * @return The set of affected positions.
	 */
	public Set<Position> getAffectedPositions() {
		/* first we generate the positions for the existentially quantified variables */
		List<Rule> rules = model.getRules();
		/* the affected positions to return */
		Set<Position> affectedPositions = new HashSet<>();
		/* the body variables */
		Set<Variable> bodyVariables;

		int position;
		/*
		 * first, for all the rules we find the affected positions due to existential
		 * quantification
		 */
		for (Rule r : rules) {
			Set<Variable> existentiallyQuantifierVariables = model.isUsingRelaxedWardedness()
					? r.getExistentiallyQuantifiedVariables_Unsafe()
					: r.getExistentiallyQuantifiedVariables();
			/* create an affected position for each existentially quantified variable */
			Atom head = r.getSingleHead();
			/* let's start the count again for the head of this rule */
			position = 0;
			for (Term t : r.getSingleHead().getArguments()) {
				if (existentiallyQuantifierVariables.contains(t))
					this.mergeAffectedPositions(affectedPositions, new Position(head, position, r));

				position++;
			}
		}

		RuleWardedDecorator rwd;
		Set<Position> bodyPositionByVariable;

		/* then, for each rule, we find those where a variable appears */
		/* in the body always in an affected position. */
		/*
		 * Then, if it appears in the head as well and the rule is not dom(*), we have a
		 * new affected position.
		 */
		/* This is done until we stop finding new affected positions */
		boolean anythingChanged;
		Set<Position> newPos;

		do {
			anythingChanged = false;
			for (Rule r : rules) {
				rwd = new RuleWardedDecorator(r);

				/* for each distinct variable of the body get all the positions */
				bodyVariables = r.getBodyPositiveVariables();
				for (Variable v : bodyVariables) {
					/* and save the variables that appear only in an */
					/* affected position, that is, we remove those that */
					/* appear in some non-affected position */
					bodyPositionByVariable = rwd.getPositiveBodyPositionsByVariable(v);
					bodyPositionByVariable.removeAll(affectedPositions);
					/* if the variable only appears in the body in affected */
					/* positions, then all the positions in the head are affected */
					/* positions and are added to the set to be returned */
					if (bodyPositionByVariable.isEmpty() && !r.isDomStar()) {
						newPos = new AtomWardedDecorator(r.getSingleHead()).getPositionsByVariable(v);
						/* set the cause for them */
						newPos.forEach(x -> x.addCause(r));
						/* if the affected positions do not already contain */
						/* all the new positions */
						if (!checkIfPresent(affectedPositions, newPos)) {
							anythingChanged = true;
							this.mergeManyAffectedPositions(affectedPositions, newPos);
						}
					}
				}
			}
		} while (anythingChanged);
		return affectedPositions;
	}

	/**
	 * It returns whether the current set of affected positions contains all the new
	 * affected positions by checking the causes too.
	 * 
	 * @param affPos
	 * @param newPos
	 * 
	 * @return if affPos does not contain newPos entirely
	 */
	private boolean checkIfPresent(Set<Position> affPos, Set<Position> newPos) {
		boolean alreadyPresent = true;
		Iterator<Position> itNewPos = newPos.iterator();

		/* for each new affected position found */
		while (itNewPos.hasNext() && alreadyPresent) {
			Position search = itNewPos.next();
			Iterator<Position> itAffPos = affPos.iterator();
			boolean found = false;
			/* check whether is already present in affPos */
			while (itAffPos.hasNext() && !found) {
				Position in = itAffPos.next();
				/* check if also the causes for affectedness correspond */
				if (in.equals(search) && in.getCauses().containsAll(search.getCauses()))
					found = true;
			}
			/* if a Position in newPos is not found in affPos */
			if (!found)
				alreadyPresent = false;
		}
		return alreadyPresent;
	}

	/**
	 * It merges two sets of affected positions. If the position is already present,
	 * then it merges the causes. Else, it simply adds the position.
	 * 
	 * @param affectedPositions
	 * @param newPositions
	 */
	private void mergeManyAffectedPositions(Set<Position> affectedPositions, Set<Position> newPositions) {
		for (Position p : newPositions) {
			if (affectedPositions.contains(p))
				affectedPositions.stream().filter(x -> x.equals(p)).forEach(x -> x.addCauses(p.getCauses()));
			else
				affectedPositions.add(p);
		}
	}

	/**
	 * It merges a set of affected positions and a new one. If the position is
	 * already present, then it merges the causes. Else it simply adds the position.
	 * 
	 * @param affectedPositions the set of affected positions to be merged
	 * @param p                 the new affected position
	 */
	private void mergeAffectedPositions(Set<Position> affectedPositions, Position p) {
		HashSet<Position> newPositions = new HashSet<>();
		newPositions.add(p);
		this.mergeManyAffectedPositions(affectedPositions, newPositions);
	}

	/**
	 * It returns all the Positions where a variable x appears in any Rule.
	 * 
	 * @param x the variable to be looked up
	 * @return the set of the positions
	 */
	public Set<Position> getAllPositivePositionsByVariable(Variable x) {

		Set<Position> allPositions = new HashSet<>();

		for (Rule r : this.model.getRules()) {
			RuleWardedDecorator rwd = new RuleWardedDecorator(r);
			allPositions.addAll(rwd.getAllPositivePositionsByVariable(x));
		}

		return allPositions;
	}

	/**
	 * It returns all the body Positions where a variable x appears in any Rule
	 * positive literal.
	 * 
	 * @param x the variable to be looked up
	 * @return the set of the positions
	 */
	public Set<Position> getAllPositiveBodyPositionsByVariable(Variable x) {

		Set<Position> allPositions = new HashSet<>();

		for (Rule r : this.model.getRules()) {
			RuleWardedDecorator rwd = new RuleWardedDecorator(r);
			allPositions.addAll(rwd.getPositiveBodyPositionsByVariable(x));
		}

		return allPositions;
	}

	/**
	 * It returns all the body Positions where a variable x appears in any Rule
	 * literal.
	 * 
	 * @param x the variable to be looked up
	 * @return the set of the positions
	 */
	public Set<Position> getAllBodyPositionsByVariable(Variable x) {

		Set<Position> allPositions = new HashSet<>();

		for (Rule r : this.model.getRules()) {
			RuleWardedDecorator rwd = new RuleWardedDecorator(r);
			allPositions.addAll(rwd.getBodyPositionsByVariable(x));
		}

		return allPositions;
	}

	/**
	 * Returns the set of variables that are harmless in a rule, which means that
	 * they occur at least once in such a rule in a non-affected position.
	 * 
	 * @return the set of the harmless variables
	 */
	public Map<Rule, Set<Variable>> getHarmlessVariablesByRule() {
		/* first we calculate the set of affected positions */
		Set<Position> affectedPos = this.getAffectedPositions();
		Set<Position> varPos;
		Map<Rule, Set<Variable>> harmlessVars = new HashMap<>();

		/* to store the harmless variables to return */
		Map<Rule, Set<Variable>> harmlessVarsToReturn = new HashMap<>();

		/* for each rule, we add all the variables */
		for (Rule r : this.model.getRules()) {
			Set<Variable> vars = r.getBodyVariables(true);
			harmlessVars.put(r, vars);
			/* initializes the output */
			harmlessVarsToReturn.put(r, new HashSet<>());
		}

		/* then for each rule, we remove those that appear */
		/* only in an affected position */
		for (Rule r : this.model.getRules()) {
			RuleWardedDecorator rwd = new RuleWardedDecorator(r);
			/* for each variable */
			for (Variable var : r.getBodyVariables(true)) {
				/* all the positions for the variable at hand in this rule */
				varPos = rwd.getPositiveBodyPositionsByVariable(var);
				for (Position p : varPos) {
					/* if it appears in some non-affected position in this rule */
					if (!affectedPos.contains(p)) {
						/* the variable is harmless, thus return it for the rule */
						Set<Variable> vars = harmlessVarsToReturn.get(r);
						vars.add(var);
						harmlessVarsToReturn.put(r, vars);
						break; // one affected position is sufficient to tell that the variable is harmless
					}
				}
			}
		}

		return harmlessVarsToReturn;
	}

	/**
	 * It returns all the dangerous variables, independently from their rule.
	 * 
	 * @return the set of the dangerous variables
	 */
	public Set<Variable> getDangerousVariables() {
		Set<Variable> res = new HashSet<>();
		Map<Rule, Set<Variable>> s = this.getHarmfulVariablesByRule(true);
		s.values().forEach(res::addAll);
		return res;
	}

	/**
	 * It returns whether the model has only existentially quantified variables that
	 * are calculated, that is, they do not produce marked nulls. This is
	 * particularly useful because can drive the choice of termination strategy. If
	 * the variables are only calculated, we do not need to check for isomorphism,
	 * with the warded criterion, yet the nearly-linear one is sufficient.
	 * 
	 * @return if all the existential variables are calculated
	 */
	public boolean areExistentialVariablesOnlyCalculated() {
		return this.model.getRules().stream()
				.allMatch(x -> x.getExistentiallyQuantifiedVariablesNotCalculated().isEmpty());
	}

	/**
	 * It returns a Map that for each rule reports the set of harmful or dangerous
	 * variables. A variable in a rule is harmful with respect to the program, if it
	 * is not harmless. If it also appears in the head, then it is dangerous. This
	 * property is not known at rule level.
	 * 
	 * @param isDangerous whether to return the dangerous variables only
	 * @return the set of the harmful/dangerous variables
	 */
	public Map<Rule, Set<Variable>> getHarmfulVariablesByRule(boolean isDangerous) {
		/* the map to return */
		Map<Rule, Set<Variable>> harmfulVariablesByRule = new HashMap<>();
		/* we obtain the set of harmless variables by rule */
		Map<Rule, Set<Variable>> harmlessVariables = this.getHarmlessVariablesByRule();
		Set<Variable> harmfulVariables;

		/* for each rule, we build the set of its dangerous variables */
		/* which are those that are not harmless */
		for (Rule r : this.model.getRules()) {
			/* the harmful variables, which are those that are not harmless */
			harmfulVariables = r.getBodyVariables(true).stream().filter(x -> !harmlessVariables.get(r).contains(x))
					.collect(Collectors.toSet());

			/* if we require only the dangerous ones, we just filter the */
			/* harmful variables and return them */
			if (isDangerous) {
				harmfulVariables = harmfulVariables.stream().filter(x -> r.getSingleHead().getVariablesAsSet().contains(x))
						.collect(Collectors.toSet());
			}
			/* add the new set to the list */
			harmfulVariablesByRule.put(r, harmfulVariables);
		}
		return harmfulVariablesByRule;
	}

	/**
	 * It returns whether the program is stratified and ground, which means that for
	 * each negative literal each term is either a constant or a harmless variable.
	 * 
	 * @return Whether the program is stratified and ground.
	 */
	public boolean isStratifiedAndGround() {
		Map<Rule, Set<Variable>> harmlessVariables = this.getHarmlessVariablesByRule();

		for (Rule r : this.model.getRules()) {
			List<Literal> negativeLiterals = r.getNegativeLiterals();
			for (Literal l : negativeLiterals) {
				for (Term t : l.getAtom().getArguments()) {
					if ((t instanceof Variable) && (!harmlessVariables.get(r).contains(t))) {
						if (log.isDebugEnabled())
							log.debug("Rule " + r + " is not stratified and ground since atom " + l
									+ " contains variable " + t + " that is harmful.");
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * It returns whether the program is weakly frontier guarded, that is, all the
	 * dangerous variables are in a guard.
	 * 
	 * @return whether the program is weakly frontier guarded or not
	 */
	public boolean isWeaklyFrontierGuarded() {
		return xWardedAnalysis(true, false).getStatus() == XWardedModelAnalysisResultStatus.SUCCESS;
	}

	/**
	 * It returns whether the program is warded, that is, all the dangerous
	 * variables are warded and the ward interacts with the other atoms only via
	 * harmless variables.
	 * 
	 * @return whether the program is warded or not
	 */
	public boolean isWarded() {
		return xWardedAnalysis(false, false).getStatus() == XWardedModelAnalysisResultStatus.SUCCESS;
	}

	public static final class XWardedModelAnalysisResult {

		public enum XWardedModelAnalysisResultStatus {
			SUCCESS, UNSUCCESS;
		}

		private final XWardedModelAnalysisResultStatus status;

		public XWardedModelAnalysisResultStatus getStatus() {
			return status;
		}

		private final Map<Rule, XWardedRuleAnalysisResult> rulesAnalysisResults;

		public Map<Rule, XWardedRuleAnalysisResult> getRulesAnalysisResults() {
			return Collections.unmodifiableMap(rulesAnalysisResults);
		}

		public XWardedModelAnalysisResult(XWardedModelAnalysisResultStatus status,
				Map<Rule, XWardedRuleAnalysisResult> rulesAnalysisResults) {
			super();
			this.status = status;
			this.rulesAnalysisResults = rulesAnalysisResults == null ? Collections.emptyMap() : rulesAnalysisResults;
		}

	}

	public XWardedModelAnalysisResult xWardedAnalysis(boolean onlyWeaklyFrontier, boolean detailedInfo) {
		/* it considers all the dangerous variables */
		Map<Rule, Set<Variable>> dangerousVarsByRule = this.getHarmfulVariablesByRule(true);
		/* and the harmless variables */
		Map<Rule, Set<Variable>> harmlessVarsByRule = this.getHarmlessVariablesByRule();

		Map<Rule, XWardedRuleAnalysisResult> rulesAnalysisResults = detailedInfo ? new HashMap<>() : null;
		XWardedModelAnalysisResultStatus status = XWardedModelAnalysisResultStatus.SUCCESS;
		if (log.isDebugEnabled()) {
			log.debug("Dangerous variables by rule are: " + dangerousVarsByRule);
			Optional<Integer> optional = dangerousVarsByRule.values().stream().map(Set::size).reduce((x, y) -> x + y);
			if (optional.isPresent()) {
				log.debug("Total of dangerous variables: " + optional.get());
			}
		}

		/*
		 * and, rule by rule, verifies that the rule is warded with respect to the
		 * dangerous
		 */
		/* and the harmless variables. */
		for (Rule r : this.model.getRules()) {
			RuleWardedDecorator rwd = new RuleWardedDecorator(r);
			if (onlyWeaklyFrontier) {
				XWardedRuleAnalysisResult res = rwd.isXWeaklyFrontierGuardedAdvanced(dangerousVarsByRule.get(r),
						detailedInfo);
				if (res.getStatus() != XWardedRuleAnalysisResultStatus.SUCCESS) {
					if (log.isDebugEnabled())
						log.debug("Rule " + r + " is not weakly frontier guarded");
					if (!detailedInfo)
						return new XWardedModelAnalysisResult(XWardedModelAnalysisResultStatus.UNSUCCESS,
								rulesAnalysisResults);
					status = XWardedModelAnalysisResultStatus.UNSUCCESS;
				}
				if (detailedInfo) {
					rulesAnalysisResults.put(r, res);
				}
			} else {
				XWardedRuleAnalysisResult res = rwd.isXWardedAdvanced(dangerousVarsByRule.get(r),
						harmlessVarsByRule.get(r), detailedInfo);
				if (res.getStatus() != XWardedRuleAnalysisResultStatus.SUCCESS) {
					if (log.isDebugEnabled())
						log.debug("Rule " + r + " is not warded");
					if (!detailedInfo)
						return new XWardedModelAnalysisResult(XWardedModelAnalysisResultStatus.UNSUCCESS,
								rulesAnalysisResults);
					status = XWardedModelAnalysisResultStatus.UNSUCCESS;
				}
				if (detailedInfo) {
					rulesAnalysisResults.put(r, res);
				}
			}
		}
		if (log.isDebugEnabled()) {
			log.debug(status == XWardedModelAnalysisResultStatus.SUCCESS ? "Wardedness test ok."
					: "Program is not warded");
		}
		return new XWardedModelAnalysisResult(status, rulesAnalysisResults);
	}

	/**
	 * It returns the literal of rDatalog that unifies with cause and is either
	 * involved in a join or in a Skolem condition.
	 * 
	 * @param rWarded the Rule to simplify
	 * @param cause   the second operand of the composition
	 * @return the literal of rDatalog that unifies with cause
	 */
	public int getCompositionLiteralPosition(Rule rWarded, Rule cause) {
		int litPos = -1;
		for (Literal l : rWarded.getLiterals()) {
			litPos++;
			AtomOperatorDecorator aod = new AtomOperatorDecorator(l.getAtom());
			/* if the literal unifies with the head it means that we */
			/* can compose along it */
			if (aod.unify().does(cause.getSingleHead())) {
				/* but we also need to know whether it is involved in a join */
				boolean inJoin = l.getAtom().getVariablesAsSet().stream()
						.anyMatch(v -> rWarded.getBodyLiteralsByVariable(v).size() > 1);
				/* or if it is involved in one or more Skolem conditions */
				boolean inCondition = l.getAtom().getVariablesAsSet().stream()
						.anyMatch(v -> rWarded.getSkolemAtomsByCalculatedVariable(v).size() > 0);
				if (inJoin || inCondition)
					return litPos;
			}
		}
		return -1;
	}

	/**
	 * It calculates and returns for each Rule, the set of causes that hamper their
	 * joins making them HH. Here we are interested only in HH joins.
	 * 
	 * @param m the model to operate on
	 * 
	 * @return a map from rules to sets of affected literals.
	 */
	public Map<Rule, Set<Rule>> getHHJoinAffectednessCausesByRule(Model m) {

		ModelWardedDecorator mwd = new ModelWardedDecorator(m);

		/* to return */
		Map<Rule, Set<Rule>> affectedRulesAndCauses = new HashMap<>();

		/* the harmful variables by rule */
		Map<Rule, Set<Variable>> harmfulVariablesByRule = mwd.getHarmfulVariablesByRule(false);

		/* we get all the affected positions (with the causes) */
		Set<Position> affectedPositions = mwd.getAffectedPositions();

		/* we find all the non-linear rules having HH joins */

		/* only affectedness in joins */
		for (Rule r : m.getRules().stream().filter(x -> !x.isLinear()).collect(Collectors.toList())) {

			RuleWardedDecorator rwd = new RuleWardedDecorator(r);

			/* we get all the positions in a rule on which there is a join */
			Set<Position> allBodyPositionsWithJoins = affectedPositions.stream()
					.filter(x -> rwd.getAllBodyVariablePositionsWithJoins().contains(x)).collect(Collectors.toSet());

			/* we consider only the positions that are harmful */
			Set<Variable> harmfulVariables = harmfulVariablesByRule.get(r);

			Set<Position> rulePositions = new HashSet<>();

			/* we need to know which harmful positions appear in the rule */
			for (Position p : allBodyPositionsWithJoins) {
				if (harmfulVariables != null && harmfulVariables.containsAll(rwd.getTermAtPosition(p)))
					rulePositions.add(p);
			}

			/* from the rule positions we get the causes */
			Set<Rule> causes = new HashSet<>();
			for (Position p : rulePositions) {
				causes.addAll(p.getCauses());
			}

			/* and return them */
			if (causes.size() > 0)
				affectedRulesAndCauses.put(r, causes);
		}
		return affectedRulesAndCauses;
	}

	/**
	 * This method ensures that the warded rules in a warded program (the non-linear
	 * ones) do not have existential quantifiers in the head. To this purpose, it
	 * decomposes each rule into two rules a non-linear rule without existential
	 * quantifiers and a linear rule with the existential quantifiers. It introduces
	 * a new temporary atom to do so and handles the possibly present linear
	 * functions in the head.
	 * 
	 * @return new Model with linearized heads.
	 */
	public Model linearizeHeads() {
		Model m = new Model(this.model);
		m.getRules().clear();
		for (Rule r : this.model.getRules()) {
			RuleWardedDecorator rwd = new RuleWardedDecorator(r);
			/* if the rule isn't warded with exists added rule same as the original rule */
			if (!rwd.isWardedWithExists()) {
				m.getRules().add(new Rule(r));
			}
			/* else transform the rule with linearizeHead method */
			else {
				m.getRules().addAll(rwd.linearizeHead());
			}
		}
		return m;
	}

	/**
	 * This method ensures that the rules in a warded program with more atoms in the
	 * head are parsed correctly. It decomposes each multi-head rule into equivalent
	 * N+1 (N is the number of the atoms in head's rule) single head rules.
	 * 
	 * @return The new model without multi-head rules
	 */
	public Model multiHeadRewriting() {
		Model m = new Model(this.model);
		m.getRules().clear();

		/* for each rule */
		for (Rule r : this.model.getRules()) {
			/* if the size of the head rule is bigger than 1 transform rule */
			if (r.getHead().size() > 1) {
				RuleWardedDecorator rd = new RuleWardedDecorator(r);
				m.getRules().addAll(rd.multiHeadRewriting());
			} else {
				m.getRules().add(new Rule(r));
			}
		}

		return m;
	}

	/**
	 * This method rewriting all the right recursions into left recursions.
	 * 
	 * @return The new Model.
	 */
	public Model rightRecursionInversion() {

		PrecedenceGraph precedenceGraph = new PrecedenceGraph();
		for (Rule rule : this.model.getRules()) {
			for (Atom headAtom : rule.getHead()) {
				Node headNode = precedenceGraph.createIfAbsent(headAtom.getName());
				for (Literal bodyLiteral : rule.getBody())
					precedenceGraph.createIfAbsent(bodyLiteral.getAtom().getName())
							.addEdge(new Edge(headNode, bodyLiteral.isPositive()));
			}
		}
		precedenceGraph.SCCs();
		Model m = new Model(this.model);
		m.getRules().clear();

		for (Rule r : this.model.getRules()) {
			RuleWardedDecorator rd = new RuleWardedDecorator(r);
			m.getRules().add(rd.rightRecursionInversion(precedenceGraph));
		}

		return m;
	}

	/**
	 * This method splits the multi-join rules into more rules (with 2 atom join).
	 * 
	 * @return The new model.
	 */
	public Model multiJoinOptimization() {
		Model m = new Model(this.model);
		m.getRules().clear();

		for (Rule r : this.model.getRules()) {
			if (r.getBody().size() > 2) {
				RuleWardedDecorator rd = new RuleWardedDecorator(r);
				List<Rule> joinOptimized = rd.multiJoinOptimization(Collections.emptySet());
				/* the bodies need to be sorted, as there could be negations */
				joinOptimized.forEach(Rule::sortBodyByNegation);
				m.getRules().addAll(joinOptimized);
			} else {
				Rule newRule = new Rule(r);
				newRule.sortBodyByNegation();
				/* the bodies need to be sorted, as there could be negations */
				m.getRules().add(newRule);
			}
		}

		return m;
	}

	/**
	 * This method removes harmful joins from the actual model.
	 * 
	 * @return a copy of the model without harmful joins
	 */
	public Model simplifyHarmfulHarmfulJoins() {
		return new HJEWardedSimplifier(this.model).harmfulJoinElimination();
	}

}
package uk.co.prometheux.prometheuxreasoner.warded;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;

import uk.co.prometheux.prometheuxreasoner.common.IdGenerator;
import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Condition;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Term;
import uk.co.prometheux.prometheuxreasoner.model.Variable;
import uk.co.prometheux.prometheuxreasoner.precedenceGraph.PrecedenceGraph;
import uk.co.prometheux.prometheuxreasoner.warded.RuleWardedDecorator.XWardedRuleAnalysisResult.XWardedRuleAnalysisResultStatus;

/**
 * It decorates a Rule, adding many useful functionalities to deal with the
 * warded fragment.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class RuleWardedDecorator {

	private Rule rule;

	private Logger log = LoggerFactory.getLogger(ModelWardedDecorator.class);

	public RuleWardedDecorator(Rule rule) {
		this.rule = rule;
	}

	/**
	 * It returns all the Positions in the positive body where a variable x appears
	 * 
	 * @param x the variable to be looked up
	 * @return the set of the positions
	 */
	public Set<Position> getPositiveBodyPositionsByVariable(Variable x) {
		List<Atom> atoms = this.rule.getPositiveLiterals().stream().map(Literal::getAtom).filter(z -> !z.isSkolemAtom())
				.collect(Collectors.toList());

		Set<Position> positions = new HashSet<>();

		for (Atom a : atoms)
			positions.addAll((new AtomWardedDecorator(a)).getPositionsByVariable(x));

		return positions;
	}

	/**
	 * It returns all the Positions in the body where a variable x appears
	 * 
	 * @param x the variable to be looked up
	 * @return the set of positions
	 */
	public Set<Position> getBodyPositionsByVariable(Variable x) {
		List<Atom> atoms = this.rule.getLiterals().stream().map(Literal::getAtom).collect(Collectors.toList());

		Set<Position> positions = new HashSet<>();

		for (Atom a : atoms)
			positions.addAll((new AtomWardedDecorator(a)).getPositionsByVariable(x));

		return positions;
	}

	/**
	 * It returns all the Positions in the head where a variable x appears
	 * 
	 * @param x the variable to be looked up
	 * @return the set of the positions
	 */
	public Set<Position> getHeadPositionByVariable(Variable x) {
		Atom head = this.rule.getSingleHead();
		return (new AtomWardedDecorator(head)).getPositionsByVariable(x);
	}

	/**
	 * It returns all the positions where a variable appears in a rule, in both the
	 * positive body and the head.
	 * 
	 * @param x the variable to be looked up
	 * @return the set of positions
	 */
	public Set<Position> getAllPositivePositionsByVariable(Variable x) {
		Set<Position> body = this.getPositiveBodyPositionsByVariable(x);
		Set<Position> head = this.getHeadPositionByVariable(x);
		body.addAll(head);
		return body;
	}

	/**
	 * It returns the Terms present at a given position in the body
	 * 
	 * @param p the Position
	 * @return the Terms
	 */
	public Set<Term> getTermAtPosition(Position p) {
		Set<Term> termSet = new HashSet<>();
		/* for each literal */
		for (Literal l : this.rule.getBody()) {
			/* if the literal has the same atom as the position */
			if (l.getAtom().getName().equals(p.getAtom())) {
				/* get the appropriate term */
				termSet.add(l.getAtom().getArguments().get(p.getPosition()));
			}
		}
		return termSet;
	}

	/**
	 * It returns all the positions for the rule
	 * 
	 * @return the set of the positions
	 */
	public Set<Position> getAllBodyVariablePositions() {
		Set<Position> pos = new HashSet<>();

		for (Literal l : this.rule.getBody()) {
			for (Term t : l.getAtom().getArguments()) {
				if (t instanceof Variable)
					pos.add(new Position(l.getAtom(), l.getAtom().getArguments().indexOf(t)));
			}
		}
		return pos;
	}

	/**
	 * It returns all the literal positions (that is, taking into account the
	 * specific literal) for the rule only if the positions correspond to variables
	 * on which a join is defined
	 * 
	 * @return the set of the positions with joins
	 */
	public Set<Position> getAllBodyVariablePositionsWithJoins() {
		Set<Position> pos = new HashSet<>();
		for (Literal l : this.rule.getBody()) {
			if (!l.getAtom().isSkolemAtom()) {
				for (Term t : l.getAtom().getArguments()) {
					if (t instanceof Variable) {
						/* if the variable appears in more than one literal */
						if (this.rule.getBodyLiteralsByVariable((Variable) t).size() > 1)
							pos.add(new Position(l.getAtom(), l.getAtom().getArguments().indexOf(t)));
					}
				}
			}
		}
		return pos;
	}

	/**
	 * It returns all the literal positions (that is, taking into account the
	 * specific literal) for the rule only if the positions correspond to variables
	 * on which are calculated variables in a Skolem atom.
	 * 
	 * @return the set of the positions with joins
	 */
	public Set<Position> getAllBodyVariablePositionsWithSkolemCalculatedVariables() {
		Set<Position> pos = new HashSet<>();
		for (Literal l : this.rule.getBody()) {
			if (!l.getAtom().isSkolemAtom()) {
				for (Term t : l.getAtom().getArguments()) {
					if (t instanceof Variable) {
						/* if the variable appears as a calculated variable in a Skolem atom */
						if (this.rule.getSkolemAtomsByCalculatedVariable((Variable) t).size() >= 1)
							pos.add(new Position(l.getAtom(), l.getAtom().getArguments().indexOf(t)));
					}
				}
			}
		}
		return pos;

	}

	/**
	 * It verifies whether the rule is warded with respect to variable vars. It is
	 * true if there is exactly one atom, the ward, that contains all such variables
	 * and it interacts with the others only via harmless variables.
	 * 
	 * @param vars              the set of variables for which to test the
	 *                          wardedness
	 * @param harmlessVariables the set of harmless variables to use to verify the
	 *                          wardedness
	 * @return whether the atom is warded or not
	 */
	public boolean isXWarded(Set<Variable> vars, Set<Variable> harmlessVariables) {
		return isXWardedAdvanced(vars, harmlessVariables, false).getStatus() == XWardedRuleAnalysisResultStatus.SUCCESS;
	}

	public XWardedRuleAnalysisResult isXWardedAdvanced(Set<Variable> vars, Set<Variable> harmlessVariables,
			boolean detailedInfo) {
		return xWardedAnalysis(vars, harmlessVariables, false, detailedInfo);
	}

	/**
	 * It verifies whether the rule is weakly frontier guarded with respect to
	 * variable vars. It is true if there is exactly one atom, the guard, that
	 * contains all such variables.
	 * 
	 * @param vars the set of variables for which to test the wardedness
	 * @return whether the atom is warded or not
	 */
	public boolean isXWeaklyFrontierGuarded(Set<Variable> vars) {
		return isXWeaklyFrontierGuardedAdvanced(vars, false).getStatus() == XWardedRuleAnalysisResultStatus.SUCCESS;
	}

	public XWardedRuleAnalysisResult isXWeaklyFrontierGuardedAdvanced(Set<Variable> vars, boolean detailedInfo) {
		return xWardedAnalysis(vars, new HashSet<>(), true, detailedInfo);
	}

	/**
	 * It represent the result of the analysis of the rule for its wardedness or
	 * weakly frontier guardedness.
	 */
	public static final class XWardedRuleAnalysisResult {
		public enum XWardedRuleAnalysisResultStatus {
			SUCCESS("The rule is warded/guarded"),
			NO_GUARD(
					"Rule is not warded (nor weakly frontier guarded) since no single atom contains all the dangerous variables."),
			SEVERAL_GUARDS(
					"Rule is not warded (nor weakly frontier guarded) since several atoms contain all the dangerous variables."),
			SHARED_DANGEROUS_VARS(
					"Rule is not warded (nor weakly frontier guarded) since dangerous variables appear in more than one atom."),
			SHARED_HARMFUL_VARS("Rule is not warded since the guard(\"ward\") joins via the harmful variable ");

			private final String description;

			public String getDescription() {
				return description;
			}

			private XWardedRuleAnalysisResultStatus(String description) {
				this.description = description;
			}
		}

		private final XWardedRuleAnalysisResultStatus status;

		public XWardedRuleAnalysisResultStatus getStatus() {
			return status;
		}

		private final List<Literal> guards;

		public List<Literal> getGuards() {
			return Collections.unmodifiableList(guards);
		}

		private final Map<Variable, Collection<Literal>> data;

		public Map<Variable, Collection<Literal>> getData() {
			return Collections.unmodifiableMap(data);
		}

		public Map<Variable, Collection<Literal>> getMultipleData() {
			Map<Variable, Collection<Literal>> multipleData = new HashMap<Variable, Collection<Literal>>();
			data.forEach((var, ls) -> {
				if (ls.size() > 1) {
					multipleData.put(var, ls);
				}
			});
			return multipleData;
		}

		public XWardedRuleAnalysisResult(XWardedRuleAnalysisResultStatus status) {
			this(status, null);
		}

		public XWardedRuleAnalysisResult(XWardedRuleAnalysisResultStatus status, List<Literal> guards) {
			this(status, guards, (Map<Variable, Collection<Literal>>) null);
		}

		public XWardedRuleAnalysisResult(XWardedRuleAnalysisResultStatus status, List<Literal> guards,
				ListMultimap<Variable, Literal> data) {
			this(status, guards, data == null ? null : data.asMap());
		}

		public XWardedRuleAnalysisResult(XWardedRuleAnalysisResultStatus status, List<Literal> guards,
				Map<Variable, Collection<Literal>> data) {
			this.status = status;
			this.guards = guards == null ? Collections.emptyList() : guards;
			this.data = data == null ? Collections.emptyMap() : data;
		}
	}

	/**
	 * @param set1 a set which should contain at least 1 value from {@code col2}.
	 * @param col2 collection which should have at least 1 value from the set
	 *             {@code set1}.
	 * @return {@code true} if {@code set1} contains at least 1 value from
	 *         {@code col2}.
	 */
	private <T> boolean _setContainsAny(Set<T> set1, Collection<T> col2) {
		for (T t : col2) {
			if (set1.contains(t))
				return true;
		}
		return false;
	}

	/**
	 * It verifies whether the rule is warded or weakly frontier guarded with
	 * respect to variable vars. In case
	 * {@code onlyWeaklyFrontierGuardedCheck = false}: It returns is true if there
	 * is exactly one atom, the ward, that contains all such (dangerous) variables
	 * and it interacts with the others only via harmless variables. In case
	 * {@code onlyWeaklyFrontierGuardedCheck = true}: It returns is true if there is
	 * exactly one atom, the guard, that contains all such (dangerous) variables.
	 * 
	 * @param dangerousVars                  the set of variables for which to test
	 *                                       the wardedness
	 * @param harmlessVariables              the set of harmless variables to use to
	 *                                       verify the wardedness
	 * @param onlyWeaklyFrontierGuardedCheck
	 * @param detailedInfo                   If {@code true}, a detailed information
	 *                                       is generated.
	 * @return whether the atom is warded/guarded or not
	 */
	private XWardedRuleAnalysisResult xWardedAnalysis(Set<Variable> dangerousVars, Set<Variable> harmlessVariables,
			boolean onlyWeaklyFrontierGuardedCheck, boolean detailedInfo) {

		if (log.isDebugEnabled())
			log.debug("BEGIN checking rule " + this.rule + " for wardedness.");

		if (dangerousVars.size() == 0) {
			log.debug(XWardedRuleAnalysisResultStatus.SUCCESS.getDescription());
			return new XWardedRuleAnalysisResult(XWardedRuleAnalysisResultStatus.SUCCESS);
		}

		if (log.isDebugEnabled())
			log.debug("Checking the following dangerous variables: " + dangerousVars);

		List<Literal> guards = new ArrayList<>();
		ListMultimap<Variable, Literal> dangerousVarsMap = detailedInfo ? ArrayListMultimap.create() : null;
		boolean hasDangerousVarsShared = false;
		Set<Variable> consideredDangerousVars = new HashSet<>();

		List<Literal> literals = rule.getLiterals();
		int literalsN = literals.size();
		int literalsI = 0;
		for (Literal l : literals) {
			literalsI++;
			Set<Variable> lVars = new HashSet<>(l.getAtom().getVariablesAsSet());
			// guards are positive literals containing all dangerous variables
			if (l.isPositive() && lVars.containsAll(dangerousVars)) {
				guards.add(l);
				// In case we do not need a detail analysis and we identified that the rule is
				// not warded/guarded.
				if (!detailedInfo && guards.size() > 1)
					break;
			}
			Set<Variable> lDangerousVars = Sets.intersection(lVars, dangerousVars);
			// guards/wards do not share dangerous variables
			if (!hasDangerousVarsShared && _setContainsAny(consideredDangerousVars, lDangerousVars)) {
				hasDangerousVarsShared = true;
				// In case we do not need a detail analysis and we identified that the rule is
				// not warded/guarded.
				if (!detailedInfo)
					break;
			}
			consideredDangerousVars.addAll(lDangerousVars);
			// add info regarding the use of dangerous vars in literals
			if (detailedInfo) {
				for (Variable lDangerousVar : lDangerousVars) {
					dangerousVarsMap.put(lDangerousVar, l);
				}
			}
		}

		if (literalsI == literalsN && guards.size() == 0) {
			log.debug(XWardedRuleAnalysisResultStatus.NO_GUARD.getDescription());
			return new XWardedRuleAnalysisResult(XWardedRuleAnalysisResultStatus.NO_GUARD);
		} else if (guards.size() > 1) {
			log.debug(XWardedRuleAnalysisResultStatus.SEVERAL_GUARDS.getDescription());
			return new XWardedRuleAnalysisResult(XWardedRuleAnalysisResultStatus.SEVERAL_GUARDS, guards);
		} else if (hasDangerousVarsShared) {
			log.debug(XWardedRuleAnalysisResultStatus.SHARED_DANGEROUS_VARS.getDescription());
			return new XWardedRuleAnalysisResult(XWardedRuleAnalysisResultStatus.SHARED_DANGEROUS_VARS, guards,
					dangerousVarsMap);
		}

		if (!onlyWeaklyFrontierGuardedCheck) {
			// check whether harmful variables are shared for the found guard/ward.
			Literal guard = guards.get(0);
			Set<Variable> harmfulVars = Sets.difference(new HashSet<>(guard.getAtom().getVariablesAsSet()), harmlessVariables);
			ListMultimap<Variable, Literal> harmfulVarsMap = detailedInfo ? ArrayListMultimap.create() : null;
			boolean hasHarmfulVarsShared = false;
			Set<Variable> consideredHarmfulVars = new HashSet<>();
			for (Literal l : literals) {
				Set<Variable> lHarmfulVars = Sets.intersection(new HashSet<>(l.getAtom().getVariablesAsSet()), harmfulVars);
				if (!hasHarmfulVarsShared && _setContainsAny(consideredHarmfulVars, lHarmfulVars)) {
					hasHarmfulVarsShared = true;
					// In case we do not need a detail analysis and we identified that the rule is
					// not warded.
					if (!detailedInfo)
						break;
				}
				consideredHarmfulVars.addAll(lHarmfulVars);
				if (detailedInfo) {
					for (Variable lHarmfulVar : lHarmfulVars) {
						harmfulVarsMap.put(lHarmfulVar, l);
					}
				}
			}
			if (hasHarmfulVarsShared) {
				log.debug(XWardedRuleAnalysisResultStatus.SHARED_HARMFUL_VARS.getDescription());
				return new XWardedRuleAnalysisResult(XWardedRuleAnalysisResultStatus.SHARED_HARMFUL_VARS, guards,
						harmfulVarsMap);
			}
		}

		log.debug(XWardedRuleAnalysisResultStatus.SUCCESS.getDescription());
		return new XWardedRuleAnalysisResult(XWardedRuleAnalysisResultStatus.SUCCESS, guards);
	}

	/**
	 * It verifies whether the rule is a datalog rule and head's rule contains the
	 * existential quantifier.
	 * 
	 * @return whether the rule is a datalog rule with existential quantifier.
	 */
	public boolean isWardedWithExists() {
		List<Literal> body = this.rule.getBody();

		return (body.size() >= 2 && this.rule.getExistentiallyQuantifiedVariables().size() >= 1);
	}

	/**
	 * It transforms the rule (warded with existential quantification in the head)
	 * into two rules a. and b. Rule a. contains the same body as the original rule,
	 * and the head is the same head as the original rule, without the existentially
	 * quantified variables. In case an existentially quantified variable was
	 * present in the original rule and its values were calculated with an
	 * expression, the operand variables appearing in the expression appear in the
	 * head of a. Possible conditions in the original rule are copied to a. The head
	 * of b. coincides with the head of the original rule, while the body copies
	 * from the first rule.
	 * 
	 * @return the two rules that linearize the input one.
	 */
	public List<Rule> linearizeHead() {
		List<Rule> rules = new ArrayList<>();
		Rule ruleCopy = new Rule(this.rule);
		List<Literal> body = ruleCopy.getBody();
		List<Condition> conditionsIterator = ruleCopy.getConditions();
		List<Condition> conditionsWardedRule = new ArrayList<>();
		List<Condition> conditionsLinearRule = new ArrayList<>();

		Set<Variable> existentialOrComputedVariables = ruleCopy.getExistentiallyQuantifiedVariables();
		existentialOrComputedVariables.addAll(ruleCopy.getCalculatedVariables());

		List<Atom> head = ruleCopy.getHead();
		/* copy of variables in head */
		List<Term> headVariablesCopy = new ArrayList<>(ruleCopy.getHead().get(0).getVariableList());

		/* remove from head copy the exists variables */
		headVariablesCopy.removeAll(existentialOrComputedVariables);
		/*
		 * Create a new atom for new rule with random name and without exists variables
		 * in head
		 */
		Atom new_atom = new Atom(IdGenerator.getNewAtomSymbol("vatom_"), headVariablesCopy);

		/* create the two condition lists for linear rule and warded rule and */
		/* add variable in linearized rule */
		for (Condition c : conditionsIterator) {
			if (existentialOrComputedVariables.contains(c.getLhs())) {
				conditionsLinearRule.add(c);
				for (Variable v : c.getAllExpressionVariables()) {
					/* the second conditions avoids the cases where the */
					/* existential variable to linearize is the group by argument */
					/* or in any case the argument of an aggregation. */
					if (!new_atom.getVariableList().contains(v) && !existentialOrComputedVariables.contains(v)) {
						List<Term> new_term_arguments = new_atom.getArguments();
						new_term_arguments.add(v);
						new_atom.setArguments(new_term_arguments);
					}
				}
			} else {
				conditionsWardedRule.add(c);
			}
		}

		Rule r1 = new Rule(new_atom, body, conditionsWardedRule, this.rule.getAnnotations());

		/*
		 * create a literal for the body (same to head of the first rule) of the second
		 * rule
		 */
		Literal new_body = new Literal(new_atom, true);
		Rule r2 = new Rule(head.get(0), new_body, conditionsLinearRule, this.rule.getAnnotations());

		rules.add(r1);
		rules.add(r2);
		return rules;
	}

	/**
	 * It transform the rule (warded with more atoms in the head) into N + 1 rule.
	 * Rule 0 contains the same body as the original rule, and a new head. Rule i
	 * contains the i-th head of the original rule, while the body is the head of
	 * Rule 0.
	 * 
	 * @return List that contains the N + 1 rules.
	 */
	public List<Rule> multiHeadRewriting() {
		List<Rule> rules = new ArrayList<>();
		Atom newHead = new Atom(IdGenerator.getNewAtomSymbol("vatom_"), new ArrayList<>(rule.getHeadVariablesAsSet()));
		rules.add(new Rule(newHead, rule.getBody(), rule.getConditions(), rule.getAnnotations()));
		Literal newBody = new Literal(newHead, true);
		for (Atom head : rule.getHead())
			rules.add(new Rule(head, Collections.singletonList(newBody), new ArrayList<>(), rule.getAnnotations()));
		return rules;
	}

	/**
	 * This method transform the right recursion into left recursion of the current
	 * rule.
	 * 
	 * @return The new Rule.
	 */
	public Rule rightRecursionInversion(PrecedenceGraph precedenceGraph) {
		Rule r = new Rule(this.rule);
		List<Literal> body = new ArrayList<>();
		Set<String> headPredicates = r.getHead().stream().map(Atom::getName).collect(Collectors.toSet());
		for (Literal l : r.getBody()) {
			if (l.isPositive() && precedenceGraph.sameStrata(l.getAtom().getName(), headPredicates))
				body.add(0, l);
			else
				body.add(l);
		}
		Rule newrule = new Rule(r.getHead(), body, r.getConditions(), r.getAnnotations());
		newrule.setDomStar(r.isDomStar());
		return newrule;
	}

	/**
	 * Rewrites the current rule into an equivalent set of rules with at most 2
	 * positive body atoms each. The rewriting preserve connectedness (when
	 * possible) and it tries to respect the original order as long as connectedness
	 * is not violated. Parametric qbind atoms come after all other positive atoms.
	 * The method should in future use a query optimiser to determine the best
	 * evaluation order.
	 * 
	 * @return The rewriting of the current rule, if it has more than 2 positive
	 *         atoms, or the rule itself otherwise.
	 * @param parametricQBindPredicates
	 */
	public List<Rule> multiJoinOptimization(Set<String> parametricQBindPredicates) {
		// the rewriting makes sense for rules with multiple body atoms
		if (rule.getBody().size() < 3)
			return Collections.singletonList(rule);
		// independent positive atoms are those that are positive and not parametric
		List<Atom> independentPositiveAtoms = new ArrayList<>();
		// dependent positive atoms are those that are positive and parametric (i.e.
		// coming from parametric qbind)
		List<Atom> dependentPositiveAtoms = new ArrayList<>();
		// stores the variables of all positive atoms
		Set<Variable> positivelyBoundVariables = new HashSet<>();
		initializePositiveAtomInfo(parametricQBindPredicates, independentPositiveAtoms, dependentPositiveAtoms,
				positivelyBoundVariables);
		// collect the conditions that act as filters (e.g. head :- a(X, Y), X = Y + 1):
		// they should be added as soon as
		// all variables are bound. The set contains variables of the condition that are
		// yet to be bound.
		Map<Condition, Set<Variable>> filterConditions = getFilterConditions(positivelyBoundVariables);
		// collect the negative literals: they also act as filters and should be added
		// as soon as all shared variables
		// are bound. The set contains the shared variables of a literal that are yet to
		// be bound.
		Map<Literal, Set<Variable>> negativeLiterals = initializeNegativeLiteralsInfo(positivelyBoundVariables);
		List<Rule> rules = new ArrayList<>();
		Atom currentHead = independentPositiveAtoms.get(0);
		independentPositiveAtoms.remove(0);
		// Use LinkedHashSet to maintain the order of variables
		Set<Term> currentHeadVars = new LinkedHashSet<>(currentHead.getVariablesAsSet());
		// noinspection SuspiciousMethodCalls
		filterConditions.forEach((cond, vars) -> vars.removeAll(currentHeadVars));
		// noinspection SuspiciousMethodCalls
		negativeLiterals.forEach((literal, vars) -> vars.removeAll(currentHeadVars));
		while (!(independentPositiveAtoms.isEmpty() && dependentPositiveAtoms.isEmpty())) {
			Atom nextAtom;
			// we exhaust parametric qbind atoms after the non-parametric ones
			nextAtom = selectNextAtom(independentPositiveAtoms, dependentPositiveAtoms, currentHeadVars);
			// initialize the rule body with the current head and the chosen atom
			List<Literal> ruleBody = new ArrayList<>(
					Arrays.asList(new Literal(currentHead, true), new Literal(nextAtom, true)));

			// initialise the new head
			currentHeadVars.addAll(nextAtom.getVariableList());
			currentHead = new Atom(IdGenerator.getNewAtomSymbol("vatom_"), new ArrayList<>(currentHeadVars));
			// collect all conditions whose variables are all bound
			List<Condition> ruleConditions = new ArrayList<>();
			for (Map.Entry<Condition, Set<Variable>> condition : new ArrayList<>(filterConditions.entrySet())) {
				condition.getValue().removeAll(nextAtom.getVariablesAsSet());
				if (condition.getValue().isEmpty()) {
					ruleConditions.add(condition.getKey());
					filterConditions.remove(condition.getKey());
				}
			}
			Rule newrule = new Rule(currentHead, ruleBody, ruleConditions, rule.getAnnotations());
			newrule.setDomStar(rule.isDomStar());
			rules.add(newrule);
			if (!independentPositiveAtoms.isEmpty())
				independentPositiveAtoms.remove(nextAtom);
			else
				dependentPositiveAtoms.remove(nextAtom);
			// create a rule for each negative literal whose shared variables are all bound
			currentHead = addRulesForNegativeAtoms(negativeLiterals, rules, currentHead, currentHeadVars, nextAtom);
		}
		// all remaining filter conditions are added to the final rule together with all
		// conditions that act as
		// assignments (e.g. head(X, Y, Z) :- a(X, Y), Z = Y + 1)
		List<Condition> remainingConditions = new ArrayList<>(filterConditions.keySet());
		rule.getConditions().forEach(condition -> {
			if (!positivelyBoundVariables.contains(condition.getLhs()))
				remainingConditions.add(condition);
		});
		List<Literal> ruleBody = new ArrayList<>(Collections.singletonList(new Literal(currentHead, true)));
		ruleBody.addAll(negativeLiterals.keySet());
		Rule newrule = new Rule(rule.getHead(), ruleBody, remainingConditions, rule.getAnnotations());
		newrule.setDomStar(rule.isDomStar());
		rules.add(newrule);
		return rules;
	}

	private Atom addRulesForNegativeAtoms(Map<Literal, Set<Variable>> negativeLiterals, List<Rule> rules,
			Atom currentHead, Set<Term> currentHeadVars, Atom nextAtom) {
		List<Literal> ruleBody;
		List<Condition> ruleConditions;
		Rule newrule;
		for (Map.Entry<Literal, Set<Variable>> literal : new ArrayList<>(negativeLiterals.entrySet())) {
			literal.getValue().removeAll(nextAtom.getVariablesAsSet());
			if (literal.getValue().isEmpty()) {
				ruleBody = new ArrayList<>(Arrays.asList(new Literal(currentHead, true)));
				ruleBody.add(literal.getKey());
				negativeLiterals.remove(literal.getKey());
				currentHead = new Atom(IdGenerator.getNewAtomSymbol("vatom_"), new ArrayList<>(currentHeadVars));
				ruleConditions = new ArrayList<>();
				newrule = new Rule(currentHead, ruleBody, ruleConditions, rule.getAnnotations());
				newrule.setDomStar(rule.isDomStar());
				rules.add(newrule);
			}
		}
		return currentHead;
	}

	private Atom selectNextAtom(List<Atom> independentPositiveAtoms, List<Atom> dependentPositiveAtoms,
			Set<Term> currentHeadVars) {
		Atom nextAtom;
		if (!independentPositiveAtoms.isEmpty()) {
			// find a joining atom
			Atom joinAtom = null;
			for (Atom atom : independentPositiveAtoms) {
				if (!Collections.disjoint(atom.getVariablesAsSet(), currentHeadVars)) {
					joinAtom = atom;
					break;
				}
			}
			// if there are no joining atoms we choose the next in line to respect the
			// original ordering
			nextAtom = joinAtom == null ? independentPositiveAtoms.get(0) : joinAtom;
		} else
			nextAtom = dependentPositiveAtoms.get(0);
		return nextAtom;
	}

	private Map<Literal, Set<Variable>> initializeNegativeLiteralsInfo(Set<Variable> positivelyBoundVariables) {
		Map<Literal, Set<Variable>> negativeLiterals = new HashMap<>();
		for (Literal literal : rule.getBody()) {
			if (!literal.isPositive()) {
				Set<Variable> sharedVariables = new HashSet<>(literal.getAtom().getVariablesAsSet());
				sharedVariables.retainAll(positivelyBoundVariables);
				negativeLiterals.put(literal, sharedVariables);
			}
		}
		return negativeLiterals;
	}

	private Map<Condition, Set<Variable>> getFilterConditions(Set<Variable> positivelyBoundVariables) {
		Map<Condition, Set<Variable>> filterConditions = new HashMap<>();
		for (Condition condition : rule.getConditions())
			if (positivelyBoundVariables.contains(condition.getLhs()))
				filterConditions.put(condition, new HashSet<>(condition.getAllVariables()));
		return filterConditions;
	}

	private void initializePositiveAtomInfo(Set<String> parametricQBindPredicates, List<Atom> independentPositiveAtoms,
			List<Atom> dependentPositiveAtoms, Set<Variable> positivelyBoundVariables) {
		for (Literal literal : rule.getBody()) {
			if (literal.isPositive()) {
				if (parametricQBindPredicates.contains(literal.getAtom().getName()))
					dependentPositiveAtoms.add(literal.getAtom());
				else
					independentPositiveAtoms.add(literal.getAtom());
				positivelyBoundVariables.addAll(literal.getAtom().getVariableList());
			}
		}
	}

}
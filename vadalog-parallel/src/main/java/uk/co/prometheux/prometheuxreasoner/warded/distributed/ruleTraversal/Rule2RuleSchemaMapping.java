package uk.co.prometheux.prometheuxreasoner.warded.distributed.ruleTraversal;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Term;
import uk.co.prometheux.prometheuxreasoner.pwlWarded.ModelPWLWardedDecorator;

public class Rule2RuleSchemaMapping {

	private Model model;
	private ModelPWLWardedDecorator modelPWLWardedDecorator;

	public Rule2RuleSchemaMapping(Model model) {
		this.model = model;
		modelPWLWardedDecorator = new ModelPWLWardedDecorator(model);
	}

	/**
	 * This method creates a map where for each rule r is maintained the body to
	 * body mapping with other rules that r can reach through direct and indirect
	 * unification
	 * 
	 * @return a map where the key is a rule and the values are the rules that are
	 *         reachable from it
	 */
	public Map<Rule, List<RuleEdge>> ruleBody2RulesBodyMappingPositions() {
		Map<Rule, List<RuleEdge>> ruleBody2RulesBodyMappingPositions = new HashMap<Rule, List<RuleEdge>>();
		/*
		 * we get the edges, where a edge represents a of a rule body to another rule
		 * body mapping
		 */
		Set<RuleEdge> edges = new TransitiveClosureRuleEdgeGraph(this.model).transitiveClosure();
		for (RuleEdge re : edges) {
			Rule rFrom = re.getRFrom();
			List<RuleEdge> reachableEdges = new ArrayList<>();
			if (!ruleBody2RulesBodyMappingPositions.containsKey(rFrom)) {
				reachableEdges.add(re);
				ruleBody2RulesBodyMappingPositions.put(rFrom, reachableEdges);
			} else {
				reachableEdges = ruleBody2RulesBodyMappingPositions.get(rFrom);
				reachableEdges.add(re);
			}
		}

		return ruleBody2RulesBodyMappingPositions;
	}

	/**
	 * This method computes the mapping of the position of an IDB to the position of
	 * an EDB As an example:
	 * 
	 * a(X,Y) :- c(X,Y). a(Z,Y) :- b(Y,X), e(X,Z).
	 * 
	 * {a=[{c=[0],e=[1]},{c=[1],b=[0]}]}
	 * 
	 * @param r
	 * @return
	 */
	public Map<String, List<Set<Entry<String, Integer>>>> idbSchemaFromEdbSchema() {
		Map<String, List<Set<Entry<String, Integer>>>> idbSchemaFromEdbSchema = new HashMap<String, List<Set<Entry<String, Integer>>>>();
		Map<Rule, List<RuleEdge>> ruleBody2RulesBodyMappingPositions = this.ruleBody2RulesBodyMappingPositions();
		for (Rule r : this.model.getRules()) {
			String head = r.getHead().get(0).getName();
			Integer headVarSize = r.getHead().get(0).getArguments().size();
			if (!idbSchemaFromEdbSchema.containsKey(head)) {
				idbSchemaFromEdbSchema.put(head, new ArrayList<Set<Entry<String, Integer>>>());
				// We allocate an empty HashSet for each variable in the head
				for (int i = 0; i < headVarSize; i++) {
					idbSchemaFromEdbSchema.get(head).add(new HashSet<Map.Entry<String, Integer>>());
				}
			}
			/*
			 * [ [{"own":0}], [{"own":2},{"own":1}], [] ]
			 */
			List<Set<Entry<String, Integer>>> headPositionsToEdbPositions = idbSchemaFromEdbSchema.get(head);
			/*
			 * since ruleBody2RulesBodyMappingPositions contains key (Rule) having idb in
			 * the body, with the following check we understand that the current rule is a
			 * rule containing idb in its body. That is, there is a rule to from which the
			 * current rule can get propagations
			 */
			if (ruleBody2RulesBodyMappingPositions.containsKey(r)) {
				// It is a rule depending by another rule
				List<RuleEdge> dependsByRules = ruleBody2RulesBodyMappingPositions.get(r);
				for (RuleEdge dependsByRule : dependsByRules) {
					Rule rTo = dependsByRule.getRTo();
					/*
					 * Among the rules it depends by, we want only exit rules, since they have edb
					 * in the body
					 */
					if (this.modelPWLWardedDecorator.isExitRule(rTo)) {
						Map<String, List<AbstractMap.SimpleEntry<Integer, Integer>>> rFromHeadPositions2rToBodyPositionsOfInterestedOperand = dependsByRule
								.rFromHeadPositions2rToBodyPositionsOfInterestedOperand();
						for (Map.Entry<String, List<AbstractMap.SimpleEntry<Integer, Integer>>> entry : rFromHeadPositions2rToBodyPositionsOfInterestedOperand
								.entrySet()) {
							String bodyAtomName = entry.getKey();
							List<AbstractMap.SimpleEntry<Integer, Integer>> headToBodyMappingPositions = entry
									.getValue();
							for (AbstractMap.SimpleEntry<Integer, Integer> headToBodyMappingPosition : headToBodyMappingPositions) {
								// We have one entry per Map
								Integer headPosition = headToBodyMappingPosition.getKey();
								Integer bodyPosition = headToBodyMappingPosition.getValue();

								// We get the body atom name to its corresponding position
								Entry<String, Integer> bodyAtomNameToBodyVarPosition = new AbstractMap.SimpleEntry<String, Integer>(
										bodyAtomName, bodyPosition);

								/**
								 * FIXME here aggregations have -1. For now we do this operation
								 */
								if (headPosition < 0) {
									headPosition = headPosition * (-1);
								}
								Set<Entry<String, Integer>> bodyAtomNameToBodyVarPositions = headPositionsToEdbPositions
										.get(headPosition);
								// We populate the list with the new mapping
								bodyAtomNameToBodyVarPositions.add(bodyAtomNameToBodyVarPosition);
							}
						}
					}
				}
			}
			/*
			 * Now we handle cases when the intensional rule has at most an extensional
			 * predicate i.e.
			 */
			/*
			 * 1) path(X,Y,Z) :- path_1(X,Y), edge(Y,Z).
			 */
			/*
			 * 2) path_1(X,Y) :- edge(Y,X).
			 */
			/*
			 * 3) sameUpRegulates(C1,C2,UG) :- edge(_,"UPREGULATES_CuG",C1,UG), edge(_,"UPREGULATES_CuG",C2,UG).

			 */
			/*
			 * In the case of 1) we do not cover the propagation of extensional body. In
			 * fact for now we are missing path[2] = edge[1] It happens because we just
			 * handled rTo rules being exit rules. In fact we can understand the propagation
			 * of path_1 via path_1(X,Y) :- edge(Y,X). but we miss the propagation from
			 * ",edge(Y,Z)"
			 */
			/*
			 * So if the current rule has two body atoms and one of them is extensional, we
			 * infer the propagations from the extensional variables to the head
			 */
			this.handleRulesWithatMostOneExtensionalInTheBody(r, headPositionsToEdbPositions);
		}

		return idbSchemaFromEdbSchema;
	}

	private void handleRulesWithatMostOneExtensionalInTheBody(Rule r,
			List<Set<Entry<String, Integer>>> headPositionsToEdbPositions) {
		/*
		 * Here we are processing a rule having only edb in its body
		 */
		List<Term> headVariables = r.getHead().get(0).getArguments();
		List<Literal> body = r.getBody();
		for (Literal l : body) {
			// for each literal in the body we create and add the map
			String lName = l.getAtom().getName();
			/*
			 * If it is not extensional there is a ruleTo, so it is handled above when
			 * analyzing intensional propagations
			 */
			if (!this.modelPWLWardedDecorator.isExtensional(lName)) {
				continue;
			}

			// for each head variable we create a pos2pos mappings
			List<Term> lVariables = l.getAtom().getArguments();
			int hvariablePos = 0;
			for (Term hvariable : headVariables) {
				int lvariablePos = 0;
				for (Term lVariable : lVariables) {
					// We found a var2var mapping
					if (hvariable.equals(lVariable)) {

						// We create the body atom name to its corresponding position
						Entry<String, Integer> bodyAtomNameToBodyVarPosition = new AbstractMap.SimpleEntry<>(lName,
								lvariablePos);

						Set<Entry<String, Integer>> bodyAtomNameToBodyVarPositions = headPositionsToEdbPositions
								.get(hvariablePos);
						// We populate the list with the new mapping
						bodyAtomNameToBodyVarPositions.add(bodyAtomNameToBodyVarPosition);
					}
					lvariablePos++;
				}
				hvariablePos++;
			}
		}
	}

	/**
	 * This method computes the mapping of the position of an IDB to the position of
	 * an EDB As an example:
	 * 
	 * b(X,Y) :- c(X,Y). a(Z,Y,X) :- b(Y,X), e(X,Z).
	 * 
	 * returns {b[{c[{0,0},{1,1}}], a[{c[{1,0},{2,1}],e[{0,1}]} /**
	 * {a=[{h=[1],b=[0]},{h=[0],d=[1]}]}
	 * 
	 * @param r
	 * @return
	 */
	public Map<String, List<Map<String, List<AbstractMap.SimpleEntry<Integer, Integer>>>>> idbAtomsVarPositionsToEdbAtomVarPositions() {
		Map<String, List<Map<String, List<AbstractMap.SimpleEntry<Integer, Integer>>>>> idbAtomsVarPositionsToEdbAtomVarPositions = new HashMap<>();
		Map<Rule, List<RuleEdge>> ruleBody2RulesBodyMappingPositions = this.ruleBody2RulesBodyMappingPositions();

		// iterate over the rules and find the mappings from the head to the body
		for (Rule r : this.model.getRules()) {
			String head = r.getHead().get(0).getName();
			// we know the head, now initialize the list of mappings
			List<Map<String, List<AbstractMap.SimpleEntry<Integer, Integer>>>> idbAtomVarPositionsToEdbAtomVarPositions = new ArrayList<>();
			idbAtomsVarPositionsToEdbAtomVarPositions.put(head, idbAtomVarPositionsToEdbAtomVarPositions);
			// if it does not contain the rule it has a rule reading from an EDB
			if (!ruleBody2RulesBodyMappingPositions.containsKey(r)) {
				List<Term> headVariables = r.getHead().get(0).getArguments();
				List<Literal> body = r.getBody();
				for (Literal l : body) {
					// for each literal in the body we create and add the map
					Map<String, List<AbstractMap.SimpleEntry<Integer, Integer>>> lMapping = new HashMap<String, List<AbstractMap.SimpleEntry<Integer, Integer>>>();
					idbAtomVarPositionsToEdbAtomVarPositions.add(lMapping);
					String lName = l.getAtom().getName();
					// for each head variable we create a pos2pos mappings
					List<AbstractMap.SimpleEntry<Integer, Integer>> pos2posMappings = new ArrayList<>();
					lMapping.put(lName, pos2posMappings);
					List<Term> lVariables = l.getAtom().getArguments();
					int hvariablePos = 0;
					for (Term hvariable : headVariables) {
						int lvariablePos = 0;
						for (Term lVariable : lVariables) {
							if (hvariable.equals(lVariable)) {
								AbstractMap.SimpleEntry<Integer, Integer> pos2PosMapping = new AbstractMap.SimpleEntry<Integer, Integer>(
										hvariablePos, lvariablePos);
								pos2posMappings.add(pos2PosMapping);
								// we found the mapping, we can iterate the next head variable
								break;
							}
							lvariablePos++;
						}
						hvariablePos++;
					}
				}
			}
			// It is a rule that depends by another rule
			else {
				List<RuleEdge> dependsByRules = ruleBody2RulesBodyMappingPositions.get(r);
				for (RuleEdge dependsByRule : dependsByRules) {
					Rule rTo = dependsByRule.getRTo();
					if (this.modelPWLWardedDecorator.isExitRule(rTo)) {
						Map<String, List<AbstractMap.SimpleEntry<Integer, Integer>>> rFromHeadPositions2rToBodyPositionsOfInterestedOperand = dependsByRule
								.rFromHeadPositions2rToBodyPositionsOfInterestedOperand();
						idbAtomVarPositionsToEdbAtomVarPositions
								.add(rFromHeadPositions2rToBodyPositionsOfInterestedOperand);
					}
				}
			}
		}

		return idbAtomsVarPositionsToEdbAtomVarPositions;
	}
}

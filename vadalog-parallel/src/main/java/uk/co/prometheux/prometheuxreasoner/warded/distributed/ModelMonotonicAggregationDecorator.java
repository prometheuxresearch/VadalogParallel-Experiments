package uk.co.prometheux.prometheuxreasoner.warded.distributed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import uk.co.prometheux.prometheuxreasoner.common.IdGenerator;
import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.ComparisonOperatorsEnum;
import uk.co.prometheux.prometheuxreasoner.model.Condition;
import uk.co.prometheux.prometheuxreasoner.model.Constant;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Term;
import uk.co.prometheux.prometheuxreasoner.model.Variable;
import uk.co.prometheux.prometheuxreasoner.model.expressions.AggregateOperator;
import uk.co.prometheux.prometheuxreasoner.model.expressions.Expression;
import uk.co.prometheux.prometheuxreasoner.warded.Position;
import uk.co.prometheux.prometheuxreasoner.warded.RuleWardedDecorator;
import uk.co.prometheux.prometheuxreasoner.warded.distributed.ruleTraversal.Rule2RuleSchemaMapping;
import uk.co.prometheux.prometheuxreasoner.warded.distributed.ruleTraversal.RuleEdge;

/**
 * This Class adheres to the pattern Decorator. It is a decorator for a Model
 * which contains monotonic aggregator rules
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

public class ModelMonotonicAggregationDecorator {

	/* The model this decorator is wrapping */
	private Model model;

	public ModelMonotonicAggregationDecorator(Model model) {
		this.model = model;
	}

	public Model rewriteRulesWithMonotonicAggregations() {
		Model newModel = new Model(this.model);
//		Set<List<Rule>> groupedRules = this.groupRulesByMonotonicAggregates(this.model.getRules());
//		List<List<Rule>> orderedGroupedRules = new ArrayList<>();
//		for(List<Rule> groupedRulesList : groupedRules) {
//			orderedGroupedRules.add(groupedRulesList);
//		}
//		orderedGroupedRules.sort((x,y)->{if(x.size() > y.size()) return -1; else return 1;});
//		for (List<Rule> rulesWithSameAggregate : orderedGroupedRules) {
//			List<Rule> newRules = new ArrayList<>();
//			for (Rule r : rulesWithSameAggregate) {
//				RuleMonotonicAggregationDecorator rdd = new RuleMonotonicAggregationDecorator(r);
//				newModel.getRules().remove(r);
//				if (newRules.isEmpty()) {
//					newRules.addAll(rdd.rewriteSingleRuleWithAggregate());
//				} else {
//					String newHeadName = newRules.get(0).getSingleHead().getName();
//					newRules.add(rdd.rewriteRuleRemovingAggregation(newHeadName));
//				}
//			}
//			newModel.getRules().addAll(newRules);
//		}
		return newModel;
	}
	
	

	/**
	 * This method rewrites contributors by adding new rules that act as msum/mprod and mmax/mmin 
	 * @param newModel
	 * @return
	 */
	public Model rewriteRecursiveRulesWithContributors(Model m) {
		// for now we have only SN/N approach
		boolean snN = true;
		if(snN) {
			return this.rewriteRecursiveRulesWithContributorsSemiNaiveNaive(m);
		}
		return this.rewriteRecursiveRulesWithContributorsSemiNaive(m);
	}

	private Model rewriteRecursiveRulesWithContributorsSemiNaive(Model m) {
		return null;
	}

	/**
	 * This method rewrites the recursive aggregation rules with contribution 
	 * adopting the Seminaive/naive aggregate evaluation rewriting
	 * that is, the delta of the new mmax/mmin will have the best values among result + delta
	 * and the msum/mprod with calculate the aggreagate by summing the whole delta and getting the
	 * max/min from its new delta row and the old result row
	 * @param m the model
	 * @return a new model where aggregations with contributors are rewritten into two new rules
	 * msum + mmax / mprod + mmin
	 */
	/**
	 * This method rewrites the recursive aggregation rules with contribution 
	 * adopting the Seminaive/naive aggregate evaluation rewriting
	 * that is, the delta of the new mmax/mmin will have the best values among result + delta
	 * and the msum/mprod with calculate the aggreagate by summing the whole delta and getting the
	 * max/min from its new delta row and the old result row
	 * @param m the model
	 * @return a new model where aggregations with contributors are rewritten into two new rules
	 * msum + mmax / mprod + mmin
	 */
	private Model rewriteRecursiveRulesWithContributorsSemiNaiveNaive(Model m) {
		List<Rule> oldRules = new ArrayList<>(m.getRules());
		Model newModel = new Model(m);

		ModelDistributedDecorator mdd = new ModelDistributedDecorator(m);
		for (Rule r : oldRules) {
			// we are interested to convert only recursive aggregation rules with contribution
			if (mdd.isRecursive(r)) {
				this.rewriteContributions(r, newModel);
			}
		}
		return newModel;

	}

	private void rewriteContributions(Rule r, Model oldModel) {
		Map<Set<Variable>, List<Condition>> contributionsToConditionsPerRule = this.contributionsToConditionsPerRule(r);
		if (!contributionsToConditionsPerRule.isEmpty()) {
			// we can remove the old rule from the model, we will rewrite it
			oldModel.removeRule(r);
			this.replaceOldRuleWithContributionsWithNewRulesWithoutContributions(r, contributionsToConditionsPerRule.entrySet(), oldModel);
		}
	}
	
	
	private void replaceOldRuleWithContributionsWithNewRulesWithoutContributions(Rule r, Set<Entry<Set<Variable>, List<Condition>>> contributionsToConditionsPerRule, Model oldModel) {
		// we maintain a list of the newly created rules
//		List<Rule> newlyCreatedRules = new ArrayList<>();
//		for(Entry<Set<Variable>, List<Condition>> contributionsToConditions : contributionsToConditionsPerRule) {
//			// in case the old rule have other conditions without contributions
//			List<Condition> allOldConditions = new ArrayList<>(r.getConditions());
//			List<Condition> oldAggregationsWithContibutions = contributionsToConditions.getValue();
//			// we remove from all the old conditions, the aggregations with contributions
//			allOldConditions.removeAll(oldAggregationsWithContibutions);
//			// we want to create the mmax/mmin rule only one time per entry
//			boolean first = false;
//			List<Term> mmaxMminHeadArgumentsList = new ArrayList<>();
//			// we create the head of the new mmax rule
//			// vatom_10(Var_1,Var_2,Var_3,Var_4,Var_6)
//			String newMmaxMminRuleHeadName = IdGenerator.getNewAtomSymbol("vatom_");
//			// we will add the head arguments during the iterations
//			Atom mmaxMminHead = new Atom(newMmaxMminRuleHeadName, mmaxMminHeadArgumentsList);
//			// we create the body of the mmax, which is the same of the old msum with contributors
//			// vatom_3(Var_1,Var_2,Var_3,Var_4)
//			// we get the body of the old rule msum/mprod with contributions
//			// and we will substitute the aggregation variable with the new mmax/mmin according 
//			// aggregation variable
//			Literal oldruleBody = r.getBody().get(0);
//			Literal mmaxMminBody = new Literal(oldruleBody);
//			List<Variable> mmaxMminGroupByVariables = new ArrayList<>();
//			List<Condition> newMmaxMMinConditions = new ArrayList<>();
//			List<Condition> newMsumMprodConditions = new ArrayList<>();
//			// the new conditions will be all the old conditions without aggregations with contributions
//			// and all the new msum/mprod conditions, we will addAll them later
//			List<Condition> allNewConditions = new ArrayList<>();
////			allNewConditions.addAll(allOldConditions);
//			// we add the variables of the conditions (other aggregations without contributions)
//			// we do this process only the first time
//			
//			// we want also modify the old conditions in the msum/mprod rule
//			/*
//			 * vatom_8(Var_1,Var_2,Var_5,Var_7,Var_8) :- vatom_7(Var_1,Var_2,Var_3,Var_4,Var_5), Var_7=Var_4, Var_8=mmax(Var_3).
//			 * vatom_10(Var_1,Var_2,Var_6,Var_7) :- vatom_8(Var_1,Var_2,Var_5,Var_4,Var_8), Var_7=munion(Var_4), Var_6=msum(Var_8).
//			 */
//			// for example here we want Var_7 of the assignment is a new variable
//			// we create the new mmax/mmin rule
//			Rule newMmaxMminRule = new Rule(mmaxMminHead, Collections.singletonList(mmaxMminBody), newMmaxMMinConditions);
//			for(Condition c : oldAggregationsWithContibutions) {
//				AggregationExpression oldAggregationWithContribution = (AggregationExpression) c.getRhs();
//				String aggregationType = oldAggregationWithContribution.getAggregationOperator().getName();
//				if(!first) {
//				first = true;
//				// now we create the mmax/mmin rule, with a new head having groupby and contributors variables and with body, the body of the msum
//				// the head of mmax/mmin is composed of groupby and contributors variables of the original msum with contributors
//				// since contributors can be also group by variables, we deduplicate them using hashset
//				HashSet<Variable> mmaxMminGroupByVariablesSet = new HashSet<>();
//				mmaxMminGroupByVariablesSet.addAll(oldAggregationWithContribution.getGroupByVariables());
//				mmaxMminGroupByVariablesSet.addAll(oldAggregationWithContribution.getContributions());
//				// and we transform it in a list
//				mmaxMminGroupByVariables.addAll(mmaxMminGroupByVariablesSet);
//				// we need a list of terms for the head of the new mmax/mmin rule
//				mmaxMminHeadArgumentsList.addAll(mmaxMminGroupByVariables);
//
//				for(Condition oldCondition : allOldConditions) {
//					Variable lhsVariable;
//					Expression rhsExpression;
//					Condition newConditionToAdd = new Condition(oldCondition);
//					
//					/* if it is an Aggregation without contributions,
//					 * we get the expression operand and we assign it
//					 * to a new variable that will be projected in the head
//					 * example, in this case the munion is our aggregation w.o. contributions:
//					 * p(X,Z,MSum,MUnion) :- q(X,Z,Y,Q,L), MSum=msum(Q,<Y>), MUnion=munion(L|Y).
//					 * 
//					 * Becomes
//					 * q1(X,Z,Y,NewVar,MMax) :- q1(X,Z,Y,Q,L), MMax=mmax(Q), NewVar = L|Y.
//					 * p(X,Z,MSum,MUnion) :- q1(X,Z,Y,NewVar,Q), MSum=msum(Q), MUnion=munion(NewVar).
//					 */
//					
//					if(oldCondition.getRhs() instanceof AggregationExpression) {
//						// we create the new variable that will be the assignment of the operand expression
//						String newConditionVariableName = IdGenerator.getNewVariableSymbol(r);
//						lhsVariable = new Variable(newConditionVariableName);
//						AggregationExpression oldRhsConditionAggregation = (AggregationExpression)oldCondition.getRhs();
//						rhsExpression = oldRhsConditionAggregation.getOperand();
//						// we create a new condition for the expression of the aggregation
//						// we override the new condition
//						newConditionToAdd = new Condition(lhsVariable, ComparisonOperatorsEnum.EQ, rhsExpression);
//						// and we remove this aggregation condition from all the new conditions
//						// in our example we convert MUnion=munion(L|Y) to MUnion=munion(NewVar)
//						allNewConditions.remove(oldCondition);
//						// and we create a new aggregation that will aggregate on the
//						// assignment created just above (newConditionToAdd)
//						AggregationExpression newAggregationExpression = new AggregationExpression
//											(oldRhsConditionAggregation.getAggregationOperator(),
//											lhsVariable, 
//											oldRhsConditionAggregation.getContributions(),
//											oldRhsConditionAggregation.getGroupByVariables());
//						newAggregationExpression.setHeadPredicateAndPosition(oldRhsConditionAggregation.getHeadPredicate(), oldRhsConditionAggregation.getHeadPosition());
//						allNewConditions.add(new Condition(oldCondition.getLhs(), ComparisonOperatorsEnum.EQ, newAggregationExpression));
//					}
//					// else it is an assignment for an expression and we simply add it as it is
//					// so we add the assigment in the head of the new mmmax/mmin rule
//					// and the condition in its body
//					newMmaxMMinConditions.add(newConditionToAdd);
//					mmaxMminHeadArgumentsList.add(newConditionToAdd.getLhs());
//				}
//				}
//				// now we work for all aggregate conditions with contributions of this rule
//				// we create the new variable that will be the assignment of the mmax/mmin
//				String newMmaxMminVariableName = IdGenerator.getNewVariableSymbol(r);
//				Variable newMmaxMminVariable = new Variable(newMmaxMminVariableName);
//				// and we add it to the mmax/mmin head arguments, now it is the last variable in the head
//				mmaxMminHeadArgumentsList.add(newMmaxMminVariable);
//				
//				// and we create the new mmax/mmin aggregation condition
//				AggregationExpression newMmaxMminExpression = null;
//				// and we create the new msum/mprod aggregation condition
//				AggregationExpression newMsumMprodExpression = null;
//				if(aggregationType.equals("msum")){
//					// we create the mmax aggregation condition
//					newMmaxMminExpression = new AggregationExpression(AggregateOperator.MMAX, 
//																oldAggregationWithContribution.getOperand(),
//																// which is empty
//																Collections.emptyList(), 
//																mmaxMminGroupByVariables);
//					// we also create the new msum/mprod aggregation for the old rule
//					newMsumMprodExpression = new AggregationExpression(AggregateOperator.MSUM,
//							// the mmax/mmin variable, since we copied the head variables from the mmax rule, containing the newMmaxVariable
//							newMmaxMminVariable, 
//							Collections.emptyList(),
//							oldAggregationWithContribution.getGroupByVariables());
//				}
//				else if(aggregationType.equals("mprod")){
//					// we create the mmin aggregation condition
//					newMmaxMminExpression = new AggregationExpression(AggregateOperator.MMIN, 
//																oldAggregationWithContribution.getOperand(),
//																// which is empty
//																Collections.emptyList(), 
//																mmaxMminGroupByVariables);
//					// we also create the new msum/mprod aggregation for the old rule
//					newMsumMprodExpression = new AggregationExpression(AggregateOperator.MPROD,
//							// the mmax/mmin variable, since we copied the head variables from the mmax rule, containing the newMmaxVariable
//							newMmaxMminVariable, 
//							Collections.emptyList(),
//							oldAggregationWithContribution.getGroupByVariables());
//				}
//				// the name and the head predicate position, which is the last by construction
//				newMmaxMminExpression.setHeadPredicateAndPosition(newMmaxMminRuleHeadName, mmaxMminHeadArgumentsList.size() - 1);
//				Condition newMmaxMinCondition = new Condition(newMmaxMminVariable, ComparisonOperatorsEnum.EQ, newMmaxMminExpression);
//				// we add it to the new mmax/mmin conditions for the new mmax/mmin rule
//				newMmaxMMinConditions.add(newMmaxMinCondition);
//				// now we consider the msum/mprod
//				// the name and the head predicate position, from the old rule
//				newMsumMprodExpression.setHeadPredicateAndPosition(r.getSingleHead().getName(), oldAggregationWithContribution.getHeadPosition());
//				// we create the new msum aggregation condition, with the old lhs, in order to be coherent with the previous head
//				Condition newMsumMprodCondition = new Condition(c.getLhs(), ComparisonOperatorsEnum.EQ, newMsumMprodExpression);
//				// we add a new msum/mprod condition without contributions, 
//				newMsumMprodConditions.add(newMsumMprodCondition);
//			}
//			// we processed all the aggregations with contributions for this rule
//			// now we modify the body of the old rule to unify it with the head of the new mmax/mmin
//			// we substitute the body name of the old msum with the head name of the new mmax
//			Literal newMsumMprodBody = new Literal(mmaxMminHead, true);
//
//			// we create the new msum rule
//			// we have added before all old conditions
//			allNewConditions.addAll(newMsumMprodConditions);
//			Rule newMsumMprod = new Rule(r.getHead(), Collections.singletonList(newMsumMprodBody), allNewConditions);
//			newlyCreatedRules.add(newMmaxMminRule);
//			newlyCreatedRules.add(newMsumMprod);
//		}
////		// and we set these two new rules to had contributors
////		Model tmp = new Model();
////		for(Rule newlyCreatedRule : newlyCreatedRules) {
////			// we ask help to the model to create the rule features
////			tmp.readRule(newlyCreatedRule.toString());
////		}
////		
////		List<Rule> rulesFromModelTmp = tmp.getRules();
////		rulesFromModelTmp.stream().forEach(rule -> rule.setHadContributors(true));
//
////		oldModel.getRules().addAll(rulesFromModelTmp);
//		newlyCreatedRules.stream().forEach(newlyCreated -> newlyCreated.setHadContributors(true));
//		oldModel.getRules().addAll(newlyCreatedRules);	
	}

	/**
	 * This method verifies if for a single rule, the contributions of the aggregations are the same
	 * in this case we can use a single mmax/mmin with contributions and group by variables as
	 * grouping variables
	 * Otherwise we need to split the rule into n*2 new rules for each aggregation with different
	 * contributions
	 * 
	 * some cases:
	 * msum(X,<Y>), mprod(K,<Y>) have the same contributions, we can use a single mmax/mmin
	 * msum(X,<Y>), mprod(K,<Y,Y,Y>) have the same contributions, 
	 * 								 we can use a single mmax/mmin (hashset solves this cases)

	 * msum(X,<Y>), mprod(K,<X,Y>) have the different contributions, we need a 
	 * 							   new mmax rile and a new mmin rule
	 * @param r
	 * @return a map contributions variables to conditions
	 */
	private Map<Set<Variable>, List<Condition>> contributionsToConditionsPerRule(Rule r) {
		Map<Set<Variable>, List<Condition>> contributorsToConditions = new HashMap<>();
//		for(Condition c : r.getConditions()) {
//			if(c.getRhs() instanceof AggregationExpression) {
//				AggregationExpression agg = (AggregationExpression) c.getRhs();
//				if(!agg.getContributions().isEmpty()) {
//					HashSet<Variable> contributionVariables = new HashSet<>(agg.getContributions());
//					if(!contributorsToConditions.containsKey(contributionVariables)) {
//						contributorsToConditions.put(contributionVariables, new ArrayList<Condition>());
//					}
//					contributorsToConditions.get(contributionVariables).add(c);		
//				}
//			}
//		}
		return contributorsToConditions;
	}

	/**
	 * Computes a set of grouped rules where each group g contains at least two
	 * rules r1 and r2, that (i) have the same predicate in the head; (ii) have the
	 * same monotonic aggregation. Notice that, if an input rule r does not contain
	 * a monotonic aggregation or it does not match with any other rule in the list,
	 * r is not returned in the output set.
	 * 
	 * @param rules the rules to group
	 * @return a set of grouped rules where each group g contains at least two rules
	 *         r1 and r2, that (i) have the same predicate in the head; (ii)
	 *         computes the head predicate with the same monotonic aggregation.
	 */
	private Set<List<Rule>> groupRulesByMonotonicAggregates(List<Rule> rules) {
		/* The output set of groups */
		Set<List<Rule>> groupedRules = new HashSet<>();
//		for (Rule r1 : rules) {
//			/* Whether this rule contains an aggregation */
//			boolean containsAggregate = r1.getConditions().stream()
//					.anyMatch(c -> c.getRhs() instanceof AggregationExpression);
//			boolean alreadyProcessed = groupedRules.stream().anyMatch(g -> g.contains(r1));
//			/* Whether this rule has been already put in a group we do not process it */
//			if (!alreadyProcessed && containsAggregate) {
//				/* The current group to build */
//				List<Rule> sameGroup = new ArrayList<>();
//				for (Rule r2 : rules) {
//					/* Whether r2 has a positive analysis with r1 */
//					if (!r1.equals(r2) && monotonicAggregateMatchingAnalysis(r1,
//							r2) == MonotonicAggregateMatchingAnalysisResultEnum.SUCCESS) {
//						sameGroup.add(r2);
//					}
//				}
//				/*
//				 * We fill the group only if r1 has a positive matching analysis with other
//				 * group
//				 */
//				/* Otherwise we ignore the rule r1 */
//				if (!sameGroup.isEmpty()) {
//					sameGroup.add(r1);
//					groupedRules.add(sameGroup);
//				}
//			}
//		}
		return groupedRules;
	}

	/**
	 * It check whether the rules of this model with the same head predicate and
	 * that contain monotonic aggregation are coherent according to Vadalog
	 * Semantics. i.e. they use the same aggregation in the same head positions to
	 * compute the head predicate.
	 */
	public void checkMonotonicAggregationCorrectness() {
//		/* We group the rules of the model by head */
//		Map<String, Set<Rule>> headToRules = groupRulesByHeadName();
//
//		/* For each group we check if there is an head computed with an aggregation */
//		/* if so we analyze all the other rules and see if their aggregate matches */
//		for (Entry<String, Set<Rule>> entry : headToRules.entrySet()) {
//			Set<Rule> currentGroup = entry.getValue();
//			boolean hasMonotonicAggregation = currentGroup.stream().anyMatch(
//					r -> r.getConditions().stream().anyMatch(c -> c.getRhs() instanceof AggregationExpression));
//			if (hasMonotonicAggregation) {
//				for (Rule r1 : currentGroup) {
//					for (Rule r2 : currentGroup) {
//						if (!r1.equals(r2) && monotonicAggregateMatchingAnalysis(r1,
//								r2) == MonotonicAggregateMatchingAnalysisResultEnum.UNSUCCESS) {
//							throw new IllegalArgumentException(
//									"Rules " + r1 + " and " + r2 + " aggregations must be coeherent.");
//						}
//					}
//				}
//			}
//		}

	}

	/**
	 * It returns a set of rules grouped by head predicate name
	 * 
	 * @return a set of rules grouped by head predicate name
	 */
	private Map<String, Set<Rule>> groupRulesByHeadName() {
		Map<String, Set<Rule>> headToRules = new HashMap<>();
		for (Rule r : this.model.getRules()) {
			String headName = r.getSingleHead().getName();
			if (!headToRules.containsKey(headName)) {
				headToRules.put(headName, new HashSet<>());
			}
			headToRules.get(headName).add(r);
		}
		return headToRules;
	}

	/**
	 * Perform an analysis to check whether two rules compute the same head with the
	 * same aggregations. The analysis has a success if: - the rules have the same
	 * aggregations; - the rules have the same positions for the group by variables;
	 * - the rules have the same positions for the same computed aggregation; - the
	 * matching aggregations have the same number of contributors;
	 * 
	 * @param r1 a Vadalog rule
	 * @param r2 a Vadalog rule
	 * @return SUCCESS if the rules computes the head predicate with the same
	 *         aggregation otherwise UNSUCCESS.
	 */
	public MonotonicAggregateMatchingAnalysisResultEnum monotonicAggregateMatchingAnalysis(Rule r1, Rule r2) {

		return MonotonicAggregateMatchingAnalysisResultEnum.SUCCESS;
	}

	// the result of the aggregation analysis
	public enum MonotonicAggregateMatchingAnalysisResultEnum {
		SUCCESS, UNSUCCESS;
	}
	
	/**
	 * Given two rules, rule r1 and rule r2, this method checks whether the
	 * variables involved in conditions of r2 are propagated to aggregate variables
	 * involved in the monotonic aggregation expressions of r1
	 * 
	 * @param r1 the first rule
	 * @param r2 the second rule
	 * @return true if the in conditions variables of r2 are propagated to the in
	 *         mAggr variables of r1
	 */
	public boolean r1MAggregationsAreConditionsInr2(Rule r1, Rule r2) {
		// if r2 has no conditions
		if (r2.getConditions().isEmpty()) {
			return false;
		}

		// we collect the aggregate expressions of r1
//		List<AggregationExpression> r1AggregateExpressions = new ArrayList<>();
//		for (Condition r1Cond : r1.getConditions()) {
//			Expression rhsExpr = r1Cond.getRhs();
//			if (rhsExpr instanceof AggregationExpression) {
//				r1AggregateExpressions.add((AggregationExpression) rhsExpr);
//			}
//
//		}
		// if r1 has no aggregations
//		if (r1AggregateExpressions.isEmpty()) {
//			return false;
//		}
		
//		Rule2RuleSchemaMapping rule2RuleMapping = new Rule2RuleSchemaMapping(this.model);
//		Map<Rule, List<RuleEdge>> ruleBody2RulesBodyMappingPositions = rule2RuleMapping.ruleBody2RulesBodyMappingPositions();
//		// if r1 is not present in the model
//		if (!ruleBody2RulesBodyMappingPositions.containsKey(r1)) {
//			return false;
//		}
//		List<RuleEdge> r2ToMappingPosWithr1 = ruleBody2RulesBodyMappingPositions.get(r1);
//		// we collect all the possible reachability mappings from r1 to r2
//		List<List<List<Integer>>> r1r2ReachabilityMappings = new ArrayList<List<List<Integer>>>();
//		for (RuleEdge r1r2 : r2ToMappingPosWithr1) {
//			if (r1r2.getRTo().equals(r2)) {
//				r1r2ReachabilityMappings.add(r1r2.getrFromBodyrToBodyMappings());
//			}
//		}
//		// if r1 does not read directly or indirectly from r2
//		if (r1r2ReachabilityMappings.isEmpty()) {
//			return false;
//		}
//
//		// we iterate over the r2 variables positions that are propagated to r1
//		// variables positions
//		for (List<List<Integer>> r2MappingPosWithr1 : r1r2ReachabilityMappings) {
//
//			// we get all the r1 positions where the variables in all the aggregation
//			// expression appear
//			List<Integer> r1InterestedAggregatePositions = new RuleMonotonicAggregationDecorator(r1)
//					.getVarPositionsInAllAggregateExprs();
//
//			// we get all the r2 positions where the variables in all the conditions appear
//			List<Integer> r2InterestedAggregatePositions = new ArrayList<Integer>();
//			Set<Variable> r2InterestedConditionPositions = r2.getConditionsVariables();
//			for (Variable v : r2InterestedConditionPositions) {
//				r2InterestedAggregatePositions.add(r2.getBodyVariablesList().indexOf(v));
//			}
//			/*
//			 * we now get the positions of the variables present in the conditions of r2
//			 * that are propagated to the interested aggregate positions of r1
//			 */
//			// we iterate over the positions of the r1 positions where the variables in all
//			// the aggregation expression appear
//			for (Integer r1InterestedAggregatePos : r1InterestedAggregatePositions) {
//				/*
//				 * we get the positions of r2 that are propagated to the current r1 interested
//				 * aggregate position
//				 */
//				List<Integer> r2PosTor1AggrPos = r2MappingPosWithr1.get(r1InterestedAggregatePos);
//				for (Integer r2InterestedAggregatePosition : r2InterestedAggregatePositions) {
//					/*
//					 * if r1 positions that refers to a variable that appears in the aggregate
//					 * expression correspond to the propagated r2 positions that refers to a
//					 * variable present in a condition
//					 */
//					if (r2PosTor1AggrPos.contains(r2InterestedAggregatePosition)) {
//						return true;
//					}
//				}
//			}
//		}
//		return false;
		return true;
	}

	// the detailed result of this analysis
	public class MonotonicAggregateMatchingAnalysisResult {
		private boolean matchingGroupByPositions = false;
		private boolean matchingExpression = false;
		private boolean matchingContributionsSize = false;
		private boolean matchingAggregateHeadPosition = false;

		public MonotonicAggregateMatchingAnalysisResult(boolean matchingGroupByPositions, boolean matchingExpression,
				boolean matchingContributionsSize, boolean matchingAggregateHeadPosition) {
			this.matchingGroupByPositions = matchingGroupByPositions;
			this.matchingExpression = matchingExpression;
			this.matchingContributionsSize = matchingContributionsSize;
			this.matchingAggregateHeadPosition = matchingAggregateHeadPosition;
		}

		public MonotonicAggregateMatchingAnalysisResultEnum getResult() {
			boolean isSuccess = matchingGroupByPositions && matchingExpression && matchingContributionsSize
					&& matchingAggregateHeadPosition;
			return isSuccess ? MonotonicAggregateMatchingAnalysisResultEnum.SUCCESS
					: MonotonicAggregateMatchingAnalysisResultEnum.UNSUCCESS;
		}

		public boolean isMatchingGroupByPositions() {
			return matchingGroupByPositions;
		}

		public boolean isMatchingExpression() {
			return matchingExpression;
		}

		public boolean isMatchingContributionsSize() {
			return matchingContributionsSize;
		}

		public boolean isMatchingAggregateHeadPosition() {
			return matchingAggregateHeadPosition;
		}

	}

	/**
	 * The model this decorator is wrapping
	 * 
	 * @return a model
	 */
	public Model getModel() {
		return this.model;
	}

}

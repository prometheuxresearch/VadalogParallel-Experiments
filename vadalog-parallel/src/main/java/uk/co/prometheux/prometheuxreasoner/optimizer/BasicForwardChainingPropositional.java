package uk.co.prometheux.prometheuxreasoner.optimizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Forward chaining used for the Program Evaluation Optimizer
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class BasicForwardChainingPropositional<TProposition, TRule> {

	private final Function<TProposition, String> getPropositionLabel;
	private final Function<TRule, Set<TProposition>> getRuleHead;
	private final Function<TRule, Set<TProposition>> getRuleBody;

	private class Proposition {
		private final String label;

		public String getLabel() {
			return label;
		}

		private final TProposition origProposition;

		public TProposition getOrigProposition() {
			return origProposition;
		}

		public Proposition(TProposition origProposition) {
			this.origProposition = origProposition;
			this.label = getPropositionLabel.apply(origProposition);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((label == null) ? 0 : label.hashCode());
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
			@SuppressWarnings("unchecked")
			Proposition other = (Proposition) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (label == null) {
				if (other.label != null)
					return false;
			} else if (!label.equals(other.label))
				return false;
			return true;
		}

		@SuppressWarnings("rawtypes")
		private BasicForwardChainingPropositional getOuterType() {
			return BasicForwardChainingPropositional.this;
		}
	}

	private class PRule {
		private final Set<Proposition> head;

		public Set<Proposition> getHead() {
			return head;
		}

		private final Set<Proposition> body;

		public Set<Proposition> getBody() {
			return body;
		}

		private final TRule origRule;

		public TRule getOrigRule() {
			return origRule;
		}

		public PRule(TRule origRule) {
			this.head = getRuleHead.apply(origRule).stream().map(origProposition -> new Proposition(origProposition))
					.collect(Collectors.toSet());
			this.body = getRuleBody.apply(origRule).stream().map(origProposition -> new Proposition(origProposition))
					.collect(Collectors.toSet());
			this.origRule = origRule;
		}

		public boolean isFactInBody(Proposition fact) {
			return body.contains(fact);
		}

		public boolean removeFactFromBody(Proposition fact) {
			return body.remove(fact);
		}

		public boolean isValidHasBody() {
			return body.size() > 0;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((body == null) ? 0 : body.hashCode());
			result = prime * result + ((head == null) ? 0 : head.hashCode());
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
			@SuppressWarnings("unchecked")
			PRule other = (PRule) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (body == null) {
				if (other.body != null)
					return false;
			} else if (!body.equals(other.body))
				return false;
			if (head == null) {
				if (other.head != null)
					return false;
			} else if (!head.equals(other.head))
				return false;
			return true;
		}

		@SuppressWarnings("rawtypes")
		private BasicForwardChainingPropositional getOuterType() {
			return BasicForwardChainingPropositional.this;
		}
	}

	/**
	 * Result of applying the forward chaining algorithm
	 */
	public class Result {

		private void initPropositions(Collection<Proposition> propositionsFrom,
				Map<String, Set<TProposition>> propositionsTo) {
			for (Proposition prop : propositionsFrom) {
				Set<TProposition> props = propositionsTo.get(prop.getLabel());
				if (props == null) {
					props = new HashSet<>();
					propositionsTo.put(prop.getLabel(), props);
				}
				props.add(prop.getOrigProposition());
			}
		}

		/**
		 * A rule which remains after applying the forward chaining
		 */
		public class RemainingRule {
			private final Map<String, Set<TProposition>> headPropositions;

			public Map<String, Set<TProposition>> getHeadPropositions() {
				return headPropositions;
			}

			private final Map<String, Set<TProposition>> bodyPropositions;

			public Map<String, Set<TProposition>> getBodyPropositions() {
				return bodyPropositions;
			}

			private final TRule origRule;

			public TRule getOrigRule() {
				return origRule;
			}

			public RemainingRule(PRule rule) {
				headPropositions = new HashMap<>();
				initPropositions(rule.getHead(), headPropositions);
				bodyPropositions = new HashMap<>();
				initPropositions(rule.getBody(), bodyPropositions);
				origRule = rule.getOrigRule();
			}
		}

		private final Map<String, Set<TProposition>> inferences;

		/**
		 * @return map between inferred propositonal atom name and associated
		 *         propositions from the original rules.
		 */
		public Map<String, Set<TProposition>> getInferences() {
			return inferences;
		}

		private final List<RemainingRule> rules;

		/**
		 * @return remaining rules after applying the forward chaining algorithm
		 */
		public List<RemainingRule> getRules() {
			return rules;
		}

		Result(final Collection<Proposition> inferences, final Collection<PRule> rules) {

			this.inferences = new HashMap<>();
			initPropositions(inferences, this.inferences);
			this.rules = rules.stream().map(rule -> new RemainingRule(rule)).collect(Collectors.toList());
		}
	}

	private final Collection<TProposition> origFacts;
	private final Collection<TRule> origRules;

	/**
	 * @param facts
	 * @param rules
	 * @param getPropositionLabel a function to get label from the propositional
	 *                            atom
	 * @param getRuleHead         a function to get the head from the rule
	 * @param getRuleBody         a function to geth the body from the rule
	 */
	public BasicForwardChainingPropositional(Collection<TProposition> facts, Collection<TRule> rules,
			Function<TProposition, String> getPropositionLabel, Function<TRule, Set<TProposition>> getRuleHead,
			Function<TRule, Set<TProposition>> getRuleBody) {
		origFacts = facts;
		origRules = rules;
		this.getPropositionLabel = getPropositionLabel;
		this.getRuleHead = getRuleHead;
		this.getRuleBody = getRuleBody;
	}

	/**
	 * Execute the forward chaining algorithm.
	 * 
	 * @return Result contains inferences and the state of the rules after applying
	 *         the forward chaining algorithm.
	 */
	public Result execute() {

		List<Proposition> factsAndInferencesToProcess = origFacts.stream().map(atom -> new Proposition(atom))
				.collect(Collectors.toList());
		Set<Proposition> allFactsAndInferencesUnique = new HashSet<>(factsAndInferencesToProcess);
		List<Proposition> allInferences = new LinkedList<>();
		List<PRule> rules = origRules.stream().map(rule -> new PRule(rule)).collect(Collectors.toList());

		while (factsAndInferencesToProcess.size() > 0) { // while we still have facts
			Proposition f = factsAndInferencesToProcess.remove(0); // take one fact
			for (PRule r : rules) {
				if (r.isFactInBody(f)) { // if the fact is in the rule's antecedent
					r.removeFactFromBody(f); // remove this fact from the antecedent
					if (!r.isValidHasBody()) { // if the antecedent (body) is empty
						Collection<Proposition> head = r.getHead();
						for (Proposition prop : head) { // add the head into the list of facts
							if (!allFactsAndInferencesUnique.contains(prop)) {
								factsAndInferencesToProcess.add(prop);
								allFactsAndInferencesUnique.add(prop);
							}
						}
						allInferences.addAll(head);
					}
				}
			}
			// remove all rules which are without their antecedents
			rules.removeIf(rule -> !rule.isValidHasBody());
		}
		return new Result(allInferences, rules);
	}

}

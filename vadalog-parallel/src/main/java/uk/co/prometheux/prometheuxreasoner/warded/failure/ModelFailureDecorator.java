package uk.co.prometheux.prometheuxreasoner.warded.failure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Term;

/**
 * This decorators wraps a model that contains special costrains called Failure
 * rules. e.g. #F :- p(X,X).
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class ModelFailureDecorator {
	private Model model;

	public ModelFailureDecorator(Model model) {
		this.model = model;
	}

	/**
	 * It checks whether the model contains a negation as failure.
	 * 
	 * @return true if the model contains a negation rule.
	 */
	public boolean containsNegationAsFailure() {
		return this.model.getRules().stream().anyMatch(r -> r.isNegationAsFailure());
	}

	/**
	 * It rewrites the negation rules by splitting them in two rules keeping all the
	 * arguments of the body of the original rule. e.g. #F :- p(X,Y), p(X,2).
	 * becomes failBody_1(X,Y,X,2) :- p(X,Y), p(X,2). #F :- failBody_1(X,Y,X,2).
	 * 
	 * @return a new model with the fail rules rewritten.
	 */
	public Model negatedRulesRewriting() {
		Model m = new Model(this.model);
		m.getRules().clear();
		Integer index = 0;
		for (Rule r : this.model.getRules()) {
			if (r.isNegationAsFailure()) {
				index++;
				List<Rule> rules = new ArrayList<>();
				List<Term> bodyTerms = new ArrayList<>();
				for (Atom a : r.getBodyAtomsList()) {
					bodyTerms.addAll(a.getArguments());
				}
				Atom newHead = new Atom("failBody_" + index, bodyTerms);
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
	 * It removes the Failure rules from the model.
	 * 
	 * @return the list of rules that have been removed.
	 */
	public List<Rule> removeFailures() {
		List<Rule> failureRules = this.model.getRules().stream().filter(r -> r.isNegationAsFailure())
				.collect(Collectors.toList());
		for (Rule r : failureRules) {
			this.model.removeRule(r);
		}
		return failureRules;
	}

}

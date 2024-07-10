package uk.co.prometheux.prometheuxreasoner.datalog;

import uk.co.prometheux.prometheuxreasoner.model.Rule;

/**
 * It decorates a rule, adding functionalities to deal with Datalog rules.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 */
public class RuleDatalogDecorator {

	private Rule rule;

	public RuleDatalogDecorator(Rule rule) {
		this.rule = rule;
	}

	/**
	 * Returns whether a Rule is Datalog, based on the presence of existential
	 * quantifiers
	 * 
	 * @return whether a rule is Datalog or not
	 */
	public boolean isDatalog() {
		return rule.getExistentiallyQuantifiedVariables().size() == 0;
	}

}
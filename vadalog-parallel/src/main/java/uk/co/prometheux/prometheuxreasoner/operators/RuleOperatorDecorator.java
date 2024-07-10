package uk.co.prometheux.prometheuxreasoner.operators;

import uk.co.prometheux.prometheuxreasoner.model.Rule;

/**
 * It enriches a Rule with the possibility to call operators.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class RuleOperatorDecorator {

	private Rule r;

	public RuleOperatorDecorator(Rule r) {
		this.r = r;
	}

	/**
	 * Returns an object to calculate a composition on this rule
	 * 
	 * @return a Composition object
	 */
	public Composition compose() {
		return new Composition(this.r);
	}

	public Folding fold() {
		return new Folding(this.r);
	}

	/**
	 * It returns whether there is a homomorphism between two rules.
	 * 
	 * @return Whether there is a homomorphism
	 */
	public HomomorphismChecker isHomomorphic() {
		return new HomomorphismChecker(this.r);
	}

	/**
	 * Returns an object to calculate the extraction of this rule
	 * 
	 * @return an object to calculate an extraction of this rule
	 */
	Extraction extract() {
		return new Extraction(this.r);
	}
}
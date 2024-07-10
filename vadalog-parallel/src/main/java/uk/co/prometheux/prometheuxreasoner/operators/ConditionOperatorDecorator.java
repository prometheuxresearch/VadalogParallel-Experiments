package uk.co.prometheux.prometheuxreasoner.operators;

import uk.co.prometheux.prometheuxreasoner.model.Condition;

/**
 * It enriches Conditions with the possibility to call operators.
 *
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
class ConditionOperatorDecorator {

	private Condition c;

	ConditionOperatorDecorator(Condition c) {
		this.c = c;
	}

	/**
	 * It returns an Object to calculate the unification of this Condition
	 * 
	 * @return A Unification Object
	 */
	Unification unify() {
		return new Unification(this.c);
	}

}

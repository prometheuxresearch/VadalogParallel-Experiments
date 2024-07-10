package uk.co.prometheux.prometheuxreasoner.operators;

import uk.co.prometheux.prometheuxreasoner.model.Atom;

/**
 * It enriches Atoms with the possibility to call operators.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class AtomOperatorDecorator {

	private Atom a;

	public AtomOperatorDecorator(Atom a) {
		this.a = a;
	}

	/**
	 * It returns an Object to calculate the unification of this Atom
	 * 
	 * @return A Unification Object
	 */
	public Unification unify() {
		return new Unification(this.a);
	}

}

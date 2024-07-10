package uk.co.prometheux.prometheuxreasoner.warded;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Term;
import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * A decorator for warded atoms
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class AtomWardedDecorator {

	private Atom atom;

	public AtomWardedDecorator(Atom atom) {
		this.atom = atom;
	}

	public Atom getAtom() {
		return atom;
	}

	public void setAtom(Atom atom) {
		this.atom = atom;
	}

	/**
	 * It returns all the positions in which x occurs
	 * 
	 * @param x the variable to look for
	 * @return the set of the positions x occurs in
	 */
	public Set<Position> getPositionsByVariable(Variable x) {
		List<Term> terms = this.atom.getArguments();
		Set<Position> positions = new HashSet<Position>();
		int pos = 0;

		for (Term t : terms) {
			if (t.equals(x))
				positions.add(new Position(this.atom.getName(), pos));
			pos++;
		}

		return positions;
	}

}
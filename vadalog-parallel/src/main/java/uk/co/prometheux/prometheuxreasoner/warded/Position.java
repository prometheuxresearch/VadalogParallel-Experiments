package uk.co.prometheux.prometheuxreasoner.warded;

import java.util.HashSet;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Rule;

/**
 * A position is a pair (Atom, Position) in the Atom.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class Position {

	private String atom;
	private int position;
	/* the cause for this affected position, which the Set */
	/* of rules causing this affectedness */
	private Set<Rule> causes = new HashSet<Rule>();

	public Position(String atom, int position) {
		this.atom = atom;
		this.position = position;
	}

	Position(Atom atom, int position, Rule cause) {
		this.atom = atom.getName();
		this.position = position;
		this.causes.add(cause);
	}

	Position(Atom atom, int position) {
		super();
		this.atom = atom.getName();
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getAtom() {
		return atom;
	}

	public void setAtom(Atom atom) {
		this.atom = atom.getName();
	}

	void addCause(Rule r) {
		this.causes.add(r);
	}

	void addCauses(Set<Rule> rSet) {
		this.causes.addAll(rSet);
	}

	public Set<Rule> getCauses() {
		return this.causes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atom == null) ? 0 : atom.hashCode());
		result = prime * result + position;
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
		Position other = (Position) obj;
		if (atom == null) {
			if (other.atom != null)
				return false;
		} else if (!atom.equals(other.atom))
			return false;
		if (position != other.position)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.atom + "[" + this.position + "]";
	}

}
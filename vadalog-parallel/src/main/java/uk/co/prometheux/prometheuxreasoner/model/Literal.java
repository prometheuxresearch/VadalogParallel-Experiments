package uk.co.prometheux.prometheuxreasoner.model;

import java.util.List;

/**
 * This Class represents a Literal

 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

public class Literal {

	private Atom atom;
	private boolean isPositive;
	
	public Literal(final Atom atom, final boolean isPositive) {
		super();
		this.atom = atom;
		this.isPositive = isPositive;
	}

	public Literal(Literal aLiteral) {
		this(aLiteral, false);
	}
	
	Literal(Literal aLiteral, boolean renameVariables) {
		super();
		this.atom = new Atom(aLiteral.getAtom(), renameVariables);
		this.isPositive = aLiteral.isPositive;
	}
	
	
	Literal(final String name, final List<Term> arguments, final boolean isPositive) {
		atom = new Atom(name, arguments);
		this.isPositive = isPositive;
	}


	public Atom getAtom() {
		return atom;
	}

	public void setAtom(Atom atom) {
		this.atom = atom;
	}

	public boolean isPositive() {
		return isPositive;
	}

	public void setPositive(boolean isPositive) {
		this.isPositive = isPositive;
	}

	@Override
	public String toString() {
		String s = "";
		if (!isPositive)
			s += "not ";
		s += atom.toString();
		return s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atom == null) ? 0 : atom.hashCode());
		result = prime * result + (isPositive ? 1231 : 1237);
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
		Literal other = (Literal) obj;
		if (atom == null) {
			if (other.atom != null)
				return false;
		} else if (!atom.equals(other.atom))
			return false;
		if (isPositive != other.isPositive)
			return false;
		return true;
	}

}

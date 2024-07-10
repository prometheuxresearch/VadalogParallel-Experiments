package uk.co.prometheux.prometheuxreasoner.model;

/**
 * This Class represents the signature of a predicate
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

class PredicateSignature implements Comparable<PredicateSignature> {

	private String name;
	int arity;

	PredicateSignature(String name, int arity) {
		this.name = name;
		this.arity = arity;
	}

	PredicateSignature(PredicateSignature aSignature) {
		this.name = aSignature.name;
		this.arity = aSignature.arity;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PredicateSignature) {
			PredicateSignature pred2 = (PredicateSignature) obj;
			return (name.equals(pred2.name) && arity == pred2.arity);
		} else {
			return super.equals(obj);
		}
	}

	@Override
	public int compareTo(PredicateSignature pred2) {
		int nameComparison = (int) Math.signum(name.compareTo(pred2.name));
		if (nameComparison != 0) {
			return nameComparison;
		} else {
			return Integer.compare(arity, pred2.arity);
		}
	}

	@Override
	public int hashCode() {
		return name.hashCode() + arity;
	}

	@Override
	public String toString() {
		return "(" + this.name + "," + this.arity + ")";
	}
}
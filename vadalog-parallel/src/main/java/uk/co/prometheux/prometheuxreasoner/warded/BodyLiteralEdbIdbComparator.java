package uk.co.prometheux.prometheuxreasoner.warded;

import java.util.Comparator;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Literal;


/**
 * This class compares the literals, so as to sort them according to
 * a specific criterion. An edb is always considered greater
 * than an idb.
 * 
 * Two edbs are considered the same.
 * Two idbs are considered the same, but in a linear program this should not happen as a case.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class BodyLiteralEdbIdbComparator implements Comparator<Literal> {

	/*
	 * The set of edbs. If a predicate name is in this set, then it is an edb. It is an idb otherwise
	 */
	private Set<String> edbSet;

	public BodyLiteralEdbIdbComparator(Set<String> edbSet) {
		this.edbSet = edbSet;
	}
	
	@Override
	public int compare(Literal o1, Literal o2) {
		String o1n = o1.getAtom().getName();
		String o2n = o2.getAtom().getName();
		
		if(edbSet.contains(o1n) && edbSet.contains(o2n))
			return 0;
		else if(!edbSet.contains(o1n) && !edbSet.contains(o2n))
			return 0;
		else if(!edbSet.contains(o1n) && edbSet.contains(o2n))
			return -1;
		else 
			return 1;
	}

}

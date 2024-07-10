package uk.co.prometheux.prometheuxreasoner.model;

import java.util.Comparator;

/**
 * This class compares the literals, so as to sort them according to
 * a specific criterion. The negated ones are always considered less
 * than the non-negated ones.
 * 
 * Two positive literals are considered the same.
 * Two negative literals are considered the same.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
class BodyLiteralsNegComparator implements Comparator<Literal> {

	@Override
	public int compare(Literal o1, Literal o2) {
		if(o1.isPositive() && o2.isPositive())
			return 0;
		else if(!o1.isPositive() && !o2.isPositive())
			return 0;
		else if(!o1.isPositive() && o2.isPositive())
			return 1;
		else 
			return -1;
	}

}

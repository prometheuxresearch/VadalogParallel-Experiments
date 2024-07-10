package uk.co.prometheux.prometheuxreasoner.setrdd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A type of partition for a set rdd
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 */
public class SetRDDHashSetPartition<T> extends SetRDDPartition<T> {

	private static final long serialVersionUID = 916208701103597775L;
	/* The underlying java set of this partition */
	private Set<T> set;

	public SetRDDHashSetPartition(Set<T> set, long numberOfFactsGenerated, long numberOfFactsDerived) {
		super(numberOfFactsGenerated, numberOfFactsDerived);
		this.set = set;
	}

	public SetRDDHashSetPartition(Set<T> set) {
		super();
		this.set = set;
	}

	@Override
	public long getSize() {
		/* Ask the size to the underlying set */
		return this.set.size();
	}

	@Override
	public Iterator<T> iterator() {
		/* Return the underlying set iterator */
		return this.set.iterator();
	}

	@Override
	public SetRDDPartition<T> union(SetRDDPartition<T> other) {
		SetRDDPartition<T> newPartition = null;
		/* We check whether other is another hashSet Partition */
		if (other instanceof SetRDDHashSetPartition) {
			SetRDDHashSetPartition<T> otherPart = (SetRDDHashSetPartition<T>) other;
			/* We apply the java set addAll method to implement the union of a partition */
			this.set.addAll(otherPart.set);
			newPartition = new SetRDDHashSetPartition<T>(this.set);
		} else {
			/* otherwise we use the iterator and we manually add new facts */
			newPartition = this.union(other.iterator());
		}
		return newPartition;
	}

	@Override
	public SetRDDPartition<T> union(Iterator<T> iterator) {
		Set<T> newSet = this.set;
		while (iterator.hasNext()) {
			T element = iterator.next();
			newSet.add(element);
		}
		return new SetRDDHashSetPartition<T>(newSet);
	}

	@Override
	public SetRDDPartition<T> diff(Iterator<T> iterator) {
		HashSet<T> diffSet = new HashSet<T>();
		int numFactsGenerated = 0;
		while (iterator.hasNext()) {
			T t = iterator.next();
			if (!this.set.contains(t)) {
				numFactsGenerated += 1;
				diffSet.add(t);
			}
		}
		return new SetRDDHashSetPartition<T>(diffSet, numFactsGenerated, diffSet.size());
	}

	@Override
	public List<T> collectAsList() {
		return new ArrayList<>(this.set);
	}

}

package uk.co.prometheux.prometheuxreasoner.setrdd;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a partition of a setRDD
 * 
 * @param <T> is the element of the rdd, i.e. of the partition
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
abstract class SetRDDPartition<T> implements Serializable {

	private static final long serialVersionUID = -4115418394980057285L;

	private long numberOfFactsGenerated = 0;
	private long numberOfFactsDerived = 0;

	SetRDDPartition(long numberOfFactsGenerated, long numberOfFactsDerived) {
		this.numberOfFactsGenerated = numberOfFactsGenerated;
		this.numberOfFactsDerived = numberOfFactsDerived;
	}

	public SetRDDPartition() {
	}

	/**
	 * Computes the size of the partition
	 * 
	 * @return the size of the partition
	 */
	public abstract long getSize();

	/**
	 * It returns an iterator of the element of the partition
	 * 
	 * @return an iterator of th element of the partition
	 */
	public abstract Iterator<T> iterator();

	/**
	 * It computes the union between this partition and another set partition (set
	 * union)
	 * 
	 * @param other, another set partition
	 * @return a new partition
	 */
	public abstract SetRDDPartition<T> union(SetRDDPartition<T> other);

	/**
	 * It computes the union between this partition and another partition, given an
	 * iterator
	 * 
	 * @param an iterator
	 * @return a new partition
	 */
	public abstract SetRDDPartition<T> union(Iterator<T> iterator);

	/**
	 * It computes the difference between this partition and another set partition
	 * (set difference)
	 * 
	 * @param other, another set partition
	 * @return a new partition
	 */
	SetRDDPartition<T> diff(SetRDDPartition<T> other) {
		return this.diff(other.iterator());
	}

	/**
	 * It computes the difference between this partition and another set partition
	 * (set difference)
	 * 
	 * @param other, the iterator of another partition
	 * @return a new partition
	 */
	public abstract SetRDDPartition<T> diff(Iterator<T> iterator);

	/**
	 * It returns the elements of this partition as a list
	 * 
	 * @return the list of the elements in this partition
	 */
	public abstract List<T> collectAsList();

	public long getNumberOfFactsGenerated() {
		return this.numberOfFactsGenerated;
	}

	public long getNumberOfFactsDerived() {
		return this.numberOfFactsDerived;
	}

}

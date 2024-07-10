package uk.co.prometheux.prometheuxreasoner.model.types;

import java.io.Serializable;

/**
 * It represents a MarkedNull. It is abstract, because it has to be instantiated
 * to have a specific type. The comparison, equals, is also based on the
 * specific type.
 *
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public abstract class MarkedNull implements Comparable<MarkedNull>, Serializable {

	private static final long serialVersionUID = 1L;
	private String nullName;
	
	public MarkedNull() {}

	MarkedNull(String nullName) {
		super();
		this.nullName = nullName;
	}

	public String getNullName() {
		return nullName;
	}

	public void setNullName(String nullName) {
		this.nullName = nullName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nullName == null) ? 0 : nullName.hashCode());
		return result;
	}

	@Override
	/**
	 * The single MarkedNull classes replace this.
	 */
	public abstract boolean equals(Object obj);

	/**
	 * The order between two marked nulls is just based on the name.
	 */
	public int compareTo(MarkedNull m) {
		return this.getNullName().compareTo(m.getNullName());
	}

	@Override
	public String toString() {
		return this.nullName;
	}

}

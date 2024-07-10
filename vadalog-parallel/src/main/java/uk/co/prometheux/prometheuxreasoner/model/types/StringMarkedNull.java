package uk.co.prometheux.prometheuxreasoner.model.types;

/**
 * A marked null of String type
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class StringMarkedNull extends MarkedNull {

	private static final long serialVersionUID = 1L;

	public StringMarkedNull(String nullName) {
		super(nullName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MarkedNull other = (MarkedNull) obj;
		if (super.getNullName() == null) {
			if (other.getNullName() != null)
				return false;
		} else if (!super.getNullName().equals(other.getNullName()))
			return false;
		return true;
	}

}

package uk.co.prometheux.prometheuxreasoner.model.types;

/**
 * It is a jolly marked null, not connected to a specific data type
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class JollyMarkedNull extends MarkedNull {

	private static final long serialVersionUID = 1L;

	public JollyMarkedNull() {
	    super();
	}

	public JollyMarkedNull(MarkedNull markedNull) {
		super(markedNull.getNullName());
	}

	public JollyMarkedNull(String nullName) {
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

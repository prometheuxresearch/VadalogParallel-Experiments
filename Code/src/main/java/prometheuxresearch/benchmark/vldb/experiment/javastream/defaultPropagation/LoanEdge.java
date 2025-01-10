package prometheuxresearch.benchmark.vldb.experiment.javastream.defaultPropagation;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class LoanEdge {

	private EntityNode nodeFrom;
	private EntityNode nodeTo;
	private Double weight;

	public LoanEdge(EntityNode nodeFrom, EntityNode nodeTo, Double weight) {
		super();
		this.nodeFrom = nodeFrom;
		this.nodeTo = nodeTo;
		this.weight = weight;
	}

	public EntityNode getNodeFrom() {
		return nodeFrom;
	}

	public EntityNode getNodeTo() {
		return nodeTo;
	}

	public Double getWeight() {
		return weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeFrom == null) ? 0 : nodeFrom.hashCode());
		result = prime * result + ((nodeTo == null) ? 0 : nodeTo.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
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
		LoanEdge other = (LoanEdge) obj;
		if (nodeFrom == null) {
			if (other.nodeFrom != null)
				return false;
		} else if (!nodeFrom.equals(other.nodeFrom))
			return false;
		if (nodeTo == null) {
			if (other.nodeTo != null)
				return false;
		} else if (!nodeTo.equals(other.nodeTo))
			return false;
		if (weight == null) {
			if (other.weight != null)
				return false;
		} else if (!weight.equals(other.weight))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + nodeFrom + "," + nodeTo + "," + weight + ")";
	}

}

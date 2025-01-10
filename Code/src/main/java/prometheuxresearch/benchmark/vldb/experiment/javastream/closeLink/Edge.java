package prometheuxresearch.benchmark.vldb.experiment.javastream.closeLink;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class Edge {

	private Node nodeFrom;
	private Node nodeTo;
	private Double weight;

	public Edge(Node nodeFrom, Node nodeTo, Double weight) {
		super();
		this.nodeFrom = nodeFrom;
		this.nodeTo = nodeTo;
		this.weight = weight;
	}

	public Node getNodeFrom() {
		return nodeFrom;
	}

	public Node getNodeTo() {
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
		Edge other = (Edge) obj;
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

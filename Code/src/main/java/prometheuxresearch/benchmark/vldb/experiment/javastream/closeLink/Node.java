package prometheuxresearch.benchmark.vldb.experiment.javastream.closeLink;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class Node {

	private Integer nodeId;

	private List<Edge> outwardEdges;

	private Set<Edge> outwardEdgesSet;

	public Node(Integer nodeId) {
		this.nodeId = nodeId;
		this.outwardEdges = new LinkedList<>();
		this.outwardEdgesSet = new HashSet<>();
	}

	public Integer getNodeId() {
		return this.nodeId;
	}

	public List<Node> getConnectedNodes() {
		return this.outwardEdges.stream().map(x -> x.getNodeTo()).collect(Collectors.toList());
	}

	public void addEdge(Edge outwardEdge) {
		if (!this.outwardEdgesSet.contains(outwardEdge)) {
			this.outwardEdgesSet.add(outwardEdge);
			this.outwardEdges.add(outwardEdge);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
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
		Node other = (Node) obj;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		return true;
	}

	public List<Edge> getOutwardEdges() {
		return this.outwardEdges;
	}

	@Override
	public String toString() {
		return this.nodeId.toString();
	}

}

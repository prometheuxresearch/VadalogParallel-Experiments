package prometheuxresearch.benchmark.vldb.experiment.javastream.closeLink;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class SingleSourceVisit {

	private Node nodeSource;
	private Map<Node, Double> aggregationTable;
	private Map<Entry<Node, Node>, Double> contributorsTable;

	public SingleSourceVisit(Node nodeFrom) {
		this.nodeSource = nodeFrom;
		this.aggregationTable = new HashMap<>();
		this.contributorsTable = new HashMap<>();
	}

	public List<String> visit() {
		Set<Node> visitedNodes = new HashSet<>();
		LinkedList<Edge> queue = new LinkedList<>(this.nodeSource.getOutwardEdges());
		visitedNodes.add(this.nodeSource);
		// until all the edges are visited
		while (!queue.isEmpty()) {
			// poll the first
			Edge edge = queue.poll();
			Node nodeToVisit = edge.getNodeTo();
			// if it is not already visited
			if (!this.nodeSource.equals(edge.getNodeTo()) && !visitedNodes.contains(nodeToVisit)) {
				visitedNodes.add(nodeToVisit);
				// we extract the node to visit
				Double w2 = edge.getWeight();
				Double w1;
				if (!this.aggregationTable.containsKey(edge.getNodeFrom())) {
					w1 = 1.0;
				} else {
					w1 = this.aggregationTable.get(edge.getNodeFrom());
				}
				Double w = w1 * w2;

				Entry<Node, Node> contributors = Map.entry(edge.getNodeFrom(), edge.getNodeTo());

				if (!this.aggregationTable.containsKey(nodeToVisit)) {
					this.aggregationTable.put(nodeToVisit, w);
					this.contributorsTable.put(contributors, w);
				} else {
					Double oldC = this.contributorsTable.get(contributors);
					if (oldC < w) {
						Double oldW = this.aggregationTable.get(nodeToVisit);
						Double newW = oldW - oldC + w;
						this.contributorsTable.replace(contributors, w);
						this.aggregationTable.replace(nodeToVisit, newW);
					}

				}
				for (Edge e : nodeToVisit.getOutwardEdges()) {
					queue.add(e);
				}
			}
		}
		List<String> output = new LinkedList<>();
		for (Entry<Node, Double> entry : this.aggregationTable.entrySet()) {
			output.add("(" + this.nodeSource + "," + entry.getKey() + "," + entry.getValue() + ")");
		}
		return output;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeSource == null) ? 0 : nodeSource.hashCode());
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
		SingleSourceVisit other = (SingleSourceVisit) obj;
		if (nodeSource == null) {
			if (other.nodeSource != null)
				return false;
		} else if (!nodeSource.equals(other.nodeSource))
			return false;
		return true;
	}

}

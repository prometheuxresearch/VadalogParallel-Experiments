package prometheuxresearch.benchmark.vldb.experiment.javastream.defaultPropagation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class SingleEntityDefaultPropagation {

	private EntityNode nodeSource;

	public SingleEntityDefaultPropagation(EntityNode nodeSource) {
		this.nodeSource = nodeSource;
	}

	public List<String> visit() {
		Set<EntityNode> visitedNode = new HashSet<>();
		LinkedList<EntityNode> queue = new LinkedList<>(this.nodeSource.getLoanEntity());
		queue.addAll(this.nodeSource.getSecurityEntity());
		List<String> output = new LinkedList<>();

		while (!queue.isEmpty()) {
			// poll the first
			EntityNode nodeToVisit = queue.poll();
			// if it is not already visited
			if (!visitedNode.contains(nodeToVisit)) {
				visitedNode.add(nodeToVisit);
				output.add("(" + this.nodeSource + "," + nodeToVisit + ")");
				for (EntityNode n : nodeToVisit.getLoanEntity()) {
					if (!visitedNode.contains(n)) {
						queue.add(n);
					}
				}
				for (EntityNode n : nodeToVisit.getSecurityEntity()) {
					if (!visitedNode.contains(n)) {
						queue.add(n);
					}
				}
			}

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
		SingleEntityDefaultPropagation other = (SingleEntityDefaultPropagation) obj;
		if (nodeSource == null) {
			if (other.nodeSource != null)
				return false;
		} else if (!nodeSource.equals(other.nodeSource))
			return false;
		return true;
	}

}

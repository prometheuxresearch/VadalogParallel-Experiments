package uk.co.prometheux.prometheuxreasoner.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * PredicateGraph represents the dependency graph of the program. There is an
 * edge from p to q if there is a rule that has q in the head and p in the body.
 *
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class PredicateGraph {
	private Set<String> nodes;
	private HashMap<String, List<String>> inEdges;
	private HashMap<String, List<String>> outEdges;

	public PredicateGraph() {
		nodes = new HashSet<String>();
		inEdges = new HashMap<String, List<String>>();
		outEdges = new HashMap<String, List<String>>();
	}

	public PredicateGraph(Model m) {
		this(m, false);
	}

	public PredicateGraph(Model m, boolean multiplicity) {
		HashMap<String, List<String>> inEdges = new HashMap<String, List<String>>();
		HashMap<String, List<String>> outEdges = new HashMap<String, List<String>>();
		Set<String> nodes = new HashSet<String>();
		// we assume there are no duplicate rules. this is important because
		// there is an edge for each rule.
		for (Rule rule : m.getRules()) {
			List<String> headatomsnames = rule.getHead().stream().map(x -> x.getName()).collect(Collectors.toList());
			List<String> bodyatomsnames = rule.getBodyAtomNamesList();
			nodes.addAll(headatomsnames);
			nodes.addAll(bodyatomsnames);
			for (String node : nodes) {
				if (inEdges.get(node) == null)
					inEdges.put(node, new ArrayList<String>());
				if (outEdges.get(node) == null)
					outEdges.put(node, new ArrayList<String>());
			}
			for (String headatomname : headatomsnames) {
				for (String bodyatomname : bodyatomsnames) {
					// if multiplicity is true, we can have multiple edges
					// between two nodes in case
					// one predicate depends from the other via multiple rules.
					if (multiplicity) {
						inEdges.get(headatomname).add(bodyatomname);
						outEdges.get(bodyatomname).add(headatomname);
					} else {
						if (!inEdges.get(headatomname).contains(bodyatomname))
							inEdges.get(headatomname).add(bodyatomname);
						if (!outEdges.get(bodyatomname).contains(headatomname))
							outEdges.get(bodyatomname).add(headatomname);
					}
				}
			}
		}
		this.nodes = nodes;
		this.inEdges = inEdges;
		this.outEdges = outEdges;
	}

	public Set<String> getNodes() {
		return nodes;
	}

	public HashMap<String, List<String>> getInEdges() {
		return inEdges;
	}

	public HashMap<String, List<String>> getOutEdges() {
		return outEdges;
	}

	/**
	 * Checks if target is reachable from source in the predicate graph.
	 * 
	 * @param source node
	 * @param target node
	 * @param bottomUp bottomUp if from the output to the input
	 * @return whether target is reachable from source
	 */
	public boolean isReachable(String source, String target, boolean bottomUp) {
		// each node is reachable from itself
		if (source.equals(target))
			return true;
		// if the source is not in the predicate graph, no node is reachable
		// from it (other than itself)
		if (!this.getNodes().contains(source))
			return false;
		/* we perform BFS in predicate graph starting from the source */
		HashSet<String> visited = new HashSet<String>();
		LinkedList<String> queue = new LinkedList<String>();
		visited.add(source);
		queue.add(source);
		while (queue.size() != 0) {
			String v = queue.poll();
			/* if we detect the target node, it is reachable */
			if (target.equals(v))
				return true;
			Iterator<String> i = null;
			if (bottomUp) {
				i = this.getInEdges().get(v).iterator();
			} else {
				i = this.getOutEdges().get(v).iterator();
			}			while (i.hasNext()) {
				String u = i.next();
				if (!visited.contains(u)) {
					queue.add(u);
					visited.add(u);
				}
			}
		}
		return false;
	}

}
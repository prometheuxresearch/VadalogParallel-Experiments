package uk.co.prometheux.prometheuxreasoner.pwlWarded;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.PredicateGraph;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.warded.BodyLiteralEdbIdbComparator;
import uk.co.prometheux.prometheuxreasoner.warded.RuleWardedDecorator;

/**
 * This Class is a Decorator with functionalities for PWL Warded programs
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

public class ModelPWLWardedDecorator {

	private Model model;
	private PredicateGraph pGraph;
	private Set<String> edbNameList;

	public ModelPWLWardedDecorator(Model model) {
		this.model = model;
		this.pGraph = new PredicateGraph(model, false);
		this.edbNameList = new HashSet<String>();
		for (Map.Entry<String, List<String>> entry : pGraph.getInEdges().entrySet()) {
			if (entry.getValue().isEmpty()) {
				edbNameList.add(entry.getKey());
			}
		}
	}

	/**
	 * Checks if a rule is linear recursive wrt the program, that is, if at most one
	 * body predicate is reachable from a head predicate.
	 */
	public boolean isLinearRecursive(Rule r) {
		boolean isRecursive = false;
		List<String> headatomsnames = r.getHead().stream().map(x -> x.getName()).collect(Collectors.toList());
		List<String> bodyatomsnames = r.getNamesOfPositiveLiterals();
		/*
		 * for each head predicate we perform BFS in predicate graph starting from the
		 * predicate
		 */
		for (String headatom : headatomsnames) {
			HashSet<String> visited = new HashSet<String>();
			LinkedList<String> queue = new LinkedList<String>();
			visited.add(headatom);
			queue.add(headatom);
			while (queue.size() != 0) {
				HashSet<String> intersect = new HashSet<String>(visited);
				String v = queue.poll();
				/* we make sure the rule is recursive */
				if (bodyatomsnames.contains(v))
					isRecursive = true;
				/*
				 * if we found more than 1 reachable body predicate, the rule is not linear
				 * recursive
				 */
				intersect.retainAll(bodyatomsnames);
				if (intersect.size() > 1)
					return false;
				/* explore the children */
				Iterator<String> i = pGraph.getOutEdges().get(v).iterator();
				while (i.hasNext()) {
					String u = i.next();
					if (!visited.contains(u)) {
						queue.add(u);
						visited.add(u);
					}
				}
			}
		}
		return isRecursive;
	}

	/**
	 * Checks if a rule is non linear recursive wrt the program, that is, it is
	 * recursive but not linear recursive.
	 */
	public boolean isNonLinearRecursive(Rule r) {
		return (isRecursive(r) && !isLinearRecursive(r));
	}

	/**
	 * Checks if a rule is recursive wrt the program, that is, if there is a body
	 * predicate that is reachable from a head predicate.
	 */
	public boolean isRecursive(Rule r) {
		List<String> headatomsnames = r.getHead().stream().map(x -> x.getName()).collect(Collectors.toList());
		List<String> bodyatomsnames = r.getNamesOfPositiveLiterals();
		/*
		 * for each head predicate we perform BFS in predicate graph starting from the
		 * predicate
		 */
		for (String headatom : headatomsnames) {
			HashSet<String> visited = new HashSet<String>();
			LinkedList<String> queue = new LinkedList<String>();
			visited.add(headatom);
			queue.add(headatom);
			while (queue.size() != 0) {
				String v = queue.poll();
				/* if we detect a body predicate, it is recursive */
				if (bodyatomsnames.contains(v))
					return true;
				Iterator<String> i = pGraph.getOutEdges().get(v).iterator();
				while (i.hasNext()) {
					String u = i.next();
					if (!visited.contains(u)) {
						queue.add(u);
						visited.add(u);
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the program is recursive, i.e., if the predicate graph of the
	 * program is acyclic
	 */

	public boolean isRecursive() {
		List<String> topSort = new ArrayList<String>();
		Set<String> q = new HashSet<String>();
		/* we check acyclicity of predicate graph by trying to do topological sort */

		/* first we make deep copies of adjacency lists */
		HashMap<String, List<String>> outEdges = new HashMap<String, List<String>>();
		for (Entry<String, List<String>> entry : pGraph.getOutEdges().entrySet()) {
			outEdges.put(entry.getKey(), new ArrayList<String>(entry.getValue()));
		}

		HashMap<String, List<String>> inEdges = new HashMap<String, List<String>>();
		for (Entry<String, List<String>> entry : pGraph.getInEdges().entrySet()) {
			inEdges.put(entry.getKey(), new ArrayList<String>(entry.getValue()));
		}

		for (String node : pGraph.getNodes()) {
			if (pGraph.getInEdges().get(node) == null || pGraph.getInEdges().get(node).size() == 0) {
				q.add(node);
			}
		}

		while (!q.isEmpty()) {
			String n = q.iterator().next();
			q.remove(n);
			topSort.add(n);
			Iterator<String> iterOut = outEdges.get(n).iterator();
			while (iterOut.hasNext()) {
				String m = iterOut.next();
				iterOut.remove();
				inEdges.get(m).remove(n);
				if (inEdges.get(m).isEmpty())
					q.add(m);
			}
		}
		boolean hasCycle = false;
		for (String node : pGraph.getNodes()) {
			if (!inEdges.get(node).isEmpty()) {
				hasCycle = true;
				break;
			}
		}
		return hasCycle;
	}

	/**
	 * Checks if the program is PWL-warded, i.e., if each rule is not non linear
	 * recursive
	 */
	public boolean isPWLWarded() {
		for (Rule rule : model.getRules()) {
			int recursiveWithHeadAtoms = 0;
			String headAtom = rule.getSingleHead().getName();
			for (String bodyAtom : rule.getBodyAtomNamesList()) {
				if (isMutuallyRecursiveWithTheHead(bodyAtom, headAtom)) {
					recursiveWithHeadAtoms++;
				}
				if (recursiveWithHeadAtoms > 1) {
					return false;
				}
			}
		}
		return true;

	}

	/**
	 * This method splits the multi-join rules into more rules (with 2 atom join)
	 * for a PWL WD program.
	 * 
	 * @return The new model.
	 */
	public Model multiJoinOptimization() {
		Model m = new Model(this.model);
		Comparator<Literal> edbComparator = new BodyLiteralEdbIdbComparator(edbNameList);

		m.getRules().clear();
		for (Rule r : this.model.getRules()) {
			/* create a copy of the rule, in order to sort it */
			Rule rc = new Rule(r);
			if (r.getBody().size() > 2) {
				/* the bodies need to be sorted, wrt the fact they are either edbs or idbs */
				rc.sortBodyByComparator(edbComparator);

				RuleWardedDecorator rd = new RuleWardedDecorator(rc);
				List<Rule> joinOptimized = rd.multiJoinOptimization(Collections.emptySet());
				m.getRules().addAll(joinOptimized);
			} else {
				m.getRules().add(rc);
			}
		}

		return m;
	}

	/**
	 * It returns the predicate graph constructed on the model
	 * 
	 * @return the PredicateGraph
	 */
	public PredicateGraph getPredicateGraph() {
		return this.pGraph;
	}

	/**
	 * It returns the Model used to instantiate this decorator
	 * 
	 * @return the original Model
	 */
	public Model getModel() {
		return this.model;
	}

	/**
	 * It returns whether a predicate is extensional or not
	 * 
	 * @param the name of the predicate to check
	 * @return a boolean true if the predicate is extensional, false otherwise
	 */
	public boolean isExtensional(String predicate) {
		return this.edbNameList.contains(predicate);
	}

	/**
	 * It returns a list of copied Literals of a specified Rule that are extensional
	 * 
	 * @param the rule of which we want extensional literals
	 * @return the List of copied extensional literals contained
	 */
	public List<Literal> getExtensionalLiteralsOf(Rule rule) {
		List<Literal> extensionalLiterals = new LinkedList<Literal>();
		for (Literal literal : rule.getLiterals()) {
			if (isExtensional(literal.getAtom().getName())) {
				extensionalLiterals.add(new Literal(literal));
			}
		}
		return extensionalLiterals;
	}
	
	/**
	 * It returns a list of copied Literals of a specified Rule that are intensional
	 * 
	 * @param the rule of which we want intensional literals
	 * @return the List of copied intensional literals contained
	 */
	public List<Atom> getIntensionalLiteralsOf(Rule rule) {
		List<Atom> intensionalLiterals = new LinkedList<Atom>();
		List<Atom> heads = rule.getHead();
		for (Atom head : heads) {
			if (!isExtensional(head.getName())) {
				intensionalLiterals.add(head);
			}
		}
		return intensionalLiterals;
	}

	/**
	 * A predicate is mutually recursive with the head of a rule if it's reachable
	 * from the head and it is present in the body of the rule or the body of the
	 * rule is reachable from the predicate. These last conditions are not verified
	 * by this method.
	 * 
	 * @param predicate, the name of the predicate to check if it's mutually
	 *                   recursive with the head.
	 * @param head,      the name of the head predicate.
	 * @return true if it's mutually recursive, false otherwise.
	 */
	public boolean isMutuallyRecursiveWithTheHead(String predicate, String head) {
		boolean isMutuallyRecursiveWithTheHead = false;
		if (this.pGraph.isReachable(head, predicate, false))
			isMutuallyRecursiveWithTheHead = true;
		return isMutuallyRecursiveWithTheHead;
	}

	public boolean isWithoutSkolem() {
		for (Rule r : model.getRules()) {
			if (!r.getSkolemAtoms().isEmpty() || !r.getConditions().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * checks whether the model it wraps is positive
	 * 
	 * @return
	 */
	public boolean isPositive() {
		for (Rule r : model.getRules()) {
			if (!r.getNegativeLiterals().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public boolean isExitRule(Rule r) {
		return this.model.getRules().contains(r)
				&& r.getBodyAtomNamesList().stream().allMatch(a -> this.pGraph.getInEdges().get(a).isEmpty());
	}

	public boolean isPWLW(Rule r) {
		Integer idbCount = 0;
		for (Atom bodyAtom : r.getBodyAtoms()) {
			String name = bodyAtom.getName();
			boolean isIDB = !this.pGraph.getInEdges().get(name).isEmpty();
			if (isIDB)
				idbCount++;
		}
		return model.getRules().contains(r) && idbCount <= 1;
	}
	
	/**
	 * It returns the string of input predicates as a comma separated list
	 */
	public String getExtensionalString() {
		String estensionalString = "";
		Set<String> addedAtomNames = new HashSet<String>();
		for (Rule r : this.model.getRules()) {
			List<Literal> extensionalLiteralsOf = getExtensionalLiteralsOf(r);
			for (Literal l : extensionalLiteralsOf) {
				if (!addedAtomNames.contains(l.getAtom().getName())) {
					addedAtomNames.add(l.getAtom().getName());
					estensionalString += l.getAtom().toClauseString();
				}
			}
		}
		return estensionalString;
	}

	/**
	 * It returns the string of intensional predicates as a comma separated list
	 */
	public String getIntensionalString() {
		String intensionalString = "";
		Set<String> addedAtomNames = new HashSet<String>();
		for (Rule r : this.model.getRules()) {
			List<Atom> extensionalLiteralsOf = getIntensionalLiteralsOf(r);
			for (Atom a : extensionalLiteralsOf) {
				if (!addedAtomNames.contains(a.getName())) {
					addedAtomNames.add(a.getName());
					intensionalString += a.toClauseString();
				}
			}
		}
		return intensionalString;
	}
}

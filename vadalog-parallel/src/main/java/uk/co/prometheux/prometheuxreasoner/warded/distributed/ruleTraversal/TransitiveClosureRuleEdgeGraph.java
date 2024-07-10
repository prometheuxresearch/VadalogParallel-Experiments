package uk.co.prometheux.prometheuxreasoner.warded.distributed.ruleTraversal;

import java.util.HashSet;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.operators.AtomOperatorDecorator;

/**
 * This Class performs the transitive closure of the EdgeRule relation. From a
 * model, it creates the EdgeRules, i.e. the edges represented by the
 * unification of two rules. From the EdgeRules it finds all the connectivity
 * among the rules by performing the transitive closure of them
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

public class TransitiveClosureRuleEdgeGraph {

	private Model model;
	private Set<RuleEdge> result;
	private Set<RuleEdge> delta;

	public TransitiveClosureRuleEdgeGraph(Model m) {
		this.model = m;
		this.result = new HashSet<RuleEdge>();
		this.delta = new HashSet<RuleEdge>();
	}

	/**
	 * this method visits the rule of a model, and for each rule, when an
	 * unification with another rule is found, it creates a new edge rule-rule
	 * 
	 * @return the list of ruleEdge from the model
	 */
	private Set<RuleEdge> createEdgesFromModel() {
		Set<RuleEdge> edgesFromModel = new HashSet<RuleEdge>();
		for (Rule r1 : this.model.getRules()) {
			int operand = 0;
			for (Literal l : r1.getBody()) {
				Atom currAtom = l.getAtom();
				for (Rule r2 : this.model.getRules()) {
					if (this.tryUnify(currAtom, r2.getSingleHead())) {
						RuleEdge re = new RuleEdge(r1, r2, operand);
						edgesFromModel.add(re);
					}
				}
				operand++;
			}
		}
		return edgesFromModel;
	}

	private boolean tryUnify(Atom left, Atom right) {
		AtomOperatorDecorator aod = new AtomOperatorDecorator(left);
		if (aod.unify().does(right)) {
			return true;
		}
		return false;
	}

	private void except() {
		this.delta.removeAll(this.result);
	}

	public Set<RuleEdge> transitiveClosure() {
		Set<RuleEdge> edge = this.createEdgesFromModel();
		delta.addAll(edge);
		while (!delta.isEmpty()) {
			Set<RuleEdge> deltaCopy = new HashSet<RuleEdge>(this.delta);
			for (RuleEdge d : deltaCopy) {
				for (RuleEdge e : edge) {
					// we perform the join
					// d(From,To),e(From,To)
					// with this settings:
					// r1: f :- m,c
					// r2: a :- f
					// r3: p :- a, b

					// from this join: d(r3,r2),e(r2,r1)
					// we obtain: j(r3,r1)
					this.joinAndNoDupl(d, e);
				}
			}
			this.except();
			this.result.addAll(this.delta);

		}
		return result;
	}

	private void joinAndNoDupl(RuleEdge d, RuleEdge e) {
		if (d.isVisited(e)) {
			return;
		}
		if (d.getRTo().equals(e.getRFrom())) {
			RuleEdge join = new RuleEdge();
			join.addIntermediateVisited(d.getVisited());
			join.addToVisited(e);
			join.setRFrom(d.getRFrom());
			join.setRTo(e.getRTo());
			join.setOperandInterested(e.getOperandInterested());
			join.inferFromBodyToBodyMapping(d.getrFromBodyrToBodyMappings(), e.getrFromBodyrToBodyMappings());
			delta.add(join);
		}
	}

}

package uk.co.prometheux.prometheuxreasoner.warded.distributed;

import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.pwlWarded.ModelPWLWardedDecorator;

/**
 * This Class adheres to the pattern Decorator. It the represents a model
 * decorator for distributed static functionalities
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class ModelDistributedDecorator {

	/* The underlying model */
	private Model model;
	private ModelPWLWardedDecorator pwlwd;

	/* The m. aggregation decorator for this model */
	private ModelMonotonicAggregationDecorator aggregationDecorator;

	public ModelDistributedDecorator(Model m) {
		this.model = m;
		this.aggregationDecorator = new ModelMonotonicAggregationDecorator(m);
		this.pwlwd = new ModelPWLWardedDecorator(m);
	}

	/**
	 * It check whether the rules of this model with the same head predicate and
	 * that contain monotonic aggregations are coherent according to Vadalog
	 * Semantics  i.e. they use the same
	 * aggregation in the same head positions to compute the head predicate.
	 */
	public void checkMonotonicAggregationSemantics() {
			this.aggregationDecorator.checkMonotonicAggregationCorrectness();
	}

	/**
	 * It rewrites a set of rules which computes same predicate with same
	 * aggregations in different rules. E.G. path(X,Z,J) :-
	 * path(X,Y,W1), edge(Y,Z,W2), J=msum(W1*W1,<Y>). path(X,Y,J) :- edge(X,Y,W),
	 * J=msum(W,<Y>). are rewritten as (1) vatom_1(X,Z,J,Y) :- path(X,Y,W1),
	 * edge(Y,Z,W2), J=W1*W2. (2) vatom_1(X,Y,J,Y) :- edge(X,Y,W), J=W (3)
	 * path(Var_1,Var_2,Var_5) :- vatom_1(Var_1,Var_2,Var_3,Var_4),
	 * Var_5=msum(Var_3,<Var_4>). Notice that it is always possible to rewrite when
	 * Vadalog semantics on the aggregation is respected, that is all predicates
	 * with the same head must be computed with the same aggregation in the same
	 * head positions.
	 * 
	 * @return a new model with matching aggregate rules rewritten
	 */
	public Model rewriteMonotonicAggregation() {
		Model rewritten = this.aggregationDecorator.rewriteRulesWithMonotonicAggregations();
		rewritten = this.aggregationDecorator.rewriteRecursiveRulesWithContributors(rewritten);
		return rewritten;

	}

	/**
	 * Returns the model this decorator is wrapping
	 * 
	 * @return the model this decorator is wrapping
	 */
	public Model getModel() {
		return model;
	}

	public boolean isRecursive(Rule r) {
		return this.pwlwd.isRecursive(r);
	}

}

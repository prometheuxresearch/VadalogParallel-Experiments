package uk.co.prometheux.prometheuxreasoner.aggregation;

import java.util.*;

import uk.co.prometheux.prometheuxreasoner.common.IdGenerator;
import uk.co.prometheux.prometheuxreasoner.model.*;
import uk.co.prometheux.prometheuxreasoner.model.expressions.AggregateOperator;
import uk.co.prometheux.prometheuxreasoner.operators.UnsupportedOperatorException;

/**
 * This Class is responsible of rewriting non-monotonic aggregates
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class NonMonotonicAggregatesRewriter {

	public Model rewriteSQLAggregates(Model model) {

		return model;
	}

	/*
	 * rewrites non-monotonic count into rules with monotonic count and
	 * non-monotonic max
	 */
	/* assumption: single head, one aggregate */
	private Collection<? extends Rule> rewriteCount(Rule rule, Condition condition) {
		
		return Collections.singletonList(null);
	}

	// Very restrictive assumptions: no nesting, single head, one aggregate etc.
	private Collection<? extends Rule> rewriteMaxMin(Rule rule, Condition condition) {

		return Collections.singletonList(null);
	}
}

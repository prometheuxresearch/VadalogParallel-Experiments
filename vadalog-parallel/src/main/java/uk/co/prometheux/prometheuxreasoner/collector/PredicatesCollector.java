package uk.co.prometheux.prometheuxreasoner.collector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uk.co.prometheux.prometheuxreasoner.model.types.VadaValue;

/**
 * 
 * This Class is a collector for output predicates
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class PredicatesCollector extends AOutputProcessorCollector<Map<String, List<List<VadaValue<?>>>>> {

	@Override
	protected void initCollection() {
		collection = new HashMap<>();
	}

	@Override
	protected void collect(int factId, String atomName, Iterable<VadaValue<?>> terms) {
		if (!collection.containsKey(atomName)) {
			collection.put(atomName, new LinkedList<>());
		}

		List<List<VadaValue<?>>> predicate = collection.get(atomName);
		List<VadaValue<?>> tuple = new LinkedList<>();
		for (VadaValue<?> value : terms)
			tuple.add(value);
		predicate.add(tuple);
	}

}

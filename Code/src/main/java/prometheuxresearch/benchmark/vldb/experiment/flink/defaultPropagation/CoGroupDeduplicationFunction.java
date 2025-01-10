package prometheuxresearch.benchmark.vldb.experiment.flink.defaultPropagation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.util.Collector;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class CoGroupDeduplicationFunction implements
		CoGroupFunction<Tuple4<Integer, Integer, String, String>, Tuple4<Integer, Integer, String, String>, Tuple4<Integer, Integer, String, String>> {

	private static final long serialVersionUID = 4362283381685712069L;

	@Override
	public void coGroup(Iterable<Tuple4<Integer, Integer, String, String>> first,
			Iterable<Tuple4<Integer, Integer, String, String>> second,
			Collector<Tuple4<Integer, Integer, String, String>> out) throws Exception {

		Set<String> generatedFacts = new HashSet<>();
		Iterator<Tuple4<Integer, Integer, String, String>> iter = second.iterator();
		while (iter.hasNext()) {
			generatedFacts.add(iter.next().f3);
		}

		iter = first.iterator();
		while (iter.hasNext()) {
			Tuple4<Integer, Integer, String, String> nextT = iter.next();
			if (!generatedFacts.contains(nextT.f3)) {
				out.collect(nextT);
				generatedFacts.add(nextT.f3);
			}
		}

		out.close();
	}

}

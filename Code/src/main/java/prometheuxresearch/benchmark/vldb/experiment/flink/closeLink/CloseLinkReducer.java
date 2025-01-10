package prometheuxresearch.benchmark.vldb.experiment.flink.closeLink;

import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.flink.api.common.functions.GroupReduceFunction;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class CloseLinkReducer implements
		GroupReduceFunction<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>, Tuple4<Integer, Integer, Integer[], Double>> {

	private static final long serialVersionUID = -5555573534672913290L;

	@Override
	public void reduce(Iterable<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> values,
			Collector<Tuple4<Integer, Integer, Integer[], Double>> out) throws Exception {
		Double sum = 0.0;
		Set<Integer> set = new HashSet<>();
		Tuple4<Integer, Integer, Integer[], Double> t = null;
		for (Tuple6<Integer, Integer, Integer, Integer[], Double, Double> value : values) {
			Integer k1 = value.f0;
			Integer k2 = value.f1;
			set.addAll(Arrays.asList(value.f3));
			sum += value.f4;
			t = new Tuple4<>(k1, k2, set.toArray(new Integer[0]), sum);
		}
		if (t != null) {
			out.collect(t);
		}
		out.close();
	}

}

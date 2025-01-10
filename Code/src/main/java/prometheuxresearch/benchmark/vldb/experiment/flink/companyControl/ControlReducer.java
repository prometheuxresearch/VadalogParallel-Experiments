package prometheuxresearch.benchmark.vldb.experiment.flink.companyControl;

import java.util.Iterator;

import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class ControlReducer
		implements GroupReduceFunction<Tuple3<Integer, Integer, Double>, Tuple3<Integer, Integer, Double>> {

	private static final long serialVersionUID = 2869990643458455651L;

	@Override
	public void reduce(Iterable<Tuple3<Integer, Integer, Double>> values,
			Collector<Tuple3<Integer, Integer, Double>> out) throws Exception {
		Iterator<Tuple3<Integer, Integer, Double>> it = values.iterator();
		Double max = 0.0;
		Tuple3<Integer, Integer, Double> maxT = null;
		while (it.hasNext()) {
			Tuple3<Integer, Integer, Double> t = it.next();
			Double currentSum = t.f2;
			if (max < currentSum) {
				max = currentSum;
				maxT = t;
			}
			if (!it.hasNext() && maxT != null) {
				out.collect(maxT);
			}
		}
	}

}

package prometheuxresearch.benchmark.vldb.experiment.flink.closeLink;

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

public class AggregationReducer
		implements GroupReduceFunction<Tuple3<String, String, Object[]>, Tuple3<String, String, Object[]>> {

	private static final long serialVersionUID = 8910388022847200800L;

	@Override
	public void reduce(Iterable<Tuple3<String, String, Object[]>> values,
			Collector<Tuple3<String, String, Object[]>> out) throws Exception {
		Iterator<Tuple3<String, String, Object[]>> it = values.iterator();
		Double max = 0.0;
		Tuple3<String, String, Object[]> maxT = null;
		while (it.hasNext()) {
			Tuple3<String, String, Object[]> t = it.next();
			Object[] fields = t.f2;
			Double currentSum = (Double) fields[3];
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

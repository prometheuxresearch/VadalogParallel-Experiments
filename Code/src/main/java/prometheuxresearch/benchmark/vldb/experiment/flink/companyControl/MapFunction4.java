package prometheuxresearch.benchmark.vldb.experiment.flink.companyControl;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class MapFunction4 implements MapFunction<Tuple3<Integer, Integer, Double>, Tuple2<Integer, Integer>> {

	private static final long serialVersionUID = -4723030734564203500L;

	@Override
	public Tuple2<Integer, Integer> map(Tuple3<Integer, Integer, Double> t) throws Exception {
		return new Tuple2<Integer, Integer>(t.f0, t.f1);
	}

}

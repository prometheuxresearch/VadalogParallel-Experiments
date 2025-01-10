package prometheuxresearch.benchmark.vldb.experiment.flink.companyControl;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class MapFunction1
		implements MapFunction<Tuple3<Integer, Integer, Double>, Tuple4<Integer, Integer, Integer, Double>> {

	private static final long serialVersionUID = 4344152396260500187L;

	@Override
	public Tuple4<Integer, Integer, Integer, Double> map(Tuple3<Integer, Integer, Double> t) throws Exception {
		return new Tuple4<>(t.f0, t.f1, t.f1, t.f2);
	}

}

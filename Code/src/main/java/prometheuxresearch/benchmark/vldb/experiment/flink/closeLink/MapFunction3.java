package prometheuxresearch.benchmark.vldb.experiment.flink.closeLink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class MapFunction3
		implements MapFunction<Tuple4<Integer, Integer, Integer[], Double>, Tuple3<Integer, Integer, Double>> {

	private static final long serialVersionUID = 458922373026677261L;

	@Override
	public Tuple3<Integer, Integer, Double> map(Tuple4<Integer, Integer, Integer[], Double> t) throws Exception {
		return new Tuple3<Integer, Integer, Double>(t.f0, t.f1, t.f3);
	}

}
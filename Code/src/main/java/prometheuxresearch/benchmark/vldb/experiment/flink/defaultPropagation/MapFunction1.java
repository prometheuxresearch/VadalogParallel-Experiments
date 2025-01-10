package prometheuxresearch.benchmark.vldb.experiment.flink.defaultPropagation;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class MapFunction1 implements MapFunction<Tuple2<Integer, Double>, Tuple2<Integer, Integer>> {

	private static final long serialVersionUID = -8610659001355951235L;

	@Override
	public Tuple2<Integer, Integer> map(Tuple2<Integer, Double> value) throws Exception {
		return new Tuple2<>(value.f0, value.f0);
	}

}

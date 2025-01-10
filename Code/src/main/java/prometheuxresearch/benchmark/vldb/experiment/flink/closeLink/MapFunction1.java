package prometheuxresearch.benchmark.vldb.experiment.flink.closeLink;

import java.util.Arrays;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple6;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class MapFunction1 implements
		MapFunction<Tuple3<Integer, Integer, Double>, Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> {

	private static final long serialVersionUID = 4522445600416051557L;

	@Override
	public Tuple6<Integer, Integer, Integer, Integer[], Double, Double> map(Tuple3<Integer, Integer, Double> t) {
		return new Tuple6<>(t.f0, t.f1, t.f1, Arrays.asList(t.f0, t.f1).toArray(new Integer[0]), t.f2, 0.0);
	}

}
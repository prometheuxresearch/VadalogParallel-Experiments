package prometheuxresearch.benchmark.vldb.experiment.flink.closeLink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.api.java.tuple.Tuple6;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class MapFunction4 implements
		MapFunction<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>, Tuple5<Integer, Integer, Integer, Integer[], Double>> {

	private static final long serialVersionUID = 5449261993839756668L;

	@Override
	public Tuple5<Integer, Integer, Integer, Integer[], Double> map(
			Tuple6<Integer, Integer, Integer, Integer[], Double, Double> t) throws Exception {
		return new Tuple5<>(t.f0, t.f1, t.f2, t.f3, t.f4);
	}

}
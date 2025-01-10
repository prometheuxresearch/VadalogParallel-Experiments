package prometheuxresearch.benchmark.vldb.experiment.flink.closeLink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple6;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class MapFunction5 implements
		MapFunction<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>, Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> {

	private static final long serialVersionUID = -9109760223908812181L;

	@Override
	public Tuple6<Integer, Integer, Integer, Integer[], Double, Double> map(
			Tuple6<Integer, Integer, Integer, Integer[], Double, Double> value) throws Exception {
		return value;
	}

}

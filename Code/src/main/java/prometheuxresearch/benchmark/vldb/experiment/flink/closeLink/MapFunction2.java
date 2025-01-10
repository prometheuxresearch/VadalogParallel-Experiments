package prometheuxresearch.benchmark.vldb.experiment.flink.closeLink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.api.java.tuple.Tuple6;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class MapFunction2 implements
		MapFunction<Tuple6<Integer, Integer, Integer[], Double, Integer, Double>, Tuple5<Integer, Integer, Integer, Integer[], Double>> {

	private static final long serialVersionUID = -6744411275325032134L;

	@Override
	public Tuple5<Integer, Integer, Integer, Integer[], Double> map(
			Tuple6<Integer, Integer, Integer[], Double, Integer, Double> t) throws Exception {
		List<Integer> l = new ArrayList<>(Arrays.asList(t.f2));
		l.add(t.f4);
		return new Tuple5<Integer, Integer, Integer, Integer[], Double>(t.f0, t.f4, t.f1, l.toArray(new Integer[0]),
				t.f3 * t.f5);
	}

}
package prometheuxresearch.benchmark.vldb.experiment.flink.companyControl;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.tuple.Tuple4;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class LeftAntiJoinFunction implements
		JoinFunction<Tuple4<Integer, Integer, Integer, Double>, Tuple4<Integer, Integer, Integer, Double>, Tuple4<Integer, Integer, Integer, Double>> {

	private static final long serialVersionUID = 7380455449331956005L;

	@Override
	public Tuple4<Integer, Integer, Integer, Double> join(Tuple4<Integer, Integer, Integer, Double> left,
			Tuple4<Integer, Integer, Integer, Double> right) {
		if (right == null) {
			return left; // Keep the left element if there is no match in the right input
		}
		return new Tuple4<>(0, 0, 0, -1.0); // Exclude the left element if there is a match
	}

}

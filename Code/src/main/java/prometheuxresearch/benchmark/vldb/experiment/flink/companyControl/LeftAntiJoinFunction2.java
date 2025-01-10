package prometheuxresearch.benchmark.vldb.experiment.flink.companyControl;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class LeftAntiJoinFunction2
		implements JoinFunction<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>> {

	private static final long serialVersionUID = -1100134623894272958L;

	@Override
	public Tuple2<Integer, Integer> join(Tuple2<Integer, Integer> left, Tuple2<Integer, Integer> right)
			throws Exception {
		if (right == null) {
			return left; // Keep the left element if there is no match in the right input
		}
		return new Tuple2<>(-1000, -1000); // Exclude the left element if there is a match
	}

}

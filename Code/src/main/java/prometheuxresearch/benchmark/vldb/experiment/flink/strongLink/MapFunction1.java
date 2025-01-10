package prometheuxresearch.benchmark.vldb.experiment.flink.strongLink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class MapFunction1
		implements MapFunction<Tuple3<String, String, String>, Tuple4<String, String, String, Integer>> {

	private static final long serialVersionUID = -5340239194569808300L;

	@Override
	public Tuple4<String, String, String, Integer> map(Tuple3<String, String, String> t) throws Exception {
		return new Tuple4<>(t.f0, t.f2, t.f1, 1);
	}

}

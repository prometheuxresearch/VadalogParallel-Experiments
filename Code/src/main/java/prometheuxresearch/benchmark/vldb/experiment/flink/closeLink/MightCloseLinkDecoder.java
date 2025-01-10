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

public class MightCloseLinkDecoder
		implements MapFunction<Tuple3<String, String, Object[]>, Tuple4<Integer, Integer, Integer[], Double>> {

	private static final long serialVersionUID = 2712314414648665082L;

	@Override
	public Tuple4<Integer, Integer, Integer[], Double> map(Tuple3<String, String, Object[]> value) throws Exception {
		Integer f1 = (Integer) value.f2[0];
		Integer f2 = (Integer) value.f2[1];
		Integer[] f3 = (Integer[]) value.f2[2];
		Double f4 = (Double) value.f2[3];
		return new Tuple4<>(f1, f2, f3, f4);
	}

}

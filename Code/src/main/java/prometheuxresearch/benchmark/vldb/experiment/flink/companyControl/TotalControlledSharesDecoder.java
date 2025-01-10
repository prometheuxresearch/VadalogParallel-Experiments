package prometheuxresearch.benchmark.vldb.experiment.flink.companyControl;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class TotalControlledSharesDecoder
		implements MapFunction<Tuple3<String, String, Object[]>, Tuple3<Integer, Integer, Double>> {

	private static final long serialVersionUID = 658575509359094183L;

	@Override
	public Tuple3<Integer, Integer, Double> map(Tuple3<String, String, Object[]> value) throws Exception {
		Integer f1 = (Integer) value.f2[0];
		Integer f2 = (Integer) value.f2[1];
		Double f3 = (Double) value.f2[2];
		return new Tuple3<>(f1, f2, f3);
	}

}

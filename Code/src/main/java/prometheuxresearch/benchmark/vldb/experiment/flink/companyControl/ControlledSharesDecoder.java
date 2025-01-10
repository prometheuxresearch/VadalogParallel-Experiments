package prometheuxresearch.benchmark.vldb.experiment.flink.companyControl;

import java.util.List;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class ControlledSharesDecoder
		implements MapFunction<Tuple3<String, String, List<Object>>, Tuple4<Integer, Integer, Integer, Double>> {

	private static final long serialVersionUID = -1267208311188066151L;

	@Override
	public Tuple4<Integer, Integer, Integer, Double> map(Tuple3<String, String, List<Object>> value) throws Exception {
		Integer f1 = (Integer) value.f2.get(0);
		Integer f2 = (Integer) value.f2.get(1);
		Integer f3 = (Integer) value.f2.get(2);
		Double f4 = (Double) value.f2.get(3);
		return new Tuple4<>(f1, f2, f3, f4);

	}

}

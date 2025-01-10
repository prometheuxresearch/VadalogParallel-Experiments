package prometheuxresearch.benchmark.vldb.experiment.flink.companyControl;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;

// Tuple4<Integer, Integer, Integer, Double>
// Tuple3<String, String, String>
/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class ControlledSharesEncoder
		implements MapFunction<Tuple4<Integer, Integer, Integer, Double>, Tuple3<String, String, Object[]>> {

	private static final long serialVersionUID = -5348604848608863205L;

	@Override
	public Tuple3<String, String, Object[]> map(Tuple4<Integer, Integer, Integer, Double> value) throws Exception {
		String datasetName = "ControlledShares";

		List<Object> keyFields = new ArrayList<>(4);
		keyFields.add(value.f0);
		keyFields.add(value.f1);
		keyFields.add(value.f2);
		keyFields.add(value.f3);

		List<Object> allFields = new ArrayList<>(4);

		allFields.add(value.f0);
		allFields.add(value.f1);
		allFields.add(value.f2);
		allFields.add(value.f3);

		return new Tuple3<String, String, Object[]>(datasetName, keyFields.toString(), allFields.toArray());
	}

}

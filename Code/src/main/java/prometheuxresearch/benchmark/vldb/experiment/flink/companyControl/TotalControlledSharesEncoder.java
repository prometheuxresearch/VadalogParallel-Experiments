package prometheuxresearch.benchmark.vldb.experiment.flink.companyControl;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class TotalControlledSharesEncoder
		implements MapFunction<Tuple3<Integer, Integer, Double>, Tuple3<String, String, Object[]>> {

	private static final long serialVersionUID = 3696588368792715414L;

	@Override
	public Tuple3<String, String, Object[]> map(Tuple3<Integer, Integer, Double> value) throws Exception {
		String datasetName = "TotalControlledShares";

		List<Object> keyFields = new ArrayList<>(2);
		keyFields.add(value.f0);
		keyFields.add(value.f1);

		List<Object> allFields = new ArrayList<>(3);

		allFields.add(value.f0);
		allFields.add(value.f1);
		allFields.add(value.f2);

		return new Tuple3<String, String, Object[]>(datasetName, keyFields.toString(), allFields.toArray());
	}

}

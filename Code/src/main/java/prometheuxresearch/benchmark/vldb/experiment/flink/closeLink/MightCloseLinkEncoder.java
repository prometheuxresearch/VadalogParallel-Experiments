package prometheuxresearch.benchmark.vldb.experiment.flink.closeLink;

import java.util.ArrayList;
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

public class MightCloseLinkEncoder
		implements MapFunction<Tuple4<Integer, Integer, Integer[], Double>, Tuple3<String, String, Object[]>> {

	private static final long serialVersionUID = -1008042976324192764L;

	@Override
	public Tuple3<String, String, Object[]> map(Tuple4<Integer, Integer, Integer[], Double> value) throws Exception {
		String datasetName = "MightCloseLink";

		List<Object> keyFields = new ArrayList<>(2);
		keyFields.add(value.f0);
		keyFields.add(value.f1);

		List<Object> allFields = new ArrayList<>(4);

		allFields.add(value.f0);
		allFields.add(value.f1);
		allFields.add(value.f2);
		allFields.add(value.f3);

		return new Tuple3<String, String, Object[]>(datasetName, keyFields.toString(), allFields.toArray());
	}

}

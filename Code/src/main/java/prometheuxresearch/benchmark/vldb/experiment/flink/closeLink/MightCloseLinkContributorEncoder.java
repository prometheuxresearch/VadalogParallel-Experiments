package prometheuxresearch.benchmark.vldb.experiment.flink.closeLink;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple5;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class MightCloseLinkContributorEncoder
		implements MapFunction<Tuple5<Integer, Integer, Integer, Integer[], Double>, Tuple3<String, String, Object[]>> {

	private static final long serialVersionUID = -2185855135233106833L;

	@Override
	public Tuple3<String, String, Object[]> map(Tuple5<Integer, Integer, Integer, Integer[], Double> value)
			throws Exception {
		String datasetName = "MightCloseLink";

		List<Object> keyFields = new ArrayList<>(3);
		keyFields.add(value.f0);
		keyFields.add(value.f1);
		keyFields.add(value.f2);

		List<Object> allFields = new ArrayList<>(5);

		allFields.add(value.f0);
		allFields.add(value.f1);
		allFields.add(value.f2);
		allFields.add(value.f3);
		allFields.add(value.f4);

		return new Tuple3<String, String, Object[]>(datasetName, keyFields.toString(), allFields.toArray());
	}

}

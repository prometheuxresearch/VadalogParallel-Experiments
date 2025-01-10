package prometheuxresearch.benchmark.vldb.experiment.flink.closeLink;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple6;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class MightCloseLinkEncoder2 implements
		MapFunction<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>, Tuple3<String, String, Object[]>> {

	private static final long serialVersionUID = 3662039760410668391L;

	@Override
	public Tuple3<String, String, Object[]> map(Tuple6<Integer, Integer, Integer, Integer[], Double, Double> value)
			throws Exception {
		String datasetName = "MightCloseLink";

		List<Object> keyFields = new ArrayList<>(2);
		keyFields.add(value.f0);
		keyFields.add(value.f1);

		List<Object> allFields = new ArrayList<>(6);

		allFields.add(value.f0);
		allFields.add(value.f1);
		allFields.add(value.f2);
		allFields.add(value.f3);
		allFields.add(value.f4);
		allFields.add(value.f5);

		return new Tuple3<String, String, Object[]>(datasetName, keyFields.toString(), allFields.toArray());
	}

}

package prometheuxresearch.benchmark.vldb.experiment.flink.companyControl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.util.Collector;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class DeduplicationCoGroup implements
		CoGroupFunction<Tuple4<Integer, Integer, Integer, Double>, Tuple4<Integer, Integer, Integer, Double>, Tuple4<Integer, Integer, Integer, Double>> {

	private static final long serialVersionUID = -499481628809368880L;

	@Override
	public void coGroup(Iterable<Tuple4<Integer, Integer, Integer, Double>> first,
			Iterable<Tuple4<Integer, Integer, Integer, Double>> second,
			Collector<Tuple4<Integer, Integer, Integer, Double>> out) throws Exception {

		Set<Tuple4<Integer, Integer, Integer, Double>> set = new HashSet<>();
		Iterator<Tuple4<Integer, Integer, Integer, Double>> it = second.iterator();
		Iterator<Tuple4<Integer, Integer, Integer, Double>> iter = first.iterator();

		while (it.hasNext()) {
			set.add(it.next());
		}

		while (iter.hasNext()) {
			Tuple4<Integer, Integer, Integer, Double> tuple = iter.next();
			if (!set.contains(tuple)) {
				set.add(tuple);
				out.collect(tuple);
			}
		}

		out.close();

	}

}

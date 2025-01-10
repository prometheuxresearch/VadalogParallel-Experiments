package prometheuxresearch.benchmark.vldb.experiment.flink.companyControl;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class TotalControlledSharesCoGroup implements
		CoGroupFunction<Tuple3<String, String, Object[]>, Tuple3<String, String, Object[]>, Tuple3<Integer, Integer, Double>> {

	private static final long serialVersionUID = -5152839692363175351L;

	@Override
	public void coGroup(Iterable<Tuple3<String, String, Object[]>> first,
			Iterable<Tuple3<String, String, Object[]>> second, Collector<Tuple3<Integer, Integer, Double>> out)
			throws Exception {

		Iterator<Tuple3<Integer, Integer, Double>> itSecond = decode(second.iterator());
		// if the right is empty there exists the left
		Iterator<Tuple3<Integer, Integer, Double>> itFirst = decode(first.iterator());
		if (!itSecond.hasNext()) {
			Tuple3<Integer, Integer, Double> newGroup = itFirst.next();
			out.collect(newGroup);
		} else {
			// right is not empty and also the left
			if (itFirst.hasNext()) {
				Tuple3<Integer, Integer, Double> newGroup = itFirst.next();
				Tuple3<Integer, Integer, Double> oldGroup = itSecond.next();
				out.collect(new Tuple3<>(newGroup.f0, newGroup.f1, newGroup.f2 + oldGroup.f2));
			}
		}

		out.close();

	}

	private Iterator<Tuple3<Integer, Integer, Double>> decode(Iterator<Tuple3<String, String, Object[]>> it) {
		LinkedList<Tuple3<Integer, Integer, Double>> l = new LinkedList<>();
		while (it.hasNext()) {
			Tuple3<String, String, Object[]> value = it.next();
			if (value.f0.equals("TotalControlledShares")) {
				Integer f1 = (Integer) value.f2[0];
				Integer f2 = (Integer) value.f2[1];
				Double f3 = (Double) value.f2[2];
				l.add(new Tuple3<>(f1, f2, f3));
			}
		}
		return l.iterator();

	}

}

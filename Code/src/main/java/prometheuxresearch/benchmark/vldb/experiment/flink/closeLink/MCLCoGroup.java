package prometheuxresearch.benchmark.vldb.experiment.flink.closeLink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.util.Collector;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class MCLCoGroup implements
		CoGroupFunction<Tuple3<String, String, Object[]>, Tuple3<String, String, Object[]>, Tuple4<Integer, Integer, Integer[], Double>> {

	private static final long serialVersionUID = 1388451044605951589L;

	@Override
	public void coGroup(Iterable<Tuple3<String, String, Object[]>> first,
			Iterable<Tuple3<String, String, Object[]>> second,
			Collector<Tuple4<Integer, Integer, Integer[], Double>> out) throws Exception {

		Set<Integer> visited = new HashSet<>();
		Double sum = 0.0;
		Iterator<Tuple4<Integer, Integer, Integer[], Double>> rightIt = decode(second.iterator());
		if (!rightIt.hasNext()) {
			Iterator<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> leftIt = decode2(first.iterator());
			while (leftIt.hasNext()) {
				Tuple6<Integer, Integer, Integer, Integer[], Double, Double> leftT = leftIt.next();
				sum += (leftT.f4 - leftT.f5);
				visited.addAll(new ArrayList<>(Arrays.asList(leftT.f3)));
				if (!leftIt.hasNext()) {
					out.collect(new Tuple4<>(leftT.f0, leftT.f1, visited.toArray(new Integer[0]), sum));
				}
			}

		} else {
			Tuple4<Integer, Integer, Integer[], Double> oldSumT = rightIt.next();
			Tuple4<Integer, Integer, Integer[], Double> newSumT = null;
			Double newSum = oldSumT.f3;
			visited.addAll(new ArrayList<>(Arrays.asList(oldSumT.f2)));
			Iterator<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> leftIt = decode2(first.iterator());
			while (leftIt.hasNext()) {
				Tuple6<Integer, Integer, Integer, Integer[], Double, Double> leftT = leftIt.next();
				newSum = newSum + leftT.f4 - leftT.f5;
				visited.addAll(new ArrayList<>(Arrays.asList(leftT.f3)));
				newSumT = new Tuple4<>(leftT.f0, leftT.f1, visited.toArray(new Integer[0]), newSum);
			}
			if (newSumT != null) {
				out.collect(newSumT);
			}

		}
		out.close();

	}

	private Iterator<Tuple4<Integer, Integer, Integer[], Double>> decode(
			Iterator<Tuple3<String, String, Object[]>> iterator) {
		LinkedList<Tuple4<Integer, Integer, Integer[], Double>> l = new LinkedList<>();
		while (iterator.hasNext()) {
			Tuple3<String, String, Object[]> value = iterator.next();
			if (value.f0.equals("MightCloseLink")) {
				Integer f1 = (Integer) value.f2[0];
				Integer f2 = (Integer) value.f2[1];
				Integer[] f3 = (Integer[]) value.f2[2];
				Double f4 = (Double) value.f2[3];
				l.add(new Tuple4<>(f1, f2, f3, f4));
			}
		}
		return l.iterator();
	}

	private Iterator<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> decode2(
			Iterator<Tuple3<String, String, Object[]>> iterator) {
		LinkedList<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> l = new LinkedList<>();
		while (iterator.hasNext()) {
			Tuple3<String, String, Object[]> value = iterator.next();
			if (value.f0.equals("MightCloseLink")) {
				Integer f1 = (Integer) value.f2[0];
				Integer f2 = (Integer) value.f2[1];
				Integer f3 = (Integer) value.f2[2];
				Integer[] f4 = (Integer[]) value.f2[3];
				Double f5 = (Double) value.f2[4];
				Double f6 = (Double) value.f2[5];
				l.add(new Tuple6<>(f1, f2, f3, f4, f5, f6));
			}
		}
		return l.iterator();
	}

}

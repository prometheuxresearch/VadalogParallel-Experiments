package prometheuxresearch.benchmark.vldb.experiment.flink.closeLink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.util.Collector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple5;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class MCLContributorCoGroup implements
		CoGroupFunction<Tuple3<String, String, Object[]>, Tuple3<String, String, Object[]>, Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> {

	private static final long serialVersionUID = 4118639214053884666L;

	@Override
	public void coGroup(Iterable<Tuple3<String, String, Object[]>> first,
			Iterable<Tuple3<String, String, Object[]>> second,
			Collector<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> out) throws Exception {

		Double newMax = 0.0;
		Double oldMax = 0.0;
		Set<Integer> visited = new HashSet<>();
		Iterator<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> rightIt = decode(second.iterator());
		if (!rightIt.hasNext()) {
			Iterator<Tuple5<Integer, Integer, Integer, Integer[], Double>> leftIt = decode2(first.iterator());
			while (leftIt.hasNext()) {
				Tuple5<Integer, Integer, Integer, Integer[], Double> leftT = leftIt.next();
				if (leftT.f4 > newMax) {
					newMax = leftT.f4;
					visited.addAll(new ArrayList<>(Arrays.asList(leftT.f3)));
				}
				if (!leftIt.hasNext()) {
					out.collect(new Tuple6<>(leftT.f0, leftT.f1, leftT.f2, visited.toArray(new Integer[0]), newMax,
							oldMax));
				}
			}
		}
		// right has one single next
		else {
			Tuple6<Integer, Integer, Integer, Integer[], Double, Double> oldMaxT = rightIt.next();
			Tuple6<Integer, Integer, Integer, Integer[], Double, Double> newMaxT = null;
			// already met in previous iteration
			oldMax = oldMaxT.f4;
			visited.addAll(Arrays.asList(oldMaxT.f3));
			Iterator<Tuple5<Integer, Integer, Integer, Integer[], Double>> leftIt = decode2(first.iterator());
			while (leftIt.hasNext()) {
				Tuple5<Integer, Integer, Integer, Integer[], Double> leftT = leftIt.next();
				if (oldMax < leftT.f4) {
					newMax = leftT.f4;
					visited.addAll(new ArrayList<>(Arrays.asList(leftT.f3)));
					newMaxT = new Tuple6<>(leftT.f0, leftT.f1, leftT.f2, visited.toArray(new Integer[0]), newMax,
							oldMax);
				}
			}
			if (newMaxT != null) {
				out.collect(newMaxT);
			}
		}

		out.close();

	}

	private Iterator<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> decode(
			Iterator<Tuple3<String, String, Object[]>> it) {
		LinkedList<Tuple6<Integer, Integer, Integer, Integer[], Double, Double>> l = new LinkedList<>();
		while (it.hasNext()) {
			Tuple3<String, String, Object[]> value = it.next();
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

	private Iterator<Tuple5<Integer, Integer, Integer, Integer[], Double>> decode2(
			Iterator<Tuple3<String, String, Object[]>> it) {
		LinkedList<Tuple5<Integer, Integer, Integer, Integer[], Double>> l = new LinkedList<>();
		while (it.hasNext()) {
			Tuple3<String, String, Object[]> value = it.next();
			if (value.f0.equals("MightCloseLink")) {
				Integer f1 = (Integer) value.f2[0];
				Integer f2 = (Integer) value.f2[1];
				Integer f3 = (Integer) value.f2[2];
				Integer[] f4 = (Integer[]) value.f2[3];
				Double f5 = (Double) value.f2[4];
				l.add(new Tuple5<>(f1, f2, f3, f4, f5));
			}
		}
		return l.iterator();

	}

}

package prometheuxresearch.benchmark.vldb.experiment.sparkSQL.closeLink;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.spark.api.java.function.CoGroupFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import scala.collection.JavaConverters;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class CloseLinkCoGroupFunction implements CoGroupFunction<Row, Row, Row, Row> {

	private static final long serialVersionUID = 4872598219301341757L;

	@Override
	public Iterator<Row> call(Row key, Iterator<Row> left, Iterator<Row> right) throws Exception {
		if (!right.hasNext()) {
			// we do not have a delta
			return Collections.emptyIterator();
		}

		if (!left.hasNext()) {
			Map<Object, Object> contributors = new HashMap<>();
			Double sum = 0.0;
			Double maxCurrentValue = null;
			Set<Integer> visited = new HashSet<>();
			while (right.hasNext()) {
				Row currentRow = right.next();
				Object contributor = currentRow.get(4);
				Double currentValue = currentRow.getDouble(3);
				Double contributorValue = currentRow.getDouble(5);
				Double newMax = currentRow.getDouble(6);
				List<Object> currVisited = currentRow.getList(2);
				for (Object obj : currVisited) {
					visited.add((Integer) obj);
				}
				if (maxCurrentValue == null)
					maxCurrentValue = newMax;
				if (maxCurrentValue < newMax)
					maxCurrentValue = newMax;
				if (!contributors.containsKey(contributor)) {
					contributors.put(contributor, contributorValue);
					sum += currentValue;
				} else {
					Double oldContributorValue = (Double) contributors.get(contributor);
					if (contributorValue > oldContributorValue) {
						sum -= oldContributorValue;
						sum += contributorValue;
						contributors.put(contributor, contributorValue);
					}
				}
			}
			Row newRowContent = RowFactory.create(key.get(0), key.get(1), visited.toArray(new Integer[0]), sum,
					toScalaMap(contributors), maxCurrentValue);
			return Collections.singletonList(newRowContent).iterator();
		}

		Row currentRow = left.next();
		Set<Integer> visited = new HashSet<>(currentRow.getList(2));
		Double currentSum = currentRow.getDouble(3);
		Map<Object, Object> oldContributors = toJavaMap(currentRow.getMap(4));
		Map<Object, Object> newContributors = new HashMap<>(oldContributors);
		Double maxCurrentValue = currentRow.getDouble(5);

		while (right.hasNext()) {
			currentRow = right.next();
			Object contributor = currentRow.get(4);
			Double currentValue = currentRow.getDouble(3);
			Double contributorValue = currentRow.getDouble(5);
			Double newMax = currentRow.getDouble(6);
			List<Object> currVisited = currentRow.getList(2);
			for (Object obj : currVisited) {
				visited.add((Integer) obj);
			}
			if (maxCurrentValue < newMax)
				maxCurrentValue = newMax;
			if (!newContributors.containsKey(contributor)) {
				newContributors.put(contributor, contributorValue);
				currentSum += (Double) currentValue;
			} else {
				Double oldValue = (Double) newContributors.get(contributor);
				if (oldValue < contributorValue) {
					currentSum -= oldValue;
					currentSum += contributorValue;
					newContributors.put(contributor, contributorValue);
				}
			}

		}
		Row newRowContent = RowFactory.create(key.get(0), key.get(1), visited.toArray(new Integer[0]), currentSum,
				toScalaMap(newContributors), maxCurrentValue);
		return Collections.singletonList(newRowContent).iterator();
	}

	private scala.collection.mutable.Map<Object, Object> toScalaMap(Map<Object, Object> m) {
		return JavaConverters.mapAsScalaMapConverter(m).asScala();
	}

	private Map<Object, Object> toJavaMap(scala.collection.Map<Object, Object> map) {
		return scala.collection.JavaConverters.mapAsJavaMapConverter(map).asJava();
	}

}

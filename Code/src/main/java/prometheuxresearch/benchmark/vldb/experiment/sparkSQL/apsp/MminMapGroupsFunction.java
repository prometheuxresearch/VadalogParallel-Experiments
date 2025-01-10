package prometheuxresearch.benchmark.vldb.experiment.sparkSQL.apsp;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.spark.api.java.function.CoGroupFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

/**
 * All Pairs Shortest Paths
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 *
 */

public class MminMapGroupsFunction implements CoGroupFunction<Row, Row, Row, Row> {

	private static final long serialVersionUID = 206998460226316409L;

	@Override
	public Iterator<Row> call(Row key, Iterator<Row> left, Iterator<Row> right) throws Exception {
		if (!right.hasNext()) {
			return Collections.emptyIterator();
		}
		// we set the min cost to the max double value
		Double currentMinCost = Double.MAX_VALUE;
		Row currentRow = null;
		// we iterate over the new (joined) paths
		while (right.hasNext()) {
			currentRow = right.next();
			// if the new path has a minimium cost then the current, it is the new minimum
			// cost
			if (currentRow.getDouble(2) < currentMinCost) {
				currentMinCost = currentRow.getDouble(2);
			}
		}
		Row shortestPath = RowFactory.create(key.get(0), key.get(1), currentMinCost);
		// if in the previous iteration we did not find this group
		if (!left.hasNext()) {
			// then we have the new shortest path in the delta
			return Collections.singletonList(shortestPath).iterator();
		}

		@SuppressWarnings("unchecked")
		List<Row> resultList = IteratorUtils.toList(left);
		// we get the shortest path in the previous iterations, it is the lastly added
		Row previousShortestPath = resultList.get(resultList.size() - 1);
		Double previousMinShortestPathCost = previousShortestPath.getDouble(2);
		// if the current min cost is lower that the previous
		if (currentMinCost < previousMinShortestPathCost) {
			// the current shortest path is the new shortest path
			return Collections.singletonList(shortestPath).iterator();
		} else {
			// the previous shortest path remains the new shortest path
			return Collections.singletonList(previousShortestPath).iterator();
		}
	}

}

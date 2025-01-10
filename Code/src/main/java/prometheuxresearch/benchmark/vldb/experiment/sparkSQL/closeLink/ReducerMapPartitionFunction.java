package prometheuxresearch.benchmark.vldb.experiment.sparkSQL.closeLink;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.spark.api.java.function.MapPartitionsFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class ReducerMapPartitionFunction implements MapPartitionsFunction<Row, Row> {

	private static final long serialVersionUID = 3414581614689630226L;

	@Override
	public Iterator<Row> call(Iterator<Row> iterator) throws Exception {
		Map<Row, Row> outputRows = new HashMap<>();
		Map<Row, Double> bestRows = new HashMap<>();
		while (iterator.hasNext()) {
			Row currentRow = iterator.next();
			Row key = RowFactory.create(currentRow.get(0), currentRow.get(1));
			Double currentSum = (Double) currentRow.get(3);
			if (!bestRows.containsKey(key)) {
				bestRows.put(key, currentSum);
				outputRows.put(key, currentRow);
			} else {
				Double oldSum = bestRows.get(key);
				if (oldSum < currentSum) {
					bestRows.replace(key, currentSum);
					outputRows.replace(key, currentRow);
				}
			}
		}
		return outputRows.values().iterator();
	}

}

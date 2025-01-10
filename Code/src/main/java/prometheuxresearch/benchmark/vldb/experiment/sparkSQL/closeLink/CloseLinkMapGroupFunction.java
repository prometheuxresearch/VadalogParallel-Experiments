package prometheuxresearch.benchmark.vldb.experiment.sparkSQL.closeLink;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.spark.api.java.function.MapGroupsFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import scala.collection.JavaConverters;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class CloseLinkMapGroupFunction implements MapGroupsFunction<Row, Row, Row> {

	private static final long serialVersionUID = 2017466154372789488L;

	@Override
	public Row call(Row key, Iterator<Row> iterator) throws Exception {
		Map<Object, Object> contributors = new HashMap<>();
		Double sum = 0.0;
		Double maxValue = null;
		Integer[] array = new Integer[2];
		array[0] = key.getInt(0);
		array[1] = key.getInt(1);

		while (iterator.hasNext()) {
			Row currentRow = iterator.next();
			Object contributor = currentRow.get(3);
			Double currentValue = currentRow.getDouble(2);
			if (maxValue == null)
				maxValue = currentValue;
			if (currentValue > maxValue)
				maxValue = currentValue;
			if (!contributors.containsKey(contributor)) {
				contributors.put(contributor, currentValue);
				sum += currentValue;
			} else {
				Double oldValue = (Double) contributors.get(contributor);
				if (currentValue > oldValue) {
					sum -= oldValue;
					sum += oldValue;
					contributors.put(contributor, oldValue);
				}
			}
		}

		return RowFactory.create(key.get(0), key.get(1), array, sum, toScalaMap(contributors), maxValue);
	}

	private scala.collection.mutable.Map<Object, Object> toScalaMap(Map<Object, Object> m) {
		return JavaConverters.mapAsScalaMapConverter(m).asScala();
	}

}

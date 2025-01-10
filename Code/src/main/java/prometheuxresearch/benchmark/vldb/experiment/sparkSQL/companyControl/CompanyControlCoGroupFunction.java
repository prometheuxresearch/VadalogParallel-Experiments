package prometheuxresearch.benchmark.vldb.experiment.sparkSQL.companyControl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.apache.spark.api.java.function.CoGroupFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class CompanyControlCoGroupFunction implements CoGroupFunction<Row, Row, Row, Row> {

	private static final long serialVersionUID = 9092886420038160353L;

	@Override
	public Iterator<Row> call(Row key, Iterator<Row> left, Iterator<Row> right) throws Exception {
		if (!right.hasNext()) {
			return Collections.emptyIterator();
		}
		// in case the result is empty
		Double finalSum = 0.0;

		// in case the result exists we need to update it
		if (left.hasNext()) {
			finalSum = left.next().getDouble(2);
		}
		if (right.hasNext()) {
			while (right.hasNext()) {
				Row currRow = right.next();
				finalSum += currRow.getDouble(3);
			}
			return Arrays.asList(RowFactory.create(key.getInt(0), key.getInt(1), finalSum)).iterator();
		}
		return Collections.emptyIterator();
	}

}

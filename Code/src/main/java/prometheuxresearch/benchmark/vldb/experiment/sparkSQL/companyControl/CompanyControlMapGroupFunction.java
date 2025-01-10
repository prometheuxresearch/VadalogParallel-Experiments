package prometheuxresearch.benchmark.vldb.experiment.sparkSQL.companyControl;

import java.util.Iterator;

import org.apache.spark.api.java.function.MapGroupsFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class CompanyControlMapGroupFunction implements MapGroupsFunction<Row, Row, Row> {

	private static final long serialVersionUID = 7864877296294218008L;

	@Override
	public Row call(Row key, Iterator<Row> it) throws Exception {
		Double sum = 0.0;
		while (it.hasNext()) {
			sum += it.next().getDouble(3);
		}
		return RowFactory.create(key.getInt(0), key.getInt(1), sum);
	}

}

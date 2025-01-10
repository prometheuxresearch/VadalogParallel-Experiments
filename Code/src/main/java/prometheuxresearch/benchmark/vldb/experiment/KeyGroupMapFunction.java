package prometheuxresearch.benchmark.vldb.experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

/**
 * This Class is a MapFunction for grouping Rows under a same key
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class KeyGroupMapFunction implements MapFunction<Row, Row> {

	private static final long serialVersionUID = 1L;
	private List<Integer> keyGroupPositions = new ArrayList<>();

	public KeyGroupMapFunction(List<Integer> keyGroupPositions) {
		this.keyGroupPositions = keyGroupPositions;
	}

	@Override
	public Row call(Row row) throws Exception {
		return RowFactory
				.create(keyGroupPositions.stream().map(pos -> row.get(pos)).collect(Collectors.toList()).toArray());
	}

	public Encoder<Row> encoder() {
		StructType st = new StructType();
		st = st.add("key", DataTypes.LongType, true);
		return RowEncoder.apply(st);
	}

}

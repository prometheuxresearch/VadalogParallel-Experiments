package prometheuxresearch.benchmark.vldb.experiment;

import java.util.List;

import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class EncoderGenerator {

	public static Encoder<Row> createEncoderFromEncoder(Encoder<Row> from, List<Integer> interestedPositions) {
		StructField[] structFieldFrom = from.schema().fields();
		StructType structTypeTo = new StructType();
		for (int pos : interestedPositions) {
			structTypeTo = structTypeTo.add(structFieldFrom[pos]);
		}
		return RowEncoder.apply(structTypeTo);
	}

}

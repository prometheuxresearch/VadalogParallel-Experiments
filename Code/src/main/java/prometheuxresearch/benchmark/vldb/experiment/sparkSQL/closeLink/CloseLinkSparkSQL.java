package prometheuxresearch.benchmark.vldb.experiment.sparkSQL.closeLink;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.KeyValueGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.storage.StorageLevel;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class CloseLinkSparkSQL {

	private static Map<String, String> configProperties = new HashMap<>();
	private static String inputFilePath;
	private static String homeDir;

	public static void main(String[] args) throws Exception {
		// Parse command line parameters using ParameterTool
		ParameterTool params = ParameterTool.fromArgs(args);
		Map<String, String> properties = params.toMap();
		run(properties);
	}

	private static Encoder<Row> keyEncoder() {
		StructType st = new StructType();
		st = st.add("x", DataTypes.IntegerType, true);
		st = st.add("y", DataTypes.IntegerType, true);
		return RowEncoder.apply(st);
	}

	private static Encoder<Row> outputEncoder() {
		StructType st = new StructType();
		st = st.add("x", DataTypes.IntegerType, true);
		st = st.add("y", DataTypes.IntegerType, true);
		st = st.add("visited", DataTypes.createArrayType(DataTypes.IntegerType), true);
		st = st.add("c", DataTypes.DoubleType, true);
		st = st.add("contributors", DataTypes.createMapType(DataTypes.IntegerType, DataTypes.DoubleType), true);
		st = st.add("max", DataTypes.DoubleType, true);
		return RowEncoder.apply(st);
	}

	public static long run(Map<String, String> properties) {

		for (Map.Entry<String, String> entry : properties.entrySet()) {
			String paramName = entry.getKey();
			String paramValue = entry.getValue();
			if (paramName.equals("inputFilePath")) {
				// Access parameters using ParameterTool
				inputFilePath = paramValue;
			}
			if (paramName.equals("spark.homeDir")) {
				// Access parameters using ParameterTool
				homeDir = paramValue;
			} else {
				configProperties.put(paramName, paramValue);
			}
		}

		System.setProperty("hadoop.home.dir", homeDir);

		SparkConf sparkConf = new SparkConf();

		for (Map.Entry<String, String> entry : configProperties.entrySet()) {
			sparkConf = sparkConf.set(entry.getKey(), entry.getValue());
		}

		SparkSession spark = SparkSession.builder().config(sparkConf).getOrCreate();

		Dataset<Row> own = spark.read().format("csv").option("inferSchema", "true").option("header", "true")
				.load(inputFilePath);

		Dataset<Row> ownCopy = own.alias("ownCopy").withColumnRenamed(own.columns()[0], "y2")
				.withColumnRenamed(own.columns()[1], "z").withColumnRenamed(own.columns()[2], "w2");

		own = own.withColumnRenamed(own.columns()[0], "x").withColumnRenamed(own.columns()[1], "y")
				.withColumnRenamed(own.columns()[2], "w");

		System.out.println("Own size is:" + own.count());
		long startTimeMillis = System.currentTimeMillis();

		Column x = new Column("x");
		Column y = new Column("y");
		Column z = new Column("z");
		Column c = new Column("c");
		Column w = new Column("w");
		Column w1 = new Column("w1");
		Column w2 = new Column("w2");
		Column y2 = new Column("y2");
		Column visited = new Column("visited");
		Column contributorValue = new Column("contributorValue");
		Column max = new Column("max");

		Column joinCondition = y.equalTo(y2);

		// closeLinks(x,y,c) :- Own(x,y,w), c=msum(w,<y>).
		CloseLinkMapGroupFunction ma = new CloseLinkMapGroupFunction();
		KeyGroupMapFunction keys = new KeyGroupMapFunction(Arrays.asList(0, 1));

		own = own.select(x, y, w, y);
		Dataset<Row> closeLinksWithContributions = own.groupByKey(keys, keyEncoder()).mapGroups(ma, outputEncoder());

		// closeLinks(x,z,c) :- closeLinks(x,y,w1), Own(y,z,w2), c=msum(w1*w2,<y>).

		Dataset<Row> closeLinksDelta = closeLinksWithContributions.select(x, y, visited, c, max).withColumnRenamed("c",
				"w1");
		CloseLinkCoGroupFunction cg = new CloseLinkCoGroupFunction();
		ReducerMapPartitionFunction r = new ReducerMapPartitionFunction();

		while (!closeLinksDelta.isEmpty()) {
			closeLinksWithContributions = closeLinksWithContributions.localCheckpoint();

			// groupVariable1, groupVariable2, Multiplication, contributor, contributorValue
			closeLinksDelta = closeLinksDelta.join(ownCopy, joinCondition).select(x, z, visited,
					w1.multiply(w2).alias("c"), y, max.alias("contributorValue"), max.multiply(w2).alias("max"));

			Column filterCond = functions.not(functions.array_contains(visited, z)).and(x.notEqual(z));
			closeLinksDelta = closeLinksDelta.where(filterCond).select(x, z,
					functions.array_union(visited, functions.array(z)).alias("visited"), c, y, contributorValue, max);

			KeyValueGroupedDataset<Row, Row> resultKey = closeLinksWithContributions.groupByKey(keys, keyEncoder());
			KeyValueGroupedDataset<Row, Row> deltaKey = closeLinksDelta.groupByKey(keys, keyEncoder());
			closeLinksDelta = resultKey.cogroup(deltaKey, cg, outputEncoder()).alias("delta");

			closeLinksWithContributions = closeLinksWithContributions.union(closeLinksDelta);
			closeLinksWithContributions = closeLinksWithContributions.repartition(x, y).mapPartitions(r,
					closeLinksWithContributions.encoder());

			closeLinksDelta = closeLinksDelta.select(x, y, visited, c, max).withColumnRenamed("c", "w1");
			closeLinksDelta = closeLinksDelta.localCheckpoint();
		}

		Dataset<Row> closeLinks = closeLinksWithContributions.select(x, y, c).where(c.geq(functions.lit(0.5)));
		long countcloseLinks = closeLinks.count();

		System.out.println("The number of close link relationships found is " + countcloseLinks);

		long endTimeMillis = System.currentTimeMillis();
		long executionTimeMillis = endTimeMillis - startTimeMillis;

		if (configProperties.get("execution.mode").equals("test")) {
			closeLinks.show();
		}

		System.out.println("Execution time in milliseconds: " + executionTimeMillis);
		spark.close();
		return countcloseLinks;
	}

}

package prometheuxresearch.benchmark.vldb.experiment.sparkSQL.companyControl;

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

import prometheuxresearch.benchmark.vldb.experiment.sparkSQL.closeLink.KeyGroupMapFunction;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class CompanyControlSparkSQL {

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
		st = st.add("csd_0", DataTypes.IntegerType, true);
		st = st.add("csd_1", DataTypes.IntegerType, true);
		return RowEncoder.apply(st);
	}

	private static Encoder<Row> outputEncoder() {
		StructType st = new StructType();
		st = st.add("tcsd_0", DataTypes.IntegerType, true);
		st = st.add("tcsd_1", DataTypes.IntegerType, true);
		st = st.add("tcsd_2", DataTypes.DoubleType, true);
		return RowEncoder.apply(st);
	}

	private static Dataset<Row> renamer(Dataset<Row> dataset, String columnPrefix) {
		for (int i = 0; i < dataset.columns().length; i++) {
			dataset = dataset.withColumnRenamed(dataset.columns()[i], columnPrefix + "_" + i);
		}

		return dataset;
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

		own = renamer(own, "o");

		System.out.println("Own size is:" + own.count());
		long startTimeMillis = System.currentTimeMillis();

		KeyGroupMapFunction keys = new KeyGroupMapFunction(Arrays.asList(0, 1));

		Dataset<Row> controlledSharesResult = own.where(functions.not(new Column("o_0").eqNullSafe(new Column("o_1"))))
				.select(new Column("o_0"), new Column("o_1"), new Column("o_1"), new Column("o_2"));
		controlledSharesResult = renamer(controlledSharesResult, "csr");
		Dataset<Row> controlledSharesDelta = renamer(controlledSharesResult.alias("csd"), "csd");

		Dataset<Row> totalControlledSharesResult = controlledSharesDelta.groupByKey(keys, keyEncoder())
				.mapGroups(new CompanyControlMapGroupFunction(), outputEncoder());

		totalControlledSharesResult = renamer(totalControlledSharesResult.alias("tcsr"), "tcsr");

		Dataset<Row> totalControlledSharesDelta = renamer(totalControlledSharesResult, "tcsd");

		Dataset<Row> controlResult = totalControlledSharesResult.where(new Column("tcsr_2").geq(functions.lit(0.5)))
				.select(new Column("tcsr_0").alias("cr_0"), new Column("tcsr_1").alias("cr_1"));
		Dataset<Row> controlDelta = renamer(controlResult.alias("cd"), "cd");

		while (!controlDelta.isEmpty()) {
			Column joinExpr = new Column("cd_1").eqNullSafe(new Column("o_0"));

			controlledSharesDelta = controlDelta.join(own, joinExpr).select(new Column("cd_0"), new Column("o_1"),
					new Column("o_0"), new Column("o_2"));

			Column filterExpr = new Column("csd_0").notEqual(new Column("csd_1"))
					.and(new Column("csd_0").notEqual(new Column("csd_2")));

			controlledSharesDelta = renamer(controlledSharesDelta, "csd").where(filterExpr).distinct();
			controlledSharesDelta = controlledSharesDelta.except(controlledSharesResult);

			controlledSharesDelta = controlledSharesDelta.localCheckpoint();

			controlledSharesResult = controlledSharesResult.union(controlledSharesDelta);

			KeyValueGroupedDataset<Row, Row> resultKey = totalControlledSharesResult.groupByKey(keys, keyEncoder());
			KeyValueGroupedDataset<Row, Row> deltaKey = controlledSharesDelta.groupByKey(keys, keyEncoder());

			totalControlledSharesDelta = resultKey.cogroup(deltaKey, new CompanyControlCoGroupFunction(),
					outputEncoder());
			totalControlledSharesDelta = totalControlledSharesDelta.localCheckpoint();

			totalControlledSharesResult = renamer(totalControlledSharesResult, "tcsr");
			totalControlledSharesResult = totalControlledSharesResult.union(totalControlledSharesDelta)
					.groupBy(new Column("tcsr_0"), new Column("tcsr_1")).max("tcsr_2").alias("tcsr_2");
			totalControlledSharesResult = renamer(totalControlledSharesResult, "tcsr");

			controlDelta = totalControlledSharesDelta.where(new Column("tcsd_2").geq(functions.lit(0.5)))
					.select(new Column("tcsd_0"), new Column("tcsd_1"));
			controlDelta = renamer(controlDelta, "cd");

		}

		long countControl = totalControlledSharesResult.where(new Column("tcsr_2").geq(functions.lit(0.5))).count();

		System.out.println("The number of control relationships found is " + countControl);
		long endTimeMillis = System.currentTimeMillis();
		long executionTimeMillis = endTimeMillis - startTimeMillis;

		System.out.println("Execution time in milliseconds: " + executionTimeMillis);

		spark.close();

		return countControl;

	}

}

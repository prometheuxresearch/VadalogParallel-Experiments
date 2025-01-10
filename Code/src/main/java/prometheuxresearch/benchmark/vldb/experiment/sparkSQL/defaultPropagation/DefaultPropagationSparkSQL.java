package prometheuxresearch.benchmark.vldb.experiment.sparkSQL.defaultPropagation;

import java.util.HashMap;
import java.util.Map;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class DefaultPropagationSparkSQL {

	private static Map<String, String> configProperties = new HashMap<>();
	private static String creditExposureFilePath;
	private static String securityFilePath;
	private static String loanFilePath;
	private static String homeDir;

	public static void main(String[] args) throws Exception {
		// Parse command line parameters using ParameterTool
		ParameterTool params = ParameterTool.fromArgs(args);
		Map<String, String> properties = params.toMap();
		run(properties);

	}

	public static long run(Map<String, String> properties) {

		for (Map.Entry<String, String> entry : properties.entrySet()) {
			String paramName = entry.getKey();
			String paramValue = entry.getValue();
			if (paramName.equals("creditExposurePath")) {
				// Access parameters using ParameterTool
				creditExposureFilePath = paramValue;
			}
			if (paramName.equals("securityPath")) {
				// Access parameters using ParameterTool
				securityFilePath = paramValue;
			}
			if (paramName.equals("loanPath")) {
				// Access parameters using ParameterTool
				loanFilePath = paramValue;
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

		Dataset<Row> creditExp = spark.read().format("csv").option("inferSchema", "true").option("header", "true")
				.load(creditExposureFilePath);

		creditExp = creditExp.withColumnRenamed(creditExp.columns()[0], "ce_0")
				.withColumnRenamed(creditExp.columns()[1], "ce_1");

		Dataset<Row> security = spark.read().format("csv").option("inferSchema", "true").option("header", "true")
				.load(securityFilePath);

		Dataset<Row> loan = spark.read().format("csv").option("inferSchema", "true").option("header", "true")
				.load(loanFilePath);

		System.out.println("creditExposure size is:" + creditExp.count());
		System.out.println("security size is:" + security.count());
		System.out.println("loan size is:" + loan.count());
		long startTimeMillis = System.currentTimeMillis();

		security = security.withColumnRenamed(security.columns()[0], "s_0")
				.withColumnRenamed(security.columns()[1], "s_1").withColumnRenamed(security.columns()[2], "s_2")
				.where(new Column("s_2").geq(functions.lit(0.3)));

		loan = loan.withColumnRenamed(loan.columns()[0], "l_0").withColumnRenamed(loan.columns()[1], "l_1")
				.withColumnRenamed(loan.columns()[2], "l_2").where(new Column("l_2").geq(functions.lit(0.5)));

		Column defaultCol1 = new Column("default_0");
		Column defaultCol2 = new Column("default_1");
		Column defaultCol3 = new Column("default_2");
		Column defaultCol4 = new Column("default_3");

		Column nullGenerator = functions
				.concat_ws("_", functions.lit("z"), functions.hash(functions.concat_ws("-",
						defaultCol1.cast(DataTypes.StringType), defaultCol2.cast(DataTypes.StringType))))
				.alias("default_2");

		Dataset<Row> defaultInit = creditExp.where(new Column("ce_1").gt(functions.lit(0.5)))
				.select(new Column("ce_0").alias("default_0"), new Column("ce_0").alias("default_1"));

		Dataset<Row> defaultResult = defaultInit.select(defaultCol1, defaultCol2, nullGenerator);

		Column cols[] = new Column[3];
		cols[0] = defaultCol1;
		cols[1] = defaultCol2;
		cols[2] = defaultCol3;
		defaultResult = defaultResult.select(defaultCol1, defaultCol2, defaultCol3,
				getReasoningKey(cols).alias("default_3"));
		defaultResult = defaultResult.dropDuplicates("default_3");

		Dataset<Row> defaultDelta = defaultResult.alias("defaultDelta");
		defaultResult = defaultResult.withColumnRenamed(defaultResult.columns()[0], "d_0")
				.withColumnRenamed(defaultResult.columns()[1], "d_1")
				.withColumnRenamed(defaultResult.columns()[2], "d_2")
				.withColumnRenamed(defaultResult.columns()[3], "d_3");

		while (!defaultDelta.isEmpty()) {
			Column joinExpr1 = defaultCol1.eqNullSafe(new Column("l_0"));
			Column joinExpr2 = defaultCol1.eqNullSafe(new Column("s_0"));
			Column joinExpr3 = defaultCol4.eqNullSafe(new Column("d_3"));

			Dataset<Row> newDeltaApp = defaultDelta.join(loan, joinExpr1).select(new Column("l_1").alias("default_0"),
					defaultCol2);

			Dataset<Row> newDelta = newDeltaApp.select(defaultCol1, defaultCol2, nullGenerator);

			newDelta = newDelta.union(
					defaultDelta.join(security, joinExpr2).select(new Column("s_1").alias("default_0"), defaultCol2)
							.select(defaultCol1, defaultCol2, nullGenerator));

			newDelta = newDelta.select(defaultCol1, defaultCol2, defaultCol3, getReasoningKey(cols).alias("default_3"))
					.dropDuplicates("default_3");

			defaultDelta = newDelta.join(defaultResult, joinExpr3, "leftanti").select(defaultCol1, defaultCol2,
					defaultCol3, defaultCol4);

			defaultDelta = defaultDelta.unpersist(false);
			defaultDelta = defaultDelta.localCheckpoint();

			defaultResult = defaultResult.union(defaultDelta);

		}

		long countDefaultResult = defaultResult.count();

		System.out.println("The number of close link relationships found is " + countDefaultResult);

		long endTimeMillis = System.currentTimeMillis();
		long executionTimeMillis = endTimeMillis - startTimeMillis;

		System.out.println("Execution time in milliseconds: " + executionTimeMillis);
		spark.close();
		return countDefaultResult;
	}

	public static Column getReasoningKey(Column[] newFact) {
		// We trasform the list of column into an array
		Column allFields = functions.array(newFact);
		// We filter the nulls
		Column only_nulls = functions
				.array_distinct(
						functions.filter(allFields, (element) -> element.cast(DataTypes.StringType).startsWith("z_")))
				.alias("nullValues");
		// the size of the nulls
		Column size = functions.size(only_nulls).alias("size");
		// a sequence 1,2,3 of the nulls in the current fact
		Column sequence = functions.sequence(functions.lit(1), size);

		// we map each null with a number in the sequence
		Column map_column = functions
				.when(size.notEqual(functions.lit(0)), functions.map_from_arrays(only_nulls, sequence))
				.otherwise(functions.map_from_arrays(only_nulls, only_nulls)).alias("nullRenaming");

		Column key_array = functions.transform(allFields, (element) -> {
			Column mapValue = functions
					.when(functions.map_contains_key(map_column, element),
							functions.concat(functions.lit("z_"), functions.element_at(map_column, element)))
					.otherwise(element);
			return mapValue;
		});

		return functions.hash(key_array);
	}

}

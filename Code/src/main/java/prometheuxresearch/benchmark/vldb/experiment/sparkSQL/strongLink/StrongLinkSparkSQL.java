package prometheuxresearch.benchmark.vldb.experiment.sparkSQL.strongLink;

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
 *  * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 *
 */

public class StrongLinkSparkSQL {
	private static Map<String, String> configProperties = new HashMap<>();
	private static String personFilePath;
	private static String companyFilePath;
	private static String keyPersonFilePath;
	private static String controlFilePath;
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
			if (paramName.equals("personPath")) {
				// Access parameters using ParameterTool
				personFilePath = paramValue;
			}
			if (paramName.equals("companyPath")) {
				// Access parameters using ParameterTool
				companyFilePath = paramValue;
			}
			if (paramName.equals("keyPersonPath")) {
				// Access parameters using ParameterTool
				keyPersonFilePath = paramValue;
			}
			if (paramName.equals("controlPath")) {
				// Access parameters using ParameterTool
				controlFilePath = paramValue;
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
		sparkConf.set("spark.cleaner.periodicGC.enabled", "true");

		SparkSession spark = SparkSession.builder().config(sparkConf).getOrCreate();

		Dataset<Row> person = spark.read().format("csv").option("inferSchema", "true").option("header", "true")
				.load(personFilePath);

		person = person.withColumnRenamed(person.columns()[0], "p_0");

		Dataset<Row> company = spark.read().format("csv").option("inferSchema", "true").option("header", "true")
				.load(companyFilePath);

		company = company.withColumnRenamed(company.columns()[0], "c_0");

		Dataset<Row> keyPerson = spark.read().format("csv").option("inferSchema", "true").option("header", "true")
				.load(keyPersonFilePath);

		keyPerson = keyPerson.withColumnRenamed(keyPerson.columns()[0], "k_0").withColumnRenamed(keyPerson.columns()[1],
				"k_1");

		Dataset<Row> control = spark.read().format("csv").option("inferSchema", "true").option("header", "true")
				.load(controlFilePath);

		control = control.withColumnRenamed(control.columns()[0], "c_0").withColumnRenamed(control.columns()[1], "c_1");

		System.out.println("control size is:" + control.count());
		System.out.println("person size is:" + person.count());
		System.out.println("company size is:" + company.count());
		System.out.println("key person size is:" + keyPerson.count());
		long startTimeMillis = System.currentTimeMillis();

		Column pscCol1 = new Column("psc0");
		Column pscCol2 = new Column("psc1");
		Column pscCol3 = new Column("psc2");
		Column pscCol4 = new Column("psc3");

		Column nullGenerator = functions
				.concat_ws("_", functions.lit("z"), functions.hash(functions.concat_ws("-", pscCol1, pscCol2)))
				.alias("psc2");

		Column joinExpr = new Column("k_1").eqNullSafe(new Column("p_0"));

		Column cols[] = new Column[3];
		cols[0] = pscCol1;
		cols[1] = pscCol2;
		cols[2] = pscCol3;

		Dataset<Row> pscInit1 = keyPerson.join(person).where(joinExpr)
				.select(new Column("k_0").alias("psc0"), new Column("k_0").alias("psc1"),
						new Column("k_1").alias("psc2"))
				.select(pscCol1, pscCol2, pscCol3, getReasoningKey(cols).alias("psc3"));

		Dataset<Row> pscInit2 = company.select(new Column("c_0").alias("psc0"), new Column("c_0").alias("psc1"))
				.select(pscCol1, pscCol2, nullGenerator)
				.select(pscCol1, pscCol2, pscCol3, getReasoningKey(cols).alias("psc3"));

		Dataset<Row> pscDelta = pscInit1.union(pscInit2).dropDuplicates("psc3");

		Dataset<Row> pscResult = pscDelta.alias("delta").withColumnRenamed(pscDelta.columns()[0], "p_0")
				.withColumnRenamed(pscDelta.columns()[1], "p_1").withColumnRenamed(pscDelta.columns()[2], "p_2")
				.withColumnRenamed(pscDelta.columns()[3], "p_3");

		while (!pscDelta.isEmpty()) {
			joinExpr = new Column("c_0").eqNullSafe(new Column("psc0"));

			Dataset<Row> app = control.join(pscDelta, joinExpr).select(new Column("c_1").alias("psc0"),
					new Column("psc1").alias("psc1"), new Column("psc2"));

			pscDelta = app.select(pscCol1, pscCol2, pscCol3, getReasoningKey(cols).alias("psc3"))
					.dropDuplicates("psc3");

			joinExpr = new Column("psc3").eqNullSafe(new Column("p_3"));

			pscDelta = pscDelta.unpersist(false);
			pscDelta = pscDelta.join(pscResult, joinExpr, "leftanti").select(pscCol1, pscCol2, pscCol3, pscCol4);

			pscDelta = pscDelta.localCheckpoint();

			pscResult = pscResult.union(pscDelta);
		}

		joinExpr = new Column("p_1").eqNullSafe(new Column("psc_1"))
				.and(new Column("p_2").eqNullSafe(new Column("psc_2")));

		Dataset<Row> pscResultCopy = pscResult.alias("pscResult").withColumnRenamed(pscResult.columns()[0], "psc_0")
				.withColumnRenamed(pscResult.columns()[1], "psc_1").withColumnRenamed(pscResult.columns()[2], "psc_2")
				.withColumnRenamed(pscResult.columns()[3], "psc_3");

		Dataset<Row> mightSL = pscResult.join(pscResultCopy).where(joinExpr)
				.groupBy(new Column("p_0"), new Column("psc_0"), new Column("p_1")).count();

		Dataset<Row> strongLink = mightSL.withColumnRenamed(mightSL.columns()[0], "sl_0")
				.withColumnRenamed(mightSL.columns()[1], "sl_1").withColumnRenamed(mightSL.columns()[2], "sl_2")
				.withColumnRenamed(mightSL.columns()[3], "sl_3").where(new Column("sl_3").geq(functions.lit(4)));

		long countStrongLink = strongLink.count();

		System.out.println("The number of strong link relationships found is " + countStrongLink);

		long endTimeMillis = System.currentTimeMillis();
		long executionTimeMillis = endTimeMillis - startTimeMillis;

		System.out.println("Execution time in milliseconds: " + executionTimeMillis);
		spark.close();
		return countStrongLink;

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

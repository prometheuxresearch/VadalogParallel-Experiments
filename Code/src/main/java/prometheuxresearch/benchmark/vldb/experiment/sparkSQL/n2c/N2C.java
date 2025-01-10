package prometheuxresearch.benchmark.vldb.experiment.sparkSQL.n2c;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

import prometheuxresearch.benchmark.vldb.experiment.SparkSessionManager;

/**
 * Non-2-Colorability
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 *
 */

public class N2C {

	private String inputFilePath;

	public N2C(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	public String evaluate() {

		SparkSessionManager.getInstance().createNewSparkSession();
		SparkSession spark = SparkSessionManager.getInstance().getSparkSession();

		/* Reading the inputs from csv */
		/* Int, Int, Double */
		Dataset<Row> weightedEdge = spark.read().format("csv").option("inferSchema", "true").load(this.inputFilePath)
				.withColumnRenamed("`_c0`", "edge_0").withColumnRenamed("`_c1`", "edge_1");

		/*
		 * We initialize the N2C as all edges excluding those have same X and Y
		 */
		Dataset<Row> N2C = weightedEdge.where((weightedEdge.col("edge_0").notEqual(weightedEdge.col("edge_1"))))
				.select(new Column("edge_0").as("n2c_0"), new Column("edge_1").as("n2c_1"),
						functions.lit("ODD").as("n2c_2"))
				.cache();

		// we initialize the delta as the current N2C
		Dataset<Row> deltaN2C = N2C.select(new Column("n2c_0").as("delta_n2c_0"), new Column("n2c_1").as("delta_n2c_1"),
				new Column("n2c_2").as("delta_n2c_2"));

		boolean isTerminated = false;

		while (!isTerminated) {

			// we perform the join between n2c[1] and edge[0]
			deltaN2C = deltaN2C.join(weightedEdge, new Column("delta_n2c_1").equalTo(new Column("edge_0"))).select(
					new Column("delta_n2c_0"), new Column("edge_1").as("delta_n2c_1"),
					/*
					 * If we are on a ODD path, the new path will be EVEN, and vice-versa
					 */
					functions.when(new Column("delta_n2c_2").equalTo("ODD"), "EVEN").otherwise("ODD")
							.as("delta_n2c_2"));

			// we exclude the duplicates from the delta the duplicates
			deltaN2C = deltaN2C.distinct().except(N2C).localCheckpoint();

			// if we do not have new paths we terminate
			isTerminated = deltaN2C.isEmpty();

			// we union the new paths to N2C
			N2C = N2C.union(deltaN2C);
		}
		long N2CCount = N2C.distinct().count();
		SparkSessionManager.getInstance().closeSparkSession();
		return String.valueOf(N2CCount);

	}

}

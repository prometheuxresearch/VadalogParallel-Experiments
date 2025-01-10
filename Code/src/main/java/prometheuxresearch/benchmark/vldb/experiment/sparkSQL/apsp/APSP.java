package prometheuxresearch.benchmark.vldb.experiment.sparkSQL.apsp;

import java.util.Arrays;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.KeyValueGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import prometheuxresearch.benchmark.vldb.experiment.KeyGroupMapFunction;
import prometheuxresearch.benchmark.vldb.experiment.SparkSessionManager;

/**
 * All Pairs Shortest Paths
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

public class APSP {
	
	private String inputFilePath;
	
	public APSP(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}
	
	public String evaluate() {
		
		SparkSessionManager.getInstance().createNewSparkSession();
		SparkSession spark = SparkSessionManager.getInstance().getSparkSession();

		/* Reading the inputs from csv */
		/* Int, Int, Double */
		Dataset<Row> weightedEdge = spark.read().format("csv")
				.option("inferSchema", "true")
				.load(this.inputFilePath)
				.withColumnRenamed("`_c0`", "edge_0")
				.withColumnRenamed("`_c1`", "edge_1")
				.withColumnRenamed("`_c2`", "edge_2");
		
		/*
		 * We initialize the APSP as all edges, their cost, excluding those have same X and Y
		 */
		Dataset<Row> APSP = weightedEdge.where((weightedEdge.col("edge_0").notEqual(weightedEdge.col("edge_1"))))
				.select(new Column("edge_0").as("apsp_0"),
						new Column("edge_1").as("apsp_1"),
						new Column("edge_2").as("apsp_2"))
						.cache();
		
		// we will use this function to co group by key and perform min
		KeyGroupMapFunction keyGroupMapFunction = new KeyGroupMapFunction(Arrays.asList(0,1));

		// we initialize the delta as the current APSP
		Dataset<Row> deltaAPSP = APSP
				.select(new Column("apsp_0").as("delta_apsp_0"),
						new Column("apsp_1").as("delta_apsp_1"),
						new Column("apsp_2").as("delta_apsp_2"));
		
		boolean isTerminated = false;
		
		while (!isTerminated) {
			
			// we perform the join between apsp[1] and edge[0]
			Dataset<Row> apsp2 = deltaAPSP
					.join(weightedEdge, new Column("delta_apsp_1").equalTo(new Column("edge_0")))
					// we exclude new edges with where X and Z are the same
					.where((new Column("delta_apsp_0").notEqual(new Column("edge_1"))))
					
					.select(
					new Column("delta_apsp_0"),
					new Column("edge_1").as("delta_apsp_1"),
					/*
					 * we want the sum between the cost of the path from X to Y and the cost of the
					 * from Y to Z
					 */
					new Column("delta_apsp_2").plus(new Column("edge_2")).as("delta_apsp_2"));

				// we prepare the key groups for the join result
				KeyValueGroupedDataset<Row, Row> apsp2GroupByKey = apsp2
						.groupByKey(keyGroupMapFunction, keyGroupMapFunction.encoder());
				
				// we prepare the key groups for the current APSP
				KeyValueGroupedDataset<Row, Row> APSPGroupByKey = APSP
						.groupByKey(keyGroupMapFunction, keyGroupMapFunction.encoder());

				// we co group these two and we get the min cost for each group
				deltaAPSP = APSPGroupByKey
						.cogroup(apsp2GroupByKey, new MminMapGroupsFunction(),
								deltaAPSP.encoder());
				
				// we exclude from the delta the new paths which we just knew having that cost in previous iterations
				deltaAPSP = deltaAPSP.except(APSP).localCheckpoint();
				
				// if we do not have new paths we terminate
				isTerminated = deltaAPSP.isEmpty();
				
				// we union the new paths to APSP
				APSP = APSP.union(deltaAPSP);
			}
			long APSPCount = APSP.count();
			SparkSessionManager.getInstance().closeSparkSession();
			return String.valueOf(APSPCount);

		}

}

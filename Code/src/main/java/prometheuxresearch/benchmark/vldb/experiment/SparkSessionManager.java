package prometheuxresearch.benchmark.vldb.experiment;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class SparkSessionManager {

	private static SparkSessionManager instance;
	private SparkSession spark;
	private SparkConf sparkConf;

	private SparkSessionManager() {
		this.sparkConf = SparkConfigurationManager.getInstance().getSparkConf();
	}

	public static SparkSessionManager getInstance() {
		if (instance == null) {
			instance = new SparkSessionManager();
		}
		return instance;
	}

	public void createNewSparkSession() {
		if (this.spark == null) {
			this.spark = SparkSession.builder().config(this.sparkConf).getOrCreate();
			this.setCheckPointDir();
		}
	}

	public void setCheckPointDir() {
		String checkpointDir = "checkpoints";
		this.spark.sparkContext().setCheckpointDir(checkpointDir);
	}

	public SparkSession getSparkSession() {
		if (this.spark == null) {
			throw new PrometheuxRuntimeException("Spark Session Is Null");
		}
		return this.spark;
	}

	public void stopSparkSession() {
		if (this.spark != null) {
			this.getSqlContext().clearCache();
			this.spark.stop();
		}
	}

	public void closeSparkSession() {
		if (this.spark == null) {
			return;
		}
		this.spark.close();
		this.spark = null;
		SparkConfigurationManager.getInstance().reset();
	}

	public SQLContext getSqlContext() {
		this.createNewSparkSession();
		return this.getSparkSession().sqlContext();
	}

}

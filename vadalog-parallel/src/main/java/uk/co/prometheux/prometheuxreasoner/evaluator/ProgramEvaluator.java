package uk.co.prometheux.prometheuxreasoner.evaluator;

import org.apache.spark.sql.SparkSession;

import uk.co.prometheux.prometheuxreasoner.common.SparkSessionManager;

/**
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public abstract class ProgramEvaluator {

	protected SparkSession spark;

	public abstract void evaluate();

	public void startUp() {
		System.out.println("Starting up distributed context");
		SparkSessionManager.getInstance().createNewSparkSession();
		spark = SparkSessionManager.getInstance().getSparkSession();
	}

	public void cleanUp() {
		System.out.println("Cleaning up distributed context");
		SparkSessionManager.getInstance().stopSparkSession();
		SparkSessionManager.getInstance().closeSparkSession();
	}
	
}

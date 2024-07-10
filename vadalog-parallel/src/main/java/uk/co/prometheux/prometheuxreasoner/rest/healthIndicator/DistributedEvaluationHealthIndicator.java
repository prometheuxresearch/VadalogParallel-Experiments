package uk.co.prometheux.prometheuxreasoner.rest.healthIndicator;

import org.apache.spark.sql.SparkSession;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestTemplate;

import uk.co.prometheux.prometheuxreasoner.common.ConfigurationManager;
import uk.co.prometheux.prometheuxreasoner.common.SparkSessionManager;
//import uk.co.prometheux.prometheuxreasoner.livy.rest.LivyRestAPIService;

/**
 * An HealthChecker class that monitors the health status of Vadalog when
 * running with distributed mode evaluation
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

@Component("Vadalog Distributed")
public class DistributedEvaluationHealthIndicator implements HealthIndicator {

	private static final String VADALOG_DISTRIBUTED = "Vadalog Distributed";
	private RestTemplate restTemplate;

	@Override
	public Health health() {
		/*
		 * Check if Vadalog Parallel job submission is through Livy Service
		 */
		boolean isLivy = ConfigurationManager.getInstance().getProperty("dist.restService").equals("livy");
		if (isLivy) {
//			String message = VADALOG_DISTRIBUTED + " with Livy Service Connection";
//			try {
//				LivyRestAPIService.getInstance().getAllLivySessions();
//				return Health.up().withDetail(message, "OK").build();
//			} catch (Exception e) {
				return Health.down(null).withDetail("", "DOWN").build();
//			}
			/*
			 * Health check from the spark master
			 */
		} else {
			String sparkMaster = ConfigurationManager.getInstance().getProperty("spark.master");
			// Check Spark Standalone cluster
			if (sparkMaster.startsWith("spark://")) {
				String sparkStandaloneUrl = sparkMaster.substring("spark://".length());
				return checkSparkStandalone(sparkStandaloneUrl);
			} else if (sparkMaster.startsWith("yarn")) {
				// Check Spark YARN cluster
				String yarnResourceManagerAddress = ConfigurationManager.getInstance()
						.getProperty("spark.hadoop.yarn.resourcemanager.address");
				return checkYarnResourceManager(yarnResourceManagerAddress);
			} else if (sparkMaster.startsWith("local")) {
				// Check local Spark session
				return checkLocalSpark();
			} else {
				// Unsupported or unknown mode
				return Health.unknown().build();
			}
		}
	}

	/**
	 * Perform health check for Spark standalone
	 */
	private Health checkSparkStandalone(String url) {
		// Perform health check for Spark standalone
		String message = VADALOG_DISTRIBUTED + " with Spark Standalone";
		try {
			restTemplate.getForEntity(url, String.class);
			return Health.up().withDetail(message, "OK").build();
		} catch (Exception e) {
			return Health.down(e).withDetail(message, "DOWN").build();
		}
	}

	/**
	 * Perform health check for YARN ResourceManager
	 */
	private Health checkYarnResourceManager(String url) {
		String message = VADALOG_DISTRIBUTED + " with YARN";
		try {
			restTemplate.getForEntity(url, String.class);
			return Health.up().withDetail(message, "OK").build();
		} catch (Exception e) {
			return Health.down(e).withDetail(message, "DOWN").build();
		}
	}

	/**
	 * Perform health check for local Spark session
	 */
	private Health checkLocalSpark() {
		String message = VADALOG_DISTRIBUTED + " with Local Spark Session";
		try {
			SparkSession spark = SparkSessionManager.getInstance().getSparkSession();
			boolean isLocal = spark.sparkContext().isLocal();
			if (isLocal) {
				return Health.up().withDetail(message, "OK").build();
			} else {
				return Health.unknown().withDetail(message, "UNKNOWN").build();
			}
		} catch (Exception e) {
			return Health.down(e).withDetail(message, "DOWN").build();
		}
	}
}

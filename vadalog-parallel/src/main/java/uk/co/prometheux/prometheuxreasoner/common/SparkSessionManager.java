package uk.co.prometheux.prometheuxreasoner.common;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.RuntimeConfig;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;
import uk.co.prometheux.prometheuxreasoner.common.io.FileUtils;

/**
 * This Class manages the Spark Session. The SparkConf for the Session are
 * retrieved populated from the config prop, that are loaded from configuration
 * file when the Spark Session is created. These properties can be set by
 * annotation if any, else default properties are considered
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class SparkSessionManager {

	private static SparkSessionManager instance;
	private SparkSession spark;
	private SparkConf sparkConf;
	private boolean isLivySparkSession = ConfigurationManager.getInstance().getProperty("restService").equals("livy");

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
			this.setHadoopHomeDir();
			this.spark = SparkSession.builder().config(this.sparkConf).getOrCreate();
			this.setCheckPointDir();
//			this.configureLogLevel();
		}
	}

//	private void configureLogLevel() {
//		String logLevel = ConfigurationManager.getInstance().getProperty("logLevel");
//		this.spark.sparkContext().setLogLevel(logLevel);
//		String appLog = ConfigurationManager.getInstance().getProperty("appLog");
//		Logger.getLogger(appLog).setLevel(Level.OFF);
//	}

	private void setHadoopHomeDir() {
		String separator = System.getProperty("file.separator");
		String username = System.getProperty("user.name");
		String hadoopHomeDir = "";
		if (separator.equals("\\"))
			hadoopHomeDir = "C:\\Users\\" + username;
		else
			hadoopHomeDir = "/home/" + username + "/hadoop";
		System.setProperty("hadoop.home.dir", hadoopHomeDir);
	}

	public void setCheckPointDir() {
		String breakLineageStrategy = ConfigurationManager.getInstance().getProperty("breakLineageStrategy");
		if (breakLineageStrategy.toLowerCase().endsWith("checkpoint")) {
			String checkpointDir = this.getCheckPointDir();
			this.spark.sparkContext().setCheckpointDir(checkpointDir);
		}
	}

	private String getCheckPointDir() {
		String checkpointDir = ConfigurationManager.getInstance().getProperty("checkpointDir");
		if (checkpointDir == null) {
			throw new PrometheuxRuntimeException("A checkpoint dir must be set in the pmtx properties");
		}
		checkpointDir = checkpointDir.replaceAll("\"", "");
		return checkpointDir;
	}

	private void deleteCheckPointDir(String master) {
		String checkPointDir = getCheckPointDir();
		if (master.equals("yarn")) {
			FileUtils.deleteDirInHDFS(checkPointDir);
		} else {
			FileUtils.deleteDirInLocal(checkPointDir);
		}
	}

	private void deleteSparkLocalDir(String master, String sparkLocalDir) {
		if (master.equals("yarn")) {
			FileUtils.deleteDirInHDFS(sparkLocalDir);
		} else {
			FileUtils.deleteDirInLocal(sparkLocalDir);
		}
	}

	public SparkSession getSparkSession() {
		if (this.spark == null) {
			throw new PrometheuxRuntimeException("Did you set the input source correctly? ");
		}
		return this.spark;
	}

	public void stopSparkSession() {
		if (this.spark != null) {
			this.getSqlContext().clearCache();
			if (!isLivySparkSession) {
				/*
				 * we do not stop if it is a LivySparkSession because in this case the Spark
				 * Session is provided by the Livy Job
				 */
				this.spark.stop();
			}
		}
	}

	public void closeSparkSession() {
		if (this.spark == null) {
			return;
		}

		try {
			RuntimeConfig conf = this.spark.conf();
			String master = conf.get("spark.master");
			String sparkLocalDir = conf.get("spark.local.dir");

			this.deleteCheckPointDir(master);

			if (!isLivySparkSession) {
				/*
				 * We do not close if it is a LivySparkSession because in this case the Spark
				 * Session is provided by the Livy Job
				 */
				this.deleteSparkLocalDir(master, sparkLocalDir);
				this.spark.close();
				this.spark = null;
				SparkConfigurationManager.getInstance().reset();
			}
		} catch (Exception e) {
			if (!isLivySparkSession) {
				this.spark.close();
				this.spark = null;
				SparkConfigurationManager.getInstance().reset();
			}
		}
	}

	public SQLContext getSqlContext() {
		this.createNewSparkSession();
		return this.getSparkSession().sqlContext();
	}

	public StorageLevel getPersistenceStorageLevel() {
		String storageLevelProp = ConfigurationManager.getInstance().getProperty("persistanceStorageLevel", "");
		StorageLevel storageLevel = StorageLevel.MEMORY_ONLY();
		if (storageLevelProp.equals("memoryOnly2")) {
			storageLevel = StorageLevel.MEMORY_ONLY_2();
		}
		if (storageLevelProp.equals("memoryOnlySer")) {
			storageLevel = StorageLevel.MEMORY_ONLY_SER();
		}
		if (storageLevelProp.equals("memoryOnlySer2")) {
			storageLevel = StorageLevel.MEMORY_ONLY_SER_2();
		}
		return storageLevel;
	}

	public void setLivySparkSession(SparkSession sparkSession) {
		if (this.spark == null) {
			this.spark = sparkSession;
			this.setCheckPointDir();
		}
	}

}

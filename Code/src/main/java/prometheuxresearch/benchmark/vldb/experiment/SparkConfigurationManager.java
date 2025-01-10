package prometheuxresearch.benchmark.vldb.experiment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.spark.SparkConf;
import org.slf4j.Logger;

/**
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 *
 */

public class SparkConfigurationManager {

	private static final String SPARK_CONF_FILE = "spark-defaults.conf";

	private SparkConf sparkConf;

	private static SparkConfigurationManager scm = null;
	private final Properties configProp = new Properties();
	private Logger log = null;

	public SparkConfigurationManager() {
	}

	public static SparkConfigurationManager getInstance() {
		if (scm == null) {
			scm = new SparkConfigurationManager();
			scm.loadProperties();
		}
		return scm;
	}

	public void loadProperties() {
		InputStream in = null;
		File external = new File(SPARK_CONF_FILE);
		Properties configPropExternal = new Properties();
		if (external.exists()) {
			try {
				in = new FileInputStream(external);
				configPropExternal.load(in);
			} catch (FileNotFoundException e) {
				throw new PrometheuxRuntimeException(e.getMessage(), e, log);
			} catch (IOException e) {
				throw new PrometheuxRuntimeException(e.getMessage(), e, log);
			}
		}
		in = this.getClass().getClassLoader().getResourceAsStream(SPARK_CONF_FILE);
		try {
			configProp.load(in);
		} catch (IOException e) {
			throw new PrometheuxRuntimeException(e.getMessage(), e, log);
		}

		if (configPropExternal.size() > 0)
			configProp.putAll(configPropExternal);

	}

	private void populateSparkConf() {
		this.sparkConf = new SparkConf();
		for (Map.Entry<Object, Object> prop : this.configProp.entrySet()) {
			String propertyName = prop.getKey().toString();
			String propertyValue = prop.getValue().toString();

			switch (propertyName) {
			case "appName": {
				this.sparkConf.setAppName(propertyValue);
				break;
			}
			case "spark.master": {
				this.sparkConf.setMaster(propertyValue);
				break;
			}
			default: {
				this.sparkConf.set(propertyName, propertyValue);
				break;
			}
			}
		}
	}

	public SparkConf getSparkConf() {
		if (this.sparkConf == null) {
			this.populateSparkConf();
		}
		return this.sparkConf;
	}

	public void reset() {
		scm = null;
	}

	public void overrideProperty(String key, String value) {
		this.configProp.setProperty(key, value);
	}

}

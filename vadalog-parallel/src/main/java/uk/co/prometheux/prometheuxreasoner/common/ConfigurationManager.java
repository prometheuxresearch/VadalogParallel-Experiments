package uk.co.prometheux.prometheuxreasoner.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;

/**
 * This Class handles configuration properties
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class ConfigurationManager {

	/**
	 * A symbol used to separate different components of the property name (the
	 * key).
	 */
	public static final String KEY_PATH_SEPARATOR = ".";
	private static final String ENVIRONMENT_PARAMETER_PREFIX = "PMTX";

	private static ConfigurationManager cm = null;
	private final Properties configProp = new Properties();
	private Logger log = null;

	private ConfigurationManager() {}

	public static ConfigurationManager getInstance() {
		if (cm == null) {
			cm = new ConfigurationManager();
			cm.loadProperties();
		}
		return cm;
	}

	/**
	 * Overwrite the properties
	 */
	public void loadProperties() {
		InputStream in = null;
		File external = new File("pmtx.properties");
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
		in = this.getClass().getClassLoader().getResourceAsStream("pmtx.properties");
		try {
			configProp.load(in);
			configProp.putAll(configPropExternal);
		} catch (IOException e) {
			throw new PrometheuxRuntimeException(e.getMessage(), e, log);
		}

		// Get environment variables which override all the properties provide from
		// files

		configPropExternal = new Properties();
		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {
			if (envName.startsWith(ENVIRONMENT_PARAMETER_PREFIX)) {
				if (envName.length() > ENVIRONMENT_PARAMETER_PREFIX.length()) {
					String envName2 = envName.substring(ENVIRONMENT_PARAMETER_PREFIX.length());
					configPropExternal.put(envName2, env.get(envName));
				}
			}
		}
		if (configPropExternal.size() > 0)
			configProp.putAll(configPropExternal);
	}

	/**
	 * Overwrite the properties
	 * 
	 * @param configProp
	 */
	public void loadProperties(Properties configProp) {
		this.configProp.clear();
		this.configProp.putAll(configProp);
	}
	
	public Properties getProperties() {
	    return this.configProp;
	}

	public String getProperty(String key, String def) {
		if (configProp.containsKey(key))
			return configProp.getProperty(key);
		return def;
	}

	public String getProperty(String key) {
		return getProperty(key, null);
	}
	
	public void setProperty(String key, String value) {
		this.configProp.setProperty(key, value);
	}

	public void overrideProperty(String key, String value) {
		this.configProp.setProperty(key, value);
	}

	private Integer getIntProperty(String key, Integer def) {
		if (configProp.containsKey(key))
			return Integer.parseInt(configProp.getProperty(key));
		return def;
	}

	public Integer getIntProperty(String key) {
		return getIntProperty(key, null);
	}

	public Boolean getBoolProperty(String key, Boolean def) {
		if (configProp.containsKey(key))
			return Boolean.parseBoolean(configProp.getProperty(key));
		return def;
	}

	public Boolean getBoolProperty(String key) {
		return getBoolProperty(key, null);
	}

	public void setRepositoryPath(String value) {
		this.configProp.setProperty("repository.path", value);
	}

	public void reset() {
		cm = null;
	}

}
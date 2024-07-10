package uk.co.prometheux.prometheuxreasoner.livy.job;

import java.lang.reflect.Type;
import java.util.Properties;

import org.apache.livy.JobHandle;
import org.apache.livy.LivyClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;
import uk.co.prometheux.prometheuxreasoner.collector.PredicatesCollector;
import uk.co.prometheux.prometheuxreasoner.common.ConfigurationManager;
import uk.co.prometheux.prometheuxreasoner.livy.LivyClientManager;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.types.JollyMarkedNull;
import uk.co.prometheux.prometheuxreasoner.model.types.MarkedNull;

/**
 * This Class executes the Vadalog Livy Job to be submitted by the Livy Client
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class LivyJobExecutor {

	public PredicatesCollector executeEvaluate(Model m) {
		LivyJob job = new LivyJob(m);
		Properties vadaProperties = ConfigurationManager.getInstance().getProperties();
//		Properties livyProperties = LivyConfigurationManager.getInstance().getConfigProp();
//		Properties livySparkProperties = LivySparkConfigurationManager.getInstance().getConfigProp();
		job.setPmtxProperties(vadaProperties);
//		job.setLivyProperties(livyProperties);
//		job.setLivySparkProperties(livySparkProperties);

		LivyClient livyClient = null;
		String predicatesCollectorSerialized = "";
		try {
			livyClient = LivyClientManager.getInstance().getLivyClient();
//			JobHandle<String> jobHandle = livyClient.submit(job);
//			predicatesCollectorSerialized = jobHandle.get();
		} catch (Exception e) {
			throw new PrometheuxRuntimeException(e.getMessage());
		}
		return getGsonBuilder().fromJson(predicatesCollectorSerialized, PredicatesCollector.class);
	}

	public PredicatesCollector executeEvaluate(Long vadalogJobId, Model m) {
		LivyJob job = new LivyJob(m);
		Properties pmtxProperties = ConfigurationManager.getInstance().getProperties();
//		Properties livyProperties = LivyConfigurationManager.getInstance().getConfigProp();
//		Properties livySparkProperties = LivySparkConfigurationManager.getInstance().getConfigProp();
		job.setPmtxProperties(pmtxProperties);
//		job.setLivyProperties(livyProperties);
//		job.setLivySparkProperties(livySparkProperties);

		LivyClient livyClient = null;
		String predicatesCollectorSerialized = "";
		try {
			livyClient = LivyClientManager.getInstance().getLivyClient();
//			JobHandle<String> jobHandle = livyClient.submit(job);
//			LivyJobHandler.getInstance().registerNewJob(vadalogJobId, jobHandle);
//			predicatesCollectorSerialized = jobHandle.get();
		} catch (Exception e) {
			throw new PrometheuxRuntimeException(e.getMessage());
		}

		return getGsonBuilder().fromJson(predicatesCollectorSerialized, PredicatesCollector.class);
	}

	private Gson getGsonBuilder() {
		Gson gson = new GsonBuilder().setLenient()
				.registerTypeAdapter(MarkedNull.class, new JsonDeserializer<MarkedNull>() {
					@Override
					public MarkedNull deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
							throws JsonParseException {
						JollyMarkedNull obj = new JollyMarkedNull();
						return obj;

					}
				}).create();
		return gson;
	}

}

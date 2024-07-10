package uk.co.prometheux.prometheuxreasoner.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;
import uk.co.prometheux.prometheuxreasoner.collector.PredicatesCollector;
import uk.co.prometheux.prometheuxreasoner.common.ConfigurationManager;
import uk.co.prometheux.prometheuxreasoner.common.SparkSessionManager;
import uk.co.prometheux.prometheuxreasoner.controller.VadalogParalellAppController;
import uk.co.prometheux.prometheuxreasoner.evaluator.ProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.library.AOutputProcessorHandler.MetadataValues;
import uk.co.prometheux.prometheuxreasoner.livy.LivyClientManager;
import uk.co.prometheux.prometheuxreasoner.livy.job.LivyJobExecutor;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.annotations.TypeEnum;
import uk.co.prometheux.prometheuxreasoner.model.types.VadaValue;
import uk.co.prometheux.prometheuxreasoner.rest.responses.EvaluateDatalogProgramResponse;
import uk.co.prometheux.prometheuxreasoner.rest.responses.PrometheuxResponseBody;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This is the REST controller of Prometheux Reasoner
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

@CrossOrigin(origins = "*")
@RestController
public class PrometheuxReasonerRestController {

	private final AtomicLong counter = new AtomicLong();

	/**
	 * It processes the output of the evaluate calls
	 * 
	 * @param vadaValueResultSet collected output facts
	 * @param newResultSet       the result set
	 * @param newResultTypes     the result types
	 */
	private void processOutput(Map<String, List<List<VadaValue<?>>>> vadaValueResultSet,
			Map<String, List<List<Object>>> newResultSet, Map<String, List<TypeEnum>> newResultTypes) {
//		VadaValueStringSerialiser stringSerialiser = new VadaValueStringSerialiser();
		for (Entry<String, List<List<VadaValue<?>>>> pred : vadaValueResultSet.entrySet()) {
			boolean currTuplesRead = true;
			List<List<Object>> newTuples = new ArrayList<>(pred.getValue().size());
			List<TypeEnum> types = null;
			for (List<VadaValue<?>> tuple : pred.getValue()) {
				if (currTuplesRead)
					types = new ArrayList<>(tuple.size());
				List<Object> newVadaVals = new ArrayList<>(tuple.size());
				for (VadaValue<?> vadaVal : tuple) {
					if (currTuplesRead)
						types.add(vadaVal.getExplicitType());
					if (vadaVal.isNull()) {
						newVadaVals.add(vadaVal.getMarkedNull().getNullName());
					} else {
						switch (vadaVal.getExplicitType()) {
						case INT:
						case DOUBLE:
							newVadaVals.add(vadaVal.getValue());
							break;
						case BOOLEAN:
//							newVadaVals.add(stringSerialiser.serialiseBoolean((Boolean) vadaVal.getValue()));
							break;
						case DATE:
//							newVadaVals.add(stringSerialiser
//									.serialiseGregorianCalendar((GregorianCalendar) vadaVal.getValue()));
							break;
						case STRING:
							newVadaVals.add(vadaVal.getValue());
							break;
						default:
							newVadaVals.add(String.valueOf(vadaVal));
							break;
						}
					}
				}
				if (currTuplesRead) {
					newResultTypes.put(pred.getKey(), types);
					currTuplesRead = false;
				}
				newTuples.add(newVadaVals);
			}
			newResultSet.put(pred.getKey(), newTuples);
		}
	}

	public EvaluateDatalogProgramResponse evaluateLivy(Model m) {
		/* invoke the evaluation of the program */
		Map<String, List<List<Object>>> resultSet = new HashMap<>();
		Map<String, List<TypeEnum>> types = new HashMap<>();
		Map<String, List<String>> columnNames = new HashMap<>();

		long vadalogParallelJobId = counter.incrementAndGet();

		LivyJobExecutor jobExecutor = new LivyJobExecutor();
		PredicatesCollector collector = jobExecutor.executeEvaluate(vadalogParallelJobId, m);
		processOutput(collector.getCollection(), resultSet, types);

		for (Entry<String, MetadataValues> mdv : collector.getMetadata().entrySet()) {
			columnNames.put(mdv.getKey(), mdv.getValue().getTermNames());
		}
		return new EvaluateDatalogProgramResponse(vadalogParallelJobId, resultSet, types, columnNames);
	}

	/**
	 * It evaluates a Vadalog program with parameters and returns the result
	 * 
	 * @param program the program to evaluate
	 * @return an object containing the evaluation outcome
	 */
	@RequestMapping("/evaluateFromRepo")
	public ResponseEntity<PrometheuxResponseBody> evaluateFromRepo(
			@RequestParam(value = "programName", defaultValue = ".") String programName) {
		/* invoke the evaluation of the program */
		ResponseEntity<PrometheuxResponseBody> evaluateDatalogProgramResponse = null;
		/* invoke the evaluation of the program */
		Map<String, List<List<Object>>> resultSet = new HashMap<>();
		Map<String, List<TypeEnum>> types = new HashMap<>();
		Map<String, List<String>> columnNames = new HashMap<>();

		Map<String, String> paramsMap = new HashMap<>();
		try {
			if (ConfigurationManager.getInstance().getProperty("restService").equals("livy")) {
				Model m = new Model();
				m = VadalogParalellAppController.getInstance().resolveIncludesAndParseWithParams(
						ConfigurationManager.getInstance().getProperty("repository.path") + File.separator
								+ programName,
						paramsMap);
				evaluateDatalogProgramResponse = new ResponseEntity<PrometheuxResponseBody>(this.evaluateLivy(m),
						HttpStatus.OK);
			} else {
				PredicatesCollector collector = new PredicatesCollector();
				ProgramEvaluator pe = VadalogParalellAppController.getInstance().evaluateProgramFromRepo(programName,
						paramsMap);
				VadalogParalellAppController.getInstance().processEvaluationOutput(pe);
				processOutput(collector.getCollection(), resultSet, types);

				for (Entry<String, MetadataValues> mdv : collector.getMetadata().entrySet()) {
					columnNames.put(mdv.getKey(), mdv.getValue().getTermNames());
				}

				evaluateDatalogProgramResponse = new ResponseEntity<PrometheuxResponseBody>(
						new EvaluateDatalogProgramResponse(counter.incrementAndGet(), resultSet, types, columnNames),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new PrometheuxRuntimeException(e.getMessage());
		} finally {
			ConfigurationManager.getInstance().reset();
			LivyClientManager.getInstance().reset();
		}
		return evaluateDatalogProgramResponse;
	}

	@RequestMapping("/stopEvaluation")
	public void stopEvaluation() {
		SparkSessionManager.getInstance().stopSparkSession();
		SparkSessionManager.getInstance().closeSparkSession();
	}

}

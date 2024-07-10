package uk.co.prometheux.prometheuxreasoner.controller;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.text.StringSubstitutor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;
import uk.co.prometheux.prometheuxreasoner.collector.PredicatesCollector;
import uk.co.prometheux.prometheuxreasoner.common.ConfigurationManager;
import uk.co.prometheux.prometheuxreasoner.evaluator.ProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.library.AOutputProcessorHandler;
import uk.co.prometheux.prometheuxreasoner.library.AOutputProcessorHandler.MetadataValues;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.ModelFactory;
import uk.co.prometheux.prometheuxreasoner.model.annotations.DatalogAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.annotations.MappingAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.types.VadaValue;
import uk.co.prometheux.prometheuxreasoner.optimizer.ProgramEvaluationOptimizer;
import uk.co.prometheux.prometheuxreasoner.parser.ParsingException;
import uk.co.prometheux.prometheuxreasoner.planner.Planner;

/**
 * This is the controller of the Prometheux Reasoner
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 * 
 */
public class VadalogParalellAppController {

	private static VadalogParalellAppController instance;

	private Logger log = LoggerFactory.getLogger(VadalogParalellAppController.class);

	/* a program model */
	private Model model = new Model();

	/* a stringbuffer where we memorize a program to be prepared */
	private StringBuffer preparedProgram;

	/* whether we have prepared an execution from a string to actualize */
	private boolean prepared = false;

	private VadalogParalellAppController() {
	}

	public synchronized static VadalogParalellAppController getInstance() {
		if (instance == null)
			instance = new VadalogParalellAppController();
		return instance;
	}

	/**
	 * It sets the repository path
	 * 
	 * @param path the repository path
	 */
	public void setRepositoryPath(String path) {
		ConfigurationManager.getInstance().setRepositoryPath(path);
	}

	/**
	 * It parses a model from a StringBuffer
	 * 
	 * @param program
	 * @param otherPrograms
	 * @return the parsed model
	 * @throws IOException
	 */
	public Model parseModel(StringBuffer program, List<String> otherPrograms) {
		ModelFactory modelFactory = new ModelFactory();

		/*
		 * we assume that there is no program potentially dependent on the main program
		 */
		if (otherPrograms.size() == 0) {
			return modelFactory.createModel(program.toString());
		} else {
			/*
			 * we assume that the program provided is a module which can depend on other
			 * programs
			 */
			return modelFactory.createModelForModule(program.toString(), otherPrograms);
		}
	}

	/**
	 * It reads a file given its name
	 * 
	 * @param filePath the path to the file containing the program
	 * @return program the StringBuffer with the loaded program
	 * @throws IOException
	 */
	private StringBuffer readFile(String filePath) throws IOException {
		Path file = Paths.get(filePath);

		InputStream in = Files.newInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		StringBuffer program = new StringBuffer();
		String line = reader.readLine();
		while (null != line) {
			program.append(line).append("\n");
			line = reader.readLine();
		}
		reader.close();
		in.close();
		return program;
	}

	/**
	 * For the given program, it resolves all includes and reads the programs from
	 * file, and returns the Vadalog model corresponding to the coherent (include
	 * resolved) program
	 * 
	 * @param filePath      absolute path to the program file to be read in
	 * @param programParams a map of parameters and their values to be applied to
	 *                      each included file
	 * @return the query model object resulting from parsing the main program and
	 *         all directly and indirectly included ones
	 * @throws IOException
	 */
	public Model resolveIncludesAndParseWithParams(String filePath, Map<String, String> programParams) {
		/* contains all files that need to be read in */
//		HashSet<String> allIncludedProgramPaths = resolveIncludes(filePath, programParams);
		StringBuffer program;
		try {
			program = setPlaceHolders(readFile(filePath), programParams);
		} catch (IOException e) {
			String format = String.format("Error when processing file '%s': %s", filePath, e.getMessage());
			throw new PrometheuxRuntimeException(format);
		}
		List<String> otherPrograms = new LinkedList<>();

		return parseModel(program, otherPrograms);
	}

	private StringBuffer setPlaceHolders(StringBuffer program, Map<String, String> programParams) {
		return programParams == null ? program
				: new StringBuffer(StringSubstitutor.replace(program.toString(), programParams));
	}

	/**
	 * It parses an input program from a file, resolves include annotations, and
	 * applies the given parameters to the programs as well as to all involved in
	 * the include cascade
	 * 
	 * @param filePath      absolute path to the file of the program to be prepared
	 * @param programParams parameters and their value applied to the program
	 */
	private void prepareProgramFromFileWithParams(String filePath, Map<String, String> programParams) {
		log.info("BEGIN parsing the program");
		this.model = resolveIncludesAndParseWithParams(filePath, programParams);
		this.prepared = true;
		log.info("END parsing the program");
	}

	/**
	 * It processes the output of the evaluation
	 * 
	 * @param persistence    persistence mode
	 * @param outputHandling output handling mode
	 * @param oProcessors    list of output processors
	 * @param outputHandler  handler for unsaved output
	 */
	public void processEvaluationOutput(ProgramEvaluator pe) {
//		try {
			pe.startUp();
			pe.evaluate();
//		} catch (Exception e) {
//			throw new PrometheuxRuntimeException(e.getMessage());
//		} finally {
			pe.cleanUp();
//		}
		log.info("END reasoning");
	}

	/**
	 * It evaluates a program
	 * 
	 * @return the result of the evaluation as list of output processors
	 */
	private ProgramEvaluator evaluateProgram() {
		Model model = this.model;
		/* we apply the rewriting steps to the model and set the termination strategy */
		ProgramEvaluationOptimizer peo = new ProgramEvaluationOptimizer(model);
		model = peo.optimizeProgramEvaluation();

		/* we calculate and return the plan */
		Planner planner = new Planner();
		ProgramEvaluator pe = planner.makePlan(model);
		return pe;
	}

	/**
	 * It evaluates the model of a program
	 * 
	 * @param model the model of the program
	 * @return the result of the evaluation as list of output processors
	 */
	public ProgramEvaluator evaluateModel(Model model) {
		log.debug("BEGIN evaluating model");
		this.model = (model);
		ProgramEvaluator pe = (this.evaluateProgram());
		log.debug("END evaluating model");
		return pe;
	}

	/**
	 * It evaluates a program in a repository
	 * 
	 * @param programName the name of the program in the repo
	 * @param params      a map of parameters
	 * @return the result of the evaluation as list of output processors
	 */
	public ProgramEvaluator evaluateProgramFromRepo(String programName, Map<String, String> params) {
		log.info("BEGIN evaluating program from repo");
		if (log.isDebugEnabled())
			log.debug("Accessing the repository: " + ConfigurationManager.getInstance().getProperty("repository.path"));
		this.prepareProgramFromFileWithParams(
				ConfigurationManager.getInstance().getProperty("repository.path") + File.separator + programName,
				params);
		ProgramEvaluator pe = this.evaluateModel(this.model);
		log.info("END evaluating program from repo");
		return pe;
	}

	public void reset() {
		instance = null;
	}

}

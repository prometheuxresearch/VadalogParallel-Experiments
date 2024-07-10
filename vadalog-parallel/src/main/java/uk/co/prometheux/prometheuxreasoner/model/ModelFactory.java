package uk.co.prometheux.prometheuxreasoner.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;
import uk.co.prometheux.prometheuxreasoner.model.annotations.DatalogAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.annotations.IncludeAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.annotations.ModuleAnnotation;

/**
 * Generator of Vadalog Language Models.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class ModelFactory {

	private Logger log = LoggerFactory.getLogger(ModelFactory.class);
	
	/**
	 * It parses a program from a String. If the program depends on other programs
	 * it is handled as a module
	 * 
	 * @param program       the program to parse
	 * @param otherPrograms other programs the current program depends on as module
	 * @return the model of the parsed program
	 * @throws IOException
	 */
	public Model parseModel(String program, List<String> otherPrograms) throws IOException {
		/*
		 * we assume that there is no program potentially dependent on the main program
		 */
		if (otherPrograms.size() == 0) {
			return this.createModel(program);
		} else { /*
					 * we assume that the program provided is a module which can depend on other
					 * programs
					 */
			return this.createModelForModule(program, otherPrograms);
		}
	}

	/**
	 * Create a single model from Vadalog programs
	 * 
	 * @param programs Vadalog programs
	 * @return Model of the Vadalog programs
	 * @throws IOException
	 */
	public Model createModel(String... programs) {
		Model finalModel = new Model();
		for (int i = 0; i < programs.length; i++) {
			finalModel.readProgram(programs[i]);
		}
		return finalModel;
	}

	/**
	 * Create a single model out of dependent Vadalog programs (aka modules).
	 * 
	 * @param mainModule   the main module
	 * @param otherModules other modules which represent a superset of all modules
	 *                     relevant to the main module via the transitive closure
	 * @return model representing a complete Vadalog program
	 * @throws IOException
	 */
	public Model createModelForModule(String mainModule, Collection<String> otherModules) {

		List<String> modulesList = new ArrayList<>(otherModules.size() + 1);
		modulesList.add(mainModule);
		modulesList.addAll(otherModules);

		String mainModuleName = UUID.randomUUID().toString();

		Map<String, String> nameModuleMap = new HashMap<>(modulesList.size());
		ListMultimap<String, String> dependencies = ArrayListMultimap.create();
		Map<String, String> moduleNameNormalisation = new HashMap<>();

		for (int i = 0; i < modulesList.size(); i++) {
			Model model = new Model();
			model.readProgram(modulesList.get(i));
			Set<String> moduleNames = new HashSet<>(5);
			Set<String> inclusions = new HashSet<>(20);
			for (DatalogAnnotation ann : model.getAnnotations()) {
				if (ann instanceof ModuleAnnotation) {
					moduleNames.add(((ModuleAnnotation) ann).getModuleName());
				} else if (ann instanceof IncludeAnnotation) {
					inclusions.add(((IncludeAnnotation) ann).getModuleName());
				}
			}

			if (i == 0) { // main module
				// check if there are inclusions in the main module/program. If there are no
				// inclusions then return the current model
				if (inclusions.size() == 0)
					return model;

				moduleNames.add(mainModuleName);
			}

			if (moduleNames.size() > 0) {
				if (moduleNames.size() > 1)
					log.warn("Module has several names");
				String normalModuleName = (i == 0) ? mainModuleName : moduleNames.iterator().next();
				moduleNames.forEach(moduleName -> {
					moduleNameNormalisation.put(moduleName, normalModuleName);
				});
				nameModuleMap.put(normalModuleName, modulesList.get(i));
				inclusions.forEach(incl -> {
					dependencies.put(normalModuleName, incl);
				});
			} else {
				log.debug("Module has no name and is to ommited from the analysis");
			}
		}

		Set<String> incNameSet = new HashSet<String>(modulesList.size());
		final List<String> queue = new LinkedList<String>();
		queue.add(mainModuleName);
		while (queue.size() > 0) {
			String currModuleName = moduleNameNormalisation.get(queue.get(0));
			if (currModuleName == null) {
				throw new PrometheuxRuntimeException("Module {0} does not exist", log, queue.get(0));
			}

			queue.remove(0);
			if (!incNameSet.contains(currModuleName)) {
				incNameSet.add(currModuleName);
				dependencies.get(currModuleName).forEach(inc -> {
					if (!incNameSet.contains(inc)) {
						queue.add(inc);
					}
				});
			}
		}

		return createModel(incNameSet.stream().map(moduleName -> nameModuleMap.get(moduleName))
				.collect(Collectors.toList()).toArray(new String[incNameSet.size()]));
	}

}

package uk.co.prometheux.prometheuxreasoner.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.pwlWarded.ModelPWLWardedDecorator;
import uk.co.prometheux.prometheuxreasoner.warded.distributed.ruleTraversal.Rule2RuleSchemaMapping;

/**
 * Controller with logic capabilities
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class PrometheuxLogicController {
	
	private static PrometheuxLogicController instance;

	public synchronized static PrometheuxLogicController getInstance() {
		if (instance == null)
			instance = new PrometheuxLogicController();
		return instance;
	}
	
	/**
	 * This method calls the schema extractors and returns the extracted schema from
	 * a Model
	 * 
	 * @param model
	 * @return
	 */
	public Map<String, List<Set<Entry<String, Integer>>>> extractSchemaFromProgram(Model model) {
		Rule2RuleSchemaMapping schemaExtractor = new Rule2RuleSchemaMapping(model);
		Map<String, List<Set<Entry<String, Integer>>>> idbSchemaFromEdbSchema = schemaExtractor
				.idbSchemaFromEdbSchema();
		return idbSchemaFromEdbSchema;
	}
	
	/**
	 * It returns the string of input predicates as a comma separated list
	 */
	public String inputPredicates(Model model) {
		ModelPWLWardedDecorator mpwlwd = new ModelPWLWardedDecorator(model);
		String inputPredicates = mpwlwd.getExtensionalString();
		return inputPredicates;
	}
	
	/**
	 * It returns the string of intensional predicates as a comma separated list
	 */
	public String intensionalPredicates(Model model) {
		ModelPWLWardedDecorator mpwlwd = new ModelPWLWardedDecorator(model);
		String intensionalPredicates = mpwlwd.getIntensionalString();
		return intensionalPredicates;
	}

}

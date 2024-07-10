package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.util.Collections;

/**
 * Optimization annotation for various strategies
 * Available strategies are default, snaJoin, sna
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 * 
 */
public class OptimizationAnnotation extends DatalogAnnotation {

	private final String optimizationStrategy;

	public OptimizationAnnotation(String optimizationStrategy) {
		super("optimizationStrategy", Collections.singletonList(optimizationStrategy));
		this.optimizationStrategy = optimizationStrategy;
	}
	
	public String getOptimizationStrategy() {
		return this.optimizationStrategy;
	}

}
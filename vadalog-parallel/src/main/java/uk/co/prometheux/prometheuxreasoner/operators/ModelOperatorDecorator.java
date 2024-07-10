package uk.co.prometheux.prometheuxreasoner.operators;

import uk.co.prometheux.prometheuxreasoner.model.Model;

/**
 * It decorates the model with operations that apply to an entire program
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class ModelOperatorDecorator {

	private Model m;

	public ModelOperatorDecorator(Model m) {
		this.m = m;
	}

	/**
	 * Returns an object to calculate the extraction of this model
	 * 
	 * @return an object to calculate an extraction of this model
	 */
	public Extraction extract() {
		return new Extraction(this.m);
	}

}

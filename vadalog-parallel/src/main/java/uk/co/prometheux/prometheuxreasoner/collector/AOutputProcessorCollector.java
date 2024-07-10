 package uk.co.prometheux.prometheuxreasoner.collector;

import uk.co.prometheux.prometheuxreasoner.library.AOutputProcessorHandler;
import uk.co.prometheux.prometheuxreasoner.model.types.VadaValue;

/**
 * 
 * This is an interface for collecting output processors.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

abstract class AOutputProcessorCollector<T> extends AOutputProcessorHandler {

	public AOutputProcessorCollector() {
		super();
		initCollection();
	}

	protected T collection = null;

	public T getCollection() {
		return collection;
	}

	abstract protected void initCollection();

	public void process(int factId, String atomName, Iterable<VadaValue<?>> terms) {
		collect(factId, atomName, terms);
	}

	abstract protected void collect(int factId, String atomName, Iterable<VadaValue<?>> terms);

}

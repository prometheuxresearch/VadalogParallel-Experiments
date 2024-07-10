package uk.co.prometheux.prometheuxreasoner.errorMessage.aggregations.evaluator;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;

public class AggregationException extends PrometheuxRuntimeException {
	
	private static final long serialVersionUID = 1L;

	public AggregationException(String message) {
		super(message);
	}
}

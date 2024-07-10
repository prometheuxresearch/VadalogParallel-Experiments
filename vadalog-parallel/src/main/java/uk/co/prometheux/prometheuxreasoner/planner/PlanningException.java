package uk.co.prometheux.prometheuxreasoner.planner;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;
import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;

/**
 * This Class raises Planning issues
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public class PlanningException extends PrometheuxRuntimeException {
	
	private static final long serialVersionUID = 1L;
	private ErrorMessage errorMessage;

	public PlanningException(ErrorMessage errorMessage) {
		super("Planning exception: "+errorMessage.getMessage());
		this.errorMessage = errorMessage;
	}
	
	public PlanningException(String message) {
		super("Planning exception: "+message);
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

}

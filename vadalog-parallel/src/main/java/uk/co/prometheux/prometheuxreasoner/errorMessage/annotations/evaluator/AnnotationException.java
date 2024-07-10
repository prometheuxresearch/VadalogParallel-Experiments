package uk.co.prometheux.prometheuxreasoner.errorMessage.annotations.evaluator;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;
import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;

/**
 * This Class handles annotation exception
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */

public class AnnotationException extends PrometheuxRuntimeException {
	
	private static final long serialVersionUID = 1L;
	private ErrorMessage errorMessage;

	public AnnotationException(String message) {
		super("Annotation exception: "+message);
	}
	
	public AnnotationException(ErrorMessage errorMessage) {
		super("Annotation exception: "+errorMessage.getMessage());
		this.errorMessage = errorMessage;
	}
	
	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}
}

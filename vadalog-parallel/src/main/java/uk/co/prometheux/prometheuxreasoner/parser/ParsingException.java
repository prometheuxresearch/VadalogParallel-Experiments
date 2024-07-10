package uk.co.prometheux.prometheuxreasoner.parser;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;
import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;

/**
 * It represents an exception in parsing a Datalog program.
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class ParsingException extends PrometheuxRuntimeException {
	private static final long serialVersionUID = 1L;
	private ErrorMessage errorMessage;
	
	public ParsingException(String message) {
		super("Parsing exception: " + message);
	}
	
	public ParsingException(ErrorMessage errorMessage) {
		super("Parsing exception: " + errorMessage.getMessage());
		this.errorMessage = errorMessage;
	}
	
	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

}
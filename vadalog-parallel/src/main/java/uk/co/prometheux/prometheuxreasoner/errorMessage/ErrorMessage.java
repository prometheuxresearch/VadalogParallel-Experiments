package uk.co.prometheux.prometheuxreasoner.errorMessage;

/**
 * This is an Interface for error messages
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public interface ErrorMessage {

	public String getMessage();

	public void setErrorMessage(String errorMessage);

	public String getStructuredInfo();

	public String getErrorCode();
	
	public void createErrorMessage(String inputMessage);

	public String getResponseMessage();
	
	public default void setColumnNumber(int colNumber) {};

}

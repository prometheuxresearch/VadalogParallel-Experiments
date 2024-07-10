package uk.co.prometheux.prometheuxreasoner.rest.responses;

/**
 * An error response object returned to the response.
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class ResponseBody {
    private String errorMessage;
    private String errorCode;
    
	public ResponseBody(String errorMessage, String errorCode) {
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}
	
	public ResponseBody() {}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
    
}

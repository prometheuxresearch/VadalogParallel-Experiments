package uk.co.prometheux.prometheuxreasoner.rest.responses;

/**
 * A generic ApiResponse
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class PrometheuxResponse<T> {
    private T data;
    private String message;
    
	public PrometheuxResponse(T data, String message) {
		this.data = data;
		this.message = message;
	}
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
    
}

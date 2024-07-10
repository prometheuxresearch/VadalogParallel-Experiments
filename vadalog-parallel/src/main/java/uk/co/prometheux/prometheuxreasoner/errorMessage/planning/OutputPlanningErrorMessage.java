package uk.co.prometheux.prometheuxreasoner.errorMessage.planning;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.handler.SyntaxErrorMessageIdentifier;

/**
 * This class represents an error messages for output planning
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class OutputPlanningErrorMessage implements PlanningErrorMessage {
	
	private String errorMessage = "";
	private String responseMessage = "";
	private String errorCode = "VL0023";
	private String structuredInfos = "";
	private List<String> vadalogProgramAsRuleList;
	private int lineNumber = 0;
	private int columnNumber = 0;
	
	public OutputPlanningErrorMessage() {
		vadalogProgramAsRuleList = SyntaxErrorMessageIdentifier.getVadalogProgramAsList();
		this.lineNumber = vadalogProgramAsRuleList.size();
		this.columnNumber  = 1;
		this.structurateMessage();
	}

	private void structurateMessage() {
		this.structuredInfos += "<program>";
		this.structuredInfos += ": Line "+(lineNumber + 1) ;
		this.structuredInfos += ": Column "+this.columnNumber;	
		this.structuredInfos += " - Error code: "+this.errorCode;	
	}
	
	@Override
	public void createErrorMessage(String inputMessage) {
		this.errorMessage += inputMessage;
		this.produceResponseMessage();
	}
	
	private void produceResponseMessage() {
		this.responseMessage = this.structuredInfos +": "+ this.errorMessage;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		
	}
	
	@Override
	public String getResponseMessage() {
		return this.responseMessage;
	}
	
	@Override
	public String getMessage() {
		return this.errorMessage;
	}

	@Override
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String getStructuredInfo() {
		return this.structuredInfos;
	}

	@Override
	public String getErrorCode() {
		return this.errorCode;
	}

}

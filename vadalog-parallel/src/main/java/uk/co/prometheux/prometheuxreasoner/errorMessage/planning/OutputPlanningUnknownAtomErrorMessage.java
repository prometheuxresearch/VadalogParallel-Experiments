package uk.co.prometheux.prometheuxreasoner.errorMessage.planning;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.handler.SyntaxErrorMessageIdentifier;
import uk.co.prometheux.prometheuxreasoner.model.annotations.OutputAnnotation;

/**
 * This class represents an error messages for output planning for
 * handling unknown output atoms
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class OutputPlanningUnknownAtomErrorMessage implements PlanningErrorMessage {
	
	private String errorMessage = "";
	private String responseMessage = "";
	private String errorCode = "VL0023";
	private String structuredInfos = "";
	private List<String> vadalogProgramAsRuleList;
	private int lineNumber = 0;
	private int columnNumber = 0;
	private List<String> annotations = new ArrayList<>();
	private String currentPredicate;
	private List<String> notContainedPredicates;
	
	public OutputPlanningUnknownAtomErrorMessage(List<String> notContainedPredicates) {
		this.notContainedPredicates = notContainedPredicates;
		for(String predicateName : notContainedPredicates) {
			annotations.add(new OutputAnnotation(predicateName).toString()+".");
		}
		vadalogProgramAsRuleList = SyntaxErrorMessageIdentifier.getVadalogProgramAsList();
		this.columnNumber = 1;
	}

	@Override
	public void createErrorMessage(String inputMessage) {
	    this.errorMessage += "unkown output atom";
	    int i = 0;
		for(String annotation : annotations) {
			this.currentPredicate = this.notContainedPredicates.get(i);
			this.lineNumber = vadalogProgramAsRuleList.indexOf(annotation);
			this.produceResponseMessage();
			i++;
		}
	}
	
	private void produceResponseMessage() {
		String structuredInfos = "<program>";
		structuredInfos += ": Line "+(lineNumber + 1) ;
		structuredInfos += ": Column "+this.columnNumber;	
		structuredInfos += " - Error code: "+this.errorCode+"";
		this.responseMessage += structuredInfos +": "+ this.errorMessage + " '"+this.currentPredicate+ "' ";
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

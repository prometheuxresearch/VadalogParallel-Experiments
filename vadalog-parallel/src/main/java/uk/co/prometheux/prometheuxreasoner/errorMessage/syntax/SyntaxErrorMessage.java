package uk.co.prometheux.prometheuxreasoner.errorMessage.syntax;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;

public class SyntaxErrorMessage implements ErrorMessage {
	
	private int column;
	private int line;
	private String errorMessage;
	private final String errorMessageType = "Syntax Error";
	
	public SyntaxErrorMessage() {}
	
	public SyntaxErrorMessage(int column, int line) {
		this.column = column;
		this.line = line;
	}

	@Override
	public void createErrorMessage(String offensiveRule) {
		this.errorMessage = "";
		this.errorMessage += "The rule "+offensiveRule+" is not written correctly."+"\n you probably missed a dot or a comma before "+offensiveRule.charAt(column)+ " at line "+this.line;
		this.errorMessage += "an offensiveRule";
		String beforeOffensiveRule = offensiveRule.substring(0, this.column);
		String newRule = beforeOffensiveRule+", "+offensiveRule.substring(this.column, offensiveRule.length());
		this.errorMessage +="\nTry to replace "+offensiveRule+" with " +newRule;
	}
	
	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
		
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessageType() {
		return errorMessageType;
	}

	@Override
	public String getStructuredInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getResponseMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setColumnNumber(int colNumber) {
		// TODO Auto-generated method stub
		
	}
	
	

}

package uk.co.prometheux.prometheuxreasoner.errorMessage.syntax;

import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;

public class NoViableAlternativeSyntaxErrorMessage implements ErrorMessage {
	
	private String errorMessage = "";
	private String responseMessage = "";
	private String errorCode = "VL0023";
	private String structuredInfos = "";
	private int lineNumber = 0;
	private int columnNumber = 0;

	public NoViableAlternativeSyntaxErrorMessage(int lineNumber, int columnNumber) {
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
		this.structurateMessage();
	}

	private void structurateMessage() {
		this.structuredInfos += "<program>";
		this.structuredInfos += ": Line " + (this.lineNumber);
		this.structuredInfos += ": Column " + this.columnNumber;
		this.structuredInfos += " - Error code: " + this.errorCode;		
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

	@Override
	public void createErrorMessage(String inputMessage) {
		this.errorMessage = inputMessage;
		this.produceResponseMessage();
	}

	private void produceResponseMessage() {
		this.responseMessage = this.structuredInfos+ ": "+this.errorMessage;
	}

	@Override
	public String getResponseMessage() {
		return this.responseMessage;
	}

	@Override
	public void setColumnNumber(int colNumber) {
		
	}

}

package uk.co.prometheux.prometheuxreasoner.errorMessage.annotations;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.handler.SyntaxErrorMessageIdentifier;
import uk.co.prometheux.prometheuxreasoner.model.Atom;

public class ExecutionModeAnnotationErrorMessage implements AnnotationErrorMessage {

	private String errorMessage = "";
	private String responseMessage = "";
	private String errorCode = "VL0023";
	private String structuredInfos = "";
	private List<String> vadalogProgramAsRuleList;
	private int lineNumber = 0;
	private int columnNumber = 0;

	public ExecutionModeAnnotationErrorMessage() {
	}

	public ExecutionModeAnnotationErrorMessage(Atom executionModeAnnotation) {
		vadalogProgramAsRuleList = SyntaxErrorMessageIdentifier.getVadalogProgramAsList();
		this.lineNumber = this.getRuleLineFromAtom(executionModeAnnotation);
		String wrongString = executionModeAnnotation.getName();
		this.columnNumber = executionModeAnnotation.toClauseString().indexOf(wrongString);
		this.structurateMessage();
	}

	@Override
	public void createErrorMessage(String offensiveRule) {
		this.errorMessage += offensiveRule;
		this.produceResponseMessage();
	}

	private void produceResponseMessage() {
		this.responseMessage = this.structuredInfos + ": " + this.errorMessage;
	}

	private void structurateMessage() {
		this.structuredInfos += "<program>";
		this.structuredInfos += ": Line " + (this.lineNumber + 1);
		this.structuredInfos += ": Column " + this.columnNumber;
		this.structuredInfos += " - Error code: " + this.errorCode;
	}

	private int getRuleLineFromAtom(Atom bind) {
		return this.vadalogProgramAsRuleList.indexOf("@" + bind.toClauseString());
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

	public String getMessage() {
		return this.errorMessage;
	}

	public String getErrorMessageType() {
		return errorCode;
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
	public String getResponseMessage() {
		return this.responseMessage;
	}

}

package uk.co.prometheux.prometheuxreasoner.errorMessage.runtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EvaluationRuntimeErrorMessage implements RuntimeErrorMessage {

	private String errorMessage = "";
	private String responseMessage = "";
	private String errorCode = "VL0023";
	private String structuredInfos = "";

	public EvaluationRuntimeErrorMessage(String message) {
		this.errorMessage = message;
	}

	@Override
	public void createErrorMessage(String offensiveRule) {
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

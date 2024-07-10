package uk.co.prometheux.prometheuxreasoner.errorMessage.aggregations;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.handler.SyntaxErrorMessageIdentifier;
import uk.co.prometheux.prometheuxreasoner.model.Rule;

public class ModelMonotonicAggregationErrorMessage implements AggregationErrorMessage {
	
	private String errorMessage = "";
	private String responseMessage = "";
	private String errorCode = "VL0023";
	private String structuredInfo = "";
	private List<String> vadalogProgramAsRuleList;
	private int lineNumber = 0;
	private int columnNumber = 0;
	
	public ModelMonotonicAggregationErrorMessage(Rule rule) {
		vadalogProgramAsRuleList = SyntaxErrorMessageIdentifier.getVadalogProgramAsList();
		this.lineNumber = this.getRuleLineFromRule(rule);
		this.columnNumber = rule.toString().length();
		this.structurateMessage();
	}

	private int getRuleLineFromRule(Rule rule) {
		int i = 1;
		for(String vadalogRuleString : vadalogProgramAsRuleList) {
			if(vadalogRuleString.startsWith(rule.toString())) {
				break;
			}
			i++;

		}
		return i;
	}

	private void structurateMessage() {
		this.structuredInfo += "<program>";
		this.structuredInfo += ":Line "+(lineNumber + 1) ;
		this.structuredInfo += " :Column "+this.columnNumber;	
		this.structuredInfo += " - Error code: "+this.errorCode;		
	}

	@Override
	public void createErrorMessage(String offensiveRule) {
		this.errorMessage += offensiveRule;
		this.produceResponseMessage();
	}
	
	
	private void produceResponseMessage() {
		this.responseMessage = this.structuredInfo +": "+ this.errorMessage;
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
		return this.structuredInfo;
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

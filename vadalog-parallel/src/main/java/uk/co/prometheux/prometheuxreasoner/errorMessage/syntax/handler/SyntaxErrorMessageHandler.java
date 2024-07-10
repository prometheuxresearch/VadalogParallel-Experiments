package uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.handler;

import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;

public interface SyntaxErrorMessageHandler {
	
	public String handle();
	
	public ErrorMessage getErrorMessageObj();
	
}

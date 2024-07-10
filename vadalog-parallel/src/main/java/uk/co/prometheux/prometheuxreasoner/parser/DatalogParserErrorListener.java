package uk.co.prometheux.prometheuxreasoner.parser;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;
import uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.handler.SyntaxErrorMessageHandler;
import uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.handler.SyntaxErrorMessageIdentifier;

/**
 * It is a listener to get the RecognitionExceptions from ANTLR and report
 * them as ParsingExceptions.
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class DatalogParserErrorListener extends BaseErrorListener implements ANTLRErrorListener {

	@Override
	public void syntaxError(Recognizer<?, ?> arg0, Object arg1, int arg2, int arg3, String arg4,
			RecognitionException arg5) {
		SyntaxErrorMessageHandler errorMessageHandler = SyntaxErrorMessageIdentifier.getParsingErrorMessageHandler(arg0, arg1, arg3, arg3, arg4, arg5);
		errorMessageHandler.handle();
		ErrorMessage errorMessage = errorMessageHandler.getErrorMessageObj();
		throw new ParsingException(errorMessage);		
	}
}
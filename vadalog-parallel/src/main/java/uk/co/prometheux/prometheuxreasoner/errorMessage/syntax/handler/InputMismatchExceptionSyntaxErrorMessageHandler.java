package uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.handler;


import org.antlr.v4.runtime.Recognizer;

import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;
import uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.InputMismatchSyntaxErrorMessage;


public class InputMismatchExceptionSyntaxErrorMessageHandler implements SyntaxErrorMessageHandler {
	
	private int lineNumber;
	private int columnNumber;
	private String errorMessage;
	private String vadalogProgram;
	String responseMessage = "";
	private ErrorMessage errorMessageObj;
	
	
	public InputMismatchExceptionSyntaxErrorMessageHandler(Recognizer<?, ?> arg0, Object arg1, int arg2, int arg3, String arg4, String vadalogProgram) {
		String[] tokenStringArr = arg1.toString().split(",");
		String lastToken = tokenStringArr[tokenStringArr.length -1];
		String tokenized = lastToken.substring(0, lastToken.length() -1 );
		String[] lineAndColumn = tokenized.split(":");
		this.lineNumber = Integer.valueOf(lineAndColumn[0]);
		this.columnNumber = Integer.valueOf(lineAndColumn[1]);
		this.errorMessage = arg4;
		this.vadalogProgram = vadalogProgram;
	}

	@Override
	public String handle() {
		String[] vadalogRules = vadalogProgram.split("\n");
		ErrorMessage inputMismatch = new InputMismatchSyntaxErrorMessage(lineNumber, columnNumber);
		inputMismatch.createErrorMessage(errorMessage);
		int newlineNumber = this.lineNumber;
		if(columnNumber > 0) {
			newlineNumber = newlineNumber - 1;
		}
		String offensiveRule = vadalogRules[newlineNumber];
		String inputMessage = this.inputMessageFromSpecificCase(offensiveRule);
		inputMessage += "\n Please fix the symbol '"+offensiveRule.charAt(columnNumber)+"' at rule "+offensiveRule;
		// if during the analysis the column number changes
		inputMismatch.setColumnNumber(this.columnNumber);
		inputMismatch.createErrorMessage(inputMessage);
		this.errorMessageObj = inputMismatch;
		return inputMismatch.getResponseMessage();
	}

	private String inputMessageFromSpecificCase(String offensiveRule) {
		String specificCase = this.identifySpecificCase(offensiveRule);
		String inputMessage;
		char offensiveChar = offensiveRule.charAt(columnNumber);
		switch (specificCase) {
		case "unexpected boolean symbol": {
			inputMessage = this.createUnexpectedSymbolForBooleanMessage(offensiveChar);
			break;
		}
		case "lower case variable":{
			inputMessage = this.createLowerCaseVariableMessage(offensiveChar);
			break;
		}
		case "upper case predicate":{
			inputMessage = this.createUpperCasePredicateMessage(offensiveChar);
			break;
		}
		default: {
			inputMessage = "";
		}
		}
		return inputMessage;
	}

	private String createUpperCasePredicateMessage(char offensiveChar) {
		String message = "unexpected symbol "+offensiveChar+".";
		message += " Predicates must start with a lower case letter.";
		return message;
	}

	private String createLowerCaseVariableMessage(char offensiveChar) {
		String message = "unexpected symbol "+offensiveChar+".";
		message += " Variables must start with an upper case letter.";
		return message;
	}

	private String createUnexpectedSymbolForBooleanMessage(char offensiveChar) {
		String message = "unexpected boolean symbol "+offensiveChar+".";
		message += " Boolean values are #T and #F.";
		return message;
	}

	private String identifySpecificCase(String offensiveRule) {
		char offensiveChar = offensiveRule.charAt(columnNumber);
		String specificCase = "";
		String previousToOffensiveChar = "";
		if(columnNumber > 0) {
			previousToOffensiveChar = String.valueOf(offensiveRule.charAt(columnNumber - 1));
		if(previousToOffensiveChar.equals("#")) {
			specificCase = "unexpected boolean symbol";
			return specificCase;
		}
		}
		if(columnNumber > 1) {
			previousToOffensiveChar = String.valueOf(offensiveRule.charAt(columnNumber - 2));
		if(previousToOffensiveChar.equals("#")) {
			columnNumber = columnNumber - 1;
			specificCase = "unexpected boolean symbol";
			return specificCase;
		}
		}
		if(Character.isLowerCase(offensiveChar)) {
			specificCase = "lower case variable";
			return specificCase;
		}
		String wholeWord = getWordFromSymbol(offensiveRule);
		if(wholeWord.isEmpty()) {
			return offensiveRule;
		}
		char firstChar = wholeWord.charAt(0);
		if(Character.isUpperCase(firstChar)) {
			specificCase = "upper case predicate";
			this.columnNumber = offensiveRule.indexOf(wholeWord);
			return specificCase;
		}
		return specificCase;
	}

	private String getWordFromSymbol(String offensiveRule) {
		String wholeWord = "";
		if(offensiveRule.length() == 0 || columnNumber < 0) {
			return " ";
		}
		char currentChar = offensiveRule.charAt(columnNumber);
		int currCharPos = columnNumber;
		while(currCharPos >= 0 && currentChar != ' ') {
			currentChar = offensiveRule.charAt(currCharPos);
			wholeWord = currentChar + wholeWord;
			currCharPos --;
		}
		return wholeWord;
	}

	public ErrorMessage getErrorMessageObj() {
		return errorMessageObj;
	}

}

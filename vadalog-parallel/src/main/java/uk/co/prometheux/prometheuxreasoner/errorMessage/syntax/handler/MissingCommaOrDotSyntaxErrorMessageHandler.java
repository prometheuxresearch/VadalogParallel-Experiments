package uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.handler;

import org.antlr.v4.runtime.Recognizer;

import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;
import uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.MissingCommaOrDotSyntaxErrorMessage;

public class MissingCommaOrDotSyntaxErrorMessageHandler implements SyntaxErrorMessageHandler {

	private int lineNumber;
	private int columnNumber;
	private String vadalogProgram;
	String responseMessage = "";
	boolean isErrorAtPreviousLine = false;
	private Integer originalLineNumber;
	private String[] vadalogRules;
	private String suggestionError = "";
	private ErrorMessage errorMessageObj;

	public MissingCommaOrDotSyntaxErrorMessageHandler(Recognizer<?, ?> arg0, Object arg1, int arg2, int arg3,
			String arg4, String vadalogProgram) {
		String[] tokenStringArr = arg1.toString().split(",");
		String lastToken = tokenStringArr[tokenStringArr.length - 1];
		String tokenized = lastToken.substring(0, lastToken.length() - 1);
		String[] lineAndColumn = tokenized.split(":");
		this.originalLineNumber = Integer.valueOf(lineAndColumn[0]);
		this.columnNumber = Integer.valueOf(lineAndColumn[1]);
		this.vadalogProgram = vadalogProgram;
		this.vadalogRules = vadalogProgram.split("\n");
		this.suggestionError = arg4;
		this.identifyLineNumber();
	}

	private void identifyLineNumber() {
		this.vadalogRules = vadalogProgram.split("\n");
		this.lineNumber = originalLineNumber - 1;
		if (columnNumber == 0) {
			int wspaceNum = 1;
			for (int wSpace = this.originalLineNumber - 2; wSpace >= 0 && vadalogRules[wSpace].equals(""); wSpace--) {
				wspaceNum++;
			}
			if ((this.lineNumber - wspaceNum) >= 0) {
				this.lineNumber -= wspaceNum;
			}
			this.isErrorAtPreviousLine = true;
		}
	}

	@Override
	public String handle() {
		ErrorMessage missingDotOrComma;
		String offensiveRule = vadalogRules[lineNumber];
		String inputMessage = "";
		inputMessage = "missing dot, missing comma, uncorrect token or unexpected symbol '"
				+ offensiveRule.charAt(columnNumber) + "'" + " at rule " + offensiveRule;
		if(this.suggestionError.startsWith("missing '.'")) {
			inputMessage = "missing '.' or ',' and unexpected symbol '"
					+ offensiveRule.charAt(columnNumber) + "'" + " at rule " + offensiveRule;
		}
		missingDotOrComma = new MissingCommaOrDotSyntaxErrorMessage(this.lineNumber + 1, this.columnNumber);
		missingDotOrComma.createErrorMessage(inputMessage);
		this.errorMessageObj = missingDotOrComma;
		return missingDotOrComma.getResponseMessage();
	}

	public ErrorMessage getErrorMessageObj() {
		return errorMessageObj;
	}

}

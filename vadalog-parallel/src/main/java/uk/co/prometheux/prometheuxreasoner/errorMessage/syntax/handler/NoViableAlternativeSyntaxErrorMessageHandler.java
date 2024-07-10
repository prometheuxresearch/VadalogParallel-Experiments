package uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.Recognizer;

import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;
import uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.NoViableAlternativeSyntaxErrorMessage;
import uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.VariableWithLowerCaseSyntaxErrorMessage;

public class NoViableAlternativeSyntaxErrorMessageHandler implements SyntaxErrorMessageHandler {

	private int lineNumber;
	private int columnNumber;
	private String tokenError;
	private String symbolError;
	private String vadalogProgram;
	String responseMessage = "";
	boolean isErrorAtPreviousLine = false;
	private Integer originalLineNumber;
	private String[] vadalogRules;
	private ErrorMessage errorMessageObj;

	public NoViableAlternativeSyntaxErrorMessageHandler(Recognizer<?, ?> arg0, Object arg1, int arg2, int arg3,
			String arg4, String vadalogProgram) {
		String[] tokenStringArr = arg1.toString().split(",");
		String lastToken = tokenStringArr[tokenStringArr.length - 1];
		String tokenized = lastToken.substring(0, lastToken.length() - 1);
		String[] lineAndColumn = tokenized.split(":");
		this.originalLineNumber = Integer.valueOf(lineAndColumn[0]);
		this.columnNumber = Integer.valueOf(lineAndColumn[1]);
		this.vadalogProgram = vadalogProgram;
		this.vadalogRules = vadalogProgram.split("\n");
		Pattern pattern = Pattern.compile("'(.*?)'");
		Matcher matcher = pattern.matcher(arg4);
		if (matcher.find()) {
			tokenError = matcher.group(1);
		} else {
			tokenError = "";
		}
		matcher = pattern.matcher(arg1.toString());
		if(matcher.find()) {
			this.symbolError = matcher.group(1);
		}
		else {
			this.symbolError = "";
		}
		this.identifyLineNumber();
		this.identifyColumnNumber();
	}

	private void identifyColumnNumber() {
		if(this.isErrorAtPreviousLine) {
			this.columnNumber = vadalogRules[lineNumber].length() + 1;
		}
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
		ErrorMessage noViableAlternative;
		String offensiveRule = vadalogRules[lineNumber];
		String inputMessage = "";
		if (this.isErrorAtPreviousLine) {
			inputMessage = "missing dot, uncorrect token '"+this.symbolError+"' or unexpected symbol '" + offensiveRule.charAt(columnNumber - 2)+"'"
					+ " at rule " + offensiveRule;
			noViableAlternative = new NoViableAlternativeSyntaxErrorMessage(this.lineNumber + 1, columnNumber - 2);
		} else {
			inputMessage = "missing dot, uncorrect token '"+this.symbolError+"' or unexpected symbol '" + offensiveRule.charAt(columnNumber - 1)+"'"
					+ " at rule " + offensiveRule;
			noViableAlternative = new NoViableAlternativeSyntaxErrorMessage(this.lineNumber + 1, columnNumber - 1);
		}
		char firstCharOfSymbol = this.symbolError.charAt(0);
		if(Character.isLetter(firstCharOfSymbol) && Character.isLowerCase(firstCharOfSymbol)) {
			noViableAlternative = new VariableWithLowerCaseSyntaxErrorMessage(lineNumber + 1, columnNumber);
			inputMessage = "unexpected symbol '"+this.symbolError+"'. If you mean a fact, it must end with a '.'. If you mean a variable, it must start with an upper case letter.";
		}
		inputMessage += "\n Please correct the token " + this.tokenError + " at rule "+ offensiveRule;
		noViableAlternative.createErrorMessage(inputMessage);
		this.errorMessageObj = noViableAlternative;
		return noViableAlternative.getResponseMessage();
	}

	public ErrorMessage getErrorMessageObj() {
		return errorMessageObj;
	}

}

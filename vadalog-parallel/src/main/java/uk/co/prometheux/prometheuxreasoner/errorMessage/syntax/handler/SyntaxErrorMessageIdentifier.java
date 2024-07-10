package uk.co.prometheux.prometheuxreasoner.errorMessage.syntax.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.Recognizer;

public class SyntaxErrorMessageIdentifier {

	public static String vadalogProgram = "";

	public static void setVadalogProgram(InputStream inputStream) throws IOException {
		InputStreamReader isReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(isReader);
		StringBuffer sb = new StringBuffer();
		String str;
		while ((str = reader.readLine()) != null) {
			sb.append(str + "\n");
		}
		vadalogProgram = sb.toString();
	}

	public static void resetVadalogProgram() {
		vadalogProgram = "";
	}

	public static String getVadalogProgram() {
		return vadalogProgram;
	}

	public static List<String> getVadalogProgramAsList() {
		String[] vadalogRules = vadalogProgram.split("\n");
		return Arrays.asList(vadalogRules);
	}

	public static SyntaxErrorMessageHandler getParsingErrorMessageHandler(Recognizer<?, ?> arg0, Object arg1, int arg2,
			int arg3, String arg4, Exception arg5) {
		if (arg5 != null) {

			String classExceptionName = arg5.getClass().getName();
			switch (classExceptionName) {
			case "org.antlr.v4.runtime.NoViableAltException": {
				return new NoViableAlternativeSyntaxErrorMessageHandler(arg0, arg1, arg2, arg3, arg4, vadalogProgram);
			}
			case "org.antlr.v4.runtime.InputMismatchException": {
				return new InputMismatchExceptionSyntaxErrorMessageHandler(arg0, arg1, arg2, arg3, arg4,
						vadalogProgram);
			}
			}
			return new NoViableAlternativeSyntaxErrorMessageHandler(arg0, arg1, arg2, arg3, arg4, vadalogProgram);
		} else {
			return new MissingCommaOrDotSyntaxErrorMessageHandler(arg0, arg1, arg2, arg3, arg4, vadalogProgram);
		}

	}

}

package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.util.Arrays;
import java.util.Objects;

/**
 * An annotation object used for specifying a library import
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class LibraryAnnotation extends AliasAnnotation {

	private static final String ANNOTATION_NAME = "library";
	private final String libraryName;
	private final String methodName;
	private final String parameterStr;

	LibraryAnnotation(String alias, String libraryName, String methodName, String parameterStr) {
		super(alias, ANNOTATION_NAME,
				methodName == null ? Arrays.asList(alias, libraryName) : Arrays.asList(alias, libraryName, methodName));
		assert (libraryName != null);
		this.libraryName = libraryName;
		this.methodName = methodName;
		this.parameterStr = parameterStr;
	}

	/**
	 * a getter for the library name
	 * 
	 * @return the library name
	 */
	public String getLibraryName() {
		return libraryName;
	}

	/**
	 * a getter for the parameters
	 * 
	 * @return the parameter string
	 */
	public String getParameterStr() {
		return parameterStr;
	}

	/**
	 * a getter for the library method name
	 * 
	 * @return the method name
	 */
	public String getMethodName() {
		return methodName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = this.getClass().toString().hashCode();
		result = prime * result + getAlias().hashCode();
		result = prime * result + libraryName.hashCode();
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !obj.getClass().equals(this.getClass()))
			return false;
		LibraryAnnotation that = (LibraryAnnotation) obj;
		return Objects.equals(this.getAlias(), that.getAlias()) && Objects.equals(this.libraryName, that.libraryName)
				&& Objects.equals(this.methodName, that.methodName);
	}
}

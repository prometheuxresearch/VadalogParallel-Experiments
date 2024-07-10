package uk.co.prometheux.prometheuxreasoner.model.annotations;

/**
 * It represents the type of post processing to apply on output.

 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public enum PostProcessingTypeEnum {
	CERTAIN(false),
	ORDERBY(true),
	UNIQUE(false),
	MAX(true),
	MIN(true),
	ARGMAX(true),
	ARGMIN(true),
	LIMIT(true),
	PRELIMIT(true),
	RAND(false);

	boolean mHasArguments;
	boolean hasArguments() {
		return mHasArguments;
	}

	PostProcessingTypeEnum(boolean hasArguments) {
		this.mHasArguments = hasArguments;
	}

	public String toString() {
		switch (this) { 
			case CERTAIN:
				return "certain";
			case ORDERBY:
				return "orderby";
			case UNIQUE:
				return "unique";
			case MAX:
				return "max";
			case ARGMAX:
				return "argmax";
			case ARGMIN:
				return "argmin";
			case MIN:
				return "min";
			case PRELIMIT:
				return "prelimit";
			case RAND:
				return "rand";
			case LIMIT:
				return "limit";
			default:
				return "unknown";
		}
	}

}

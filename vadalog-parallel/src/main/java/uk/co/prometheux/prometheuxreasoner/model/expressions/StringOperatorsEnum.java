package uk.co.prometheux.prometheuxreasoner.model.expressions;


/**
 * This Class features string operators
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
enum StringOperatorsEnum {
	SUBSTRING, CONTAINS, STARTS_WITH, ENDS_WITH, CONCAT, INDEX_OF, STRING_LENGTH;
	
		public String toString() {
			switch (this) {
				case SUBSTRING:
					return "substring";
				case CONTAINS:
					return "contains";
				case STARTS_WITH:
					return "starts_with";
				case ENDS_WITH:
					return "ends_with";
				case CONCAT:
					return "concat";
				case INDEX_OF:
					return "index_of";
				case STRING_LENGTH:
					return "string_length";
				default:
					return null;
			}
		}
}

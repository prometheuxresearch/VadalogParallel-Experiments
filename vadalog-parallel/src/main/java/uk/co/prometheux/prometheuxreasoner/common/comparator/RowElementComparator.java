package uk.co.prometheux.prometheuxreasoner.common.comparator;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * This Class is an helper to compare two Row elements
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class RowElementComparator {

	public static int compare(Object first, Object second) {
		assert (first.getClass().equals(second.getClass()));
		if (first instanceof String) {
			return compare((String) first, (String) second);
		}
		if (first instanceof Integer) {
			return compare((Integer) first, (Integer) second);
		}
		if (first instanceof Double) {
			return compare((Double) first, (Double) second);
		}
		if (first instanceof List<?>) {
			return compare((List<?>) first, (List<?>) second);
		}
		if (first instanceof Set<?>) {
			return compare((Set<?>) first, (Set<?>) second);
		}
		if (first instanceof Date) {
			return compare((Date) first, (Date) second);
		}
		if (first instanceof Timestamp) {
			return compare((Timestamp) first, (Timestamp) second);
		}
		return 1;
	}

	private static int compare(String first, String second) {
		return first.compareTo(second);
	}

	public static int compare(Integer first, Integer second) {
		return first.compareTo(second);
	}

	public static int compare(Double first, Double second) {
		return first.compareTo(second);
	}

	public static int compare(List<?> first, List<?> second) {
		if (first.size() == second.size())
			return 0;
		if (first.size() > second.size()) {
			return 1;
		}
		return -1;
	}

	public static int compare(Set<?> first, Set<?> second) {
		if (first.size() == second.size())
			return 0;
		if (first.size() > second.size()) {
			return 1;
		}
		return -1;
	}

	private static int compare(Date first, Date second) {
		return first.compareTo(second);
	}

	private static int compare(Timestamp first, Timestamp second) {
		return first.compareTo(second);
	}

}

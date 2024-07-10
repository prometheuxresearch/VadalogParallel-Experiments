package uk.co.prometheux.prometheuxreasoner.model.expressions;

/**
 * The list of aggregate operators
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public enum AggregateOperator {
	MSUM("msum", true, true),
	MPROD("mprod", false, true),
	MCOUNT("mcount", true, true),
	MUNION("munion", true, true),
	MMAX("mmax", true, true),
	MMIN("mmin", false, true),
	UNION("union", true, false),
    LIST("list", true, false),
    SET("set", true, false),
	COUNT("count", true, false),
	SUM("sum", true, false),
	PROD("prod", false, false),
	MIN("min", false, false),
	MAX("max", true, false),
	AVG("avg", true, false);

	private final String name;
	private final boolean isIncreasing;
    private final boolean isMonotonic;

    AggregateOperator(String name, boolean isIncreasing, boolean isMonotonic) {
		this.name = name;
		this.isIncreasing = isIncreasing;
        this.isMonotonic = isMonotonic;
    }

    @Override
    public String toString() {
		return name;
	}

    /**
     * Returns the aggregate name
     * @return the aggregate name
     */
	public String getName() {
	    return name;
    }

    /**
     * It returns whether the monotonic aggregation is increasing (true)
     * 	 * or decreasing (false).
     * @return whether the aggregation is increasing
     */
	public boolean isIncreasing() {
		return isIncreasing;
	}

    public boolean isMonotonic() {
        return isMonotonic;
    }
}



package uk.co.prometheux.prometheuxreasoner.model;

import uk.co.prometheux.prometheuxreasoner.model.expressions.Expression;

/**
 * A generic Term (Variable or Constant)
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 *
 */
public abstract class Term extends Expression {

	private static final long serialVersionUID = 1L;

	public Term() {
		super();
	}

	/**
	 * It clones this Term
	 * 
	 * @param renameVariables whether the variables have to be renamed
	 * @return the cloned Term
	 */
	@Override
	public abstract Term clone(boolean renameVariables);

	@Override
	public int getArity() {
		return 0;
	}

	public String getName() {
		return getOperationName();
	}

}

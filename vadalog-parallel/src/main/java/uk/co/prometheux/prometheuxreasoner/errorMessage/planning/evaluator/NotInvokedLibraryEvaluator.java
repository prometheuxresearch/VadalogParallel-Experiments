package uk.co.prometheux.prometheuxreasoner.errorMessage.planning.evaluator;

import java.util.Collection;

import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;
import uk.co.prometheux.prometheuxreasoner.errorMessage.planning.NotInvokedLibraryErrorMessage;
import uk.co.prometheux.prometheuxreasoner.model.annotations.AliasAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.annotations.LibraryAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.expressions.ImplementationExpression;
import uk.co.prometheux.prometheuxreasoner.planner.PlanningException;

/**
 * This class represents an evaluator for libraries in the planning phase
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class NotInvokedLibraryEvaluator implements PlanningEvaluator {

	private ImplementationExpression expression;
	private Collection<AliasAnnotation> implementationAliases;

	public NotInvokedLibraryEvaluator(ImplementationExpression expression,
			Collection<AliasAnnotation> implementationAliases) {
		this.expression = expression;
		this.implementationAliases = implementationAliases;
	}

	@Override
	public void evaluate() {
		boolean match = false;
		String operationName = this.expression.getOperationName();
		String operationNameUsedAlias = operationName.split(":")[0];
		for (AliasAnnotation aliasAnnotation : implementationAliases) {
			LibraryAnnotation annotation = (LibraryAnnotation) aliasAnnotation;
			if (annotation.getAlias().startsWith(operationNameUsedAlias)) {
				match = true;
				break;
			}
		}
		if (!match) {
			String inputMessage = "The operation " + expression.getOperationName() + " is used without "
					+ "importing a correct library.";
			ErrorMessage errorMessage = new NotInvokedLibraryErrorMessage(expression.getOperationName());
			errorMessage.createErrorMessage(inputMessage);
			throw new PlanningException(errorMessage);
		}
	}

}

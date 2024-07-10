package uk.co.prometheux.prometheuxreasoner.errorMessage.annotations.evaluator;

import java.util.List;

import uk.co.prometheux.prometheuxreasoner.errorMessage.ErrorMessage;
import uk.co.prometheux.prometheuxreasoner.errorMessage.annotations.InputAnnotationErrorMessage;
import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Constant;
import uk.co.prometheux.prometheuxreasoner.model.Term;


public class InputAnnotationEvaluator implements AnnotationEvaluator {
	
	private Atom inputAnnotation;

	public InputAnnotationEvaluator(Atom inputAnnotation) {
		this.inputAnnotation = inputAnnotation;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void evaluate() {
		ErrorMessage errorMessage = new InputAnnotationErrorMessage(inputAnnotation);
		List<Term> arguments = inputAnnotation.getArguments();
		if(arguments.size() != 1) {
			errorMessage.createErrorMessage("The input annotation must have exactly one argument, but you specified "+arguments.size()+" arguments");
			throw new AnnotationException(errorMessage);
		}
		if(!(((Constant<String>) arguments.get(0)).strip() instanceof String)){
			errorMessage.createErrorMessage("The input predicate name must be a string, "
					+ "but you put '"+arguments.get(0).toString()+"'");
			throw new AnnotationException(errorMessage);
		}
	}

}

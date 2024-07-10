package uk.co.prometheux.prometheuxreasoner.errorMessage.annotations.evaluator;

import java.util.List;

import uk.co.prometheux.prometheuxreasoner.errorMessage.annotations.ExecutionModeAnnotationErrorMessage;
import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Constant;
import uk.co.prometheux.prometheuxreasoner.model.Term;

public class ExecutionModeAnnotationEvaluator implements AnnotationEvaluator {
	
	private Atom bindAnnotation;

	public ExecutionModeAnnotationEvaluator(Atom bindAnnotation) {
		this.bindAnnotation = bindAnnotation;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void evaluate() {
		ExecutionModeAnnotationErrorMessage errorMessage = new ExecutionModeAnnotationErrorMessage(bindAnnotation);
		List<Term> arguments = bindAnnotation.getArguments();
		if(arguments.size() != 1) {
			errorMessage.createErrorMessage("The executionMode annotation must have exactly one arguments, but you specified "+arguments.size()+" arguments");
			throw new AnnotationException(errorMessage);
		}
		if(!(((Constant<String>) arguments.get(0)).strip() instanceof String)){
			errorMessage.createErrorMessage("The execution mode argument must be a string, but you put '"+arguments.get(0).toString()+"'");
			throw new AnnotationException(errorMessage);
		}
		else {
			String executionMode = ((Constant<String>) arguments.get(0)).strip().toString();
			if(!executionMode.equals("streaming")
					&& !executionMode.equals("distributed")){
				errorMessage.createErrorMessage("'"+executionMode+"' is not an allowed execution mode. Available execution modes are 'streaming' or 'distributed'");
				throw new AnnotationException(errorMessage);
			}
		}
	}

}

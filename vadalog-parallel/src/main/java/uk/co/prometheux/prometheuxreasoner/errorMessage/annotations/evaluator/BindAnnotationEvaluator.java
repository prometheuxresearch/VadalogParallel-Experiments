package uk.co.prometheux.prometheuxreasoner.errorMessage.annotations.evaluator;

import java.util.List;

import uk.co.prometheux.prometheuxreasoner.errorMessage.annotations.BindAnnotationErrorMessage;
import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Constant;
import uk.co.prometheux.prometheuxreasoner.model.Term;


public class BindAnnotationEvaluator implements AnnotationEvaluator {
	
	private Atom bindAnnotation;

	public BindAnnotationEvaluator(Atom bindAnnotation) {
		this.bindAnnotation = bindAnnotation;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void evaluate() {
		BindAnnotationErrorMessage errorMessage = new BindAnnotationErrorMessage(bindAnnotation);
		List<Term> arguments = bindAnnotation.getArguments();
		if(arguments.size() != 4) {
			errorMessage.createErrorMessage("The bind annotation must have exactly four arguments, but you specified "+arguments.size()+" arguments");
			throw new AnnotationException(errorMessage);
		}
		if(!(((Constant<String>) arguments.get(0)).strip() instanceof String)){
			errorMessage.createErrorMessage("The predicate name must be a string, "
					+ "but you put '"+arguments.get(0).toString()+"'");
			throw new AnnotationException(errorMessage);
		}
		String datasource = arguments.get(1).toString();
		if(!(((Constant<String>) arguments.get(1)).strip() instanceof String)){
			errorMessage.createErrorMessage("source or db "+datasource+ " must be a string, "
					+ "but you put '"+datasource+"'");
			throw new AnnotationException(errorMessage);
		}
		if(!(((Constant<String>) arguments.get(2)).strip() instanceof String)){
			errorMessage.createErrorMessage("path or schema "+arguments.get(2).toString()+ " must be a string,"
					+ "but you put '"+arguments.get(2).toString()+"'");
			throw new AnnotationException(errorMessage);
		}
		if(!(((Constant<String>) arguments.get(3)).strip() instanceof String)){
			errorMessage.createErrorMessage("file or query" +arguments.get(3).toString()+ " must be a string, "
					+ "but you put '"+arguments.get(3).toString()+"'");
			throw new AnnotationException(errorMessage.getResponseMessage());
		}
//		List<String> dataSources = DistributedReaderFactory.getInstance().getDataSourcesList();
//		datasource = datasource.replaceAll("\"", "").toString();
//		if(!dataSources.contains(datasource)) {
//			errorMessage.createErrorMessage("Datasource "+datasource+" is not supported.\n"
//					+ "Supported datasources are "+ dataSources);
//			throw new AnnotationException(errorMessage.getResponseMessage());
//		}
	}

}

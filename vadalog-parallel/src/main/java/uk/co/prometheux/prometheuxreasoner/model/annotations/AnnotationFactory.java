package uk.co.prometheux.prometheuxreasoner.model.annotations;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.prometheux.prometheuxreasoner.errorMessage.annotations.evaluator.AnnotationEvaluator;
import uk.co.prometheux.prometheuxreasoner.errorMessage.annotations.evaluator.BindAnnotationEvaluator;
import uk.co.prometheux.prometheuxreasoner.errorMessage.annotations.evaluator.InputAnnotationEvaluator;
import uk.co.prometheux.prometheuxreasoner.errorMessage.annotations.evaluator.OutputAnnotationEvaluator;
import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Constant;
import uk.co.prometheux.prometheuxreasoner.model.Term;

/**
 * It builds Datalog annotations on the basis of the various atoms
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class AnnotationFactory {

	private Logger log = LoggerFactory.getLogger(AnnotationFactory.class);

	private static AnnotationFactory instance = new AnnotationFactory();

	private AnnotationFactory() {
	}

	public static AnnotationFactory getInstance() {
		return instance;
	}

	/**
	 * It returns the right kind of annotation given its atom.
	 * 
	 * @param a the atom to build the annotation from
	 * @return the annotation
	 */
	@SuppressWarnings("unchecked")
	public DatalogAnnotation getAnnotation(Atom a) {

		String annotationType = a.getName();
		DatalogAnnotation da = null;
		
        AnnotationEvaluator evaluator;
		switch (annotationType) {
		case "bind": {
            evaluator = new BindAnnotationEvaluator(a);
            evaluator.evaluate();
			String predicateName, connection, schema, table;
			if (a.getArguments().size() == 4) {
				predicateName = ((Constant<String>) a.getArguments().get(0)).strip();
				connection = ((Constant<String>) a.getArguments().get(1)).strip();
				schema = ((Constant<String>) a.getArguments().get(2)).strip();
				table = ((Constant<String>) a.getArguments().get(3)).strip();
				da = new BindAnnotation(predicateName, connection, schema, table, false);
			} else {
				log.warn("Input annotation with wrong number of parameters. Ignored.");
				da = this.getDefaultAnnotation(a);
			}
			break;
		}
		case "qbind": {
            evaluator = new BindAnnotationEvaluator(a);
            evaluator.evaluate();
			String qPredicateName, qConnection, qSchema, query;
			if (a.getArguments().size() == 4) {
				qPredicateName = ((Constant<String>) a.getArguments().get(0)).strip();
				qConnection = ((Constant<String>) a.getArguments().get(1)).strip();
				qSchema = ((Constant<String>) a.getArguments().get(2)).strip();
				query = ((Constant<String>) a.getArguments().get(3)).strip();
				da = new BindAnnotation(qPredicateName, qConnection, qSchema, query, true);
			} else {
				log.warn("Input annotation with wrong number of parameters. Ignored.");
				da = this.getDefaultAnnotation(a);
			}
			break;
		}
//		case "chase": {
//            evaluator = new BindAnnotationEvaluator(a);
//            evaluator.evaluate();
//			String predicateName, connection, schema, table;
//			if (a.getArguments().size() == 4) {
//				predicateName = ((Constant<String>) a.getArguments().get(0)).strip();
//				connection = ((Constant<String>) a.getArguments().get(1)).strip();
//				schema = ((Constant<String>) a.getArguments().get(2)).strip();
//				table = ((Constant<String>) a.getArguments().get(3)).strip();
//				da = new ChaseAnnotation(predicateName, connection, schema, table, false);
//			} else {
//				log.warn("Chase annotation with wrong number of parameters. Ignored.");
//				da = this.getDefaultAnnotation(a);
//			}
//			break;
//		}
		case "mapping": {
			String predicateName, colName, colType;
			int varPos;
			if (a.getArguments().size() == 4) {
				predicateName = ((Constant<String>) a.getArguments().get(0)).strip();
				varPos = ((Constant<Integer>) a.getArguments().get(1)).strip();
				colName = ((Constant<String>) a.getArguments().get(2)).strip();
				colType = ((Constant<String>) a.getArguments().get(3)).strip();
				da = new MappingAnnotation(predicateName, varPos, colName, colType);
			} else {
				log.warn("Mapping annotation with wrong number of parameters. Ignored.");
				da = this.getDefaultAnnotation(a);
			}

			break;
		}
		case "library": {
			if (a.getArguments().size() <= 4 && a.getArguments().size() >= 2) {
				String alias = ((Constant<String>) a.getArguments().get(0)).strip();
				String library = ((Constant<String>) a.getArguments().get(1)).strip();
				String methodName = null;
				String parameterStr = null;
				if (a.getArguments().size() >= 3) {
					methodName = ((Constant<String>) a.getArguments().get(2)).strip();
					if (methodName.length() == 0)
						methodName = null;
				}
				if (a.getArguments().size() >= 4) {
					parameterStr = ((Constant<String>) a.getArguments().get(3)).strip();
				}
				return new LibraryAnnotation(alias, library, methodName, parameterStr);
			} else {
				log.warn("Library annotation with wrong number of parameters. Ignored.");
				da = this.getDefaultAnnotation(a);
			}
			break;
		}
		case "input": {
            evaluator = new InputAnnotationEvaluator(a);
            evaluator.evaluate();
			if (a.getArguments().size() == 1) {
				String predicateName = ((Constant<String>) a.getArguments().get(0)).strip();
				da = new InputAnnotation(predicateName);
			} else {
				log.warn("Input annotation with wrong number of parameters. Ignored.");
				da = this.getDefaultAnnotation(a);
			}
			break;
		}
		case "output": {
            evaluator = new OutputAnnotationEvaluator(a);
            evaluator.evaluate();
			if (a.getArguments().size() == 1) {
				String predicateName = ((Constant<String>) a.getArguments().get(0)).strip();
				da = new OutputAnnotation(predicateName);
			} else {
				log.warn("Output annotation with wrong number of parameters. Ignored.");
				da = this.getDefaultAnnotation(a);
			}
			break;
		}
		case "post": {
			if (a.getArguments().size() != 2) {
				log.warn("Postprocessing annotation with wrong number of parameters. Ignored.");
				da = this.getDefaultAnnotation(a);
				break;
			}

			String predicateName = ((Constant<String>) a.getArguments().get(0)).strip();
			String directive = ((Constant<String>) a.getArguments().get(1)).strip();

			/* here we parse the specific directive */
			PostProcessingTypeEnum postType = null;
			for (PostProcessingTypeEnum type : PostProcessingTypeEnum.values())
				if (directive.toLowerCase().startsWith(type.toString()))
					postType = type;
			if (postType == null) {
				log.warn("Unknown postprocessing directive. Ignored");
				da = this.getDefaultAnnotation(a);
				break;
			}

			/* at this point we parse the parameters of the annotation */
			int start = directive.toLowerCase().indexOf("(");
			int end = directive.toLowerCase().indexOf(")");
			if ((start == -1 || end < start) && postType.hasArguments()) {
				log.warn("Wrong postprocessing parameters. Ignored");
				da = this.getDefaultAnnotation(a);
				break;
			}

			List<Integer> integerParams = new ArrayList<>();
			/* in the cases where we have parameters */
			if (postType.hasArguments()) {
				String params = directive.toLowerCase().substring(start + 1, end);
				params = params.replaceAll("\\s+", "");
				StringTokenizer st = new StringTokenizer(params, ",");

				try {
					while (st.hasMoreTokens()) {
						if (postType == PostProcessingTypeEnum.ARGMAX || postType == PostProcessingTypeEnum.ARGMIN) {
							integerParams.add(Integer.parseInt(st.nextToken().replace("<", "").replace(">", "")));
						} else {
							integerParams.add(Integer.parseInt(st.nextToken()));
						}
					}
				} catch (NumberFormatException ex) {
					log.warn("Directive " + directive + " with wrongly typed parameters.");
					break;
				}
				if (integerParams.size() < 1) {
					log.warn("Directive " + directive + " with wrong number of parameters.");
					break;
				}
			}
			/* we create the annotation and set the parameters */
			da = new PostProcessingAnnotation(predicateName, postType, integerParams);
			break;
		}
		case ModuleAnnotation.ANNOTATION_NAME: {
			String moduleName;
			if (a.getArguments().size() == 1) {
				moduleName = ((Constant<String>) a.getArguments().get(0)).strip();
				da = new ModuleAnnotation(moduleName);
			} else {
				log.warn("Module annotation with wrong number of parameters. Ignored.");
				da = this.getDefaultAnnotation(a);
			}
			break;
		}
		case IncludeAnnotation.ANNOTATION_NAME: {
			String moduleName;
			if (a.getArguments().size() == 1) {
				moduleName = ((Constant<String>) a.getArguments().get(0)).strip();
				da = new IncludeAnnotation(moduleName);
			} else {
				log.warn("Include annotation with wrong number of parameters. Ignored.");
				da = this.getDefaultAnnotation(a);
			}
			break;
		}

		case "optimizationStrategy": {
			String optimizationStrategyName = ((Constant<String>) a.getArguments().get(0)).strip();
			da = new OptimizationAnnotation(optimizationStrategyName);
			break;
		}

		default:
			da = getDefaultAnnotation(a);
			log.warn("Unknown annotation: " + annotationType + ". DefaultAnnotation created.");
			break;

		}
		return da;
	}

	/**
	 * It returns a default annotation
	 * 
	 * @param a the atom
	 * @return the default annotation
	 */
	private DatalogAnnotation getDefaultAnnotation(Atom a) {
		String annotationName = a.getName();
		List<Object> terms = new ArrayList<>();
		for (Term t : a.getArguments()) {
			if (t instanceof Constant<?>)
				terms.add(((Constant<?>) t).strip());
		}
		return new DefaultAnnotation(annotationName, terms);
	}
}

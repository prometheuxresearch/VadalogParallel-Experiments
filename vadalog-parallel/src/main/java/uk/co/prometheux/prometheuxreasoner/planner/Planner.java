package uk.co.prometheux.prometheuxreasoner.planner;

import java.util.ArrayList;
import java.util.List;
//import org.apache.spark.sql.functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import uk.co.prometheux.prometheuxreasoner.common.visitor.SparkExpressionVisitor;
//import uk.co.prometheux.prometheuxreasoner.errorMessage.planning.evaluator.OutputPlanningEvaluator;
//import uk.co.prometheux.prometheuxreasoner.errorMessage.planning.evaluator.PlanningEvaluator;
import uk.co.prometheux.prometheuxreasoner.evaluator.ProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.evaluator.asp.ASPProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.evaluator.closelinks.CloseLinksProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.evaluator.companycontrol.CompanyControlProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.evaluator.defaultpropagation.DefaultPropagationProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.evaluator.n2c.N2CProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.evaluator.psc.PSCProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.evaluator.samegeneration.SameGenerationProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.evaluator.stronglinks.StrongLinksProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.evaluator.transitiveClosure.TransitiveClosureProgramEvaluator;
import uk.co.prometheux.prometheuxreasoner.evaluator.trianglecounting.TriangleCountingProgramEvaluator;
//import uk.co.prometheux.prometheuxreasoner.function.FilterCondition;
//import uk.co.prometheux.prometheuxreasoner.model.Atom;
//import uk.co.prometheux.prometheuxreasoner.model.ComparisonOperatorsEnum;
//import uk.co.prometheux.prometheuxreasoner.model.Condition;
//import uk.co.prometheux.prometheuxreasoner.model.Constant;
//import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
//import uk.co.prometheux.prometheuxreasoner.model.Term;
//import uk.co.prometheux.prometheuxreasoner.model.Variable;
//import uk.co.prometheux.prometheuxreasoner.model.annotations.AliasAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.annotations.BindAnnotation;
//import uk.co.prometheux.prometheuxreasoner.model.annotations.ChaseAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.annotations.DatalogAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.annotations.InputAnnotation;
//import uk.co.prometheux.prometheuxreasoner.model.annotations.InputOutputAnnotation;
//import uk.co.prometheux.prometheuxreasoner.model.annotations.LibraryAnnotation;
//import uk.co.prometheux.prometheuxreasoner.model.annotations.MappingAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.annotations.OutputAnnotation;
//import uk.co.prometheux.prometheuxreasoner.model.annotations.PostProcessingAnnotation;
//import uk.co.prometheux.prometheuxreasoner.model.annotations.TypeEnum;
//import uk.co.prometheux.prometheuxreasoner.model.expressions.AggregationExpression;
//import uk.co.prometheux.prometheuxreasoner.model.expressions.Expression;
//import uk.co.prometheux.prometheuxreasoner.model.expressions.ImplementationExpression;
//import uk.co.prometheux.prometheuxreasoner.operators.AtomOperatorDecorator;
//import uk.co.prometheux.prometheuxreasoner.planner.aggregation.AggregationHelper;
//import uk.co.prometheux.prometheuxreasoner.planner.explain.DatasetChaseOutputPlan;
import uk.co.prometheux.prometheuxreasoner.pwlWarded.ModelPWLWardedDecorator;
//import uk.co.prometheux.prometheuxreasoner.record.FactTable;
//import uk.co.prometheux.prometheuxreasoner.warded.ModelWardedDecorator;
//import uk.co.prometheux.prometheuxreasoner.warded.Position;

/**
 * This Class represents Planner for reproduce Vadalog Parallel specific
 * programs
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 */

public class Planner {

	/* The log for this planner */
	private Logger log = LoggerFactory.getLogger(Planner.class);

	public Planner() {
	}

	public ProgramEvaluator makePlan(Model m) {
		log.info("Planning");

		ModelPWLWardedDecorator pwlwd = new ModelPWLWardedDecorator(m);

		for (Rule r : m.getRules()) {

			/* here we learn whether r is recursive or not */
			boolean isRecursive = pwlwd.isRecursive(r);
			log.info(r + " (recursive?) : " + isRecursive);

			boolean isExitRule = pwlwd.isExitRule(r);
			log.info(r + " (exitRule?) : " + isExitRule);

			boolean negationAsFailure = r.isNegationAsFailure();
			log.info(r + " (negationAsFailure?) : " + negationAsFailure);
			
			boolean tgd = r.isTGD();
			log.info(r + " (tgd?) : " + tgd);
			
			boolean linear = r.isLinear();
			log.info(r + " (linear?) : " + linear);
			
			boolean isPWLW = pwlwd.isPWLW(r);
			log.info(r + " (PWLW?) : " + isPWLW);
		}

		List<List<BindAnnotation>> inputOutputAnnotations = this.processAnnotations(m);
		
		if (isTransitiveClosure(m)) {
			return new TransitiveClosureProgramEvaluator(inputOutputAnnotations.get(0), inputOutputAnnotations.get(1));
		}

		if (isSameGeneration(m)) {
			return new SameGenerationProgramEvaluator(inputOutputAnnotations.get(0), inputOutputAnnotations.get(1));
		}
		
		if (isTriangleCounting(m)) {
			return new TriangleCountingProgramEvaluator(inputOutputAnnotations.get(0), inputOutputAnnotations.get(1));
		}
		
		if (isASP(m)) {
			return new ASPProgramEvaluator(inputOutputAnnotations.get(0), inputOutputAnnotations.get(1));
		}
		if (isCloseLinks(m))
			return new CloseLinksProgramEvaluator(inputOutputAnnotations.get(0), inputOutputAnnotations.get(1));

		if (isCompanyControl(m)) {
			return new CompanyControlProgramEvaluator(inputOutputAnnotations.get(0), inputOutputAnnotations.get(1));
		}

		if (isDefaultPropagation(m)) {
			return new DefaultPropagationProgramEvaluator(inputOutputAnnotations.get(0), inputOutputAnnotations.get(1));
		}
		if (isN2C(m)) {
			return new N2CProgramEvaluator(inputOutputAnnotations.get(0), inputOutputAnnotations.get(1));
		}

		if (isPsc(m)) {
			return new PSCProgramEvaluator(inputOutputAnnotations.get(0), inputOutputAnnotations.get(1));
		}

		if (isStrongLinks(m)) {
			return new StrongLinksProgramEvaluator(inputOutputAnnotations.get(0), inputOutputAnnotations.get(1));
		}

		throw new IllegalArgumentException("Unexpected program: " + m);
	}

	private boolean isTransitiveClosure(Model m) {
		Model transitiveClosure = new Model();
		transitiveClosure.readRule("tc(X,Y) :- arc(X,Y).");
		transitiveClosure.readRule("tc(X,Z) :- tc(X,Y), arc(Y,Z).");
		return m.getRules().equals(transitiveClosure.getRules());
	}

	private boolean isSameGeneration(Model m) {
		Model sameGeneration = new Model();
		sameGeneration.readRule("sg(X,Y) :- arc(P,X), arc(P,Y), X != Y.");
		sameGeneration.readRule("sg(X,Y) :- arc(A,X), sg(A,B), arc(B,Y).");
		return m.getRules().equals(sameGeneration.getRules());
	}

	private boolean isTriangleCounting(Model m) {
		Model triangleCounting = new Model();
		triangleCounting.readRule("t(X,Y,Z) :- arc(X,Y), arc(Y,Z), arc(Z,X), X < Y, Y < Z.");
		triangleCounting.readRule("count_t(C) :- t(X,Y,Z), C = mcount(1).");
		return m.getRules().equals(triangleCounting.getRules());
	}

	private boolean isASP(Model m) {
		Model ASP = new Model();
		ASP.readRule("asp(X,Y,DM) :- arc(X,Y,D), DM = mmin(D).");
		ASP.readRule("asp(X,Y,DM) :- asp(X,Y,D1), arc(Y,Z,D2), DM = mmin(D1+D2).");
		return m.getRules().equals(ASP.getRules());
	}

	private boolean isN2C(Model m) {
		Model N2C = new Model();
		N2C.readRule("odd(X,Y) :- edge(X,Y).");
		N2C.readRule("even(X,Z) :- odd(X,Y), edge(Y,Z).");
		N2C.readRule("odd(X,Z) :- even(X,Y), edge(Y,Z).");
		return m.getRules().equals(N2C.getRules());
	}

	private boolean isCloseLinks(Model m) {
		Model closeLinks = new Model();
		closeLinks.readRule("mcl(X,Y,TW) :- own(X,Y,W), TW = msum(W).");
		closeLinks.readRule("mcl(X,Z,TW) :- mcl(X,Y,W1), own(Y,Z,W2), TW = msum(W1*W2).");
		closeLinks.readRule("cl(X,Y) :- mcl(X,Z,TW), TW >= 0.2.");
		return m.getRules().equals(closeLinks.getRules());
	}

	private boolean isCompanyControl(Model m) {
		Model companyControl = new Model();
		companyControl.readRule("controlledShares(X,Y,Y,W) :- own(X,Y,W), X != Y.");
		companyControl.readRule("controlledShares(X,Z,Y,W) :- control(X,Y), own(Y,Z,W), X != Z.");
		companyControl.readRule("tControlledShares(X,Z,TW) :- controlledShares(X,Z,Y,W), TW = msum(W).");
		companyControl.readRule("control(X,Z) :- tControlledShares(X,Z,TW), TW > 0.5.");
		return m.getRules().equals(companyControl.getRules());
	}

	private boolean isPsc(Model m) {
		Model PSC = new Model();
		PSC.readRule("psc(X,X,P) :- keyPerson(X,P), person(P).");
		PSC.readRule("psc(X,X,P) :- company(X).");
		PSC.readRule("psc(X,Z,P) :- control(Y,X), psc(Y,Z,P).");
		return m.getRules().equals(PSC.getRules());
	}

	private boolean isStrongLinks(Model m) {
		Model strongLinks = new Model();
		strongLinks.readRule("psc(X,X,P) :- keyPerson(X,P), person(P).");
		strongLinks.readRule("psc(X,X,P) :- company(X).");
		strongLinks.readRule("psc(X,Z,P) :- control(Y,X), psc(Y,Z,P).");
		strongLinks.readRule("sl(X,Y,W,Z) :- psc(X,Z,P), psc(Y,Z,P), X != Y, W = mcount(1), W > 3.");
		return m.getRules().equals(strongLinks.getRules());
	}

	private boolean isDefaultPropagation(Model m) {
		Model defaultPropagation = new Model();
		defaultPropagation.readRule("dflt(A,A,D) :- finEntity(A,P), P > 0.5.");
		defaultPropagation.readRule("dflt(C,A,D2) :- dflt(B,A,D1), loan(B,C,LGD), LGD >= 0.5.");
		defaultPropagation.readRule("dflt(C,A,D2) :- dflt(B,A,D1), security(B,C,S), S >= 0.3.");
		return m.getRules().equals(defaultPropagation.getRules());
	}

	/**
	 * 
	 * For the reproducibility of the Vadalog Parallel Paper we just need the path
	 * to input datasource
	 */
	private List<List<BindAnnotation>> processAnnotations(Model m) {
		List<BindAnnotation> inputBinds = new ArrayList<>();
		List<BindAnnotation> outputBinds = new ArrayList<>();

		List<List<BindAnnotation>> inputAndOutputBinds = new ArrayList<List<BindAnnotation>>();
		inputAndOutputBinds.add(inputBinds);
		inputAndOutputBinds.add(outputBinds);

		log.info("processing annotations");

		// let's start with annotations
		for (DatalogAnnotation da : m.getAnnotations()) {
			if (da instanceof InputAnnotation) {
				String inputPredicateName = ((InputAnnotation) da).getPredicateName();
				List<BindAnnotation> bas = m.getBindAnnotationsByPredicateName(inputPredicateName);
				inputBinds.addAll(bas);
			}
			if (da instanceof OutputAnnotation) {
				String inputPredicateName = ((OutputAnnotation) da).getPredicateName();
				List<BindAnnotation> bas = m.getBindAnnotationsByPredicateName(inputPredicateName);
				outputBinds.addAll(bas);
			}
		}
		if (inputBinds.isEmpty()) {
			throw new PlanningException("No bindings for input datasources");
		}
		if (inputBinds.isEmpty()) {
			throw new PlanningException("No bindings for output datasources");
		}
		
		return inputAndOutputBinds;
	}
	// get the mappings
//				List<MappingAnnotation> mas = m.getMappingAnnotationsByPredicateName(predicateName);
//				if (da instanceof OutputAnnotation) {
//					if (log.isDebugEnabled())
//						log.debug("No mappings for output annotation: " + da + ". Let's infer them.");
//					inferInputOutputMappings(m, (InputOutputAnnotation) da, mas);
//				} else if (da instanceof InputAnnotation) {
//					if (log.isDebugEnabled())
//						log.debug("No mappings for input annotation: " + da + ". Let's infer them.");
//					inferInputOutputMappings(m, (InputOutputAnnotation) da, mas);
//				}

	// for now we assume that the mappings are all alike
	// we will change this assumption, in case we support multiple bindings with
	// different mappings.
//				List<List<MappingAnnotation>> maList = new ArrayList<>();
//				for (int j = 0; j < bas.size(); j++)
//					maList.add(mas);

	// create and save the right plan
//				if (da instanceof InputAnnotation) {
//					DatasetInputPlan dip = new DatasetInputPlan(predicateName, bas, maList);
//					dip.setDescription(predicateName);
//					inputPlans.add(dip);
//				} else if (da instanceof OutputAnnotation) {
	// for now we support one single output binding
//					DatasetOutputPlan op = new DatasetOutputPlan(predicateName, bas.get(0), mas);
//					op.setDescription(predicateName);

	// if the output plan has to be wrapped by a post processing one or not.
//					List<PostProcessingAnnotation> ppa = m.getPostProcessingAnnotationByPredicateName(predicateName);

//					DatasetOutputPlan opR = op;

	// if the output plan has to be wrapped by a post processing one or not.
//					if (ppa.size() > 0) {
//						opR = new DatasetPostprocessingOutputPlan(op, ppa);
//					}

	/*
	 * here we get all the chase annotations to apply to the OutputProcessors.
	 */
//					List<ChaseAnnotation> chaseAnnotations = m.getChaseAnnotationByPredicateName(predicateName);

//					if (chaseAnnotations.size() > 0) {
	/*
	 * if the post processing post processing output plan that wraps an output plan
	 * has to be wrapped by a chase output processor one or not.
	 */
//						DatasetOutputPlan opChase = new DatasetChaseOutputPlan(opR, chaseAnnotations.get(0));
//						outputPlans.add(opChase);
//					} else {
//						outputPlans.add(opR);
//					}
//				}
//			}

//		}
//		log.info("END processing annotations");
//	}

}

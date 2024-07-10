package uk.co.prometheux.prometheuxreasoner.optimizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.prometheux.prometheuxreasoner.aggregation.NonMonotonicAggregatesRewriter;
import uk.co.prometheux.prometheuxreasoner.common.ConfigurationManager;
import uk.co.prometheux.prometheuxreasoner.common.IdGenerator;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Model;
//import uk.co.prometheux.prometheuxreasoner.model.ParametricQbindComparator;
import uk.co.prometheux.prometheuxreasoner.model.PredicateGraph;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.annotations.DatalogAnnotation;
import uk.co.prometheux.prometheuxreasoner.model.annotations.OutputAnnotation;
import uk.co.prometheux.prometheuxreasoner.nearlyLinear.ModelNearlyLinearDecorator;
import uk.co.prometheux.prometheuxreasoner.warded.ModelWardedDecorator;
import uk.co.prometheux.prometheuxreasoner.warded.NotWardedException;
import uk.co.prometheux.prometheuxreasoner.warded.ReasoningSemantics;
import uk.co.prometheux.prometheuxreasoner.warded.distributed.ModelDistributedDecorator;
import uk.co.prometheux.prometheuxreasoner.warded.egdHarmless.HarmlessEGDSufficientConditionException;
import uk.co.prometheux.prometheuxreasoner.warded.egdHarmless.ModelHarmlessEGDDecorator;
import uk.co.prometheux.prometheuxreasoner.warded.failure.ModelFailureDecorator;

/**
 * This is the optimizer class for the evaluation of Vadalog programs. It
 * applies the rewriting and optimization steps to the program target of the
 * evaluation.
 * 
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * 
 * @author Prometheux Limited
 * 
 */
public class ProgramEvaluationOptimizer {

	private Model model;
	private Logger log = LoggerFactory.getLogger(ProgramEvaluationOptimizer.class);

	public ProgramEvaluationOptimizer(Model model) {
		this.model = model;
	}

	/**
	 * It applies rewriting steps to the target program and sets the termination
	 * strategy
	 * 
	 * @return model the optimized model of the program
	 */
	public Model optimizeProgramEvaluation() {
		log.info("BEGIN optimizing the evaluation of the program");

		IdGenerator.reset();
		/* first of all we try to understand in what fragment we are */
		log.info("Fragment detection");
		ReasoningSemantics.getInstance().setSetSemantics();

		ModelWardedDecorator mwd = new ModelWardedDecorator(this.model);
		ModelNearlyLinearDecorator mnld = new ModelNearlyLinearDecorator(this.model);
		ModelHarmlessEGDDecorator mehd = new ModelHarmlessEGDDecorator(this.model);
		ModelFailureDecorator mfd = new ModelFailureDecorator(this.model);
		ModelDistributedDecorator mdd = new ModelDistributedDecorator(this.model);

		Model model = this.model;

		/* multi-head rewriting */
		log.info("Multi-head rewriting");
		model = mwd.multiHeadRewriting();
		mwd = new ModelWardedDecorator(model);
		mnld = new ModelNearlyLinearDecorator(model);

		log.info("Non-monotonic aggregate rewriting");
		model = new NonMonotonicAggregatesRewriter().rewriteSQLAggregates(model);
		mwd = new ModelWardedDecorator(model);
		mnld = new ModelNearlyLinearDecorator(model);

		boolean containsLabelledNulls = !mwd.areExistentialVariablesOnlyCalculated();
		boolean isUniqueNullsRequired = ConfigurationManager.getInstance().getProperty("nullGenerationMode")
				.equals("UNIQUE_NULLS");
		if (containsLabelledNulls && isUniqueNullsRequired) {
			ReasoningSemantics.getInstance().setWardedSemantics();
		}

		/* the list of EGDs in the model */
		List<Rule> egds = new ArrayList<>();
		/* whenever there are EGDs in the model */
		boolean containsEGDs = !model.getEGDs().isEmpty();
		if (containsEGDs) {
			/* we extract the EGDs for Warded, NearlyLinear and PWL check */
			/* EGDs could be harmless, but not warded */
			/* still decidable properties are maintained */
			log.info("Extracting EGDs from the model");
			mehd = new ModelHarmlessEGDDecorator(model);
			egds = mehd.removeEGDs();
			mwd = new ModelWardedDecorator(model);
			mnld = new ModelNearlyLinearDecorator(model);
			ReasoningSemantics.getInstance().setRelaxedWardedSemantics();
		}

		/* a list of failure rules */
		List<Rule> failureRules = new ArrayList<>();
		if (mfd.containsNegationAsFailure()) {
			/* removing failure rules from the model to skip */
			/* harmful join elimination and linearization */
			log.info("Removing failure rules");
			mfd = new ModelFailureDecorator(model);
			failureRules = mfd.removeFailures();
			mwd = new ModelWardedDecorator(model);
			mnld = new ModelNearlyLinearDecorator(model);
		}

		boolean isWarded = mwd.isWarded();
		boolean isNearlyLinear = mnld.isNearlyLinear();
		boolean isEGDHarmless = mehd.checkEGDHarmlessSufficientCondition();
		boolean isNegationAsFailure = mfd.containsNegationAsFailure();

		if (log.isDebugEnabled())
			log.debug(model.toString());

		/* if the program is not nearly-linear, but it is warded */
		if (!isNearlyLinear && isWarded) {
			log.info("The program is not nearly-linear but warded");
			log.info("Let's linearize the heads");
			model = mwd.linearizeHeads();
			log.debug("Linearized heads program:\n");
			if (log.isDebugEnabled())
				log.debug(model.toString());

			mwd = new ModelWardedDecorator(model);
			mnld = new ModelNearlyLinearDecorator(model);

			log.info("Let's choose the Termination Strategy");
			if (mnld.isNearlyLinear()) {
				model = mnld.evaluateCalculatedVariables();
				log.info("Linearization led to nearly-linearity");
				log.info("The program is nearly-linear");
			} else if (mwd.areExistentialVariablesOnlyCalculated()) {
				log.info("Existentially quantified variables are only calculated, let's stick with nearly-linear");
				/* in this case, all the existentially quantified variables */
				/* are calculated, then no explicit marked nulls are produced. */
				log.info("The program is nearly-linear");
			} else {
				/* the program remained warded */
				if (log.isDebugEnabled())
					log.debug(model.toString());

				/* we set the appropriate termination strategy */
				/* if there are no dangerous variables, then the nearly */
				/* linear termination strategy is fine */
				log.info("Choosing termination strategy");
				mwd = new ModelWardedDecorator(model);
				if (mwd.getDangerousVariables().isEmpty()) {
					log.info("There are no dangerous variables, so let's use the nearly-linear termination");
				} else {
					log.info("There are dangerous variables, so let's use the warded termination");
				}
			}
			/* if it is not even warded, we throw an exception */
		} else if (!isWarded) {
			log.warn("The program is not warded. It cannot be processed");
			throw new NotWardedException("The program is not warded. It cannot be processed.");
		} else { /* if the program is nearly linear */
			mnld = new ModelNearlyLinearDecorator(model);
			model = mnld.evaluateCalculatedVariables();
			/* if the program is nearly-linear, no special pre-processing is needed */
			/* we set the appropriate termination strategy */
			log.info("The program is nearly-linear.");
		}

		/* whenever it contains EGDs */
		if (containsEGDs) {
			/* we now add the EGDs after the wardedness check */
			log.info("Adding the EGDs in the model");
			for (Rule egd : egds) {
				model.readRule(egd.toString());
			}
			/* We create a new decorator */
			mehd = new ModelHarmlessEGDDecorator(model);
			isEGDHarmless = mehd.checkEGDHarmlessSufficientCondition();
			/* and check if the harmless sufficient condition holds */
			/* if so we rewrite the EGDs in multiple rules */
			if (isEGDHarmless) {
				/* and rewriting the EGDs */
				model = mehd.egdRulesRewriting();
				/* if there is any EGD and the harmless sufficient condition */
				/* holds we check the termination strategy */
				log.info("The program contains harmless EGDs");
				/* for the EGDs rewritten */
				mwd = new ModelWardedDecorator(model);
			}
			/* the harmless sufficient condition does not hold */
			/* we cannot say anything about EGDs harmlessness */
			/* then we throw an exception */
			else {
				log.warn("The program does not satisfy the EGDs harmless sufficient condition. It cannot be processed");
				throw new HarmlessEGDSufficientConditionException(
						"The program does not satisfy the EGDs harmless sufficient condition. It cannot be processed.");
			}
		}

		isNegationAsFailure = !failureRules.isEmpty();
		/* whenever there is a negation as failure rule */
		if (isNegationAsFailure) {
			/* we now add the failure rules */
			log.info("Adding the Failure rules in the model");
			for (Rule fr : failureRules) {
				model.readRule(fr.toString());
			}
			mfd = new ModelFailureDecorator(model);
			/* we split the negated rules in two rules */
			model = mfd.negatedRulesRewriting();
			/* we create a new decorator */
			mwd = new ModelWardedDecorator(model);
		}

		log.info("Multi-join optimization");
		model = mwd.multiJoinOptimization();
		mwd = new ModelWardedDecorator(model);

		log.info("Right recursion inversion");
		model = mwd.rightRecursionInversion();
		mwd = new ModelWardedDecorator(model);

		log.info("Push selections down and push projections down");
		mwd = new ModelWardedDecorator(model);
		/* put all qbind-related literals to the end of the rule */
		/* this is very important for evaluating joins with qbind-related literals */
		this.excludeRulesWithNonReachablePredicates(model);

		/*
		 * We check the semantics of the monotonic aggregations in different rules with
		 * same heads
		 */
		/* If the Vadalog semantics is respected we perform the rewriting */
		mdd = new ModelDistributedDecorator(model);
		mdd.checkMonotonicAggregationSemantics();
		model = mdd.rewriteMonotonicAggregation();

		log.info("END optimizing the evaluation of the program");
		return this.model;
	}

	/**
	 * This method excludes the rules that are not reached by the output
	 * 
	 * @param model
	 */
	private void excludeRulesWithNonReachablePredicates(Model model) {
		PredicateGraph pg = new PredicateGraph(model);
		List<String> outputPredicateNames = new ArrayList<>();
		Set<String> reachablePredicatesNames = new HashSet<>();
		List<DatalogAnnotation> outputAnnotations = model.getAnnotations().stream()
				.filter(annotation -> annotation instanceof OutputAnnotation).collect(Collectors.toList());
		for (DatalogAnnotation outputAnnotation : outputAnnotations) {
			String outputPredicateName = ((OutputAnnotation) outputAnnotation).getPredicateName();
			outputPredicateNames.add(outputPredicateName);
		}
		Set<Rule> rules = new HashSet<>(model.getRules());
		Set<Rule> reachableRules = new HashSet<Rule>();
		// First step
		for (Rule r : rules) {
			/*
			 * we did multi-head rewriting before calling this method
			 */
			String headPredicateName = r.getSingleHead().getName();
			/*
			 * we iterate over all output predicates and we check if the head of the current
			 * rule is reachable by the output
			 */
			for (String outputPredicateName : outputPredicateNames) {
				if (pg.isReachable(outputPredicateName, headPredicateName, true)) {
					reachableRules.add(r);
					// we collect reachable predicate names for later
					reachablePredicatesNames.add(headPredicateName);
					reachablePredicatesNames.addAll(r.getBodyAtomNamesList());
					break;
				}
			}
		}
		/*
		 * Second Step.
		 * Now we include all EGD and Failure rules which body reaches reachable predicates This is to
		 * include EGDs and Failure rules, that may be not included at this point, since
		 * they do not have an head that is reachable from output.
		 * This is to include EGDs and Failure rules.
		 */
		Set<Rule> egdOrFailureRules = new HashSet<>();
		/* we also collect all the EGD and Failure bodies among all reachable rules
		 * we will use at third step
		 */
		Set<String> egdOrFailureBodyNames = new HashSet<>();
		for(Rule r : rules) {
			if(r.isEGD() || r.isNegationAsFailure()) {
				egdOrFailureRules.add(r);
			}
		}
		for (Rule r : egdOrFailureRules) {
			/*
			 * we want include the rule only if all predicates in its body reaches a
			 * reachable predicate
			 */
			int bodySizeReaches = 0;
			for (Literal l : r.getBody()) {
				String currentLiteralName = l.getAtom().getName();
				for (String reachablePredicateName : reachablePredicatesNames) {
					boolean isReachable = pg.isReachable(currentLiteralName, reachablePredicateName, true);
					if (isReachable) {
						bodySizeReaches += 1;
						break;
					}
				}
			}
			// if all elements of the body reach a reachable predicate
			boolean bodyReachesReachablePredicates = bodySizeReaches > 0 && bodySizeReaches == r.getBody().size();
			if (bodyReachesReachablePredicates) {
				// we add this rule
				reachableRules.add(r);
				List<String> bodyAtomNames = r.getBodyAtomNamesList();
				egdOrFailureBodyNames.addAll(bodyAtomNames);
			}
		}
		/*
		 * Third Step.
		 * At this point there may be excluded some rules derived from EGDs or Failure rules
		 * which body read directly from an input fact, so a predicate that is not present in
		 * the predicate graph. This may happens when the rules that read from the input are
		 * the only Failure-derived or EGD-derived rule and for this reason are not
		 * in the reachable predicates of the first step
		 */
		/*
		 * We want to find the rules having head that reaches a egdBody or a failure body
		 */
		for(Rule r : rules) {
			String headName = r.getSingleHead().getName();
			for(String egdOrFailureBodyName : egdOrFailureBodyNames) {
				boolean headReachesEgdOrFailureRule = pg.isReachable(egdOrFailureBodyName, headName, true);
				if(headReachesEgdOrFailureRule) {
					reachableRules.add(r);
				}

			}
		}

		model.getRules().clear();
		/*
		 * if we do not have reachable rules we restore the original ones which are the
		 * ones written by the user
		 */
		if (reachableRules.isEmpty()) {
			reachableRules = rules;
		}
		// now we update the model with the new rules
		model.getRules().addAll(reachableRules);
	}

}

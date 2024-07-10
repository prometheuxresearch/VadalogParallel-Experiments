package uk.co.prometheux.prometheuxreasoner.warded;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.prometheux.prometheuxreasoner.common.IdGenerator;
import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Variable;
import uk.co.prometheux.prometheuxreasoner.nearlyLinear.ModelNearlyLinearDecorator;
import uk.co.prometheux.prometheuxreasoner.operators.AtomOperatorDecorator;
import uk.co.prometheux.prometheuxreasoner.operators.AtomToAtomUnifier;
import uk.co.prometheux.prometheuxreasoner.operators.Composition;
import uk.co.prometheux.prometheuxreasoner.operators.FoldingException;
import uk.co.prometheux.prometheuxreasoner.operators.RuleOperatorDecorator;
import uk.co.prometheux.prometheuxreasoner.operators.UnificationException;

/**
 * Class used to simplify a warded program with harmful rules into an equivalent
 * program (i.e. without loss of meaning or generality) without harmful rules.
 * The implementation is based on the Harmful Join Elimination Algorithm.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
class HJEWardedSimplifier {

	private Model model;
	private ModelWardedDecorator mwd;
	private Logger log = LoggerFactory.getLogger(HJEWardedSimplifier.class);

	private Map<Integer, Rule> backComposAncestorMap;

	HJEWardedSimplifier(Model m) {
		this.model = m;
		this.mwd = new ModelWardedDecorator(this.model);
		this.backComposAncestorMap = new HashMap<>();
	}

	/**
	 * Here we simplify the harmful-harmful (HH) joins in the warded rules.
	 * 
	 * The presence of these joins prevents the application of the termination
	 * conditions applied for the nearly-linear case, because the linear structures
	 * cannot be considered independently of one another. Specifically, the
	 * termination within one structure is no longer independent of the marked-null
	 * values (z1, ..., zn) as they could match with the same marked-null values in
	 * other structures: in the same or in an ancestor structure (that propagated
	 * the null with a dangerous rule). Therefore, we cannot apply a termination
	 * criterion based on ground values and pattern for nulls. After the
	 * simplification we apply here, the nearly-linear termination algorithm can be
	 * applied.
	 * 
	 * let P be our warded program
	 * 
	 * for each rule R in P with any HH join, do the following:
	 * 
	 * - for each HH join in R - let a(*) be an atom of R involved in the HH join
	 * 
	 * DETACH FROM DANGER (GROUNDING) - add the rules a'(*) :- a(*), dom(*) to P -
	 * add the rules a(*) :- a'(*) to P - rewrite R by replacing a(*) with a'(*) and
	 * add it to P - whenever the head of a rule R' that is not a cause of
	 * affectedness for R unifies with a(*) - add the rule R' to P, where the head
	 * is replaced with a'(*)
	 * 
	 * RECOGNITION - for each harmful rule R' in P such that there is an isomorphism
	 * between the body of R and the body of R' - remove R' and add a rule built as
	 * follows: R'_head :- R_head
	 * 
	 * BACK-COMPOSITION - copy R into a bucket B - for each rule R in B that
	 * contains an HH join or an affected variable constrained by a Skolem atom: -
	 * remove R from B - for each cause rule Rc (the rule that causes the
	 * affectedness), do the following: - rewrite R by composing Rc * R and
	 * introducing Skolem predicates to take into account the Skolem dependencies
	 * between the variables. - (a Skolem atom has the form f_i_y(y1, ..., yn) and
	 * expresses that y1, ..., yn are operands in a Skolem function f_i to calculate
	 * marked-null values for x when bound to existentially quantified variable y.)
	 * Notice that only y appears in the composition. - check for loops by
	 * attempting FOLDING with the ancestor of the currently generated rule, in
	 * order to avoid non-termination. - add R' to B - (notice that in this
	 * rewriting, the "detach from danger" phase is not necessary).
	 * 
	 * SKOLEM SIMPLIFICATION + LINEARIZATION - for each rule R in B: - if there is a
	 * Skolem atom that conditions an unaffected variable in the body: DROP THE RULE
	 * - if there is a single Skolem atom that conditions a variable not in the
	 * body: DROP THE RULE - if there are multiple Skolem atoms that condition the
	 * same variable not in the body: - if they unify: drop them and PROPAGATE the
	 * unification to the whole rule - if they do not unify: DROP THE RULE - if
	 * there is a recursive Skolem atom, DROP the rule
	 * 
	 * @return the warded program without harmful-harmful joins
	 */
	Model harmfulJoinElimination() {

		log.info("BEGIN simplifying harmful-harmful joins");

		/* we clone the program */
		Model m = new Model(this.model);

		/* we get all the rules and the causes for HH affectedness */
		Map<Rule, Set<Rule>> rulesAndCauses = this.mwd.getHHJoinAffectednessCausesByRule(m);
		/* if there are not causes for HH affectedness, we end the simplification */
		if (rulesAndCauses.isEmpty())
			return m;

		/* we create a temporary new program, bucket, containing all the rules */
		/* that have to be back-composed along with the respective causes. */
		Model bucket = new Model();
		bucket.getRules().addAll(rulesAndCauses.keySet());
		rulesAndCauses.values().forEach(x -> x.forEach(y -> {
			if (!bucket.getRules().contains(y)) // required if same causes for multiple HH joins
				bucket.getRules().add(y);
		}));

		if (log.isDebugEnabled())
			log.debug("Causes of HH joins: " + rulesAndCauses);

		/* we find RECOGNITION cases for original harmful joins */
		List<Rule> originalHarmfulJoins = new ArrayList<>(rulesAndCauses.keySet());
		Map<Rule, Set<Rule>> originalRecognitions = this.checkOriginalCasesRecognition(originalHarmfulJoins);

		/* while there are HH rules, we DETACH FROM DANGER (GROUNDING PHASE 1) */
		/* Basically, we make the affected-harmless cases explicit */
		/*
		 * So as to handle the harmful-harmful ones explicitly with backward
		 * composition.
		 */
		ModelNearlyLinearDecorator mnld = new ModelNearlyLinearDecorator(m);
		/*
		 * map with correspondences <originalAtom, hatom>, useful after the
		 * back-composition
		 */
		Map<String, String> correspHAtoms = new HashMap<>();
		while (rulesAndCauses.size() > 0) {
			/* we detach from danger each cause */
			Set<Rule> hhRules = rulesAndCauses.keySet();
			/* for each rule with at least a HH join */
			for (Rule r : hhRules) {
				Set<Rule> causes = rulesAndCauses.get(r);
				/* the detached rule is put back */
				/* so that if some danger remains, it is handled at the next loop. */
				if (causes != null && !causes.isEmpty()) {
					Iterator<Rule> causesIt = causes.iterator();
					if (causesIt.hasNext()) {
						Rule cause = causesIt.next();
						mnld.detachFromDanger(m, causes, r, cause, this.mwd.getCompositionLiteralPosition(r, cause),
								correspHAtoms);
						/* we detach just the first cause... optimistically */
						/* if it does not suffice, we will find other causes */
						/* at the next loops. */
						/* This should give us the minimum safe program */
					}
				}
			}
			rulesAndCauses = this.mwd.getHHJoinAffectednessCausesByRule(m);
		}

		/*
		 * we handle RECOGNITION for original harmful joins by adding a rule between the
		 * heads of each couple of rules in originalRecognition and removing the rules
		 * from the bucket
		 */
		Set<Rule> recRules = originalRecognitions.keySet();
		for (Rule r : recRules) {
			boolean folded = false;
			Set<Rule> recWith = originalRecognitions.get(r);
			for (Rule t : recWith) {
				/* if a rule in recWith appears in the keySet, we do not add the new rule */
				/* between heads in order to avoid useless rules in the final program */
				if (!recRules.contains(t)) {
					/* we build the new rule */
					try {
						RuleOperatorDecorator rod = new RuleOperatorDecorator(t);
						Rule recNew = rod.fold().into(r);
						if (log.isDebugEnabled())
							log.debug("Rules correctly folded into: " + recNew);
						folded = true;
						m.getRules().add(recNew);
					} catch (FoldingException fe) {
						if (log.isDebugEnabled())
							log.debug("Rules could not be folded");
					}
				}
			}
			/* we remove the original rule from the bucket and the model */
			if (folded) {
				bucket.getRules().remove(r);
				m.getRules().remove(r);
				this.model.getRules().remove(r);
			}
		}

		/* we handle BACK-COMPOSITION */
		log.debug("BEGIN back-composition");

		Model backBucket = new Model(this.mwd.getModel()); // bucket for back-composition
		Model backBucketOnlyNew = new Model(); // support bucket with only new final rules added during back-composition

		ModelNearlyLinearDecorator bmnld = new ModelNearlyLinearDecorator(backBucket);
		ModelWardedDecorator bmwd = new ModelWardedDecorator(backBucket);
		Set<Position> affectedPositionsBucket = bmwd.getAffectedPositions();

		/* we get the causes of affectedness */
		rulesAndCauses = bmnld.getAffectednessCausesByRule(backBucket, affectedPositionsBucket, true);

		/* while there are affected rules and variables */
		while (rulesAndCauses.size() > 0) {

			/* for each affected rule, we compose back along all the causes */
			for (Map.Entry<Rule, Set<Rule>> ruleCauses : rulesAndCauses.entrySet()) {
				Rule rWarded = ruleCauses.getKey(); // the warded rule to simplify

				/* for all the causes */
				for (Rule cause : ruleCauses.getValue()) {
					/* we compose all of them */
					RuleOperatorDecorator rod = new RuleOperatorDecorator(cause);

					/*
					 * we try to compose, handling the existential quantification in the
					 * composition.
					 */
					if (log.isDebugEnabled())
						log.debug("Trying to compose: " + cause + " and " + rWarded);
					IdGenerator.resetVarSymbols();

					/* we compose exactly with the first atom that unifies and */
					/* is involved either in a join or in a Skolem condition. */
					/* We are sure that this method returns something significant, because */
					/* ruleCauses is built with the same logic as getCompositionLiteralPosition() */
					Composition c = rod.compose().atPosition(bmwd.getCompositionLiteralPosition(rWarded, cause));
					Rule rDatalogSimpl = c.with(rWarded, true);

					/* if now the composition is successful, here we should have */
					/* a simplified rule, maybe still affected. */
					if (log.isDebugEnabled())
						log.debug("Rules correctly composed into: " + rDatalogSimpl);

					/*
					 * if the simplified rule presents in the body two or more atoms with the same
					 * name
					 */
					/*
					 * and the same arguments, we leave one of them to reduce future number of
					 * foldings
					 */
					List<Literal> body = new ArrayList<>(new HashSet<Literal>(rDatalogSimpl.getBody()));
					rDatalogSimpl = new Rule(new ArrayList<>(rDatalogSimpl.getHead()), body,
							rDatalogSimpl.getConditions());

					/*
					 * we handle FOLDING inside Back-Composition in order to avoid non-termination
					 */
					/* we check whether the composed rule should be added to the program */
					boolean doNotAdd = this.checkForAdditionInModel(rDatalogSimpl, backBucket);
					if (!doNotAdd) {
						/*
						 * we attempt folding between the current rule rDatalogSimpl and its ancestors
						 * in ancestorsMap
						 */
						/* from the direct one (rWarded) to the last one (the original harmful rule) */
						rDatalogSimpl = this.checkFoldingWithAncestors(rDatalogSimpl, rWarded, backComposAncestorMap);

						/* we update ancestorMap with the new rule and rWarded as direct ancestor */
						this.setAncestor(rDatalogSimpl, rWarded);

						/* we check whether this new rule should be added to the program */
						doNotAdd = this.checkForAdditionInModel(rDatalogSimpl, backBucket);
					}

					/* we add this new rule to the program, if possible */
					if (!doNotAdd) {
						backBucket.getRules().add(rDatalogSimpl);
						backBucketOnlyNew.getRules().add(rDatalogSimpl);
					}
				}

				/* there is not any remaining cause to simplify rWarded with */
				backBucket.getRules().remove(rWarded);
				backBucketOnlyNew.getRules().remove(rWarded);
			}

			/* we update the causes of affectedness */
			rulesAndCauses = bmnld.getAffectednessCausesByRule(backBucket, affectedPositionsBucket, true);

			if (log.isDebugEnabled())
				log.debug("Bucket:\n" + backBucket);
		}

		if (log.isDebugEnabled())
			log.debug("END back-composition");

		/*
		 * (GROUNDING PHASE 2) we convert all instances of atoms (in the rules added by
		 * back-composition)
		 */
		/*
		 * which were previously subjected to grounding into the corresponding hatom_i,
		 */
		/* in order to avoid null values in output of final program */
		backBucket = this.updateModelBackComposedWithGrounding(backBucket, backBucketOnlyNew, correspHAtoms);

		if (log.isDebugEnabled())
			log.debug("Bucket:\n" + backBucket);
		log.info("END simplifying harmful-harmful joins");

		/*
		 * we handle SKOLEM SIMPLIFICATION: here we should try to unify the various
		 * Skolem-predicates
		 */
		backBucket.deduplicateRules();
		Model newBucket = new Model(backBucket);

		ModelWardedDecorator mwb = new ModelWardedDecorator(newBucket);
		Map<Rule, Set<Variable>> harmlessByRule = mwb.getHarmlessVariablesByRule();

		log.debug("BEGIN resolving Skolem-atoms.");
		for (Rule r : backBucket.getRules()) {

			/* whether the rule has been dropped */
			boolean dropped = false;

			/* the unifiers to apply to the rule after all the unifications */
			List<AtomToAtomUnifier> mappings = new ArrayList<>();

			Map<String, Set<Atom>> skolemAtomsByName = r.getSkolemAtomsByName();
			Set<String> atomNames = skolemAtomsByName.keySet();

			/* for all the distinct atom names */
			for (String atomName : atomNames) {
				Atom unifiedByName = null;

				/* exit if the rule has been dropped */
				if (dropped)
					break;

				/* we drop all the rules where there is a Skolem-predicate whose */
				/* calculated variable appears in the body in a unaffected position. */
				/* In this case, the rule would never */
				/* fire as at this point we do not have affected variables anymore. */
				for (Atom a : skolemAtomsByName.get(atomName)) {
					if (harmlessByRule.get(r).contains(a.getCalculatedVariable())) {
						newBucket.getRules().remove(r);
						if (log.isDebugEnabled())
							log.debug("Dropping " + r + " as it never fires: " + a.getCalculatedVariable()
									+ " binds to a harmless variable.");
						dropped = true;
					} else {
						/* here, if we haven't dropped the rule, we try to unify the Skolem atoms */

						/* if it is the first, we just keep it */
						/* for the next unifications. */
						if (unifiedByName == null) {
							unifiedByName = a;

							/* else we keep unifying */
						} else {
							AtomOperatorDecorator aod = new AtomOperatorDecorator(unifiedByName);

							/* if it unifies */
							try {
								if (log.isDebugEnabled())
									log.debug("Trying to alter " + r + " by unifying " + a + " and " + unifiedByName);
								AtomToAtomUnifier aau;
								aau = aod.unify().with(a);
								unifiedByName = aau.applyAsAmapping(a);
								/* we store the mapping to be applied to the whole rule */
								mappings.add(aau);

								/* else, if it does not unify */
							} catch (UnificationException e) {
								/* we remove the rule */
								if (log.isDebugEnabled())
									log.debug("Dropping " + r + " as it never fires: " + a + " and " + unifiedByName
											+ " do not unify.");
								newBucket.getRules().remove(r);
								dropped = true;
							}
						}
					}
				}
			}

			/*
			 * if the rule has not been dropped, unify according to the unification of the
			 */
			/* Skolem atoms. */
			if (!dropped) {
				Rule r1 = new Rule(r);
				for (AtomToAtomUnifier aau : mappings) {
					r1 = aau.applyAsMappingWithConditions(r1);
				}
				/* eliminate duplicate atoms and conditions from the rule */
				/* as they may derive from the unification */
				r1.deduplicateBody();
				r1.deduplicateConditions();

				/* at this point we have unified all the possible Skolem functors. */
				/*
				 * The ones we have now are such that the constrained variable does not appear
				 * in the
				 */
				/*
				 * body. In general we can drop them, unless two of them are related to the same
				 * variable
				 */
				/* (and do not unify, in which case we drop the rule) */

				/* drop Skolem atoms, which at this point */
				/* have been unified, or the rule dropped. */
				/* It can never happen that they have not been unified and */
				/* still regard affected variables. */
				for (Atom skolemAtom1 : r1.getSkolemAtoms()) {
					/* if we meet a recursive Skolem atom, we drop the rule */
					if (!dropped && skolemAtom1.getVariablesAsSet().contains(skolemAtom1.getCalculatedVariable())) {
						if (log.isDebugEnabled())
							log.debug("Dropping rule " + r + " as it never fires: " + skolemAtom1 + " is recursive.");
						dropped = true;
						/* if the Skolem atom is not recursive */
					} else if (!dropped) {
						for (Atom skolemAtom2 : r1.getSkolemAtoms()) {
							if (!skolemAtom1.equals(skolemAtom2) && skolemAtom1.getCalculatedVariable()
									.equals(skolemAtom2.getCalculatedVariable())) {
								if (log.isDebugEnabled())
									log.debug("Dropping rule " + r + " as it never fires: "
											+ skolemAtom1.getCalculatedVariable()
											+ " is constrained by two non-unifying Skolem atoms.");
								dropped = true;
							}
						}
					}
				}

				newBucket.getRules().remove(r);

				/* here we remove the remained Skolem atoms */
				if (!dropped) {
					if (r1.getSkolemAtoms().size() > 0) {
						r1.dropSkolemAtoms();
						if (log.isDebugEnabled())
							log.debug("Dropping Skolem atoms from " + r1
									+ " as the constrained variable does not appear in the body.");
					}
					/* and add the fixed rule */
					if (log.isDebugEnabled())
						log.debug("Adding unified rule " + r1);
					newBucket.getRules().add(r1);
				}
			}
		}
		log.debug("END resolving Skolem-atoms.");
		log.debug("Deduplicating rules.");

		/* here we merge m and the bucket into m */
		m.getRules().addAll(newBucket.getRules());
		m.deduplicateRules();

		return m;
	}

	/**
	 * It finds the cases of recognition between harmful joins in the original
	 * program and it creates a map <harmful rule, <harmful rules it has recognition
	 * with>>. There exists recognition between two distinct rules if their bodies
	 * are isomorphic.
	 * 
	 * @param originalHarmfulJoins
	 * 
	 * @return the map built as described above
	 */
	private Map<Rule, Set<Rule>> checkOriginalCasesRecognition(List<Rule> originalHarmfulJoins) {
		Map<Rule, Set<Rule>> originalRecognitions = new HashMap<>();
		/* support Lists to check recognition */
		List<Rule> suppRecog = new ArrayList<>(originalHarmfulJoins);
		List<Rule> tmpRecog;

		/*
		 * for each rule in originalHarmfulJoins, we check if it has recognition with a
		 * distinct rule
		 */
		for (Rule r : originalHarmfulJoins) {
			/* we initialize tmp */
			tmpRecog = new ArrayList<>(suppRecog);
			for (Rule t : tmpRecog) {
				/* if the rules are distinct and their bodies are isomorphic */
				if (!r.equals(t) && this.areBodyIsomorphic(r, t)) {
					/* we add rule r to keySet */
					Set<Rule> rules = originalRecognitions.get(r);
					if (rules == null)
						rules = new HashSet<Rule>();
					/* we add t to set associated with r in the map */
					rules.add(t);
					originalRecognitions.put(r, rules);
					/* we remove r from supp */
					suppRecog.remove(r);
				}
			}
		}
		return originalRecognitions;
	}

	/**
	 * It checks whether the bodies of the two rules are isomorphic.
	 * 
	 * @param t
	 * @param r
	 * 
	 * @return if the bodies of the rules are isomorphic
	 */
	private boolean areBodyIsomorphic(Rule r1, Rule r2) {
		RuleOperatorDecorator rod1 = new RuleOperatorDecorator(r1);
		RuleOperatorDecorator rod2 = new RuleOperatorDecorator(r2);

		return rod1.isHomomorphic().toBody(r2) && rod2.isHomomorphic().toBody(r1);
	}

	/**
	 * It checks whether two rules are isomorphic.
	 * 
	 * @param t
	 * @param r
	 * 
	 * @return if the bodies of the rules are isomorphic
	 */
	private boolean areIsomorphic(Rule r1, Rule r2) {
		RuleOperatorDecorator rod1 = new RuleOperatorDecorator(r1);
		RuleOperatorDecorator rod2 = new RuleOperatorDecorator(r2);

		return rod1.isHomomorphic().to(r2) && rod2.isHomomorphic().to(r1);
	}

	/**
	 * It attempts folding between the current rule rDatalogSimpl and its ancestors
	 * in ancestorsMap from the direct one (the father) to the last one.
	 * 
	 * @param ruleToFold
	 * @param rWarded
	 * @param ancestorsMap
	 * 
	 * @return the result of folding or the original rule, if every folding failed
	 */
	private Rule checkFoldingWithAncestors(Rule ruleToFold, Rule rWarded, Map<Integer, Rule> ancestorsMap) {
		Rule closestAncestor = rWarded; // the direct ancestor
		boolean folded = false; // if the folding succeeded

		while (closestAncestor != null && !folded) {
			/* support copies of current rules considered for folding */
			Rule ruleToFoldTemp = new Rule(ruleToFold);
			Rule ruleToFoldSupp = new Rule(ruleToFold);
			Rule closestAncestorTemp = new Rule(closestAncestor);

			/*
			 * we list all possible subsets of Literals in rDatalogSupp to attempt folding
			 * with
			 */
			List<Set<Literal>> foldingLiterals = this.getLiteralsForFolding(ruleToFoldSupp, closestAncestorTemp);
			if (log.isDebugEnabled())
				log.debug("Literals for folding: " + foldingLiterals);

			/* we try to fold current ancestor into rDatalogSimpl */
			Iterator<Set<Literal>> itS = foldingLiterals.iterator();
			if (log.isDebugEnabled())
				log.debug("Trying to fold: " + closestAncestor + " into " + ruleToFold);
			/* we attempt folding for each sublist of Literals previously defined */
			while (itS.hasNext() && !folded) {
				try {
					RuleOperatorDecorator rod = new RuleOperatorDecorator(closestAncestor);
					ruleToFold = rod.fold().intoWithLiterals(ruleToFoldTemp, itS.next());
					if (log.isDebugEnabled())
						log.debug("Rules correctly folded into: " + ruleToFold);
					folded = true;
				} catch (FoldingException fe) {
					if (log.isDebugEnabled())
						log.debug("Rules could not be folded");
				}
			}
			/* we update the closest ancestor with the father of the previous one */
			closestAncestor = this.getAncestor(closestAncestor);
		}
		return ruleToFold;
	}

	/**
	 * It gets all subsets of the literals in the body of sortedRDatalogSimpl such
	 * that all the literals appear in rAncestor, in order to filter the possible
	 * attempts for Folding.
	 * 
	 * @param sortedRDatalogSimpl
	 * @param rAncestor
	 * 
	 * @return list of subsets of literals in sortedRDatalogSimpl
	 */
	private List<Set<Literal>> getLiteralsForFolding(Rule sortedRDatalogSimpl, Rule rAncestor) {
		List<Set<Literal>> foldingLiterals = new ArrayList<>(); // output
		/* each set will have dimension setLength */
		int setLength = rAncestor.getBody().size();

		/* we get the names of all the body atoms in rWardedTemp */
		Set<String> rWardedTempBodyNames = new HashSet<>();
		for (Atom a : rAncestor.getBodyAtoms()) {
			rWardedTempBodyNames.add(a.getSimpleName());
		}

		/*
		 * we find all possible sublists for the body of sortedRDatalogSimpl of
		 * dimension setLength
		 */
		/* we remove all subsets which do not have only rWardedTemp atoms */
		foldingLiterals = getSubsets(sortedRDatalogSimpl.getBody(), setLength, rWardedTempBodyNames);

		/* we now remove all those subsets which do not have the */
		return foldingLiterals;
	}

	/**
	 * It gets each subset of length k from a list such that it contains only
	 * elements of rAncestorBodyNames.
	 * 
	 * @param superSet
	 * @param k
	 * @param idx
	 * @param current
	 * @param solution
	 * @param rAncestorBodyNames
	 */
	private static void getSubsets(List<Literal> superSet, int k, int idx, Set<Literal> current,
			List<Set<Literal>> solution, Set<String> rAncestorBodyNames) {
		// successful stop clause
		if (current.size() == k) {
			solution.add(new HashSet<>(current));
			return;
		}
		// unsuccessful stop clause
		if (idx == superSet.size()) {
			return;
		}
		Literal x = superSet.get(idx);
		/* we remove all subsets which do not have only rWardedTemp atoms */
		if (rAncestorBodyNames.contains(x.getAtom().getSimpleName()))
			current.add(x);
		// "guess" x is in the subset
		getSubsets(superSet, k, idx + 1, current, solution, rAncestorBodyNames);
		current.remove(x);
		// "guess" x is not in the subset
		getSubsets(superSet, k, idx + 1, current, solution, rAncestorBodyNames);
	}

	private static List<Set<Literal>> getSubsets(List<Literal> superSet, int k, Set<String> rAncestorBodyNames) {
		List<Set<Literal>> res = new ArrayList<>();
		getSubsets(superSet, k, 0, new HashSet<Literal>(), res, rAncestorBodyNames);
		return res;
	}

	/**
	 * We check whether a rule rDatalogSimpl should be added to the program.
	 * 
	 * @param rDatalogSimpl
	 * 
	 * @return the boolean result of the check
	 */
	private boolean checkForAdditionInModel(Rule rDatalogSimpl, Model backBucket) {
		boolean doNotAdd = false; // output

		/* we check whether this new rule should be added to the program */
		/* we do not add unnecessary rules nor rules which would never be called */
		List<Atom> rDatalogSimplHead = rDatalogSimpl.getHead();
		Set<Atom> rDatalogSimplBody = rDatalogSimpl.getBodyAtoms();
		/* first, we check if an atom in the head is also */
		/* present in the body with the same arguments */
		Iterator<Atom> itH = rDatalogSimplHead.iterator();
		String headNamesRDatalog = null;
		String bodyNamesRDatalog = null;
		while (itH.hasNext() && !doNotAdd) {
			Atom a = itH.next();
			if (rDatalogSimplBody.contains(a))
				doNotAdd = true;
			/* we update the string with the names of the head atoms */
			/* they will be used in the following check */
			headNamesRDatalog += a.getName();
		}
		/* then, we check if the head and the body have the same atoms */
		if (!doNotAdd) {
			Iterator<Atom> itB = rDatalogSimplBody.iterator();
			while (itB.hasNext())
				bodyNamesRDatalog += itB.next().getName();
			if (headNamesRDatalog != null && bodyNamesRDatalog != null && headNamesRDatalog.equals(bodyNamesRDatalog))
				doNotAdd = true;
		}
		/*
		 * finally, we check if this rule is equal or isomorphic to a rule already
		 * present in the bucket
		 */
		List<Rule> bucketRules = backBucket.getRules();
		Iterator<Rule> itR = bucketRules.iterator();
		while (itR.hasNext() && !doNotAdd) {
			Rule curr = new Rule(itR.next());
			Rule rDatalogTemp = new Rule(rDatalogSimpl);
			// curr.sortBody(); //<-----------------------------
			// rDatalogTemp.sortBody(); //<-----------------------------
			if (rDatalogTemp.equals(curr) || this.areIsomorphic(rDatalogTemp, curr)) {
				doNotAdd = true;
			}
		}
		return doNotAdd;
	}

	/**
	 * It converts all instances of atoms in backBucket (in the rules also present
	 * in backBucketOnlyNew) into the corresponding hatom_i defined in
	 * correspHAtoms, in order to avoid null values in output of final program.
	 * 
	 * @param backBucket
	 * @param backBucketOnlyNew
	 * @param correspHAtoms
	 * 
	 * @return the updated model
	 */
	private Model updateModelBackComposedWithGrounding(Model backBucket, Model backBucketOnlyNew,
			Map<String, String> correspHAtoms) {
		List<Rule> rules = backBucket.getRules(); // backBucket rules
		List<Rule> rulesTemp = new ArrayList<>(rules); // support list
		List<Rule> rulesSupp = backBucketOnlyNew.getRules(); // only new rules (from back-composition)

		/* for each rule in backBucket, we check whether it is a new rule */
		/* if that is the case, we check if it should be updated with hatoms */
		for (Rule r : rulesTemp) {
			if (rulesSupp.contains(r)) {
				rules.remove(r);
				r = updateSingleRuleWithGrounding(r, correspHAtoms);
				rules.add(r);
			}
		}
		return backBucket;
	}

	private Rule updateSingleRuleWithGrounding(Rule r, Map<String, String> correspHAtoms) {
		List<Literal> bodyLiterals = r.getBody();
		/* we check the instances of body atoms with correspondence in correspHAtoms */
		for (Literal bl : bodyLiterals) {
			String hatom = correspHAtoms.get(bl.getAtom().getSimpleName());
			/* we update the name */
			if (hatom != null)
				bl.getAtom().setName(hatom);
		}
		/* we check the instances of head atoms with correspondence in correspHAtoms */
		List<Atom> headAtoms = r.getHead();
		for (Atom hl : headAtoms) {
			String hatom = correspHAtoms.get(hl.getSimpleName());
			/* we update the name */
			if (hatom != null)
				hl.setName(hatom);
		}
		return r;
	}

	/**
	 * Sets the ancestor for a rule in the ancestor map
	 * 
	 * @param rule
	 * @param ancestor
	 */
	private void setAncestor(Rule rule, Rule ancestor) {
		this.backComposAncestorMap.put(System.identityHashCode(rule), ancestor);
	}

	/**
	 * Returns the ancestor of the rule from the unfolding tree
	 * 
	 * @param rule
	 * @return ancestor
	 */
	private Rule getAncestor(Rule rule) {
		return this.backComposAncestorMap.get(System.identityHashCode(rule));
	}

}

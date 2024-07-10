package uk.co.prometheux.prometheuxreasoner.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import uk.co.prometheux.prometheuxreasoner.model.Rule;

/**
 * This Class generates identifiers
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class IdGenerator {

	/* for variable renaming */
	private static Map<Rule, Map<String, String>> ruleRenamer = new HashMap<>();

	/* for new variable symbols in a rule */
	private static Map<Rule, Integer> ruleCounter = new HashMap<>();

	/* counters for the whole program */
	private static int modelCounter = 0;
	private static int groundCounter = 0;
	private static int eurekaCounter = 0;
	private static int skolemCounter = 0;

	/* for function symbols for a rule */
	private static Map<Rule, Integer> ruleFunction = new HashMap<>();
	private static AtomicInteger nullCounter = new AtomicInteger();
	private static int ruleId = 0;

	/**
	 * It returns a new atom symbol
	 * 
	 * @param vAtomName the name for the virtual atom
	 * @return the new atom symbol
	 */
	public static String getNewAtomSymbol(String vAtomName) {
//		int mc;
		String atom = null;
		if (vAtomName.equals("vatom_")) {
			atom = "vatom_" + (++modelCounter);
		} else if (vAtomName.equals("hatom_")) {
			atom = "hatom_" + (++groundCounter);
		} else if (vAtomName.equals("new_")) {
			atom = "new_" + (++eurekaCounter);
		} else if (vAtomName.equals("skatom_")) {
			atom = "skatom_" + (++skolemCounter);
		}
		return atom;
	}

	/**
	 * It returns an integer that identifies a rule in a run.
	 * 
	 * @param r the rule
	 * @return the integer
	 */
	public static int getRuleFunction(Rule r) {
		Integer fcn = ruleFunction.get(r);
		if (fcn == null) {
			ruleFunction.put(r, ruleId);
			fcn = ruleId;
			++ruleId;
		}

		return fcn;
	}

	/**
	 * It returns a new variable symbol, unique within a rule
	 * 
	 * @param r the rule
	 * @return the new variable symbol
	 */
	public static String getNewVariableSymbol(Rule r) {
		return getNewVariableSymbol(r, new HashSet<String>());
	}

	/**
	 * It returns a new variable symbol, unique within a rule
	 * 
	 * @param r            the context rule
	 * @param namesToAvoid a set of names to be avoided
	 * @return the new variable symbol
	 */
	public static String getNewVariableSymbol(Rule r, Set<String> namesToAvoid) {
		Integer cnt = ruleCounter.get(r);
		if (cnt == null)
			cnt = 0;

		/* we need to generate a symbol and verify */
		/* whether it is not already present in the rule, which could */
		/* happen in case of multiple composition. */

		String var = "Var_" + (++cnt);

		Set<String> allRNames = r.getBodyVariables().stream().map(x -> x.getName()).collect(Collectors.toSet());
		allRNames.addAll(r.getSingleHead().getVariablesAsSet().stream().map(x -> x.getName()).collect(Collectors.toSet()));
		allRNames.addAll(r.getLiterals().stream().filter(x -> x.getAtom().isSkolemAtom())
				.map(x -> x.getAtom().getCalculatedVariable().getName()).collect(Collectors.toSet()));

		allRNames.addAll(namesToAvoid);

		/* we update the name to avoid the ones that are already */
		/* present in the body. */
		while (allRNames.contains(var))
			var = "Var_" + (++cnt);

		ruleCounter.put(r, cnt);

		return var;
	}

	/**
	 * It returns a new variable symbol, unique within a rule and deterministically
	 * associated to an input. It is useful to apply a deterministic renaming to the
	 * variables of a rule.
	 * 
	 * @param r        the rule
	 * @param inputVar the inputVariable
	 * @return the symbol
	 */
	public static String getNewDeterministicVariableSymbol(Rule r, String inputVar, Set<String> namesToAvoid) {
		Map<String, String> varToVar = ruleRenamer.get(r);
		if (varToVar == null) {
			varToVar = new HashMap<>();
			ruleRenamer.put(r, varToVar);
		}
		String newString = varToVar.get(inputVar);
		if (newString == null) {
			newString = getNewVariableSymbol(r, namesToAvoid);
			varToVar.put(inputVar, newString);
		}
		return newString;
	}

	/**
	 * It resets the counter and cleans the cache
	 */
	public static void reset() {
		ruleCounter = (new HashMap<>());
		ruleRenamer = (new HashMap<>());
		ruleFunction = (new HashMap<>());
		modelCounter = (0);
		groundCounter = (0);
		eurekaCounter = (0);
		skolemCounter = (0);
		nullCounter.set(-1);
		ruleId = (0);
	}

	/**
	 * It resets only the counters for the variables in the rules. This is used by
	 * the composition.
	 */
	public static void resetVarSymbols() {
		ruleRenamer = (new HashMap<>());
		ruleCounter = (new HashMap<>());
	}
}
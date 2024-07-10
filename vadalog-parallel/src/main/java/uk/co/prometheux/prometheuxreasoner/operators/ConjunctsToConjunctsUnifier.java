package uk.co.prometheux.prometheuxreasoner.operators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * It represents the unifier between two conjuncts sets of atoms.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
class ConjunctsToConjunctsUnifier {

	private Set<AtomToAtomUnifier> unifiers = new HashSet<>();
	private AtomToAtomUnifier generalUnifier = new AtomToAtomUnifier(null, null);

	/**
	 * It extends the set of unifiers if the new one does not contradict the
	 * previous ones.
	 * 
	 * @param unifier the new unifier
	 * @return whether the extension has been done or not
	 */
	boolean extendWithUnifier(AtomToAtomUnifier unifier) {
		/* if it is the first, it can be added */
		if (unifiers.isEmpty())
			unifiers.add(unifier);
		/* otherwise, it can be added only if it does not */
		/* violate any existing correspondence */
		else {
			for (Variable var : unifier.getVariables()) {
				for (AtomToAtomUnifier aau : this.unifiers) {
					if ((aau.getCorrespondenceByVariable(var) != null
							&& unifier.getCorrespondenceByVariable(var) != null)
							&& (!aau.getCorrespondenceByVariable(var).equals(unifier.getCorrespondenceByVariable(var))))
						return false;
				}
			}
			this.unifiers.add(unifier);
		}

		return true;
	}

	/**
	 * It checks whether the set of unifiers for conditions does not contradict the
	 * ones defined for the atoms.
	 * 
	 * @param unifier the new unifier
	 * @return whether the extension is correct or it contradicts
	 */
	boolean checkWithUnifier(ConditionToConditionUnifier unifier) {
		/* it is correct only if it does not */
		/* violate any existing correspondence */
		for (Variable var : unifier.getVariables()) {
			for (AtomToAtomUnifier aau : this.unifiers) {
				if ((aau.getCorrespondenceByVariable(var) != null && unifier.getCorrespondenceByVariable(var) != null)
						&& (!aau.getCorrespondenceByVariable(var).equals(unifier.getCorrespondenceByVariable(var))))
					return false;
			}
		}
		return true;
	}

	/**
	 * It checks whether the set of unifiers for conditions does not contradict the
	 * ones defined for the atoms. If that is the case, it extends the general
	 * unifier
	 * 
	 * @param unifier the new unifier
	 * @return whether the extension has been done or not
	 */
	boolean extendWithConditionUnifier(ConditionToConditionUnifier unifier) {
		/* it is correct only if it does not */
		/* violate any existing correspondence */
		for (Variable var : unifier.getVariables()) {
			for (AtomToAtomUnifier aau : this.unifiers) {
				if ((aau.getCorrespondenceByVariable(var) != null && unifier.getCorrespondenceByVariable(var) != null)
						&& (!aau.getCorrespondenceByVariable(var).equals(unifier.getCorrespondenceByVariable(var))))
					return false;
			}
			// create a fake unifier, to add the mapping to the general one
			AtomToAtomUnifier dummyUnifier = new AtomToAtomUnifier(null, null);
			dummyUnifier.mapping = unifier.mapping;
			this.unifiers.add(dummyUnifier);
		}
		return true;
	}

	/**
	 * @return the union of all unifiers
	 */
	void createGeneralUnifier() {
		generalUnifier.mapping = new HashMap<>();
		for (AtomToAtomUnifier atomToAtomUnifier : unifiers) {
			generalUnifier.mapping.putAll(atomToAtomUnifier.mapping);
		}

	}

	public AtomToAtomUnifier getGeneralUnifier() {
		if (generalUnifier != null)
			return generalUnifier;
		throw new RuntimeException("Unifier not yet generated");
	}

	public String toString() {
		StringJoiner sj = new StringJoiner(",");
		for (AtomToAtomUnifier aau : this.unifiers)
			sj.add(aau.toString());
		return sj.toString();
	}

}
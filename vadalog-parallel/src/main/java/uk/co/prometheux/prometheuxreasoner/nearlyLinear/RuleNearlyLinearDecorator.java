package uk.co.prometheux.prometheuxreasoner.nearlyLinear;

import java.util.ArrayList;
import java.util.List;

import uk.co.prometheux.prometheuxreasoner.common.IdGenerator;
import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.ComparisonOperatorsEnum;
import uk.co.prometheux.prometheuxreasoner.model.Condition;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Rule;

/**
 * It adds to the Rules functionalities useful for the nearly-linear fragment.
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
class RuleNearlyLinearDecorator {

	private Rule r;

	RuleNearlyLinearDecorator(Rule r) {
		this.r = r;
	}

	/**
	 * It detaches a Datalog rule from the danger in a literal e.g. a(X,Y),b(Y,Z)
	 * goes into c(Z) given a(X,Y) and modifiers a1(X,Y),b(Y,Z) goes into c(Z)
	 * 
	 * @param unificationLiteral the literal to rename
	 * @param newName            the new name to be assigned
	 * @return a safe rule where dangerous variables have been detached.
	 */
	Rule detachFromDanger(Literal unificationLiteral, String newName) {
		Rule rDatalogRenamed = new Rule(this.r);
		rDatalogRenamed.clearBody();

		/* rename */
		Literal newL = new Literal(unificationLiteral);
		newL.getAtom().setName(newName);

		/* we add the new literal */
		rDatalogRenamed.getLiterals().add(newL);

		/* add all the other literals */
		for (Literal l : this.r.getLiterals())
			if (!l.equals(unificationLiteral))
				rDatalogRenamed.getLiterals().add(l);

		return rDatalogRenamed;
	}

	/**
	 * evaluateCalculatedVariables transform the rule r, in two rule, that transform
	 * the model in the nearly linear model.
	 * 
	 * @return List of rule
	 */
	List<Rule> evaluateCalculatedVariables() {
		List<Rule> evaluatedRules = new ArrayList<>();
		Rule currentRule = new Rule(this.r);
		List<Condition> newCondition = new ArrayList<>();
		/*
		 * add condition in the new rule, and remove the condition from the currentRule
		 */
		for (Condition c : this.r.getConditions()) {
			if (c.getCompOp().equals(ComparisonOperatorsEnum.EQ)) {
				newCondition.add(c);
				currentRule.getConditions().remove(c);
			}
		}
		/* newAtom is the head of the new rule and the body of the current rule */
		Atom newAtom = new Atom(IdGenerator.getNewAtomSymbol("vatom_"),
				new ArrayList<>(currentRule.getSingleHead().getArguments()));
		List<Literal> newBody = new ArrayList<>();
		newBody.add(new Literal(newAtom, true));
		Rule newRule = new Rule(newAtom, new ArrayList<>(currentRule.getBody()), newCondition);
		newRule.setAnnotations(currentRule.getAnnotations());
		currentRule.setBody(newBody);
		evaluatedRules.add(newRule);
		evaluatedRules.add(currentRule);
		return evaluatedRules;
	}

}

package uk.co.prometheux.prometheuxreasoner.operators;

import java.util.HashSet;
import java.util.Set;

import uk.co.prometheux.prometheuxreasoner.common.IdGenerator;
import uk.co.prometheux.prometheuxreasoner.datalog.RuleDatalogDecorator;
import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Variable;

/**
 * It applies the extract operator to a Datalog rule (or to a Datalog program).
 * For each atom having one or more variables that do not appear in the head and
 * is not involved in any joins, we introduce a new symbol having only the
 * needed variables and externalize the atom, by removing the original one from
 * the first rule.
 * 
 * example (one variable is left): 1. d(x',y) :- p(x',y'),c(y)
 * 
 * e(x') :- p(x',y') d(x',y) :- e(x'),c(y)
 * 
 * example (extraction with 0-arity). In this case
 * 
 * 2. d(y) :- p(x',y'),c(y)
 * 
 * e :- c(y) d(y) :- e, c(y)
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class Extraction {

	private Rule rule;
	private Model model;

	Extraction(Rule rule) {
		this.rule = rule;
	}

	Extraction(Model model) {
		this.model = model;
	}

	public Model getModel() {
		Model m1 = new Model();
		for (Rule r : this.model.getRules()) {
			RuleOperatorDecorator rod = new RuleOperatorDecorator(r);
			RuleDatalogDecorator rdd = new RuleDatalogDecorator(r);
			/* only Datalog rules are extracted */
			if (rdd.isDatalog()) {
				Extraction e = rod.extract();
				e.model = this.model;
				m1.getRules().addAll(e.getRules());
			} else {
				m1.getRules().add(r);
			}
		}
		return m1;
	}

	public Set<Rule> getRules() {

		Set<Rule> extracted = new HashSet<Rule>();

		/* we simplify the rule */
		Rule newRule = new Rule(this.rule);
		extracted.add(newRule);

		for (Literal l : this.rule.getLiterals()) {

			Set<Variable> varsToRemove = new HashSet<Variable>();
			for (Variable v : l.getAtom().getVariablesAsSet()) {
				/* if a variable appears only in this literal and even not in the head */
				/* then this literal can be extracted */
				if (this.rule.getBodyLiteralsByVariable(v).size() == 1
						&& !this.rule.getSingleHead().getVariablesAsSet().contains(v)) {
					varsToRemove.add(v);
				}
			}

			/* after analyzing all the variables, we proceed with the extraction */
			if (varsToRemove.size() > 0) {

				/* we remove the current literal */
				newRule.getBody().remove(l);
				/* we create a new atom with all the variables except for those to remove */
				Literal le = new Literal(l);
				le.getAtom().setName(IdGenerator.getNewAtomSymbol("vatom_"));
				le.getAtom().getArguments().removeAll(varsToRemove);
				newRule.getBody().add(le);

				/* we create a new rule for the atom */
				Literal bodyLiteral = new Literal(l);
				Atom headAtom = new Atom(le.getAtom());
				Rule copyRule = new Rule(headAtom, bodyLiteral, null);
				extracted.add(copyRule);
			}
		}
		return extracted;
	}
}

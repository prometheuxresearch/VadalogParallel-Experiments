package uk.co.prometheux.prometheuxreasoner.operators;

import java.util.Set;
import java.util.stream.Collectors;

import uk.co.prometheux.prometheuxreasoner.model.Literal;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Variable;


/**
 * This operator takes as input a rule R1, and a rule R2, and performs the folding from R1 into 
 * 
 * 
 *  Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */
public class Folding {
	
	//Rule to fold into
	private Rule r1;
	
	public Folding(Rule r) {
		this.r1 = r;
	}
	
	public Rule into(Rule r2) {
		Rule resultRule = new Rule(r2);
		Rule r2cc = new Rule(r2);
		Rule r1c = new Rule(r1);
		
		/* we start from a clone of r2, where we rename all the variables */
		Set<String> namesToAvoid = r2.getVariables().stream()
								.map(x -> x.getName())
								.collect(Collectors.toSet());
		
		/* also the names of the calculated variables must be avoided */
		namesToAvoid.addAll(r2.getLiterals().stream()
				.filter(x -> x.getAtom().isSkolemAtom())
				.map(x -> x.getAtom().getCalculatedVariable().getName())
						.collect(Collectors.toSet()));
		
		/* we must be sure that the new names in r1c do not conflict with */
		/* any name in r2 */
		r1c.renameVariables(namesToAvoid);
		
		resultRule.clearBody();
		r2cc.clearBody();
		
		Rule r1cc = new Rule(r1);
		
		//Add necessary literals to rr, by not adding the ones that need to be folded 
		for (Literal l : r2.getBody()){
			String atomName = l.getAtom().getName();
			if(!r1cc.getBodyAtomsByNameList().containsKey(atomName)){
				resultRule.getLiterals().add(l);
			}
			else {
				r2cc.getBody().add(l);
				r1cc.getBody().remove(r1cc.getFirstLiteralByName(atomName));
			}
		}
		
		HomomorphismChecker hc = new HomomorphismChecker(r1);

		if(!hc.toBody(r2cc)) {
			throw new FoldingException("Cannot unfold, since there exist no homomorphism");
		}
				
		resultRule.getBody().add(new Literal(r1c.getSingleHead(), true));
		
		resultRule = hc.getGeneralUnifier().applyAsAmapping(resultRule);
		
		return resultRule;
	}
	
	public Rule intoWithLiterals(Rule r2, Set<Literal> literals) {
		Rule resultRule = new Rule(r2);
		Rule portionOfFoldableRule = new Rule(r2);
		Rule r1c = new Rule(r1);
		
		/* we start from a clone of r2, where we rename all the variables */
		Set<String> namesToAvoid = r2.getVariables().stream()
								.map(x -> x.getName())
								.collect(Collectors.toSet());
		
		/* also the names of the calculated variables must be avoided */
		namesToAvoid.addAll(r2.getLiterals().stream()
				.filter(x -> x.getAtom().isSkolemAtom())
				.map(x -> x.getAtom().getCalculatedVariable().getName())
						.collect(Collectors.toSet()));
		
		/* we must be sure that the new names in r1c do not conflict with */
		/* any name in r2 */
		r1c.renameVariables(namesToAvoid);
		
		resultRule.clearBody();
		portionOfFoldableRule.clearBody();
		
		//Add necessary literals to resultRule, by not adding the ones that need to be folded 
		for (Literal literalToAddToBody : r2.getBody()){
			if(!literals.contains(literalToAddToBody)) {
				resultRule.getLiterals().add(literalToAddToBody);
			}
			else {
				portionOfFoldableRule.getLiterals().add(literalToAddToBody);
				literals.remove(literalToAddToBody);
			}
		}
				
		HomomorphismChecker hc = new HomomorphismChecker(r1, resultRule.getBodyVariables().stream().map(Variable::toString).collect(Collectors.toSet()));

		if(!hc.toBodyWithConditions(portionOfFoldableRule)) {
			throw new FoldingException("Cannot fold, since there exist no homomorphism");
		}
				
		resultRule.getBody().add(new Literal(r1c.getSingleHead(), true));
		
		resultRule = hc.getGeneralUnifier().applyAsMappingWithConditions(resultRule);
		
		return resultRule;
	}

}
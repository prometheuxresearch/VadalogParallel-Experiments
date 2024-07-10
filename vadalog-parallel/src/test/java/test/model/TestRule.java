package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import uk.co.prometheux.prometheuxreasoner.common.IdGenerator;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.model.Variable;
import uk.co.prometheux.prometheuxreasoner.operators.RuleOperatorDecorator;

public class TestRule {
	
	@Test
	public void testRuleEquality() {
		IdGenerator.reset();
		Model m = new Model();
		m.readRule("p(X,Y) :- q(Y), Y>2+1.");
		m.readRule("p(X,Y) :- q(Y).");
		m.readRule("p(Vvar_5) :- p(Vvar_5,Vvar_6), q(Vvar_2,Vvar_3).");
		m.readRule("p(Vvar_5) :- p(Vvar_5,Vvar_6), q(Vvar_2,Vvar_3).");
		
		
		Rule r1 = new Rule(m.getRules().get(0));
		Rule r2 = new Rule(m.getRules().get(2));
		Rule r3 = new Rule(m.getRules().get(3));
		
		assertNotEquals(m.getRules().get(0), m.getRules().get(1));
		assertEquals(r1, m.getRules().get(0));
		assertEquals(r2, r3);
		
		/* absence of side effects */
		RuleOperatorDecorator rod2 = new RuleOperatorDecorator(r2);
		RuleOperatorDecorator rod3 = new RuleOperatorDecorator(r3);

		rod2.isHomomorphic().to(r3);
		rod3.isHomomorphic().to(r2);
		
		assertEquals(r2, r3);
		assertEquals(r3, r2);

	
	}
	
	@Test
	public void testGetExistentiallyQuantifiedVariables2() {
		IdGenerator.reset();
		Model m = new Model();
		m.readRule("a(X,Y) :- b(X),c(X).");

		Rule r1 = m.getRules().get(0);

		Set<Variable> expected1 = new HashSet<Variable>();
		expected1.add(new Variable("Y"));
		
		assertTrue(r1.getExistentiallyQuantifiedVariables().equals(expected1));
	}

	@Test
	public void testGetExistentiallyQuantifiedVariables() {
		IdGenerator.reset();
		Model m = new Model();
		m.readRule("predicate(X,Z) :- predicate(Y),p(2,\"a\",3,X,\"c\").");
		m.readRule("predicate(X) :- p(X,Y,Z).");
		m.readRule("predicate(Q) :- p(X,Y,Z).");
		m.readRule("predicate(X,Y,Q) :- p(X,Y,Z).");
		m.readRule("predicate(K,X,Y,Q) :- p(X,Y,Z).");

		Rule r1 = m.getRules().get(0);
		Rule r2 = m.getRules().get(1);
		Rule r3 = m.getRules().get(2);
		Rule r4 = m.getRules().get(3);
		Rule r5 = m.getRules().get(4);

		Set<Variable> expected1 = new HashSet<Variable>();
		expected1.add(new Variable("Z"));
		
		Set<Variable> expected2 = new HashSet<Variable>();

		Set<Variable> expected3 = new HashSet<Variable>();
		expected3.add(new Variable("Q"));
		
		Set<Variable> expected4 = new HashSet<Variable>();
		expected4.add(new Variable("Q"));
		
		Set<Variable> expected5 = new HashSet<Variable>();
		expected5.add(new Variable("K"));
		expected5.add(new Variable("Q"));
		
		assertTrue(r1.getExistentiallyQuantifiedVariables().equals(expected1));
		assertTrue(r2.getExistentiallyQuantifiedVariables().equals(expected2));
		assertTrue(r3.getExistentiallyQuantifiedVariables().equals(expected3));
		assertTrue(r4.getExistentiallyQuantifiedVariables().equals(expected4));
		assertTrue(r5.getExistentiallyQuantifiedVariables().equals(expected5));
	}
	
	@Test
	public void testPositiveRelationalRule() {
		IdGenerator.reset();
		Model m = new Model();
		m.readRule("p(X,Y) :- q(Y), Y>2+1.");
		m.readRule("r(X) :- s(Y), Y>=3."); 
		m.readRule("p(X,Y) :- p(X,Z), t(Z, W) .");
		m.readRule("a(X,Y,M) :- b(X,Q,Y),c(Y,Z),not p(\"a\",2,N).");
		m.readRule("q(X,J) :- vatom_3(X,Y,Z),J=Y+Z.");
		m.readRule("p(X,Y) :- w(X,Y).");
		m.readRule("q(X,Q,Z) :- vatom_12(X,Z,Y), Q=msum(Y).");
		
		
		Rule r1 = m.getRules().get(0);
		Rule r2 = m.getRules().get(1);
		Rule r3 = m.getRules().get(2);
		Rule r4 = m.getRules().get(3);
		Rule r5 = m.getRules().get(4);
		Rule r6 = m.getRules().get(5);
		Rule r7 = m.getRules().get(6);
		
		assertEquals(false,r1.isPositiveRelational());
		assertEquals(false,r2.isPositiveRelational());
		assertEquals(true,r3.isPositiveRelational());
		assertEquals(false,r4.isPositiveRelational());
		assertEquals(false, r5.isPositiveRelational());
		assertEquals(true,r6.isPositiveRelational());
		assertEquals(false,r7.isPositiveRelational());
		
	}
	
	@Test
	public void testIsTautology() {
		IdGenerator.reset();
		Model m = new Model();
		m.readRule("goal(X,Y):-goal(X,Y).");
		m.readRule("goal(X,Y):-goal(X,Y),goal(X,Y).");
		m.readRule("goal(X,Y):-goal(X,Y),edb(X,Y).");
		m.readRule("goal(X,Y):-edb(X,Y).");
		m.readRule("goal(X,Y):-goal(Y,X).");
		m.readRule("goal(X,Y):-goal(X,Y), X>0.");
		
		assertTrue(m.getRules().get(0).isTautology());
		assertTrue(m.getRules().get(1).isTautology());
		assertFalse(m.getRules().get(2).isTautology());
		assertFalse(m.getRules().get(3).isTautology());
		assertFalse(m.getRules().get(4).isTautology());
		assertFalse(m.getRules().get(5).isTautology());
	}
}
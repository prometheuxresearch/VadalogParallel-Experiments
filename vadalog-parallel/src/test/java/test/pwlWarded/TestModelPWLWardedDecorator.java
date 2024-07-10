package test.pwlWarded;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.co.prometheux.prometheuxreasoner.common.IdGenerator;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;
import uk.co.prometheux.prometheuxreasoner.pwlWarded.ModelPWLWardedDecorator;

public class TestModelPWLWardedDecorator {
	
	@Test
	public void testIsRecursive() {
		Model m = new Model();
		m.readRule("p(Y,Z) :- s(X,Y).");
		m.readRule("s(Y,Z) :- p(X,Y).");
		m.readRule("t(Z) :- p(Z,Y),a(Y,M).");
		ModelPWLWardedDecorator msw = new ModelPWLWardedDecorator(m);
		assert(msw.isRecursive());
		
		m = new Model();
		m.readRule("p(X) :- s(X,Y).");
		m.readRule("s(X,Y) :- r(X).");
		m.readRule("r(M) :- b(X,Y),a(Y,M).");
		msw = new ModelPWLWardedDecorator(m);
		assert(!msw.isRecursive());
	}
	
	@Test
	public void testRecursiveRule() {
		/* test if a rule is recursieve */
		Model m = new Model();
		m.readRule("desc(X,Y) :- desc(X,Z), child(Z,Y) .");
		ModelPWLWardedDecorator msw = new ModelPWLWardedDecorator(m);
		for (Rule rule : m.getRules()) {
			assert(msw.isRecursive(rule));
		}
		
		m = new Model();
		m.readRule("desc(X,Y) :- child(X,Y).");
		msw = new ModelPWLWardedDecorator(m);
		Rule rule = m.getRules().get(0);
		assert(!msw.isRecursive(rule));
		
		m.readRule("child(X,Y) :- a(X,Y) .");
		m.readRule("a(X,Y) :- desc(X,Y) .");
		m.readRule("a(X,Y) :- b(X) .");
		Rule rule1 = m.getRules().get(3);
		msw = new ModelPWLWardedDecorator(m);
		assert(msw.isRecursive(rule));
		assert(!msw.isRecursive(rule1));
	}
	
	@Test
	public void testLinearRecursiveRule() {
		/* test both linear recursive and non-linear recursive rule*/
		Model m = new Model();
		m.readRule("p(X),r(Y) :- t(X,Y) .");
		m.readRule("t(X,Y) :- r(X),q(Y).");
		m.readRule("q(X,Y) :- t(X,Y).");
		m.readRule("t(X,Y) :- n(Y) .");
		Rule rule = m.getRules().get(0); //linear recursive but not non-linear recursive
		Rule rule1 = m.getRules().get(1); //not linear recursive but non-linear recursive
		Rule rule3 = m.getRules().get(3); // non-recursive, i.e., neither linear nor non-linear recursive
		ModelPWLWardedDecorator msw = new ModelPWLWardedDecorator(m);
		assert(msw.isLinearRecursive(rule));
		assert(!msw.isNonLinearRecursive(rule));
		assert(!msw.isLinearRecursive(rule1));
		assert(msw.isNonLinearRecursive(rule1));
		assert(!msw.isLinearRecursive(rule3));
		assert(!msw.isNonLinearRecursive(rule3));
	}
	
	@Test
	public void testPWLWarded() {
		Model m = new Model();
		/* first when the program is non-recursive */
		m.readRule("a(X,Y),b(Y) :- c(X) .");
		m.readRule("c(X) :- d(X,Y) .");
		ModelPWLWardedDecorator msw = new ModelPWLWardedDecorator(m);
		assert(msw.isPWLWarded());
		/* we add linear recursion */
		Model m1 = new Model(m);
		m1.readRule("d(X,Y) :- b(X) .");
		ModelPWLWardedDecorator msw1 = new ModelPWLWardedDecorator(m1);
		assert(msw1.isPWLWarded());
		/* we add non-linear recursion */
		Model m2 = new Model(m);
		m2.readRule("d(X,Y) :- a(X,Y), b(Y).");
		ModelPWLWardedDecorator msw2 = new ModelPWLWardedDecorator(m2);
		assert(!msw2.isPWLWarded());
	}
	
	@Test
	public void testMultiJoinOptimization() {	
		IdGenerator.reset();
		Model m = new Model();
		Model expectedModel = new Model();
		
		m.readRule("i(X,Y) :- e1(X,Z), e2(Z,W), i(X,Y).");
		m.readRule("i(X,Y) :- i(X,Y).");
		
		ModelPWLWardedDecorator msw = new ModelPWLWardedDecorator(m);
		
		expectedModel.readRule("vatom_1(X,Y,Z) :- i(X,Y), e1(X,Z).");
		expectedModel.readRule("vatom_2(X,Y,Z,W) :- vatom_1(X,Y,Z), e2(Z,W).");
		expectedModel.readRule("i(X,Y) :- vatom_2(X,Y,Z,W).");
		expectedModel.readRule("i(X,Y) :- i(X,Y).");
		
		List<Rule> result = msw.multiJoinOptimization().getRules();
		List<Rule> expectedResult = new ArrayList<>(expectedModel.getRules());
		assertEquals(result, expectedResult);
		
	}
	
}

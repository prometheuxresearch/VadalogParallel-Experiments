package test.nearlyLinear;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.co.prometheux.prometheuxreasoner.common.IdGenerator;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.nearlyLinear.ModelNearlyLinearDecorator;

public class TestModelNearlyLinearDecorator {

	@Test
	public void testIsNearlyLinear() {
		Model m = new Model();
		m.readRule("p(Y,Z) :- p(X,Y).");
		m.readRule("s(Y,Z) :- p(X,Y).");
		m.readRule("t(Z) :- p(Z,Y),a(Y,M).");
		ModelNearlyLinearDecorator mnl = new ModelNearlyLinearDecorator(m);
		assert(!mnl.isNearlyLinear());
		
		m = new Model();
		m.readRule("p(X) :- p(X,Y).");
		m.readRule("s(X) :- p(X).");
		m.readRule("t(M) :- p(X,Y),a(Y,M).");
		mnl = new ModelNearlyLinearDecorator(m);
		assert(mnl.isNearlyLinear());
		
		m = new Model();
		m.readRule("d(Y,Y) :- c(X),b(X,Y).");
		m.readRule("b(Z,Y) :- a(X,Y).");
		mnl = new ModelNearlyLinearDecorator(m);
		assert(mnl.isNearlyLinear());
		
		m = new Model();
		m.readRule("d(Y,Y) :- c(X),b(X,Y).");
		m.readRule("b(Z,Y) :- a(X,Y).");
		mnl = new ModelNearlyLinearDecorator(m);
		assert(mnl.isNearlyLinear());
		
		m = new Model();
		m.readRule("c(Z) :- a(X,Y).");
		m.readRule("d(Y,Y) :- c(X),b(X,Y).");
		m.readRule("b(Z,Y) :- a(X,Y).");
		mnl = new ModelNearlyLinearDecorator(m);
		assert(!mnl.isNearlyLinear());
				
	}
	
	
	@Test
	public void testTrySimplifyWarded() {
		
		/* CASE 4 */
		/* multiple cause (two existential quantifications) */
		IdGenerator.reset();
		
		Model m = new Model();
		m.readRule("p(X,Z) :- p(X,Y)."); // Y is affected
		m.readRule("t(Y) :- p(X,Y).");
		m.readRule("t(Z) :- t(X).");
		m.readRule("q(X) :- p(X,Y),t(Y).");
	
	
	
		ModelNearlyLinearDecorator mnl = new ModelNearlyLinearDecorator(m);
		System.out.println(mnl.isNearlyLinear());
		Model m1 = mnl.simplifyAffectedJoins();
		System.out.println(new ModelNearlyLinearDecorator(m1).isNearlyLinear());
	
		System.out.println(m1);
		//assert(mnl.simplifyAffectedJoins().equals(m1));	
	}
	
	@Test
	public void testevaluateCalculatedVariables() {
		IdGenerator.reset();
		
		Model m = new Model();
		Model expectedModel = new Model();
		m.readRule("c(X,W) :- a(X,Y), b(X,Z), W = Y + Z, W>=2.");
		expectedModel.readRule("vatom_1(X,W) :- a(X,Y), b(X,Z), W=Y+Z.");
		expectedModel.readRule("c(X,W) :- vatom_1(X,W), W>=2.");
		
		ModelNearlyLinearDecorator mnl = new ModelNearlyLinearDecorator(m);
		Model resultModel = mnl.evaluateCalculatedVariables();
		System.out.println(resultModel.toString());
		assertEquals(expectedModel, resultModel);
	}
	
	@Test
	public void testevaluateCalculatedVariables2() {
		IdGenerator.reset();
		
		Model m = new Model();
		Model expectedModel = new Model();
		m.readRule("c(X,W) :- a(X,Y), W = 5, W>=2.");
		expectedModel.readRule("vatom_1(X,W):-a(X,Y),W=5.");
		expectedModel.readRule("c(X,W):-vatom_1(X,W),W>=2.");
		
		ModelNearlyLinearDecorator mnl = new ModelNearlyLinearDecorator(m);
		Model resultModel = mnl.evaluateCalculatedVariables();
		System.out.println(resultModel.toString());
		assertEquals(expectedModel, resultModel);
	}
	
	@Test
	public void testevaluateCalculatedVariables3() {
		IdGenerator.reset();
		
		Model m = new Model();
		Model expectedModel = new Model();
		m.readRule("c(X,W,M) :- a(X,Y), W = 10, W>=2, M = X + Y, M >=2.");
		expectedModel.readRule("vatom_1(X,W,M) :- a(X,Y), W=10, M=X+Y.");
		expectedModel.readRule("c(X,W,M) :- vatom_1(X,W,M), W>=2, M>=2.");
		
		ModelNearlyLinearDecorator mnl = new ModelNearlyLinearDecorator(m);
		Model resultModel = mnl.evaluateCalculatedVariables();
		System.out.println(resultModel.toString());
		assertEquals(expectedModel, resultModel);
	}
	
	@Test
	public void testevaluateCalculatedVariables4() {
		IdGenerator.reset();
		
		Model m = new Model();
		Model expectedModel = new Model();
		m.readRule("b(X,J) :- a(X,Y), J = 3, J > 2.");
		expectedModel.readRule("vatom_1(X,J) :- a(X,Y), J=3.");
		expectedModel.readRule("b(X,J) :- vatom_1(X,J), J>2.");
		
		ModelNearlyLinearDecorator mnl = new ModelNearlyLinearDecorator(m);
		Model resultModel = mnl.evaluateCalculatedVariables();
		System.out.println(resultModel.toString());
		assertEquals(expectedModel, resultModel);
	}
	
	

}

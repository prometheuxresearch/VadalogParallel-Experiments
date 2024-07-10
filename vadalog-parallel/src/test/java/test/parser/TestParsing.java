package test.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.GregorianCalendar;

import org.junit.Test;

import uk.co.prometheux.prometheuxreasoner.model.Atom;
import uk.co.prometheux.prometheuxreasoner.model.Model;
import uk.co.prometheux.prometheuxreasoner.model.Rule;


public class TestParsing {

    @Test
    public void testReadRule() {
        Model m = new Model();
        m.readRule("predicate(X) :- predicate(Y).");
        m.readRule("predicate(X) :- not predicate(Y).");
        assertEquals(2, m.getRules().size());
        assertEquals("predicate(X) :- predicate(Y).", m.getRules().get(0).toString());
        assertEquals("predicate(X) :- not predicate(Y).", m.getRules().get(1).toString());
    }

    @Test
    public void testDomStarRule() {
        Model m = new Model();
        m.readRule("predicate(X) :- predicate(Y).");
        m.readRule("predicate(X) :- not predicate(Y), dom(*).");
        assertEquals(2, m.getRules().size());
        assertEquals("predicate(X) :- predicate(Y).", m.getRules().get(0).toString());
        assertEquals("predicate(X) :- not predicate(Y), dom(*).", m.getRules().get(1).toString());
    }

    @Test
    public void testRuleWithConditions() {
        Model m = new Model();
        m.readRule("p(X,Y) :- q(X,Y), X=Y+Z.");
        m.readRule("p(X,Y) :- q(X,Y), X=Y+Z, Y<=2.0, K=\"Luigi\".");
        m.readRule("p(X,Y) :- q(X,Y), M = ((Y+Z)/2)*X.");
        m.readRule("p(X,Y) :- q(X,Y), M = -Y+1, Y=-0.5.");
        m.readRule("p(X,Y) :- q(X,Y), g(Y,Z), M = -Y+1, Y=-0.5.");
        m.readRule("p(X,Y) :- q(X,Y), X=Y-1.");
        m.readRule("p(X,Y) :- q(X,Y), X=Y-1.3.");

        assert (m.getRules().get(0).getConditions().size() == 1);
        assert (m.getRules().get(1).getConditions().size() == 3);
        assert (m.getRules().get(5).getConditions().size() == 1);
        assert (m.getRules().get(6).getConditions().size() == 1);

        Rule r = m.getRules().get(0);
        System.out.println(r);
        r = m.getRules().get(1);
        System.out.println(r);
        r = m.getRules().get(2);
        System.out.println(r);
        r = m.getRules().get(3);
        System.out.println(r);
        r = m.getRules().get(4);
        System.out.println(r);

        r = m.getRules().get(5);
        System.out.println(r);
        assertEquals("X=Y-1", r.getConditions().get(0).toString());

        r = m.getRules().get(6);
        System.out.println(r);
        assertEquals("X=Y-1.3", r.getConditions().get(0).toString());
    }

    @Test
    public void testRuleWithAggregations() {
        Model m = new Model();
        m.readRule("p(J,Y) :- q(X,Y), J=msum(X).");
        m.readRule("p(J,Y) :- q(X,Y,Z), J=mcount(X+Z).");
        m.readRule("p(J,Y) :- q(X,Y,Z), J=mprod(X+Z,<Y>).");
        m.readRule("p(J,Y) :- q(X,Y,Z), J=msum(X,<Y,Z>).");
        m.readRule("p(J,K,Y) :- q(X,Y,Z), J=msum(X,<Y,Z>), K=mprod(Y*Z , <X>).");

        assert (m.getRules().get(0).getConditions().size() == 1);
        assert (m.getRules().get(1).getConditions().size() == 1);
        assert (m.getRules().get(2).getConditions().size() == 1);
        assert (m.getRules().get(3).getConditions().size() == 1);
        assert (m.getRules().get(4).getConditions().size() == 2);


        Rule r = m.getRules().get(0);
        System.out.println(r);
        r = m.getRules().get(1);
        System.out.println(r);
        r = m.getRules().get(2);
        System.out.println(r);
        r = m.getRules().get(3);
        System.out.println(r);
        r = m.getRules().get(4);
        System.out.println(r);
    }


    @Test
    public void testRuleWithConstants() {
        Model m = new Model();
        m.readRule("predicate(X) :- predicate(Y),p(2,\"a\",3,X,2).");
        m.readRule("predicate(X) :- predicate(Y),p(2,\"a\",3.2,X,2).");
        m.readRule("predicate(X) :- predicate(Y),p(2,\"a\",3.2,3,X,2).");
        m.readRule("predicate(X) :- predicate(Y),p(2,\"a\",3, 3.2,X,2).");


        Rule r = m.getRules().get(0);
        assertEquals("predicate(X)", r.getHead().get(0).toString());
        assertEquals("predicate(Y)", r.getLiterals().get(0).toString());
        assertEquals("p(2,\"a\",3,X,2)", r.getLiterals().get(1).toString());
    }

    @Test
    public void testRuleAnnotations() {
        Model m = new Model();
        m.readRule("@a(3.5) predicate(X) :- predicate(Y).");
        m.readRule("@a(2) @b(1,\"a\") predicate(X) :- predicate(Z),predicate(Z,Q).");
        m.readRule("c(1).");
        m.readRule("c(2).");
        m.sortRules();
        m.sortFacts();

        Rule rule1 = m.getRules().get(0);
        Rule rule2 = m.getRules().get(1);

        Atom a1 = m.getFacts().get(0);
        Atom a2 = m.getFacts().get(1);

        assert (rule1.toString().equals("@a(2) @b(1,\"a\") predicate(X) :- predicate(Z), predicate(Z,Q)."));
        assert (rule2.toString().equals("@a(3.5) predicate(X) :- predicate(Y)."));

        assert (a1.toString().equals("c(1)"));
        assert (a2.toString().equals("c(2)"));
    }

    @Test
    public void testRuleWithSetConstants() {
        Model m = new Model();
        m.readRule("predicate(X) :- predicate(Y),p(2,{1,2,-3}).");
        m.readRule("predicate(X) :- predicate(Y),p(2,{}).");
        m.readRule("predicate(X) :- predicate(Y),p(2,{ }).");
        m.readRule("predicate(X) :- predicate(Y),p(2,{\"a\",\"b\"}).");
        m.readRule("predicate(X) :- predicate(Y),p(2,{-1.5}).");


        Rule r = m.getRules().get(0);
        Rule r1 = m.getRules().get(1);
        Rule r2 = m.getRules().get(2);
        Rule r3 = m.getRules().get(3);
        Rule r4 = m.getRules().get(4);


        System.out.println(r);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r3);
        System.out.println(r4);


        assertEquals("predicate(X)", r.getHead().get(0).toString());
        assertEquals("predicate(Y)", r.getLiterals().get(0).toString());
        assertEquals("p(2,{1,2,-3})", r.getLiterals().get(1).toString());
        assertEquals("p(2,{})", r1.getLiterals().get(1).toString());
        assertEquals("p(2,{})", r2.getLiterals().get(1).toString());
        assertEquals("p(2,{\"b\",\"a\"})", r3.getLiterals().get(1).toString());
        assertEquals("p(2,{-1.5})", r4.getLiterals().get(1).toString());
    }

    @Test
    public void testRuleWithListConstants() {
        Model m = new Model();
        m.readRule("p(X) :- q(Y, 4), r(0, [1,2,-3]).");
        m.readRule("p(X) :- q(Y, 4), r(0, []).");
        m.readRule("p(X) :- q(Y, 4), r(0, [1,\"2\",-3]).");
        m.readRule("p(X) :- q(Y, 4), r(0, [1,[\"2\"],[-3]]).");

        Rule r0 = m.getRules().get(0);
        Rule r1 = m.getRules().get(1);
        Rule r2 = m.getRules().get(2);
        Rule r3 = m.getRules().get(3);


        System.out.println(r0);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r3);


        assertEquals("p(X)", r0.getHead().get(0).toString());
        assertEquals("q(Y,4)", r0.getLiterals().get(0).toString());
        assertEquals("r(0,[1,2,-3])", r0.getLiterals().get(1).toString());
        assertEquals("r(0,[])", r1.getLiterals().get(1).toString());
        assertEquals("r(0,[1,\"2\",-3])", r2.getLiterals().get(1).toString());
        assertEquals("r(0,[1,[\"2\"],[-3]])", r3.getLiterals().get(1).toString());
    }

    @Test
    public void testRuleWithDateConstants() {
        Model m = new Model();
        m.readRule("predicate(X) :- predicate(Y),p(2, 2011-10-21 11:11:11).");
        m.readRule("predicate(X) :- predicate(Y),p(2,2012-10-20).");


        Rule r = m.getRules().get(0);
        Rule r1 = m.getRules().get(1);


        System.out.println(r);
        System.out.println(r1);

        //9 = October
        GregorianCalendar c1 = new GregorianCalendar(2011, 9, 21, 11, 11, 11);
        GregorianCalendar c2 = new GregorianCalendar(2012, 9, 20);
        System.out.println(r.getLiterals().get(1).toString());
        System.out.println("p(2, " + c1.toString() + ")");
        assertEquals("predicate(X)", r.getHead().get(0).toString());
        assertEquals("predicate(Y)", r.getLiterals().get(0).toString());
        assertEquals(r.getLiterals().get(1).toString(), "p(2," + c1.toString() + ")");
        assertEquals(r1.getLiterals().get(1).toString(), "p(2," + c2.toString() + ")");
    }

    @Test
    public void testRuleWithNegation() {
        Model m = new Model();
        m.readRule("q(X) :- a(X), not b(X).");
        m.readRule("q(X) :- not q(Y), not b(X), c(X).");

        Rule r = m.getRules().get(0);
        Rule r1 = m.getRules().get(1);

        System.out.println(r);
        System.out.println(r1);

        assert (!r.getBody().get(1).isPositive());
        assert (!r1.getBody().get(1).isPositive());

    }

}

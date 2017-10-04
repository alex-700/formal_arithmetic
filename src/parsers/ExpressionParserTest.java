package parsers;

import expressions.logic.*;
import expressions.terms.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ExpressionParserTest {

    @Test
    void testLogic() throws Exception {
        Expression expr = new Entailment(new Predicate("P123"), new Entailment(new Predicate("P321"), new Predicate("P213")));
        assertEquals(expr, ExpressionParser.parse("  P123   ->    P321   ->   P213 "));
        assertEquals(expr, ExpressionParser.parse("  P123   ->   ( P321   ->   P213 )  "));

        expr = new Entailment(new Entailment(new Predicate("P321"), new Predicate("P213")), new Predicate("P123"));
        assertEquals(expr, ExpressionParser.parse("  ( P321   ->    P213 )  ->   P123 "));

        expr = new Entailment(new And(new Predicate("A"), new Predicate("B")), new Or(new Predicate("A"), new Predicate("B")));
        assertEquals(expr, ExpressionParser.parse("   A   &  B    ->  A   |   B  "));

        expr = new Entailment(new And(new Not(new Predicate("A")), new Not(new Predicate("B"))), new Not(new Or(new Predicate("A"), new Predicate("B"))));
        assertEquals(expr, ExpressionParser.parse("   !  A   &  !  B    ->    !  (  A   |   B)  "));
    }

    @Test
    void testPredicate() throws Exception {
        List<Term> list = new ArrayList<Term>();
        list.add(new Variable("a"));
        Expression expr = new Predicate("P", list);
        assertEquals(expr, ExpressionParser.parse("P(a)"));
        assertEquals(expr, ExpressionParser.parse("P(  a  )"));
        assertEquals(expr, ExpressionParser.parse("P(  a  )  "));
        assertEquals(expr, ExpressionParser.parse(" P   ( a  )  "));

        list = new ArrayList<Term>();
        list.add(new Variable("a"));
        list.add(new Variable("b"));
        list.add(new Variable("c"));
        expr = new Predicate("P", list);
        assertEquals(expr, ExpressionParser.parse("P(a,b,c)"));
        assertEquals(expr, ExpressionParser.parse("P(  a,b,c  )"));
        assertEquals(expr, ExpressionParser.parse("P (  a,b,c  )  "));
        assertEquals(expr, ExpressionParser.parse(" P   ( a  , b , c  )  "));

        expr = new Predicate("A");
        assertEquals(expr, ExpressionParser.parse(" A "));

        list = new ArrayList<Term>();
        list.add(new Variable("a123"));
        list.add(new Variable("a12"));
        list.add(new Variable("a1"));
        expr = new Predicate("P321", list);
        assertEquals(expr, ExpressionParser.parse("   P321  (   a123  ,  a12,  a1  )"));


        list = new ArrayList<Term>();
        list.add(new Variable("a123"));
        list.add(new Variable("a12"));
        expr = new Predicate("=", list);
        assertEquals(expr, ExpressionParser.parse("    a123    =   a12"));
    }

    @Test
    void testQuantifier() throws Exception {
        Expression expr = new All("a12", new Predicate("P"));
        assertEquals(expr, ExpressionParser.parse("  @  a12   P "));

        List<Term> list = new ArrayList<Term>();
        list.add(new Variable("x"));
        list.add(new Variable("x"));
        expr = new All("x", new Predicate("=", list));
        assertEquals(expr, ExpressionParser.parse("@xx=x"));
        expr = new Exist("x", new Predicate("=", list));
        assertEquals(expr, ExpressionParser.parse("?xx=x"));
    }

    @Test
    void testMiddle() throws Exception {
        Expression expr;
        List<Term> list = new ArrayList<Term>();
        list.add(new Sum(new Variable("x"), new Successor(new Successor(new Successor(new Zero())))));
        list.add(new Successor(new Successor(new Successor(new Variable("x")))));
        expr = new All("x", new Predicate("=", list));
        assertEquals(expr, ExpressionParser.parse("@xx+0'''=x'''"));

        list = new ArrayList<Term>();
        list.add(new Sum(new Sum(new Variable("x"), new Successor(new Successor(new Successor(new Zero())))), new Variable("x")));
        list.add(new Mul(new Successor(new Successor(new Successor(new Variable("x")))), new Successor(new Successor(new Successor(new Zero())))));
        expr = new Exist("x", new Predicate("=", list));
        assertEquals(expr, ExpressionParser.parse("?xx+0'''+x=x'''*0'''"));
    }

    @Test
    void testFunction() throws Exception {
        List<Term> list = new ArrayList<Term>();
        list.add(new Variable("b"));
        list.add(new Variable("c"));
        list.add(new Variable("d"));
        Term term1 = new Function("a", list);
        List<Term> list1 = new ArrayList<Term>();
        list1.add(term1);
        list1.add(new Variable("e"));
        Term term2 = new Function("f", list1);
        List<Term> list2 = new ArrayList<Term>();
        list2.add(term2);
        list2.add(new Variable("x"));

        Expression expression = new Predicate("=", list2);
        assertEquals(expression, ExpressionParser.parse(" f ( a ( b ,   c ,     d   ) ,    e   )    =   x   "));
    }

    @Test
    void testHard() throws Exception {
        List<Term> terms = new ArrayList<Term>();
        terms.add(new Successor(new Successor(new Variable("x"))));
        terms.add(new Successor(new Successor(new Successor(new Variable("x")))));
        Expression expression = new Predicate("=", terms);
        assertEquals(expression, ExpressionParser.parse("(((x))')'=(((x))')''"));
    }
}
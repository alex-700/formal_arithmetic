package proofs.axioms;

import expressions.logic.Expression;
import org.junit.jupiter.api.Test;
import parsers.ExpressionParser;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class AxiomTest {

    @Test
    public void testMatch9() throws Exception {
        Expression expression = ExpressionParser.parse("(x'+0=(x+0)')&@y(((x)'+y=(x+y)')->((x)'+(y)'=(x+(y)')'))->((x)'+y=(x+y)')");
        assertTrue(ArithmeticAxiom.Nine.match(expression));
        expression = ExpressionParser.parse("(x'+0=(x+0)')&@y(((x)'+0=(x+0)')->((x)'+(0)=(x+(0))'))->((x)'+0=(x+0)')");
        assertTrue(ArithmeticAxiom.Nine.match(expression));
        expression = ExpressionParser.parse("P(0)&@x123 (P(x123) -> P(x123')) -> P(x123)");
        assertTrue(ArithmeticAxiom.Nine.match(expression));
    }

    @Test
    public void testMatchOther() throws Exception {
        Expression expression = ExpressionParser.parse("@xP(x)->P(y)");
        assertTrue(LogicAxiom.Eleven.match(expression));
        expression = ExpressionParser.parse("@aQ(a)->Q(a)");
        assertTrue(LogicAxiom.Eleven.match(expression));
        expression = ExpressionParser.parse("@x(@xP(x)&Q(x))->@xP(x)&Q(x)");
        assertTrue(LogicAxiom.Eleven.match(expression));
        expression = ExpressionParser.parse("@x(Q(x))->Q(x)");
        assertTrue(LogicAxiom.Eleven.match(expression));
        expression = ExpressionParser.parse("@x(@yP(y)&Q(x))->@yP(y)&Q(x)");
        assertTrue(LogicAxiom.Eleven.match(expression));
        expression = ExpressionParser.parse("@x((@xP(x))&Q(x))->(@xP(x))&Q(x)");
        assertTrue(LogicAxiom.Eleven.match(expression));
    }
}
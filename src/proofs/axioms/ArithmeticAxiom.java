package proofs.axioms;

import expressions.logic.All;
import expressions.logic.And;
import expressions.logic.Entailment;
import expressions.logic.Expression;
import expressions.terms.Successor;
import expressions.terms.Term;
import expressions.terms.Variable;
import expressions.terms.Zero;
import parsers.ExpressionParser;
import parsers.ParserException;
import verifiers.VerifierException;

import java.util.HashSet;

public enum ArithmeticAxiom implements Axiom {

    One("a=b->a'=b'"),
    Two("a=b->a=c->b=c"),
    Three("a'=b'->a=b"),
    Four("!(a'=0)"),
    Five("a+b'=(a+b)'"),
    Six("a+0=a"),
    Seven("a*0=0"),
    Eight("a*b'=a*b+a"),
    Nine();

    public Expression expr;

    ArithmeticAxiom(String s) {
        try {
            this.expr = ExpressionParser.parse(s);
        } catch (ParserException e) {
            System.out.println(e.getMessage());
        }
    }

    ArithmeticAxiom() {
        this.expr = null;
    }

    @Override
    public boolean match(Expression expression) throws VerifierException {
        if (this.equals(ArithmeticAxiom.Nine)) {
            if (!(expression instanceof Entailment)) return false;
            Expression left1 = ((Entailment) expression).left;
            Expression fourth = ((Entailment) expression).right;
            if (!(left1 instanceof And)) return false;
            Expression first = ((And) left1).left;
            Expression right2 = ((And) left1).right;
            if (!(right2 instanceof All)) return false;
            String name = ((All) right2).variable;
            Expression e = ((All) right2).expression;
            if (!(e instanceof Entailment)) return false;
            Expression second = ((Entailment) e).left;
            Expression third = ((Entailment) e).right;
            Term[] zero = new Term[1];
            zero[0] = new Zero();
            Term[] succ = new Term[1];
            succ[0] = new Successor(new Variable(name));
            return fourth.equals(second)
            && fourth.almostEquals(first, name, zero, new HashSet<>())
            && fourth.almostEquals(third, name, succ, new HashSet<>());
        } else {
            return expr.equals(expression);
        }
    }

    @Override
    public String toString() {
        if (this.equals(ArithmeticAxiom.Nine)) {
            return "(Схема аксиом индукции)";
        } else {
            return String.format("(Аксиома ФА номер %d)",(ordinal() + 1));
        }
    }
}
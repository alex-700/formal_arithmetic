package proofs.axioms;

import expressions.logic.All;
import expressions.logic.Entailment;
import expressions.logic.Exist;
import expressions.logic.Expression;
import expressions.terms.Term;
import parsers.ExpressionParser;
import parsers.ParserException;
import verifiers.VerifierException;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Ale on 31.01.2015.
 */
public enum LogicAxiom implements Axiom {

    One("A->B->A"),
    Two("(A->B)->(A->B->C)->(A->C)"),
    Three("A->B->A&B"),
    Four("A&B->A"),
    Five("A&B->B"),
    Six("A->A|B"),
    Seven("B->A|B"),
    Eight("(A->C)->(B->C)->(A|B->C)"),
    Nine("(A->B)->(A->!B)->!A"),
    Ten("!!A->A"),
    Eleven("@xA->A"),
    Twelve("A->?xA");

    public Expression expr;

    private LogicAxiom(String s) {
        try {
            this.expr = ExpressionParser.parse(s);
        } catch (ParserException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean match(Expression ex) throws VerifierException {
        if (this.equals(LogicAxiom.Eleven)) {
            if (!(ex instanceof Entailment)) return false;
            Expression left = ((Entailment) ex).left;
            Expression right = ((Entailment) ex).right;
            if (!(left instanceof All)) return false;
            String name = ((All) left).variable;
            Expression exprLeft = ((All) left).expression;
            Term[] newName = new Term[1];
            newName[0] = null;
            boolean ok;
            try {
                ok = exprLeft.almostEquals(right, name, newName, new HashSet<String>());
            } catch (VerifierException e) {
                throw new VerifierException(": " + e.getMessage().replace("...", ex.toString()));
            }
            return ok;
        } else if (this.equals(LogicAxiom.Twelve)) {
            if (!(ex instanceof Entailment)) return false;
            Expression left = ((Entailment) ex).left;
            Expression right = ((Entailment) ex).right;
            if (!(right instanceof Exist)) return false;
            String name = ((Exist) right).variable;
            Expression exprRight = ((Exist) right).expression;
            Term[] newName = new Term[1];
            newName[0] = null;
            boolean ok;
            try {
                ok = exprRight.almostEquals(left, name, newName, new HashSet<String>());
            } catch (VerifierException e) {
                throw new VerifierException(": " + e.getMessage().replace("...", ex.toString()));
            }
            return ok;
        } else {
            HashMap<String, Expression> map = new HashMap<String, Expression>();
            return expr.matches(ex, map);
        }
    }


    @Override
    public String toString() {
        return " (" + "Схема аксиом " + (ordinal() + 1) + ")";
    }

}

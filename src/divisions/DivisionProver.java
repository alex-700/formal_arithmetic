package divisions;

import expressions.logic.*;
import expressions.terms.*;
import parsers.ExpressionParser;
import proofs.axioms.ArithmeticAxiom;
import proofs.Proof;
import verifiers.VerifierException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DivisionProver {
    public static final String lemmaVariable = "t";

    public static Proof prove(int a, int b) throws VerifierException {
        if (a % b != 0) {
            return null;
        } else {
            final Exist goal = (Exist) DivisionUtils.divide(term(a), term(b));
            Proof proof = new Proof();
            if (a != 0) {
                Term bTerm = term(b);
                //proveZeroDivision(b, proof);
                Expression lemma = addLemma(proof, b);
                proveTDivideT(b, proof);
                Term A, B, C;
                for (int i = 0; i < a / b - 1; i++) {
                    System.out.println(i);
                    //proof = ProofOptimizer.optimize(proof);
                    DivisionUtils.addConst((Predicate) proof.getProofedExpression(), bTerm, proof);
                    ReplaceUtils.reverseEquality((Predicate) proof.getProofedExpression(), proof);
                    A = ((Predicate) proof.getProofedExpression()).getTerms().get(0);
                    C = ((Predicate) proof.getProofedExpression()).getTerms().get(1);
                    proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Eight, bTerm, term(i + 1)));
                    ReplaceUtils.reverseEquality((Predicate) proof.getProofedExpression(), proof);
                    B = ((Predicate) proof.getProofedExpression()).getTerms().get(1);
                    proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Two, A, B, C));
                    proof.addExpression(((Entailment) proof.getProofedExpression()).right);
                    proof.addExpression(((Entailment) proof.getProofedExpression()).right);
                    ReplaceUtils.reverseEquality((Predicate) proof.getProofedExpression(), proof);
                    A = ((Predicate) proof.getProofedExpression()).getTerms().get(0);
                    C = ((Predicate) proof.getProofedExpression()).getTerms().get(1);
                    applyLemma(proof, lemma, term(b * (i + 1)));
                    B = ((Predicate) proof.getProofedExpression()).getTerms().get(1);
                    proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Two, A, B, C));
                    proof.addExpression(((Entailment) proof.getProofedExpression()).right);
                    proof.addExpression(((Entailment) proof.getProofedExpression()).right);
                }
            } else {
                proveZeroDivision(b, proof);
            }
            proof.addExpression(new Entailment(proof.getProofedExpression(), goal));
            proof.addExpression(goal);
            return proof;
        }
    }

    private static Term term(int b) {
        Term ans = new Zero();
        for (int i = 0; i < b; i++) {
            ans = new Successor(ans);
        }
        return ans;
    }

    private static void applyLemma(Proof proof, Expression expression, Term term) throws VerifierException {
        Map<String, Term> map = new HashMap<>();
        map.put(lemmaVariable, term);
        Expression expr = ((All) expression).expression.replaceFreeVariables(map, new HashSet<>());
        proof.addExpression(new Entailment(expression, expr));
        proof.addExpression(expr);
    }

    private static Expression addLemma(Proof proof, int b) throws VerifierException {
        Variable var = new Variable(lemmaVariable);
        proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Six, var));
        for (int i = 0; i < b; i++) {
            Term A = ((Predicate) proof.getProofedExpression()).getTerms().get(0);
            Term B = ((Predicate) proof.getProofedExpression()).getTerms().get(1);
            proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.One, A, B));
            proof.addExpression(((Entailment) proof.getProofedExpression()).right);
            Term C = ((Predicate) proof.getProofedExpression()).getTerms().get(1);
            proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Five, ((Sum) A).left, ((Sum) A).right));
            ReplaceUtils.reverseEquality((Predicate) proof.getProofedExpression(), proof);
            A = ((Predicate) proof.getProofedExpression()).getTerms().get(0);
            B = ((Predicate) proof.getProofedExpression()).getTerms().get(1);
            proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Two, A, B, C));
            proof.addExpression(((Entailment) proof.getProofedExpression()).right);
            proof.addExpression(((Entailment) proof.getProofedExpression()).right);
        }
        Expression truth = ExpressionParser.parseConst("0=0->0=0->0=0");
        Expression expr = proof.getProofedExpression();
        proof.addExpression(truth);
        proof.addExpression(new Entailment(expr, new Entailment(truth, expr)));
        proof.addExpression(new Entailment(truth, expr));
        proof.addExpression(new Entailment(truth, new All(var.toString(), expr)));
        proof.addExpression(new All(var.toString(), expr));
        return proof.getProofedExpression();
    }

    private static void proveZeroDivision(int b, Proof proof) throws VerifierException {
        Term bTerm = new Zero();
        for (int i = 0; i < b; i++) {
            bTerm = new Successor(bTerm);
        }
        proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Seven, bTerm));
        ReplaceUtils.reverseEquality((Predicate) proof.getProofedExpression(), proof);
    }

    private static void proveTDivideT(int b, Proof proof) throws VerifierException {
        Term bTerm = new Zero();
        for (int i = 0; i < b; i++) {
            bTerm = new Successor(bTerm);
        }
        final Zero zero = new Zero();
        proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Eight, bTerm, zero));
        ReplaceUtils.reverseEquality((Predicate) proof.getProofedExpression(), proof);
        proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Seven, bTerm));
        DivisionUtils.addConst((Predicate) proof.getProofedExpression(), bTerm, proof);
        Term A = ((Predicate) proof.getProofedExpression()).getTerms().get(0);
        Term C = ((Predicate) proof.getProofedExpression()).getTerms().get(1);
        Term B = new Mul(bTerm, new Successor(new Zero()));
        proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Two, A, B, C));
        proof.addExpression(((Entailment) proof.getProofedExpression()).right);
        proof.addExpression(((Entailment) proof.getProofedExpression()).right);
        ReplaceUtils.reverseEquality((Predicate) proof.getProofedExpression(), proof);
        A = ((Predicate) proof.getProofedExpression()).getTerms().get(0);
        C = ((Predicate) proof.getProofedExpression()).getTerms().get(1);
        DivisionUtils.proveTEqual0T(proof, bTerm);
        ReplaceUtils.reverseEquality((Predicate) proof.getProofedExpression(), proof);
        B = ((Predicate) proof.getProofedExpression()).getTerms().get(1);
        proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Two, A, B, C));
        proof.addExpression(((Entailment) proof.getProofedExpression()).right);
        proof.addExpression(((Entailment) proof.getProofedExpression()).right);
    }
}
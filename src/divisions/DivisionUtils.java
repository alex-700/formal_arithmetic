package divisions;

import deductors.Deductor;
import deductors.DeductorException;
import expressions.logic.*;
import expressions.terms.*;
import proofs.axioms.ArithmeticAxiom;
import proofs.Proof;
import verifiers.VerifierException;

import java.util.Arrays;

public class DivisionUtils {
    public static final String nameForExist = "z";

    public static Expression divide(Term term1, Term term2) {
        return new Exist(nameForExist, new Predicate("=", Arrays.asList(term1, new Mul(term2, new Variable(nameForExist)))));
    }

    public static void addConst(Predicate predicate, Term term, Proof proof) throws VerifierException {
        assert predicate.getName().equals("=");
        Term term1 = predicate.getTerms().get(0);
        Term term2 = predicate.getTerms().get(1);
        proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Six, term1));
        ReplaceUtils.reverseEquality((Predicate) proof.getProofedExpression(), proof);
        proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Six, term2));
        ReplaceUtils.reverseEquality((Predicate) proof.getProofedExpression(), proof);
        proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Two, term1, term2, new Sum(term1, new Zero())));
        proof.addExpression(((Entailment) proof.getProofedExpression()).right);
        proof.addExpression(((Entailment) proof.getProofedExpression()).right);
        proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Two, term2, new Sum(term1, new Zero()), new Sum(term2, new Zero())));
        proof.addExpression(((Entailment) proof.getProofedExpression()).right);
        proof.addExpression(((Entailment) proof.getProofedExpression()).right);
        Proof deductionProof = new Proof();
        Variable var = new Variable("t");
        deductionProof.addAssumption(new Predicate("=", Arrays.asList(new Sum(term1, var), new Sum(term2, var))));
        deductionProof.addExpression(new Predicate("=", Arrays.asList(new Sum(term1, var), new Sum(term2, var))));
        deductionProof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Five, term1, var));
        ReplaceUtils.reverseEquality((Predicate) deductionProof.getProofedExpression(), deductionProof);
        deductionProof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Five, term2, var));
        ReplaceUtils.reverseEquality((Predicate) deductionProof.getProofedExpression(), deductionProof);
        deductionProof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.One, new Sum(term1, var), new Sum(term2, var)));
        deductionProof.addExpression(((Entailment) deductionProof.getProofedExpression()).right);
        deductionProof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Two, new Successor(new Sum(term1, var)), new Sum(term1, new Successor(var)), new Successor(new Sum(term2, var))));
        deductionProof.addExpression(((Entailment) deductionProof.getProofedExpression()).right);
        deductionProof.addExpression(((Entailment) deductionProof.getProofedExpression()).right);
        ReplaceUtils.reverseEquality((Predicate) deductionProof.getProofedExpression(), deductionProof);
        deductionProof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Two, new Successor(new Sum(term2, var)), new Sum(term1, new Successor(var)), new Sum(term2, new Successor(var))));
        deductionProof.addExpression(((Entailment) deductionProof.getProofedExpression()).right);
        deductionProof.addExpression(((Entailment) deductionProof.getProofedExpression()).right);
        try {
            deductionProof = Deductor.deduct(deductionProof);
        } catch (DeductorException e) {
            e.printStackTrace();
            return;
        }
        Expression base = proof.getProofedExpression();
        proof.merge(deductionProof);
        Expression inductiveStep = proof.getProofedExpression();
        InductionUtils.induction(proof, base, inductiveStep, var);
        ReplaceUtils.replaceVariable(proof, var, term);
    }

    public static void proveTEqual0T(Proof proof, Term term) throws VerifierException {
        proof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Six, new Zero()));
        ReplaceUtils.reverseEquality((Predicate) proof.getProofedExpression(), proof);
        Expression base = proof.getProofedExpression();
        Variable var = new Variable("t");
        Proof deductionProof = new Proof();
        Expression assumption = new Predicate("=", Arrays.asList(var, new Sum(new Zero(), var)));
        deductionProof.addAssumption(assumption);
        deductionProof.addExpression(assumption);
        deductionProof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Five, new Zero(), var));
        ReplaceUtils.reverseEquality((Predicate) deductionProof.getProofedExpression(), deductionProof);
        deductionProof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.One, var, new Sum(new Zero(), var)));
        deductionProof.addExpression(((Entailment) deductionProof.getProofedExpression()).right);
        ReplaceUtils.reverseEquality((Predicate) deductionProof.getProofedExpression(), deductionProof);
        Term A = ((Predicate) deductionProof.getProofedExpression()).getTerms().get(0);
        Term B = ((Predicate) deductionProof.getProofedExpression()).getTerms().get(1);
        Term C = new Sum(new Zero(), new Successor(var));
        deductionProof.addExpressionList(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Two, A, B, C));
        deductionProof.addExpression(((Entailment) deductionProof.getProofedExpression()).right);
        deductionProof.addExpression(((Entailment) deductionProof.getProofedExpression()).right);
        try {
            deductionProof = Deductor.deduct(deductionProof);
        } catch (DeductorException e) {
            e.printStackTrace();
        }
        Expression inductionStep = deductionProof.getProofedExpression();
        proof.merge(deductionProof);
        InductionUtils.induction(proof, base, inductionStep, var);
        ReplaceUtils.replaceVariable(proof, var, term);
    }
}
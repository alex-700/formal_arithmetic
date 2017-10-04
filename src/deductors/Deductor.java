package deductors;

import expressions.logic.All;
import expressions.logic.Entailment;
import expressions.logic.Exist;
import expressions.logic.Expression;
import proofs.assumptions.Assumption;
import proofs.axioms.Axiom;
import proofs.axioms.LogicAxiom;
import proofs.derivation_rules.AllRule;
import proofs.derivation_rules.ExistRule;
import proofs.derivation_rules.ModusPonens;
import proofs.Proof;
import proofs.Statement;
import verifiers.VerifierException;

public class Deductor {
    public static Proof deduct(Proof deductProof) throws DeductorException, VerifierException {
        Proof proof = new Proof();
        Expression alpha = deductProof.getLastAssumption();
        int lastNumber = deductProof.getAssumptions().expressions.size() - 1;
        for (int i = 0; i < lastNumber; i++) {
            proof.addAssumption(deductProof.getAssumptions().expressions.get(i));
        }
        int i = 0;
        for (Statement statement : deductProof.getStatements()) {
            ++i;
            if (statement.getType() instanceof Assumption) {
                if (((Assumption) statement.getType()).getNumber() == lastNumber) {
                    proof.addExpressionList(DeductorHelper.getDeductionAEA(statement.getExpression()));
                } else {
                    proof.addExpressionList(DeductorHelper.getDeductionAxiom(statement.getExpression(), alpha));
                }
            } else if (statement.getType() instanceof Axiom) {
                if (statement.getType().equals(LogicAxiom.Eleven)) {
                    proof.addExpressionList(DeductorHelper.getDeductionAxiom(statement.getExpression(), alpha));
                } else
                if (statement.getType().equals(LogicAxiom.Twelve)) {
                    proof.addExpressionList(DeductorHelper.getDeductionAxiom(statement.getExpression(), alpha));
                } else {
                    proof.addExpressionList(DeductorHelper.getDeductionAxiom(statement.getExpression(), alpha));
                }
            } else if (statement.getType() instanceof ModusPonens) {
                Expression f = deductProof.getStatements().get(((ModusPonens) statement.getType()).getFirst()).getExpression();
                Expression s = deductProof.getStatements().get(((ModusPonens) statement.getType()).getSecond()).getExpression();
                proof.addExpressionList(DeductorHelper.getDeductionForMP(alpha, f, s, statement.getExpression()));
            } else if (statement.getType() instanceof AllRule) {
                Expression left = ((Entailment) statement.getExpression()).left;
                Expression withQ = ((Entailment) statement.getExpression()).right;
                Expression withoutQ = ((All) withQ).expression;
                String name = ((All) withQ).variable;
                if (alpha.hasFree(name))
                    throw new DeductorException(String.format("Вывод некорректен начиная с формулы номер %d : используется правило вывода с квантором по переменной %s входящей свободно в допущение %s", i, name, alpha));
                proof.addExpressionList(DeductorHelper.getDeductionAllRule(alpha, withQ, withoutQ, left));
            } else if (statement.getType() instanceof ExistRule) {
                Expression right = ((Entailment) statement.getExpression()).right;
                Expression withQ = ((Entailment) statement.getExpression()).left;
                Expression withoutQ = ((Exist) withQ).expression;
                String name = ((Exist) withQ).variable;
                if (alpha.hasFree(name))
                    throw new DeductorException(String.format("Вывод некорректен начиная с формулы номер %d : используется правило вывода с квантором по переменной %s входящей свободно в допущение %s", i, name, alpha));
                proof.addExpressionList(DeductorHelper.getDeductionExistRule(alpha, withQ, withoutQ, right));
            }
        }
        return proof;
    }

}
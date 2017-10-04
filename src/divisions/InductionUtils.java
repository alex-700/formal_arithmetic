package divisions;

import expressions.logic.All;
import expressions.logic.And;
import expressions.logic.Entailment;
import expressions.logic.Expression;
import expressions.terms.Variable;
import parsers.ExpressionParser;
import proofs.Proof;
import verifiers.VerifierException;

public class InductionUtils {
    public static void induction(Proof proof, Expression base, Expression inductionStep, Variable var) throws VerifierException {
            Expression truth = ExpressionParser.parseConst("0=0->0=0->0=0");
            proof.addExpression(new Entailment(inductionStep, new Entailment(truth, inductionStep)));
            proof.addExpression(new Entailment(truth, inductionStep));
            Expression allInduction = new All(var.toString(), inductionStep);
            proof.addExpression(new Entailment(truth, allInduction));
            proof.addExpression(allInduction);
            proof.addExpression(new Entailment(base, new Entailment(allInduction, new And(base, allInduction))));
            proof.addExpression(((Entailment) proof.getProofedExpression()).right);
            proof.addExpression(((Entailment) proof.getProofedExpression()).right);
            proof.addExpression(new Entailment(new And(base, allInduction), ((Entailment) inductionStep).left));
            proof.addExpression(((Entailment) proof.getProofedExpression()).right);
    }
}
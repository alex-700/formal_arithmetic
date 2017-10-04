package proofs.axioms;

import expressions.logic.Expression;
import proofs.StatementType;
import verifiers.VerifierException;

public interface Axiom extends StatementType {
    boolean match(Expression expr) throws VerifierException;
}

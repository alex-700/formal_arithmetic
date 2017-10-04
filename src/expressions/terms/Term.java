package expressions.terms;

import verifiers.VerifierException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface Term {
    boolean almostEquals(Term term, String variableName, Term[] newName, HashSet<String> quantifier) throws VerifierException;

    String getOperatorName();

    boolean free(HashSet<String> quantifiers);

    boolean hasVariable(String variableName);

    Priority getPriority();

    StringBuilder toStringQuick();

    void getFreeVariables(Set<String> variables, Set<String> notFree);

    Term replaceFreeVariables(Map<String, Term> rep, Set<String> notFree);
}
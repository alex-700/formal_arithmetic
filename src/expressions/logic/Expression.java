package expressions.logic;

import expressions.terms.Term;
import verifiers.VerifierException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface Expression {
    boolean matches(Expression expression, Map<String, Expression> map);

    String getOperatorName();

    boolean almostEquals(Expression expression, String variableName, Term[] newVariableName, HashSet<String> quantifiers) throws VerifierException;

    boolean hasFree(String variableName);

    Priority getPriority();

    Expression fillPredicate(HashMap<String, Expression> hashMap);

    StringBuilder toStringQuick();

    void getFreeVariables(Set<String> variables, Set<String> notFree);

    Expression replaceFreeVariables(Map<String, Term> rep, Set<String> notFree);
}

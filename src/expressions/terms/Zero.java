package expressions.terms;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Zero implements Term {
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Zero;
    }

    @Override
    public int hashCode() {
        return 238746;
    }

    @Override
    public boolean almostEquals(Term term, String variableName, Term[] newName, HashSet<String> quantifier) {
        return term instanceof Zero;
    }

    @Override
    public String getOperatorName() {
        return Zero.class.getSimpleName();
    }

    @Override
    public boolean free(HashSet<String> quantifiers) {
        return true;
    }

    @Override
    public boolean hasVariable(String variableName) {
        return false;
    }

    @Override
    public Priority getPriority() {
        return Priority.Zero;
    }

    @Override
    public String toString() {
        return "0";
    }

    @Override
    public StringBuilder toStringQuick() {
        return new StringBuilder("0");
    }

    @Override
    public void getFreeVariables(Set<String> variables, Set<String> notFree) {}

    @Override
    public Term replaceFreeVariables(Map<String, Term> rep, Set<String> notFree) {
        return this;
    }
}
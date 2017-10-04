package expressions.terms;

import verifiers.VerifierException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Successor implements Term {

    Term term;

    public Successor(Term term) {
        this.term = term;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Successor)) return false;

        Successor successor = (Successor) o;

        if (!term.equals(successor.term)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return term.hashCode();
    }

    @Override
    public boolean almostEquals(Term t, String variableName, Term[] newName, HashSet<String> quantifier) throws VerifierException {
        return t instanceof Successor && term.almostEquals(((Successor) t).term, variableName, newName, quantifier);
    }

    @Override
    public String getOperatorName() {
        return Successor.class.getSimpleName();
    }

    @Override
    public boolean free(HashSet<String> quantifiers) {
        return term.free(quantifiers);
    }

    @Override
    public boolean hasVariable(String variableName) {
        return term.hasVariable(variableName);
    }

    @Override
    public Priority getPriority() {
        return Priority.Successor;
    }

    @Override
    public String toString() {
        String sexpr = term.toString();
        if (term.getPriority().ordinal() < getPriority().ordinal()) sexpr = "(" + sexpr + ")";
        return String.format("%s'",sexpr);
    }

    @Override
    public StringBuilder toStringQuick() {
        StringBuilder sexpr = term.toStringQuick();
        if (term.getPriority().ordinal() < getPriority().ordinal()) sexpr.insert(0, '(').append(')');
        return sexpr.append("'");
    }

    @Override
    public void getFreeVariables(Set<String> variables, Set<String> notFree) {
        term.getFreeVariables(variables, notFree);
    }

    @Override
    public Term replaceFreeVariables(Map<String, Term> rep, Set<String> notFree) {
        return new Successor(term.replaceFreeVariables(rep, notFree));
    }

}
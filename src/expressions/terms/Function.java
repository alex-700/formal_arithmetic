package expressions.terms;

import verifiers.VerifierException;

import java.util.*;

public class Function implements Term {
    String name;
    List<Term> terms;

    public Function(String name, List<Term> terms) {
        this.name = name;
        this.terms = terms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Function)) return false;

        Function function = (Function) o;

        if (!name.equals(function.name)) return false;
        if (!terms.equals(function.terms)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + terms.hashCode();
        return result;
    }

    @Override
    public boolean almostEquals(Term term, String variableName, Term[] newName, HashSet<String> quantifier) throws VerifierException {
        if (!(getOperatorName().equals(term.getOperatorName())
                && name.equals(((Function) term).name)
                && (terms.size() == ((Function) term).terms.size()))) {
            return false;
        } else {
            boolean ok = true;
            Function pExpr = (Function) term;
            for (int i = 0; i < terms.size(); i++) {
                if (!terms.get(i).almostEquals(pExpr.terms.get(i), variableName, newName, quantifier)) {
                    ok = false;
                    break;
                }
            }
            return ok;
        }
    }

    @Override
    public String getOperatorName() {
        return Function.class.getSimpleName();
    }

    @Override
    public boolean free(HashSet<String> quantifiers) {
        boolean ok = true;
        for (Term term : terms) {
            if (!term.free(quantifiers)) {
                ok = false;
                break;
            }
        }
        return ok;
    }

    @Override
    public boolean hasVariable(String variableName) {
        boolean ok = false;
        for (Term term : terms) {
            if (term.hasVariable(variableName)) {
                ok = true;
                break;
            }
        }
        return ok;
    }

    @Override
    public Priority getPriority() {
        return Priority.Function;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < terms.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(terms.get(i).toString());
        }
        return String.format("%s(%s)", name, sb);
    }

    @Override
    public StringBuilder toStringQuick() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < terms.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(terms.get(i).toStringQuick());
        }
        return sb.insert(0, name + '(').append(')');
    }

    @Override
    public void getFreeVariables(Set<String> variables, Set<String> notFree) {
        for (Term term : terms) {
            term.getFreeVariables(variables, notFree);
        }
    }

    @Override
    public Term replaceFreeVariables(Map<String, Term> rep, Set<String> notFree) {
        List<Term> newTerm = new ArrayList<>();
        for (Term term : terms) {
            newTerm.add(term.replaceFreeVariables(rep, notFree));
        }
        return new Function(name, newTerm);
    }
}
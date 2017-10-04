package expressions.logic;

import expressions.terms.Term;
import verifiers.VerifierException;

import java.util.*;

public class Predicate implements Expression {
    String name;
    List<Term> terms;

    public Predicate(String name, List<Term> terms) {
        this.name = name;
        this.terms = terms;
    }

    public Predicate(String name) {
        this.name = name;
        terms = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Predicate)) return false;

        Predicate predicate = (Predicate) o;

        return name.equals(predicate.name) && !(terms != null ? !terms.equals(predicate.terms) : predicate.terms != null);

    }

    @Override
    public String toString() {
        if (terms == null) {
            return name;
        } else if (name.equals("=")) {
            return String.format("%s=%s", terms.get(0).toString(), terms.get(1).toString());
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < terms.size(); i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(terms.get(i).toString());
            }
            return String.format("%s(%s)", name, sb);
        }
    }

    @Override
    public StringBuilder toStringQuick() {
        if (terms == null) {
            return new StringBuilder(name);
        } else if (name.equals("=")) {
            return terms.get(0).toStringQuick().append("=").append(terms.get(1).toStringQuick());
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < terms.size(); i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(terms.get(i).toStringQuick());
            }
            return sb.insert(0, name + '(').append(')');
        }
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (terms != null ? terms.hashCode() : 0);
        return result;
    }

    @Override
    public boolean matches(Expression expression, Map<String, Expression> map) {
        if (!map.containsKey(name)) {
            map.put(name, expression);
            return true;
        } else return map.get(name).equals(expression);
    }

    @Override
    public String getOperatorName() {
        return Predicate.class.getSimpleName();
    }

    @Override
    public boolean almostEquals(Expression expression, String variableName, Term[] newVariableName, HashSet<String> quantifiers) throws VerifierException {
        if (!(getOperatorName().equals(expression.getOperatorName())
                && name.equals(((Predicate) expression).name)
                && (terms.size() == ((Predicate) expression).terms.size()))) {
            return false;
        } else {
            boolean ok = true;
            Predicate pExpr = (Predicate) expression;
            for (int i = 0; i < terms.size(); i++) {
                if (!terms.get(i).almostEquals(pExpr.terms.get(i), variableName, newVariableName, quantifiers)) {
                    ok = false;
                    break;
                }
            }
            return ok;
        }
    }

    @Override
    public boolean hasFree(String variableName) {
        if (terms == null) return false;
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
        return Priority.Predicate;
    }

    @Override
    public Expression fillPredicate(HashMap<String, Expression> hashMap) {
        return hashMap.get(name);
    }

    @Override
    public void getFreeVariables(Set<String> variables, Set<String> notFree) {
        for (Term term : terms) {
            term.getFreeVariables(variables, notFree);
        }
    }

    @Override
    public Expression replaceFreeVariables(Map<String, Term> rep, Set<String> notFree) {
        List<Term> newTerms = new ArrayList<>();
        for (Term term : terms) {
            newTerms.add(term.replaceFreeVariables(rep, notFree));
        }
        return new Predicate(name, newTerms);
    }

    public String getName() {
        return name;
    }

    public List<Term> getTerms() {
        return terms;
    }
}
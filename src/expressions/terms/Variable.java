package expressions.terms;

import verifiers.VerifierException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Variable implements Term {
    String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variable)) return false;

        Variable variable = (Variable) o;

        if (!name.equals(variable.name)) return false;

        return true;
    }


    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean almostEquals(Term term, String variableName, Term[] newName, HashSet<String> quantifier) throws VerifierException {
        if (name.equals(variableName)) {
            if (newName[0] == null) {
                newName[0] = term;
                boolean ok = term.free(quantifier);
                if (!ok) throw new VerifierException(String.format("Терм %s не свободен для подстановки  в формулу ... вместо переменной %s",term.toString(), variableName));
                return true;
            } else {
                if (newName[0].equals(term)) {
                    boolean ok = term.free(quantifier);
                    if (!ok) throw new VerifierException(String.format("Терм %s не свободен для подстановки  в формулу ... вместо переменной %s",term.toString(), variableName));
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return getOperatorName().equals(term.getOperatorName()) && name.equals(((Variable)term).name);
        }
    }

    @Override
    public String getOperatorName() {
        return Variable.class.getSimpleName();
    }

    @Override
    public boolean free(HashSet<String> quantifiers) {
        return !quantifiers.contains(name);
    }

    @Override
    public boolean hasVariable(String variableName) {
        return variableName.equals(name);
    }

    @Override
    public Priority getPriority() {
        return Priority.Variable;
    }

    @Override
    public StringBuilder toStringQuick() {
        return new StringBuilder(name);
    }

    @Override
    public void getFreeVariables(Set<String> variables, Set<String> notFree) {
        if (!notFree.contains(name)) variables.add(name);
    }

    @Override
    public Term replaceFreeVariables(Map<String, Term> rep, Set<String> notFree) {
        if (notFree.contains(name) || !rep.containsKey(name)) {
            return this;
        } else {
            return rep.get(name);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
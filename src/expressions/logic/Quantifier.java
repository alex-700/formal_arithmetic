package expressions.logic;

import expressions.terms.Term;
import verifiers.VerifierException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Quantifier implements Expression {
    public String variable;
    public Expression expression;
    String name, symbol;

    public Quantifier(String variable, Expression expression, String name, String symbol) {
        this.variable = variable;
        this.expression = expression;
        this.name = name;
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantifier)) return false;

        Quantifier that = (Quantifier) o;

        if (!expression.equals(that.expression)) return false;
        if (!name.equals(that.name)) return false;
        if (!symbol.equals(that.symbol)) return false;
        if (!variable.equals(that.variable)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = variable.hashCode();
        result = 31 * result + expression.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + symbol.hashCode();
        return result;
    }

    @Override
    public boolean matches(Expression expression, Map<String, Expression> map) {
        return false;
    }

    @Override
    public String getOperatorName() {
        return name;
    }

    @Override
    public boolean almostEquals(Expression expr, String variableName, Term[] newVariableName, HashSet<String> quantifiers) throws VerifierException {
        if (getOperatorName().equals(expr.getOperatorName()) && (((Quantifier) expr).variable).equals(variable)) {
            if (variableName.equals(variable)) {
                return this.equals(expr);
            } else {
                quantifiers.add(variable);
                boolean ans = expression.almostEquals(((Quantifier) expr).expression, variableName, newVariableName, quantifiers);
                quantifiers.remove(variable);
                return ans;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean hasFree(String variableName) {
        return !variableName.equals(variable) && expression.hasFree(variableName);
    }

    @Override
    public Priority getPriority() {
        return Priority.Quantifier;
    }

    @Override
    public String toString() {
        String sexpr = expression.toString();
        //if (expression.getPriority().ordinal() < getPriority().ordinal()) sexpr = "(" + sexpr + ")";
        return String.format("%s%s(%s)", symbol, variable, sexpr);
    }

    @Override
    public StringBuilder toStringQuick() {
        StringBuilder sexpr = expression.toStringQuick();
        /*if (expression.getPriority().ordinal() < getPriority().ordinal())*/ sexpr.insert(0, "(").append(")");
        return sexpr.insert(0, symbol + variable);
    }

    @Override
    public void getFreeVariables(Set<String> variables, Set<String> notFree) {
        notFree.add(variable);
        expression.getFreeVariables(variables, notFree);
        notFree.remove(variable);
    }
}
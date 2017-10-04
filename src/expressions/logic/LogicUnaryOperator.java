package expressions.logic;

import expressions.terms.Term;
import verifiers.VerifierException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class LogicUnaryOperator implements Expression {
    Expression expression;
    String operatorName, symbol;

    public LogicUnaryOperator(Expression expression, String operatorName, String symbol) {
        this.expression = expression;
        this.operatorName = operatorName;
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogicUnaryOperator)) return false;

        LogicUnaryOperator that = (LogicUnaryOperator) o;

        if (!expression.equals(that.expression)) return false;
        if (!operatorName.equals(that.operatorName)) return false;
        if (!symbol.equals(that.symbol)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = expression.hashCode();
        result = 31 * result + operatorName.hashCode();
        result = 31 * result + symbol.hashCode();
        return result;
    }

    @Override
    public boolean matches(Expression expr, Map<String, Expression> map) {

        return getOperatorName().equals(expr.getOperatorName())
                && expression.matches(((LogicUnaryOperator) expr).expression, map);
    }

    @Override
    public String getOperatorName() {
        return operatorName;
    }

    @Override
    public boolean almostEquals(Expression expr, String variableName, Term[] newVariableName, HashSet<String> quantifiers) throws VerifierException {
        return getOperatorName().equals(expr.getOperatorName())
                && expression.almostEquals(((LogicUnaryOperator)expr).expression, variableName, newVariableName, quantifiers);
    }

    @Override
    public boolean hasFree(String variableName) {
        return expression.hasFree(variableName);
    }

    @Override
    public Priority getPriority() {
        return Priority.valueOf(operatorName);
    }

    @Override
    public String toString() {
        String sexpr = expression.toString();
        if (expression.getPriority().ordinal() < getPriority().ordinal()) sexpr = "(" + sexpr + ")";
        return symbol + sexpr;
    }

    @Override
    public StringBuilder toStringQuick() {
        StringBuilder sexpr = expression.toStringQuick();
        if (expression.getPriority().ordinal() < getPriority().ordinal()) sexpr.insert(0, "(").append(")");
        return sexpr.insert(0, symbol);
    }

    @Override
    public void getFreeVariables(Set<String> variables, Set<String> notFree) {
        expression.getFreeVariables(variables, notFree);
    }
}
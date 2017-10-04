package expressions.logic;

import expressions.terms.Term;
import verifiers.VerifierException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class LogicBinaryOperator implements Expression {
    public Expression left;
    public Expression right;
    String operatorName, symbol;

    public LogicBinaryOperator(Expression left, Expression right, String operatorName, String symbol) {
        this.left = left;
        this.right = right;
        this.operatorName = operatorName;
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogicBinaryOperator)) return false;

        LogicBinaryOperator that = (LogicBinaryOperator) o;

        if (!left.equals(that.left)) return false;
        if (!operatorName.equals(that.operatorName)) return false;
        if (!right.equals(that.right)) return false;
        if (!symbol.equals(that.symbol)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = left.hashCode();
        result = 31 * result + right.hashCode();
        result = 31 * result + operatorName.hashCode();
        result = 31 * result + symbol.hashCode();
        return result;
    }

    @Override
    public boolean matches(Expression expression, Map<String, Expression> map) {
        return operatorName.equals(expression.getOperatorName())
                && left.matches(((LogicBinaryOperator) expression).left, map)
                && right.matches(((LogicBinaryOperator) expression).right, map);
    }

    @Override
    public String getOperatorName() {
        return operatorName;
    }

    @Override
    public boolean almostEquals(Expression expression, String variableName, Term[] newVariableName, HashSet<String> quantifiers) throws VerifierException {
        return getOperatorName().equals(expression.getOperatorName())
                && left.almostEquals(((LogicBinaryOperator) expression).left, variableName, newVariableName, quantifiers)
                && right.almostEquals(((LogicBinaryOperator) expression).right, variableName, newVariableName, quantifiers);
    }

    @Override
    public boolean hasFree(String variableName) {
        return left.hasFree(variableName) || right.hasFree(variableName);
    }

    @Override
    public Priority getPriority() {
        return Priority.valueOf(operatorName);
    }

    @Override
    public String toString() {
        String sleft = left.toString(), sright = right.toString();
        if (left.getPriority().ordinal() <= getPriority().ordinal()) sleft = "(" + sleft + ")";
        if (right.getPriority().ordinal() <= getPriority().ordinal()) sright = "(" + sright + ")";
        return String.format("%s%s%s", sleft, symbol, sright);
    }

    @Override
    public StringBuilder toStringQuick() {
        StringBuilder sleft = left.toStringQuick(), sright = right.toStringQuick();
        if (left.getPriority().ordinal() <= getPriority().ordinal()) sleft.insert(0, "(").append(")");
        if (right.getPriority().ordinal() <= getPriority().ordinal()) sright.insert(0, "(").append(")");
        return sleft.append(symbol).append(sright);
    }

    @Override
    public void getFreeVariables(Set<String> variables, Set<String> notFree) {
        left.getFreeVariables(variables, notFree);
        right.getFreeVariables(variables, notFree);
    }

}
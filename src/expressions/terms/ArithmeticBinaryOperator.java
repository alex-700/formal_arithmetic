package expressions.terms;

import verifiers.VerifierException;

import java.util.HashSet;
import java.util.Set;

public abstract class ArithmeticBinaryOperator implements Term {
    public Term left;
    public Term right;
    String operatorName, symbol;

    public ArithmeticBinaryOperator(Term left, Term right, String operatorName, String symbol) {
        this.left = left;
        this.right = right;
        this.operatorName = operatorName;
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArithmeticBinaryOperator)) return false;

        ArithmeticBinaryOperator that = (ArithmeticBinaryOperator) o;

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
    public boolean almostEquals(Term term, String variableName, Term[] newName, HashSet<String> quantifier) throws VerifierException {
        return getOperatorName().equals(term.getOperatorName())
                && left.almostEquals((((ArithmeticBinaryOperator) term).left), variableName, newName, quantifier)
                && right.almostEquals((((ArithmeticBinaryOperator) term).right), variableName, newName, quantifier);
    }

    @Override
    public String getOperatorName() {
        return operatorName;
    }

    @Override
    public boolean free(HashSet<String> quantifiers) {
        return left.free(quantifiers) && right.free(quantifiers);
    }

    @Override
    public boolean hasVariable(String variableName) {
        return left.hasVariable(variableName) || right.hasVariable(variableName);
    }

    @Override
    public Priority getPriority() {
        return Priority.valueOf(operatorName);
    }

    @Override
    public String toString() {
        String sleft = left.toString(), sright = right.toString();
        if (left.getPriority().ordinal() < getPriority().ordinal()) sleft = "(" + sleft + ")";
        if (right.getPriority().ordinal() <= getPriority().ordinal()) sright = "(" + sright + ")";
        return String.format("%s%s%s", sleft, symbol, sright);
    }

    @Override
    public StringBuilder toStringQuick() {
        StringBuilder sleft = left.toStringQuick(), sright = right.toStringQuick();
        if (left.getPriority().ordinal() < getPriority().ordinal()) sleft.insert(0, '(').append(')');
        if (right.getPriority().ordinal() <= getPriority().ordinal()) sright.insert(0,'(').append(')');
        return sleft.append(symbol).append(sright);
    }

    @Override
    public void getFreeVariables(Set<String> variables, Set<String> notFree) {
        left.getFreeVariables(variables, notFree);
        right.getFreeVariables(variables, notFree);
    }

}
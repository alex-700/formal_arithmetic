package proofs;

import expressions.logic.Expression;

public class Statement {
    private Expression expression;
    private StatementType type;

    public Statement(Expression expression, StatementType type) {
        this.expression = expression;
        this.type = type;
    }

    @Override
    public String toString() {
        return expression.toString() + " " + type.toString();
    }

    public StringBuilder toStringQuick() { return expression.toStringQuick().append(" ").append(type); }

    public Expression getExpression() {
        return expression;
    }

    public StatementType getType() {
        return type;
    }
}
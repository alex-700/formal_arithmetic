package proofs.assumptions;

import expressions.logic.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assumptions {
    public List<Expression> expressions;
    public Map<Expression, Integer> expressionsMap;

    public Assumptions() {
        expressions = new ArrayList<Expression>();
        expressionsMap = new HashMap<Expression, Integer>();
    }

    public void add(Expression expr) {
        expressions.add(expr);
        expressionsMap.put(expr, expressions.size() - 1);
    }

    public Assumption check(Expression expression) {
        if (expressionsMap.containsKey(expression)) {
            return new Assumption(expressionsMap.get(expression));
        } else {
            return null;
        }
    }
}
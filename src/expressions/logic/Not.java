package expressions.logic;

import expressions.terms.Term;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Not extends LogicUnaryOperator {
    public Not(Expression expr) {
        super(expr, Not.class.getSimpleName(), "!");
    }

    @Override
    public Expression fillPredicate(HashMap<String, Expression> hashMap) {
        return new Not(expression.fillPredicate(hashMap));
    }

    @Override
    public Expression replaceFreeVariables(Map<String, Term> rep, Set<String> notFree) {
        return new Not(expression.replaceFreeVariables(rep, notFree));
    }
}
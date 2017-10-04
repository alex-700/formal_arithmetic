package expressions.logic;

import expressions.terms.Term;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Or extends LogicBinaryOperator {
    public Or(Expression left, Expression right) {
        super(left, right, Or.class.getSimpleName(), "|");
    }

    @Override
    public Expression fillPredicate(HashMap<String, Expression> hashMap) {
        return new Or(left.fillPredicate(hashMap), right.fillPredicate(hashMap));
    }

    @Override
    public Expression replaceFreeVariables(Map<String, Term> rep, Set<String> notFree) {
        return new Or(left.replaceFreeVariables(rep, notFree), right.replaceFreeVariables(rep, notFree));
    }
}
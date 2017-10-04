package expressions.logic;

import expressions.terms.Term;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class And extends LogicBinaryOperator  {
    public And(Expression left, Expression right) {
        super(left, right, And.class.getSimpleName(), "&");
    }

    @Override
    public Expression fillPredicate(HashMap<String, Expression> hashMap) {
        return new And(left.fillPredicate(hashMap), right.fillPredicate(hashMap));
    }

    @Override
    public Expression replaceFreeVariables(Map<String, Term> rep, Set<String> notFree) {
        return new And(left.replaceFreeVariables(rep, notFree), right.replaceFreeVariables(rep, notFree));
    }
}
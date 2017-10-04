package expressions.logic;

import expressions.terms.Term;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Entailment extends LogicBinaryOperator {
    public Entailment(Expression left, Expression right) {
        super(left, right, Entailment.class.getSimpleName(), "->");
    }

    @Override
    public Expression fillPredicate(HashMap<String, Expression> hashMap) {
        return new Entailment(left.fillPredicate(hashMap), right.fillPredicate(hashMap));
    }

    @Override
    public Expression replaceFreeVariables(Map<String, Term> rep, Set<String> notFree) {
        return new Entailment(left.replaceFreeVariables(rep, notFree), right.replaceFreeVariables(rep, notFree));
    }


}
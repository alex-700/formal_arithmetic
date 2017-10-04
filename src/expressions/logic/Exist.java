package expressions.logic;

import expressions.terms.Term;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Exist extends Quantifier {
    public Exist(String v, Expression expr) {
        super(v, expr, Exist.class.getSimpleName(), "?");
    }


    @Override
    public Expression fillPredicate(HashMap<String, Expression> hashMap) {
        return new Exist(variable, expression.fillPredicate(hashMap));
    }

    @Override
    public Expression replaceFreeVariables(Map<String, Term> rep, Set<String> notFree) {
        notFree.add(variable);
        Expression ret = new Exist(variable, expression.replaceFreeVariables(rep, notFree));
        notFree.remove(variable);
        return ret;
    }
}
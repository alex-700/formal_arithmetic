package expressions.logic;

import expressions.terms.Term;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class All extends Quantifier {
    public All(String v, Expression expr) {
        super(v, expr, All.class.getSimpleName(), "@");
    }

    @Override
    public Expression fillPredicate(HashMap<String, Expression> hashMap) {
        return new All(variable, expression.fillPredicate(hashMap));
    }

    @Override
    public Expression replaceFreeVariables(Map<String, Term> rep, Set<String> notFree) {
        notFree.add(variable);
        Expression ret = new All(variable, expression.replaceFreeVariables(rep, notFree));
        notFree.remove(variable);
        return ret;
    }
}
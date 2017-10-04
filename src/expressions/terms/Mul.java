package expressions.terms;

import java.util.Map;
import java.util.Set;

public class Mul extends ArithmeticBinaryOperator {
    public Mul(Term left, Term right) {
        super(left, right, Mul.class.getSimpleName(), "*");
    }


    @Override
    public Term replaceFreeVariables(Map<String, Term> rep, Set<String> notFree) {
        return new Mul(left.replaceFreeVariables(rep, notFree), right.replaceFreeVariables(rep, notFree));
    }
}
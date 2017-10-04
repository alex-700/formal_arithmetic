package expressions.terms;

import java.util.Map;
import java.util.Set;

public class Sum extends ArithmeticBinaryOperator {
    public Sum(Term left, Term right) {
        super(left, right, Sum.class.getSimpleName(), "+");
    }

    @Override
    public Term replaceFreeVariables(Map<String, Term> rep, Set<String> notFree) {
        return new Sum(left.replaceFreeVariables(rep, notFree), right.replaceFreeVariables(rep, notFree));
    }
}
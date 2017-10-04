package proofs;

import proofs.derivation_rules.AllRule;
import proofs.derivation_rules.ExistRule;
import proofs.derivation_rules.ModusPonens;
import verifiers.VerifierException;

import java.util.*;

public class ProofOptimizer {
    public static Proof optimize(Proof proof) throws VerifierException {
        Queue<Statement> queue = new ArrayDeque<>();
        SortedSet<Integer> numbers = new TreeSet<>();

        List<Statement> statements = proof.getStatements();
        queue.add(statements.get(statements.size() - 1));
        numbers.add(statements.size() - 1);
        while (!queue.isEmpty()) {
            Statement cur = queue.poll();
            if (cur.getType() instanceof AllRule) {
                if (numbers.add(((AllRule) cur.getType()).number)) {
                    queue.add(statements.get(((AllRule) cur.getType()).number));
                }
            } else if (cur.getType() instanceof ExistRule) {
                if (numbers.add(((ExistRule) cur.getType()).number)) {
                    queue.add(statements.get(((AllRule) cur.getType()).number));
                }
            } else if (cur.getType() instanceof ModusPonens) {
                if (numbers.add(((ModusPonens) cur.getType()).getFirst())) {
                    queue.add(statements.get(((ModusPonens) cur.getType()).getFirst()));
                }
                if (numbers.add(((ModusPonens) cur.getType()).getSecond())) {
                    queue.add(statements.get(((ModusPonens) cur.getType()).getSecond()));
                }
            }
        }
        Proof ans = new Proof();
        for (int x : numbers) {
            ans.addExpression(statements.get(x).getExpression());
        }
        return ans;
    }

}
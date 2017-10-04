package proofs.derivation_rules;

public class AllRule extends DerivationRule {
    public int number;

    public AllRule(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("(Правило вывода всеобщности для %d)", number + 1);
    }
}
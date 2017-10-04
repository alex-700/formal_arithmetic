package proofs.derivation_rules;

public class ExistRule extends DerivationRule {
    public int number;

    public ExistRule(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("(Правило вывода существования для %d)", number + 1);
    }
}
package proofs.derivation_rules;

public class ModusPonens extends DerivationRule {
    private int first, second;

    public ModusPonens(int first, int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return String.format("(M.P. %d %d)", first + 1, second + 1);
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }
}
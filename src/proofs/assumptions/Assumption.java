package proofs.assumptions;

import proofs.StatementType;

public class Assumption implements StatementType {
    private int number;

    public Assumption(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("(Допушение %d)", number + 1);
    }

    public int getNumber() {
        return number;
    }
}
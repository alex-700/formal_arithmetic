package main;

import divisions.DivisionProver;
import proofs.ProofOptimizer;
import verifiers.VerifierException;

import java.io.File;
import java.io.FileNotFoundException;

public class Task7 {
    public static void main(String[] args) throws VerifierException, FileNotFoundException {
        ProofOptimizer.optimize(DivisionProver.prove(8 * 8, 8)).printQuick(new File("output.txt"));
    }


}
package main;

import deductors.Deductor;
import deductors.DeductorException;
import parsers.ParserException;
import proofs.Proof;
import verifiers.Verifier;
import verifiers.VerifierException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Task4 {

    private static long start;

    public static void stop(String name) {
        long finish = System.currentTimeMillis();
        System.out.println(name + " " + (finish - start) + " ms");
        System.out.flush();
    }

    private static void start() {
        start = System.currentTimeMillis();
    }

    public static void main(String[] args) throws FileNotFoundException, ParserException, DeductorException, VerifierException {
        String input = args.length <= 0 ? "input.txt" : args[0];
        String output = args.length <= 1 ? "output.txt" : args[1];
        try {
            start();
            Proof proof = Deductor.deduct(Verifier.verify(new File(input)));
            stop("Deduction");
            start();
            proof.printQuick(new File(output));
            stop("Print");
        } catch (VerifierException | DeductorException e) {
            PrintWriter out = new PrintWriter(output);
            out.println(e.getMessage());
            out.close();
        }
    }

}
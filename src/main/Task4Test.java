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

public class Task4Test {
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
        File dir = new File("HW4");
        String[] s = new String[0];
        if (dir.exists() && dir.isDirectory()) {
            s = dir.list();
        }
        File dirAns = new File("HW4ans");
        if (!dirAns.exists()) {
            if (!dirAns.mkdir()) {
                System.out.println("All bad");
                return;
            }
        }
        for (String file : s) {
            System.out.println("test= '" + file + "'");
            try {
                start();
                Proof proof = Deductor.deduct(Verifier.verify(new File(dir + "/" + file)));
                stop("Deduction");
                start();
                proof.printExpressions(new File("HW4ans/" + file));
                stop("Print");
            } catch (VerifierException | DeductorException e) {
                System.out.println("incorrect");
                PrintWriter out = new PrintWriter("HW4ans/"+file);
                out.println(e.getMessage());
                out.close();
            }
        }

    }

}
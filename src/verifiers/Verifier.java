package verifiers;

import parsers.ExpressionParser;
import parsers.ParserException;
import proofs.Proof;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Verifier {
    public static Proof verify(File f) throws FileNotFoundException, ParserException, VerifierException {
        Scanner in = new Scanner(f);
        Proof proof = new Proof();
        String[] header = in.nextLine().split("\\|\\-");
        List<String> assumptions = new ArrayList<>();
        header[0] += ",";
        char[] assString = header[0].toCharArray();
        int balance = 0;
        int start = 0;
        for (int i = 0; i < assString.length; i++) {
            if (assString[i] == '(') balance++;
            if (assString[i] == ')') balance--;
            if (assString[i] == ',' && balance == 0) {
                assumptions.add(header[0].substring(start, i));
                start = i + 1;
            }
        }

        for (String assumption : assumptions) {
            if (!assumption.isEmpty()) proof.addAssumption(ExpressionParser.parse(assumption));
        }
        int i = 0;
        while (in.hasNextLine()) {
            ++i;
            String s = in.nextLine();
            try {
                if (!s.isEmpty()) proof.addExpression(ExpressionParser.parse(s));
            } catch (VerifierException e) {
                throw new VerifierException(String.format("Вывод некорректен начиная с формулы номер %d %s", i, e.getMessage()));
            }
        }
        in.close();
        return proof;
    }

}
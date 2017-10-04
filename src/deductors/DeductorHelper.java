package deductors;

import expressions.logic.Entailment;
import expressions.logic.Expression;
import parsers.ExpressionParser;
import parsers.ParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DeductorHelper {

    public static List<Expression> getDeductionForMP(Expression a, Expression f, Expression s, Expression m) {
        List<Expression> list = new ArrayList<>();
        list.add(new Entailment(new Entailment (a, f), new Entailment (new Entailment (a, new Entailment (f, m)), new Entailment (a, m))));
        list.add(new Entailment (new Entailment (a, new Entailment (f, m)), new Entailment (a, m)));
        list.add(new Entailment(a, m));
        return list;
    }

    public static List<Expression> getDeductionAEA(Expression a) {
        List<Expression> list = new ArrayList<>();
        list.add(new Entailment (a, new Entailment (a, a)));
        list.add(new Entailment (new Entailment (a, new Entailment (a, a)), new Entailment (new Entailment (a, new Entailment (new Entailment (a, a), a)), new Entailment (a, a))));
        list.add(new Entailment (new Entailment (a, new Entailment (new Entailment (a, a), a)), new Entailment (a, a)));
        list.add(new Entailment (a, new Entailment (new Entailment (a, a), a)));
        list.add(new Entailment (a, a));
        return list;
    }

    public static List<Expression> getDeductionAxiom(Expression a, Expression b) {
        List<Expression> list = new ArrayList<>();
        list.add(a);
        list.add(new Entailment (a, new Entailment (b, a)));
        list.add(new Entailment (b, a));
        return list;
    }

    public static List<Expression> getDeductionExistRule(Expression alpha, Expression withQ, Expression withoutQ, Expression right) {
        HashMap<String, Expression> hashMap = new HashMap<>();
        hashMap.put("A", alpha);
        hashMap.put("B", withoutQ);
        hashMap.put("C", right);
        hashMap.put("D", withQ);
        Scanner in = null;
        try {
            in = new Scanner(new File("lemmas/existLemma"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Expression> list = new ArrayList<>();
        assert in != null;
        while (in.hasNextLine()) {
            String s = in.nextLine();
            if (!s.isEmpty()) try {
                list.add(ExpressionParser.parse(s).fillPredicate(hashMap));
            } catch (ParserException e) {
                e.printStackTrace();
            }
        }
        in.close();
        return list;
    }

    public static List<Expression> getDeductionAllRule(Expression alpha, Expression withQ, Expression withoutQ, Expression left) {
        HashMap<String, Expression> hashMap = new HashMap<>();
        hashMap.put("A", alpha);
        hashMap.put("B", left);
        hashMap.put("C", withoutQ);
        hashMap.put("D", withQ);
        Scanner in = null;
        try {
            in = new Scanner(new File("lemmas/allLemma"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Expression> list = new ArrayList<>();
        assert in != null;
        while (in.hasNextLine()) {
            String s = in.nextLine();
            if (!s.isEmpty()) try {
                list.add(ExpressionParser.parse(s).fillPredicate(hashMap));
            } catch (ParserException e) {
                e.printStackTrace();
            }
        }
        in.close();
        return list;
    }

}
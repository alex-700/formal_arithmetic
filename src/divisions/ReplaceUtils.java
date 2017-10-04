package divisions;

import expressions.logic.*;
import expressions.terms.Sum;
import expressions.terms.Term;
import expressions.terms.Variable;
import expressions.terms.Zero;
import parsers.ExpressionParser;
import parsers.ParserException;
import proofs.axioms.ArithmeticAxiom;
import proofs.Proof;
import verifiers.VerifierException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;

public class ReplaceUtils {

    private static Expression expr(String string) throws ParserException {
        return ExpressionParser.parse(string);
    }

    private static void replaceOne(int pos, List<String> variables, Map<String, Term> rep, List<Expression> answer) {
        if (pos == variables.size()) return;
        Entailment last = (Entailment) answer.get(answer.size() - 1);
        answer.add(new Entailment(last.left, new All(variables.get(pos), last.right)));
        replaceOne(pos + 1, variables, rep, answer);
        Quantifier qu = (Quantifier) ((Entailment) answer.get(answer.size() - 1)).right;
        answer.add(qu);
        Map<String, Term> map = new HashMap<>();
        map.put(variables.get(pos), rep.get(variables.get(pos)));
        answer.add(new Entailment(qu, qu.expression.replaceFreeVariables(map, new HashSet<>())));
    }

    public static List<Expression> replaceVariableInArithmeticAxiom(ArithmeticAxiom axiom, Map<String, Term> rep) {
        try {
            Scanner scanner = new Scanner(new File("divisionStaff/axioms"));
            for (int i = 0; i < axiom.ordinal(); i++) {
                scanner.nextLine();
            }
            String ex = scanner.nextLine();
            scanner.close();
            Expression truth = ExpressionParser.parse("0=0->0=0->0=0");
            List<Expression> list = new ArrayList<>();
            Set<String> variables = new HashSet<>();
            expr(ex).getFreeVariables(variables, new HashSet<>());
            list.add(expr(ex));
            list.add(truth);
            list.add(new Entailment(expr(ex), new Entailment(truth, expr(ex))));
            list.add(new Entailment(truth, expr(ex)));
            replaceOne(0, new ArrayList<>(variables), rep, list);
            list.add(((Entailment) list.get(list.size() - 1)).right);
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (ParserException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Expression> replaceVariableInArithmeticAxiom(ArithmeticAxiom axiom, Function<String, Term> function) {
        Set<String> var = new HashSet<>();
        axiom.expr.getFreeVariables(var, new HashSet<>());
        Map<String, Term> hashMap = new HashMap<>();
        for (String s : var) {
            hashMap.put(s, function.apply(s));
        }
        return replaceVariableInArithmeticAxiom(axiom, hashMap);
    }

    public static List<Expression> replaceVariableInArithmeticAxiom(ArithmeticAxiom axiom, Term... terms) {
        Map<String, Term> map = new HashMap<>();
        for (int i = 0; i < terms.length; i++) {
            map.put("" + ((char) ('a' + i)), terms[i]);
        }
        return replaceVariableInArithmeticAxiom(axiom, map);
    }

    public static void reverseEquality(Predicate predicate, Proof proof) throws VerifierException {
        List<Term> terms = predicate.getTerms();
        proof.addExpressionList(replaceVariableInArithmeticAxiom(ArithmeticAxiom.Two, terms.get(0), terms.get(1), terms.get(0)));
        proof.addExpression(((Entailment) proof.getProofedExpression()).right);
        proof.addExpressionList(proveTEqualT(terms.get(0)));
        proof.addExpression(new Predicate("=", Arrays.asList(terms.get(1), terms.get(0))));
    }

    public static List<Expression> proveTEqualT(Term term) {
        List<Expression> list = new ArrayList<>();
        list.addAll(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Six, term));
        list.addAll(ReplaceUtils.replaceVariableInArithmeticAxiom(ArithmeticAxiom.Two, new Sum(term, new Zero()), term, term));
        list.add(((Entailment) list.get(list.size() - 1)).right);
        list.add(((Entailment)list.get(list.size() - 1)).right);
        return list;
    }

    public static void replaceVariable(Proof proof, Variable var, Term term) throws VerifierException {
        Expression expression = proof.getProofedExpression();
        Expression truth = ExpressionParser.parseConst("0=0->0=0->0=0");
        proof.addExpression(new Entailment(truth, expression));
        proof.addExpression(new Entailment(truth, new All(var.toString(), expression)));
        proof.addExpression(new All(var.toString(), expression));
        Map<String, Term> map = new HashMap<>(); map.put(var.toString(), term);
        Expression expr = expression.replaceFreeVariables(map, new HashSet<>());
        proof.addExpression(new Entailment(new All(var.toString(), expression), expr));
        proof.addExpression(expr);
    }
}
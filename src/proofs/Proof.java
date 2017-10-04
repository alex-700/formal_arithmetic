package proofs;

import expressions.logic.All;
import expressions.logic.Entailment;
import expressions.logic.Exist;
import expressions.logic.Expression;
import proofs.assumptions.Assumption;
import proofs.assumptions.Assumptions;
import proofs.axioms.ArithmeticAxiom;
import proofs.axioms.LogicAxiom;
import proofs.derivation_rules.AllRule;
import proofs.derivation_rules.ExistRule;
import proofs.derivation_rules.ModusPonens;
import verifiers.VerifierException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Proof {
    private Assumptions assumptions;
    private List<Statement> statements;
    private Map<Expression, Integer> expressionsMap;
    private Map<Expression, Set<Integer>> rightMap;

    public Proof() {
        assumptions = new Assumptions();
        statements = new ArrayList<>();
        expressionsMap = new HashMap<>();
        rightMap = new HashMap<>();
    }

    public Expression getProofedExpression() {
        if (statements.size() == 0) return null;
        return statements.get(statements.size() - 1).getExpression();
    }

    public void addExpression(Expression expression) throws VerifierException {
        VerifierException exception = null;
        Assumption assumption = assumptions.check(expression);
        if (assumption != null) {
            addStatement(new Statement(expression, assumption));
            return;
        }
        for (LogicAxiom logicAxiom : LogicAxiom.values()) {
            if (logicAxiom.match(expression)) {
                addStatement(new Statement(expression, logicAxiom));
                return;
            }
        }
        for (ArithmeticAxiom arithmeticAxiom : ArithmeticAxiom.values()) {
            if (arithmeticAxiom.match(expression)) {
                addStatement(new Statement(expression, arithmeticAxiom));
                return;
            }
        }
        if (rightMap.containsKey(expression)) {
            for (int second : rightMap.get(expression)) {
                Entailment secondExpression = (Entailment) statements.get(second).getExpression();
                if (expressionsMap.containsKey(secondExpression.left)) {
                    addStatement(new Statement(expression, new ModusPonens(expressionsMap.get(secondExpression.left), second)));
                    return;
                }
            }
        }
        if (expression instanceof Entailment) {
            Expression left = ((Entailment) expression).left;
            Expression right = ((Entailment) expression).right;
            if (right instanceof All) {
                Expression right2 = ((All) right).expression;
                String var = ((All) right).variable;
                Expression expr = new Entailment(left, right2);
                if (expressionsMap.containsKey(expr)) {
                    if (left.hasFree(var)) {
                        exception = new VerifierException(String.format(": переменная %s входит свободно в формулу %s", var, left.toStringQuick()));
                    } else {
                        addStatement(new Statement(expression, new AllRule(expressionsMap.get(expr))));
                        return;
                    }
                }
            }
            if (left instanceof Exist) {
                Expression left2 = ((Exist) left).expression;
                String var = ((Exist) left).variable;
                Expression expr = new Entailment(left2, right);
                if (expressionsMap.containsKey(expr)) {
                    if (right.hasFree(var)) {
                        exception = new VerifierException(String.format(": переменная %s входит свободно в формулу %s", var, right.toStringQuick()));
                    } else {
                        addStatement(new Statement(expression, new ExistRule(expressionsMap.get(expr))));
                        return;
                    }
                }
            }
        }
        if (exception != null) {
            throw exception;
        } else {
            throw new VerifierException("");
        }
        //addStatement(new Statement(expression, new Error()));
    }

    public void addExpressionList(List<Expression> expressions) throws VerifierException {
        for (Expression expr : expressions) {
            addExpression(expr);
        }
    }

    private void addStatement(Statement statement) {
        statements.add(statement);
        if (!(statement.getType() instanceof Error)) {
            if (!expressionsMap.containsKey(statement.getExpression()))
                expressionsMap.put(statement.getExpression(), statements.size() - 1);
            if (statement.getExpression() instanceof Entailment) {
                if (!rightMap.containsKey(((Entailment) statement.getExpression()).right)) {
                    rightMap.put(((Entailment) statement.getExpression()).right, new TreeSet<Integer>());
                }
                rightMap.get(((Entailment) statement.getExpression()).right).add(statements.size() - 1);
            }
        }
    }

    public String getHeader() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < assumptions.expressions.size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(assumptions.expressions.get(i));
        }
        sb.append("|-");
        sb.append(getProofedExpression());
        return sb.toString();
    }

    @Deprecated
    public void print(File f) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(f);
        out.println(getHeader());
        for (int i = 0; i < statements.size(); i++) {
            out.format("(%d) %s\n", i + 1, statements.get(i));
        }
        out.flush();
        out.close();
    }

    public void printQuick(File f) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(f);
        out.println(getHeader());
        for (int i = 0; i < statements.size(); i++) {
            out.format("(%d) %s\n", i + 1, statements.get(i).toStringQuick());
        }
        out.flush();
        out.close();
    }

    public void printExpressions(File f) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(f);
        out.println(getHeader());
        for (Statement st : statements) {
            out.println(st.getExpression().toStringQuick());
        }
        out.close();
    }

    public void addAssumption(Expression expression) {
        assumptions.add(expression);
    }

    public Expression getLastAssumption() {
        return assumptions.expressions.get(assumptions.expressions.size() - 1);
    }

    public Assumptions getAssumptions() {
        return assumptions;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public void merge(Proof other) throws VerifierException {
        for (Statement statement : other.getStatements()) {
            addExpression(statement.getExpression());
        }
    }
}
package parsers;

import expressions.logic.*;
import expressions.terms.*;

import java.util.ArrayList;
import java.util.List;

public class ExpressionParser {
    private static char[] s = null;
    private static int index = 0;

    public static Expression parse(String s) throws ParserException {
        ExpressionParser.s = (s + "#").toCharArray();
        index = 0;
        Expression expression = parseEntailment();
        if (getChar() != '#') {
            throw new ParserException(s + " hahaha");
        }
        return expression;
    }

    public static Expression parseConst(String s) {
        try {
            return parse(s);
        } catch (ParserException e) {
            return null;
        }
    }

    private static char getChar() {
        return s[index++];
    }

    private static void returnChar() {
        index--;
    }

    private static void skipSpace() {
        while (s[index] == ' ') {
            index++;
        }
    }

    private static Expression parseEntailment() throws ParserException {
        Expression left = parseOr(parseAnd(parseUnary()));
        skipSpace();
        char c = getChar();
        if (c == '-') {
            char c1 = getChar();
            if (c1 != '>') {
                throw new ParserException("Expected '>' after '-' at " + index + " symbol");
            }
            return new Entailment(left, parseEntailment());
        }
        returnChar();
        return left;
    }

    private static Expression parseOr(Expression left) throws ParserException {
        skipSpace();
        char c = getChar();
        if (c == '|') {
            Expression right = parseAnd(parseUnary());
            return parseOr(new Or(left, right));
        }
        returnChar();
        return left;
    }

    private static Expression parseAnd(Expression left) throws ParserException {
        skipSpace();
        char c = getChar();
        if (c == '&') {
            Expression right = parseUnary();
            return parseAnd(new And(left, right));
        }
        returnChar();
        return left;
    }

    private static Expression parseUnary() throws ParserException {
        skipSpace();
        char c = getChar();
        switch (c) {
            case '!':
                return new Not(parseUnary());
            case '@':
                String v = parseVariableName();
                return new All(v, parseUnary());
            case '?':
                v = parseVariableName();
                return new Exist(v, parseUnary());
            case '(':
                Expression expression;
                int current = index;
                try {
                    expression = parseEntailment();
                    char c1 = getChar();
                    if (c1 != ')') {
                        throw new ParserException("Expected close bracket at " + index + " symbol");
                    }
                } catch (ParserException e) {
                    index = current;
                    returnChar();
                    expression = parsePredicate();
                }

                return expression;
        }
        returnChar();
        return parsePredicate();
    }

    private static String parseVariableName() throws ParserException {
        skipSpace();
        char c = getChar();
        if ('a' <= c && c <= 'z') {
            StringBuilder sb = new StringBuilder();
            sb.append(c);
            c = getChar();
            while ('0' <= c && c <= '9') {
                sb.append(c);
                c = getChar();
            }
            returnChar();
            return sb.toString();
        } else {
            throw new ParserException("Expected lowercase letter at " + index + " symbol");
        }
    }

    private static String parsePredicateName() throws ParserException {
        skipSpace();
        char c = getChar();
        if ('A' <= c && c <= 'Z') {
            StringBuilder sb = new StringBuilder();
            sb.append(c);
            c = getChar();
            while ('0' <= c && c <= '9') {
                sb.append(c);
                c = getChar();
            }
            returnChar();
            return sb.toString();
        } else {
            throw new ParserException("Expected uppercase letter at " + index + " symbol");
        }
    }

    private static Expression parsePredicate() throws ParserException {
        char c = getChar();
        if ('a' <= c && c <= 'z' || c == '(' || c == '0') {
            returnChar();
            Term left = parseTerm(parseMul(parseFactor()));
            skipSpace();
            char c1 = getChar();
            if (c1 != '=') {
                throw new ParserException("Expected '=' after term at " + index + " symbol");
            }
            Term right = parseTerm(parseMul(parseFactor()));
            List<Term> list = new ArrayList<>();
            list.add(left);
            list.add(right);
            return new Predicate("=", list);
        }
        if ('A' <= c && c <= 'Z') {
            returnChar();
            String name = parsePredicateName();
            skipSpace();
            if (getChar() == '(') {
                List<Term> list = new ArrayList<>();
                do {
                    list.add(parseTerm(parseMul(parseFactor())));
                    skipSpace();
                    c = getChar();
                } while (c == ',');
                if (c != ')') throw new ParserException("Expected close bracket at " + index + " symbol");
                return new Predicate(name, list);
            } else {
                returnChar();
                return new Predicate(name);
            }
        }
        throw new ParserException("Expected predicate at " + index + " symbol");
    }

    private static Term parseTerm(Term left) throws ParserException {
        skipSpace();
        char c = getChar();
        if (c == '+') {
            Term right = parseMul(parseFactor());
            return parseTerm(new Sum(left, right));
        }
        returnChar();
        return left;
    }

    private static Term parseMul(Term left) throws ParserException {
        skipSpace();
        char c = getChar();
        if (c == '*') {
            Term right = parseFactor();
            return parseMul(new Mul(left, right));
        }
        returnChar();
        return left;
    }

    private static Term parseFactor() throws ParserException {
        skipSpace();
        char c = getChar();
        Term term = null;
        if ('a' <= c && c <= 'z') {
            returnChar();
            String name = parseVariableName();
            skipSpace();
            c = getChar();
            if (c == '(') {
                List<Term> list = new ArrayList<>();
                do {
                    skipSpace();
                    list.add(parseTerm(parseMul(parseFactor())));
                    skipSpace();
                    c = getChar();
                } while (c == ',');
                if (c != ')') throw new ParserException("Expected close bracket at " + index + " symbol");
                term = new Function(name, list);
            } else {
                returnChar();
                term = new Variable(name);
            }
        } else if (c == '(') {
            term = parseTerm(parseMul(parseFactor()));
            skipSpace();
            if (getChar() != ')') throw new ParserException("Expected close bracket at " + index + " symbol");
        } else if (c == '0') {
            term = new Zero();
        }
        if (term == null) throw new ParserException("Unknown parsing error");

        skipSpace();
        c = getChar();
        if (c == '\'') {
            int count = 0;
            for (; c == '\''; c = getChar(), count++) ;
            /*
            returnChar();
            if (term instanceof Successors) {
                ((Successors) term).addCount(count);
                return term;
            } else {
                return new Successors(count, term);
            }*/
            for (int i = 0; i < count; i++) {
                term = new Successor(term);
            }
        }
        returnChar();
        return term;
    }

}
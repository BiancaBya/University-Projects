package com.lftc;

import com.lftc.simbols.CustomException;
import com.lftc.simbols.Element;
import com.lftc.stateMachine.Parser;
import com.lftc.stateMachine.StateMachine;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Token {

    private static final String identifiersPath = "C:\\Users\\Next\\Desktop\\LFTC\\Lab1\\Lab1\\src\\main\\resources\\identifiers.txt";
    private static final StateMachine identifiersMachine = Parser.FileParser(identifiersPath);

    private static final String integersPath = "C:\\Users\\Next\\Desktop\\LFTC\\Lab1\\Lab1\\src\\main\\resources\\integers.txt";
    private static final StateMachine integersMachine = Parser.FileParser(integersPath);

    private static final String realsPath = "C:\\Users\\Next\\Desktop\\LFTC\\Lab1\\Lab1\\src\\main\\resources\\realNumbers.txt";
    private static final StateMachine realsMachine = Parser.FileParser(realsPath);

    private final List<String> separators = List.of(" ", ",", ";", ":", "'", "\"", "{", "}", "[", "]", "(", ")", "=", "!");
    private final List<String> arithmeticOperators = List.of("+", "-", "*", "/", "%");
    private final List<String> logicOperators = List.of("&&", "||");
    private final List<String> comparisonOperators = List.of("==", "!=", "<", "<=", ">", ">=");
    private final List<String> allDelimiters = Stream.of(separators, arithmeticOperators, logicOperators, comparisonOperators)
            .flatMap(List::stream)
            .toList();

    private final List<String> reservedWords = List.of("int", "float", "struct", "cout", "cin", "while", "if", "else", "main", "return", "#include", "iostream", "using", "namespace", "std");

    private final String delim = allDelimiters.stream()
            .map(Pattern::quote)
            .collect(Collectors.joining("|"));
    private final String split = "(?=" + delim + ")|(?<=" + delim + ")";

    private final String line;
    private final int lineNumber;

    public Token(String line, int lineNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
    }

    public List<Element> getLineTokens() {
        List<Element> tokens = new ArrayList<>();

        String[] elements = line.split(split);

        for (int i = 0; i < elements.length; ++i) {
            String e = elements[i];

            if (e.equals(" ")) continue;

            if (e.equals("<") && i < elements.length - 1 && elements[i + 1].equals("<")) {
                tokens.add(new Element("<<", TokenType.IO_OPERATOR)); i++; continue;
            }
            if (e.equals(">") && i < elements.length - 1 && elements[i + 1].equals(">")) {
                tokens.add(new Element(">>", TokenType.IO_OPERATOR)); i++; continue;
            }
            if (e.equals("<") && i < elements.length - 1 && elements[i + 1].equals("=")) {
                tokens.add(new Element("<=", TokenType.COMPARISON_OPERATOR)); i++; continue;
            }
            if (e.equals(">") && i < elements.length - 1 && elements[i + 1].equals("=")) {
                tokens.add(new Element(">=", TokenType.COMPARISON_OPERATOR)); i++; continue;
            }
            if (e.equals("=") && i < elements.length - 1 && elements[i + 1].equals("=")) {
                tokens.add(new Element("==", TokenType.COMPARISON_OPERATOR)); i++; continue;
            }

            if (e.equals("!") && i < elements.length - 1 && elements[i + 1].equals("=")) {
                tokens.add(new Element("!=", TokenType.COMPARISON_OPERATOR)); i++; continue;
            }

            if (reservedWords.contains(e)) {
                tokens.add(new Element(e, TokenType.RESERVED_WORD)); continue;
            }
            if (separators.contains(e)) {
                tokens.add(new Element(e, TokenType.SEPARATOR)); continue;
            }
            if (arithmeticOperators.contains(e)) {
                tokens.add(new Element(e, TokenType.ARITHMETIC_OPERATOR)); continue;
            }
            if (logicOperators.contains(e)) {
                tokens.add(new Element(e, TokenType.LOGIC_OPERATOR)); continue;
            }
            if (comparisonOperators.contains(e)) {
                tokens.add(new Element(e, TokenType.COMPARISON_OPERATOR)); continue;
            }

            if (identifiersMachine.verifySequence(e)) {
                tokens.add(new Element(e, TokenType.IDENTIFIER)); continue;
            }
            else if (realsMachine.verifySequence(e)) {
                tokens.add(new Element(e, TokenType.REAL_CONSTANT)); continue;
            }
            else if (integersMachine.verifySequence(e)) {
                tokens.add(new Element(e, TokenType.INTEGER_CONSTANT)); continue;
            }


            System.out.println(elements[i]);
        }

        return tokens;
    }

    private boolean validateTokens(List<Element> tokens) {
        return  isDeclaration(tokens) ||
                isReadStatement(tokens) ||
                isWriteStatement(tokens) ||
                isAssignment(tokens) ||
                isReturnStatement(tokens) ||
                isMainFunction(tokens) ||
                isUsingNamespaceStd(tokens) ||
                isLibrary(tokens) ||
                isBrace(tokens) ||
                isStructDefinition(tokens) ||
                isIfStatement(tokens) ||
                isWhileStatement(tokens);
    }


    private static boolean isDeclaration(List<Element> tokens) {
        if (tokens.size() != 5 && tokens.size() != 3) return false;
        for (int i = 0; i < tokens.size(); i++) {
            var e = tokens.get(i);
            if (i == 0 && (!e.getToken().equals("int") && !e.getToken().equals("float") && !e.getToken().equals("struct")))
                return false;
            if (i == 1 && !e.getType().equals(TokenType.IDENTIFIER))
                return false;
            if (i == 2 && (!e.getToken().equals("=") && !e.getToken().equals(";")))
                return false;
            if (i == 3 && (!e.getType().equals(TokenType.IDENTIFIER) && (!e.getType().equals(TokenType.INTEGER_CONSTANT) || !e.getType().equals(TokenType.REAL_CONSTANT))))
                return false;
            if (i == 4 && !e.getToken().equals(";"))
                return false;
        }
        return true;
    }

    private static boolean isAssignment(List<Element> tokens) {
        if (tokens.size() < 3) return false;
        if (!tokens.get(0).getType().equals(TokenType.IDENTIFIER)) return false;
        if (!tokens.get(1).getToken().equals("=")) return false;

        for (int i = 2; i < tokens.size(); i++) {
            var e = tokens.get(i);
            if (e.getType().equals(TokenType.IDENTIFIER) || e.getType().equals(TokenType.INTEGER_CONSTANT) || e.getType().equals(TokenType.REAL_CONSTANT)) continue;
            if (e.getToken().equals("+") || e.getToken().equals("-") || e.getToken().equals("*") || e.getToken().equals("/") || e.getToken().equals("%")) continue;
            if (e.getToken().equals(";") && i == tokens.size() - 1) return true;
            return false;
        }
        return true;
    }


    private static boolean isReadStatement(List<Element> tokens) {
        if (tokens.size() < 4) return false;
        if (!tokens.get(0).getToken().equals("cin")) return false;
        if (!tokens.get(1).getToken().equals(">>")) return false;
        if (!tokens.get(2).getType().equals(TokenType.IDENTIFIER)) return false;

        for (int i = 3; i < tokens.size(); i += 2) {
            if (tokens.get(i).getToken().equals(";") && i == tokens.size() - 1) return true;
            if (!tokens.get(i).getToken().equals(">>")) return false;
            if (i + 1 >= tokens.size() || !tokens.get(i + 1).getType().equals(TokenType.IDENTIFIER)) return false;
        }
        return true;
    }

    private static boolean isWriteStatement(List<Element> tokens) {
        if (tokens.size() < 4) return false;
        if (!tokens.get(0).getToken().equals("cout")) return false;
        if (!tokens.get(1).getToken().equals("<<")) return false;

        for (int i = 2; i < tokens.size(); i += 2) {
            var e = tokens.get(i);
            if (!e.getType().equals(TokenType.IDENTIFIER) && (!e.getType().equals(TokenType.INTEGER_CONSTANT) || !e.getType().equals(TokenType.REAL_CONSTANT))) return false;
            if (i + 1 == tokens.size() - 1 && tokens.get(i + 1).getToken().equals(";")) return true;
            if (i + 1 < tokens.size() && !tokens.get(i + 1).getToken().equals("<<")) {
                return false;
            }
        }
        return true;
    }

    private static boolean isLogicalCondition(List<Element> tokens) {
        if (tokens.size() < 3) return false;

        if (!tokens.get(0).getType().equals(TokenType.IDENTIFIER) && (!tokens.get(0).getType().equals(TokenType.INTEGER_CONSTANT) || !tokens.get(0).getType().equals(TokenType.REAL_CONSTANT)))
            return false;
        if (!tokens.get(1).getType().equals(TokenType.COMPARISON_OPERATOR))
            return false;
        if (!tokens.get(2).getType().equals(TokenType.IDENTIFIER) && (!tokens.get(2).getType().equals(TokenType.INTEGER_CONSTANT) || !tokens.get(2).getType().equals(TokenType.REAL_CONSTANT)))
            return false;

        if (tokens.size() == 3) return true;

        for (int i = 3; i < tokens.size(); i += 4) {
            if (!tokens.get(i).getType().equals(TokenType.LOGIC_OPERATOR))
                return false;
            if (i + 1 >= tokens.size() || (!tokens.get(i + 1).getType().equals(TokenType.IDENTIFIER) && (!tokens.get(i + 1).getType().equals(TokenType.INTEGER_CONSTANT) || !tokens.get(i + 1).getType().equals(TokenType.REAL_CONSTANT))))
                return false;
            if (i + 2 >= tokens.size() || !tokens.get(i + 2).getType().equals(TokenType.COMPARISON_OPERATOR))
                return false;
            if (i + 3 >= tokens.size() || (!tokens.get(i + 3).getType().equals(TokenType.IDENTIFIER) && (!tokens.get(i + 3).getType().equals(TokenType.INTEGER_CONSTANT) || !tokens.get(i + 3).getType().equals(TokenType.REAL_CONSTANT))))
                return false;
        }

        return true;
    }


    private static boolean isIfStatement(List<Element> tokens) {
        if (tokens.size() < 6) return false;
        if (!tokens.get(0).getToken().equals("if")) return false;
        if (!tokens.get(1).getToken().equals("(")) return false;
        if (!isLogicalCondition(tokens.subList(2, tokens.size() - 1))) return false;
        if (!tokens.get(tokens.size() - 1).getToken().equals(")")) return false;
        return true;
    }

    private static boolean isWhileStatement(List<Element> tokens) {
        if (tokens.size() < 6) return false;
        if (!tokens.get(0).getToken().equals("while")) return false;
        if (!tokens.get(1).getToken().equals("(")) return false;
        if (!isLogicalCondition(tokens.subList(2, tokens.size() - 1))) return false;
        if (!tokens.get(tokens.size() - 1).getToken().equals(")")) return false;
        return true;
    }

    private static boolean isReturnStatement(List<Element> tokens) {
        if (tokens.size() != 3) return false;
        if (!tokens.get(0).getToken().equals("return")) return false;
        if (!tokens.get(1).getToken().equals("0")) return false;
        if (!tokens.get(2).getToken().equals(";")) return false;
        return true;
    }

    private static boolean isStructDefinition(List<Element> tokens) {
        if (tokens.size() < 5) return false;
        if (!tokens.get(0).getToken().equals("struct")) return false;
        if (!tokens.get(1).getType().equals(TokenType.IDENTIFIER)) return false;
        if (!tokens.get(2).getToken().equals("{")) return false;
        if (!tokens.get(tokens.size() - 2).getToken().equals("}")) return false;
        if (!tokens.get(tokens.size() - 1).getToken().equals(";")) return false;
        if (!isDeclaration(tokens.subList(3, tokens.size() - 2))) return false;
        return true;
    }

    private static boolean isMainFunction(List<Element> tokens) {
        if (tokens.size() < 5) return false;
        if (!tokens.get(0).getToken().equals("int")) return false;
        if (!tokens.get(1).getToken().equals("main")) return false;
        if (!tokens.get(2).getToken().equals("(")) return false;
        if (!tokens.get(3).getToken().equals(")")) return false;
        if (!tokens.get(4).getToken().equals("{")) return false;
        return true;
    }

    private static boolean isLibrary(List<Element> tokens) {
        if (tokens.size() != 4) return false;
        if (!tokens.get(0).getToken().equals("#include")) return false;
        if (!tokens.get(1).getToken().equals("<")) return false;
        if (!tokens.get(2).getToken().equals("iostream")) return false;
        return tokens.get(3).getToken().equals(">");
    }

    private static boolean isUsingNamespaceStd(List<Element> tokens) {
        if (tokens.size() != 4) return false;
        if (!tokens.get(0).getToken().equals("using")) return false;
        if (!tokens.get(1).getToken().equals("namespace")) return false;
        if (!tokens.get(2).getToken().equals("std")) return false;
        return tokens.get(3).getToken().equals(";");
    }

    private static boolean isBrace(List<Element> tokens) {
        if (tokens.size() != 1) return false;
        return tokens.getFirst().getToken().equals("}") || tokens.getFirst().getToken().equals("{");
    }
}



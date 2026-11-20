package com.lftc;

import com.lftc.simbols.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Map<String, Integer> atoms = new HashMap<>();
    private static final SymbolsTable symbolsTable = new SymbolsTable();
    private static final List<FIP> FipList = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException {

        initAtoms();

        ProcessBuilder pb = new ProcessBuilder(
                "C:/Users/Next/Desktop/LFTC/Lab1/Lab1/lexer.exe",
                "src/main/resources/program.txt"
        );

        pb.directory(new File("C:/Users/Next/Desktop/LFTC/Lab1/Lab1"));
        pb.redirectErrorStream(true);

        Process process = pb.start();

        Pattern linePattern = Pattern.compile("\\(line\\s+(\\d+)\\)");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+");
                if (parts.length < 2) {
                    System.err.println("Invalid line: " + line);
                    continue;
                }

                String tokenType = parts[0];
                String lexeme = parts[1];

                int lineNumber = -1;
                Matcher m = linePattern.matcher(line);
                if (m.find()) {
                    lineNumber = Integer.parseInt(m.group(1));
                }

                System.out.printf("%-15s %-10s %n", tokenType, lexeme);

                FIP fip;

                if (tokenType.equals("ID")) {
                    Integer position = symbolsTable.findElement(lexeme);
                    int pos = (position != null) ? position : symbolsTable.addElement(lexeme);
                    fip = new FIP(lexeme, atoms.get("IDENTIFIER"), pos);
                }

                else if (tokenType.equals("INT_CONST")) {
                    Integer position = symbolsTable.findElement(lexeme);
                    int pos = (position != null) ? position : symbolsTable.addElement(lexeme);
                    fip = new FIP(lexeme, atoms.get("INTEGER_CONSTANT"), pos);
                }

                else if (tokenType.equals("FLOAT_CONST")) {
                    Integer position = symbolsTable.findElement(lexeme);
                    int pos = (position != null) ? position : symbolsTable.addElement(lexeme);
                    fip = new FIP(lexeme, atoms.get("REAL_CONSTANT"), pos);
                }

                else if (tokenType.equals("LEXICAL_ERROR")) {
                    throw new CustomException("Lexical error at line " + lineNumber + " for token: " + lexeme);
                }

                else if (atoms.get(lexeme) != null) {
                    fip = new FIP(lexeme, atoms.get(lexeme), -1);
                }

                else {
                    throw new CustomException("Unknown atom type for token: " + tokenType + " / lexeme: " + lexeme);
                }

                FipList.add(fip);
            }
        }

        System.out.println("\nFIP\n");
        FipList.forEach(fip -> System.out.println(fip.toString()));

        System.out.println("\nTS\n");
        symbolsTable.printHashTable();

        System.out.println("\nParsing\n");

        ProcessBuilder pbParser = new ProcessBuilder(
                "C:/Users/Next/Desktop/LFTC/Lab1/Lab1/parser.exe",
                "src/main/resources/program.txt"
        );

        pbParser.directory(new File("C:/Users/Next/Desktop/LFTC/Lab1/Lab1"));
        pbParser.redirectErrorStream(true);

        Process processParser = pbParser.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(processParser.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    private static void initAtoms() {
        atoms.put("IDENTIFIER", 0);
        atoms.put("INTEGER_CONSTANT", 1);
        atoms.put("REAL_CONSTANT", 2);

        atoms.put(" ", 3);
        atoms.put(".", 4);
        atoms.put(",", 5);
        atoms.put(";", 6);
        atoms.put(":", 7);
        atoms.put("'", 8);
        atoms.put("\"", 9);
        atoms.put("{", 10);
        atoms.put("}", 11);
        atoms.put("[", 12);
        atoms.put("]", 13);
        atoms.put("(", 14);
        atoms.put(")", 15);
        atoms.put("=", 16);
        atoms.put("!", 17);

        atoms.put("+", 18);
        atoms.put("-", 19);
        atoms.put("*", 20);
        atoms.put("/", 21);
        atoms.put("%", 22);

        atoms.put("&&", 23);
        atoms.put("||", 24);

        atoms.put("==", 25);
        atoms.put("!=", 26);
        atoms.put("<", 27);
        atoms.put("<=", 28);
        atoms.put(">", 29);
        atoms.put(">=", 30);

        atoms.put("int", 31);
        atoms.put("float", 32);
        atoms.put("struct", 33);
        atoms.put("cout", 34);
        atoms.put("cin", 35);
        atoms.put("while", 36);
        atoms.put("if", 37);
        atoms.put("else", 38);
        atoms.put("main", 39);
        atoms.put("return", 40);
        atoms.put("#include", 41);
        atoms.put("iostream", 42);
        atoms.put("using", 43);
        atoms.put("namespace", 44);
        atoms.put("std", 45);

        atoms.put("<<", 46);
        atoms.put(">>", 47);

        atoms.put("repeta", 48);
        atoms.put("panacand", 49);
        atoms.put("sfrepeta", 50);
    }
}

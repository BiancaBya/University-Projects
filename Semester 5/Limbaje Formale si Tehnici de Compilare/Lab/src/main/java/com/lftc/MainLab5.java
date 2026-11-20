package com.lftc;

import com.lftc.simbols.*;
import com.lftc.syntax.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class MainLab5 {

    private static final Map<String, Integer> atoms = new HashMap<>();
    private static final SymbolsTable symbolsTable = new SymbolsTable();
    private static final List<FIP> fipList = new ArrayList<>();
    private static final List<String> terminalsForParser = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        initAtoms();

        System.out.println("\nPART 1\n");
        runPart1(
                Path.of("src/main/resources/part1_grammar.txt"),
                Path.of("src/main/resources/part1_input.txt")
        );

        System.out.println("\nPART 2\n");

        buildFipAndTsFromProgram(Path.of("src/main/resources/program.txt"));

        System.out.println("\nFIP\n");
        fipList.forEach(System.out::println);

        System.out.println("\nTS\n");
        symbolsTable.printHashTable();

        runPart2(
                Path.of("src/main/resources/grammar_mlp.txt"),
                terminalsForParser
        );
    }

    private static void runPart1(Path grammarPath, Path inputPath) throws IOException {
        Grammar g = new GrammarReader().read(grammarPath);

        String inputLine = Files.readString(inputPath).trim();
        List<String> tokens = inputLine.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(inputLine.split("\\s+")));
        tokens.add(Grammar.END);

        System.out.println("Input: " + tokens);

        buildAndParse(g, tokens);
    }

    private static void runPart2(Path grammarPath, List<String> tokens) throws IOException {
        Grammar g = new GrammarReader().read(grammarPath);

        List<String> input = new ArrayList<>(tokens);
        input.add(Grammar.END);

        System.out.println("Input terminals (first 120): " + input.subList(0, Math.min(120, input.size())) + (input.size() > 120 ? " ..." : ""));

        buildAndParse(g, input);
    }

    private static void buildAndParse(Grammar g, List<String> input) {
        FirstFollow ffComp = new FirstFollow();
        FirstFollow.Result ff = ffComp.compute(g);

        LR0Automaton.Result aut = new LR0Automaton().build(g);

        ParsingTable.BuildResult table = new ParsingTable().build(g, aut, ff);

        System.out.println("SLR applicable: " + table.isSLR);
        if (!table.isSLR) {
            System.out.println("Conflicts:");
            table.conflicts.forEach(System.out::println);
            return;
        }

        SLRParser parser = new SLRParser(g, table.action, table.goTo);
        SLRParser.ParseResult res = parser.parse(input);

        if (!res.accepted) {
            System.out.println("ERROR: " + res.errorMessage);
            System.out.println("Reductions so far:");
            parser.formatProductions(res.appliedProductions).forEach(System.out::println);
            return;
        }

        System.out.println("ACCEPTED");
        System.out.println("Applied productions:");
        parser.formatProductions(res.appliedProductions).forEach(System.out::println);
    }

    private static void buildFipAndTsFromProgram(Path programPath) throws IOException {
        fipList.clear();
        terminalsForParser.clear();

        List<String> lines = Files.readAllLines(programPath);

        int lineNumber = 1;
        for (String line : lines) {
            Token tokenLine = new Token(line, lineNumber);
            List<Element> elements = tokenLine.getLineTokens();

            for (Element e : elements) {
                addToFipAndTs(e, lineNumber);
                terminalsForParser.add(TerminalMapper.toTerminal(e));
            }
            lineNumber++;
        }
    }

    private static void addToFipAndTs(Element e, int lineNumber) {
        String lexeme = e.getToken();

        if (e.getType() == TokenType.IDENTIFIER) {
            int tsPos = upsertInTs(lexeme);
            fipList.add(new FIP(lexeme, atoms.get("IDENTIFIER"), tsPos));
            return;
        }

        if (e.getType() == TokenType.INTEGER_CONSTANT) {
            int tsPos = upsertInTs(lexeme);
            fipList.add(new FIP(lexeme, atoms.get("INTEGER_CONSTANT"), tsPos));
            return;
        }

        if (e.getType() == TokenType.REAL_CONSTANT) {
            int tsPos = upsertInTs(lexeme);
            fipList.add(new FIP(lexeme, atoms.get("REAL_CONSTANT"), tsPos));
            return;
        }

        Integer atomCode = atoms.get(lexeme);
        if (atomCode == null) {
            throw new CustomException("Unknown atom '" + lexeme + "' at line " + lineNumber);
        }
        fipList.add(new FIP(lexeme, atomCode, -1));
    }

    private static int upsertInTs(String lexeme) {
        Integer position = symbolsTable.findElement(lexeme);
        return (position != null) ? position : symbolsTable.addElement(lexeme);
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
    }
}

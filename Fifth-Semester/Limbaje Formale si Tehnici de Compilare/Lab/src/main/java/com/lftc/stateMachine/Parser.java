package com.lftc.stateMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    public static StateMachine ConsoleParser() {
        StateMachine stateMachine = new StateMachine();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("States:");
            String statesData = readNonEmptyLine(in);
            String[] statesNames = statesData.trim().split("\\s+");

            System.out.println("Alphabet:");
            String alphabetData = readNonEmptyLine(in);
            List<String> alphabetValues = Arrays.stream(alphabetData.trim().split("\\s+")).toList();

            System.out.println("Initial State:");
            String initialState = readNonEmptyLine(in).trim();

            System.out.println("Final States:");
            String finalStatesData = readNonEmptyLine(in);
            List<String> finalStates = Arrays.stream(finalStatesData.trim().split("\\s+")).toList();

            Map<String, State> statesMap = new HashMap<>();
            for (String stateName : statesNames) {
                statesMap.put(stateName, new State(stateName, finalStates.contains(stateName)));
            }

            if (!statesMap.containsKey(initialState)) {
                throw new IllegalArgumentException("Initial State \"" + initialState + "\" does not exist.");
            }
            if (finalStates.stream().anyMatch(s -> !statesMap.containsKey(s))) {
                throw new IllegalArgumentException("Final States not in the state machine.");
            }

            System.out.println("Transitions: <from> <symbol> <to>");
            System.out.println("Ex: q0 0 q1");

            while (true) {
                String line = in.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) break;

                String[] parts = line.split("\\s+");
                if (parts.length != 3) {
                    System.out.println("Invalid format. Use: <from> <symbol> <to>");
                    continue;
                }
                String from = parts[0];
                String symStr = parts[1];
                String to = parts[2];

                if (!statesMap.containsKey(from)) {
                    System.out.println("Initial state \"" + to + "\" does not exist");
                    continue;
                }
                if (!statesMap.containsKey(to)) {
                    System.out.println("Destination state \"" + to + "\" does not exist");
                    continue;
                }
                if (symStr.length() != 1) {
                    System.out.println("Symbol must be a single character.");
                    continue;
                }
                char symbol = symStr.charAt(0);
                if (alphabetValues.stream().noneMatch(s -> s.length() == 1 && s.charAt(0) == symbol)) {
                    System.out.println("Symbol \"" + symbol + "\" not in alphabet");
                    continue;
                }

                statesMap.get(from).addTransition(symbol, statesMap.get(to));
            }

            alphabetValues.forEach(s -> stateMachine.addSymbol(s.charAt(0)));
            stateMachine.setInitialState(statesMap.get(initialState));
            for (var st : statesMap.values()) {
                stateMachine.addState(st);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stateMachine;
    }

    private static String readNonEmptyLine(BufferedReader in) throws IOException {
        String line;
        while (true) {
            line = in.readLine();
            if (line == null) throw new IOException();
            if (!line.trim().isEmpty()) return line;
            System.out.println("Again:");
        }
    }

    public static StateMachine FileParser(String filename) {
        StateMachine stateMachine = new StateMachine();
        try(BufferedReader buffer = new BufferedReader(new FileReader(filename))) {
            String statesData = buffer.readLine();
            String[] statesNames = statesData.split(" ");

            String alphabetData = buffer.readLine();
            List<String> alphabetValues = Arrays.stream(alphabetData.split(" ")).toList();

            String initialState = buffer.readLine();

            String finalStatesData = buffer.readLine();
            List<String> finalStates = Arrays.stream(finalStatesData.split(" ")).toList();

            Map<String, State> statesMap = new HashMap<>();
            for (String stateName : statesNames) {
                State state = new State(stateName, finalStates.contains(stateName));
                statesMap.put(stateName, state);
            }

            String transitionData;
            while ((transitionData = buffer.readLine()) != null) {
                String[] transition = transitionData.split(" ");
                String from  = transition[0];
                String symbol = transition[1];
                String to = transition[2];
                statesMap.get(from).addTransition(symbol.charAt(0), statesMap.get(to));
            }

            alphabetValues.forEach(sym -> stateMachine.addSymbol(sym.charAt(0)));
            stateMachine.setInitialState(statesMap.get(initialState));
            for(var entry : statesMap.entrySet()) {
                stateMachine.addState(entry.getValue());
            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stateMachine;
    }
}

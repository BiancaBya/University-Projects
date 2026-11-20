package com.lftc.stateMachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Menu {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

//    static String path = "C:\\Users\\Next\\Desktop\\LFTC\\Lab1\\Lab1\\src\\main\\resources\\integers.txt";
//    static String path = "C:\\Users\\Next\\Desktop\\LFTC\\Lab1\\Lab1\\src\\main\\resources\\stateMachine.txt";
    static String path = "C:\\Users\\Next\\Desktop\\LFTC\\Lab1\\Lab1\\src\\main\\resources\\activitate.txt";
    static StateMachine stateMachine = Parser.FileParser(path);

//    static StateMachine stateMachine = Parser.ConsoleParser();


    public static void main(String[] args) throws IOException{
        while (true) {
            printMenu();
            int option = Integer.parseInt(reader.readLine());
            switch (option) {
                case 1:
                    stateMachine.printAlphabet();
                    break;
                case 2:
                    stateMachine.printInitialState();
                    break;
                case 3:
                    stateMachine.printFinalStates();
                    break;
                case 4:
                    stateMachine.printStates();
                    break;
                case 5:
                    System.out.print("Sequence > ");
                    System.out.println(stateMachine.verifySequence(reader.readLine().trim()));
                    break;
                case 6:
                    System.out.print("Sequence > ");
                    System.out.println(stateMachine.verifyPrefix(reader.readLine().trim()));
                    break;
                case 7:
                    return;
                default:
                    System.out.print("Invalid input!");
            }
        }
    }

    private static void printMenu() {
        String menu = """
                1. Alphabet
                2. Initial State
                3. Final States
                4. Transitions
                5. Check Sequence
                6. Longest Prefix
                7. Exit
                
                Option >""";
        System.out.print(menu);
    }

}

/*
Activitate

public class State {
    private String name;
    private boolean isAcceptState;
    private final Map<Character, Set<State>> transitions;
}
public class StateMachine {
    private final Set<State> states;
    private final Set<Character> alphabet;
    private State initialState;
    private final Set<State> finalStates;
}

StateMachine:
    states: Set<State>
    alphabet: Set<Character>
    initialState: State
    finalStates: Set<State>

State:
    name: String
    isAcceptState: Boolean
    transitions: Map<Character,Set<State>>

 */



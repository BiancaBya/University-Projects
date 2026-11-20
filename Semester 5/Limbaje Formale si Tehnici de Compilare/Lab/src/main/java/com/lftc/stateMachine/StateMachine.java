package com.lftc.stateMachine;

import java.util.HashSet;
import java.util.Set;

public class StateMachine {
    private final Set<State> states;
    private final Set<Character> alphabet;
    private State initialState;
    private final Set<State> finalStates;

    public StateMachine() {
        states = new HashSet<>();
        alphabet = new HashSet<>();
        finalStates = new HashSet<>();
    }

    public boolean verifyDeterministic() {
        for (State s : states) {
            for (var entry : s.getTransitions().entrySet()) {
                if (entry.getValue().size() > 1) return false;
            }
        }
        return true;
    }

    public boolean verifySequence(String sequence) {
        if (!verifyDeterministic()) {
            throw new IllegalArgumentException("Not deterministic");
        }
        State state = initialState;
        for(int i = 0; i < sequence.length(); i++){
            Character symbol = sequence.charAt(i);
            if(!alphabet.contains(symbol)){
                return false;
            }
            State next = state.next(symbol);
            if(next == null){
                return false;
            }
            state = next;
        }
        return state.isFinalState();
    }

    public String verifyPrefix(String sequence) {
        if (!verifyDeterministic()) {
            throw new IllegalArgumentException("Not deterministic");
        }
        State state = initialState;
        int prefix = 0;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            if (!alphabet.contains(c)) break;
            State next = state.next(c);
            if (next == null) break;
            state = next;
            if (state.isFinalState()) {
                prefix = i + 1;
            }
        }
        if (prefix >= 0)
            return sequence.substring(0, prefix);
        else
            return "";
    }

    public void addState(State state) {
        states.add(state);
        if(state.isFinalState()) {
            finalStates.add(state);
        }
    }

    public void addSymbol(Character symbol) {
        alphabet.add(symbol);
    }

    public void printAlphabet() {
        System.out.println(getAlphabet());
    }

    public void printInitialState() {
        System.out.println(getInitialState().getState());
    }

    public void printFinalStates() {
        System.out.println(getFinalStates().stream().map(State::getState).toList());
    }

    public void printStates() {
        for (State state : getStates()) {
            state.getTransitions().forEach((symbol, destinations) ->{
                destinations.forEach(destination ->
                        System.out.println(state.getState() + " -> " + symbol + " -> " + destination.getState()));
            });
        }
    }

    public Set<State> getStates() {
        return states;
    }

    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public State getInitialState() {
        return initialState;
    }

    public Set<State> getFinalStates() {
        return finalStates;
    }

    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }
}



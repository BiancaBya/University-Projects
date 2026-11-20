package com.lftc.stateMachine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class State {
    private final String state;
    private final boolean finalState;
    private final Map<Character, Set<State>> transitions;

    public State(String state, boolean finalState) {
        this.state = state;
        this.finalState = finalState;
        transitions = new HashMap<>();
    }

    public void addTransition(Character symbol, State destination) {
        if (!transitions.containsKey(symbol)) {
            transitions.put(symbol, new HashSet<>());
        }
        transitions.get(symbol).add(destination);
    }

    public State next(Character symbol) {
        Set<State> states = transitions.get(symbol);
        if(states == null || states.isEmpty()) return null;
        if(states.size() > 1){
            throw new RuntimeException("Not deterministic");
        }
        return states.iterator().next();
    }

    public String getState() {
        return state;
    }

    public boolean isFinalState() {
        return finalState;
    }

    public Map<Character, Set<State>> getTransitions() {
        return transitions;
    }

}



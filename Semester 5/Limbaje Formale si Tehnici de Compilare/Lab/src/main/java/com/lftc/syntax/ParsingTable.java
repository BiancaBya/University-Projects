package com.lftc.syntax;

import java.util.*;

public final class ParsingTable {

    public enum ActionType { SHIFT, REDUCE, ACCEPT }

    public static final class Action {
        public final ActionType type;
        public final int value;
        private Action(ActionType type, int value) {
            this.type = type;
            this.value = value;
        }

        public static Action shift(int state) { return new Action(ActionType.SHIFT, state); }
        public static Action reduce(int prodIndex) { return new Action(ActionType.REDUCE, prodIndex); }
        public static Action accept() { return new Action(ActionType.ACCEPT, 0); }

        @Override
        public String toString() {
            return switch (type) {
                case SHIFT -> "s" + value;
                case REDUCE -> "r(" + value + ")";
                case ACCEPT -> "acc";
            };
        }
    }

    public static final class BuildResult {
        public final boolean isSLR;
        public final List<String> conflicts;
        public final Map<Integer, Map<String, Action>> action;
        public final Map<Integer, Map<String, Integer>> goTo;

        BuildResult(boolean isSLR, List<String> conflicts,
                    Map<Integer, Map<String, Action>> action,
                    Map<Integer, Map<String, Integer>> goTo) {
            this.isSLR = isSLR;
            this.conflicts = conflicts;
            this.action = action;
            this.goTo = goTo;
        }
    }

    public BuildResult build(Grammar g, LR0Automaton.Result aut, FirstFollow.Result ff) {
        Map<Integer, Map<String, Action>> action = new HashMap<>();
        Map<Integer, Map<String, Integer>> goTo = new HashMap<>();
        List<String> conflicts = new ArrayList<>();

        for (var e : aut.transitions.entrySet()) {
            int from = e.getKey();
            for (var t : e.getValue().entrySet()) {
                String X = t.getKey();
                int to = t.getValue();

                if (g.isNonTerminal(X)) {
                    goTo.computeIfAbsent(from, k -> new HashMap<>()).put(X, to);
                } else {
                    putAction(action, conflicts, from, X, Action.shift(to));
                }
            }
        }

        for (LR0Automaton.State st : aut.states) {
            int i = st.id;

            for (LR0Item it : st.items) {
                Production p = g.getProductions().get(it.prodIndex);

                if (it.dot == p.rhs.size()) {
                    if (p.lhs.equals(g.getAugmentedStart())) {
                        putAction(action, conflicts, i, Grammar.END, Action.accept());
                    } else {
                        Set<String> followA = ff.follow.getOrDefault(p.lhs, Set.of());
                        for (String a : followA) {
                            putAction(action, conflicts, i, a, Action.reduce(it.prodIndex));
                        }
                    }
                }
            }
        }

        boolean ok = conflicts.isEmpty();
        return new BuildResult(ok, conflicts, action, goTo);
    }

    private void putAction(Map<Integer, Map<String, Action>> action,
                           List<String> conflicts,
                           int state, String terminal, Action newAct) {
        Map<String, Action> row = action.computeIfAbsent(state, k -> new HashMap<>());
        Action old = row.get(terminal);
        if (old == null) {
            row.put(terminal, newAct);
            return;
        }
        if (!old.toString().equals(newAct.toString())) {
            conflicts.add("Conflict at state " + state + ", terminal '" + terminal + "': " + old + " vs " + newAct);
        }
    }
}

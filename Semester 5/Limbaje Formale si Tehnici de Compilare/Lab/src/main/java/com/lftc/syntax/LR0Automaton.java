package com.lftc.syntax;

import java.util.*;

public final class LR0Automaton {

    public static final class State {
        public final int id;
        public final Set<LR0Item> items;

        State(int id, Set<LR0Item> items) {
            this.id = id;
            this.items = items;
        }
    }

    public static final class Result {
        public final List<State> states;
        public final Map<Integer, Map<String, Integer>> transitions;

        Result(List<State> states, Map<Integer, Map<String, Integer>> transitions) {
            this.states = states;
            this.transitions = transitions;
        }
    }

    public Result build(Grammar g) {

        Set<LR0Item> startItems = closure(g, Set.of(new LR0Item(0, 0)));
        List<State> states = new ArrayList<>();
        Map<Integer, Map<String, Integer>> trans = new HashMap<>();

        Map<Set<LR0Item>, Integer> idByItems = new HashMap<>(new SetKeyHashMap<>());
        Queue<Set<LR0Item>> q = new ArrayDeque<>();

        int id0 = 0;
        idByItems.put(startItems, id0);
        states.add(new State(id0, startItems));
        q.add(startItems);

        while (!q.isEmpty()) {
            Set<LR0Item> I = q.poll();
            int fromId = idByItems.get(I);

            Map<String, Set<LR0Item>> moved = new LinkedHashMap<>();
            for (LR0Item it : I) {
                Production p = g.getProductions().get(it.prodIndex);
                if (it.dot >= p.rhs.size()) continue;
                String X = p.rhs.get(it.dot);
                moved.computeIfAbsent(X, k -> new LinkedHashSet<>())
                        .add(new LR0Item(it.prodIndex, it.dot + 1));
            }

            for (var entry : moved.entrySet()) {
                String X = entry.getKey();
                Set<LR0Item> J = closure(g, entry.getValue());

                Integer toId = idByItems.get(J);
                if (toId == null) {
                    toId = states.size();
                    idByItems.put(J, toId);
                    states.add(new State(toId, J));
                    q.add(J);
                }

                trans.computeIfAbsent(fromId, k -> new HashMap<>()).put(X, toId);
            }
        }

        return new Result(states, trans);
    }

    private Set<LR0Item> closure(Grammar g, Set<LR0Item> items) {
        Set<LR0Item> result = new LinkedHashSet<>(items);
        boolean changed;

        do {
            changed = false;
            List<LR0Item> snapshot = new ArrayList<>(result);

            for (LR0Item it : snapshot) {
                Production p = g.getProductions().get(it.prodIndex);
                if (it.dot >= p.rhs.size()) continue;

                String next = p.rhs.get(it.dot);
                if (!g.isNonTerminal(next)) continue;

                for (int prodIdx : g.getProductionIndexesForLhs(next)) {
                    LR0Item newItem = new LR0Item(prodIdx, 0);
                    if (result.add(newItem)) changed = true;
                }
            }
        } while (changed);

        return result;
    }

    private static final class SetKeyHashMap<K> extends HashMap<Set<LR0Item>, Integer> {
        @Override
        public Integer get(Object key) { return super.get(key); }
    }
}

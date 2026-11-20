package com.lftc.syntax;

import java.util.*;

public final class FirstFollow {

    public static final class Result {
        public final Map<String, Set<String>> first;
        public final Map<String, Set<String>> follow;

        Result(Map<String, Set<String>> first, Map<String, Set<String>> follow) {
            this.first = first;
            this.follow = follow;
        }
    }

    public Result compute(Grammar g) {
        Map<String, Set<String>> first = new HashMap<>();
        Map<String, Set<String>> follow = new HashMap<>();

        for (String t : g.getTerminals()) first.put(t, new LinkedHashSet<>(Set.of(t)));
        for (String nt : g.getNonTerminals()) first.putIfAbsent(nt, new LinkedHashSet<>());
        first.putIfAbsent(Grammar.EPS, new LinkedHashSet<>(Set.of(Grammar.EPS)));

        for (String nt : g.getNonTerminals()) follow.put(nt, new LinkedHashSet<>());
        follow.get(g.getStart()).add(Grammar.END);

        boolean changed;
        do {
            changed = false;
            for (Production p : g.getProductions()) {
                String A = p.lhs;
                List<String> alpha = p.rhs;

                if (alpha.isEmpty()) {
                    changed |= first.get(A).add(Grammar.EPS);
                    continue;
                }

                boolean allNullable = true;
                for (String X : alpha) {
                    Set<String> firstX = first.getOrDefault(X, Set.of());
                    for (String s : firstX) {
                        if (!s.equals(Grammar.EPS)) {
                            if (first.get(A).add(s)) changed = true;
                        }
                    }
                    if (!firstX.contains(Grammar.EPS)) {
                        allNullable = false;
                        break;
                    }
                }
                if (allNullable) {
                    if (first.get(A).add(Grammar.EPS)) changed = true;
                }
            }
        } while (changed);

        do {
            changed = false;
            for (Production p : g.getProductions()) {
                String A = p.lhs;
                List<String> rhs = p.rhs;

                for (int i = 0; i < rhs.size(); i++) {
                    String B = rhs.get(i);
                    if (!g.isNonTerminal(B)) continue;

                    List<String> beta = rhs.subList(i + 1, rhs.size());
                    Set<String> firstBeta = firstOfSequence(first, beta);

                    for (String s : firstBeta) {
                        if (!s.equals(Grammar.EPS)) {
                            if (follow.get(B).add(s)) changed = true;
                        }
                    }

                    if (beta.isEmpty() || firstBeta.contains(Grammar.EPS)) {
                        for (String s : follow.get(A)) {
                            if (follow.get(B).add(s)) changed = true;
                        }
                    }
                }
            }
        } while (changed);

        return new Result(first, follow);
    }

    public static Set<String> firstOfSequence(Map<String, Set<String>> first, List<String> seq) {
        if (seq.isEmpty()) return new LinkedHashSet<>(Set.of(Grammar.EPS));

        Set<String> result = new LinkedHashSet<>();
        boolean allNullable = true;

        for (String X : seq) {
            Set<String> firstX = first.getOrDefault(X, Set.of());
            for (String s : firstX) {
                if (!s.equals(Grammar.EPS)) result.add(s);
            }
            if (!firstX.contains(Grammar.EPS)) {
                allNullable = false;
                break;
            }
        }
        if (allNullable) result.add(Grammar.EPS);
        return result;
    }
}

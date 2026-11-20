package com.lftc.syntax;

import java.util.*;

public final class Grammar {
    public static final String EPS = "EPS";
    public static final String END = "$";

    private final String start;
    private final List<Production> productions;
    private final Map<String, List<Integer>> prodByLhs;
    private final Set<String> nonTerminals;
    private final Set<String> terminals;
    private final String augmentedStart;

    public Grammar(String start, List<Production> originalProductions) {
        this.start = start;

        this.nonTerminals = new LinkedHashSet<>();
        for (Production p : originalProductions) nonTerminals.add(p.lhs);

        this.terminals = new LinkedHashSet<>();
        for (Production p : originalProductions) {
            for (String sym : p.rhs) {
                if (sym.equals(EPS)) continue;
                if (!nonTerminals.contains(sym)) terminals.add(sym);
            }
        }
        terminals.add(END);

        this.augmentedStart = start + "'";
        Production aug = new Production(augmentedStart, List.of(start));

        this.productions = new ArrayList<>();
        this.productions.add(aug);
        this.productions.addAll(originalProductions);

        this.prodByLhs = new HashMap<>();
        for (int i = 0; i < productions.size(); i++) {
            prodByLhs.computeIfAbsent(productions.get(i).lhs, k -> new ArrayList<>()).add(i);
        }
        nonTerminals.add(augmentedStart);
    }

    public String getStart() { return start; }
    public String getAugmentedStart() { return augmentedStart; }

    public List<Production> getProductions() { return productions; }

    public Set<String> getNonTerminals() { return Collections.unmodifiableSet(nonTerminals); }
    public Set<String> getTerminals() { return Collections.unmodifiableSet(terminals); }

    public List<Integer> getProductionIndexesForLhs(String lhs) {
        return prodByLhs.getOrDefault(lhs, List.of());
    }

    public boolean isNonTerminal(String sym) { return nonTerminals.contains(sym); }
    public boolean isTerminal(String sym) { return terminals.contains(sym) && !isNonTerminal(sym); }
}

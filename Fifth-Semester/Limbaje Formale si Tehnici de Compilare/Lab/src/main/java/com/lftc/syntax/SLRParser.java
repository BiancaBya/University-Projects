package com.lftc.syntax;

import java.util.*;

public final class SLRParser {

    public static final class ParseResult {
        public final boolean accepted;
        public final List<Integer> appliedProductions;
        public final String errorMessage;

        private ParseResult(boolean accepted, List<Integer> appliedProductions, String errorMessage) {
            this.accepted = accepted;
            this.appliedProductions = appliedProductions;
            this.errorMessage = errorMessage;
        }

        public static ParseResult ok(List<Integer> prods) { return new ParseResult(true, prods, null); }
        public static ParseResult err(String msg, List<Integer> prodsSoFar) { return new ParseResult(false, prodsSoFar, msg); }
    }

    private final Grammar grammar;
    private final Map<Integer, Map<String, ParsingTable.Action>> action;
    private final Map<Integer, Map<String, Integer>> goTo;

    public SLRParser(Grammar grammar,
                     Map<Integer, Map<String, ParsingTable.Action>> action,
                     Map<Integer, Map<String, Integer>> goTo) {
        this.grammar = grammar;
        this.action = action;
        this.goTo = goTo;
    }

    public ParseResult parse(List<String> inputTokens) {
        List<String> input = new ArrayList<>(inputTokens);
        if (input.isEmpty() || !input.getLast().equals(Grammar.END)) input.add(Grammar.END);

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);

        int pos = 0;
        List<Integer> reductions = new ArrayList<>();

        while (true) {
            int state = stack.peek();
            String a = input.get(pos);

            ParsingTable.Action act = action.getOrDefault(state, Map.of()).get(a);

            if (act == null) {
                String expected = action.getOrDefault(state, Map.of()).keySet().toString();
                return ParseResult.err(
                        "Syntax error at token '" + a + "' (pos=" + pos + "), state=" + state + ". Expected one of: " + expected,
                        reductions
                );
            }

            switch (act.type) {
                case SHIFT -> {
                    stack.push(act.value);
                    pos++;
                }
                case REDUCE -> {
                    Production p = grammar.getProductions().get(act.value);

                    int popCount = p.rhs.size();
                    for (int i = 0; i < popCount; i++) stack.pop();

                    int s2 = stack.peek();
                    Integer next = goTo.getOrDefault(s2, Map.of()).get(p.lhs);
                    if (next == null) {
                        return ParseResult.err(
                                "Goto missing for state=" + s2 + ", nonterminal=" + p.lhs + " after reducing by " + p,
                                reductions
                        );
                    }
                    stack.push(next);
                    reductions.add(act.value);
                }
                case ACCEPT -> {
                    return ParseResult.ok(reductions);
                }
            }
        }
    }

    public List<String> formatProductions(List<Integer> prodIndexes) {
        List<String> lines = new ArrayList<>();
        for (int idx : prodIndexes) {
            lines.add(idx + ": " + grammar.getProductions().get(idx));
        }
        return lines;
    }
}

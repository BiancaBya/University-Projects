package com.lftc.syntax;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class GrammarReader {

    public Grammar read(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);

        String start = null;
        List<Production> prods = new ArrayList<>();

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            if (line.startsWith("START")) {
                String[] parts = line.split("\\s+");
                if (parts.length != 2) throw new IllegalArgumentException("Invalid START line: " + line);
                start = parts[1];
                continue;
            }

            int arrow = line.indexOf("->");
            if (arrow < 0) throw new IllegalArgumentException("Missing '->' in line: " + line);

            String lhs = line.substring(0, arrow).trim();
            String rhsAll = line.substring(arrow + 2).trim();

            String[] alts = rhsAll.split("\\|");
            for (String altRaw : alts) {
                String alt = altRaw.trim();
                if (alt.equals(Grammar.EPS)) {
                    prods.add(new Production(lhs, List.of()));
                } else {
                    List<String> symbols = Arrays.stream(alt.split("\\s+"))
                            .filter(s -> !s.isBlank())
                            .toList();
                    if (symbols.size() == 1 && symbols.getFirst().equals(Grammar.EPS)) {
                        prods.add(new Production(lhs, List.of()));
                    } else {
                        prods.add(new Production(lhs, symbols));
                    }
                }
            }
        }

        if (start == null) throw new IllegalArgumentException("Missing START symbol in grammar file.");
        return new Grammar(start, prods);
    }
}

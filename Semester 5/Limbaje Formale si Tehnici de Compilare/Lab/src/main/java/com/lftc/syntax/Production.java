package com.lftc.syntax;

import java.util.List;
import java.util.Objects;

public final class Production {
    public final String lhs;
    public final List<String> rhs;

    public Production(String lhs, List<String> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        if (rhs.isEmpty()) return lhs + " -> " + Grammar.EPS;
        return lhs + " -> " + String.join(" ", rhs);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Production p)) return false;
        return lhs.equals(p.lhs) && rhs.equals(p.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs);
    }
}

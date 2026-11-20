package com.lftc.syntax;

import java.util.Objects;

public final class LR0Item {
    public final int prodIndex;
    public final int dot;

    public LR0Item(int prodIndex, int dot) {
        this.prodIndex = prodIndex;
        this.dot = dot;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LR0Item it)) return false;
        return prodIndex == it.prodIndex && dot == it.dot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(prodIndex, dot);
    }

    @Override
    public String toString() {
        return "(" + prodIndex + ", dot=" + dot + ")";
    }
}

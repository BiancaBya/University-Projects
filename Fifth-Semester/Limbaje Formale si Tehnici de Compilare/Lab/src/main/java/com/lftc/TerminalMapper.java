package com.lftc;

import com.lftc.simbols.Element;

public final class TerminalMapper {
    private TerminalMapper() {}

    public static String toTerminal(Element e) {
        return switch (e.getType()) {
            case IDENTIFIER -> "ID";
            case INTEGER_CONSTANT -> "INT_CONST";
            case REAL_CONSTANT -> "REAL_CONST";
            default -> e.getToken();
        };
    }
}

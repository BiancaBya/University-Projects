package com.lftc.simbols;

import com.lftc.TokenType;

public class Element {

    private String token;
    private TokenType type;

    public Element(String token, TokenType type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public TokenType getType() {
        return type;
    }
}



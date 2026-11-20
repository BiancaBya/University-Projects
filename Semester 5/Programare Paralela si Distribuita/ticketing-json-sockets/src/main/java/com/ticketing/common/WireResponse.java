package com.ticketing.common;

import com.fasterxml.jackson.databind.JsonNode;

public class WireResponse {
    public String type;
    public String requestId;
    public boolean ok;
    public String message;
    public JsonNode data;

    public WireResponse() {}

    public WireResponse(String type, String requestId, boolean ok, String message, JsonNode data) {
        this.type = type;
        this.requestId = requestId;
        this.ok = ok;
        this.message = message;
        this.data = data;
    }
}

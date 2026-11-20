package com.ticketing.common;

import com.fasterxml.jackson.databind.JsonNode;

public class WireMessage {
    public String type;
    public String requestId;
    public JsonNode data;

    public WireMessage() {}

    public WireMessage(String type, String requestId, JsonNode data) {
        this.type = type;
        this.requestId = requestId;
        this.data = data;
    }
}

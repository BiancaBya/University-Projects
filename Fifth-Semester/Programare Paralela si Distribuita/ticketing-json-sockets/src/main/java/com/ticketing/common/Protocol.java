package com.ticketing.common;

public final class Protocol {
    private Protocol() {}

    public static final String RESERVE_REQUEST = "RESERVE_REQUEST";
    public static final String PAY_REQUEST = "PAY_REQUEST";
    public static final String REFUND_REQUEST = "REFUND_REQUEST";
    public static final String PING = "PING";

    public static final String SERVER_SHUTDOWN = "SERVER_SHUTDOWN";

    public static final String RESERVE_RESPONSE = "RESERVE_RESPONSE";
    public static final String PAY_RESPONSE = "PAY_RESPONSE";
    public static final String REFUND_RESPONSE = "REFUND_RESPONSE";
    public static final String PING_RESPONSE = "PING_RESPONSE";
    public static final String ERROR = "ERROR";
}

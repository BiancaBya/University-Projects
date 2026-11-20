package com.ticketing.server;

import java.util.HashSet;
import java.util.Set;

public class ServerRuntime {
    private final Set<ClientConnection> connections = new HashSet<>();
    private final Object connectionsMutex = new Object();

    public void register(ClientConnection c) {
        synchronized (connectionsMutex) {
            connections.add(c);
        }
    }

    public void unregister(ClientConnection c) {
        synchronized (connectionsMutex) {
            connections.remove(c);
        }
    }

    public void broadcastShutdown(String message) {
        ClientConnection[] snapshot;
        synchronized (connectionsMutex) {
            snapshot = connections.toArray(new ClientConnection[0]);
        }
        for (ClientConnection c : snapshot) {
            c.sendShutdown(message);
        }
    }

    public int activeConnections() {
        synchronized (connectionsMutex) {
            return connections.size();
        }
    }
}

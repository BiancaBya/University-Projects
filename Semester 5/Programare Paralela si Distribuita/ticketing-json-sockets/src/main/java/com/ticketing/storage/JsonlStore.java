package com.ticketing.storage;

import com.ticketing.common.Json;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.locks.ReentrantLock;

public class JsonlStore {
    private final Path file;
    private final ReentrantLock lock = new ReentrantLock(true);

    public JsonlStore(Path file) {
        this.file = file;
    }

    public void appendObject(Object o) throws IOException {
        lock.lock();
        try {
            Files.createDirectories(file.getParent());
            try (BufferedWriter w = Files.newBufferedWriter(file,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
                w.write(Json.MAPPER.writeValueAsString(o));
                w.newLine();
            }
        } finally {
            lock.unlock();
        }
    }

    public Path getFile() {
        return file;
    }
}

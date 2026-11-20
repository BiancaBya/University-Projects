package com.ticketing.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.locks.ReentrantLock;

public class SnapshotStore {
    private final Path file;
    private final ReentrantLock lock = new ReentrantLock(true);

    public SnapshotStore(Path file) {
        this.file = file;
    }

    public void appendTextBlock(String text) throws IOException {
        lock.lock();
        try {
            Files.createDirectories(file.getParent());
            try (BufferedWriter w = Files.newBufferedWriter(file,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
                w.write(text);
                if (!text.endsWith(System.lineSeparator())) {
                    w.newLine();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public Path getFile() {
        return file;
    }
}

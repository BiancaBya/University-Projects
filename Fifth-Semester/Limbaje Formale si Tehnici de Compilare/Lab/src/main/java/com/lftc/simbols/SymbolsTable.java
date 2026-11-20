package com.lftc.simbols;

public class SymbolsTable {
    private int capacity;
    private int size;
    private String[] table;

    public SymbolsTable() {
        this.capacity = 1 << 10;
        this.table = new String[this.capacity];
    }

    public Integer addElement(String element) {
        if (size + 1 >= capacity) resize();

        int idx = hash(element, capacity);

        while (idx < capacity) {
            String inHashTable = table[idx];
            if (inHashTable == null) {
                table[idx] = element;
                size++;
                return idx;
            }
            if (inHashTable.equals(element)) {
                return idx;
            }
            idx = idx + 1;
        }

        return null;
    }

    public Integer findElement(String element) {
        int idx = hash(element, capacity);

        while (idx < capacity) {
            String inHashTable = table[idx];
            if (inHashTable == null) return null;
            if (inHashTable.equals(element)) return idx;
            idx = idx + 1;
        }

        return null;
    }

    private void resize() {
        int newCap = capacity << 1;
        String[] old = table;
        table = new String[newCap];
        capacity = newCap;
        size = 0;

        for (String s : old) {
            if (s != null) {
                int idx = hash(s, capacity);
                while (table[idx] != null) {
                    idx = (idx + 1) % capacity;
                }
                table[idx] = s;
                size++;
            }
        }
    }

    private int hash (String value, int cap) {
        return (value.hashCode()) % cap;
    }

    public void printHashTable() {
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                System.out.println("TS element -> index: " + i + " atom: " + table[i]);
            }
        }
    }

}



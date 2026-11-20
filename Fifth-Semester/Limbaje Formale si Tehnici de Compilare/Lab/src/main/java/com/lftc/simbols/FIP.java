package com.lftc.simbols;

public class FIP {
    private final String atom;
    private final int atomsIndex;
    private final int TSIndex;

    public FIP(String atom, int atomsIndex, int TSIndex) {
        this.atom = atom;
        this.atomsIndex = atomsIndex;
        this.TSIndex = TSIndex;
    }

    @Override
    public String toString() {
        return "FIP element " +
                "atom = '" + atom + '\'' +
                ", atomsIndex = " + atomsIndex +
                ", TSIndex = " + TSIndex;
    }
}



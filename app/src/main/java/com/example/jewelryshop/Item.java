package com.example.jewelryshop;

public class Item {
    private String nev;
    private String termekleiras;
    private Tipus tipus;
    private String ar;
    private final int kep;

    public Item(String nev, String termekleiras, Tipus tipus, String ar, int kep) {
        this.nev = nev;
        this.termekleiras = termekleiras;
        this.tipus = tipus;
        this.ar = ar;
        this.kep = kep;
    }

    public String getNev() {
        return nev;
    }

    public String getTermekleiras() {
        return termekleiras;
    }

    public Tipus getTipus() {
        return tipus;
    }

    public String getPrice() {
        return ar;
    }

    public int getKep() {
        return kep;
    }
}

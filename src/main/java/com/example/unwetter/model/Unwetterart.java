package com.example.unwetter.model;
public class Unwetterart {
    private int id;
    private String bezeichnung;

    public Unwetterart(int id, String bezeichnung) {
        this.id = id;
        this.bezeichnung = bezeichnung;
    }

    public int getId() {
        return id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    @Override
    public String toString() {
        return bezeichnung;  // В ComboBox будет видно bezeichnung
    }
}

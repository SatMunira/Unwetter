package com.example.unwetter.model;

public class Ort {
    private int id;
    private String name;

    public Ort(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

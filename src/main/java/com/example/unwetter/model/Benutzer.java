package com.example.unwetter.model;

public class Benutzer {
    private int id;
    private String benutzername;
    private String passwort;
    private String rolle;

    public Benutzer(int id, String benutzername, String passwort, String rolle) {
        this.id = id;
        this.benutzername = benutzername;
        this.passwort = passwort;
        this.rolle = rolle;
    }

    public String getBenutzername() { return benutzername; }
    public String getPasswort() { return passwort; }
    public String getRolle() { return rolle; }

    public int getId() {
        return id;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
}


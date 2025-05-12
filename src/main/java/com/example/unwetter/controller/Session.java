package com.example.unwetter.controller;

import com.example.unwetter.model.Benutzer;

public class Session {
    private static Benutzer aktuellerBenutzer;

    public static void setAktuellerBenutzer(Benutzer benutzer) {
        aktuellerBenutzer = benutzer;
    }

    public static Benutzer getAktuellerBenutzer() {
        return aktuellerBenutzer;
    }
}

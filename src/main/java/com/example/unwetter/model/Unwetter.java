package com.example.unwetter.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Unwetter {
    private int id;
    private int unwetterartId;
    private String unwetterart;
    private int ortId;
    private String ort;
    private LocalDate datum;
    private BigDecimal schadenhoehe;
    private String bemerkung;

    // Конструктор для Insert (без id, с id-шниками FK)
    public Unwetter(int unwetterartId, int ortId, LocalDate datum, BigDecimal schadenhoehe, String bemerkung) {
        this.unwetterartId = unwetterartId;
        this.ortId = ortId;
        this.datum = datum;
        this.schadenhoehe = schadenhoehe;
        this.bemerkung = bemerkung;
    }

    // Конструктор для SELECT (с JOIN данными + id-шники FK)
    public Unwetter(int id, int unwetterartId, String unwetterart, int ortId, String ort,
                    LocalDate datum, BigDecimal schadenhoehe, String bemerkung) {
        this.id = id;
        this.unwetterartId = unwetterartId;
        this.unwetterart = unwetterart;
        this.ortId = ortId;
        this.ort = ort;
        this.datum = datum;
        this.schadenhoehe = schadenhoehe;
        this.bemerkung = bemerkung;
    }

    // Конструктор для SELECT (с JOIN данными, без id-шников FK)
    public Unwetter(int id, String unwetterart, LocalDate datum, String ort, BigDecimal schadenhoehe, String bemerkung) {
        this.id = id;
        this.unwetterart = unwetterart;
        this.datum = datum;
        this.ort = ort;
        this.schadenhoehe = schadenhoehe;
        this.bemerkung = bemerkung;
    }

    // Геттеры
    public int getId() { return id; }
    public int getUnwetterartId() { return unwetterartId; }
    public String getUnwetterart() { return unwetterart; }
    public int getOrtId() { return ortId; }
    public String getOrt() { return ort; }
    public LocalDate getDatum() { return datum; }
    public BigDecimal getSchadenhoehe() { return schadenhoehe; }
    public String getBemerkung() { return bemerkung; }

    // Сеттеры
    public void setId(int id) { this.id = id; }
    public void setUnwetterart(String unwetterart) { this.unwetterart = unwetterart; }
    public void setOrt(String ort) { this.ort = ort; }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public void setSchadenhoehe(BigDecimal schadenhoehe) {
        this.schadenhoehe = schadenhoehe;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }

    public void setUnwetterartId(int unwetterartId) { this.unwetterartId = unwetterartId; }
    public void setOrtId(int ortId) { this.ortId = ortId; }


}

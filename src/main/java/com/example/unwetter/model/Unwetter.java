package com.example.unwetter.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Unwetter {
    private int id;
    private String unwetterart;
    private LocalDate datum;
    private String ort;
    private BigDecimal schadenhoehe;
    private String bemerkung;

    // Конструктор без ID (для создания)
    public Unwetter(String unwetterart, LocalDate datum, String ort, BigDecimal schadenhoehe, String bemerkung) {
        this.unwetterart = unwetterart;
        this.datum = datum;
        this.ort = ort;
        this.schadenhoehe = schadenhoehe;
        this.bemerkung = bemerkung;
    }

    // Конструктор с ID (для чтения из базы)
    public Unwetter(int id, String unwetterart, LocalDate datum, String ort, BigDecimal schadenhoehe, String bemerkung) {
        this(unwetterart, datum, ort, schadenhoehe, bemerkung);
        this.id = id;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUnwetterart() { return unwetterart; }
    public void setUnwetterart(String unwetterart) { this.unwetterart = unwetterart; }

    public LocalDate getDatum() { return datum; }
    public void setDatum(LocalDate datum) { this.datum = datum; }

    public String getOrt() { return ort; }
    public void setOrt(String ort) { this.ort = ort; }

    public BigDecimal getSchadenhoehe() { return schadenhoehe; }
    public void setSchadenhoehe(BigDecimal schadenhoehe) { this.schadenhoehe = schadenhoehe; }

    public String getBemerkung() { return bemerkung; }
    public void setBemerkung(String bemerkung) { this.bemerkung = bemerkung; }
}
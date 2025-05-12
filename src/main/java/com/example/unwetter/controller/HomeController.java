package com.example.unwetter.controller;

import com.example.unwetter.dao.UnwetterDAO;
import com.example.unwetter.model.Unwetter;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController {

    @FXML private Label ereignisseLabel;
    @FXML private Label gesamtschadenLabel;
    @FXML private Label letztesDatumLabel;
    @FXML private LineChart<String, Number> unwetterChart;
    @FXML private VBox ereignisList;
    @FXML private BarChart<String, Number> barChart;
    @FXML private PieChart pieChart;
    @FXML private AreaChart<String, Number> areaChart;


    public void initialize() {
        UnwetterDAO dao = new UnwetterDAO();
        List<Unwetter> daten = dao.getAllUnwetter();

        if (daten.isEmpty()) return;

        // Statistik
        int anzahl = daten.size();
        double gesamtschaden = daten.stream()
                .mapToDouble(u -> u.getSchadenhoehe().doubleValue())
                .sum();
        double durchschnitt = gesamtschaden / anzahl;

        String letztesDatum = daten.stream()
                .max(Comparator.comparing(Unwetter::getDatum))
                .map(u -> u.getDatum().toString())
                .orElse("-");

        ereignisseLabel.setText(String.valueOf(anzahl));
        gesamtschadenLabel.setText(formatNumber(gesamtschaden) + " €");
        letztesDatumLabel.setText(letztesDatum);

        // Letzte 5 Ereignisse
        List<Unwetter> letzteEreignisse = daten.stream()
                .sorted(Comparator.comparing(Unwetter::getDatum).reversed())
                .limit(5)
                .collect(Collectors.toList());

        ereignisList.getChildren().clear();
        for (Unwetter u : letzteEreignisse) {
            Label item = new Label(String.format("%s in %s – %s – %.2f €",
                    u.getUnwetterart(), u.getOrt(), u.getDatum(), u.getSchadenhoehe()));
            ereignisList.getChildren().add(item);
        }

        // Chart
        var groupedData = daten.stream()
                .collect(Collectors.groupingBy(
                        u -> u.getDatum().toString(),
                        Collectors.summingDouble(u -> u.getSchadenhoehe().doubleValue())
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Schadenhöhe");

        groupedData.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey()))
                .forEach(entry -> {
                    series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                });

        unwetterChart.getData().add(series);

        var artGrouped = daten.stream()
                .collect(Collectors.groupingBy(
                        Unwetter::getUnwetterart,
                        Collectors.summingDouble(u -> u.getSchadenhoehe().doubleValue())
                ));

        XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
        barSeries.setName("Gesamtschaden je Unwetterart");

        artGrouped.forEach((art, summe) -> {
            barSeries.getData().add(new XYChart.Data<>(art, summe));
        });

        barChart.getData().add(barSeries);

        var ortGrouped = daten.stream()
                .collect(Collectors.groupingBy(
                        Unwetter::getOrt,
                        Collectors.counting()
                ));

        pieChart.getData().clear();
        ortGrouped.forEach((ort, count) -> {
            pieChart.getData().add(new PieChart.Data(ort, count));
        });

        var monatGrouped = daten.stream()
                .collect(Collectors.groupingBy(
                        u -> u.getDatum().getYear() + "-" + String.format("%02d", u.getDatum().getMonthValue()),
                        Collectors.summingDouble(u -> u.getSchadenhoehe().doubleValue())
                ));

        XYChart.Series<String, Number> areaSeries = new XYChart.Series<>();
        areaSeries.setName("Schaden je Monat");

        monatGrouped.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey()))
                .forEach(entry -> {
                    areaSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                });

        areaChart.getData().add(areaSeries);

    }
    private String formatNumber(double value) {
        if (value >= 1_000_000_000) {
            return String.format("%.1fB", value / 1_000_000_000);
        } else if (value >= 1_000_000) {
            return String.format("%.1fM", value / 1_000_000);
        } else if (value >= 1_000) {
            return String.format("%.1fT", value / 1_000);
        } else {
            return String.format("%.0f", value);
        }
    }

}

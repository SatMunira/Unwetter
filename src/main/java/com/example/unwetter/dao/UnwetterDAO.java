package com.example.unwetter.dao;

import com.example.unwetter.model.Unwetter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

public class UnwetterDAO {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=UnwetterDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "Test123!"; // замени на свой, если другой




    public List<Unwetter> getAllUnwetter() {
        List<Unwetter> list = new ArrayList<>();
        String sql = "SELECT * FROM Unwetter";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String art = rs.getString("unwetterart");
                LocalDate datum = rs.getDate("datum").toLocalDate();
                String ort = rs.getString("ort");
                BigDecimal schaden = rs.getBigDecimal("schadenhoehe");
                String bemerkung = rs.getString("bemerkung");

                list.add(new Unwetter(id, art, datum, ort, schaden, bemerkung));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public void insertUnwetter(Unwetter u) {
        String sql = "INSERT INTO Unwetter (unwetterart, datum, ort, schadenhoehe, bemerkung) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getUnwetterart());
            stmt.setDate(2, Date.valueOf(u.getDatum()));
            stmt.setString(3, u.getOrt());
            stmt.setBigDecimal(4, u.getSchadenhoehe());
            stmt.setString(5, u.getBemerkung());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUnwetter(int id) {
        String sql = "DELETE FROM Unwetter WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTestdaten() {
        String sql = "DELETE FROM Unwetter WHERE bemerkung LIKE 'Test%'";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}

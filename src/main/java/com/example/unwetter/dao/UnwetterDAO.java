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
    private static final String PASSWORD = "Test123!";

    public List<Unwetter> getAllUnwetter() {
        List<Unwetter> list = new ArrayList<>();
        String sql = """
            SELECT u.id, ua.bezeichnung AS unwetterart, u.datum, o.name AS ort, u.schadenhoehe, u.bemerkung
            FROM Unwetter u
            JOIN Unwetterart ua ON u.unwetterart_id = ua.id
            JOIN Ort o ON u.ort_id = o.id
        """;

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
        String sql = "INSERT INTO Unwetter (unwetterart_id, datum, ort_id, schadenhoehe, bemerkung) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, u.getUnwetterartId());
            stmt.setDate(2, Date.valueOf(u.getDatum()));
            stmt.setInt(3, u.getOrtId());
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

    public void updateUnwetter(Unwetter u) {
        String sql = """
        UPDATE Unwetter
        SET unwetterart_id = ?, ort_id = ?, datum = ?, schadenhoehe = ?, bemerkung = ?
        WHERE id = ?
    """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, u.getUnwetterartId());
            stmt.setInt(2, u.getOrtId());
            stmt.setDate(3, Date.valueOf(u.getDatum()));
            stmt.setBigDecimal(4, u.getSchadenhoehe());
            stmt.setString(5, u.getBemerkung());
            stmt.setInt(6, u.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}



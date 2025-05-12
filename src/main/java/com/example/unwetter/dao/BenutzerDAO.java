package com.example.unwetter.dao;

import com.example.unwetter.model.Benutzer;

import java.sql.*;

public class BenutzerDAO {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=UnwetterDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "Test123!";

    public Benutzer login(String username, String password) {
        String sql = "SELECT * FROM Benutzer WHERE benutzername = ? AND passwort = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Benutzer(
                        rs.getInt("id"),
                        rs.getString("benutzername"),
                        rs.getString("passwort"),
                        rs.getString("rolle")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePasswort(int benutzerId, String neuesPasswort) {
        String sql = "UPDATE Benutzer SET passwort = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, neuesPasswort);
            stmt.setInt(2, benutzerId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


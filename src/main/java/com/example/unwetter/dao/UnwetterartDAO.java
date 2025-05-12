package com.example.unwetter.dao;

import com.example.unwetter.model.Unwetterart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnwetterartDAO {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=UnwetterDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "Test123!";

    public List<Unwetterart> getAllUnwetterarten() {
        List<Unwetterart> list = new ArrayList<>();
        String sql = "SELECT id, bezeichnung FROM Unwetterart";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Unwetterart(rs.getInt("id"), rs.getString("bezeichnung")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}

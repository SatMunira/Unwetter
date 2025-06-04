package com.example.unwetter.dao;

import com.example.unwetter.model.Ort;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrtDAO {
    String URL = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=UnwetterDB;"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "loginTimeout=10;";  // 10 секунд таймаут
    String USER = "sa";
    String PASSWORD = "YourStrong!Passw0rd";

    public List<Ort> getAllOrte() {
        List<Ort> list = new ArrayList<>();
        String sql = "SELECT id, name FROM Ort";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Ort(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}


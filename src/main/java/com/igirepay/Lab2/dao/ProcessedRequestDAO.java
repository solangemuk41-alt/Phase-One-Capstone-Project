package com.igirepay.Lab2.dao;

import com.igirepay.util.DBConnection;

import java.sql.*;

public class ProcessedRequestDAO {
    public boolean isAlreadyProcessed(String referenceId) {
        String sql = "SELECT id FROM processed_requests WHERE reference_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, referenceId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Error checking reference ID: " + e.getMessage());
            return false;
        }
    }

    public boolean markAsProcessed(String referenceId) {
        String sql = "INSERT INTO processed_requests (reference_id) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, referenceId);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error saving reference ID: " + e.getMessage());
            return false;
        }
    }
}

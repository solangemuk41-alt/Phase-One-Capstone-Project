package com.igirepay.Lab2.dao;

import com.igirepay.Lab1.model.Transaction;
import com.igirepay.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (account_id, reference_id, transaction_type, amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transaction.getAccountId());
            stmt.setString(2, transaction.getReferenceId());
            stmt.setString(3, transaction.getTransactionType());
            stmt.setDouble(4, transaction.getAmount());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error recording transaction: " + e.getMessage());
            return false;
        }
    }

    public List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                    rs.getInt("id"),
                    rs.getInt("account_id"),
                    rs.getString("reference_id"),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("created_at")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }
        return transactions;
    }

    public Transaction getTransactionByReferenceId(String referenceId) {
        String sql = "SELECT * FROM transactions WHERE reference_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, referenceId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Transaction(
                    rs.getInt("id"),
                    rs.getInt("account_id"),
                    rs.getString("reference_id"),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("created_at")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching transaction: " + e.getMessage());
        }
        return null;
    }
}

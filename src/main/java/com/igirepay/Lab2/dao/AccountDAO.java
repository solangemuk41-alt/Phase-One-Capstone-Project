package com.igirepay.Lab2.dao;

import com.igirepay.Lab1.model.Account;
import com.igirepay.Lab1.model.WalletAccount;
import com.igirepay.Lab1.model.SavingsAccount;
import com.igirepay.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (customer_id, account_type, balance) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, account.getCustomerId());
            stmt.setString(2, account.getAccountType());
            stmt.setDouble(3, account.getBalance());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error creating account: " + e.getMessage());
            return false;
        }
    }

    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM accounts WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String type = rs.getString("account_type");
                int id = rs.getInt("id");
                int customerId = rs.getInt("customer_id");
                double balance = rs.getDouble("balance");
                String createdAt = rs.getString("created_at");

                if (type.equals("WALLET")) {
                    return new WalletAccount(id, customerId, balance, createdAt);
                } else {
                    return new SavingsAccount(id, customerId, balance, createdAt);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching account: " + e.getMessage());
        }
        return null;
    }

    public List<Account> getAccountsByCustomerId(int customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("account_type");
                int id = rs.getInt("id");
                double balance = rs.getDouble("balance");
                String createdAt = rs.getString("created_at");

                if (type.equals("WALLET")) {
                    accounts.add(new WalletAccount(id, customerId, balance, createdAt));
                } else {
                    accounts.add(new SavingsAccount(id, customerId, balance, createdAt));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching accounts: " + e.getMessage());
        }
        return accounts;
    }

    public boolean updateBalance(int accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error updating balance: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAccount(int accountId) {
        String sql = "DELETE FROM accounts WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting account: " + e.getMessage());
            return false;
        }
    }
}

package com.igirepay.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.igirepay.lab3.service.PaymentService;
import com.igirepay.Lab2.dao.AccountDAO;
import com.igirepay.Lab1.model.Account;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionScreen {

    public static void show(VBox contentArea, String type) {
        contentArea.getChildren().clear();

        Label title = new Label(type.equals("DEPOSIT") ? "Deposit Money" : "Withdraw Money");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12; -fx-padding: 25;");
        card.setMaxWidth(450);

        AccountDAO accountDAO = new AccountDAO();
        List<Account> allAccounts = accountDAO.getAccountsByCustomerId(MainApp.loggedInCustomer.getId());

        List<Account> accounts = type.equals("WITHDRAW")
                ? allAccounts.stream()
                  .filter(acc -> acc.getAccountType().equals("SAVINGS"))
                  .collect(Collectors.toList())
                : allAccounts.stream()
                  .filter(acc -> acc.getAccountType().equals("WALLET"))
                  .collect(Collectors.toList());

        ComboBox<String> accountDropdown = new ComboBox<>();
        accountDropdown.setPromptText("Select Account");
        accountDropdown.setMaxWidth(Double.MAX_VALUE);
        accountDropdown.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 5; -fx-font-size: 14;");

        for (Account acc : accounts) {
            accountDropdown.getItems().add(
                    "ID: " + acc.getId() + " | " + acc.getAccountType() +
                            " | Balance: " + String.format("%,.2f", acc.getBalance()) + " RWF"
            );
        }

        if (!accounts.isEmpty()) {
            accountDropdown.getSelectionModel().selectFirst();
        }

        TextField amountField = new TextField();
        amountField.setPromptText("Enter Amount (RWF)");
        amountField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        Label msgLabel = new Label("");
        msgLabel.setFont(Font.font("Arial", 14));
        msgLabel.setWrapText(true);

        Button actionBtn = new Button(type.equals("DEPOSIT") ? "Deposit" : "Withdraw");
        actionBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 12 20; -fx-font-size: 14; -fx-cursor: hand;");

        actionBtn.setOnAction(e -> {
            try {
                int selectedIndex = accountDropdown.getSelectionModel().getSelectedIndex();
                if (selectedIndex < 0) {
                    msgLabel.setTextFill(Color.web("#e94560"));
                    msgLabel.setText("Please select an account!");
                    return;
                }

                int accountId = accounts.get(selectedIndex).getId();
                double amount = Double.parseDouble(amountField.getText().trim());

                if (amount <= 0) {
                    msgLabel.setTextFill(Color.web("#e94560"));
                    msgLabel.setText("Amount must be greater than 0!");
                    return;
                }

                PaymentService service = new PaymentService();
                String result = type.equals("DEPOSIT")
                        ? service.deposit(accountId, amount)
                        : service.withdraw(accountId, amount);

                if (result.contains("successful")) {
                    msgLabel.setTextFill(Color.GREEN);
                    show(contentArea, type);
                } else {
                    msgLabel.setTextFill(Color.web("#e94560"));
                    msgLabel.setText(result);
                }

            } catch (NumberFormatException ex) {
                msgLabel.setTextFill(Color.web("#e94560"));
                msgLabel.setText("Invalid amount! Please enter a valid number.");
            }
        });

        if (accounts.isEmpty()) {
            Label noAccounts = new Label(
                    type.equals("DEPOSIT")
                            ? "No WALLET account found. Please create a Wallet first."
                            : "No SAVINGS account found. Please create a Savings account first."
            );
            noAccounts.setTextFill(Color.web("#e94560"));
            noAccounts.setWrapText(true);
            card.getChildren().add(noAccounts);
        } else {
            card.getChildren().addAll(
                    label("Select Account"), accountDropdown,
                    label("Amount (RWF)"), amountField,
                    msgLabel, actionBtn
            );
        }

        contentArea.getChildren().addAll(title, card);
    }

    static Label label(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#a8a8b3"));
        return l;
    }
}
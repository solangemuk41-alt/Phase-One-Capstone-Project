package com.igirepay.ui;

import javafx.geometry.Pos;
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

        if (type.equals("WITHDRAW")) {
            // Two tab buttons for withdraw direction
            String activeStyle = "-fx-background-color: #e94560; -fx-text-fill: white; " +
                    "-fx-background-radius: 8; -fx-padding: 10 20; -fx-font-size: 13; -fx-cursor: hand;";
            String inactiveStyle = "-fx-background-color: #0f3460; -fx-text-fill: #a8a8b3; " +
                    "-fx-background-radius: 8; -fx-padding: 10 20; -fx-font-size: 13; -fx-cursor: hand;";

            Button savingsToWalletBtn = new Button("Savings → Wallet");
            Button walletToSavingsBtn = new Button("Wallet → Savings");

            savingsToWalletBtn.setStyle(activeStyle);
            walletToSavingsBtn.setStyle(inactiveStyle);

            HBox tabs = new HBox(10, savingsToWalletBtn, walletToSavingsBtn);
            tabs.setAlignment(Pos.CENTER_LEFT);

            VBox tabContent = new VBox();
            showWithdrawForm(tabContent, "SAVINGS_TO_WALLET");

            savingsToWalletBtn.setOnAction(e -> {
                savingsToWalletBtn.setStyle(activeStyle);
                walletToSavingsBtn.setStyle(inactiveStyle);
                showWithdrawForm(tabContent, "SAVINGS_TO_WALLET");
            });

            walletToSavingsBtn.setOnAction(e -> {
                walletToSavingsBtn.setStyle(activeStyle);
                savingsToWalletBtn.setStyle(inactiveStyle);
                showWithdrawForm(tabContent, "WALLET_TO_SAVINGS");
            });

            contentArea.getChildren().addAll(title, tabs, tabContent);

        } else {
            // DEPOSIT
            showDepositForm(contentArea, title);
        }
    }

    private static void showDepositForm(VBox contentArea, Label title) {
        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12; -fx-padding: 25;");
        card.setMaxWidth(450);

        AccountDAO accountDAO = new AccountDAO();
        List<Account> allAccounts = accountDAO.getAccountsByCustomerId(MainApp.loggedInCustomer.getId());

        List<Account> walletAccounts = allAccounts.stream()
                .filter(acc -> acc.getAccountType().equals("WALLET"))
                .collect(Collectors.toList());

        ComboBox<String> accountDropdown = new ComboBox<>();
        accountDropdown.setPromptText("Select Wallet Account");
        accountDropdown.setMaxWidth(Double.MAX_VALUE);
        accountDropdown.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 5; -fx-font-size: 14;");

        for (Account acc : walletAccounts) {
            accountDropdown.getItems().add(
                    "ID: " + acc.getId() +
                            " | Balance: " + String.format("%,.2f", acc.getBalance()) + " RWF"
            );
        }
        if (!walletAccounts.isEmpty()) accountDropdown.getSelectionModel().selectFirst();

        TextField amountField = new TextField();
        amountField.setPromptText("Enter Amount (RWF)");
        amountField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        Label msgLabel = new Label("");
        msgLabel.setFont(Font.font("Arial", 14));
        msgLabel.setWrapText(true);

        Button depositBtn = new Button("Deposit");
        depositBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 12 20; -fx-font-size: 14; -fx-cursor: hand;");

        depositBtn.setOnAction(e -> {
            try {
                int selectedIndex = accountDropdown.getSelectionModel().getSelectedIndex();
                if (selectedIndex < 0) {
                    msgLabel.setTextFill(Color.web("#e94560"));
                    msgLabel.setText("Please select an account!");
                    return;
                }
                int accountId = walletAccounts.get(selectedIndex).getId();
                double amount = Double.parseDouble(amountField.getText().trim());
                if (amount <= 0) {
                    msgLabel.setTextFill(Color.web("#e94560"));
                    msgLabel.setText("Amount must be greater than 0!");
                    return;
                }
                PaymentService service = new PaymentService();
                String result = service.deposit(accountId, amount);
                if (result.contains("successful")) {
                    msgLabel.setTextFill(Color.GREEN);
                    msgLabel.setText("Deposit successful!");
                    show(contentArea, "DEPOSIT");
                } else {
                    msgLabel.setTextFill(Color.web("#e94560"));
                    msgLabel.setText(result);
                }
            } catch (NumberFormatException ex) {
                msgLabel.setTextFill(Color.web("#e94560"));
                msgLabel.setText("Invalid amount! Please enter a valid number.");
            }
        });

        if (walletAccounts.isEmpty()) {
            Label noAccounts = new Label("No WALLET account found. Please create a Wallet first.");
            noAccounts.setTextFill(Color.web("#e94560"));
            noAccounts.setWrapText(true);
            card.getChildren().add(noAccounts);
        } else {
            card.getChildren().addAll(
                    label("Select Wallet Account"), accountDropdown,
                    label("Amount (RWF)"), amountField,
                    msgLabel, depositBtn
            );
        }

        contentArea.getChildren().addAll(title, card);
    }

    private static void showWithdrawForm(VBox contentArea, String direction) {
        contentArea.getChildren().clear();

        AccountDAO accountDAO = new AccountDAO();
        List<Account> allAccounts = accountDAO.getAccountsByCustomerId(MainApp.loggedInCustomer.getId());

        List<Account> fromAccounts = direction.equals("SAVINGS_TO_WALLET")
                ? allAccounts.stream()
                  .filter(acc -> acc.getAccountType().equals("SAVINGS"))
                  .collect(Collectors.toList())
                : allAccounts.stream()
                  .filter(acc -> acc.getAccountType().equals("WALLET"))
                  .collect(Collectors.toList());

        List<Account> toAccounts = direction.equals("SAVINGS_TO_WALLET")
                ? allAccounts.stream()
                  .filter(acc -> acc.getAccountType().equals("WALLET"))
                  .collect(Collectors.toList())
                : allAccounts.stream()
                  .filter(acc -> acc.getAccountType().equals("SAVINGS"))
                  .collect(Collectors.toList());

        String fromLabel = direction.equals("SAVINGS_TO_WALLET") ? "Select Savings Account" : "Select Wallet Account";
        String toLabel   = direction.equals("SAVINGS_TO_WALLET") ? "Destination: Wallet" : "Destination: Savings";
        String btnLabel  = direction.equals("SAVINGS_TO_WALLET") ? "Withdraw to Wallet" : "Move to Savings";
        String fromEmpty = direction.equals("SAVINGS_TO_WALLET")
                ? "No SAVINGS account found. Please create a Savings account first."
                : "No WALLET account found. Please create a Wallet account first.";
        String toEmpty = direction.equals("SAVINGS_TO_WALLET")
                ? "No WALLET account found. Please create a Wallet account first."
                : "No SAVINGS account found. Please create a Savings account first.";

        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12; -fx-padding: 25;");
        card.setMaxWidth(450);

        if (fromAccounts.isEmpty()) {
            Label msg = new Label(fromEmpty);
            msg.setTextFill(Color.web("#e94560"));
            msg.setWrapText(true);
            card.getChildren().add(msg);
            contentArea.getChildren().add(card);
            return;
        }

        if (toAccounts.isEmpty()) {
            Label msg = new Label(toEmpty);
            msg.setTextFill(Color.web("#e94560"));
            msg.setWrapText(true);
            card.getChildren().add(msg);
            contentArea.getChildren().add(card);
            return;
        }

        ComboBox<String> fromDropdown = new ComboBox<>();
        fromDropdown.setPromptText(fromLabel);
        fromDropdown.setMaxWidth(Double.MAX_VALUE);
        fromDropdown.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 5; -fx-font-size: 14;");
        for (Account acc : fromAccounts) {
            fromDropdown.getItems().add(
                    "ID: " + acc.getId() +
                            " | Balance: " + String.format("%,.2f", acc.getBalance()) + " RWF"
            );
        }
        fromDropdown.getSelectionModel().selectFirst();

        // Show destination info
        Label destInfo = new Label(toLabel + ": ID " + toAccounts.get(0).getId() +
                " | Balance: " + String.format("%,.2f", toAccounts.get(0).getBalance()) + " RWF");
        destInfo.setTextFill(Color.web("#a8a8b3"));
        destInfo.setFont(Font.font("Arial", 13));

        TextField amountField = new TextField();
        amountField.setPromptText("Enter Amount (RWF)");
        amountField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        Label msgLabel = new Label("");
        msgLabel.setFont(Font.font("Arial", 14));
        msgLabel.setWrapText(true);

        Button actionBtn = new Button(btnLabel);
        actionBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 12 20; -fx-font-size: 14; -fx-cursor: hand;");

        actionBtn.setOnAction(e -> {
            try {
                int selectedIndex = fromDropdown.getSelectionModel().getSelectedIndex();
                if (selectedIndex < 0) {
                    msgLabel.setTextFill(Color.web("#e94560"));
                    msgLabel.setText("Please select an account!");
                    return;
                }
                int fromId = fromAccounts.get(selectedIndex).getId();
                int toId = toAccounts.get(0).getId();
                double amount = Double.parseDouble(amountField.getText().trim());
                if (amount <= 0) {
                    msgLabel.setTextFill(Color.web("#e94560"));
                    msgLabel.setText("Amount must be greater than 0!");
                    return;
                }
                PaymentService service = new PaymentService();
                String result = service.transfer(fromId, toId, amount);
                if (result.contains("successful")) {
                    msgLabel.setTextFill(Color.GREEN);
                    msgLabel.setText(btnLabel + " successful! No fees applied.");
                    showWithdrawForm(contentArea, direction);
                } else {
                    msgLabel.setTextFill(Color.web("#e94560"));
                    msgLabel.setText(result);
                }
            } catch (NumberFormatException ex) {
                msgLabel.setTextFill(Color.web("#e94560"));
                msgLabel.setText("Invalid amount! Please enter a valid number.");
            }
        });

        card.getChildren().addAll(
                label(fromLabel), fromDropdown,
                label(toLabel), destInfo,
                label("Amount (RWF)"), amountField,
                msgLabel, actionBtn
        );

        contentArea.getChildren().add(card);
    }

    static Label label(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#a8a8b3"));
        return l;
    }
}
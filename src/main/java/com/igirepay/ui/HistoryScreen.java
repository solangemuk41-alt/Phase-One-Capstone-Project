package com.igirepay.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.igirepay.Lab1.model.Account;
import com.igirepay.Lab1.model.Customer;
import com.igirepay.Lab1.model.Transaction;
import com.igirepay.Lab2.dao.AccountDAO;
import com.igirepay.Lab2.dao.CustomerDAO;
import com.igirepay.Lab2.dao.TransactionDAO;
import com.igirepay.lab3.service.PaymentService;

import java.util.List;

public class HistoryScreen {

    public static void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Transaction History");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setTextFill(Color.WHITE);

        AccountDAO accountDAO = new AccountDAO();
        List<Account> accounts = accountDAO.getAccountsByCustomerId(MainApp.loggedInCustomer.getId());

        if (accounts.isEmpty()) {
            Label noAcc = new Label("No accounts found. Please create an account first.");
            noAcc.setTextFill(Color.web("#e94560"));
            contentArea.getChildren().addAll(title, noAcc);
            return;
        }

        ComboBox<String> accountDropdown = new ComboBox<>();
        accountDropdown.setPromptText("Select Account");
        accountDropdown.setMaxWidth(300);
        accountDropdown.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 5; -fx-font-size: 14;");

        for (Account acc : accounts) {
            accountDropdown.getItems().add(
                    "ID: " + acc.getId() + " | " + acc.getAccountType() +
                            " | Balance: " + String.format("%,.2f", acc.getBalance()) + " RWF"
            );
        }
        accountDropdown.getSelectionModel().selectFirst();

        VBox transactionsBox = new VBox(10);
        transactionsBox.setPadding(new Insets(10, 0, 0, 0));

        loadTransactions(transactionsBox, accounts.get(0).getId());

        accountDropdown.setOnAction(e -> {
            int index = accountDropdown.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                loadTransactions(transactionsBox, accounts.get(index).getId());
            }
        });

        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getChildren().addAll(label("Select Account:"), accountDropdown);

        contentArea.getChildren().addAll(title, topBar, transactionsBox);
    }

    private static void loadTransactions(VBox transactionsBox, int accountId) {
        transactionsBox.getChildren().clear();

        PaymentService service = new PaymentService();
        TransactionDAO transactionDAO = new TransactionDAO();
        AccountDAO accountDAO = new AccountDAO();
        CustomerDAO customerDAO = new CustomerDAO();

        List<Transaction> history = service.getHistory(accountId);

        if (history.isEmpty()) {
            Label empty = new Label("No transactions found for this account.");
            empty.setTextFill(Color.web("#a8a8b3"));
            empty.setFont(Font.font("Arial", 14));
            transactionsBox.getChildren().add(empty);
            return;
        }

        Label count = new Label("Showing " + history.size() + " transaction(s)");
        count.setTextFill(Color.web("#a8a8b3"));
        count.setFont(Font.font("Arial", 13));
        transactionsBox.getChildren().add(count);

        for (Transaction tx : history) {
            HBox card = new HBox(15);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new Insets(15, 20, 15, 20));
            card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12;");

            Label icon = new Label(getIcon(tx.getTransactionType()));
            icon.setFont(Font.font("Arial", FontWeight.BOLD, 22));
            icon.setTextFill(getTypeColor(tx.getTransactionType()));
            icon.setMinWidth(40);

            VBox info = new VBox(4);
            HBox.setHgrow(info, Priority.ALWAYS);

            Label type = new Label(tx.getTransactionType());
            type.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            type.setTextFill(getTypeColor(tx.getTransactionType()));

            if (tx.getTransactionType().equals("TRANSFER")) {
                try {

                    List<Transaction> allTx = transactionDAO.getTransactionsByReferenceId(tx.getReferenceId());
                    for (Transaction other : allTx) {
                        if (other.getAccountId() != accountId) {

                            Account otherAccount = accountDAO.getAccountById(other.getAccountId());
                            if (otherAccount != null) {

                                List<Customer> allCustomers = customerDAO.getAllCustomers();
                                for (Customer c : allCustomers) {
                                    if (c.getId() == otherAccount.getCustomerId()) {
                                        Label recipient = new Label(
                                                other.getAccountId() == accountId
                                                        ? "From: " + c.getFullName() + " (" + c.getPhoneNumber() + ")"
                                                        : "To: " + c.getFullName() + " (" + c.getPhoneNumber() + ")"
                                        );
                                        recipient.setTextFill(Color.web("#a8a8b3"));
                                        recipient.setFont(Font.font("Arial", 12));
                                        info.getChildren().add(recipient);
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    // silently ignore if lookup fails
                }
            }

            Label ref = new Label("Ref: " + tx.getReferenceId());
            ref.setTextFill(Color.web("#a8a8b3"));
            ref.setFont(Font.font("Arial", 11));

            Label time = new Label(tx.getTimestamp() != null ? tx.getTimestamp() : "—");
            time.setTextFill(Color.web("#a8a8b3"));
            time.setFont(Font.font("Arial", 11));

            info.getChildren().addAll(type, ref, time);

            // Amount
            boolean isCredit = tx.getTransactionType().equals("DEPOSIT") ||
                    tx.getTransactionType().equals("TRANSFER");
            Label amount = new Label(
                    (isCredit ? "+ " : "- ") +
                            String.format("%,.2f", tx.getAmount()) + " RWF"
            );
            amount.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            amount.setTextFill(isCredit ? Color.web("#00c853") : Color.web("#e94560"));

            card.getChildren().addAll(icon, info, amount);
            transactionsBox.getChildren().add(card);
        }
    }

    private static String getIcon(String type) {
        switch (type) {
            case "DEPOSIT":  return "↓";
            case "WITHDRAW": return "↑";
            case "TRANSFER": return "⇄";
            default:         return "•";
        }
    }

    private static Color getTypeColor(String type) {
        switch (type) {
            case "DEPOSIT":  return Color.web("#00c853");
            case "WITHDRAW": return Color.web("#e94560");
            case "TRANSFER": return Color.web("#1a6fc4");
            default:         return Color.web("#a8a8b3");
        }
    }

    static Label label(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#a8a8b3"));
        l.setFont(Font.font("Arial", 13));
        return l;
    }
}
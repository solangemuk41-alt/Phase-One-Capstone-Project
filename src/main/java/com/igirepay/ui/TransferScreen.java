package com.igirepay.ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.igirepay.lab3.service.PaymentService;
import com.igirepay.Lab2.dao.AccountDAO;
import com.igirepay.Lab2.dao.CustomerDAO;
import com.igirepay.Lab1.model.Account;
import com.igirepay.Lab1.model.Customer;

import java.util.List;

public class TransferScreen {

    public static void show(VBox contentArea) {
        showStep1(contentArea);
    }

    // STEP 1: Enter phone number and amount
    private static void showStep1(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Transfer Money");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        VBox card = new VBox(14);
        card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12; -fx-padding: 25;");
        card.setMaxWidth(450);

        // Recipient phone
        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter recipient phone number");
        phoneField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        // Amount
        TextField amountField = new TextField();
        amountField.setPromptText("Amount to pay (RWF)");
        amountField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        Label msgLabel = new Label("");
        msgLabel.setTextFill(Color.web("#e94560"));
        msgLabel.setWrapText(true);

        Button nextBtn = new Button("NEXT");
        nextBtn.setMaxWidth(Double.MAX_VALUE);
        nextBtn.setStyle("-fx-background-color: #1a6fc4; -fx-text-fill: white; " +
                "-fx-background-radius: 25; -fx-padding: 14; -fx-font-size: 15; " +
                "-fx-font-weight: bold; -fx-cursor: hand;");

        nextBtn.setOnAction(e -> {
            String phone = phoneField.getText().trim();
            String amountText = amountField.getText().trim();

            if (phone.isEmpty() || amountText.isEmpty()) {
                msgLabel.setText("Please fill in all fields!");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException ex) {
                msgLabel.setText("Please enter a valid amount!");
                return;
            }

            if (amount <= 0) {
                msgLabel.setText("Amount must be greater than 0!");
                return;
            }

            if (phone.equals(MainApp.loggedInCustomer.getPhoneNumber())) {
                msgLabel.setText("You cannot transfer to yourself!");
                return;
            }

            CustomerDAO customerDAO = new CustomerDAO();
            Customer recipient = customerDAO.getCustomerByPhone(phone);

            if (recipient == null) {
                msgLabel.setText("No customer found with that phone number!");
                return;
            }


            showStep2(contentArea, recipient, amount);
        });

        card.getChildren().addAll(
                label("Recipient Phone Number"), phoneField,
                label("Amount to pay"), amountField,
                msgLabel, nextBtn
        );

        contentArea.getChildren().addAll(title, card);
    }

    // STEP 2: Confirmation screen
    private static void showStep2(VBox contentArea, Customer recipient, double amount) {
        contentArea.getChildren().clear();

        Label title = new Label("Confirmation");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        double fee = amount > 1000 ? 100.0 : 20.0;
        double totalCost = amount + fee;

        VBox card = new VBox(16);
        card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12; -fx-padding: 30;");
        card.setMaxWidth(450);

        card.getChildren().addAll(
                confirmRow("Name:", recipient.getFullName()),
                confirmRow("Number:", recipient.getPhoneNumber()),
                confirmRow("Amount:", "RWF " + String.format("%,.0f", amount)),
                confirmRow("Fees:", "RWF " + String.format("%,.0f", fee)),
                new Separator(),
                confirmRow("Total cost:", "RWF " + String.format("%,.0f", totalCost))
        );

        Label msgLabel = new Label("");
        msgLabel.setFont(Font.font("Arial", 14));
        msgLabel.setWrapText(true);

        Button payBtn = new Button("PAY");
        payBtn.setMaxWidth(Double.MAX_VALUE);
        payBtn.setStyle("-fx-background-color: #1a6fc4; -fx-text-fill: white; " +
                "-fx-background-radius: 25; -fx-padding: 14; -fx-font-size: 15; " +
                "-fx-font-weight: bold; -fx-cursor: hand;");

        Button editBtn = new Button("EDIT");
        editBtn.setMaxWidth(Double.MAX_VALUE);
        editBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                "-fx-background-radius: 25; -fx-padding: 12; -fx-font-size: 15; " +
                "-fx-border-color: white; -fx-border-radius: 25; -fx-cursor: hand;");

        payBtn.setOnAction(e -> {
            AccountDAO accountDAO = new AccountDAO();

            List<Account> fromAccounts = accountDAO.getAccountsByCustomerId(
                    MainApp.loggedInCustomer.getId());
            if (fromAccounts.isEmpty()) {
                msgLabel.setTextFill(Color.web("#e94560"));
                msgLabel.setText("You have no accounts to send from!");
                return;
            }

            List<Account> toAccounts = accountDAO.getAccountsByCustomerId(recipient.getId());
            if (toAccounts.isEmpty()) {
                msgLabel.setTextFill(Color.web("#e94560"));
                msgLabel.setText("Recipient has no accounts!");
                return;
            }

            int fromId = fromAccounts.get(0).getId();
            int toId = toAccounts.get(0).getId();

            PaymentService service = new PaymentService();
            String result = service.transfer(fromId, toId, totalCost);

            if (result.contains("successful")) {
                showSuccess(contentArea, recipient, amount, fee, totalCost);
            } else {
                msgLabel.setTextFill(Color.web("#e94560"));
                msgLabel.setText(result);
            }
        });

        editBtn.setOnAction(e -> showStep1(contentArea));

        card.getChildren().addAll(msgLabel, payBtn, editBtn);
        contentArea.getChildren().addAll(title, card);
    }

    // STEP 3: Success screen
    private static void showSuccess(VBox contentArea, Customer recipient,
                                    double amount, double fee, double totalCost) {
        contentArea.getChildren().clear();

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12; -fx-padding: 40;");
        box.setMaxWidth(450);

        Label icon = new Label("✓");
        icon.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        icon.setTextFill(Color.GREEN);

        Label successLabel = new Label("Transfer Successful!");
        successLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        successLabel.setTextFill(Color.WHITE);

        Label details = new Label(
                "Sent RWF " + String.format("%,.0f", amount) +
                        " to " + recipient.getFullName() +
                        "\nFees: RWF " + String.format("%,.0f", fee) +
                        "\nTotal deducted: RWF " + String.format("%,.0f", totalCost)
        );
        details.setTextFill(Color.web("#a8a8b3"));
        details.setFont(Font.font("Arial", 14));
        details.setWrapText(true);

        Button doneBtn = new Button("Done");
        doneBtn.setMaxWidth(Double.MAX_VALUE);
        doneBtn.setStyle("-fx-background-color: #1a6fc4; -fx-text-fill: white; " +
                "-fx-background-radius: 25; -fx-padding: 14; -fx-font-size: 15; " +
                "-fx-font-weight: bold; -fx-cursor: hand;");
        doneBtn.setOnAction(e -> showStep1(contentArea));

        box.getChildren().addAll(icon, successLabel, details, doneBtn);
        contentArea.getChildren().addAll(box);
    }

    private static HBox confirmRow(String labelText, String valueText) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);

        Label lbl = new Label(labelText);
        lbl.setTextFill(Color.web("#a8a8b3"));
        lbl.setFont(Font.font("Arial", 14));
        lbl.setPrefWidth(120);

        Label val = new Label(valueText);
        val.setTextFill(Color.WHITE);
        val.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        row.getChildren().addAll(lbl, val);
        return row;
    }

    static Label label(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#a8a8b3"));
        return l;
    }
}
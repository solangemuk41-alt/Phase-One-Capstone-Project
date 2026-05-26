package com.igirepay.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.igirepay.lab3.service.PaymentService;

public class TransactionScreen {

    public static void show(VBox contentArea, String type) {
        contentArea.getChildren().clear();

        Label title = new Label(type.equals("DEPOSIT") ? "Deposit Money" : "Withdraw Money");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12; -fx-padding: 25;");
        card.setMaxWidth(450);

        TextField accountIdField = new TextField();
        accountIdField.setPromptText("Enter Account ID");
        accountIdField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

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
                int accountId = Integer.parseInt(accountIdField.getText().trim());
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
                } else {
                    msgLabel.setTextFill(Color.web("#e94560"));
                }
                msgLabel.setText(result);

            } catch (NumberFormatException ex) {
                msgLabel.setTextFill(Color.web("#e94560"));
                msgLabel.setText("Invalid input! Please enter valid numbers.");
            }
        });

        card.getChildren().addAll(
                label("Account ID"), accountIdField,
                label("Amount (RWF)"), amountField,
                msgLabel, actionBtn
        );

        contentArea.getChildren().addAll(title, card);
    }

    static Label label(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#a8a8b3"));
        return l;
    }
}

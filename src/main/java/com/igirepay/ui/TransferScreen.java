package com.igirepay.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.igirepay.lab3.service.PaymentService;

public class TransferScreen {

    public static void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Transfer Money");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12; -fx-padding: 25;");
        card.setMaxWidth(450);

        TextField fromField = new TextField();
        fromField.setPromptText("From Account ID");
        fromField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        TextField toField = new TextField();
        toField.setPromptText("To Account ID");
        toField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount (RWF)");
        amountField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        Label msgLabel = new Label("");
        msgLabel.setFont(Font.font("Arial", 14));
        msgLabel.setWrapText(true);

        Button transferBtn = new Button("Send Money");
        transferBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 12 20; -fx-font-size: 14; -fx-cursor: hand;");

        transferBtn.setOnAction(e -> {
            try {
                int fromId = Integer.parseInt(fromField.getText().trim());
                int toId = Integer.parseInt(toField.getText().trim());
                double amount = Double.parseDouble(amountField.getText().trim());

                if (amount <= 0) {
                    msgLabel.setTextFill(Color.web("#e94560"));
                    msgLabel.setText("Amount must be greater than 0!");
                    return;
                }

                if (fromId == toId) {
                    msgLabel.setTextFill(Color.web("#e94560"));
                    msgLabel.setText("Cannot transfer to the same account!");
                    return;
                }

                PaymentService service = new PaymentService();
                String result = service.transfer(fromId, toId, amount);

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
                label("From Account ID"), fromField,
                label("To Account ID"), toField,
                label("Amount (RWF)"), amountField,
                msgLabel, transferBtn
        );

        contentArea.getChildren().addAll(title, card);
    }

    static Label label(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#a8a8b3"));
        return l;
    }
}

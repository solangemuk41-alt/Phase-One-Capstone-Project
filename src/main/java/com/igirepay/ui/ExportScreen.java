package com.igirepay.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.igirepay.Lab1.model.Transaction;
import com.igirepay.lab3.service.PaymentService;
import com.igirepay.util.CSVExporter;

import java.util.List;

public class ExportScreen {

    public static void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Export Transactions to CSV");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12; -fx-padding: 25;");
        card.setMaxWidth(450);

        TextField accountIdField = new TextField();
        accountIdField.setPromptText("Enter Account ID");
        accountIdField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        Label msgLabel = new Label("");
        msgLabel.setFont(Font.font("Arial", 14));
        msgLabel.setWrapText(true);

        Button exportBtn = new Button("Export CSV");
        exportBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 12 20; -fx-font-size: 14; -fx-cursor: hand;");

        exportBtn.setOnAction(e -> {
            try {
                int accountId = Integer.parseInt(accountIdField.getText().trim());
                PaymentService service = new PaymentService();
                List<Transaction> history = service.getHistory(accountId);

                if (history.isEmpty()) {
                    msgLabel.setTextFill(Color.web("#e94560"));
                    msgLabel.setText("No transactions found for this account.");
                } else {
                    String filename = "transactions_" + accountId + ".csv";
                    CSVExporter.exportTransactions(history, filename);
                    msgLabel.setTextFill(Color.GREEN);
                    msgLabel.setText("Exported " + history.size() + " transaction(s) to: " + filename);
                }
            } catch (NumberFormatException ex) {
                msgLabel.setTextFill(Color.web("#e94560"));
                msgLabel.setText("Please enter a valid Account ID!");
            }
        });

        card.getChildren().addAll(
                label("Account ID"), accountIdField,
                msgLabel, exportBtn
        );

        contentArea.getChildren().addAll(title, card);
    }

    static Label label(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#a8a8b3"));
        return l;
    }
}

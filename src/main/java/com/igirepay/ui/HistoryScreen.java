package com.igirepay.ui;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.igirepay.Lab1.model.Transaction;
import com.igirepay.lab3.service.PaymentService;

import java.util.List;

public class HistoryScreen {

    public static void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Transaction History");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        HBox searchBox = new HBox(10);
        TextField accountIdField = new TextField();
        accountIdField.setPromptText("Enter Account ID");
        accountIdField.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");
        accountIdField.setPrefWidth(200);

        Button searchBtn = new Button("Load History");
        searchBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand; -fx-font-size: 14;");

        searchBox.getChildren().addAll(accountIdField, searchBtn);

        // Table
        TableView<Transaction> table = new TableView<>();
        table.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
        table.setPrefHeight(400);

        TableColumn<Transaction, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);

        TableColumn<Transaction, String> refCol = new TableColumn<>("Reference ID");
        refCol.setCellValueFactory(new PropertyValueFactory<>("referenceId"));
        refCol.setPrefWidth(200);

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        typeCol.setPrefWidth(100);

        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount (RWF)");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setPrefWidth(120);
        TableColumn<Transaction, String> timeCol = new TableColumn<>("Timestamp");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        timeCol.setPrefWidth(160);
        table.getColumns().addAll(idCol, refCol, typeCol, amountCol, timeCol);

        Label msgLabel = new Label("");
        msgLabel.setTextFill(Color.web("#a8a8b3"));
        searchBtn.setOnAction(e -> {
            try {
                int accountId = Integer.parseInt(accountIdField.getText().trim());
                PaymentService service = new PaymentService();
                List<Transaction> history = service.getHistory(accountId);

                if (history.isEmpty()) {
                    msgLabel.setText("No transactions found for account " + accountId);
                    table.setItems(FXCollections.observableArrayList());
                } else {
                    msgLabel.setText("Found " + history.size() + " transaction(s)");
                    table.setItems(FXCollections.observableArrayList(history));
                }
            } catch (NumberFormatException ex) {
                msgLabel.setText("Please enter a valid Account ID!");
            }
        });

        contentArea.getChildren().addAll(title, searchBox, msgLabel, table);
    }
}

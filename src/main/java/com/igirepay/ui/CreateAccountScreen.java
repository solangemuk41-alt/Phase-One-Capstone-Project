package com.igirepay.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.igirepay.Lab2.dao.AccountDAO;
import com.igirepay.Lab1.model.SavingsAccount;
import com.igirepay.Lab1.model.WalletAccount;

public class CreateAccountScreen {

    public static void show(VBox contentArea, String type) {
        contentArea.getChildren().clear();

        Label title = new Label("Create " + type + " Account");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12; -fx-padding: 25;");
        card.setMaxWidth(450);

        Label desc = new Label(type.equals("WALLET")
                ? "A Wallet Account allows instant transfers with no restrictions."
                : "A Savings Account has a minimum balance of 1,000 RWF and a withdrawal fee of 200 RWF.");
        desc.setTextFill(Color.web("#a8a8b3"));
        desc.setWrapText(true);
        desc.setFont(Font.font("Arial", 13));

        Label msgLabel = new Label("");
        msgLabel.setFont(Font.font("Arial", 14));

        Button createBtn = new Button("Create " + type + " Account");
        createBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 12 20; -fx-font-size: 14; -fx-cursor: hand;");

        createBtn.setOnAction(e -> {
            AccountDAO dao = new AccountDAO();
            boolean success;

            if (type.equals("WALLET")) {
                success = dao.createAccount(new WalletAccount(0, MainApp.loggedInCustomer.getId(), 0.00, null));
            } else {
                success = dao.createAccount(new SavingsAccount(0, MainApp.loggedInCustomer.getId(), 0.00, null));
            }

            if (success) {
                msgLabel.setTextFill(Color.GREEN);
                msgLabel.setText(type + " account created successfully!");
            } else {
                msgLabel.setTextFill(Color.web("#e94560"));
                msgLabel.setText("Failed to create account.");
            }
        });

        card.getChildren().addAll(desc, msgLabel, createBtn);
        contentArea.getChildren().addAll(title, card);
    }
}

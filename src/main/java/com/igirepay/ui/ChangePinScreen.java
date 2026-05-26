package com.igirepay.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.igirepay.lab3.service.AuthService;

public class ChangePinScreen {

    public static void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Change PIN");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12; -fx-padding: 25;");
        card.setMaxWidth(400);

        PasswordField oldPinField = new PasswordField();
        oldPinField.setPromptText("Old PIN");
        oldPinField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        PasswordField newPinField = new PasswordField();
        newPinField.setPromptText("New PIN");
        newPinField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        Label msgLabel = new Label("");
        msgLabel.setFont(Font.font("Arial", 14));

        Button changeBtn = new Button("Change PIN");
        changeBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 12 20; -fx-font-size: 14; -fx-cursor: hand;");

        changeBtn.setOnAction(e -> {
            String oldPin = oldPinField.getText().trim();
            String newPin = newPinField.getText().trim();

            if (oldPin.isEmpty() || newPin.isEmpty()) {
                msgLabel.setTextFill(Color.web("#e94560"));
                msgLabel.setText("Please fill in all fields!");
                return;
            }
            AuthService authService = new AuthService();
            boolean success = authService.changePin(
                    MainApp.loggedInCustomer.getPhoneNumber(), oldPin, newPin);

            if (success) {
                msgLabel.setTextFill(Color.GREEN);
                msgLabel.setText("PIN changed successfully!");
                oldPinField.clear();
                newPinField.clear();
            } else {
                msgLabel.setTextFill(Color.web("#e94560"));
                msgLabel.setText("Old PIN is incorrect!");
            }
        });
        card.getChildren().addAll(
                label("Old PIN"), oldPinField,
                label("New PIN"), newPinField,
                msgLabel, changeBtn
        );
        contentArea.getChildren().addAll(title, card);
    }
    static Label label(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#a8a8b3"));
        return l;
    }
}

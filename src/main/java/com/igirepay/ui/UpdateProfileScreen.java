package com.igirepay.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.igirepay.Lab2.dao.CustomerDAO;

public class UpdateProfileScreen {

    public static void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("Update Profile");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12; -fx-padding: 25;");
        card.setMaxWidth(450);

        TextField nameField = new TextField(MainApp.loggedInCustomer.getFullName());
        nameField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        TextField emailField = new TextField(MainApp.loggedInCustomer.getEmail());
        emailField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        TextField phoneField = new TextField(MainApp.loggedInCustomer.getPhoneNumber());
        phoneField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        Label msgLabel = new Label("");
        msgLabel.setTextFill(Color.web("#e94560"));

        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20; -fx-font-size: 14; -fx-cursor: hand;");

        saveBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                msgLabel.setText("All fields are required!");
                return;
            }

            MainApp.loggedInCustomer.setFullName(name);
            MainApp.loggedInCustomer.setEmail(email);
            MainApp.loggedInCustomer.setPhoneNumber(phone);

            CustomerDAO dao = new CustomerDAO();
            boolean success = dao.updateCustomer(MainApp.loggedInCustomer);

            if (success) {
                msgLabel.setTextFill(Color.GREEN);
                msgLabel.setText("Profile updated successfully!");
            } else {
                msgLabel.setTextFill(Color.web("#e94560"));
                msgLabel.setText("Update failed. Try again.");
            }
        });

        card.getChildren().addAll(
            label("Full Name"), nameField,
            label("Email"), emailField,
            label("Phone Number"), phoneField,
            msgLabel, saveBtn
        );

        contentArea.getChildren().addAll(title, card);
    }

    static Label label(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#a8a8b3"));
        return l;
    }
}

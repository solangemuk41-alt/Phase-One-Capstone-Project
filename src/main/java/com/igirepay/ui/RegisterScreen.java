package com.igirepay.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.igirepay.Lab2.dao.CustomerDAO;
import com.igirepay.Lab1.model.Customer;

public class RegisterScreen {

    public static void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(40));
        centerBox.setMaxWidth(450);

        Label title = new Label("Create Account");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setTextFill(Color.web("#e94560"));

        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: #16213e; -fx-background-radius: 15; -fx-padding: 30;");


        TextField nameField = createField("Full Name", "Eg: Kalisa Jean");
        TextField emailField = createField("Email", "Eg: kalisa@gmail.com");
        TextField phoneField = createField("Phone Number", "Eg: 0788123456");
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("Create a 4-digit PIN");
        pinField.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        Label pinLabel = makeLabel("PIN");
        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.web("#e94560"));

        Button registerBtn = new Button("Register");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 12; -fx-font-size: 15; -fx-font-weight: bold; -fx-cursor: hand;");

        Button backBtn = new Button("Already have an account? Login");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e94560; -fx-cursor: hand; -fx-font-size: 13;");

        registerBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String pin = pinField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || pin.isEmpty()) {
                errorLabel.setText("Please fill in all fields!");
                return;
            }

            CustomerDAO dao = new CustomerDAO();
            Customer customer = new Customer(0, name, email, phone, pin);
            boolean success = dao.addCustomer(customer);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully! Please login.");
                MainApp.showLoginScreen();
            } else {
                errorLabel.setText("Registration failed. Phone or email may already exist.");
            }
        });

        backBtn.setOnAction(e -> MainApp.showLoginScreen());

        card.getChildren().addAll(
                makeLabel("Full Name"), nameField,
                makeLabel("Email"), emailField,
                makeLabel("Phone Number"), phoneField,
                pinLabel, pinField,
                errorLabel, registerBtn, backBtn
        );

        centerBox.getChildren().addAll(title, card);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 900, 650);
        MainApp.primaryStage.setScene(scene);
    }

    static TextField createField(String label, String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");
        return field;
    }

    static Label makeLabel(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.web("#a8a8b3"));
        label.setFont(Font.font("Arial", 13));
        return label;
    }

    static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

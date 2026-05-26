package com.igirepay.ui;

import com.igirepay.lab3.service.AuthService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.igirepay.Lab1.model.Customer;

public class MainApp extends Application {

    public static Stage primaryStage;
    public static Customer loggedInCustomer;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("IgirePay - Digital Wallet");
        primaryStage.setWidth(900);
        primaryStage.setHeight(650);
        primaryStage.setResizable(false);

        showLoginScreen();
        primaryStage.show();
    }

    public static void showLoginScreen() {
        // Root layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Center panel
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(50));
        centerBox.setMaxWidth(400);

        // Logo / Title
        Label title = new Label("IgirePay");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        title.setTextFill(Color.web("#e94560"));

        Label subtitle = new Label("Digital Wallet System");
        subtitle.setFont(Font.font("Arial", 16));
        subtitle.setTextFill(Color.web("#a8a8b3"));

        // Card
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: #16213e; -fx-background-radius: 15; -fx-padding: 30;");
        card.setAlignment(Pos.CENTER_LEFT);

        Label loginLabel = new Label("Login to your account");
        loginLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        loginLabel.setTextFill(Color.WHITE);

        // Phone field
        Label phoneLabel = new Label("Phone Number");
        phoneLabel.setTextFill(Color.web("#a8a8b3"));
        TextField phoneField = new TextField();
        phoneField.setPromptText("07XXXXXXXX");
        phoneField.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        // PIN field
        Label pinLabel = new Label("PIN");
        pinLabel.setTextFill(Color.web("#a8a8b3"));
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("Enter your PIN");
        pinField.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; " +
                "-fx-prompt-text-fill: #555; -fx-background-radius: 8; -fx-padding: 10; -fx-font-size: 14;");

        // Error label
        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.web("#e94560"));
        errorLabel.setFont(Font.font("Arial", 13));

        // Login button
        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 12; -fx-font-size: 15; -fx-font-weight: bold; -fx-cursor: hand;");

        // Register link
        Button registerBtn = new Button("Don't have an account? Register");
        registerBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e94560; " +
                "-fx-cursor: hand; -fx-font-size: 13; -fx-border-color: transparent;");

        // Login action
        loginBtn.setOnAction(e -> {
            String phone = phoneField.getText().trim();
            String pin = pinField.getText().trim();

            if (phone.isEmpty() || pin.isEmpty()) {
                errorLabel.setText("Please fill in all fields!");
                return;
            }

            AuthService authService = new AuthService();
            Customer customer = authService.login(phone, pin);

            if (customer != null) {
                loggedInCustomer = customer;
                DashboardScreen.show();
            } else {
                errorLabel.setText("Wrong phone number or PIN!");
            }
        });

        // Register action
        registerBtn.setOnAction(e -> RegisterScreen.show());

        card.getChildren().addAll(loginLabel, phoneLabel, phoneField, pinLabel, pinField,
                errorLabel, loginBtn, registerBtn);

        centerBox.getChildren().addAll(title, subtitle, card);

        root.setCenter(centerBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

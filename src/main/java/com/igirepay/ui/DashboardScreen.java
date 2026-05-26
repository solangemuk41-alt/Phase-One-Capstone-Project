package com.igirepay.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.igirepay.Lab2.dao.AccountDAO;
import com.igirepay.Lab1.model.Account;

import java.util.List;

public class DashboardScreen {

    static BorderPane root;
    static VBox contentArea;

    public static void show() {

        root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Sidebar (FIXED WITH SCROLL)
        ScrollPane sidebarScroll = new ScrollPane();
        sidebarScroll.setFitToWidth(true);
        sidebarScroll.setStyle("-fx-background: #0f3460; -fx-border-color: transparent;");

        VBox sidebar = buildSidebar();
        sidebarScroll.setContent(sidebar);

        root.setLeft(sidebarScroll);

        // Content area
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(30));
        contentArea.setStyle("-fx-background-color: #16213e;");
        root.setCenter(contentArea);

        showHome();

        Scene scene = new Scene(root, 900, 650);
        MainApp.primaryStage.setScene(scene);
        MainApp.primaryStage.show();
    }

    static VBox buildSidebar() {

        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setStyle("-fx-background-color: #0f3460;");

        // Logo
        Label logo = new Label("IgirePay");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        logo.setTextFill(Color.web("#e94560"));

        Label userLabel = new Label(
                "Hello, " + MainApp.loggedInCustomer.getFullName()
        );
        userLabel.setTextFill(Color.web("#a8a8b3"));
        userLabel.setWrapText(true);

        sidebar.getChildren().addAll(logo, userLabel);

        String[] menuItems = {
                "Home", "Update Profile", "My Accounts", "Create Wallet",
                "Create Savings", "Deposit", "Withdraw", "Transfer", "Transactions",
                "Export CSV", "Change PIN", "Logout"
        };

        for (String item : menuItems) {
            Button btn = new Button(item);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setAlignment(Pos.CENTER_LEFT);

            btn.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #a8a8b3;" +
                            "-fx-font-size: 14;" +
                            "-fx-padding: 10 15;" +
                            "-fx-cursor: hand;"
            );

            btn.setOnMouseEntered(e ->
                    btn.setStyle(
                            "-fx-background-color: #e94560;" + "-fx-text-fill: white;" +
                                    "-fx-font-size: 14;" +
                                    "-fx-padding: 10 15;" +
                                    "-fx-cursor: hand;"
                    )
            );

            btn.setOnMouseExited(e ->
                    btn.setStyle(
                            "-fx-background-color: transparent;" + "-fx-text-fill: #a8a8b3;" +
                                    "-fx-font-size: 14;" +
                                    "-fx-padding: 10 15;" +
                                    "-fx-cursor: hand;"
                    )
            );

            btn.setOnAction(e -> handleMenu(item));

            sidebar.getChildren().add(btn);
        }

        return sidebar;
    }
    static void handleMenu(String item) {

        switch (item) {
            case "Home":
                showHome();
                break;

            case "Update Profile":
                UpdateProfileScreen.show(contentArea);
                break;

            case "My Accounts":
                AccountsScreen.show(contentArea);
                break;

            case "Create Wallet":
                CreateAccountScreen.show(contentArea, "WALLET");
                break;

            case "Create Savings":
                CreateAccountScreen.show(contentArea, "SAVINGS");
                break;

            case "Deposit":
                TransactionScreen.show(contentArea, "DEPOSIT");
                break;

            case "Withdraw":
                TransactionScreen.show(contentArea, "WITHDRAW");
                break;

            case "Transfer":
                TransferScreen.show(contentArea);
                break;

            case "Transactions":
                HistoryScreen.show(contentArea);
                break;

            case "Export CSV":
                ExportScreen.show(contentArea);
                break;

            case "Change PIN":
                ChangePinScreen.show(contentArea);
                break;

            case "Logout":
                logout();
                break;
        }
    }

    // ✅ SAFE LOGOUT
    static void logout() {
        MainApp.loggedInCustomer = null;
        MainApp.showLoginScreen();
    }

    static void showHome() {

        contentArea.getChildren().clear();
        Label welcome = new Label(
                "Welcome back, " + MainApp.loggedInCustomer.getFullName() + "!"
        );
        welcome.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        welcome.setTextFill(Color.WHITE);

        Label sub = new Label("Here is a summary of your accounts:");
        sub.setTextFill(Color.web("#a8a8b3"));

        AccountDAO accountDAO = new AccountDAO();
        List<Account> accounts =
                accountDAO.getAccountsByCustomerId(MainApp.loggedInCustomer.getId());

        VBox accountsBox = new VBox(10);

        if (accounts.isEmpty()) {
            Label noAcc = new Label("No accounts yet. Create one from the sidebar.");
            noAcc.setTextFill(Color.web("#a8a8b3"));
            accountsBox.getChildren().add(noAcc);
        } else {

            for (Account acc : accounts) {

                HBox card = new HBox();
                card.setStyle(
                        "-fx-background-color: #0f3460;" +
                                "-fx-background-radius: 12;" +
                                "-fx-padding: 20;"
                );
                card.setSpacing(20);
                VBox info = new VBox(5);

                Label type = new Label(acc.getAccountType() + " ACCOUNT");
                type.setTextFill(Color.web("#e94560"));
                type.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                Label id = new Label("Account ID: " + acc.getId());
                id.setTextFill(Color.web("#a8a8b3"));

                Label balance = new Label(
                        "Balance: " + String.format("%,.2f", acc.getBalance()) + " RWF"
                );
                balance.setTextFill(Color.WHITE);
                balance.setFont(Font.font("Arial", FontWeight.BOLD, 20));

                info.getChildren().addAll(type, id, balance);
                card.getChildren().add(info);

                accountsBox.getChildren().add(card);
            }
        }

        contentArea.getChildren().addAll(welcome, sub, accountsBox);
    }
}
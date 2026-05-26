package com.igirepay.ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.igirepay.Lab2.dao.AccountDAO;
import com.igirepay.Lab1.model.Account;

import java.util.List;

public class AccountsScreen {

    public static void show(VBox contentArea) {
        contentArea.getChildren().clear();

        Label title = new Label("My Accounts");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        AccountDAO accountDAO = new AccountDAO();
        List<Account> accounts = accountDAO.getAccountsByCustomerId(MainApp.loggedInCustomer.getId());
//        System.out.println;

        VBox accountsList = new VBox(12);

        if (accounts.isEmpty()) {
            Label empty = new Label("No accounts found. Create one from the sidebar.");
            empty.setTextFill(Color.web("#a8a8b3"));
            empty.setFont(Font.font("Arial", 15));
            accountsList.getChildren().add(empty);
        } else {
            for (Account acc : accounts) {
                HBox card = new HBox(20);
                card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 12; -fx-padding: 20;");
                card.setAlignment(Pos.CENTER_LEFT);

                VBox info = new VBox(6);
                HBox.setHgrow(info, Priority.ALWAYS);

                Label type = new Label(acc.getAccountType() + " ACCOUNT");
                type.setFont(Font.font("Arial", FontWeight.BOLD, 13));
                type.setTextFill(Color.web("#e94560"));

                Label id = new Label("ID: " + acc.getId());
                id.setTextFill(Color.web("#a8a8b3"));

                Label balance = new Label(String.format("%,.2f RWF", acc.getBalance()));
//                System.out.println(acc.getBalance());
                balance.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                balance.setTextFill(Color.WHITE);

                info.getChildren().addAll(type, id, balance);

                // Delete button (only for zero balance)
                Button deleteBtn = new Button("Delete");
                deleteBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                        "-fx-background-radius: 8; -fx-padding: 8 15; -fx-cursor: hand;");

                deleteBtn.setOnAction(e -> {
                    if (acc.getBalance() > 0) {
                        showAlert("Cannot delete account with balance! Withdraw first.");
                    } else {
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Delete Account");
                        confirm.setContentText("Are you sure you want to delete account ID: " + acc.getId() + "?");
                        confirm.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                boolean success = accountDAO.deleteAccount(acc.getId());
                                if (success) show(contentArea); // refresh
                                else showAlert("Failed to delete account.");
                            }
                        });
                    }
                });

                card.getChildren().addAll(info, deleteBtn);
                accountsList.getChildren().add(card);
            }
        }

        contentArea.getChildren().addAll(title, accountsList);
    }

    static void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

//    public static void main(String[] args) {
//        AccountDAO accountDAO = new AccountDAO();
//        List<Account> accounts = accountDAO.getAccountsByCustomerId(MainApp.loggedInCustomer.getId());
//
//        for (Account acc : accounts) {
//            System.out.println(acc);
//        }
////        System.out.println(acco);
//    }
}

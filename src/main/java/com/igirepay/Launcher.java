package com.igirepay;

import com.igirepay.console.ConsoleApp;
import com.igirepay.ui.MainApp;
import javafx.application.Application;

import java.util.Scanner;

public class Launcher {

    public static void main(String[] args) {

        System.out.println("=============================");
        System.out.println("   Welcome to IgirePay       ");
        System.out.println("=============================");
        System.out.println();
        System.out.println("Choose mode:");
        System.out.println("1. GUI     (JavaFX Window)");
        System.out.println("2. Console (Terminal)");
        System.out.print("Choose: ");

        Scanner scanner = new Scanner(System.in);

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());

            if (choice == 1) {
                // Launch JavaFX GUI
                Application.launch(MainApp.class, args);
            } else if (choice == 2) {
                // Launch Console
                ConsoleApp.start();
            } else {
                System.out.println("Invalid choice! Starting Console mode by default.");
                ConsoleApp.start();
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Starting Console mode by default.");
            ConsoleApp.start();
        }
    }
}

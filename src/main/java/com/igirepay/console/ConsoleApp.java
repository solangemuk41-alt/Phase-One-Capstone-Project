package com.igirepay.console;

import com.igirepay.Lab1.model.*;
import com.igirepay.Lab2.dao.AccountDAO;
import com.igirepay.Lab2.dao.CustomerDAO;
import com.igirepay.lab3.service.AuthService;
import com.igirepay.lab3.service.PaymentService;
import com.igirepay.util.CSVExporter;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp {

    static Scanner scanner = new Scanner(System.in);
    static AuthService authService = new AuthService();
    static PaymentService paymentService = new PaymentService();
    static CustomerDAO customerDAO = new CustomerDAO();
    static AccountDAO accountDAO = new AccountDAO();
    static Customer loggedInCustomer = null;

    public static void start() {
        System.out.println("\n=============================");
        System.out.println("   IgirePay — Console Mode   ");
        System.out.println("=============================");

        while (true) {
            showMainMenu();
        }
    }

    // ─────────────────────────────────────────
    // MAIN MENU
    // ─────────────────────────────────────────
    static void showMainMenu() {
        System.out.println("\n======= MAIN MENU =======");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose: ");

        int choice = readInt();

        switch (choice) {
            case 1 -> registerCustomer();
            case 2 -> loginMenu();
            case 3 -> { System.out.println("Goodbye!"); System.exit(0); }
            default -> System.out.println("Invalid choice! Try again.");
        }
    }

    // ─────────────────────────────────────────
    // REGISTER
    // ─────────────────────────────────────────
    static void registerCustomer() {
        System.out.println("\n--- Register New Customer ---");
        System.out.print("Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Create PIN: ");
        String pin = scanner.nextLine();

        Customer customer = new Customer(0, name, email, phone, pin);
        boolean success = customerDAO.addCustomer(customer);
        System.out.println(success ? "✓ Registration successful!" : "✗ Registration failed. Try again.");
    }

    // ─────────────────────────────────────────
    // LOGIN
    // ─────────────────────────────────────────
    static void loginMenu() {
        System.out.println("\n--- Login ---");
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("PIN: ");
        String pin = scanner.nextLine();

        loggedInCustomer = authService.login(phone, pin);
        if (loggedInCustomer != null) {
            showDashboard();
        }
    }

    // ─────────────────────────────────────────
    // DASHBOARD
    // ─────────────────────────────────────────
    static void showDashboard() {
        while (true) {
            System.out.println("\n========= DASHBOARD =========");
            System.out.println("Hello, " + loggedInCustomer.getFullName() + "!");
            System.out.println("\n-- Customer Management --");
            System.out.println(" 1. Update My Information");
            System.out.println(" 2. View My Accounts");
            System.out.println("\n-- Account Management --");
            System.out.println(" 3. Create Wallet Account");
            System.out.println(" 4. Create Savings Account");
            System.out.println(" 5. View Account Balance");
            System.out.println(" 6. Delete Inactive Account");
            System.out.println("\n-- Transaction Management --");
            System.out.println(" 7. Deposit Money");
            System.out.println(" 8. Withdraw Money");
            System.out.println(" 9. Transfer Money");
            System.out.println("10. View Transaction History");
            System.out.println("11. Export Transactions to CSV");
            System.out.println("\n-- Settings --");
            System.out.println("12. Change PIN");
            System.out.println("13. Logout");
            System.out.print("\nChoose: ");

            int choice = readInt();

            switch (choice) {
                case 1  -> updateCustomer();
                case 2  -> viewAccounts();
                case 3  -> createAccount("WALLET");
                case 4  -> createAccount("SAVINGS");
                case 5  -> viewBalance();
                case 6  -> deleteAccount();
                case 7  -> depositMoney();
                case 8  -> withdrawMoney();
                case 9  -> transferMoney();
                case 10 -> viewHistory();
                case 11 -> exportCSV();
                case 12 -> changePIN();
                case 13 -> {
                    System.out.println("Logged out successfully.");
                    loggedInCustomer = null;
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ─────────────────────────────────────────
    // CUSTOMER MANAGEMENT
    // ─────────────────────────────────────────
    static void updateCustomer() {
        System.out.println("\n--- Update My Information ---");
        System.out.println("Current Name : " + loggedInCustomer.getFullName());
        System.out.println("Current Email: " + loggedInCustomer.getEmail());
        System.out.println("Current Phone: " + loggedInCustomer.getPhoneNumber());

        System.out.print("New Full Name (Enter to keep): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) loggedInCustomer.setFullName(name);

        System.out.print("New Email (Enter to keep): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) loggedInCustomer.setEmail(email);

        System.out.print("New Phone (Enter to keep): ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty()) loggedInCustomer.setPhoneNumber(phone);

        boolean success = customerDAO.updateCustomer(loggedInCustomer);
        System.out.println(success ? "✓ Information updated!" : "✗ Update failed.");
    }

    static void viewAccounts() {
        System.out.println("\n--- My Accounts ---");
        List<Account> accounts = accountDAO.getAccountsByCustomerId(loggedInCustomer.getId());
        if (accounts.isEmpty()) {
            System.out.println("No accounts found. Create one first.");
        } else {
            for (Account acc : accounts) {
                System.out.println(acc);
            }
        }
    }

    // ─────────────────────────────────────────
    // ACCOUNT MANAGEMENT
    // ─────────────────────────────────────────
    static void createAccount(String type) {
        System.out.println("\n--- Create " + type + " Account ---");
        Account account;
        if (type.equals("WALLET")) {
            account = new WalletAccount(0, loggedInCustomer.getId(), 0.00, null);
        } else {
            account = new SavingsAccount(0, loggedInCustomer.getId(), 0.00, null);
        }
        boolean success = accountDAO.createAccount(account);
        System.out.println(success ? "✓ " + type + " account created!" : "✗ Failed to create account.");
    }

    static void viewBalance() {
        System.out.println("\n--- View Account Balance ---");
        System.out.print("Enter Account ID: ");
        int accountId = readInt();

        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            System.out.println("✗ Account not found!");
        } else {
            System.out.println("Account Type : " + account.getAccountType());
            System.out.println("Balance      : " + String.format("%,.2f", account.getBalance()) + " RWF");
        }
    }

    static void deleteAccount() {
        System.out.println("\n--- Delete Inactive Account ---");
        viewAccounts();
        System.out.print("Enter Account ID to delete: ");
        int accountId = readInt();

        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            System.out.println("✗ Account not found!");
            return;
        }
        if (account.getBalance() > 0) {
            System.out.println("✗ Cannot delete! Balance is " + account.getBalance() + " RWF. Withdraw first.");
            return;
        }

        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("yes")) {
            boolean success = accountDAO.deleteAccount(accountId);
            System.out.println(success ? "✓ Account deleted!" : "✗ Failed to delete.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // ─────────────────────────────────────────
    // TRANSACTION MANAGEMENT
    // ─────────────────────────────────────────
    static void depositMoney() {
        System.out.println("\n--- Deposit Money ---");
        System.out.print("Account ID: ");
        int accountId = readInt();
        System.out.print("Amount (RWF): ");
        double amount = readDouble();

        if (amount <= 0) { System.out.println("✗ Invalid amount!"); return; }
        System.out.println(paymentService.deposit(accountId, amount));
    }

    static void withdrawMoney() {
        System.out.println("\n--- Withdraw Money ---");
        System.out.print("Account ID: ");
        int accountId = readInt();
        System.out.print("Amount (RWF): ");
        double amount = readDouble();

        if (amount <= 0) { System.out.println("✗ Invalid amount!"); return; }
        System.out.println(paymentService.withdraw(accountId, amount));
    }

    static void transferMoney() {
        System.out.println("\n--- Transfer Money ---");
        System.out.print("From Account ID: ");
        int fromId = readInt();
        System.out.print("To Account ID: ");
        int toId = readInt();
        System.out.print("Amount (RWF): ");
        double amount = readDouble();

        if (amount <= 0) { System.out.println("✗ Invalid amount!"); return; }
        System.out.println(paymentService.transfer(fromId, toId, amount));
    }

    static void viewHistory() {
        System.out.println("\n--- Transaction History ---");
        System.out.print("Account ID: ");
        int accountId = readInt();

        List<Transaction> history = paymentService.getHistory(accountId);
        if (history.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("\n" + String.format("%-5s %-40s %-10s %-12s %s",
                    "ID", "Reference ID", "Type", "Amount", "Timestamp"));
            System.out.println("-".repeat(90));
            for (Transaction tx : history) {
                System.out.println(String.format("%-5s %-40s %-10s %-12s %s",
                        tx.getId(),
                        tx.getReferenceId(),
                        tx.getTransactionType(),
                        String.format("%,.2f", tx.getAmount()),
                        tx.getTimestamp()));
            }
        }
    }

    static void exportCSV() {
        System.out.println("\n--- Export Transactions to CSV ---");
        System.out.print("Account ID: ");
        int accountId = readInt();

        List<Transaction> history = paymentService.getHistory(accountId);
        if (history.isEmpty()) {
            System.out.println("✗ No transactions to export.");
        } else {
            CSVExporter.exportTransactions(history, "transactions_" + accountId + ".csv");
        }
    }

    // ─────────────────────────────────────────
    // SETTINGS
    // ─────────────────────────────────────────
    static void changePIN() {
        System.out.println("\n--- Change PIN ---");
        System.out.print("Old PIN: ");
        String oldPin = scanner.nextLine();
        System.out.print("New PIN: ");
        String newPin = scanner.nextLine();

        authService.changePin(loggedInCustomer.getPhoneNumber(), oldPin, newPin);
    }

    // ─────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────
    static int readInt() {
        try {
            int val = Integer.parseInt(scanner.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid number! Using 0.");
            return 0;
        }
    }

    static double readDouble() {
        try {
            double val = Double.parseDouble(scanner.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid number! Using 0.");
            return 0;
        }
    }
}

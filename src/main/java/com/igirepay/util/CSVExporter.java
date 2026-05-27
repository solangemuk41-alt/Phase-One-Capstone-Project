package com.igirepay.util;

import com.igirepay.Lab1.model.Transaction;
import com.igirepay.Lab2.dao.AccountDAO;
import com.igirepay.Lab2.dao.CustomerDAO;
import com.igirepay.Lab2.dao.TransactionDAO;
import com.igirepay.Lab1.model.Account;
import com.igirepay.Lab1.model.Customer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {

    public static void exportTransactions(List<Transaction> transactions, String filename) {
        AccountDAO accountDAO = new AccountDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        TransactionDAO transactionDAO = new TransactionDAO();

        try (FileWriter writer = new FileWriter(filename)) {


            writer.write("ID,Account ID,Account Type,Customer Name,Phone Number," +
                    "Reference ID,Transaction Type,Amount (RWF),Recipient/Sender,Date\n");

            for (Transaction tx : transactions) {

                Account account = accountDAO.getAccountById(tx.getAccountId());
                String accountType = account != null ? account.getAccountType() : "—";


                String customerName = "—";
                String phoneNumber = "—";
                if (account != null) {
                    List<Customer> allCustomers = customerDAO.getAllCustomers();
                    for (Customer c : allCustomers) {
                        if (c.getId() == account.getCustomerId()) {
                            customerName = c.getFullName();
                            phoneNumber = c.getPhoneNumber();
                            break;
                        }
                    }
                }

                String recipientOrSender = "—";
                if (tx.getTransactionType().equals("TRANSFER")) {
                    try {
                        List<Transaction> relatedTx = transactionDAO
                                .getTransactionsByReferenceId(tx.getReferenceId());
                        for (Transaction other : relatedTx) {
                            if (other.getAccountId() != tx.getAccountId()) {
                                Account otherAccount = accountDAO.getAccountById(other.getAccountId());
                                if (otherAccount != null) {
                                    List<Customer> allCustomers = customerDAO.getAllCustomers();
                                    for (Customer c : allCustomers) {
                                        if (c.getId() == otherAccount.getCustomerId()) {
                                            recipientOrSender = c.getFullName() +
                                                    " (" + c.getPhoneNumber() + ")";
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    } catch (Exception e) {
                        recipientOrSender = "—";
                    }
                }

                writer.write(
                        tx.getId() + "," +
                                tx.getAccountId() + "," +
                                accountType + "," +
                                escapeCsv(customerName) + "," +
                                phoneNumber + "," +
                                tx.getReferenceId() + "," +
                                tx.getTransactionType() + "," +
                                String.format("%.2f", tx.getAmount()) + "," +
                                escapeCsv(recipientOrSender) + "," +
                                (tx.getTimestamp() != null ? tx.getTimestamp() : "—") + "\n"
                );
            }

            System.out.println("CSV exported successfully: " + filename);

        } catch (IOException e) {
            System.out.println("Error exporting CSV: " + e.getMessage());
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) return "—";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
package com.igirepay.util;

import com.igirepay.Lab1.model.Transaction;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {

    public static void exportTransactions(List<Transaction> transactions, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {

            writer.write("ID,Account ID,Reference ID,Type,Amount,Timestamp\n");

            for (Transaction tx : transactions) {
                writer.write(
                    tx.getId() + "," +
                    tx.getAccountId() + "," +
                    tx.getReferenceId() + "," +
                    tx.getTransactionType() + "," +
                    tx.getAmount() + "," +
                    tx.getTimestamp() + "\n"
                );
            }

            System.out.println("Transactions exported to " + filename);

        } catch (IOException e) {
            System.out.println("Error exporting CSV: " + e.getMessage());
        }
    }
}

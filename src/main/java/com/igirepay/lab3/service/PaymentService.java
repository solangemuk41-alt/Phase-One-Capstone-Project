package com.igirepay.lab3.service;

import com.igirepay.Lab1.model.Account;
import com.igirepay.Lab1.model.Transaction;
import com.igirepay.Lab2.dao.AccountDAO;
import com.igirepay.Lab2.dao.ProcessedRequestDAO;
import com.igirepay.Lab2.dao.TransactionDAO;

import java.util.List;
import java.util.UUID;

public class PaymentService {

    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();
    private ProcessedRequestDAO processedRequestDAO = new ProcessedRequestDAO();

    public String deposit(int accountId, double amount) {
        String referenceId = UUID.randomUUID().toString();

        if (processedRequestDAO.isAlreadyProcessed(referenceId)) {
            return "Duplicate transaction detected! Rejected.";
        }

        Account account = accountDAO.getAccountById(accountId);
        if (account == null) return "Account not found!";

        double newBalance = account.getBalance() + amount;
        accountDAO.updateBalance(accountId, newBalance);

        Transaction tx = new Transaction(0, accountId, referenceId, "DEPOSIT", amount, null);
        transactionDAO.addTransaction(tx);
        processedRequestDAO.markAsProcessed(referenceId);

        return "Deposit successful! New balance: " + newBalance;
    }

    public String withdraw(int accountId, double amount) {
        String referenceId = UUID.randomUUID().toString();

        if (processedRequestDAO.isAlreadyProcessed(referenceId)) {
            return "Duplicate transaction detected! Rejected.";
        }

        Account account = accountDAO.getAccountById(accountId);
        if (account == null) return "Account not found!";

        if (account.getBalance() < amount) {
            return "Insufficient balance!";
        }

        double newBalance = account.getBalance() - amount;
        accountDAO.updateBalance(accountId, newBalance);

        Transaction tx = new Transaction(0, accountId, referenceId, "WITHDRAW", amount, null);
        transactionDAO.addTransaction(tx);
        processedRequestDAO.markAsProcessed(referenceId);

        return "Withdrawal successful! New balance: " + newBalance;
    }

    public String transfer(int fromAccountId, int toAccountId, double amount) {
        String referenceId = UUID.randomUUID().toString();

        if (processedRequestDAO.isAlreadyProcessed(referenceId)) {
            return "Duplicate transaction detected! Rejected.";
        }

        Account sender = accountDAO.getAccountById(fromAccountId);
        Account receiver = accountDAO.getAccountById(toAccountId);

        if (sender == null || receiver == null) return "Account not found!";
        if (sender.getBalance() < amount) return "Insufficient balance!";

        accountDAO.updateBalance(fromAccountId, sender.getBalance() - amount);
        accountDAO.updateBalance(toAccountId, receiver.getBalance() + amount);

        transactionDAO.addTransaction(new Transaction(0, fromAccountId, referenceId, "TRANSFER", amount, null));
        transactionDAO.addTransaction(new Transaction(0, toAccountId, referenceId + "-IN", "TRANSFER", amount, null));
        processedRequestDAO.markAsProcessed(referenceId);

        return "Transfer successful! " + amount + " sent to account " + toAccountId;
    }

    public List<Transaction> getHistory(int accountId) {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }
}

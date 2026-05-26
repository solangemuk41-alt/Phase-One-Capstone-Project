package com.igirepay.Lab1.model;

public class Transaction {

    private int id;
    private int accountId;
    private String referenceId;
    private String transactionType;
    private double amount;
    private String timestamp;

    public Transaction(int id, int accountId, String referenceId, String transactionType, double amount, String timestamp) {
        this.id = id;
        this.accountId = accountId;
        this.referenceId = referenceId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public int getAccountId() { return accountId; }
    public String getReferenceId() { return referenceId; }
    public String getTransactionType() { return transactionType; }
    public double getAmount() { return amount; }
    public String getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Transaction{ref=" + referenceId + ", type=" + transactionType + ", amount=" + amount + ", time=" + timestamp + "}";
    }
}

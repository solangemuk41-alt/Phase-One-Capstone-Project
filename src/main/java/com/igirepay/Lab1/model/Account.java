package com.igirepay.Lab1.model;

public class Account {

    private int id;
    private int customerId;
    private String accountType;
    private double balance;
    private String createdAt;

    public Account(int id, int customerId, String accountType, double balance, String createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public String getAccountType() { return accountType; }
    public double getBalance() { return balance; }
    public String getCreatedAt() { return createdAt; }

    public void setBalance(double balance) { this.balance = balance; }

    public void deposit(double amount) {
        this.balance += amount;
        System.out.println("Deposited: " + amount);
    }

    public void withdraw(double amount) {
        if (amount > this.balance) {
            System.out.println("Insufficient balance!");
        } else {
            this.balance -= amount;
            System.out.println("Withdrawn: " + amount);
        }
    }

    @Override
    public String toString() {
        return "Account{id=" + id + ", type=" + accountType + ", balance=" + balance + "}";
    }
}

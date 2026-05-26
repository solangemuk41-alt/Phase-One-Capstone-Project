package com.igirepay.Lab1.model;

public class SavingsAccount extends Account {

    private double withdrawalFee;
    private double minimumBalance;

    public SavingsAccount(int id, int customerId, double balance, String createdAt) {
        super(id, customerId, "SAVINGS", balance, createdAt);
        this.withdrawalFee = 200;
        this.minimumBalance = 1000;
    }

    public double getWithdrawalFee() { return withdrawalFee; }
    public double getMinimumBalance() { return minimumBalance; }

    @Override
    public void withdraw(double amount) {
        double total = amount + withdrawalFee;
        if ((getBalance() - total) < minimumBalance) {
            System.out.println("Cannot withdraw! Must maintain minimum balance of " + minimumBalance + " RWF.");
        } else {
            setBalance(getBalance() - total);
            System.out.println("Savings withdrawal successful: " + amount + " (Fee: " + withdrawalFee + ")");
        }
    }

    @Override
    public void deposit(double amount) {
        setBalance(getBalance() + amount);
        System.out.println("Savings deposit successful: " + amount);
    }

    @Override
    public String toString() {
        return "SavingsAccount{id=" + getId() + ", balance=" + getBalance() + ", minBalance=" + minimumBalance + "}";
    }
}

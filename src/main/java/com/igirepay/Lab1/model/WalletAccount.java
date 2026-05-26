package com.igirepay.Lab1.model;

public class WalletAccount extends Account {

    private boolean instantTransfer;

    public WalletAccount(int id, int customerId, double balance, String createdAt) {
        super(id, customerId, "WALLET", balance, createdAt);
        this.instantTransfer = true;
    }

    public boolean isInstantTransfer() { return instantTransfer; }

    @Override
    public void withdraw(double amount) {
        if (amount > getBalance()) {
            System.out.println("Insufficient balance in Wallet!");
        } else {
            setBalance(getBalance() - amount);
            System.out.println("Wallet withdrawal successful: " + amount);
        }
    }

    @Override
    public void deposit(double amount) {
        setBalance(getBalance() + amount);
        System.out.println("Wallet deposit successful: " + amount);
    }

    @Override
    public String toString() {
        return "WalletAccount{id=" + getId() + ", balance=" + getBalance() + ", instantTransfer=" + instantTransfer + "}";
    }
}

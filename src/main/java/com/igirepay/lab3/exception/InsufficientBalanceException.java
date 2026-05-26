package com.igirepay.lab3.exception;


public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(double requested, double available) {
        super(String.format(
                "Insufficient balance: requested %.2f RWF but only %.2f RWF available.",
                requested, available
        ));
    }
}

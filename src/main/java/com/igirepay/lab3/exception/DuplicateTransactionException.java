package com.igirepay.lab3.exception;


public class DuplicateTransactionException extends Exception {
    public DuplicateTransactionException(String referenceId) {
        super("Duplicate transaction rejected — reference already processed: " + referenceId);
    }
}

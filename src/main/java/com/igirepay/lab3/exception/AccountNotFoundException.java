package lab3.exception;

/** Thrown when a referenced account ID does not exist in the database. */
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String accountId) {
        super("Account not found: " + accountId);
    }
}

package lab3.exception;

/** Thrown when a transaction amount is zero or negative. */
public class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) { super(message); }
}

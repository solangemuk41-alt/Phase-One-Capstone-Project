package lab3.exception;

/** Thrown when a request with the same reference ID has already been processed. */
public class DuplicateTransactionException extends Exception {
    public DuplicateTransactionException(String referenceId) {
        super("Duplicate transaction rejected — reference already processed: " + referenceId);
    }
}

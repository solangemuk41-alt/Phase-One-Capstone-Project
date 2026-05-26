package lab3.exception;

/** Thrown when an account does not have enough funds for a withdrawal/transfer. */
public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(double requested, double available) {
        super(String.format(
                "Insufficient balance: requested %.2f RWF but only %.2f RWF available.",
                requested, available
        ));
    }
}

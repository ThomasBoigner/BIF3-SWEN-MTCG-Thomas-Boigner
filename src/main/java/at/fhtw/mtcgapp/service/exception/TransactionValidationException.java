package at.fhtw.mtcgapp.service.exception;

public class TransactionValidationException extends ValidationException {
    private static final String NOT_ENOUGH_COINS = "User %s has not enough coins to purchase a package!";
    private static final String NO_PACKAGES_AVAILABLE = "No packages available!";

    protected TransactionValidationException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    protected TransactionValidationException(String message) {
        super(message);
    }

    public static TransactionValidationException notEnoughCoins(String username) {
        return new TransactionValidationException(String.format(NOT_ENOUGH_COINS, username));
    }

    public static TransactionValidationException noPackagesAvailable() {
        return new TransactionValidationException(NO_PACKAGES_AVAILABLE);
    }
}

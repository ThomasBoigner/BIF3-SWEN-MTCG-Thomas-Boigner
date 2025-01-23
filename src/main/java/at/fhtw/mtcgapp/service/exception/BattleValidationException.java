package at.fhtw.mtcgapp.service.exception;

public class BattleValidationException extends ValidationException {
    private static final String SAME_USER = "You can not battle against yourself!";
    private static final String DECK_NOT_CONFIGURED = "Can not join battle because your deck is not configured!";

    protected BattleValidationException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    protected BattleValidationException(String message) {
        super(message);
    }

    public static BattleValidationException deckNotConfigured() {
        return new BattleValidationException(DECK_NOT_CONFIGURED);
    }

    public static BattleValidationException sameUser() {
        return new BattleValidationException(SAME_USER);
    }
}

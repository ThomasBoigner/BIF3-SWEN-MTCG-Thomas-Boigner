package at.fhtw.mtcgapp.service.exception;

public class TradeValidationException extends ValidationException{
    private static final String CARD_NOT_IN_STACK_OF_USER = "The card you try to trade is not in your stack!";

    protected TradeValidationException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    protected TradeValidationException(String message) {
        super(message);
    }

    public static TradeValidationException cardNotInStackOfUser() {
        return new TradeValidationException(CARD_NOT_IN_STACK_OF_USER);
    }
}

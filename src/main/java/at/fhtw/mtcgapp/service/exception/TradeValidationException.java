package at.fhtw.mtcgapp.service.exception;

import java.util.UUID;

public class TradeValidationException extends ValidationException{
    private static final String CARD_NOT_IN_STACK_OF_USER = "The card you try to trade is not in your stack!";
    private static final String TRADE_NOT_FOUND = "Trade with uuid %s could not be found!";
    private static final String NOT_YOUR_TRADE = "You tried to delete a trade that is not owned by you!";

    protected TradeValidationException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    protected TradeValidationException(String message) {
        super(message);
    }

    public static TradeValidationException cardNotInStackOfUser() {
        return new TradeValidationException(CARD_NOT_IN_STACK_OF_USER);
    }

    public static TradeValidationException tradeNotFound(UUID uuid) {
        return new TradeValidationException(String.format(TRADE_NOT_FOUND, uuid));
    }

    public static TradeValidationException notYourTrade() {
        return new TradeValidationException(NOT_YOUR_TRADE);
    }
}

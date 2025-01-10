package at.fhtw.mtcgapp.service.exception;

import java.util.UUID;

public class TradeValidationException extends ValidationException{
    private static final String CARD_NOT_IN_STACK_OF_USER = "The card you try to trade is not in your stack!";
    private static final String TRADE_NOT_FOUND = "Trade with token %s could not be found!";
    private static final String YOUR_TRADE = "You can not accept your own trade!";
    private static final String CARD_CAN_NOT_BE_FOUND = "Card with token %s could not be found!";
    private static final String CARD_DAMAGE_IS_TOO_LOW = "The damage of the card is lower than the requested minimum damage!";
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

    public static TradeValidationException yourTrade() {
        return new TradeValidationException(YOUR_TRADE);
    }

    public static TradeValidationException cardCanNotBeFound() {
        return new TradeValidationException(CARD_CAN_NOT_BE_FOUND);
    }

    public static TradeValidationException cardDamageIsTooLow() {
        return new TradeValidationException(CARD_DAMAGE_IS_TOO_LOW);
    }

    public static TradeValidationException notYourTrade() {
        return new TradeValidationException(NOT_YOUR_TRADE);
    }
}

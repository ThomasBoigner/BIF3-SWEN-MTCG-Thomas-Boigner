package at.fhtw.mtcgapp.service.exception;

public class CardValidationException extends ValidationException {
    private static final String WRONG_NUMBER_OF_CARDS = "A Deck needs to consist of 4 cards!";
    private static final String USER_DOES_NOT_OWN_CARD = "User does not own a specified card!";

    protected CardValidationException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    protected CardValidationException(String message) {
        super(message);
    }

    public static CardValidationException wrongNumberOfCards() {
        return new CardValidationException(WRONG_NUMBER_OF_CARDS);
    }

    public static CardValidationException userDoesNotOwnCard() {
        return new CardValidationException(USER_DOES_NOT_OWN_CARD);
    }
}

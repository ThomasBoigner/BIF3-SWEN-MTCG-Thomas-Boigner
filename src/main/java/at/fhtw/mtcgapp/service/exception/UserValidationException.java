package at.fhtw.mtcgapp.service.exception;

public class UserValidationException extends ValidationException {
    private static final String USER_WITH_USERNAME_ALREADY_EXISTS = "User with username %s already exists!";

    protected UserValidationException(String message, Exception exception) {
        super(message, exception);
    }

    protected UserValidationException(String message) {super(message);}

    public static UserValidationException userWithUsernameAlreadyExists(String username) {
        return new UserValidationException(USER_WITH_USERNAME_ALREADY_EXISTS.formatted(username));
    }
}

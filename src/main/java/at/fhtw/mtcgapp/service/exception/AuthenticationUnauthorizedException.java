package at.fhtw.mtcgapp.service.exception;

public class AuthenticationUnauthorizedException extends UnauthorizedException {
    private static final String WRONG_CREDENTIALS = "Username or password is incorrect!";
    private static final String INVALID_TOKEN = "Authentication token is invalid!";

    protected AuthenticationUnauthorizedException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
    protected AuthenticationUnauthorizedException(String message) {
        super(message);
    }

    public static AuthenticationUnauthorizedException wrongCredentials() {
        return new AuthenticationUnauthorizedException(WRONG_CREDENTIALS);
    }

    public static AuthenticationUnauthorizedException invalidToken() {
        return new AuthenticationUnauthorizedException(INVALID_TOKEN);
    }
}

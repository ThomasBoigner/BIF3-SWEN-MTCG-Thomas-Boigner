package at.fhtw.mtcgapp.service.exception;

public class AuthenticationUnauthorizedDeniedException extends UnauthorizedException {
    private static final String WRONG_CREDENTIALS = "Username or password is incorrect.";

    protected AuthenticationUnauthorizedDeniedException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
    protected AuthenticationUnauthorizedDeniedException(String message) {
        super(message);
    }

    public static AuthenticationUnauthorizedDeniedException wrongCredentials() {
        return new AuthenticationUnauthorizedDeniedException(WRONG_CREDENTIALS);
    }
}

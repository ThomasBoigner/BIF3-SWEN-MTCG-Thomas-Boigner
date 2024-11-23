package at.fhtw.mtcgapp.service.exception;

public class AuthenticationAccessDeniedException extends AccessDeniedException {
    private static final String WRONG_CREDENTIALS = "Username or password is incorrect.";

    protected AuthenticationAccessDeniedException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
    protected AuthenticationAccessDeniedException(String message) {
        super(message);
    }

    public static AuthenticationAccessDeniedException wrongCredentials() {
        return new AuthenticationAccessDeniedException(WRONG_CREDENTIALS);
    }
}

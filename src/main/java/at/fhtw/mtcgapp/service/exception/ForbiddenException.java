package at.fhtw.mtcgapp.service.exception;

public class ForbiddenException extends RuntimeException {
    private static final String ACCESS_FORBIDDEN = "You are not allowed to access this resource!";

    protected ForbiddenException(String message, Throwable rootCause) { super(message, rootCause);}
    protected ForbiddenException(String message) {super(message);}

    public static ForbiddenException forbidden() {
        return new ForbiddenException(ACCESS_FORBIDDEN);
    }
}

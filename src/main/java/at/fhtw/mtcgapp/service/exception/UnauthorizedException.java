package at.fhtw.mtcgapp.service.exception;

public class UnauthorizedException extends RuntimeException {
    protected UnauthorizedException(String message, Throwable rootCause) { super(message, rootCause);}
    protected UnauthorizedException(String message) { super(message);}
}

package at.fhtw.mtcgapp.service.exception;

public class ValidationException extends RuntimeException {
    protected ValidationException(String message, Throwable rootCause) { super(message, rootCause);}
    protected ValidationException(String message) { super(message);}
}

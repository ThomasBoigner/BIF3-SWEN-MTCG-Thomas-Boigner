package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import at.fhtw.mtcgapp.service.exception.AccessDeniedException;
import at.fhtw.mtcgapp.service.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractController implements RestController {
    protected Response handleServiceErrors(Request request, Function<Request, Response> func) {
        if (func == null) {
            log.warn("Null Function passed to handleServiceErrors");
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try{
            return func.apply(request);
        }
        catch (ConstraintViolationException cExc) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, cExc.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")));
        }
        catch (IllegalArgumentException | ValidationException | NullPointerException e) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, e.getMessage());
        }
        catch (AccessDeniedException e) {
            return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, e.getMessage());
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

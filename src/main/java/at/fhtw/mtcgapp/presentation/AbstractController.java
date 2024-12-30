package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import at.fhtw.mtcgapp.service.exception.AuthenticationUnauthorizedException;
import at.fhtw.mtcgapp.service.exception.ForbiddenException;
import at.fhtw.mtcgapp.service.exception.UnauthorizedException;
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
            log.trace("Handling constraint violation exception of type {}", cExc.getClass());
            return new Response(HttpStatus.BAD_REQUEST, ContentType.PLAIN_TEXT, cExc.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")));
        }
        catch (IllegalArgumentException | ValidationException | NullPointerException e) {
            log.trace("Handling validation exception of type {}", e.getClass());
            return new Response(HttpStatus.BAD_REQUEST, ContentType.PLAIN_TEXT, e.getMessage());
        }
        catch (UnauthorizedException e) {
            log.trace("Handling unauthorized exception of type {}", e.getClass());
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.PLAIN_TEXT, e.getMessage());
        }
        catch (ForbiddenException e) {
            log.trace("Handling forbidden exception of type {}", e.getClass());
            return new Response(HttpStatus.FORBIDDEN, ContentType.PLAIN_TEXT, e.getMessage());
        }
        catch (Exception e) {
            log.trace("Handling exception of type {}", e.getClass());
            log.error(e.getMessage(), e);
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected String extractAuthToken(HeaderMap headerMap) {
        if (headerMap == null || headerMap.getHeader("Authorization") == null) {
            throw AuthenticationUnauthorizedException.noTokenProvided();
        }
        return headerMap.getHeader("Authorization").replace("Bearer ", "");
    }
}

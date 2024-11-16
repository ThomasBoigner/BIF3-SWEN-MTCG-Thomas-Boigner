package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import at.fhtw.mtcgapp.service.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

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
        catch (ValidationException validationException) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, validationException.getMessage());
        }
        catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

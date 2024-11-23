package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.AuthenticationService;
import at.fhtw.mtcgapp.service.command.LoginCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@RequiredArgsConstructor

@Slf4j
public class AuthenticationController extends AbstractController{
    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;

    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.POST && request.getPathname().equals("/sessions")) {
            return handleServiceErrors(request, this::login);
        }
        if (request.getMethod() == Method.GET && request.getPathname().equals("/sessions/logout")) {
            return handleServiceErrors(request, this::logout);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response login(Request request) {
        log.debug("Incoming http POST login user request {}", request);
        Objects.requireNonNull(request.getBody(), "Body must not be null!");

        LoginCommand command;
        try {
            command = objectMapper.readValue(request.getBody(), LoginCommand.class);
        } catch (JsonMappingException e) {
            log.warn("Request body with wrong format was received {}!", request.getBody());
            return new Response(HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            log.warn("Could not deserialize the login user command {}!", request.getBody());
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String token = authenticationService.loginUser(command);

        return new Response(HttpStatus.OK, ContentType.JSON, token);
    }

    private Response logout(Request request) {
        log.debug("Incoming http GET logout user request {}", request);

        if (request.getHeaderMap() == null || request.getHeaderMap().getHeader("Authorization") == null) {
            return new Response(HttpStatus.UNAUTHORIZED);
        }

        authenticationService.logoutUser(request.getHeaderMap().getHeader("Authorization").replace("Bearer ", ""));
        return new Response(HttpStatus.OK);
    }
}

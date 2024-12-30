package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.UserService;
import at.fhtw.mtcgapp.service.command.CreateUserCommand;
import at.fhtw.mtcgapp.service.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@RequiredArgsConstructor

@Slf4j
public class UserController extends AbstractController {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET && request.getPathname().contains("/users") && request.getPathParts().size() == 2) {
            return handleServiceErrors(request, this::getUser);
        }
        if (request.getMethod() == Method.POST && request.getPathname().equals("/users")) {
            return handleServiceErrors(request, this::createUser);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response getUser(Request request) {
        log.debug("Incoming http GET request {}", request);

        UserDto userDto = userService.getUser(extractAuthToken(request.getHeaderMap()), request.getPathParts().get(1));

        String json;
        try {
            json = objectMapper.writeValueAsString(userDto);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize the user dto!", e);
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new Response(HttpStatus.OK, ContentType.JSON, json);
    }

    private Response createUser(Request request) {
        log.debug("Incoming http POST request {}", request);
        Objects.requireNonNull(request.getBody(), "Body must not be null!");

        CreateUserCommand command;
        try {
            command = objectMapper.readValue(request.getBody(), CreateUserCommand.class);
        } catch (JsonMappingException e) {
            log.warn("Request body with wrong format was received {}!", request.getBody());
            return new Response(HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            log.warn("Could not deserialize the create user command {}!", request.getBody());
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        UserDto userDto = userService.createUser(command);

        String json;
        try {
            json = objectMapper.writeValueAsString(userDto);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize the user dto!", e);
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new Response(HttpStatus.CREATED, ContentType.JSON, json);
    }
}

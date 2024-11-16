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

@RequiredArgsConstructor

@Slf4j
public class UserController extends AbstractController {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST) {
            return handleServiceErrors(request, this::createUser);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response createUser(Request request) {
        log.debug("Incoming http POST request {}", request);

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
        log.debug("{}", command);

        UserDto userDto = userService.createUser(command);

        log.debug("{}", userDto);
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

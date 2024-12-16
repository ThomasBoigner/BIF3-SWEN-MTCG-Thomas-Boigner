package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.PackageService;
import at.fhtw.mtcgapp.service.command.CreateCardCommand;
import at.fhtw.mtcgapp.service.dto.PackageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor

@Slf4j
public class PackageController extends AbstractController {
    private final PackageService packageService;
    private final ObjectMapper objectMapper;

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST && request.getPathname().equals("/packages")) {
            return handleServiceErrors(request, this::createPackage);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response createPackage(Request request) {
        log.debug("Incoming http POST request {}", request);
        Objects.requireNonNull(request.getBody(), "Body must not be null!");

        List<CreateCardCommand> commands;
        try {
            commands = objectMapper.readValue(request.getBody(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CreateCardCommand.class));
        } catch (JsonMappingException e) {
            log.warn("Request body with wrong format was received {}!", request.getBody());
            return new Response(HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            log.warn("Could not deserialize the create card command {}!", request.getBody());
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PackageDto packageDto = packageService.createPackage(extractAuthToken(request.getHeaderMap()), commands);

        String json;
        try {
            json = objectMapper.writeValueAsString(packageDto);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize the package dto!", e);
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new Response(HttpStatus.CREATED, ContentType.JSON, json);
    }
}

package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.CardService;
import at.fhtw.mtcgapp.service.dto.CardDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor

@Slf4j
public class DeckController extends AbstractController {
    private final CardService cardService;
    private final ObjectMapper objectMapper;

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET && request.getPathname().equals("/deck")) {
            return handleServiceErrors(request, this::getDeck);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response getDeck(Request request) {
        log.debug("Incoming http GET request {}", request);

        List<CardDto> cardDtos = cardService.getDeckOfUser(extractAuthToken(request.getHeaderMap()));

        String json;
        try {
            json = objectMapper.writeValueAsString(cardDtos);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize the card dtos!", e);
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new Response(HttpStatus.OK, ContentType.JSON, json);
    }
}

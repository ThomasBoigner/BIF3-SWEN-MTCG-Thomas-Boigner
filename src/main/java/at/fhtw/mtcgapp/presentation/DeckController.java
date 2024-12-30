package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.CardService;
import at.fhtw.mtcgapp.service.dto.CardDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
        if (request.getMethod() == Method.PUT && request.getPathname().equals("/deck")) {
            return handleServiceErrors(request, this::configureDeck);
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

        if(request.getParams() != null && request.getParams().contains("format=plain")) {
            return new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, cardDtos.toString());
        }

        String json;
        try {
            json = objectMapper.writeValueAsString(cardDtos);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize the card dtos!", e);
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new Response(HttpStatus.OK, ContentType.JSON, json);
    }

    private Response configureDeck(Request request) {
        log.debug("Incoming http PUT request {}", request);
        Objects.requireNonNull(request.getBody(), "Body must not be null!");

        List<UUID> cardIds;
        try {
            cardIds = objectMapper.readValue(request.getBody(), objectMapper.getTypeFactory().constructCollectionType(List.class, UUID.class));
        } catch (JsonMappingException e) {
            log.warn("Request body with wrong format was received {}!", request.getBody());
            return new Response(HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            log.warn("Could not deserialize the ids {}!", request.getBody());
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        cardService.configureDeck(extractAuthToken(request.getHeaderMap()), cardIds);

        return new Response(HttpStatus.OK);
    }
}

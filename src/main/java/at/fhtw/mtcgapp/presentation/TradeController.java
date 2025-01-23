package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.TradeService;
import at.fhtw.mtcgapp.service.command.CreateTradeCommand;
import at.fhtw.mtcgapp.service.dto.TradeDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor

@Slf4j
public class TradeController extends AbstractController {
    private final TradeService tradeService;
    private final ObjectMapper objectMapper;

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET && request.getPathname().equals("/tradings")) {
            return handleServiceErrors(request, this::getTrades);
        }
        if (request.getMethod() == Method.POST && request.getPathname().equals("/tradings")) {
            return handleServiceErrors(request, this::createTrade);
        }
        if (request.getMethod() == Method.POST && request.getPathname().contains("/tradings") && request.getPathParts().size() == 2) {
            return handleServiceErrors(request, this::acceptTrade);
        }
        if (request.getMethod() == Method.DELETE && request.getPathname().contains("/tradings") && request.getPathParts().size() == 2) {
            return handleServiceErrors(request, this::deleteTrade);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response getTrades(Request request) {
        log.debug("Incoming http GET request {}", request);

        List<TradeDto> trades = tradeService.getTrades(extractAuthToken(request.getHeaderMap()));

        String json;
        try {
            json = objectMapper.writeValueAsString(trades);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize the trade dtos!", e);
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new Response(HttpStatus.OK, ContentType.JSON, json);
    }

    private Response createTrade(Request request) {
        log.debug("Incoming http POST request {}", request);
        Objects.requireNonNull(request.getBody(), "Body must not be null!");

        CreateTradeCommand command;
        try {
            command = objectMapper.readValue(request.getBody(), CreateTradeCommand.class);
        } catch (JsonMappingException e) {
            log.warn("Request body with wrong format was received {}!", request.getBody());
            return new Response(HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            log.warn("Could not deserialize the create trade command {}!", request.getBody());
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        TradeDto tradeDto = tradeService.createTrade(extractAuthToken(request.getHeaderMap()), command);

        String json;
        try {
            json = objectMapper.writeValueAsString(tradeDto);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize the trade dto!", e);
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new Response(HttpStatus.CREATED, ContentType.JSON, json);
    }

    private Response acceptTrade(Request request) {
        log.debug("Incoming http POST request {}", request);
        Objects.requireNonNull(request.getBody(), "Body must not be null!");

        UUID cardId;
        try {
            cardId = objectMapper.readValue(request.getBody(), UUID.class);
        } catch (JsonMappingException e) {
            log.warn("Request body with wrong format was received {}!", request.getBody());
            return new Response(HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            log.warn("Could not deserialize the card id {}!", request.getBody());
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        tradeService.acceptTrade(extractAuthToken(request.getHeaderMap()), UUID.fromString(request.getPathParts().get(1)), cardId);

        return new Response(HttpStatus.CREATED);
    }

    private Response deleteTrade(Request request) {
        log.debug("Incoming http DELETE request {}", request);

        tradeService.deleteTrade(extractAuthToken(request.getHeaderMap()), UUID.fromString(request.getPathParts().get(1)));

        return new Response(HttpStatus.OK);
    }
}

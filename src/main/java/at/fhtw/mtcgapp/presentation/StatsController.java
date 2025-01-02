package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.StatsService;
import at.fhtw.mtcgapp.service.dto.UserStatsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor

@Slf4j
public class StatsController extends AbstractController {
    private final StatsService statsService;
    private final ObjectMapper objectMapper;

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET && request.getPathname().equals("/stats")) {
            return handleServiceErrors(request, this::getUserStats);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response getUserStats(Request request) {
        log.debug("Incoming http GET request {}", request);

        UserStatsDto userStatsDto = statsService.getUserStats(extractAuthToken(request.getHeaderMap()));

        String json;
        try {
            json = objectMapper.writeValueAsString(userStatsDto);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize the user stats dto!", e);
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new Response(HttpStatus.OK, ContentType.JSON, json);
    }
}

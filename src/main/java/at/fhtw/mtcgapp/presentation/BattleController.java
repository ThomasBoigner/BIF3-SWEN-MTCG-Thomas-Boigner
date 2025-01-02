package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.BattleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor

@Slf4j
public class BattleController extends AbstractController {
    private final BattleService battleService;

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST && request.getPathname().equals("/battles")) {
            return handleServiceErrors(request, this::battleUser);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response battleUser(Request request) {
        log.info("Incoming http POST request {}", request);

        battleService.battleUser(extractAuthToken(request.getHeaderMap()));

        return new Response(HttpStatus.OK);
    }
}

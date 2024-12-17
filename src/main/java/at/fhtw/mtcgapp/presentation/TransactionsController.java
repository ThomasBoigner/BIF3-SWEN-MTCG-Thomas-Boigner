package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor

@Slf4j
public class TransactionsController extends AbstractController {
    private final PackageService packageService;

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST && request.getPathname().equals("/transactions/packages")) {
            return handleServiceErrors(request, this::acquirePackage);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response acquirePackage(Request request) {
        log.debug("Incoming http POST request {}", request);
        packageService.acquirePackage(extractAuthToken(request.getHeaderMap()));
        return new Response(HttpStatus.CREATED);
    }
}

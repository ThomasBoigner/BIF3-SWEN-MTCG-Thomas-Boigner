package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.PackageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    private TransactionsController transactionsController;
    @Mock
    private PackageService packageService;

    @BeforeEach
    void setUp(){
        transactionsController = new TransactionsController(packageService);
    }

    @Test
    void ensureAcquirePackageWorksProperly() {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        Request request = Request.builder()
                .method(Method.POST)
                .headerMap(headerMap)
                .pathname("/transactions/packages")
                .build();

        // When
        Response response = transactionsController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        verify(packageService).acquirePackage(eq("Thomas-mtgcToken"));
    }
}

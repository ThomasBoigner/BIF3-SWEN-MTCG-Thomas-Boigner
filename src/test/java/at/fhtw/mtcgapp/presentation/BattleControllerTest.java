package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.BattleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class BattleControllerTest {
    private BattleController battleController;
    @Mock
    private BattleService battleService;

    @BeforeEach
    void setUp() {
        battleController = new BattleController(battleService);
    }

    @Test
    void ensureBattleUserWorksProperly() {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        Request request = Request.builder()
                .method(Method.POST)
                .headerMap(headerMap)
                .pathname("/battles")
                .build();

        // When
        Response response = battleController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }
}

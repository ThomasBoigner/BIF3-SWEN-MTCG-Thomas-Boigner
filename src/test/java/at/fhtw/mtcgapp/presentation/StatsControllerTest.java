package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.StatsService;
import at.fhtw.mtcgapp.service.dto.UserStatsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatsControllerTest {
    private StatsController statsController;
    @Mock
    private StatsService statsService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        statsController = new StatsController(statsService, objectMapper);
    }

    @Test
    void ensureGetUserStatsWorksProperly() throws JsonProcessingException {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        UserStatsDto userStatsDto = UserStatsDto.builder()
                .name("Thomas")
                .elo(100)
                .wins(5)
                .losses(3)
                .build();


        Request request = Request.builder()
                .method(Method.GET)
                .headerMap(headerMap)
                .pathname("/stats")
                .build();

        when(statsService.getUserStats(eq("Thomas-mtgcToken"))).thenReturn(userStatsDto);

        // When
        Response response = statsController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("OK");
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContent()).isEqualTo(objectMapper.writeValueAsString(userStatsDto));
    }
}

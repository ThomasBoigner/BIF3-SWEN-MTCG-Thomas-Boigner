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

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScoreBoardControllerTest {
    private ScoreBoardController scoreBoardController;
    @Mock
    private StatsService statsService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
        scoreBoardController = new ScoreBoardController(statsService, objectMapper);
    }

    @Test
    void ensureGetScoreBoardWorksProperly() throws JsonProcessingException {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        UserStatsDto userStatsDto1 = UserStatsDto.builder()
                .name("Thomas")
                .elo(100)
                .wins(5)
                .losses(3)
                .build();

        UserStatsDto userStatsDto2 = UserStatsDto.builder()
                .name("Max")
                .elo(200)
                .wins(10)
                .losses(6)
                .build();

        Request request = Request.builder()
                .method(Method.GET)
                .headerMap(headerMap)
                .pathname("/scoreboard")
                .build();

        when(statsService.getScoreBoard(eq("Thomas-mtgcToken"))).thenReturn(List.of(userStatsDto1, userStatsDto2));

        // When
        Response response = scoreBoardController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("OK");
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContent()).isEqualTo(objectMapper.writeValueAsString(List.of(userStatsDto1, userStatsDto2)));
    }
}

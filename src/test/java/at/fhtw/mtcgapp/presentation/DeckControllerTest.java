package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.model.DamageType;
import at.fhtw.mtcgapp.service.CardService;
import at.fhtw.mtcgapp.service.dto.CardDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeckControllerTest {
    private DeckController deckController;
    @Mock
    private CardService cardService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        deckController = new DeckController(cardService, objectMapper);
    }

    @Test
    void ensureGetDeckWorksProperly() throws JsonProcessingException {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        List<CardDto> cardDtos = List.of(
                CardDto.builder()
                        .id(UUID.randomUUID())
                        .name("test1")
                        .damage(10)
                        .damageType(DamageType.NORMAL.getDbValue())
                        .build(),
                CardDto.builder()
                        .id(UUID.randomUUID())
                        .name("test2")
                        .damage(20)
                        .damageType(DamageType.FIRE.getDbValue())
                        .build());

        Request request = Request.builder()
                .method(Method.GET)
                .headerMap(headerMap)
                .pathname("/deck")
                .build();

        when(cardService.getDeckOfUser(eq("Thomas-mtgcToken"))).thenReturn(cardDtos);

        // When
        Response response = deckController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("OK");
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContent()).isEqualTo(objectMapper.writeValueAsString(cardDtos));
    }
}

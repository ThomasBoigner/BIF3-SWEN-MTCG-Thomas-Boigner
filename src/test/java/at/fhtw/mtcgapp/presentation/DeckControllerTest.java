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
import static org.mockito.Mockito.verify;
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

    @Test
    void ensureConfigureDeckWorksProperly() throws JsonProcessingException {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        List<String> cardIds = List.of("aa9999a0-734c-49c6-8f4a-651864b14e62", "d6e9c720-9b5a-40c7-a6b2-bc34752e3463", "d60e23cf-2238-4d49-844f-c7589ee5342e", "02a9c76e-b17d-427f-9240-2dd49b0d3bfd");

        Request request = Request.builder()
                .method(Method.PUT)
                .headerMap(headerMap)
                .pathname("/deck")
                .body(objectMapper.writeValueAsString(cardIds))
                .build();

        // When
        Response response = deckController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        verify(cardService).configureDeck(eq("Thomas-mtgcToken"), eq(cardIds));
    }

    @Test
    void ensureConfigureDeckReturnsStatus400WhenCardIdsCanNotBeParsed() {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        Request request = Request.builder()
                .method(Method.PUT)
                .headerMap(headerMap)
                .pathname("/deck")
                .body("")
                .build();

        // When
        Response response = deckController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void ensureConfigureDeckReturnsStatus400WhenBodyIsNull() {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        Request request = Request.builder()
                .method(Method.PUT)
                .headerMap(headerMap)
                .pathname("/deck")
                .body(null)
                .build();

        // When
        Response response = deckController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }
}

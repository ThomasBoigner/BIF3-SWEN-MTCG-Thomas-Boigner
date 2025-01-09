package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.TradeService;
import at.fhtw.mtcgapp.service.command.CreateTradeCommand;
import at.fhtw.mtcgapp.service.dto.CardDto;
import at.fhtw.mtcgapp.service.dto.TradeDto;
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
public class TradeControllerTest {
    private TradeController tradeController;
    @Mock
    private TradeService tradeService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        tradeController = new TradeController(tradeService, objectMapper);
    }

    @Test
    void ensureGetTradesWorksProperly() throws JsonProcessingException {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        Request request = Request.builder()
                .method(Method.GET)
                .headerMap(headerMap)
                .pathname("/tradings")
                .build();

        TradeDto trade1 = TradeDto.builder()
                .id(UUID.randomUUID())
                .cardToTrade(CardDto.builder()
                        .name("Dragon")
                        .damageType("fire")
                        .damage(50)
                        .build())
                .minimumDamage(50)
                .type("monster")
                .build();

        TradeDto trade2 = TradeDto.builder()
                .id(UUID.randomUUID())
                .cardToTrade(CardDto.builder()
                        .name("Water Spell")
                        .damageType("water")
                        .damage(20)
                        .build())
                .minimumDamage(15)
                .type("monster")
                .build();

        List<TradeDto> tradeDtos = List.of(trade1, trade2);

        when(tradeService.getTrades(eq("Thomas-mtgcToken"))).thenReturn(tradeDtos);

        // When
        Response response = tradeController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("OK");
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContent()).isEqualTo(objectMapper.writeValueAsString(tradeDtos));
    }

    @Test
    void ensureCreateTradeWorksProperly() throws JsonProcessingException {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        CreateTradeCommand command = CreateTradeCommand.builder()
                .id(UUID.randomUUID())
                .cardToTrade(UUID.randomUUID())
                .minimumDamage(50)
                .type("monster")
                .build();

        Request request = Request.builder()
                .method(Method.POST)
                .headerMap(headerMap)
                .pathname("/tradings")
                .body(objectMapper.writeValueAsString(command))
                .build();


        TradeDto tradeDto = TradeDto.builder()
                .id(UUID.randomUUID())
                .cardToTrade(CardDto.builder()
                        .name("Dragon")
                        .damageType("fire")
                        .damage(50)
                        .build())
                .minimumDamage(50)
                .type("monster")
                .build();

        when(tradeService.createTrade(eq("Thomas-mtgcToken"), eq(command))).thenReturn(tradeDto);

        // When
        Response response = tradeController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getMessage()).isEqualTo("CREATED");
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContent()).isEqualTo(objectMapper.writeValueAsString(tradeDto));
    }

    @Test
    void ensureCreateTradeReturnsStatus400WhenCommandCanNotBeParsed() {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        Request request = Request.builder()
                .method(Method.POST)
                .headerMap(headerMap)
                .pathname("/tradings")
                .body("")
                .build();

        // When
        Response response = tradeController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void ensureCreateTradeReturnsStatus400WhenBodyIsNull() {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        Request request = Request.builder()
                .method(Method.POST)
                .headerMap(headerMap)
                .pathname("/tradings")
                .body(null)
                .build();

        // When
        Response response = tradeController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }
}

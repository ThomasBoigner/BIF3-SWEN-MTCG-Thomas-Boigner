package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.*;
import at.fhtw.mtcgapp.persistence.repository.CardRepository;
import at.fhtw.mtcgapp.persistence.repository.TradeRepository;
import at.fhtw.mtcgapp.service.command.CreateTradeCommand;
import at.fhtw.mtcgapp.service.dto.TradeDto;
import at.fhtw.mtcgapp.service.exception.TradeValidationException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {
    private TradeService tradeService;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private TradeRepository tradeRepository;
    @Mock
    private CardRepository cardRepository;

    @BeforeEach
    void setUp() {
        tradeService = new TradeServiceImpl(tradeRepository, authenticationService, cardRepository, Validation.buildDefaultValidatorFactory().getValidator());
    }

    @Test
    void ensureGetTradesWorksProperly() {
        // Given
        String authToken = "Thomas-mtgcToken";

        User user = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(0)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();

        Trade trade1 = Trade.builder()
                .token(UUID.randomUUID())
                .minimumDamage(50)
                .type(CardType.SPELL)
                .cardToTrade(MonsterCard.builder()
                        .token(UUID.randomUUID())
                        .name("Dragon")
                        .damage(50)
                        .damageType(DamageType.NORMAL)
                        .defence(10)
                        .build())
                .trader(user)
                .build();

        Trade trade2 = Trade.builder()
                .token(UUID.randomUUID())
                .minimumDamage(40)
                .type(CardType.MONSTER)
                .cardToTrade(SpellCard.builder()
                        .token(UUID.randomUUID())
                        .name("FireSpell")
                        .damage(15)
                        .damageType(DamageType.FIRE)
                        .criticalHitMultiplier(0.2)
                        .build())
                .trader(user)
                .build();
        when(tradeRepository.getTrades()).thenReturn(List.of(trade1, trade2));
        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);

        // When
        List<TradeDto> trades = tradeService.getTrades(authToken);

        // Then
        assertThat(trades).hasSize(2);
        assertThat(trades).contains(new TradeDto(trade1));
        assertThat(trades).contains(new TradeDto(trade2));
    }

    @Test
    void ensureCreateTradeWorksProperly() {
        // Given
        String authToken = "Thomas-mtgcToken";

        MonsterCard card = MonsterCard.builder()
                .token(UUID.fromString("99f8f8dc-e25e-4a95-aa2c-782823f36e2a"))
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        User user = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(0)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>(List.of(card)))
                .trades(new ArrayList<>())
                .build();

        CreateTradeCommand command = CreateTradeCommand.builder()
                .id(UUID.randomUUID())
                .cardToTrade(UUID.fromString("99f8f8dc-e25e-4a95-aa2c-782823f36e2a"))
                .minimumDamage(50)
                .type("monster")
                .build();

        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);
        when(tradeRepository.save(any(Trade.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        // When
        TradeDto tradeDto = tradeService.createTrade(authToken, command);

        // Then
        assertThat(tradeDto.id()).isEqualTo(command.id());
        assertThat(tradeDto.cardToTrade().id()).isEqualTo(command.cardToTrade());
        assertThat(tradeDto.type()).isEqualTo(command.type());
        assertThat(tradeDto.minimumDamage()).isEqualTo(command.minimumDamage());
    }

    @Test
    void ensureCreateTradeThrowsTradeValidationExceptionWhenCardIsNotInStackOfUser() {
        // Given
        String authToken = "Thomas-mtgcToken";

        User user = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(0)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();

        CreateTradeCommand command = CreateTradeCommand.builder()
                .id(UUID.randomUUID())
                .cardToTrade(UUID.fromString("99f8f8dc-e25e-4a95-aa2c-782823f36e2a"))
                .minimumDamage(50)
                .type("monster")
                .build();

        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);

        // Then
        assertThrows(TradeValidationException.class, () -> tradeService.createTrade(authToken, command));
    }

    @Test
    void ensureCreateTradeThrowsConstraintViolationExceptionWhenCommandViolatesConstraint() {
        // Given
        String authToken = "Thomas-mtgcToken";

        User user = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(0)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();

        CreateTradeCommand command = CreateTradeCommand.builder()
                .id(UUID.randomUUID())
                .cardToTrade(null)
                .minimumDamage(-1)
                .type("")
                .build();

        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);

        // Then
        assertThrows(ConstraintViolationException.class, () -> tradeService.createTrade(authToken, command));
    }
}

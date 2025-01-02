package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.*;
import at.fhtw.mtcgapp.persistence.repository.CardRepository;
import at.fhtw.mtcgapp.service.dto.CardDto;
import at.fhtw.mtcgapp.service.exception.CardValidationException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
public class CardServiceTest {
    private CardService cardService;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private CardRepository cardRepository;

    @BeforeEach
    void setUp(){
        cardService = new CardServiceImpl(cardRepository, authenticationService);
    }

    @Test
    void ensureGetCardsOfUserWorksProperly(){
        // Given
        String authToken = "Thomas-mtgcToken";

        MonsterCard monsterCard = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();


        SpellCard spellCard = SpellCard.builder()
                .token(UUID.randomUUID())
                .name("FireSpell")
                .damage(15)
                .damageType(DamageType.FIRE)
                .criticalHitChance(0.2)
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
                .stack(List.of(monsterCard, spellCard))
                .trades(new ArrayList<>())
                .build();

        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);

        // When
        List<CardDto> cards = cardService.getCardsOfUser(authToken);

        // Then
        assertThat(cards).hasSize(2);
        assertThat(cards).contains(new CardDto(monsterCard));
        assertThat(cards).contains(new CardDto(spellCard));
    }

    @Test
    void ensureGetDeckOfUserWorksProperly() {
        // Given
        String authToken = "Thomas-mtgcToken";

        MonsterCard monsterCard = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        SpellCard spellCard = SpellCard.builder()
                .token(UUID.randomUUID())
                .name("FireSpell")
                .damage(15)
                .damageType(DamageType.FIRE)
                .criticalHitChance(0.2)
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
                .deck(List.of(monsterCard, spellCard))
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();

        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);

        // When
        List<CardDto> cards = cardService.getDeckOfUser(authToken);

        // Then
        assertThat(cards).hasSize(2);
        assertThat(cards).contains(new CardDto(monsterCard));
        assertThat(cards).contains(new CardDto(spellCard));
    }

    @Test
    void ensureConfigureDeckWorksProperly() {
        // Given
        String authToken = "Thomas-mtgcToken";

        List<UUID> cardIds = List.of(UUID.fromString("aa9999a0-734c-49c6-8f4a-651864b14e62"), UUID.fromString("d6e9c720-9b5a-40c7-a6b2-bc34752e3463"), UUID.fromString("d60e23cf-2238-4d49-844f-c7589ee5342e"), UUID.fromString("02a9c76e-b17d-427f-9240-2dd49b0d3bfd"));

        MonsterCard card1 = MonsterCard.builder()
                .token(UUID.fromString("aa9999a0-734c-49c6-8f4a-651864b14e62"))
                .name("WaterGoblin")
                .damage(10.0)
                .damageType(DamageType.WATER)
                .defence(10)
                .build();

        MonsterCard card2 = MonsterCard.builder()
                .token(UUID.fromString("d6e9c720-9b5a-40c7-a6b2-bc34752e3463"))
                .name("Dragon")
                .damage(50.0)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        MonsterCard card3 = MonsterCard.builder()
                .token(UUID.fromString("d60e23cf-2238-4d49-844f-c7589ee5342e"))
                .name("Ork")
                .damage(45.0)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        SpellCard card4 = SpellCard.builder()
                .token(UUID.fromString("02a9c76e-b17d-427f-9240-2dd49b0d3bfd"))
                .name("FireSpell")
                .damage(25.0)
                .damageType(DamageType.FIRE)
                .criticalHitChance(0.2)
                .build();

        SpellCard card5 = SpellCard.builder()
                .token(UUID.fromString("64f51878-db2c-47c9-98e5-71c85d01db18"))
                .name("WaterSpell")
                .damage(20.0)
                .damageType(DamageType.WATER)
                .criticalHitChance(0.1)
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
                .deck(new ArrayList<>(List.of(card1, card2, card3, card5)))
                .stack(new ArrayList<>(List.of(card4)))
                .trades(new ArrayList<>())
                .build();

        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);

        // When
        cardService.configureDeck(authToken, cardIds);

        // Then
        verify(cardRepository).resetDeckOfUser(eq(user.getId()));
        verify(cardRepository).configureDeckOfUser(eq(List.of(card4, card1, card2, card3)));
    }

    @Test
    void ensureConfigureDeckThrowsValidationExceptionWhenUserTriesToConfigureDeckWithLessThan4Cards() {
        // Given
        String authToken = "Thomas-mtgcToken";

        List<UUID> cardIds = List.of(UUID.fromString("aa9999a0-734c-49c6-8f4a-651864b14e62"));

        // Then
        assertThrows(CardValidationException.class, () -> cardService.configureDeck(authToken, cardIds));
    }

    @Test
    void ensureConfigureDeckThrowsValidationExceptionWhenUserTriesToConfigureDeckWithCardHeDoesNotOwn() {
        // Given
        String authToken = "Thomas-mtgcToken";

        List<UUID> cardIds = List.of(UUID.fromString("afcf6007-2124-47c9-be3b-01379acced51"), UUID.fromString("d6e9c720-9b5a-40c7-a6b2-bc34752e3463"), UUID.fromString("d60e23cf-2238-4d49-844f-c7589ee5342e"), UUID.fromString("02a9c76e-b17d-427f-9240-2dd49b0d3bfd"));

        MonsterCard card1 = MonsterCard.builder()
                .token(UUID.fromString("aa9999a0-734c-49c6-8f4a-651864b14e62"))
                .name("WaterGoblin")
                .damage(10.0)
                .damageType(DamageType.WATER)
                .defence(10)
                .build();

        MonsterCard card2 = MonsterCard.builder()
                .token(UUID.fromString("d6e9c720-9b5a-40c7-a6b2-bc34752e3463"))
                .name("Dragon")
                .damage(50.0)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        MonsterCard card3 = MonsterCard.builder()
                .token(UUID.fromString("d60e23cf-2238-4d49-844f-c7589ee5342e"))
                .name("Ork")
                .damage(45.0)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        SpellCard card4 = SpellCard.builder()
                .token(UUID.fromString("02a9c76e-b17d-427f-9240-2dd49b0d3bfd"))
                .name("FireSpell")
                .damage(25.0)
                .damageType(DamageType.FIRE)
                .criticalHitChance(0.2)
                .build();

        SpellCard card5 = SpellCard.builder()
                .token(UUID.fromString("64f51878-db2c-47c9-98e5-71c85d01db18"))
                .name("WaterSpell")
                .damage(20.0)
                .damageType(DamageType.WATER)
                .criticalHitChance(0.1)
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
                .deck(new ArrayList<>(List.of(card1, card2, card3, card5)))
                .stack(new ArrayList<>(List.of(card4)))
                .trades(new ArrayList<>())
                .build();

        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);

        // Then
        assertThrows(CardValidationException.class, () -> cardService.configureDeck(authToken, cardIds));
    }
}

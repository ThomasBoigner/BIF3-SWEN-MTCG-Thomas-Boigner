package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.DamageType;
import at.fhtw.mtcgapp.model.MonsterCard;
import at.fhtw.mtcgapp.model.SpellCard;
import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.CardRepository;
import at.fhtw.mtcgapp.service.dto.CardDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
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

        User user = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(0)
                .battlesFought(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();

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
        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);
        when(cardRepository.getCardsOfUser(eq(user.getId()))).thenReturn(List.of(monsterCard, spellCard));

        // When
        List<CardDto> cards = cardService.getCardsOfUser(authToken);

        // Then
        assertThat(cards).hasSize(2);
        assertThat(cards).contains(new CardDto(monsterCard));
        assertThat(cards).contains(new CardDto(spellCard));
    }
}

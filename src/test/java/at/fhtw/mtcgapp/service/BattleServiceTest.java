package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.DamageType;
import at.fhtw.mtcgapp.model.MonsterCard;
import at.fhtw.mtcgapp.model.SpellCard;
import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import at.fhtw.mtcgapp.service.exception.BattleValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BattleServiceTest {
    private BattleService battleService;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        battleService = new BattleServiceImpl(authenticationService, userRepository);
    }

    @Test
    void ensureBattleUserWorksProperly() {
        // Given
        String authToken = "Thomas-mtgcToken";

        MonsterCard cardPlayer1 = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        MonsterCard cardPlayer2 = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Ork")
                .damage(45)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        User player1 = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(100)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>(List.of(cardPlayer1)))
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        cardPlayer1.setUser(player1);

        User player2 = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(100)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>(List.of(cardPlayer2)))
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        cardPlayer2.setUser(player2);

        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(player1);
        when(userRepository.getUserInQueue()).thenReturn(Optional.of(player2));

        // When
        battleService.battleUser(authToken);

        // Then
        assertThat(player1.getElo()).isEqualTo(103);
        assertThat(player1.getWins()).isEqualTo(1);
        assertThat(player2.getElo()).isEqualTo(95);
        assertThat(player2.getLosses()).isEqualTo(1);
    }

    @Test
    void ensureBattleUserAddsUsersToQueue() {
        // Given
        String authToken = "Thomas-mtgcToken";

        MonsterCard cardPlayer1 = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        User player1 = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(100)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>(List.of(cardPlayer1)))
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();


        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(player1);
        when(userRepository.getUserInQueue()).thenReturn(Optional.empty());

        // When
        battleService.battleUser(authToken);

        // Then
        assertThat(player1.isInQueue()).isTrue();
    }
    @Test
    void ensureBattleUserThrowsBVEWhenUserHasNoDeckConfigured() {
        // Given
        String authToken = "Thomas-mtgcToken";

        User player1 = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(100)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();


        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(player1);

        // Then
        assertThrows(BattleValidationException.class, () ->battleService.battleUser(authToken));
    }

    @Test
    void ensureBattleUserThrowsBVEWhenUserTriesToFightHimself() {
        // Given
        String authToken = "Thomas-mtgcToken";

        MonsterCard cardPlayer1 = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        User player1 = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(100)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>(List.of(cardPlayer1)))
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();

        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(player1);
        when(userRepository.getUserInQueue()).thenReturn(Optional.of(player1));

        // Then
        assertThrows(BattleValidationException.class, () ->battleService.battleUser(authToken));
    }

    @Test
    void ensureBattleRoundWorksProperly() {
        // Given
        MonsterCard cardPlayer1 = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .user(User.builder().username("Player1").build())
                .build();


        MonsterCard cardPlayer2 = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Ork")
                .damage(45)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .user(User.builder().username("Player2").build())
                .build();

        // When
        double result = battleService.battleRound(cardPlayer1, cardPlayer2);

        // Then
        assertThat(result).isEqualTo(5);
    }

    @Test
    void ensureBattleRoundCalculatesElementAdvantageProperlyForPlayer1() {
        // Given
        MonsterCard cardPlayer1 = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.WATER)
                .defence(10)
                .user(User.builder().username("Player1").build())
                .build();


        SpellCard cardPlayer2 = SpellCard.builder()
                .token(UUID.randomUUID())
                .name("FireSpell")
                .damage(15)
                .damageType(DamageType.FIRE)
                .criticalHitMultiplier(1.2)
                .user(User.builder().username("Player2").build())
                .build();

        // When
        double result = battleService.battleRound(cardPlayer1, cardPlayer2);

        // Then
        assertThat(result).isEqualTo(94);
    }

    @Test
    void ensureBattleRoundCalculatesElementAdvantageProperlyForPlayer2() {
        // Given
        MonsterCard cardPlayer1 = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .user(User.builder().username("Player1").build())
                .build();


        SpellCard cardPlayer2 = SpellCard.builder()
                .token(UUID.randomUUID())
                .name("FireSpell")
                .damage(15)
                .damageType(DamageType.FIRE)
                .criticalHitMultiplier(1.2)
                .user(User.builder().username("Player2").build())
                .build();

        // When
        double result = battleService.battleRound(cardPlayer1, cardPlayer2);

        // Then
        assertThat(result).isEqualTo(38);
    }

    @Test
    void ensureBattleRoundWorksWhenPlayer1CardCanNotFight() {
        // Given
        MonsterCard cardPlayer1 = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Goblin")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .user(User.builder().username("Player1").build())
                .build();


        MonsterCard cardPlayer2 = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(45)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .user(User.builder().username("Player2").build())
                .build();

        // When
        double result = battleService.battleRound(cardPlayer1, cardPlayer2);

        // Then
        assertThat(result).isEqualTo(-35);
    }
}

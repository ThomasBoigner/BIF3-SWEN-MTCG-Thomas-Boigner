package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.Card;
import at.fhtw.mtcgapp.model.DamageType;
import at.fhtw.mtcgapp.model.MonsterCard;
import at.fhtw.mtcgapp.model.SpellCard;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
    void ensureBattleRoundWorksProperly() {
        // Given
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
                .build();


        SpellCard cardPlayer2 = SpellCard.builder()
                .token(UUID.randomUUID())
                .name("FireSpell")
                .damage(15)
                .damageType(DamageType.FIRE)
                .criticalHitMultiplier(1.2)
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
                .build();


        SpellCard cardPlayer2 = SpellCard.builder()
                .token(UUID.randomUUID())
                .name("FireSpell")
                .damage(15)
                .damageType(DamageType.FIRE)
                .criticalHitMultiplier(1.2)
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
                .build();


        MonsterCard cardPlayer2 = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(45)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        // When
        double result = battleService.battleRound(cardPlayer1, cardPlayer2);

        // Then
        assertThat(result).isEqualTo(-35);
    }
}

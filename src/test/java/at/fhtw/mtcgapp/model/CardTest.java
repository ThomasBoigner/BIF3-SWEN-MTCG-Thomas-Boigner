package at.fhtw.mtcgapp.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CardTest {

    @Test
    void ensureCalculateDamageWorksProperly() {
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
        double damagePlayer1 = cardPlayer1.calculateDamage(cardPlayer2);
        double damagePlayer2 = cardPlayer2.calculateDamage(cardPlayer1);

        // Then
        assertThat(damagePlayer1).isEqualTo(40);
        assertThat(damagePlayer2).isEqualTo(35);
    }

    @Test
    void ensureCalculateDamageCalculatesElementAdvantageProperlyForPlayer1() {
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
        double damagePlayer1 = cardPlayer1.calculateDamage(cardPlayer2);
        double damagePlayer2 = cardPlayer2.calculateDamage(cardPlayer1);

        // Then
        assertThat(damagePlayer1).isEqualTo(100);
        assertThat(damagePlayer2).isEqualTo(6);
    }

    @Test
    void ensureCalculateDamageWorksWithTwoSpellCards() {
        // Given
        SpellCard cardPlayer1 = SpellCard.builder()
                .token(UUID.randomUUID())
                .name("FireSpell")
                .damage(15)
                .damageType(DamageType.FIRE)
                .criticalHitMultiplier(1.2)
                .user(User.builder().username("Player1").build())
                .build();


        SpellCard cardPlayer2 = SpellCard.builder()
                .token(UUID.randomUUID())
                .name("WaterSpell")
                .damage(10)
                .damageType(DamageType.WATER)
                .criticalHitMultiplier(1.2)
                .user(User.builder().username("Player2").build())
                .build();

        // When
        double damagePlayer1 = cardPlayer1.calculateDamage(cardPlayer2);
        double damagePlayer2 = cardPlayer2.calculateDamage(cardPlayer1);

        // Then
        assertThat(damagePlayer1).isEqualTo(18);
        assertThat(damagePlayer2).isEqualTo(24);
    }


    @Test
    void ensureCalculateDamageWorksWhenPlayer1CardCanNotFight() {
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
        double damagePlayer1 = cardPlayer1.calculateDamage(cardPlayer2);
        double damagePlayer2 = cardPlayer2.calculateDamage(cardPlayer1);

        // Then
        assertThat(damagePlayer1).isEqualTo(0);
        assertThat(damagePlayer2).isEqualTo(35);
    }

    @Test
    void ensureHasWeaknessWorksProperly() {
        // Given
        Card goblin = MonsterCard.builder().name("Goblin").build();
        Card ork = MonsterCard.builder().name("Ork").build();
        Card knight = MonsterCard.builder().name("Knight").build();
        Card spell = SpellCard.builder().name("FireSpell").build();
        Card fireElves = MonsterCard.builder().name("FireElves").build();

        Card dragon = MonsterCard.builder().name("Dragon").build();
        Card wizard = MonsterCard.builder().name("Wizard").build();
        Card waterSpell = SpellCard.builder().name("WaterSpell").build();
        Card kraken = MonsterCard.builder().name("Kraken").build();

        // Then
        assertThat(goblin.hasWeakness(dragon)).isTrue();
        assertThat(ork.hasWeakness(wizard)).isTrue();
        assertThat(knight.hasWeakness(waterSpell)).isTrue();
        assertThat(spell.hasWeakness(kraken)).isTrue();
        assertThat(fireElves.hasWeakness(dragon)).isTrue();
        assertThat(wizard.hasWeakness(wizard)).isFalse();
    }

    @Test
    void ensureHasElementAdvantageWorksProperly() {
        // Given
        SpellCard fireSpell = SpellCard.builder().name("FireSpell").damageType(DamageType.FIRE).build();
        SpellCard waterSpell = SpellCard.builder().name("WaterSpell").damageType(DamageType.WATER).build();
        SpellCard normalSpell = SpellCard.builder().name("NormalSpell").damageType(DamageType.NORMAL).build();

        // Then
        assertThat(waterSpell.hasElementAdvantage(fireSpell)).isTrue();
        assertThat(fireSpell.hasElementAdvantage(normalSpell)).isTrue();
        assertThat(normalSpell.hasElementAdvantage(waterSpell)).isTrue();

        assertThat(fireSpell.hasElementAdvantage(waterSpell)).isFalse();
        assertThat(normalSpell.hasElementAdvantage(fireSpell)).isFalse();
        assertThat(waterSpell.hasElementAdvantage(normalSpell)).isFalse();
    }
}

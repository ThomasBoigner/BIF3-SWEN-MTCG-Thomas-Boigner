package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Card;
import at.fhtw.mtcgapp.model.DamageType;
import at.fhtw.mtcgapp.model.MonsterCard;
import at.fhtw.mtcgapp.model.SpellCard;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class CardRepositoryTest {

    Consumer<CreateContainerCmd> cmd = e -> e.withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)));
    @Container
    private GenericContainer<?> postgres = new GenericContainer<>(DockerImageName.parse("postgres:17-alpine"))
            .withCreateContainerCmdModifier(cmd)
            .withExposedPorts(5432)
            .withEnv(Map.ofEntries(
                    entry("POSTGRES_DB", "mtcgdb"),
                    entry("POSTGRES_USER", "mtcgdb"),
                    entry("POSTGRES_PASSWORD", "mtcgdb")
            ));

    private CardRepository cardRepository;

    @BeforeEach
    void setUp() {
        UnitOfWork unitOfWork = new UnitOfWork();
        cardRepository = new CardRepositoryImpl(unitOfWork);
    }

    @Test
    void ensureSaveMonsterCardWorksProperly(){
        // Given
        MonsterCard monsterCard = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        // When
        Card returned = cardRepository.save(monsterCard);

        // Then
        assertThat(returned.getId()).isNotZero();
        assertThat((MonsterCard)returned).isEqualTo(monsterCard);
    }

    @Test
    void ensureSaveSpellCardWorksProperly(){
        // Given
        SpellCard spellCard = SpellCard.builder()
                .token(UUID.randomUUID())
                .name("FireSpell")
                .damage(15)
                .damageType(DamageType.FIRE)
                .criticalHitChance(0.2)
                .build();

        // When
        Card returned = cardRepository.save(spellCard);

        // Then
        assertThat(returned.getId()).isNotZero();
        assertThat((SpellCard)returned).isEqualTo(spellCard);
    }

    @Test
    void ensureUpdateCardWorksWithMonsterCard(){
        // Given
        MonsterCard monsterCard = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();
        cardRepository.save(monsterCard);

        // When
        Card returned = cardRepository.updateCard(monsterCard);

        // Then
        assertThat(returned).isEqualTo(monsterCard);
    }

    @Test
    void ensureUpdateCardWorksWithSpellCard(){
        // Given
        SpellCard spellCard = SpellCard.builder()
                .token(UUID.randomUUID())
                .name("FireSpell")
                .damage(15)
                .damageType(DamageType.FIRE)
                .criticalHitChance(0.2)
                .build();
        cardRepository.save(spellCard);

        // When
        Card returned = cardRepository.updateCard(spellCard);

        // Then
        assertThat(returned).isEqualTo(spellCard);
    }
}

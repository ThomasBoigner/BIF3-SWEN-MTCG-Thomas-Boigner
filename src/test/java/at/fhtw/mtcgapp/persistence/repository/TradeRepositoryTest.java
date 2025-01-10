package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class TradeRepositoryTest {
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

    private TradeRepository tradeRepository;
    private CardRepository cardRepository;
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        UnitOfWork unitOfWork = new UnitOfWork();
        tradeRepository = new TradeRepositoryImpl(unitOfWork);
        cardRepository = new CardRepositoryImpl(unitOfWork);
        userRepository = new UserRepositoryImpl(unitOfWork, cardRepository);
    }

    @Test
    void ensureGetTradesWorksProperly() {
        // Given
        MonsterCard card = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();
        cardRepository.save(card);

        User user = User.builder()
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
        userRepository.save(user);

        Trade trade = Trade.builder()
                .token(UUID.randomUUID())
                .minimumDamage(50)
                .type(CardType.SPELL)
                .cardToTrade(card)
                .trader(user)
                .build();
        tradeRepository.save(trade);

        // When
        List<Trade> returned = tradeRepository.getTrades();

        // Then
        assertThat(returned).hasSize(1);
        assertThat(returned).contains(trade);
    }

    @Test
    void ensureSaveTradeWorksProperly() {
        // Given
        MonsterCard card = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();
        cardRepository.save(card);

        User user = User.builder()
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
        userRepository.save(user);

        Trade trade = Trade.builder()
                .token(UUID.randomUUID())
                .minimumDamage(50)
                .type(CardType.SPELL)
                .cardToTrade(card)
                .trader(user)
                .build();

        // When
        Trade returned = tradeRepository.save(trade);

        // Then
        assertThat(returned.getId()).isNotZero();
        assertThat(returned).isEqualTo(trade);
    }
}

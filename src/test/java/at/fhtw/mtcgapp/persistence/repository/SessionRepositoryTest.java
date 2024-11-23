package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Session;
import at.fhtw.mtcgapp.model.User;
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
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class SessionRepositoryTest {

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

    private SessionRepository sessionRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        UnitOfWork unitOfWork = new UnitOfWork();
        userRepository = new UserRepositoryImpl(unitOfWork);
        sessionRepository = new SessionRepositoryImpl(unitOfWork);
    }

    @Test
    void ensureSaveSessionWorksProperly(){
        // Given
        User user = User.builder()
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("Password")
                .bio("")
                .image("")
                .elo(0)
                .battlesFought(0)
                .coins(20)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();

        Session session = Session.builder()
                .token("Thomas-mtcgToken")
                .user(user)
                .build();

        userRepository.save(user);
        user.setId(1);

        // When
        Session returned = sessionRepository.save(session);

        // Then
        assertThat(returned).isEqualTo(session);
    }
    @Test
    void ensureExistsByUsernameReturnsTrue(){
        // Given
        User user = User.builder()
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("Password")
                .bio("")
                .image("")
                .elo(0)
                .battlesFought(0)
                .coins(20)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();

        Session session = Session.builder()
                .token("Thomas-mtcgToken")
                .user(user)
                .build();

        userRepository.save(user);
        user.setId(1);
        Session returned = sessionRepository.save(session);

        // When
        boolean exists = sessionRepository.existsByToken("Thomas-mtcgToken");

        // Then
        assertThat(exists).isTrue();
    }
}

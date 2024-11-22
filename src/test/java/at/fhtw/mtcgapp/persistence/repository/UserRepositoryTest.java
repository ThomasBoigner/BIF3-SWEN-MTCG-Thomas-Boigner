package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserRepositoryTest {

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

    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository = new UserRepositoryImpl(new UnitOfWork());
    }

    @Test
    void ensureSaveUserWorksProperly(){
        // Given
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

        // When
        User returned = userRepository.save(user);

        // Then
        assertThat(returned).isEqualTo(user);
    }

    @Test
    void ensureExistsByUsernameReturnsTrue(){
        // Given
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
        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByUsername(user.getUsername());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void ensureExistsByUsernameReturnsFalse(){
        // When
        boolean exists = userRepository.existsByUsername("username");

        // Then
        assertThat(exists).isFalse();
    }
}
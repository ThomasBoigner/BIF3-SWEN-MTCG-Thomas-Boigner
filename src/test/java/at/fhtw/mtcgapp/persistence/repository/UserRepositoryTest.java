package at.fhtw.mtcgapp.persistence.repository;

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

import java.util.*;
import java.util.function.Consumer;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
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
    void ensureFindByUsernameWorksProperly() {
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
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();

        userRepository.save(user);
        user.setId(1);

        // When
        Optional<User> fetchedUser = userRepository.findByUsername(user.getUsername());

        // Then
        assertThat(fetchedUser.isPresent()).isTrue();
        assertThat(fetchedUser.get()).isEqualTo(user);
    }

    @Test
    void ensureFindByUsernameReturnsEmptyOptional() {
        // When
        Optional<User> fetchedUser = userRepository.findByUsername("Thomas");

        // Then
        assertThat(fetchedUser.isEmpty()).isTrue();
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
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();

        // When
        User returned = userRepository.save(user);

        // Then
        assertThat(returned.getId()).isNotZero();
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
                .wins(0)
                .losses(0)
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
    void ensureExistsByUsernameIsCaseInsensitiveReturnsTrue(){
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
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByUsername("thomas");

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

    @Test
    void ensureUpdateUserWorksProperly() {
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
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        userRepository.save(user);

        // When
        User returned = userRepository.updateUser(user);

        // Then
        assertThat(returned).isEqualTo(user);
    }

    @Test
    void ensureFindAllUsersWorksProperly() {
        // Given
        User user1 = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(100)
                .wins(4)
                .losses(3)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Max")
                .password("pwd")
                .bio("bio2")
                .image("image2")
                .coins(40)
                .elo(400)
                .wins(8)
                .losses(2)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        userRepository.save(user2);

        // When
        List<User> returned = userRepository.findAllUsers();

        assertThat(returned.size()).isEqualTo(2);
        assertThat(returned).contains(user1);
        assertThat(returned).contains(user2);
    }
}

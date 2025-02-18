package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.DamageType;
import at.fhtw.mtcgapp.model.MonsterCard;
import at.fhtw.mtcgapp.model.SpellCard;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import at.fhtw.mtcgapp.model.Package;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;

@Testcontainers
public class PackageRepositoryTest {

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

    private PackageRepository packageRepository;
    private CardRepository cardRepository;

    @BeforeEach
    void setUp() {
        UnitOfWork unitOfWork = new UnitOfWork();
        cardRepository = new CardRepositoryImpl(unitOfWork);
        packageRepository = new PackageRepositoryImpl(unitOfWork, cardRepository);
    }

    @Test
    void ensureSavePackageWorksProperly(){
        // Given
        MonsterCard monsterCard = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        Package pkg = Package.builder()
                .token(UUID.randomUUID())
                .price(5)
                .cards(List.of(monsterCard))
                .build();

        // When
        Package returned = packageRepository.save(pkg);

        // Then
        assertThat(returned).isEqualTo(pkg);
    }

    @Test
    void ensureGetPackageWorksProperly() {
        // Given
        Package pkg = Package.builder()
                .token(UUID.randomUUID())
                .price(5)
                .build();

        MonsterCard monsterCard = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .cardPackage(pkg)
                .defence(10)
                .build();

        SpellCard spellCard = SpellCard.builder()
                .token(UUID.randomUUID())
                .name("FireSpell")
                .damage(15)
                .damageType(DamageType.FIRE)
                .cardPackage(pkg)
                .criticalHitMultiplier(0.2)
                .build();

        pkg.setCards(List.of(spellCard, monsterCard));

        packageRepository.save(pkg);

        // When
        Optional<Package> returned = packageRepository.getPackage();

        // Then
        assertThat(returned.isPresent()).isTrue();
        assertThat(returned.get()).isEqualTo(pkg);
        assertThat(returned.get().getCards()).hasSize(2);
        assertThat(returned.get().getCards()).contains(spellCard);
        assertThat(returned.get().getCards()).contains(monsterCard);
    }

    @Test
    void ensureGetPackageReturnsOptionalEmpty() {
        // When
        Optional<Package> returned = packageRepository.getPackage();

        // Then
        assertThat(returned.isPresent()).isFalse();
    }

    @Test
    void ensureDeletePackageWorksProperly() {
        // Given
        Package pkg = Package.builder()
                .token(UUID.randomUUID())
                .price(5)
                .build();

        MonsterCard monsterCard = MonsterCard.builder()
                .token(UUID.randomUUID())
                .name("Dragon")
                .damage(50)
                .damageType(DamageType.NORMAL)
                .cardPackage(pkg)
                .defence(10)
                .build();

        SpellCard spellCard = SpellCard.builder()
                .token(UUID.randomUUID())
                .name("FireSpell")
                .damage(15)
                .damageType(DamageType.FIRE)
                .cardPackage(pkg)
                .criticalHitMultiplier(0.2)
                .build();

        pkg.setCards(List.of(spellCard, monsterCard));

        packageRepository.save(pkg);

        // When
        packageRepository.deletePackage(pkg.getId());
    }
}

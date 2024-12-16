package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.*;
import at.fhtw.mtcgapp.model.Package;
import at.fhtw.mtcgapp.persistence.repository.PackageRepository;
import at.fhtw.mtcgapp.service.command.CreateCardCommand;
import at.fhtw.mtcgapp.service.dto.CardDto;
import at.fhtw.mtcgapp.service.dto.PackageDto;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PackageServiceTest {
    private PackageService packageService;
    @Mock
    private PackageRepository packageRepository;

    @BeforeEach
    void setUp() {
        packageService = new PackageService(packageRepository, Validation.buildDefaultValidatorFactory().getValidator());
    }

    @Test
    void ensureCreatePackageWorksProperly() {
        // Given
        CreateCardCommand createCardCommand1 = CreateCardCommand.builder()
                .id(UUID.fromString("845f0dc7-37d0-426e-994e-43fc3ac83c08"))
                .name("WaterGoblin")
                .damage(10.0)
                .build();
        CreateCardCommand createCardCommand2 = CreateCardCommand.builder()
                .id(UUID.fromString("99f8f8dc-e25e-4a95-aa2c-782823f36e2a"))
                .name("Dragon")
                .damage(50.0)
                .build();
        CreateCardCommand createCardCommand3 = CreateCardCommand.builder()
                .id(UUID.fromString("1cb6ab86-bdb2-47e5-b6e4-68c5ab389334"))
                .name("Ork")
                .damage(45.0)
                .build();
        CreateCardCommand createCardCommand4 = CreateCardCommand.builder()
                .id(UUID.fromString("dfdd758f-649c-40f9-ba3a-8657f4b3439f"))
                .name("FireSpell")
                .damage(25.0)
                .build();

        Card card1 = MonsterCard.builder()
                .token(createCardCommand1.id())
                .name(createCardCommand1.name())
                .damage(createCardCommand1.damage())
                .damageType(DamageType.WATER)
                .defence(10)
                .build();

        Card card2 = MonsterCard.builder()
                .token(createCardCommand2.id())
                .name(createCardCommand2.name())
                .damage(createCardCommand2.damage())
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        Card card3 = MonsterCard.builder()
                .token(createCardCommand3.id())
                .name(createCardCommand3.name())
                .damage(createCardCommand3.damage())
                .damageType(DamageType.NORMAL)
                .defence(10)
                .build();

        Card card4 = SpellCard.builder()
                .token(createCardCommand4.id())
                .name(createCardCommand4.name())
                .damage(createCardCommand4.damage())
                .damageType(DamageType.FIRE)
                .criticalHitChance(0.2)
                .build();

        List<CreateCardCommand> commands = List.of(createCardCommand1, createCardCommand2, createCardCommand3, createCardCommand4);
        when(packageRepository.save(any(Package.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        // When
        PackageDto packageDto = packageService.createPackage("Thomas-mtgcToken", commands);

        // Then
        assertThat(packageDto.id()).isNotNull();
        assertThat(packageDto.price()).isEqualTo(5);
        assertThat(packageDto.cards()).hasSize(4);
        assertThat(packageDto.cards()).contains(new CardDto(card1));
        assertThat(packageDto.cards()).contains(new CardDto(card2));
        assertThat(packageDto.cards()).contains(new CardDto(card3));
        assertThat(packageDto.cards()).contains(new CardDto(card4));
    }

    @Test
    void ensureCreatePackageThrowsConstraintViolationExceptionWhenCommandViolatesConstraint() {
        // Given
        CreateCardCommand createCardCommand1 = CreateCardCommand.builder()
                .id(null)
                .name(null)
                .damage(-10)
                .build();
        CreateCardCommand createCardCommand2 = CreateCardCommand.builder()
                .id(UUID.fromString("99f8f8dc-e25e-4a95-aa2c-782823f36e2a"))
                .name(null)
                .damage(-20)
                .build();
        CreateCardCommand createCardCommand3 = CreateCardCommand.builder()
                .id(UUID.fromString("1cb6ab86-bdb2-47e5-b6e4-68c5ab389334"))
                .name(null)
                .damage(-23)
                .build();
        CreateCardCommand createCardCommand4 = CreateCardCommand.builder()
                .id(UUID.fromString("dfdd758f-649c-40f9-ba3a-8657f4b3439f"))
                .name(null)
                .damage(-25.0)
                .build();

        List<CreateCardCommand> commands = List.of(createCardCommand1, createCardCommand2, createCardCommand3, createCardCommand4);

        // When
        assertThrows(ConstraintViolationException.class, () -> packageService.createPackage("Thomas-mtgcToken", commands));
    }
}
package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.*;
import at.fhtw.mtcgapp.model.Package;
import at.fhtw.mtcgapp.persistence.repository.PackageRepository;
import at.fhtw.mtcgapp.service.command.CreateCardCommand;
import at.fhtw.mtcgapp.service.dto.PackageDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor

@Slf4j
public class PackageService {
    private final PackageRepository packageRepository;
    private final Validator validator;

    public PackageDto createPackage(String authToken, List<CreateCardCommand> commands) {
        log.debug("Trying to create user with commands {}", commands);

        Set<ConstraintViolation<CreateCardCommand>> violations = commands.stream()
                .map(command -> validator.validate(command))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        if (!violations.isEmpty()) {
            log.warn("Input validation for user failed!");
            throw new ConstraintViolationException(violations);
        }

        Package pkg = Package.builder()
                .token(UUID.randomUUID())
                .price(5)
                .build();

        List<Card> cards = commands.stream().map(command -> {
            DamageType damageType = DamageType.NORMAL;

            if (command.name().contains("Fire")) {
                damageType = DamageType.FIRE;
            }
            if (command.name().contains("Water")) {
                damageType = DamageType.WATER;
            }

            if (command.name().contains("Spell")) {
                return SpellCard.builder()
                        .token(command.id())
                        .name(command.name())
                        .damage(command.damage())
                        .cardPackage(pkg)
                        .damageType(damageType)
                        .criticalHitChance(0.2)
                        .build();
            } else {
                return MonsterCard.builder()
                        .token(command.id())
                        .name(command.name())
                        .damage(command.damage())
                        .cardPackage(pkg)
                        .damageType(damageType)
                        .defence(10)
                        .build();
            }
        }).toList();
        pkg.setCards(cards);
        log.trace("Mapped commands {} to package object {}", commands, pkg);

        log.info("Created package {}", pkg);
        return new PackageDto(packageRepository.save(pkg));
    }
}

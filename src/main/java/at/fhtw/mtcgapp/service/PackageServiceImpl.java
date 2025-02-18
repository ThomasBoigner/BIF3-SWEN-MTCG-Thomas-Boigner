package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.*;
import at.fhtw.mtcgapp.model.Package;
import at.fhtw.mtcgapp.persistence.repository.CardRepository;
import at.fhtw.mtcgapp.persistence.repository.PackageRepository;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import at.fhtw.mtcgapp.service.command.CreateCardCommand;
import at.fhtw.mtcgapp.service.dto.PackageDto;
import at.fhtw.mtcgapp.service.exception.TransactionValidationException;
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

@RequiredArgsConstructor

@Slf4j
public class PackageServiceImpl implements PackageService {
    private final AuthenticationService authenticationService;
    private final PackageRepository packageRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final Validator validator;

    @Override
    public PackageDto createPackage(String authToken, List<CreateCardCommand> commands) {
        log.debug("Trying to create cards with commands {}", commands);

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);

        Set<ConstraintViolation<CreateCardCommand>> violations = commands.stream()
                .map(command -> validator.validate(command))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        if (!violations.isEmpty()) {
            log.warn("Input validation for card failed!");
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
                        .token((command.id() != null) ? command.id() : UUID.randomUUID())
                        .name(command.name())
                        .damage(command.damage())
                        .cardPackage(pkg)
                        .damageType(damageType)
                        .criticalHitMultiplier(1.2)
                        .build();
            } else {
                return MonsterCard.builder()
                        .token((command.id() != null) ? command.id() : UUID.randomUUID())
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

        log.info("User {} created package {}", user.getUsername(), pkg);
        return new PackageDto(packageRepository.save(pkg));
    }

    @Override
    public void acquirePackage(String authToken) {
        log.debug("Trying to acquire package with auth token {}", authToken);

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);

        if (user.getCoins() < 5) {
            log.warn("User {} has not enough coins to purchase a package!", user);
            throw TransactionValidationException.notEnoughCoins(user.getUsername());
        }

        Package pkg = packageRepository.getPackage().orElseThrow(TransactionValidationException::noPackagesAvailable);

        pkg.getCards().forEach(card -> card.setUser(user));
        pkg.getCards().forEach(card -> card.setCardPackage(null));
        user.getStack().addAll(pkg.getCards());
        user.setCoins(user.getCoins() - 5);

        pkg.getCards().forEach(cardRepository::updateCard);
        userRepository.updateUser(user);
        packageRepository.deletePackage(pkg.getId());

        log.info("User {} acquired package {}", user, pkg);
    }
}

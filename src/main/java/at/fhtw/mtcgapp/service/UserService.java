package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import at.fhtw.mtcgapp.service.command.CreateUserCommand;
import at.fhtw.mtcgapp.service.dto.UserDto;
import at.fhtw.mtcgapp.service.exception.UserValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor

@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final Validator validator;

    public UserDto createUser(CreateUserCommand command) {
        log.info("Trying to create user with command {}", command);

        Set<ConstraintViolation<CreateUserCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            log.warn("Input validation for user failed!");
            throw new ConstraintViolationException(violations);
        }

        if (userRepository.existsByUsername(command.username())) {
            log.warn("User with username {} already exists!", command.username());
            throw UserValidationException.userWithUsernameAlreadyExists(command.username());
        }

        User user = User.builder()
                .token(UUID.randomUUID())
                .username(command.username())
                .password(command.password())
                .bio("")
                .image("")
                .elo(0)
                .battlesFought(0)
                .coins(20)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        log.trace("Mapped command {} to user object {}", command, user);

        log.info("Created user {}", user);
        return new UserDto(userRepository.save(user));
    }
}
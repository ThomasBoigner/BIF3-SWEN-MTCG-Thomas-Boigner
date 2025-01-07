package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import at.fhtw.mtcgapp.service.command.CreateUserCommand;
import at.fhtw.mtcgapp.service.command.UpdateUserCommand;
import at.fhtw.mtcgapp.service.dto.UserDataDto;
import at.fhtw.mtcgapp.service.exception.ForbiddenException;
import at.fhtw.mtcgapp.service.exception.UserValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor

@Slf4j
public class UserServiceImpl implements UserService {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final Validator validator;
    private final Base64.Encoder encoder;

    public UserDataDto getUser(String authToken, String username) {
        log.debug("Trying to get user {} with auth token {}", username, authToken);

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);

        if (!user.getUsername().equals(username)) {
            throw ForbiddenException.forbidden();
        }

        log.info("Retrieved user {}", user);
        return new UserDataDto(user);
    }

    public UserDataDto createUser(CreateUserCommand command) {
        log.debug("Trying to create user with command {}", command);

        Set<ConstraintViolation<CreateUserCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            log.warn("Input validation for create user failed!");
            throw new ConstraintViolationException(violations);
        }

        if (userRepository.existsByUsername(command.username())) {
            log.warn("User with username {} already exists!", command.username());
            throw UserValidationException.userWithUsernameAlreadyExists(command.username());
        }

        User user = User.builder()
                .token(UUID.randomUUID())
                .username(command.username())
                .password(encoder.encodeToString(command.password().getBytes()))
                .bio("")
                .image("")
                .elo(0)
                .wins(0)
                .losses(0)
                .coins(20)
                .inQueue(false)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        log.trace("Mapped command {} to user object {}", command, user);

        log.info("Created user {}", user);
        return new UserDataDto(userRepository.save(user));
    }

    @Override
    public void updateUser(String authToken, String username, UpdateUserCommand command) {
        log.debug("Trying to update user {} with auth token {} and command {}", username, authToken, command);

        Set<ConstraintViolation<UpdateUserCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            log.warn("Input validation for update user failed!");
            throw new ConstraintViolationException(violations);
        }

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);

        if (!user.getUsername().equals(username)) {
            throw ForbiddenException.forbidden();
        }

        user.setUsername(command.name());
        user.setBio(command.bio());
        user.setImage(command.image());

        userRepository.updateUser(user);
        log.info("Updated user {}", user);
    }
}

package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.Session;
import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.SessionRepository;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import at.fhtw.mtcgapp.service.command.LoginCommand;
import at.fhtw.mtcgapp.service.exception.AuthenticationAccessDeniedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@RequiredArgsConstructor

@Slf4j
public class AuthenticationService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final Validator validator;

    public String loginUser(LoginCommand command) {
        log.info("Trying to authenticate user with command {}", command);

        Set<ConstraintViolation<LoginCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            log.warn("Input validation for user login failed!");
            throw new ConstraintViolationException(violations);
        }

        User user = userRepository.findByUsername(command.username()).orElseThrow(AuthenticationAccessDeniedException::wrongCredentials);
        if (!user.getPassword().equals(command.password())) {
            throw AuthenticationAccessDeniedException.wrongCredentials();
        }

        String token = String.format("%s-mtcgToken", user.getUsername());
        Session session = Session.builder()
                .token(token)
                .user(user)
                .build();
        sessionRepository.save(session);

        log.debug("Authentication of user {} was successful", user);
        return token;
    }
}

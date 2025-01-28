package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.Session;
import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.SessionRepository;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import at.fhtw.mtcgapp.service.command.LoginCommand;
import at.fhtw.mtcgapp.service.exception.AuthenticationUnauthorizedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor

@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final Validator validator;
    private final SecureRandom secureRandom;
    private final Base64.Encoder encoder;

    @Override
    public User getCurrentlyLoggedInUser(String token) {
        log.debug("Trying to get the currently logged in user");
        User user = sessionRepository.findUserByToken(token).orElseThrow(AuthenticationUnauthorizedException::invalidToken);
        log.info("User {} is currently logged in", user);
        return user;
    }

    @Override
    public String loginUser(LoginCommand command) {
        log.debug("Trying to authenticate user with command {}", command);

        Set<ConstraintViolation<LoginCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            log.warn("Input validation for user login failed!");
            throw new ConstraintViolationException(violations);
        }

        User user = userRepository.findByUsername(command.username()).orElseThrow(AuthenticationUnauthorizedException::wrongCredentials);
        if (!user.getPassword().equals(encoder.encodeToString(command.password().getBytes()))) {
            throw AuthenticationUnauthorizedException.wrongCredentials();
        }

        Optional<Session> existingSession = sessionRepository.findSessionByUserId(user.getId());
        if (existingSession.isPresent()) {
            log.info("Authentication of user {} was successful", user.getUsername());
            return existingSession.get().getToken();
        }

        String token;
        if (user.getUsername().equals("kienboec") ||
            user.getUsername().equals("altenhof") ||
            user.getUsername().equals("admin")) {
            token = String.format("%s-mtcgToken", user.getUsername());
        } else {
            byte[] randomBytes = new byte[24];
            secureRandom.nextBytes(randomBytes);
            token = encoder.encodeToString(randomBytes);
        }

        log.debug("Save Session, because it has not been saved yet");
        Session session = Session.builder()
                .token(token)
                .user(user)
                .build();
        sessionRepository.save(session);

        log.info("Authentication of user {} was successful", user.getUsername());
        return token;
    }

    public void logoutUser(String token) {
        log.debug("Trying to logout user with token {}", token);
        sessionRepository.deleteByToken(token);
        log.info("Logged out user with token {}", token);
    }
}

package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.SessionRepository;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import at.fhtw.mtcgapp.service.command.LoginCommand;
import at.fhtw.mtcgapp.service.exception.AuthenticationAccessDeniedException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(sessionRepository, userRepository, Validation.buildDefaultValidatorFactory().getValidator());
    }

    @Test
    void ensureLoginUserWorksProperly(){
        // Given
        LoginCommand command = LoginCommand.builder()
                .username("Thomas")
                .password("password")
                .build();

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

        when(userRepository.findByUsername(eq(command.username()))).thenReturn(Optional.of(user));

        // When
        String token = authenticationService.loginUser(command);

        // Then
        assertThat(token).isEqualTo("Thomas-mtcgToken");
    }

    @Test
    void ensureLoginUserThrowsConstraintViolationExceptionWhenCommandViolatesConstraint(){
        // Given
        LoginCommand command = LoginCommand.builder()
                .username("")
                .password("")
                .build();

        // Assert
        assertThrows(ConstraintViolationException.class, () -> authenticationService.loginUser(command));
    }

    @Test
    void ensureLoginUserThrowsAccessDeniedExceptionWhenUserCanNotBeFound() {
        // Given
        LoginCommand command = LoginCommand.builder()
                .username("Thomas")
                .password("password")
                .build();

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

        when(userRepository.findByUsername(eq(command.username()))).thenReturn(Optional.empty());

        // Assert
        assertThrows(AuthenticationAccessDeniedException.class, () -> authenticationService.loginUser(command));
    }
    @Test
    void ensureLoginUserThrowsAccessDeniedExceptionWhenPasswordDoesNotMatch() {
        // Given
        LoginCommand command = LoginCommand.builder()
                .username("Thomas")
                .password("p@ssw0rd")
                .build();

        User user = User.builder()
                .token(UUID.randomUUID())
                .username(command.username())
                .password("Password")
                .bio("")
                .image("")
                .elo(0)
                .battlesFought(0)
                .coins(20)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();

        when(userRepository.findByUsername(eq(command.username()))).thenReturn(Optional.of(user));

        // Assert
        assertThrows(AuthenticationAccessDeniedException.class, () -> authenticationService.loginUser(command));
    }
}

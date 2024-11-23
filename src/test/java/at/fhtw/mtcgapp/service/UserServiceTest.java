package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import at.fhtw.mtcgapp.service.command.CreateUserCommand;
import at.fhtw.mtcgapp.service.dto.UserDto;
import at.fhtw.mtcgapp.service.exception.UserValidationException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private Base64.Encoder encoder = Base64.getEncoder();

    @BeforeEach
    void setUp(){
        userService = new UserService(userRepository, Validation.buildDefaultValidatorFactory().getValidator(), encoder);
    }

    @Test
    void ensureCreateUserWorksProperly(){
        // Given
        CreateUserCommand command = CreateUserCommand.builder()
                .username("Thomas")
                .password("password")
                .build();
        when(userRepository.existsByUsername(eq(command.username()))).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        // When
        UserDto userDto = userService.createUser(command);

        // Then
        assertThat(userDto.id()).isNotNull();
        assertThat(userDto.username()).isEqualTo(command.username());
        assertThat(userDto.bio()).isEqualTo("");
        assertThat(userDto.image()).isEqualTo("");
        assertThat(userDto.elo()).isEqualTo(0);
        assertThat(userDto.battlesFought()).isEqualTo(0);
        assertThat(userDto.coins()).isEqualTo(20);
    }

    @Test
    void ensureCreateUserThrowsUserValidationExceptionWhenUserWithUsernameAlreadyExists(){
        // Given
        CreateUserCommand command = CreateUserCommand.builder()
                .username("Thomas")
                .password("password")
                .build();
        when(userRepository.existsByUsername(eq(command.username()))).thenReturn(true);

        // Assert
        assertThrows(UserValidationException.class, () -> userService.createUser(command));
    }

    @Test
    void ensureCreateUserThrowsConstraintViolationExceptionWhenCommandViolatesConstraint(){
        // Given
        CreateUserCommand command = CreateUserCommand.builder()
                .username("Thomas")
                .password("pwd")
                .build();

        // Assert
        assertThrows(ConstraintViolationException.class, () -> userService.createUser(command));
    }
}

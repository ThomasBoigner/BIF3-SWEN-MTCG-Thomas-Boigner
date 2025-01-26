package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import at.fhtw.mtcgapp.service.command.CreateUserCommand;
import at.fhtw.mtcgapp.service.command.UpdateUserCommand;
import at.fhtw.mtcgapp.service.dto.UserDataDto;
import at.fhtw.mtcgapp.service.exception.ForbiddenException;
import at.fhtw.mtcgapp.service.exception.UserValidationException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private UserService userService;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private UserRepository userRepository;
    private Base64.Encoder encoder = Base64.getEncoder();

    @BeforeEach
    void setUp(){
        userService = new UserServiceImpl(authenticationService, userRepository, Validation.buildDefaultValidatorFactory().getValidator(), encoder);
    }

    @Test
    void ensureGetUserWorksProperly() {
        // Given
        String authToken = "Thomas-mtgcToken";

        User user = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(0)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);

        // When
        UserDataDto returned = userService.getUser(authToken, "Thomas");

        // Then
        assertThat(returned).isEqualTo(new UserDataDto(user));
    }

    @Test
    void ensureGetUserThrowsForbiddenExceptionWhenUsernameDoesNotMatch() {
        // Given
        String authToken = "Thomas-mtgcToken";

        User user = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(0)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);

        // When
        assertThrows(ForbiddenException.class, () -> userService.getUser(authToken, "Tom"));
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
        UserDataDto userDto = userService.createUser(command);

        // Then
        assertThat(userDto.id()).isNotNull();
        assertThat(userDto.username()).isEqualTo(command.username());
        assertThat(userDto.bio()).isEqualTo("");
        assertThat(userDto.image()).isEqualTo("");
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

    @Test
    void ensureUpdateUserWorksProperly(){
        // Given
        String authToken = "Thomas-mtgcToken";

        UpdateUserCommand userCommand = UpdateUserCommand.builder()
                .name("Domas")
                .bio("me codin...")
                .image(":^)")
                .build();

        User user = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(0)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);
        when(userRepository.existsByUsername(eq(userCommand.name()))).thenReturn(false);

        // When
        userService.updateUser(authToken, "Thomas", userCommand);

        // Then
        assertThat(user.getUsername()).isEqualTo("Domas");
        assertThat(user.getBio()).isEqualTo("me codin...");
        assertThat(user.getImage()).isEqualTo(":^)");
    }

    @Test
    void ensureUpdateUserThrowsConstraintViolationExceptionWhenCommandViolatesConstraint(){
        // Given
        String authToken = "Thomas-mtgcToken";

        UpdateUserCommand userCommand = UpdateUserCommand.builder()
                .name("")
                .bio(null)
                .image(null)
                .build();

        User user = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(0)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();

        // When
        assertThrows(ConstraintViolationException.class, () -> userService.updateUser(authToken, "Thomas", userCommand));
    }

    @Test
    void ensureUpdateUserThrowsForbiddenExceptionWhenUsernameDoesNotMatch() {
        // Given
        String authToken = "Thomas-mtgcToken";

        UpdateUserCommand userCommand = UpdateUserCommand.builder()
                .name("Domas")
                .bio("me codin...")
                .image(":^)")
                .build();

        User user = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(0)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);

        // When
        assertThrows(ForbiddenException.class, () -> userService.updateUser(authToken, "Momas", userCommand));
    }

    @Test
    void ensureUpdateUserThrowsUserValidationExceptionWhenUsernameIsTaken() {
        // Given
        String authToken = "Thomas-mtgcToken";

        UpdateUserCommand userCommand = UpdateUserCommand.builder()
                .name("Domas")
                .bio("me codin...")
                .image(":^)")
                .build();

        User user = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(0)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);
        when(userRepository.existsByUsername(eq(userCommand.name()))).thenReturn(true);

        // When
        assertThrows(UserValidationException.class, () -> userService.updateUser(authToken, "Thomas", userCommand));
    }

    @Test
    void ensureUpdateUserWorksProperlyWhenUpdatingToSameUsername(){
        // Given
        String authToken = "Thomas-mtgcToken";

        UpdateUserCommand userCommand = UpdateUserCommand.builder()
                .name("Thomas")
                .bio("me codin...")
                .image(":^)")
                .build();

        User user = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(0)
                .wins(0)
                .losses(0)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);
        when(userRepository.existsByUsername(eq(userCommand.name()))).thenReturn(true);

        // When
        userService.updateUser(authToken, "Thomas", userCommand);

        // Then
        assertThat(user.getUsername()).isEqualTo("Thomas");
        assertThat(user.getBio()).isEqualTo("me codin...");
        assertThat(user.getImage()).isEqualTo(":^)");
    }
}

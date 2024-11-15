package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.UserService;
import at.fhtw.mtcgapp.service.command.CreateUserCommand;
import at.fhtw.mtcgapp.service.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private UserController userController;
    @Mock
    private UserService userService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        userController = new UserController(userService, objectMapper);
    }

    @Test
    void ensureCreateUserWorksProperly() throws JsonProcessingException {
        // Given
        CreateUserCommand userCommand = CreateUserCommand.builder()
                .username("Thomas")
                .password("pwd")
                .build();

        Request request = Request.builder()
                .method(Method.POST)
                .pathname("/users")
                .body(objectMapper.writeValueAsString(userCommand))
                .build();

        UserDto userDto = UserDto.builder()
                .username("Thomas")
                .bio("")
                .image("")
                .elo(0)
                .battlesFought(0)
                .coins(20)
                .build();

        when(userService.createUser(eq(userCommand))).thenReturn(userDto);

        // When
        Response response = userController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getMessage()).isEqualTo("CREATED");
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContent()).isEqualTo(objectMapper.writeValueAsString(userDto));
    }

    @Test
    void ensureCreateUserReturnsStatus400WhenCommandCanNotBeParsed() throws JsonProcessingException {
        // Given
        Request request = Request.builder()
                .method(Method.POST)
                .pathname("/users")
                .body("")
                .build();

        UserDto userDto = UserDto.builder()
                .username("Thomas")
                .bio("")
                .image("")
                .elo(0)
                .battlesFought(0)
                .coins(20)
                .build();

        // When
        Response response = userController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }
}

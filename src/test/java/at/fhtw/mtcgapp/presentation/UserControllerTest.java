package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.UserService;
import at.fhtw.mtcgapp.service.command.CreateUserCommand;
import at.fhtw.mtcgapp.service.command.UpdateUserCommand;
import at.fhtw.mtcgapp.service.dto.UserDataDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
    void ensureGetUserWorksProperly() throws JsonProcessingException {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtcgToken");

        UserDataDto userDto = UserDataDto.builder()
                .username("Thomas")
                .bio("")
                .image("")
                .coins(20)
                .build();

        Request request = Request.builder()
                .method(Method.GET)
                .headerMap(headerMap)
                .pathname("/users/Thomas")
                .pathParts(List.of("users", "Thomas"))
                .build();
        when(userService.getUser(eq("Thomas-mtcgToken"), eq("Thomas"))).thenReturn(userDto);

        // When
        Response response = userController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("OK");
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContent()).isEqualTo(objectMapper.writeValueAsString(userDto));
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

        UserDataDto userDto = UserDataDto.builder()
                .username("Thomas")
                .bio("")
                .image("")
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
    void ensureCreateUserReturnsStatus400WhenCommandCanNotBeParsed() {
        // Given
        Request request = Request.builder()
                .method(Method.POST)
                .pathname("/users")
                .body("")
                .build();

        // When
        Response response = userController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void ensureCreateUserReturnsStatus400WhenBodyIsNull() {
        // Given
        Request request = Request.builder()
                .method(Method.POST)
                .pathname("/users")
                .body(null)
                .build();

        // When
        Response response = userController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentType()).isEqualTo("text/plain");
        assertThat(response.getContent()).isEqualTo("Body must not be null!");
    }

    @Test
    void ensureUpdateUserWorksProperly() throws JsonProcessingException {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtcgToken");

        UpdateUserCommand userCommand = UpdateUserCommand.builder()
                .name("Thomas")
                .bio("me codin...")
                .image(":^)")
                .build();

        Request request = Request.builder()
                .method(Method.PUT)
                .pathname("/users/Thomas")
                .headerMap(headerMap)
                .pathParts(List.of("users", "Thomas"))
                .body(objectMapper.writeValueAsString(userCommand))
                .build();

        // When
        Response response = userController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void ensureUpdateUserReturnsStatus400WhenCommandCanNotBeParsed() {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtcgToken");

        Request request = Request.builder()
                .method(Method.PUT)
                .pathname("/users/Thomas")
                .headerMap(headerMap)
                .pathParts(List.of("users", "Thomas"))
                .body("")
                .build();

        // When
        Response response = userController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void ensureUpdateUserReturnsStatus400WhenBodyIsNull() {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtcgToken");

        Request request = Request.builder()
                .method(Method.PUT)
                .pathname("/users/Thomas")
                .headerMap(headerMap)
                .pathParts(List.of("users", "Thomas"))
                .body(null)
                .build();

        // When
        Response response = userController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }
}

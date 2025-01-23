package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.AuthenticationService;
import at.fhtw.mtcgapp.service.command.LoginCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    private AuthenticationController authenticationController;
    @Mock
    private AuthenticationService authenticationService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        authenticationController = new AuthenticationController(authenticationService, objectMapper);
    }

    @Test
    void ensureLoginWorksProperly() throws JsonProcessingException {
        // Given
        LoginCommand loginCommand = LoginCommand.builder()
                .username("Thomas")
                .password("password")
                .build();

        Request request = Request.builder()
                .method(Method.POST)
                .pathname("/sessions")
                .body(objectMapper.writeValueAsString(loginCommand))
                .build();

        when(authenticationService.loginUser(eq(loginCommand))).thenReturn("Thomas-mtgcToken");

       // When
       Response response = authenticationController.handleRequest(request);

       // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("OK");
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContent()).isEqualTo("Thomas-mtgcToken");
    }

    @Test
    void ensureLoginReturnsStatus400WhenCommandCanNotBeParsed() {
        // Given
        Request request = Request.builder()
                .method(Method.POST)
                .pathname("/sessions")
                .body("")
                .build();

        // When
        Response response = authenticationController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void ensureLoginReturnsStatus400WhenBodyIsNull() {
        // Given
        Request request = Request.builder()
                .method(Method.POST)
                .pathname("/sessions")
                .body(null)
                .build();

        // When
        Response response = authenticationController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentType()).isEqualTo("text/plain");
        assertThat(response.getContent()).isEqualTo("Body must not be null!");
    }

    @Test
    void ensureLogoutWorksProperly() {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        Request request = Request.builder()
                .method(Method.GET)
                .headerMap(headerMap)
                .pathname("/sessions/logout")
                .build();

        // When
        Response response = authenticationController.handleRequest(request);

        // Then
        verify(authenticationService).logoutUser(eq("Thomas-mtgcToken"));
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void ensureLogoutReturnsStatus401IfUserIsNotAuthenticated() {
        // Given
        Request request = Request.builder()
                .method(Method.GET)
                .pathname("/sessions/logout")
                .build();

        // When
        Response response = authenticationController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(401);
    }
}

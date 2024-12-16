package at.fhtw.mtcgapp.presentation;

import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcgapp.service.PackageService;
import at.fhtw.mtcgapp.service.command.CreateCardCommand;
import at.fhtw.mtcgapp.service.dto.CardDto;
import at.fhtw.mtcgapp.service.dto.PackageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PackageControllerTest {
    private PackageController packageController;
    @Mock
    private PackageService packageService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
        packageController = new PackageController(packageService, objectMapper);
    }

    @Test
    void ensureCreatePackageWorksProperly() throws JsonProcessingException {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        CreateCardCommand createCardCommand1 = CreateCardCommand.builder()
                .id(UUID.randomUUID())
                .name("test1")
                .damage(10)
                .build();

        CreateCardCommand createCardCommand2 = CreateCardCommand.builder()
                .id(UUID.randomUUID())
                .name("test2")
                .damage(20)
                .build();

        Request request = Request.builder()
                .method(Method.POST)
                .headerMap(headerMap)
                .pathname("/packages")
                .body(objectMapper.writeValueAsString(List.of(createCardCommand1, createCardCommand2)))
                .build();

        PackageDto packageDto = PackageDto.builder()
                .id(UUID.randomUUID())
                .price(10)
                .cards(List.of(
                        CardDto.builder()
                                .id(createCardCommand1.id())
                                .name(createCardCommand1.name())
                                .damage(createCardCommand1.damage()).build(),
                        CardDto.builder()
                                .id(createCardCommand2.id())
                                .name(createCardCommand2.name())
                                .damage(createCardCommand2.damage()).build()))
                .build();

        when(packageService.createPackage(eq("Thomas-mtgcToken"), eq(List.of(createCardCommand1, createCardCommand2)))).thenReturn(packageDto);

        // When
        Response response = packageController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getMessage()).isEqualTo("CREATED");
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContent()).isEqualTo(objectMapper.writeValueAsString(packageDto));
    }


    @Test
    void ensureCreatePackageReturnsStatus400WhenCommandCanNotBeParsed() {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        Request request = Request.builder()
                .method(Method.POST)
                .headerMap(headerMap)
                .pathname("/users")
                .body("")
                .build();

        // When
        Response response = packageController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void ensureCreatePackageReturnsStatus400WhenBodyIsNull() {
        // Given
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:Bearer Thomas-mtgcToken");

        Request request = Request.builder()
                .method(Method.POST)
                .headerMap(headerMap)
                .pathname("/packages")
                .body(null)
                .build();

        // When
        Response response = packageController.handleRequest(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContent()).isEqualTo("Body must not be null!");
    }
}

package at.fhtw.mtcgapp.service.command;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateUserCommand(
        @JsonAlias({"Username"})
        @NotBlank(message = "Username must not be blank!")
        String username,
        @JsonAlias({"Password"})
        @NotBlank(message = "Password must not be blank!")
        @Size(min = 4, message = "Password must have at least 4 characters!")
        String password) {
}

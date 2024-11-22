package at.fhtw.mtcgapp.service.command;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginCommand(
        @JsonAlias({"Username"})
        @NotBlank(message = "Username must not be blank")
        String username,
        @JsonAlias({"Password"})
        @NotBlank(message = "Password must not be blank")
        String password
) { }

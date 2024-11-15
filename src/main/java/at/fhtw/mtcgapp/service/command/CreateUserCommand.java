package at.fhtw.mtcgapp.service.command;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;

@Builder
public record CreateUserCommand(
        @JsonAlias({"Username"})
        String username,
        @JsonAlias({"Password"})
        String password) { }

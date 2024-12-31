package at.fhtw.mtcgapp.service.command;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateUserCommand (
        @JsonAlias({"Name"})
        @NotBlank(message = "Username must not be blank!")
        String name,
        @JsonAlias({"Bio"})
        @NotNull(message = "Bio must not be null!")
        String bio,
        @JsonAlias({"Image"})
        @NotNull(message = "Image must not be null!")
        String image
) { }

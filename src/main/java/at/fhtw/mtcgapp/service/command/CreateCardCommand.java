package at.fhtw.mtcgapp.service.command;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateCardCommand(
        @JsonAlias({"Id"})
        UUID id,
        @JsonAlias({"Name"})
        @NotBlank(message = "Name must not be null")
        String name,
        @JsonAlias({"Damage"})
        @PositiveOrZero(message = "Damage must be positive")
        double damage
) { }

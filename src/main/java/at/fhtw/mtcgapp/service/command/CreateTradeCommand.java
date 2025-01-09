package at.fhtw.mtcgapp.service.command;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateTradeCommand(
        @JsonAlias({"Id"})
        UUID id,
        @JsonAlias({"CardToTrade"})
        @NotNull(message = "Card to trade must not be null")
        UUID cardToTrade,
        @JsonAlias({"Type"})
        @NotBlank(message = "Card type must not be blank")
        String type,
        @JsonAlias({"MinimumDamage"})
        @PositiveOrZero(message = "Minimum damage must be positive")
        double minimumDamage
) { }

package at.fhtw.mtcgapp.service.dto;

import at.fhtw.mtcgapp.model.User;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Builder
public record UserDto(UUID id, String username, String bio, String image, int coins, int elo, int battlesFought) {
    public UserDto(User user) {
        this(user.getToken(), user.getUsername(), user.getBio(), user.getImage(), user.getCoins(), user.getElo(), user.getBattlesFought());
        log.debug("Created UserDto {}", this);
    }
}

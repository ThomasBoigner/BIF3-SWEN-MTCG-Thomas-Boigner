package at.fhtw.mtcgapp.service.dto;

import at.fhtw.mtcgapp.model.User;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public record UserStatsDto(String name, int elo, int wins, int losses) {
    public UserStatsDto(User user) {
        this(user.getUsername(), user.getElo(), user.getWins(), user.getLosses());
    }
}

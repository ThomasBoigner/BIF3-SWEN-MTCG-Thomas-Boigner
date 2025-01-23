package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.service.dto.UserStatsDto;

import java.util.List;

public interface StatsService {
    UserStatsDto getUserStats(String authToken);
    List<UserStatsDto> getScoreBoard(String authToken);
}

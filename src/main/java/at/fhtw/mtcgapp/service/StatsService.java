package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.service.dto.UserStatsDto;

public interface StatsService {
    UserStatsDto getUserStats(String authToken);
}

package at.fhtw.mtcgapp.presentation;

import at.fhtw.mtcgapp.service.StatsServiceSingleton;
import at.fhtw.mtcgapp.util.ObjectMapperSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatsControllerSingleton {
    INSTANCE(new StatsController(
            StatsServiceSingleton.INSTANCE.getStatsService(),
            ObjectMapperSingleton.INSTANCE.getObjectMapper()));

    private final StatsController statsController;
}

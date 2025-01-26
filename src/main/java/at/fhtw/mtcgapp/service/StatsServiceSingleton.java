package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.persistence.repository.UserRepositorySingleton;
import lombok.Getter;

@Getter
public enum StatsServiceSingleton {
    INSTANCE(new StatsServiceImpl(AuthenticationServiceSingleton.INSTANCE.getAuthenticationService(), UserRepositorySingleton.INSTANCE.getUserRepository()));

    private final StatsService statsService;

    StatsServiceSingleton(StatsService statsService) {
        this.statsService = statsService;
    }
}

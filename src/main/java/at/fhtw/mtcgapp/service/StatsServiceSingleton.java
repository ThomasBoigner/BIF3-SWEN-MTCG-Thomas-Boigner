package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.persistence.repository.UserRepositorySingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatsServiceSingleton {
    INSTANCE(new StatsServiceImpl(AuthenticationServiceSingleton.INSTANCE.getAuthenticationService(), UserRepositorySingleton.INSTANCE.getUserRepository()));

    private final StatsService statsService;
}
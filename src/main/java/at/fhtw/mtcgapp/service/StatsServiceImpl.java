package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import at.fhtw.mtcgapp.service.dto.UserStatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor

@Slf4j
public class StatsServiceImpl implements StatsService{
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @Override
    public UserStatsDto getUserStats(String authToken) {
        log.debug("Trying to get stats of user with auth token {}", authToken);

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);

        log.info("Retrieved user {}", user);
        return new UserStatsDto(user);
    }
}

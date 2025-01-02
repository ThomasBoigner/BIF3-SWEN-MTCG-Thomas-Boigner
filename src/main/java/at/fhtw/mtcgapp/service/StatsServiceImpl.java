package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import at.fhtw.mtcgapp.service.dto.UserStatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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

    @Override
    public List<UserStatsDto> getScoreBoard(String authToken) {
        log.debug("Trying to get scoreboard with auth token {}", authToken);

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);
        List<UserStatsDto> scoreBoard = userRepository.findAllUsers().stream().map(UserStatsDto::new).toList();

        log.info("User {} retrieved scoreboard with {} entries", user.getUsername(), scoreBoard.size());
        return scoreBoard;
    }
}

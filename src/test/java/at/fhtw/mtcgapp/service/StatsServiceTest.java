package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import at.fhtw.mtcgapp.service.dto.UserStatsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatsServiceTest {
    private StatsService statsService;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        statsService = new StatsServiceImpl(authenticationService, userRepository);
    }

    @Test
    void ensureGetUserStatsWorksProperly() {
        // Given
        String authToken = "Thomas-mtgcToken";

        User user = User.builder()
                .id(0)
                .token(UUID.randomUUID())
                .username("Thomas")
                .password("pwd")
                .bio("bio")
                .image("image")
                .coins(20)
                .elo(100)
                .wins(4)
                .losses(3)
                .deck(new ArrayList<>())
                .stack(new ArrayList<>())
                .trades(new ArrayList<>())
                .build();
        when(authenticationService.getCurrentlyLoggedInUser(eq(authToken))).thenReturn(user);

        // When
        UserStatsDto userStatsDto = statsService.getUserStats(authToken);

        // Then
        assertThat(userStatsDto.name()).isEqualTo(user.getUsername());
        assertThat(userStatsDto.elo()).isEqualTo(user.getElo());
        assertThat(userStatsDto.wins()).isEqualTo(user.getWins());
    }
}

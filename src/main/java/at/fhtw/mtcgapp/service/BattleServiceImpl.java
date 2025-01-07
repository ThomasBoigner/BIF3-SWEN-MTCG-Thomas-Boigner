package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor

@Slf4j
public class BattleServiceImpl implements BattleService {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @Override
    public void battleUser(String authToken) {
        log.debug("Trying to battle with user with auth token {}", authToken);

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);

        Optional<User> enemyOptional = userRepository.getUserInQueue();

        if (enemyOptional.isEmpty()) {
            user.setInQueue(true);
            userRepository.updateUser(user);
            return;
        }

        User enemy = enemyOptional.get();
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

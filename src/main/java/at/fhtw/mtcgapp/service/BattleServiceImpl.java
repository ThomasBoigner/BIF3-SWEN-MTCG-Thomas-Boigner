package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.Card;
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

        User player1 = authenticationService.getCurrentlyLoggedInUser(authToken);

        Optional<User> player2Optional = userRepository.getUserInQueue();

        if (player2Optional.isEmpty()) {
            player1.setInQueue(true);
            userRepository.updateUser(player1);
            return;
        }

        User player2 = player2Optional.get();
        for (int i = 0;
             i < 100 && !player1.getDeck().isEmpty() && !player2.getDeck().isEmpty();
             i++) {
            Card cardPlayer1 = player1.getDeck().get((int) (Math.random() * (player1.getDeck().size() - 1)));
            Card cardPlayer2 = player2.getDeck().get((int) (Math.random() * (player1.getDeck().size() - 1)));

            int result = battleRound(cardPlayer1, cardPlayer2);

            if (result > 0) {
                player1.getDeck().add(cardPlayer2);
                player2.getDeck().remove(cardPlayer2);
            } else {
                player2.getDeck().add(cardPlayer1);
                player1.getDeck().remove(cardPlayer1);
            }
        }
    }

    public int battleRound(Card player1, Card player2) {
        return 0;
    }
}

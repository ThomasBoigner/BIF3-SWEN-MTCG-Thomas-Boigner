package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.*;
import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import at.fhtw.mtcgapp.service.exception.BattleValidationException;
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

        if (player1.getDeck().isEmpty()) {
            log.warn("User {} has no deck configured", player1.getUsername());
            throw BattleValidationException.deckNotConfigured();
        }

        Optional<User> player2Optional = userRepository.getUserInQueue();

        if (player2Optional.isEmpty()) {
            log.debug("No other player is in the queue, adding player to queue");
            player1.setInQueue(true);
            userRepository.updateUser(player1);
            return;
        }

        User player2 = player2Optional.get();

        if (player1.equals(player2)) {
            log.warn("User {} is the same as the one in queue", player1.getUsername());
            throw BattleValidationException.sameUser();
        }

        player2.setInQueue(false);

        log.info("{} will fight against {} ", player1.getUsername(), player2.getUsername());
        for (int i = 0;
             i < 100 && !player1.getDeck().isEmpty() && !player2.getDeck().isEmpty();
             i++) {
            log.info("Round {}, {} has {} cards and {} has {} cards", i, player1.getUsername(), player1.getDeck().size(), player2.getUsername(), player2.getDeck().size());
            Card cardPlayer1 = player1.getDeck().get((int) (Math.random() * (player1.getDeck().size() - 1)));
            Card cardPlayer2 = player2.getDeck().get((int) (Math.random() * (player2.getDeck().size() - 1)));
            log.info("{} fights with card {} and {} with card {}", player1.getUsername(), cardPlayer1, player2.getUsername(), cardPlayer2);

            double player1Damage = cardPlayer1.calculateDamage(cardPlayer2);
            double player2Damage = cardPlayer2.calculateDamage(cardPlayer1);

            log.info("{} deals {} damage, {} deals {} damage", player1.getUsername(), player1Damage, player2.getUsername(), player2Damage);
            if (player1Damage > player2Damage) {
                log.info("{} won the round", player1.getUsername());
                player1.getDeck().add(cardPlayer2);
                player2.getDeck().remove(cardPlayer2);
            } if(player2Damage > player1Damage) {
                log.info("{} won the round", player2.getUsername());
                player2.getDeck().add(cardPlayer1);
                player1.getDeck().remove(cardPlayer1);
            } else {
                log.info("Round ended in a draw");
            }
        }

        if (player1.getDeck().isEmpty()) {
            log.info("{} won the battle", player2.getUsername());
            player2.setElo(player2.getElo() + 3);
            player2.setWins(player2.getWins() + 1);
            player1.setElo(player1.getElo() - 5);
            player1.setLosses(player1.getLosses() + 1);
        }

        if (player2.getDeck().isEmpty()) {
            log.info("{} won the battle", player1.getUsername());
            player1.setElo(player1.getElo() + 3);
            player1.setWins(player1.getWins() + 1);
            player2.setElo(player2.getElo() - 5);
            player2.setLosses(player2.getLosses() + 1);
        }

        userRepository.updateUser(player1);
        userRepository.updateUser(player2);
    }
}

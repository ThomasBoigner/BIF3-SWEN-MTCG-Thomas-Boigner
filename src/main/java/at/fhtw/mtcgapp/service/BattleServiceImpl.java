package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.*;
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
        player2.setInQueue(false);

        for (int i = 0;
             i < 100 && !player1.getDeck().isEmpty() && !player2.getDeck().isEmpty();
             i++) {
            Card cardPlayer1 = player1.getDeck().get((int) (Math.random() * (player1.getDeck().size() - 1)));
            Card cardPlayer2 = player2.getDeck().get((int) (Math.random() * (player1.getDeck().size() - 1)));

            double result = battleRound(cardPlayer1, cardPlayer2);

            if (result > 0) {
                player1.getDeck().add(cardPlayer2);
                player2.getDeck().remove(cardPlayer2);
            } else {
                player2.getDeck().add(cardPlayer1);
                player1.getDeck().remove(cardPlayer1);
            }
        }

        if (player1.getDeck().isEmpty()) {
            player2.setElo(player2.getElo() + 3);
            player2.setWins(player2.getWins() + 1);
            player1.setElo(player1.getElo() - 5);
            player1.setLosses(player1.getLosses() + 1);
        }

        if (player2.getDeck().isEmpty()) {
            player1.setElo(player1.getElo() + 3);
            player1.setWins(player1.getWins() + 1);
            player2.setElo(player2.getElo() - 5);
            player2.setLosses(player2.getLosses() + 1);
        }

        userRepository.updateUser(player1);
        userRepository.updateUser(player2);
    }

    public double battleRound(Card cardPlayer1, Card cardPlayer2) {
        log.info("Player 1 fights with card {} and player 2 with card {}", cardPlayer1, cardPlayer2);
        double player1Damage = cardPlayer1.getDamage();
        double player2Damage = cardPlayer2.getDamage();

        if (cardPlayer1 instanceof MonsterCard) {
            player2Damage = player2Damage - ((MonsterCard) cardPlayer1).getDefence();
            log.info("Player 1's card is a monster card that reduces the damage of player 2. Player 2's damage is now {}", player2Damage);
        }

        if (cardPlayer2 instanceof MonsterCard) {
            player1Damage = player1Damage - ((MonsterCard) cardPlayer2).getDefence();
            log.info("Player 2's card is a monster card that reduces the damage of player 1. Player 1's damage is now {}", player1Damage);
        }

        if (cardPlayer1 instanceof SpellCard) {
            player1Damage = player1Damage * ((SpellCard) cardPlayer1).getCriticalHitMultiplier();
            log.info("Player 1's card is a spell card that multiplied his damage. Player 1's damage is now {}", player1Damage);
        }

        if (cardPlayer2 instanceof SpellCard) {
            player2Damage = player2Damage * ((SpellCard) cardPlayer2).getCriticalHitMultiplier();
            log.info("Player 2's card is a spell card that multiplied his damage. Player 2's damage is now {}", player2Damage);
        }

        if (cardPlayer1 instanceof SpellCard || cardPlayer2 instanceof SpellCard) {
            if ((cardPlayer1.getDamageType() == DamageType.WATER && cardPlayer2.getDamageType() == DamageType.FIRE) ||
                (cardPlayer1.getDamageType() == DamageType.FIRE && cardPlayer2.getDamageType() == DamageType.NORMAL) ||
                (cardPlayer1.getDamageType() == DamageType.NORMAL && cardPlayer2.getDamageType() == DamageType.WATER)) {
                player1Damage = player1Damage * 2;
                log.info("Player 1 has an element advantage over Player 2 and doubles his damage. Player 1's damage is now {}", player1Damage);
            }

            if ((cardPlayer2.getDamageType() == DamageType.WATER && cardPlayer1.getDamageType() == DamageType.FIRE) ||
                (cardPlayer2.getDamageType() == DamageType.FIRE && cardPlayer1.getDamageType() == DamageType.NORMAL) ||
                (cardPlayer2.getDamageType() == DamageType.NORMAL && cardPlayer1.getDamageType() == DamageType.WATER)) {
                player2Damage = player2Damage * 2;
                log.info("Player 2 has an element advantage over Player 1 and doubles his damage. Player 2's damage is now {}", player2Damage);
            }
        }

        if ((cardPlayer1.getName().contains("Goblin") && cardPlayer2.getName().contains("Dragon")) ||
            (cardPlayer1.getName().contains("Ork") && cardPlayer2.getName().contains("Wizzard")) ||
            (cardPlayer1.getName().contains("Knight") && cardPlayer2.getName().equals("WaterSpell")) ||
            (cardPlayer1.getName().contains("Spell") && cardPlayer2.getName().contains("Kraken")) ||
            (cardPlayer1.getName().equals("FireElves") && cardPlayer2.getName().contains("Dragon"))) {
            player1Damage = 0;
            log.info("Player 1's card can not fight against player 2's card, therefore his damage is set to 0");
        }

        if ((cardPlayer2.getName().contains("Goblin") && cardPlayer1.getName().contains("Dragon")) ||
            (cardPlayer2.getName().contains("Ork") && cardPlayer1.getName().contains("Wizzard")) ||
            (cardPlayer2.getName().contains("Knight") && cardPlayer1.getName().equals("WaterSpell")) ||
            (cardPlayer2.getName().contains("Spell") && cardPlayer1.getName().contains("Kraken")) ||
            (cardPlayer2.getName().equals("FireElves") && cardPlayer1.getName().contains("Dragon"))) {
            player2Damage = 0;
            log.info("Player 2's card can not fight against player 1's card, therefore his damage is set to 0");
        }

        double result = player1Damage - player2Damage;
        log.info("The result is {}", result);
        return result;
    }
}

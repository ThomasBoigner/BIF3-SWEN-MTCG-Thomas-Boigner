package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.Card;
import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.CardRepository;
import at.fhtw.mtcgapp.service.dto.CardDto;
import at.fhtw.mtcgapp.service.exception.CardValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Slf4j
public class CardServiceImpl implements CardService{
    private final CardRepository cardRepository;
    private final AuthenticationService authenticationService;

    public List<CardDto> getCardsOfUser(String authToken) {
        log.debug("Trying to get all cards of user with authToken {}", authToken);

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);
        List<Card> cards = user.getStack();

        log.info("Found {} cards for user {}", cards.size(), user.getUsername());
        return cards.stream().map(CardDto::new).toList();
    }

    @Override
    public List<CardDto> getDeckOfUser(String authToken) {
        log.debug("Trying to get cards in deck of user with authToken {}", authToken);

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);
        List<Card> cards = user.getDeck();

        log.info("Found {} cards in deck of user {}", cards.size(), user.getUsername());
        return cards.stream().map(CardDto::new).toList();
    }

    @Override
    public void configureDeck(String authToken, List<UUID> cardIds) {
        log.debug("Trying to configure deck of user with authToken {} with cardIds {}", authToken, cardIds);

        if (cardIds.size() != 4) {
            log.warn("Tried to configure deck with wrong number cards");
            throw CardValidationException.wrongNumberOfCards();
        }

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);

        user.getStack().addAll(user.getDeck());
        user.getDeck().clear();

        List<Card> newDeck = user.getStack().stream().filter(card -> cardIds.contains(card.getToken())).toList();
        if (newDeck.size() != 4) {
            log.warn("User does not own one of the cards");
            throw CardValidationException.userDoesNotOwnCard();
        }

        user.getStack().removeIf(card -> cardIds.contains(card.getToken()));
        user.setDeck(newDeck);

        cardRepository.resetDeckOfUser(user.getId());
        cardRepository.configureDeckOfUser(user.getDeck());

        log.info("Configured deck to contain cards {}", user.getDeck());
    }
}

package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.Card;
import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.CardRepository;
import at.fhtw.mtcgapp.service.dto.CardDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor

@Slf4j
public class CardServiceImpl implements CardService{
    private final CardRepository cardRepository;
    private final AuthenticationService authenticationService;

    public List<CardDto> getCardsOfUser(String authToken) {
        log.debug("Trying to get all cards of user with authToken {}", authToken);

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);
        List<Card> cards = cardRepository.getCardsOfUser(user.getId());

        log.info("Found {} cards for user {}", cards.size(), user.getUsername());
        return cards.stream().map(CardDto::new).toList();
    }

    @Override
    public List<CardDto> getDeckOfUser(String authToken) {
        log.debug("Trying to get cards in deck of user with authToken {}", authToken);

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);
        List<Card> cards = cardRepository.getCardsInDeckOfUser(user.getId());

        log.info("Found {} cards in deck of user {}", cards.size(), user.getUsername());
        return cards.stream().map(CardDto::new).toList();
    }

    @Override
    public void configureDeck(String authToken, List<String> cardIds) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

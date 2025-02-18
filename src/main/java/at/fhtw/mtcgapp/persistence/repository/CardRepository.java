package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Card;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardRepository {
    Card save(Card card);
    Card updateCard(Card card);
    List<Card> getCardsOfUser(long userId);
    List<Card> getCardsInDeckOfUser(long userId);
    void resetDeckOfUser(long userId);
    void configureDeckOfUser(List<Card> cardIds);
    Optional<Card> getCardByToken(UUID token);
}

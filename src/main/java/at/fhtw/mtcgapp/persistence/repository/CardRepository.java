package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Card;

import java.util.List;

public interface CardRepository {
    Card save(Card card);
    Card updateCard(Card card);
    List<Card> getCardsOfUser(long userId);
    List<Card> getCardsInDeckOfUser(long userId);
}

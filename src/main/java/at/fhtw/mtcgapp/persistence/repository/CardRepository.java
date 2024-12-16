package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Card;

public interface CardRepository {
    Card save(Card card);
}

package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Card;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor

@Slf4j
public class CardRepositoryImpl implements CardRepository {
    private final UnitOfWork unitOfWork;

    @Override
    public Card save(Card card) {
        return null;
    }
}

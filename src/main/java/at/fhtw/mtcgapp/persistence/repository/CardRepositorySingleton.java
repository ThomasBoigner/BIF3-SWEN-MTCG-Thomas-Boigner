package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.Getter;

@Getter
public enum CardRepositorySingleton {
    INSTANCE(new CardRepositoryImpl(new UnitOfWork()));

    private final CardRepository cardRepository;

    CardRepositorySingleton(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
}

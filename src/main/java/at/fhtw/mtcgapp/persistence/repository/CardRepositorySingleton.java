package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardRepositorySingleton {
    INSTANCE(new CardRepositoryImpl(new UnitOfWork()));

    private final CardRepository cardRepository;
}

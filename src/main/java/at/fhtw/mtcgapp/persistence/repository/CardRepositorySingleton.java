package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.persistence.UnitOfWorkSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardRepositorySingleton {
    INSTANCE(new CardRepositoryImpl(UnitOfWorkSingleton.INSTANCE.getUnitOfWork()));

    private final CardRepository cardRepository;
}

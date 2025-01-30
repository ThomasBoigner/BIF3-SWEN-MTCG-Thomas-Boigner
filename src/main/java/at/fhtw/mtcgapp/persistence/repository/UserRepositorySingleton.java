package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.persistence.UnitOfWork;
import at.fhtw.mtcgapp.persistence.UnitOfWorkSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRepositorySingleton {
    INSTANCE(new UserRepositoryImpl(UnitOfWorkSingleton.INSTANCE.getUnitOfWork(), CardRepositorySingleton.INSTANCE.getCardRepository()));

    private final UserRepository userRepository;
}

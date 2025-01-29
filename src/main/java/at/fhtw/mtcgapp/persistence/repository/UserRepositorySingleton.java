package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRepositorySingleton {
    INSTANCE(new UserRepositoryImpl(new UnitOfWork(), CardRepositorySingleton.INSTANCE.getCardRepository()));

    private final UserRepository userRepository;
}

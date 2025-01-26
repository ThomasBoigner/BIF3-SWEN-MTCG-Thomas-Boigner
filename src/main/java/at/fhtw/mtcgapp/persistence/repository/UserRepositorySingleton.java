package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.Getter;

@Getter
public enum UserRepositorySingleton {
    INSTANCE(new UserRepositoryImpl(new UnitOfWork(), CardRepositorySingleton.INSTANCE.getCardRepository()));

    private final UserRepository userRepository;

    UserRepositorySingleton(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

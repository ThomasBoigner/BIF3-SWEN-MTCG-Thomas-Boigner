package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.Getter;

@Getter
public enum SessionRepositorySingleton {
    INSTANCE(new SessionRepositoryImpl(new UnitOfWork(), CardRepositorySingleton.INSTANCE.getCardRepository()));

    private final SessionRepository sessionRepository;

    SessionRepositorySingleton(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
}

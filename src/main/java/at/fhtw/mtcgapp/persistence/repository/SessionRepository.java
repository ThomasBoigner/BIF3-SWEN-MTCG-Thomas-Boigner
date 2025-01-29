package at.fhtw.mtcgapp.persistence.repository;


import at.fhtw.mtcgapp.model.Session;
import at.fhtw.mtcgapp.model.User;

import java.util.Optional;

public interface SessionRepository {
    Optional<User> findUserByToken(String token);
    Optional<Session> findSessionByUserId(long id);
    Session save(Session session);
    void deleteByToken(String token);
}

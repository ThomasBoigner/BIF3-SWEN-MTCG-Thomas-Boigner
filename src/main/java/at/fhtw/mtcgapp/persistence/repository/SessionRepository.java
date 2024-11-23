package at.fhtw.mtcgapp.persistence.repository;


import at.fhtw.mtcgapp.model.Session;
import at.fhtw.mtcgapp.model.User;

import java.util.Optional;

public interface SessionRepository {
    Optional<User> findUserByToken(String token);
    Session save(Session session);
    boolean existsByToken(String token);
    void deleteByToken(String token);
}

package at.fhtw.mtcgapp.persistence.repository;


import at.fhtw.mtcgapp.model.Session;

public interface SessionRepository {
    Session save(Session session);
    void deleteByToken(String token);
}

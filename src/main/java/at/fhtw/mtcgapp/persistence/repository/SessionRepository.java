package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.User;

public interface SessionRepository {
    void login(User user);
    void logout(String token);
}

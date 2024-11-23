package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    User save(User user);
    boolean existsByUsername(String username);
}

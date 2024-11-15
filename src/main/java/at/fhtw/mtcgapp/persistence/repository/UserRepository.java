package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.User;

public interface UserRepository {
    User save(User user);
}

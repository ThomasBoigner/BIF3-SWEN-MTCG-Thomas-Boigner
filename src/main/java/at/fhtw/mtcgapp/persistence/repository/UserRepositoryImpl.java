package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.User;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public User save(User user) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean existsByUsername(String username) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}

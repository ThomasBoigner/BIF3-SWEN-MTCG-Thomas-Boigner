package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.DataAccessException;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor

@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final UnitOfWork unitOfWork;

    @Override
    public User save(User user) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean existsByUsername(String username) {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                EXISTS (
                    SELECT username
                    FROM user
                    WHERE username = ?
                )
                """)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getBoolean(1);

        } catch (SQLException e) {
            throw new DataAccessException("Exists nicht erfolgreich", e);
        }
    }
}

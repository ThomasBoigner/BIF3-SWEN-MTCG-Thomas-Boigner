package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.DataAccessException;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor

@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final UnitOfWork unitOfWork;

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        log.debug("Trying to save user {}", user);
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                INSERT INTO mtcg.user (token, username, password, bio, image, coins, elo, battles_fought)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """)) {
            preparedStatement.setObject(1, user.getToken());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getBio());
            preparedStatement.setString(5, user.getImage());
            preparedStatement.setInt(6, user.getCoins());
            preparedStatement.setInt(7, user.getElo());
            preparedStatement.setInt(8, user.getBattlesFought());

            preparedStatement.execute();
            unitOfWork.commitTransaction();
            return user;
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            log.error("Could not create user due to a sql exception");
            throw new DataAccessException("Insert into failed!", e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        log.debug("Trying to evaluate if user with username {} already exists", username);
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                Select EXISTS (
                    SELECT username
                    FROM mtcg.user
                    WHERE username = ?
                )
                """)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            boolean exists = resultSet.getBoolean(1);
            log.debug(exists ? "User with username {} does exist" : " User with username {} does not exist", username);
            return exists;

        } catch (SQLException e) {
            log.error("Could not evaluate if user exists due to a sql exception");
            throw new DataAccessException("Exists failed!", e);
        }
    }
}

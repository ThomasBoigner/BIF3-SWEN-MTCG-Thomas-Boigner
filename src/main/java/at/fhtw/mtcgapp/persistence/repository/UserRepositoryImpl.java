package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.DataAccessException;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor

@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final UnitOfWork unitOfWork;
    private final CardRepository cardRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        log.debug("Trying to find user with username: {}", username);
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT "user".id, "user".token, "user".username, "user".password, "user".bio, "user".image, "user".elo, "user".wins, "user".losses, "user".coins, "user".in_queue
                From mtcg.user
                where username = ?
                """)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            User user = User.builder()
                    .id(resultSet.getLong("id"))
                    .token(UUID.fromString(resultSet.getString("token")))
                    .username(resultSet.getString("username"))
                    .password(resultSet.getString("password"))
                    .bio(resultSet.getString("bio"))
                    .image(resultSet.getString("image"))
                    .elo(resultSet.getInt("elo"))
                    .wins(resultSet.getInt("wins"))
                    .losses(resultSet.getInt("losses"))
                    .coins(resultSet.getInt("coins"))
                    .inQueue(resultSet.getBoolean("in_queue"))
                    .deck(new ArrayList<>())
                    .stack(new ArrayList<>())
                    .trades(new ArrayList<>())
                    .build();

            return Optional.of(user);
        } catch (SQLException e) {
            log.error("Could not find user due to a sql exception");
            throw new DataAccessException("Select failed!", e);
        }
    }

    @Override
    public User save(User user) {
        log.debug("Trying to save user {}", user);
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                INSERT INTO mtcg.user (token, username, password, bio, image, coins, elo, wins, losses, in_queue)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id;
                """)) {
            preparedStatement.setObject(1, user.getToken());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getBio());
            preparedStatement.setString(5, user.getImage());
            preparedStatement.setInt(6, user.getCoins());
            preparedStatement.setInt(7, user.getElo());
            preparedStatement.setInt(8, user.getWins());
            preparedStatement.setInt(9, user.getLosses());
            preparedStatement.setBoolean(10, user.isInQueue());

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            user.setId(resultSet.getLong("id"));

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
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                Select EXISTS (
                    SELECT username
                    FROM mtcg.user
                    WHERE LOWER(username) = LOWER(?)
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

    @Override
    public User updateUser(User user) {
        log.debug("Trying to update user {}", user);
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                UPDATE mtcg.user
                SET token = ?, username = ?, password = ?, bio = ?, image = ?, coins = ?, elo = ?, wins = ?, losses = ?, in_queue = ?
                WHERE id = ?
                """)) {
            preparedStatement.setObject(1, user.getToken());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getBio());
            preparedStatement.setString(5, user.getImage());
            preparedStatement.setInt(6, user.getCoins());
            preparedStatement.setInt(7, user.getElo());
            preparedStatement.setInt(8, user.getWins());
            preparedStatement.setInt(9, user.getLosses());
            preparedStatement.setBoolean(10, user.isInQueue());
            preparedStatement.setLong(11, user.getId());

            preparedStatement.executeUpdate();

            unitOfWork.commitTransaction();

            return user;
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            log.error("Could not update user due to a sql exception");
            throw new DataAccessException("Update failed!", e);
        }
    }

    @Override
    public List<User> findAllUsers() {
        log.debug("Trying to find all users");
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT "user".id, "user".token, "user".username, "user".password, "user".bio, "user".image, "user".elo, "user".wins, "user".losses, "user".coins, "user".in_queue
                FROM mtcg.user
                ORDER BY "user".elo DESC
                """)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(
                        User.builder()
                        .id(resultSet.getLong("id"))
                        .token(UUID.fromString(resultSet.getString("token")))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .bio(resultSet.getString("bio"))
                        .image(resultSet.getString("image"))
                        .elo(resultSet.getInt("elo"))
                        .wins(resultSet.getInt("wins"))
                        .losses(resultSet.getInt("losses"))
                        .coins(resultSet.getInt("coins"))
                        .inQueue(resultSet.getBoolean("in_queue"))
                        .deck(new ArrayList<>())
                        .stack(new ArrayList<>())
                        .trades(new ArrayList<>())
                        .build()
                );
            }

            return users;
        } catch (SQLException e) {
            log.error("Could not get users due to a sql exception");
            throw new DataAccessException("Select failed!", e);
        }
    }

    @Override
    public Optional<User> getUserInQueue() {
        log.debug("Trying to find user that is in queue");
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT "user".id, "user".token, "user".username, "user".password, "user".bio, "user".image, "user".elo, "user".wins, "user".losses, "user".coins, "user".in_queue
                From mtcg.user
                where "user".in_queue = true
                """)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            User user = User.builder()
                    .id(resultSet.getLong("id"))
                    .token(UUID.fromString(resultSet.getString("token")))
                    .username(resultSet.getString("username"))
                    .password(resultSet.getString("password"))
                    .bio(resultSet.getString("bio"))
                    .image(resultSet.getString("image"))
                    .elo(resultSet.getInt("elo"))
                    .wins(resultSet.getInt("wins"))
                    .losses(resultSet.getInt("losses"))
                    .coins(resultSet.getInt("coins"))
                    .inQueue(resultSet.getBoolean("in_queue"))
                    .deck(new ArrayList<>())
                    .stack(new ArrayList<>())
                    .trades(new ArrayList<>())
                    .build();

            user.setStack(cardRepository.getCardsOfUser(user.getId()));
            user.getStack().forEach(card -> card.setUser(user));
            user.setDeck(cardRepository.getCardsInDeckOfUser(user.getId()));
            user.getDeck().forEach(card -> card.setUser(user));

            return Optional.of(user);
        } catch (SQLException e) {
            log.error("Could not find user due to a sql exception");
            throw new DataAccessException("Select failed!", e);
        }
    }
}

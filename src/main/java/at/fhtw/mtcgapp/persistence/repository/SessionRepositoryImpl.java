package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Session;
import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.DataAccessException;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor

@Slf4j
public class SessionRepositoryImpl implements SessionRepository {
    private final UnitOfWork unitOfWork;
    private final CardRepository cardRepository;

    @Override
    public Optional<User> findUserByToken(String token) {
        log.debug("Trying to find user with auth token {}", token);
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT "user".id, "user".token, "user".username, "user".password, "user".bio, "user".image, "user".elo, "user".wins, "user".losses, "user".coins, "user".in_queue
                FROM mtcg.session inner join mtcg.user on "session".fk_user_id = "user".id
                WHERE session.token = ?
                """)) {
            preparedStatement.setString(1, token);
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
            log.error("Could not find user of session due to a sql exception");
            throw new DataAccessException("Select failed!", e);
        }
    }

    @Override
    public Session save(Session session) {
        log.debug("Trying to create session {}", session);
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                INSERT INTO mtcg.session (token, fk_user_id)
                VALUES (?, ?)
                RETURNING id;
                """)) {
            preparedStatement.setString(1, session.getToken());
            preparedStatement.setLong(2, session.getUser().getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                session.setId(resultSet.getLong("id"));
            }

            unitOfWork.commitTransaction();
            return session;
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            log.error("Could not create session due to a sql exception");
            throw new DataAccessException("Insert into failed!", e);
        }
    }

    @Override
    public boolean existsByToken(String token) {
        log.debug("Trying to evaluate if session with token {} already exists", token);
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT EXISTS(
                    SELECT token
                    FROM mtcg.session
                    WHERE token = ?
                )
                """)) {
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean exists = false;
            if(resultSet.next()) {
                exists = resultSet.getBoolean(1);
            }
            log.debug(exists ? "Session with token {} does exist" : " Session with token {} does not exist", token);
            return exists;
        } catch (SQLException e) {
            log.error("Could not evaluate if session exists due to a sql exception");
            throw new DataAccessException("Exists failed!", e);
        }
    }

    @Override
    public void deleteByToken(String token) {
        log.debug("Trying to delete session with token {}", token);
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                DELETE FROM mtcg.session
                WHERE token = ?
                """)) {
            preparedStatement.setString(1, token);

            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            log.error("Could not delete session due to a sql exception");
            throw new DataAccessException("Delete session failed!", e);
        }
    }
}

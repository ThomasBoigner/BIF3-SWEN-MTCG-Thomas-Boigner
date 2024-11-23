package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Session;
import at.fhtw.mtcgapp.persistence.DataAccessException;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor

@Slf4j
public class SessionRepositoryImpl implements SessionRepository {
    private final UnitOfWork unitOfWork;

    @Override
    public Session save(Session session) {
        log.debug("Trying to create session {}", session);
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                INSERT INTO mtcg.session (token, fk_user_id)
                VALUES (?, ?)
                """)) {
            preparedStatement.setString(1, session.getToken());
            preparedStatement.setLong(2, session.getUser().getId());

            preparedStatement.execute();
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
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT EXISTS(
                    SELECT token
                    FROM mtcg.session
                    WHERE token = ?
                )
                """)) {
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            boolean exists = resultSet.getBoolean(1);
            log.debug(exists ? "Session with token {} does exist" : " Session with token {} does not exist", token);
            return exists;
        }
        catch (SQLException e) {
            log.error("Could not evaluate if session exists due to a sql exception");
            throw new DataAccessException("Exists failed!", e);
        }
    }

    @Override
    public void deleteByToken(String token) {

    }
}

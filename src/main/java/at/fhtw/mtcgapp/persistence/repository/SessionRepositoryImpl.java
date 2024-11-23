package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Session;
import at.fhtw.mtcgapp.persistence.DataAccessException;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
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
    public void deleteByToken(String token) {

    }
}

package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Package;
import at.fhtw.mtcgapp.persistence.DataAccessException;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor

@Slf4j
public class PackageRepositoryImpl implements PackageRepository {
    private final UnitOfWork unitOfWork;
    private final CardRepository cardRepository;

    @Override
    public Package save(Package pkg) {
        log.debug("Trying to save package {}", pkg);
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                INSERT INTO mtcg.user (token, username, password, bio, image, coins, elo, battles_fought)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """)) {

        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            log.error("Could not create package due to a sql exception");
            throw new DataAccessException("Insert into failed!", e);
        }
        return null;
    }
}

package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Package;
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
public class PackageRepositoryImpl implements PackageRepository {
    private final UnitOfWork unitOfWork;
    private final CardRepository cardRepository;

    @Override
    public Package save(Package pkg) {
        log.debug("Trying to save package {}", pkg);
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                INSERT INTO mtcg.package (token, price)
                VALUES (?, ?)
                RETURNING id;
                """)) {
            preparedStatement.setObject(1, pkg.getToken());
            preparedStatement.setInt(2, pkg.getPrice());

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            pkg.setId(resultSet.getLong("id"));

            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            log.error("Could not create package due to a sql exception");
            throw new DataAccessException("Insert into failed!", e);
        }

        pkg.getCards().forEach(cardRepository::save);
        return pkg;
    }

    @Override
    public Optional<Package> getPackage() {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public void deletePackage(long id) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }
}

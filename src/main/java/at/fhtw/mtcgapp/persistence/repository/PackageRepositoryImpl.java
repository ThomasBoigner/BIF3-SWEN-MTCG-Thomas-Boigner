package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.*;
import at.fhtw.mtcgapp.model.Package;
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
        log.debug("Trying to get a package");
        Package pkg = null;
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT package.id, package.token, package.price
                FROM mtcg.package
                ORDER BY RANDOM() LIMIT 1
                """)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                pkg = Package.builder()
                        .id(resultSet.getLong("id"))
                        .token(UUID.fromString(resultSet.getString("token")))
                        .price(resultSet.getInt("price"))
                        .cards(new ArrayList<>())
                        .build();
            }

        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            log.error("Could not get package due to a sql exception");
            throw new DataAccessException("Select failed!", e);
        }

        if (pkg == null) {
            return Optional.empty();
        }

        List<MonsterCard> monsterCards = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT monster_card.id, monster_card.token, monster_card.name, monster_card.damage, monster_card.damage_type, monster_card.fk_user_id, monster_card.fk_package_id, monster_card.defence
                FROM mtcg.monster_card
                WHERE fk_package_id = ?
                """)) {
            preparedStatement.setLong(1, pkg.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                monsterCards.add(MonsterCard.builder()
                        .id(resultSet.getLong("id"))
                        .token(UUID.fromString(resultSet.getString("token")))
                        .name(resultSet.getString("name"))
                        .damage(resultSet.getDouble("damage"))
                        .damageType(DamageType.forDBValue(resultSet.getString("damage_type")))
                        .user(null)
                        .cardPackage(pkg)
                        .defence(resultSet.getDouble("defence"))
                        .build());
            }
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            log.error("Could not get monster cards of package due to a sql exception");
            throw new DataAccessException("Select failed!", e);
        }

        List<SpellCard> spellCards = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT spell_card.id, spell_card.token, spell_card.name, spell_card.damage, spell_card.damage_type, spell_card.fk_user_id, spell_card.fk_package_id, spell_card.critical_hit_chance
                FROM mtcg.spell_card
                WHERE fk_package_id = ?
                """)) {
            preparedStatement.setLong(1, pkg.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                spellCards.add(SpellCard.builder()
                        .id(resultSet.getLong("id"))
                        .token(UUID.fromString(resultSet.getString("token")))
                        .name(resultSet.getString("name"))
                        .damage(resultSet.getDouble("damage"))
                        .damageType(DamageType.forDBValue(resultSet.getString("damage_type")))
                        .user(null)
                        .cardPackage(pkg)
                        .criticalHitChance(resultSet.getDouble("critical_hit_chance"))
                        .build());
            }
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            log.error("Could not get spell cards of package due to a sql exception");
            throw new DataAccessException("Select failed!", e);
        }

        log.debug("Spellcards: {}", spellCards);
        log.debug("MonsterCards: {}", monsterCards);
        pkg.getCards().addAll(monsterCards);
        pkg.getCards().addAll(spellCards);

        return Optional.of(pkg);
    }

    @Override
    public void deletePackage(long id) {
        log.debug("Trying to delete package with id {}", id);
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                DELETE
                FROM mtcg.package
                WHERE id = ?
                """)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            log.error("Could not delete package due to a sql exception");
            throw new DataAccessException("Delete failed!", e);
        }
    }
}

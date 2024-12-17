package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Card;
import at.fhtw.mtcgapp.model.MonsterCard;
import at.fhtw.mtcgapp.model.SpellCard;
import at.fhtw.mtcgapp.persistence.DataAccessException;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor

@Slf4j
public class CardRepositoryImpl implements CardRepository {
    private final UnitOfWork unitOfWork;

    @Override
    public Card save(Card card) {
        log.debug("Trying to save card {}", card);
        if (card instanceof MonsterCard) {
            try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    INSERT INTO mtcg.monster_card (token, name, damage, damage_type, fk_user_id, fk_package_id, defence)
                    VALUES (?, ?, ?, ?::mtcg.damage_type, ?, ?, ?)
                    RETURNING id;
                    """)) {
                preparedStatement.setObject(1, card.getToken());
                preparedStatement.setString(2, card.getName());
                preparedStatement.setDouble(3, card.getDamage());
                preparedStatement.setString(4, card.getDamageType().getDbValue());
                preparedStatement.setObject(5, (card.getUser() != null) ? card.getUser().getId() : null);
                preparedStatement.setObject(6, (card.getCardPackage() != null) ? card.getCardPackage().getId() : null);
                preparedStatement.setDouble(7, ((MonsterCard) card).getDefence());

                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                card.setId(resultSet.getLong("id"));

                unitOfWork.commitTransaction();

                return card;
            } catch (SQLException e) {
                unitOfWork.rollbackTransaction();
                log.error("Could not create monster card due to a sql exception");
                throw new DataAccessException("Insert into failed!", e);
            }
        } else if (card instanceof SpellCard) {
            try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    INSERT INTO mtcg.spell_card (token, name, damage, damage_type, fk_user_id, fk_package_id, critical_hit_chance)
                    VALUES (?, ?, ?, ?::mtcg.damage_type, ?, ?, ?)
                    RETURNING id;
                    """)) {
                preparedStatement.setObject(1, card.getToken());
                preparedStatement.setString(2, card.getName());
                preparedStatement.setDouble(3, card.getDamage());
                preparedStatement.setString(4, card.getDamageType().getDbValue());
                preparedStatement.setObject(5, (card.getUser() != null) ? card.getUser().getId() : null);
                preparedStatement.setObject(6, (card.getCardPackage() != null) ? card.getCardPackage().getId() : null);
                preparedStatement.setDouble(7, ((SpellCard) card).getCriticalHitChance());

                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                card.setId(resultSet.getLong("id"));

                unitOfWork.commitTransaction();

                return card;
            } catch (SQLException e) {
                unitOfWork.rollbackTransaction();
                log.error("Could not create spell card due to a sql exception");
                throw new DataAccessException("Insert into failed!", e);
            }
        }
        throw new IllegalArgumentException("Card type not supported!");
    }

    @Override
    public Card updateCard(Card card) {
        log.debug("Trying to update card {}", card);
        if (card instanceof MonsterCard) {
            try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    UPDATE mtcg.monster_card
                    SET token = ?, name = ?, damage = ?, damage_type = ?::mtcg.damage_type, fk_user_id = ?, fk_package_id = ?, defence = ?
                    WHERE id = ?;
                    """)) {
                preparedStatement.setObject(1, card.getToken());
                preparedStatement.setString(2, card.getName());
                preparedStatement.setDouble(3, card.getDamage());
                preparedStatement.setString(4, card.getDamageType().getDbValue());
                preparedStatement.setObject(5, (card.getUser() != null) ? card.getUser().getId() : null);
                preparedStatement.setObject(6, (card.getCardPackage() != null) ? card.getCardPackage().getId() : null);
                preparedStatement.setDouble(7, ((MonsterCard) card).getDefence());
                preparedStatement.setLong(8, card.getId());

                preparedStatement.executeUpdate();
                unitOfWork.commitTransaction();
                return card;
            } catch (SQLException e) {
                unitOfWork.rollbackTransaction();
                log.error("Could not update monster card due to a sql exception");
                throw new DataAccessException("Update failed!", e);
            }
        } else if (card instanceof SpellCard) {
            try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    UPDATE mtcg.spell_card
                    SET token = ?, name = ?, damage = ?, damage_type = ?::mtcg.damage_type, fk_user_id = ?, fk_package_id = ?, critical_hit_chance = ?
                    WHERE id = ?;
                    """)) {
                preparedStatement.setObject(1, card.getToken());
                preparedStatement.setString(2, card.getName());
                preparedStatement.setDouble(3, card.getDamage());
                preparedStatement.setString(4, card.getDamageType().getDbValue());
                preparedStatement.setObject(5, (card.getUser() != null) ? card.getUser().getId() : null);
                preparedStatement.setObject(6, (card.getCardPackage() != null) ? card.getCardPackage().getId() : null);
                preparedStatement.setDouble(7, ((SpellCard) card).getCriticalHitChance());
                preparedStatement.setLong(8, card.getId());

                preparedStatement.executeUpdate();
                unitOfWork.commitTransaction();
                return card;
            } catch (SQLException e) {
                unitOfWork.rollbackTransaction();
                log.error("Could not update spell card due to a sql exception");
                throw new DataAccessException("Update failed!", e);
            }
        }
        throw new IllegalArgumentException("Card type not supported!");
    }
}

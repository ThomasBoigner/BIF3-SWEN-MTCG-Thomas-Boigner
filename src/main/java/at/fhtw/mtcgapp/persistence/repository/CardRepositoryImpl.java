package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Card;
import at.fhtw.mtcgapp.model.DamageType;
import at.fhtw.mtcgapp.model.MonsterCard;
import at.fhtw.mtcgapp.model.SpellCard;
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
import java.util.stream.Stream;

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
                    INSERT INTO mtcg.spell_card (token, name, damage, damage_type, fk_user_id, fk_package_id, critical_hit_multiplier)
                    VALUES (?, ?, ?, ?::mtcg.damage_type, ?, ?, ?)
                    RETURNING id;
                    """)) {
                preparedStatement.setObject(1, card.getToken());
                preparedStatement.setString(2, card.getName());
                preparedStatement.setDouble(3, card.getDamage());
                preparedStatement.setString(4, card.getDamageType().getDbValue());
                preparedStatement.setObject(5, (card.getUser() != null) ? card.getUser().getId() : null);
                preparedStatement.setObject(6, (card.getCardPackage() != null) ? card.getCardPackage().getId() : null);
                preparedStatement.setDouble(7, ((SpellCard) card).getCriticalHitMultiplier());

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
                    SET token = ?, name = ?, damage = ?, damage_type = ?::mtcg.damage_type, fk_user_id = ?, fk_package_id = ?, critical_hit_multiplier = ?
                    WHERE id = ?;
                    """)) {
                preparedStatement.setObject(1, card.getToken());
                preparedStatement.setString(2, card.getName());
                preparedStatement.setDouble(3, card.getDamage());
                preparedStatement.setString(4, card.getDamageType().getDbValue());
                preparedStatement.setObject(5, (card.getUser() != null) ? card.getUser().getId() : null);
                preparedStatement.setObject(6, (card.getCardPackage() != null) ? card.getCardPackage().getId() : null);
                preparedStatement.setDouble(7, ((SpellCard) card).getCriticalHitMultiplier());
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

    @Override
    public List<Card> getCardsOfUser(long userId) {
        log.debug("Trying to get cards of user {}", userId);

        List<MonsterCard> monsterCards = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT monster_card.id, monster_card.token, monster_card.name, monster_card.damage, monster_card.damage_type, monster_card.fk_user_id, monster_card.fk_package_id, monster_card.defence
                FROM mtcg.monster_card
                WHERE fk_user_id = ? AND in_deck = false
                """)) {
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                monsterCards.add(MonsterCard.builder()
                        .id(resultSet.getLong("id"))
                        .token(UUID.fromString(resultSet.getString("token")))
                        .name(resultSet.getString("name"))
                        .damage(resultSet.getDouble("damage"))
                        .damageType(DamageType.forDBValue(resultSet.getString("damage_type")))
                        .user(null)
                        .cardPackage(null)
                        .defence(resultSet.getDouble("defence"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Could not get monster card due to a sql exception");
            throw new DataAccessException("Select failed!", e);
        }

        List<SpellCard> spellCards = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT spell_card.id, spell_card.token, spell_card.name, spell_card.damage, spell_card.damage_type, spell_card.fk_user_id, spell_card.fk_package_id, spell_card.critical_hit_multiplier
                FROM mtcg.spell_card
                WHERE fk_user_id = ? AND in_deck = false
                """)) {
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                spellCards.add(SpellCard.builder()
                        .id(resultSet.getLong("id"))
                        .token(UUID.fromString(resultSet.getString("token")))
                        .name(resultSet.getString("name"))
                        .damage(resultSet.getDouble("damage"))
                        .damageType(DamageType.forDBValue(resultSet.getString("damage_type")))
                        .user(null)
                        .cardPackage(null)
                        .criticalHitMultiplier(resultSet.getDouble("critical_hit_multiplier"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Could not get spell card due to a sql exception");
            throw new DataAccessException("Select failed!", e);
        }

        return new ArrayList<>(Stream.concat(monsterCards.stream(), spellCards.stream()).toList());
    }

    @Override
    public List<Card> getCardsInDeckOfUser(long userId) {
        log.debug("Trying to get cards in deck of user {}", userId);

        List<MonsterCard> monsterCards = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT monster_card.id, monster_card.token, monster_card.name, monster_card.damage, monster_card.damage_type, monster_card.fk_user_id, monster_card.fk_package_id, monster_card.defence
                FROM mtcg.monster_card
                WHERE fk_user_id = ? AND in_deck = true
                """)) {
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                monsterCards.add(MonsterCard.builder()
                        .id(resultSet.getLong("id"))
                        .token(UUID.fromString(resultSet.getString("token")))
                        .name(resultSet.getString("name"))
                        .damage(resultSet.getDouble("damage"))
                        .damageType(DamageType.forDBValue(resultSet.getString("damage_type")))
                        .user(null)
                        .cardPackage(null)
                        .defence(resultSet.getDouble("defence"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Could not get monster card due to a sql exception");
            throw new DataAccessException("Select failed!", e);
        }

        List<SpellCard> spellCards = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT spell_card.id, spell_card.token, spell_card.name, spell_card.damage, spell_card.damage_type, spell_card.fk_user_id, spell_card.fk_package_id, spell_card.critical_hit_multiplier
                FROM mtcg.spell_card
                WHERE fk_user_id = ? AND in_deck = true
                """)) {
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                spellCards.add(SpellCard.builder()
                        .id(resultSet.getLong("id"))
                        .token(UUID.fromString(resultSet.getString("token")))
                        .name(resultSet.getString("name"))
                        .damage(resultSet.getDouble("damage"))
                        .damageType(DamageType.forDBValue(resultSet.getString("damage_type")))
                        .user(null)
                        .cardPackage(null)
                        .criticalHitMultiplier(resultSet.getDouble("critical_hit_multiplier"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Could not get spell card due to a sql exception");
            throw new DataAccessException("Select failed!", e);
        }

        return new ArrayList<>(Stream.concat(monsterCards.stream(), spellCards.stream()).toList());
    }

    @Override
    public void resetDeckOfUser(long userId) {
        log.debug("Trying to reset deck of user {}", userId);
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                UPDATE mtcg.card
                SET in_deck = false
                WHERE fk_user_id = ?;
                """)) {

            preparedStatement.setLong(1, userId);

            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            log.error("Could not remove card from deck due to a sql exception");
            throw new DataAccessException("Update failed!", e);
        }
    }

    @Override
    public void configureDeckOfUser(List<Card> cards) {
        log.debug("Trying to add cards {} to deck", cards);
        for(Card card : cards) {
            try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                UPDATE mtcg.card
                SET in_deck = true
                WHERE id = ?;
                """)) {

                preparedStatement.setLong(1, card.getId());

                preparedStatement.executeUpdate();
                unitOfWork.commitTransaction();
            } catch (SQLException e) {
                log.error("Could not add card to deck due to a sql exception");
                throw new DataAccessException("Update failed!", e);
            }
        }
    }

    @Override
    public Optional<Card> getCardByToken(UUID token) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

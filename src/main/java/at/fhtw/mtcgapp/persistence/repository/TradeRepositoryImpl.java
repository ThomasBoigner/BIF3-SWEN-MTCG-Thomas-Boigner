package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.*;
import at.fhtw.mtcgapp.persistence.DataAccessException;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor

@Slf4j
public class TradeRepositoryImpl implements TradeRepository {
    private final UnitOfWork unitOfWork;

    @Override
    public List<Trade> getTrades() {
        log.debug("Trying to get all trades");

        List<Trade> trades = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                SELECT trade.id as trade_id, trade.token as trade_token, trade.minimum_damage as trade_minimum_damage, trade.type as trade_type, monster_card.id as monster_card_id, monster_card.token as monster_card_token, monster_card.name as monster_card_name, monster_card.damage as monster_card_damage, monster_card.damage_type as monster_card_damage_type, monster_card.defence as monster_card_defence, spell_card.id as spell_card_id, spell_card.token as spell_card_token, spell_card.name as spell_card_name, spell_card.damage as spell_card_damage, spell_card.damage_type as spell_card_damage_type, spell_card.critical_hit_multiplier as spell_card_critical_hit_multiplier, "user".id as user_id, "user".token as user_token, "user".username as user_username, "user".password as user_password, "user".bio as user_bio, "user".image as user_image, "user".coins as user_coins, "user".elo as user_elo, "user".wins as user_wins, "user".losses as user_losses, "user".in_queue as user_in_queue
                FROM mtcg.trade
                left join mtcg.monster_card on monster_card.id = trade.fk_monster_card_id
                left join mtcg.spell_card on spell_card.id = trade.fk_spell_card_id
                inner join mtcg.user on "user".id = trade.fk_user_id""")) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Card cardToTrade = null;
                if (resultSet.getObject("monster_card_id") != null) {
                    cardToTrade = MonsterCard.builder()
                            .id(resultSet.getLong("monster_card_id"))
                            .token(UUID.fromString(resultSet.getString("monster_card_token")))
                            .name(resultSet.getString("monster_card_name"))
                            .damage(resultSet.getDouble("monster_card_damage"))
                            .damageType(DamageType.forDBValue(resultSet.getString("monster_card_damage_type")))
                            .defence(resultSet.getDouble("monster_card_defence"))
                            .build();
                }
                if (resultSet.getObject("spell_card_id") != null) {
                    cardToTrade = SpellCard.builder()
                            .id(resultSet.getLong("spell_card_id"))
                            .token(UUID.fromString(resultSet.getString("spell_card_token")))
                            .name(resultSet.getString("spell_card_name"))
                            .damage(resultSet.getDouble("spell_card_damage"))
                            .damageType(DamageType.forDBValue(resultSet.getString("spell_card_damage_type")))
                            .criticalHitMultiplier(resultSet.getDouble("spell_card_critical_hit_multiplier"))
                            .build();
                }

                trades.add(Trade.builder()
                        .id(resultSet.getLong("trade_id"))
                        .token(UUID.fromString(resultSet.getString("trade_token")))
                        .minimumDamage(resultSet.getDouble("trade_minimum_damage"))
                        .type(CardType.forDBValue(resultSet.getString("trade_type")))
                        .trader(User.builder()
                                .id(resultSet.getLong("user_id"))
                                .username(resultSet.getString("user_username"))
                                .password(resultSet.getString("user_password"))
                                .bio(resultSet.getString("user_bio"))
                                .image(resultSet.getString("user_image"))
                                .coins(resultSet.getInt("user_coins"))
                                .elo(resultSet.getInt("user_elo"))
                                .wins(resultSet.getInt("user_wins"))
                                .losses(resultSet.getInt("user_losses"))
                                .inQueue(resultSet.getBoolean("user_in_queue"))
                                .build())
                        .cardToTrade(cardToTrade)
                        .build());
            }
        } catch (SQLException e) {
            log.error("Could not get trade due to a sql exception");
            throw new DataAccessException("Select failed!", e);
        }
        return trades;
    }

    @Override
    public Trade save(Trade trade) {
        log.debug("Trying to save trade {}", trade);

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                INSERT INTO mtcg.trade (token, minimum_damage, type, fk_monster_card_id, fk_spell_card_id, fk_user_id)
                VALUES (?, ?, ?::mtcg.card_type, ?, ?, ?)
                RETURNING id;
                """)) {
            preparedStatement.setObject(1, trade.getToken());
            preparedStatement.setDouble(2, trade.getMinimumDamage());
            preparedStatement.setString(3, trade.getType().getDbValue());
            preparedStatement.setObject(4, (trade.getCardToTrade() != null && trade.getCardToTrade() instanceof MonsterCard) ? trade.getCardToTrade().getId() : null);
            preparedStatement.setObject(5, (trade.getCardToTrade() != null && trade.getCardToTrade() instanceof SpellCard) ? trade.getCardToTrade().getId() : null);
            preparedStatement.setObject(6, (trade.getTrader() != null) ? trade.getTrader().getId() : null);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            trade.setId(resultSet.getLong("id"));

            unitOfWork.commitTransaction();

            return trade;
        } catch (SQLException e) {
            log.error("Could not create trade due to a sql exception");
            throw new DataAccessException("Insert into failed!", e);
        }
    }
}

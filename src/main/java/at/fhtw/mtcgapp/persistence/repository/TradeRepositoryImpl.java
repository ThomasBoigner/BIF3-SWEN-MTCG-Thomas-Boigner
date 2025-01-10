package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.CardType;
import at.fhtw.mtcgapp.model.MonsterCard;
import at.fhtw.mtcgapp.model.SpellCard;
import at.fhtw.mtcgapp.model.Trade;
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
                SELECT trade.id, trade.token, trade."minimumDamage", trade.type, card.id, card.token, card.name, card.damage, card.damage_type
                FROM mtcg.trade inner join mtcg.card on trade.fk_card_id = card.id
                """)) {
           ResultSet resultSet = preparedStatement.executeQuery();

           while (resultSet.next()) {
               Trade trade = Trade.builder()
                       .id(resultSet.getLong("trade.id"))
                       .token(UUID.fromString(resultSet.getString("trade.token")))
                       .minimumDamage(resultSet.getDouble("trade.minimumDamage"))
                       .type(CardType.forDBValue(resultSet.getString("trade.type")))
                       .build();
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
                INSERT INTO mtcg.trade (token, "minimumDamage", type, fk_monster_card_id, fk_spell_card_id, fk_user_id)
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

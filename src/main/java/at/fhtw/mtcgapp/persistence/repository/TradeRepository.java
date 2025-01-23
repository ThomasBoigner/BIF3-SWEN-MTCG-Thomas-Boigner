package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Trade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TradeRepository {
    List<Trade> getTrades();
    Trade save(Trade trade);
    Optional<Trade> getTradeByToken(UUID token);
    void deleteTradeById(long id);
}

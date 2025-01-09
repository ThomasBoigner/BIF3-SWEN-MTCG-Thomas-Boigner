package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Trade;

import java.util.List;

public interface TradeRepository {
    List<Trade> getTrades();
    Trade save(Trade trade);
}

package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.Getter;

@Getter
public enum TradeRepositorySingleton {
    INSTANCE(new TradeRepositoryImpl(new UnitOfWork()));

    private final TradeRepository tradeRepository;

    TradeRepositorySingleton(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }
}

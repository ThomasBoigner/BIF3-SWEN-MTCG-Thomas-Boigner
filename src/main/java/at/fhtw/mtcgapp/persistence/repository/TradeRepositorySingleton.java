package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeRepositorySingleton {
    INSTANCE(new TradeRepositoryImpl(new UnitOfWork()));

    private final TradeRepository tradeRepository;

}

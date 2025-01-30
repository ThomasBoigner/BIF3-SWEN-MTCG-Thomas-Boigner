package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.persistence.UnitOfWork;
import at.fhtw.mtcgapp.persistence.UnitOfWorkSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeRepositorySingleton {
    INSTANCE(new TradeRepositoryImpl(UnitOfWorkSingleton.INSTANCE.getUnitOfWork()));

    private final TradeRepository tradeRepository;
}

package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.persistence.repository.CardRepositorySingleton;
import at.fhtw.mtcgapp.persistence.repository.TradeRepositorySingleton;
import at.fhtw.mtcgapp.util.ValidatorSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeServiceSingleton {
    INSTANCE(new TradeServiceImpl(
            TradeRepositorySingleton.INSTANCE.getTradeRepository(),
            AuthenticationServiceSingleton.INSTANCE.getAuthenticationService(),
            CardRepositorySingleton.INSTANCE.getCardRepository(),
            ValidatorSingleton.INSTANCE.getValidator()));

    private final TradeService tradeService;
}
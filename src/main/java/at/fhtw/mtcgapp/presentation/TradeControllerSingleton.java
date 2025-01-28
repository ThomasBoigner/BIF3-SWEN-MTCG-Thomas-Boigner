package at.fhtw.mtcgapp.presentation;

import at.fhtw.mtcgapp.service.TradeServiceSingleton;
import at.fhtw.mtcgapp.util.ObjectMapperSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeControllerSingleton {
    INSTANCE(new TradeController(
            TradeServiceSingleton.INSTANCE.getTradeService(),
            ObjectMapperSingleton.INSTANCE.getObjectMapper()));

    private final TradeController tradeController;
}

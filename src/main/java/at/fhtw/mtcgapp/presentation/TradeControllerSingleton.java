package at.fhtw.mtcgapp.presentation;

import at.fhtw.mtcgapp.service.TradeServiceSingleton;
import at.fhtw.mtcgapp.util.ObjectMapperSingleton;
import lombok.Getter;

@Getter
public enum TradeControllerSingleton {
    INSTANCE(new TradeController(
            TradeServiceSingleton.INSTANCE.getTradeService(),
            ObjectMapperSingleton.INSTANCE.getObjectMapper()));

    private final TradeController tradeController;

    TradeControllerSingleton(TradeController tradeController) {
        this.tradeController = tradeController;
    }
}

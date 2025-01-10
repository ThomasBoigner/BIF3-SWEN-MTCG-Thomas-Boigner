package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.service.command.CreateTradeCommand;
import at.fhtw.mtcgapp.service.dto.TradeDto;

import java.util.List;
import java.util.UUID;

public interface TradeService {
    List<TradeDto> getTrades(String authToken);
    TradeDto createTrade(String authToken, CreateTradeCommand command);
    void acceptTrade(String authToken, UUID tradeId, UUID cardID);
    void deleteTrade(String authToken, UUID tradeId);
}

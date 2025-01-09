package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.service.command.CreateTradeCommand;
import at.fhtw.mtcgapp.service.dto.TradeDto;

import java.util.List;

public interface TradeService {
    List<TradeDto> getTrades(String authToken);
    TradeDto createTrade(String authToken, CreateTradeCommand command);
}

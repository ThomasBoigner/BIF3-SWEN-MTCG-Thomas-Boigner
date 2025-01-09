package at.fhtw.mtcgapp.service.dto;

import at.fhtw.mtcgapp.model.Trade;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Builder
public record TradeDto(UUID id, CardDto cardToTrade, String type, double minimumDamage) {
    public TradeDto(Trade trade) {
        this(trade.getToken(), new CardDto(trade.getCardToTrade()), trade.getType().getDbValue(), trade.getMinimumDamage());
        log.debug("Created TradeDto {}", this);
    }
}

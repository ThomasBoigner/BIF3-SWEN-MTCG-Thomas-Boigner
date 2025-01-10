package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.Card;
import at.fhtw.mtcgapp.model.CardType;
import at.fhtw.mtcgapp.model.Trade;
import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.repository.CardRepository;
import at.fhtw.mtcgapp.persistence.repository.TradeRepository;
import at.fhtw.mtcgapp.service.command.CreateTradeCommand;
import at.fhtw.mtcgapp.service.dto.TradeDto;
import at.fhtw.mtcgapp.service.exception.TradeValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor

@Slf4j
public class TradeServiceImpl implements TradeService {
    private final TradeRepository tradeRepository;
    private final AuthenticationService authenticationService;
    private final CardRepository cardRepository;
    private final Validator validator;

    @Override
    public List<TradeDto> getTrades(String authToken) {
        log.debug("Trying to get all trades with auth token {}", authToken);

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);
        List<TradeDto> trades = tradeRepository.getTrades().stream().map(TradeDto::new).toList();

        log.info("User {} retrieved all({}) trades", user.getUsername(), trades.size());
        return trades;
    }

    @Override
    public TradeDto createTrade(String authToken, CreateTradeCommand command) {
        log.debug("Trying to create trade with command {}", command);

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);

        Set<ConstraintViolation<CreateTradeCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            log.warn("Input validation for trade failed!");
            throw new ConstraintViolationException(violations);
        }

        Card cardToTrade = user.getStack().stream().filter(card -> card.getToken().equals(command.cardToTrade())).findFirst().orElseThrow(TradeValidationException::cardNotInStackOfUser);
        cardToTrade.setUser(null);

        Trade trade = Trade.builder()
                .token((command.id() != null) ? command.id() : UUID.randomUUID())
                .minimumDamage(command.minimumDamage())
                .type(CardType.forDBValue(command.type()))
                .cardToTrade(cardToTrade)
                .trader(user)
                .build();
        log.trace("Mapped command {} to trade object {}", command, trade);

        cardRepository.updateCard(cardToTrade);

        log.info("User {} created trade {}", user.getUsername(), trade);
        return new TradeDto(tradeRepository.save(trade));
    }

    @Override
    public void deleteTrade(String authToken, UUID tradeId) {
        log.debug("Trying to delete trade with token {}", tradeId);

        User user = authenticationService.getCurrentlyLoggedInUser(authToken);

        Trade trade = tradeRepository.getTradeByToken(tradeId).orElseThrow(() -> TradeValidationException.tradeNotFound(tradeId));

        log.trace("Comparing user {} with trade user {}", user, trade.getTrader());
        if (!user.equals(trade.getTrader())) {
            throw TradeValidationException.notYourTrade();
        }

        trade.getCardToTrade().setUser(user);

        cardRepository.updateCard(trade.getCardToTrade());
        tradeRepository.deleteTradeById(trade.getId());
        log.info("Deleted trade {}", trade);
    }
}

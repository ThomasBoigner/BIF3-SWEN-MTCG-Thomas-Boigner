package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.service.dto.CardDto;

import java.util.List;
import java.util.UUID;

public interface CardService {
    List<CardDto> getCardsOfUser(String authToken);
    List<CardDto> getDeckOfUser(String authToken);
    void configureDeck(String authToken, List<UUID> cardIds);
}

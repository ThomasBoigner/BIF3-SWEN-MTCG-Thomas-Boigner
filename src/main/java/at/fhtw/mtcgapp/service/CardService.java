package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.service.dto.CardDto;

import java.util.List;

public interface CardService {
    List<CardDto> getCardsOfUser(String authToken);
    List<CardDto> getDeckOfUser(String authToken);
}

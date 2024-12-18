package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.persistence.repository.CardRepository;
import at.fhtw.mtcgapp.service.dto.CardDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor

@Slf4j
public class CardService {
    private final CardRepository cardRepository;

    public List<CardDto> getCardsOfUser(String authToken) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

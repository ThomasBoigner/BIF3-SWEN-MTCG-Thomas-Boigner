package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.Card;

public interface BattleService {
    void battleUser(String authToken);
    double battleRound(Card cardPlayer1, Card cardPlayer2);
}

package at.fhtw.mtcgapp.presentation;

import at.fhtw.mtcgapp.service.CardServiceSingleton;
import at.fhtw.mtcgapp.util.ObjectMapperSingleton;
import lombok.Getter;

@Getter
public enum DeckControllerSingleton {
    INSTANCE(new DeckController(
            CardServiceSingleton.INSTANCE.getCardService(),
            ObjectMapperSingleton.INSTANCE.getObjectMapper()));

    private final DeckController deckController;

    DeckControllerSingleton(DeckController deckController) {
        this.deckController = deckController;
    }
}

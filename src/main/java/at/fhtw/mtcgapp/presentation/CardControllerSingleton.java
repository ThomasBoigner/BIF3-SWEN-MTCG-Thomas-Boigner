package at.fhtw.mtcgapp.presentation;

import at.fhtw.mtcgapp.service.CardServiceSingleton;
import at.fhtw.mtcgapp.util.ObjectMapperSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardControllerSingleton {
    INSTANCE(new CardController(
            CardServiceSingleton.INSTANCE.getCardService(),
            ObjectMapperSingleton.INSTANCE.getObjectMapper()));

    private final CardController cardController;
}

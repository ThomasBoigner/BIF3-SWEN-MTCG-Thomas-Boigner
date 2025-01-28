package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.persistence.repository.CardRepositorySingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardServiceSingleton {
    INSTANCE(new CardServiceImpl(
            CardRepositorySingleton.INSTANCE.getCardRepository(),
            AuthenticationServiceSingleton.INSTANCE.getAuthenticationService()));

    private final CardService cardService;
}

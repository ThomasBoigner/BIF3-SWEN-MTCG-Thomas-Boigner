package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.persistence.repository.UserRepositorySingleton;
import lombok.Getter;

@Getter
public enum BattleServiceSingleton {
    INSTANCE(new BattleServiceImpl(
            AuthenticationServiceSingleton.INSTANCE.getAuthenticationService(),
            UserRepositorySingleton.INSTANCE.getUserRepository()));

    private final BattleService battleService;

    BattleServiceSingleton(BattleService battleService) {
        this.battleService = battleService;
    }
}

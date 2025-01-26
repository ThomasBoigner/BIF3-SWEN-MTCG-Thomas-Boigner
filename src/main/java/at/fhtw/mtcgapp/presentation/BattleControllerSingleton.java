package at.fhtw.mtcgapp.presentation;

import at.fhtw.mtcgapp.service.BattleServiceSingleton;
import lombok.Getter;

@Getter
public enum BattleControllerSingleton {
    INSTANCE(new BattleController(BattleServiceSingleton.INSTANCE.getBattleService()));

    private final BattleController battleController;

    BattleControllerSingleton(BattleController battleController) {
        this.battleController = battleController;
    }
}

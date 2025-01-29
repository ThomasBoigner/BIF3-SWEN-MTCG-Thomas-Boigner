package at.fhtw;

import at.fhtw.httpserver.server.Server;
import at.fhtw.httpserver.utils.Router;
import at.fhtw.mtcgapp.presentation.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Server server = new Server(10001, configureRouter());
        try {
            server.start();
        } catch (IOException e) {
            log.error("Failed to start server", e);
        }
    }

    private static Router configureRouter(){
        Router router = new Router();
        router.addService("/users", UserControllerSingleton.INSTANCE.getUserController());
        router.addService("/sessions", AuthenticationControllerSingleton.INSTANCE.getAuthenticationController());
        router.addService("/packages", PackageControllerSingleton.INSTANCE.getPackageController());
        router.addService("/transactions", TransactionsControllerSingleton.INSTANCE.getTransactionsController());
        router.addService("/cards", CardControllerSingleton.INSTANCE.getCardController());
        router.addService("/deck", DeckControllerSingleton.INSTANCE.getDeckController());
        router.addService("/stats", StatsControllerSingleton.INSTANCE.getStatsController());
        router.addService("/scoreboard", ScoreBoardControllerSingleton.INSTANCE.getScoreBoardController());
        router.addService("/battles", BattleControllerSingleton.INSTANCE.getBattleController());
        router.addService("/tradings", TradeControllerSingleton.INSTANCE.getTradeController());

        return router;
    }
}
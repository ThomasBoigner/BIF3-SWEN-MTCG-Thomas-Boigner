package at.fhtw;

import at.fhtw.httpserver.server.Server;
import at.fhtw.httpserver.utils.Router;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import at.fhtw.mtcgapp.persistence.repository.*;
import at.fhtw.mtcgapp.presentation.*;
import at.fhtw.mtcgapp.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Base64;

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
        ObjectMapper objectMapper = new ObjectMapper();
        UnitOfWork unitOfWork = new UnitOfWork();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Base64.Encoder encoder = Base64.getEncoder();

        CardRepository cardRepository = new CardRepositoryImpl(unitOfWork);
        UserRepository userRepository = new UserRepositoryImpl(unitOfWork, cardRepository);
        SessionRepository sessionRepository = new SessionRepositoryImpl(unitOfWork, cardRepository);
        PackageRepository packageRepository = new PackageRepositoryImpl(unitOfWork, cardRepository);

        AuthenticationService authenticationService = new AuthenticationServiceImpl(sessionRepository, userRepository, validator, encoder);
        PackageService packageService = new PackageServiceImpl(authenticationService, packageRepository, cardRepository, userRepository, validator);
        CardService cardService = new CardServiceImpl(cardRepository, authenticationService);
        StatsService statsService = new StatsServiceImpl(authenticationService, userRepository);
        BattleService battleService = new BattleServiceImpl(authenticationService, userRepository);

        router.addService("/users", new UserController(new UserServiceImpl(authenticationService, userRepository, validator, encoder), objectMapper));
        router.addService("/sessions", new AuthenticationController(authenticationService, objectMapper));
        router.addService("/packages", new PackageController(packageService, objectMapper));
        router.addService("/transactions", new TransactionsController(packageService));
        router.addService("/cards", new CardController(cardService, objectMapper));
        router.addService("/deck", new DeckController(cardService, objectMapper));
        router.addService("/stats", new StatsController(statsService, objectMapper));
        router.addService("/scoreboard", new ScoreBoardController(statsService, objectMapper));
        router.addService("/battles", new BattleController(battleService));

        return router;
    }
}
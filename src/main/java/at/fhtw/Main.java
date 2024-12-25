package at.fhtw;

import at.fhtw.httpserver.server.Server;
import at.fhtw.httpserver.utils.Router;
import at.fhtw.mtcgapp.persistence.DatabaseManager;
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
        if (args.length == 1) {
            DatabaseManager.databaseUrl = args[0];
        }
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

        UserRepository userRepository = new UserRepositoryImpl(unitOfWork);
        SessionRepository sessionRepository = new SessionRepositoryImpl(unitOfWork);
        CardRepository cardRepository = new CardRepositoryImpl(unitOfWork);
        PackageRepository packageRepository = new PackageRepositoryImpl(unitOfWork, cardRepository);

        AuthenticationService authenticationService = new AuthenticationServiceImpl(sessionRepository, userRepository, validator, encoder);
        PackageService packageService = new PackageServiceImpl(authenticationService, packageRepository, cardRepository, userRepository, validator);

        router.addService("/users", new UserController(new UserServiceImpl(userRepository, validator, encoder), objectMapper));
        router.addService("/sessions", new AuthenticationController(authenticationService, objectMapper));
        router.addService("/packages", new PackageController(packageService, objectMapper));
        router.addService("/transactions", new TransactionsController(packageService));
        router.addService("/cards", new CardController(new CardServiceImpl(cardRepository, authenticationService), objectMapper));

        return router;
    }
}
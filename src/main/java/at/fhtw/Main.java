package at.fhtw;

import at.fhtw.httpserver.server.Server;
import at.fhtw.httpserver.utils.Router;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import at.fhtw.mtcgapp.persistence.repository.UserRepositoryImpl;
import at.fhtw.mtcgapp.presentation.UserController;
import at.fhtw.mtcgapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        ObjectMapper objectMapper = new ObjectMapper();
        UnitOfWork unitOfWork = new UnitOfWork();

        router.addService("/users", new UserController(new UserService(new UserRepositoryImpl(unitOfWork)), objectMapper));

        return router;
    }
}
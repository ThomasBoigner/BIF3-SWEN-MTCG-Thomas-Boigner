package at.fhtw;

import at.fhtw.httpserver.server.Server;
import at.fhtw.httpserver.utils.Router;
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

        return router;
    }
}
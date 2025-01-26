package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.persistence.repository.SessionRepositorySingleton;
import at.fhtw.mtcgapp.persistence.repository.UserRepositorySingleton;
import at.fhtw.mtcgapp.util.Base64EncoderSingleton;
import at.fhtw.mtcgapp.util.ValidatorSingleton;
import lombok.Getter;

@Getter
public enum AuthenticationServiceSingleton {
    INSTANCE(new AuthenticationServiceImpl(
            SessionRepositorySingleton.INSTANCE.getSessionRepository(),
            UserRepositorySingleton.INSTANCE.getUserRepository(),
            ValidatorSingleton.INSTANCE.getValidator(),
            Base64EncoderSingleton.INSTANCE.getEncoder()));

    private final AuthenticationService authenticationService;

    AuthenticationServiceSingleton(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}

package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.persistence.repository.SessionRepositorySingleton;
import at.fhtw.mtcgapp.persistence.repository.UserRepositorySingleton;
import at.fhtw.mtcgapp.util.Base64EncoderSingleton;
import at.fhtw.mtcgapp.util.SecureRandomSingleton;
import at.fhtw.mtcgapp.util.ValidatorSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthenticationServiceSingleton {
    INSTANCE(new AuthenticationServiceImpl(
            SessionRepositorySingleton.INSTANCE.getSessionRepository(),
            UserRepositorySingleton.INSTANCE.getUserRepository(),
            ValidatorSingleton.INSTANCE.getValidator(),
            SecureRandomSingleton.INSTANCE.getSecureRandom(),
            Base64EncoderSingleton.INSTANCE.getEncoder()));

    private final AuthenticationService authenticationService;
}

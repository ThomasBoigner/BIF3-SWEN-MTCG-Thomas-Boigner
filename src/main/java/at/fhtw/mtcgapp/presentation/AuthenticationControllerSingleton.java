package at.fhtw.mtcgapp.presentation;

import at.fhtw.mtcgapp.service.AuthenticationServiceSingleton;
import at.fhtw.mtcgapp.util.ObjectMapperSingleton;
import lombok.Getter;

@Getter
public enum AuthenticationControllerSingleton {
    INSTANCE(new AuthenticationController(
            AuthenticationServiceSingleton.INSTANCE.getAuthenticationService(),
            ObjectMapperSingleton.INSTANCE.getObjectMapper()));

    private final AuthenticationController authenticationController;

    AuthenticationControllerSingleton(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }
}

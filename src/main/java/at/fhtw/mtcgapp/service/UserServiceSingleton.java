package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.persistence.repository.UserRepositorySingleton;
import at.fhtw.mtcgapp.util.Base64EncoderSingleton;
import at.fhtw.mtcgapp.util.ValidatorSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserServiceSingleton {
    INSTANCE(new UserServiceImpl(
            AuthenticationServiceSingleton.INSTANCE.getAuthenticationService(),
            UserRepositorySingleton.INSTANCE.getUserRepository(),
            ValidatorSingleton.INSTANCE.getValidator(),
            Base64EncoderSingleton.INSTANCE.getEncoder()));

    private final UserService userService;
}

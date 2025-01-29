package at.fhtw.mtcgapp.presentation;

import at.fhtw.mtcgapp.service.UserServiceSingleton;
import at.fhtw.mtcgapp.util.ObjectMapperSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserControllerSingleton {
    INSTANCE(new UserController(
            UserServiceSingleton.INSTANCE.getUserService(),
            ObjectMapperSingleton.INSTANCE.getObjectMapper()));

    private final UserController userController;
}

package at.fhtw.mtcgapp.presentation;

import at.fhtw.mtcgapp.service.UserServiceSingleton;
import at.fhtw.mtcgapp.util.ObjectMapperSingleton;
import lombok.Getter;

@Getter
public enum UserControllerSingleton {
    INSTANCE(new UserController(
            UserServiceSingleton.INSTANCE.getUserService(),
            ObjectMapperSingleton.INSTANCE.getObjectMapper()));

    private final UserController userController;

    UserControllerSingleton(UserController userController) {
        this.userController = userController;
    }
}

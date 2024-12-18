package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.service.command.LoginCommand;

public interface AuthenticationService {
    User getCurrentlyLoggedInUser(String token);
    String loginUser(LoginCommand command);
    void logoutUser(String token);
}

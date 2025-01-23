package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.service.command.CreateUserCommand;
import at.fhtw.mtcgapp.service.command.UpdateUserCommand;
import at.fhtw.mtcgapp.service.dto.UserDataDto;

public interface UserService {
    UserDataDto getUser(String authToken, String username);
    UserDataDto createUser(CreateUserCommand command);
    void updateUser(String authToken, String username, UpdateUserCommand command);
}

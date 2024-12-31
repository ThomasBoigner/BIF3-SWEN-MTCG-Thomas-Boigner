package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.service.command.CreateUserCommand;
import at.fhtw.mtcgapp.service.command.UpdateUserCommand;
import at.fhtw.mtcgapp.service.dto.UserDto;

public interface UserService {
    UserDto getUser(String authToken, String username);
    UserDto createUser(CreateUserCommand command);
    void updateUser(String authToken, String username, UpdateUserCommand command);
}

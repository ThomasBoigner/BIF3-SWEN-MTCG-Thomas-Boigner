package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.service.command.CreateUserCommand;
import at.fhtw.mtcgapp.service.dto.UserDto;

public interface UserService {
    UserDto createUser(CreateUserCommand command);
}

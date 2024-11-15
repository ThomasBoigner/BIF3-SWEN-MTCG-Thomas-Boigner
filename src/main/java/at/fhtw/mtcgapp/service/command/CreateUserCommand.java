package at.fhtw.mtcgapp.service.command;

import lombok.Builder;

@Builder
public record CreateUserCommand(String username, String password) { }

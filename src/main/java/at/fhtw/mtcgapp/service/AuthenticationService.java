package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.persistence.repository.SessionRepository;
import at.fhtw.mtcgapp.service.command.LoginCommand;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor

@Slf4j
public class AuthenticationService {
    private final SessionRepository authenticationRepository;
    private final Validator validator;

    public String loginUser(LoginCommand command) {
        throw new UnsupportedOperationException();
    }
}

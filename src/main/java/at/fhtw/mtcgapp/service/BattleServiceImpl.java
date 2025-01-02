package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor

@Slf4j
public class BattleServiceImpl implements BattleService {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @Override
    public void battleUser(String authToken) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

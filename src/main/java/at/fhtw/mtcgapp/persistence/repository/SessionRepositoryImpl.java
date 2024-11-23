package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.User;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor

@Slf4j
public class SessionRepositoryImpl implements SessionRepository {
    private final UnitOfWork unitOfWork;

    @Override
    public void login(User user) {

    }

    @Override
    public void logout(String token) {

    }
}

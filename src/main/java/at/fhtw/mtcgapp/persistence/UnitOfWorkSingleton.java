package at.fhtw.mtcgapp.persistence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UnitOfWorkSingleton {
    INSTANCE(new UnitOfWork());

    private final UnitOfWork unitOfWork;
}

package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PackageRepositorySingleton {
    INSTANCE(new PackageRepositoryImpl(new UnitOfWork(), CardRepositorySingleton.INSTANCE.getCardRepository()));

    private final PackageRepository packageRepository;
}

package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.persistence.UnitOfWorkSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PackageRepositorySingleton {
    INSTANCE(new PackageRepositoryImpl(UnitOfWorkSingleton.INSTANCE.getUnitOfWork(), CardRepositorySingleton.INSTANCE.getCardRepository()));

    private final PackageRepository packageRepository;
}

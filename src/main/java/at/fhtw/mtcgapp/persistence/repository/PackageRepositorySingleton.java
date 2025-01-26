package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.Getter;

@Getter
public enum PackageRepositorySingleton {
    INSTANCE(new PackageRepositoryImpl(new UnitOfWork(), CardRepositorySingleton.INSTANCE.getCardRepository()));

    private final PackageRepository packageRepository;

    PackageRepositorySingleton(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }
}

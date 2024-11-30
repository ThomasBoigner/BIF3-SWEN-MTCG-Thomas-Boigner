package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Package;
import at.fhtw.mtcgapp.persistence.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor

@Slf4j
public class PackageRepositoryImpl implements PackageRepository {
    private final UnitOfWork unitOfWork;

    @Override
    public Package save(Package pkg) {
        return null;
    }
}

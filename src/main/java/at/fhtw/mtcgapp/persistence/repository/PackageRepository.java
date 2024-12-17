package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Package;

import java.util.Optional;

public interface PackageRepository {
    Package save(Package pkg);
    Optional<Package> getPackage();
    void deletePackage(long id);
}

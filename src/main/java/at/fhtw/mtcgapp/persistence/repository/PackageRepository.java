package at.fhtw.mtcgapp.persistence.repository;

import at.fhtw.mtcgapp.model.Package;

public interface PackageRepository {
    Package save(Package pkg);
}

package at.fhtw.mtcgapp.service;

import at.fhtw.mtcgapp.persistence.repository.CardRepositorySingleton;
import at.fhtw.mtcgapp.persistence.repository.PackageRepositorySingleton;
import at.fhtw.mtcgapp.persistence.repository.UserRepositorySingleton;
import at.fhtw.mtcgapp.util.ValidatorSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PackageServiceSingleton {
    INSTANCE(new PackageServiceImpl(
            AuthenticationServiceSingleton.INSTANCE.getAuthenticationService(),
            PackageRepositorySingleton.INSTANCE.getPackageRepository(),
            CardRepositorySingleton.INSTANCE.getCardRepository(),
            UserRepositorySingleton.INSTANCE.getUserRepository(),
            ValidatorSingleton.INSTANCE.getValidator()
    ));

    private final PackageService packageService;
}

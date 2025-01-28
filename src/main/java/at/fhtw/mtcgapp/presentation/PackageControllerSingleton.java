package at.fhtw.mtcgapp.presentation;

import at.fhtw.mtcgapp.service.PackageServiceSingleton;
import at.fhtw.mtcgapp.util.ObjectMapperSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PackageControllerSingleton {
    INSTANCE(new PackageController(
            PackageServiceSingleton.INSTANCE.getPackageService(),
            ObjectMapperSingleton.INSTANCE.getObjectMapper()));

    private final PackageController packageController;
}

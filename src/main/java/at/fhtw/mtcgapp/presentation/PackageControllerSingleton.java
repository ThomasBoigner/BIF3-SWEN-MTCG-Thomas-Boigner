package at.fhtw.mtcgapp.presentation;

import at.fhtw.mtcgapp.service.PackageServiceSingleton;
import at.fhtw.mtcgapp.util.ObjectMapperSingleton;
import lombok.Getter;

@Getter
public enum PackageControllerSingleton {
    INSTANCE(new PackageController(
            PackageServiceSingleton.INSTANCE.getPackageService(),
            ObjectMapperSingleton.INSTANCE.getObjectMapper()));

    private final PackageController packageController;

    PackageControllerSingleton(PackageController packageController) {
        this.packageController = packageController;
    }
}

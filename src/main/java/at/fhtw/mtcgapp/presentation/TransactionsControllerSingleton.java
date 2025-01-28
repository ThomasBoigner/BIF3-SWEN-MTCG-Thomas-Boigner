package at.fhtw.mtcgapp.presentation;

import at.fhtw.mtcgapp.service.PackageServiceSingleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionsControllerSingleton {
    INSTANCE(new TransactionsController(PackageServiceSingleton.INSTANCE.getPackageService()));

    private final TransactionsController transactionsController;
}

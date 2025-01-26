package at.fhtw.mtcgapp.presentation;

import at.fhtw.mtcgapp.service.PackageServiceSingleton;
import lombok.Getter;

@Getter
public enum TransactionsControllerSingleton {
    INSTANCE(new TransactionsController(PackageServiceSingleton.INSTANCE.getPackageService()));

    private final TransactionsController transactionsController;

    TransactionsControllerSingleton(TransactionsController transactionsController) {
        this.transactionsController = transactionsController;
    }
}

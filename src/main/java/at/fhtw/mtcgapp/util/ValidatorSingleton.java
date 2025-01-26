package at.fhtw.mtcgapp.util;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.Getter;

@Getter
public enum ValidatorSingleton {
    INSTANCE(Validation.buildDefaultValidatorFactory().getValidator());

    private final Validator validator;

    ValidatorSingleton(Validator validator) {
        this.validator = validator;
    }
}

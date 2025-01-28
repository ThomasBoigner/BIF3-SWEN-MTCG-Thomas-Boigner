package at.fhtw.mtcgapp.util;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ValidatorSingleton {
    INSTANCE(Validation.buildDefaultValidatorFactory().getValidator());

    private final Validator validator;
}

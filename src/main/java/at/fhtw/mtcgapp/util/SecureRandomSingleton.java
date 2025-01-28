package at.fhtw.mtcgapp.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;

@Getter
@RequiredArgsConstructor
public enum SecureRandomSingleton {
    INSTANCE(new SecureRandom());

    private final SecureRandom secureRandom;
}

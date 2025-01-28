package at.fhtw.mtcgapp.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Base64;

@Getter
@RequiredArgsConstructor
public enum Base64EncoderSingleton {
    INSTANCE(Base64.getEncoder());

    private final Base64.Encoder encoder;
}

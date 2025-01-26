package at.fhtw.mtcgapp.util;

import lombok.Getter;

import java.util.Base64;

@Getter
public enum Base64EncoderSingleton {
    INSTANCE(Base64.getEncoder());

    private final Base64.Encoder encoder;

    Base64EncoderSingleton(Base64.Encoder encoder) {
        this.encoder = encoder;
    }
}

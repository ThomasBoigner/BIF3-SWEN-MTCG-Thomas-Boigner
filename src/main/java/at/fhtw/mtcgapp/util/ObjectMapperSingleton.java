package at.fhtw.mtcgapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ObjectMapperSingleton {
    INSTANCE(new ObjectMapper());

    private final ObjectMapper objectMapper;
}
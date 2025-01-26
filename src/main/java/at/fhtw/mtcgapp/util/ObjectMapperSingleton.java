package at.fhtw.mtcgapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public enum ObjectMapperSingleton {
    INSTANCE(new ObjectMapper());

    private final ObjectMapper objectMapper;

    ObjectMapperSingleton(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}

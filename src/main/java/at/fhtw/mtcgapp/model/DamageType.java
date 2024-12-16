package at.fhtw.mtcgapp.model;

import lombok.Getter;

@Getter
public enum DamageType {
    FIRE("fire"),
    WATER("water"),
    NORMAL("normal");

    private final String dbValue;

    DamageType(String dbValue) {
        this.dbValue = dbValue;
    }
}

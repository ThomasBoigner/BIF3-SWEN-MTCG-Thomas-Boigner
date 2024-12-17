package at.fhtw.mtcgapp.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DamageType {
    FIRE("fire"),
    WATER("water"),
    NORMAL("normal");

    private final String dbValue;

    DamageType(String dbValue) {
        this.dbValue = dbValue;
    }

    public static DamageType forDBValue(String dbValue) {
        return Arrays.stream(values())
                .filter(damageType -> damageType.getDbValue().equalsIgnoreCase(dbValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown shortcut for DamageType: %s", dbValue)));
    }
}

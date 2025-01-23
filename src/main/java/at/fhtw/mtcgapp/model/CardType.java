package at.fhtw.mtcgapp.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CardType {
    MONSTER("monster"),
    SPELL("spell");

    private final String dbValue;

    CardType(String dbValue) {
        this.dbValue = dbValue;
    }

    public static CardType forDBValue(String dbValue) {
        return Arrays.stream(values())
                .filter(cardType -> cardType.getDbValue().equalsIgnoreCase(dbValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown card type: %s", dbValue)));
    }
}

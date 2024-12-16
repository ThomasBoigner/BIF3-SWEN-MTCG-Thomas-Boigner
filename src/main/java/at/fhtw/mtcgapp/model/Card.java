package at.fhtw.mtcgapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Card {
    private long id;
    private UUID token;
    private String name;
    private double damage;

    private User user;
    private DamageType damageType;
    private Trade trade;
    private Package cardPackage;
}

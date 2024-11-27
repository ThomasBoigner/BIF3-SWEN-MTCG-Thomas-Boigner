package at.fhtw.mtcgapp.model;

import lombok.Data;

import java.util.UUID;

@Data
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

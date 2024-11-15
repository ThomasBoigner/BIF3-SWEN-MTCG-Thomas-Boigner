package at.fhtw.mtcgapp.model;

import java.util.UUID;

public abstract class Card {
    private UUID uuid;
    private String name;
    private double damage;

    private User user;
    private DamageType damageType;
    private Trade trade;
    private Package cardPackage;
}

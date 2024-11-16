package at.fhtw.mtcgapp.model;

import java.util.UUID;

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

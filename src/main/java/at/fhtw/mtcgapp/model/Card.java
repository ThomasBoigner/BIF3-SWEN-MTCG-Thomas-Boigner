package at.fhtw.mtcgapp.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
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

    @Override
    public String toString() {
        return "Card{" +
               "damageType=" + damageType +
               ", damage=" + damage +
               ", name='" + name + '\'' +
               ", token=" + token +
               ", id=" + id +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id && Double.compare(damage, card.damage) == 0 && Objects.equals(token, card.token) && Objects.equals(name, card.name) && damageType == card.damageType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, name, damage, damageType);
    }
}

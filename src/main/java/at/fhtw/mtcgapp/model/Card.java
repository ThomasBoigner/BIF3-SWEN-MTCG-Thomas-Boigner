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
    protected long id;
    protected UUID token;
    protected String name;
    protected double damage;

    protected User user;
    protected DamageType damageType;
    protected Trade trade;
    protected Package cardPackage;

    public double calculateDamage(Card cardOfOtherPlayer) {
        if (cardOfOtherPlayer instanceof MonsterCard) {
            return calculateDamage((MonsterCard) cardOfOtherPlayer);
        }
        if (cardOfOtherPlayer instanceof SpellCard) {
            return calculateDamage((SpellCard) cardOfOtherPlayer);
        }
        return damage;
    }

    protected abstract double calculateDamage(MonsterCard otherCard);
    protected abstract double calculateDamage(SpellCard otherCard);

    protected boolean hasElementAdvantage(Card otherCard) {
        return (this.getDamageType() == DamageType.WATER && otherCard.getDamageType() == DamageType.FIRE) ||
            (this.getDamageType() == DamageType.FIRE && otherCard.getDamageType() == DamageType.NORMAL) ||
            (this.getDamageType() == DamageType.NORMAL && otherCard.getDamageType() == DamageType.WATER);
    }

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

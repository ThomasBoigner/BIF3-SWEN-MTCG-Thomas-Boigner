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

    public double calculateDamage(Card otherCard) {
        if (hasWeakness(otherCard)) {
            return 0;
        }
        return switch (otherCard) {
            case MonsterCard otherMonsterCard -> calculateDamageMonsterCard(otherMonsterCard);
            case SpellCard otherSpellCard -> calculateDamageSpellCard(otherSpellCard);
            default -> damage;
        };
    }

    protected abstract double calculateDamageMonsterCard(MonsterCard otherCard);
    protected abstract double calculateDamageSpellCard(SpellCard otherCard);

    public boolean hasWeakness(Card otherCard) {
        return (this.name.contains("Goblin") && otherCard.getName().contains("Dragon")) ||
               (this.name.contains("Ork") && otherCard.getName().contains("Wizard")) ||
               (this.name.contains("Knight") && otherCard.getName().equals("WaterSpell")) ||
               (this.name.contains("Spell") && otherCard.getName().contains("Kraken")) ||
               (this.name.equals("FireElves") && otherCard.getName().contains("Dragon"));
    }

    public boolean hasElementAdvantage(Card otherCard) {
        return (this.damageType == DamageType.WATER && otherCard.getDamageType() == DamageType.FIRE) ||
            (this.damageType == DamageType.FIRE && otherCard.getDamageType() == DamageType.NORMAL) ||
            (this.damageType == DamageType.NORMAL && otherCard.getDamageType() == DamageType.WATER);
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

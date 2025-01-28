package at.fhtw.mtcgapp.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SpellCard extends Card {
    private double criticalHitMultiplier;

    @Override
    public double calculateDamage(MonsterCard otherCard) {
        double calculatedDamage = (this.damage - otherCard.getDefence()) * this.criticalHitMultiplier;
        if (hasElementAdvantage(otherCard)) {
            calculatedDamage *= 2;
        }
        return calculatedDamage;
    }

    @Override
    public double calculateDamage(SpellCard otherCard) {
        double calculatedDamage = this.damage * criticalHitMultiplier;
        if (hasElementAdvantage(otherCard)) {
            calculatedDamage *= 2;
        }
        return calculatedDamage;
    }

    @Override
    public String toString() {
        return "SpellCard{" +
               "criticalHitChance=" + criticalHitMultiplier +
               "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SpellCard spellCard = (SpellCard) o;
        return Double.compare(criticalHitMultiplier, spellCard.criticalHitMultiplier) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), criticalHitMultiplier);
    }
}

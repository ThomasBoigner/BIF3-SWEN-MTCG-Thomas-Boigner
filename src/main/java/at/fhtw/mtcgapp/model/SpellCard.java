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
    private double criticalHitChance;

    @Override
    public String toString() {
        return "SpellCard{" +
               "criticalHitChance=" + criticalHitChance +
               "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SpellCard spellCard = (SpellCard) o;
        return Double.compare(criticalHitChance, spellCard.criticalHitChance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), criticalHitChance);
    }
}

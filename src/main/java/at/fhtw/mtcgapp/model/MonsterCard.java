package at.fhtw.mtcgapp.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MonsterCard extends Card {
    private double defence;

    @Override
    protected double calculateDamageMonsterCard(MonsterCard otherCard) {
        return this.damage - otherCard.defence;
    }

    @Override
    protected double calculateDamageSpellCard(SpellCard otherCard) {
        if (hasElementAdvantage(otherCard)) {
            return this.damage * 2;
        }
        return this.damage;
    }

    @Override
    public String toString() {
        return "MonsterCard{" +
               "defence=" + defence +
               "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MonsterCard that = (MonsterCard) o;
        return Double.compare(defence, that.defence) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), defence);
    }
}
